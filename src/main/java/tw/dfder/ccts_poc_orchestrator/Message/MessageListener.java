package tw.dfder.ccts_poc_orchestrator.Message;


import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import tw.dfder.ccts_poc_orchestrator.Entity.LogMessageEnvelope;
import tw.dfder.ccts_poc_orchestrator.Entity.PaymentMessageEnvelope;
import tw.dfder.ccts_poc_orchestrator.Entity.UpdatePointsEnvelope;
import tw.dfder.ccts_poc_orchestrator.configuration.RabbitmqConfig;
import tw.dfder.ccts_poc_orchestrator.configuration.ServiceConfig;

import java.io.IOException;

@EnableRabbit
@Service("MessageListener")
public class MessageListener {
    private final Gson gson;
    private final CCTSMessageSender sender;
    private final ServiceConfig serviceConfig;
    @Autowired
    public MessageListener(Gson gson, CCTSMessageSender sender, ServiceConfig serviceConfig) {
        this.gson = gson;
        this.sender = sender;
        this.serviceConfig = serviceConfig;
    }


    @RabbitListener(queues = {
            RabbitmqConfig.QUEUE_PAYMENT_RESPONSE
    })
    public void receivedMessageFromPayment(String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel ch) throws IOException {
//        decode the message
        PaymentMessageEnvelope paymentMessageEnvelope = gson.fromJson(msg, PaymentMessageEnvelope.class);
        ch.basicAck(deliveryTag,false);
        System.out.println("receive msg from payment service" + paymentMessageEnvelope);

        if(paymentMessageEnvelope.getMethod().equals("rollback")) {
            System.out.println("rollback success. Pid: " + paymentMessageEnvelope.getPaymentId());
        } else if (paymentMessageEnvelope.isValid()){
            System.out.println("Success!! " + paymentMessageEnvelope);
            UpdatePointsEnvelope updatePointsEnvelope = new UpdatePointsEnvelope();
            updatePointsEnvelope.setPaymentId(paymentMessageEnvelope.getPaymentId());
            updatePointsEnvelope.setBuyerId(paymentMessageEnvelope.getBuyerId());
            updatePointsEnvelope.setPoints(paymentMessageEnvelope.getTotalAmount());
            updatePointsEnvelope.setValid(true);
            updatePointsEnvelope.setCommunicationType("request");

            sender.sendMessage(
                    gson.toJson(updatePointsEnvelope),
                    "pointService",
                    RabbitmqConfig.ROUTING_UPDATEPOINT_REQUEST,
                    "t-orc-point-01"
            );

        }else {
            System.out.println("Fail!!" + paymentMessageEnvelope );
        }

    }



    @RabbitListener(queues = {
            RabbitmqConfig.QUEUE_UPDATEPOINT_RESPONSE
    })
    public void receivedMessageFromPointService(String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel ch) throws IOException {
        UpdatePointsEnvelope updatePointsEnvelope = gson.fromJson(msg, UpdatePointsEnvelope.class);
        ch.basicAck(deliveryTag, false);
        System.out.println("receive msg from Point service" + updatePointsEnvelope);

        if(updatePointsEnvelope.getCommunicationType().equals("success")){

            LogMessageEnvelope logMessageEnvelope = new LogMessageEnvelope();
            if(updatePointsEnvelope.isValid())
            {
                logMessageEnvelope.setPaymentId(updatePointsEnvelope.getPaymentId());
                logMessageEnvelope.setBuyerId(updatePointsEnvelope.getBuyerId());
                logMessageEnvelope.setPoints(updatePointsEnvelope.getPoints());
                sender.sendMessage(
                        gson.toJson(logMessageEnvelope),
                        "loggingService",
                        RabbitmqConfig.ROUTING_LOGGING_REQUEST,
                       "t-orc-logging-01"
                );
                System.out.println("msg sent " + logMessageEnvelope);
            }else{
                System.out.println("Fail!! " + logMessageEnvelope);
            }
        }else if(updatePointsEnvelope.getCommunicationType().equals("rollback")){
            PaymentMessageEnvelope req = new PaymentMessageEnvelope();
            req.setMethod("rollback");
            req.setPaymentId(updatePointsEnvelope.getPaymentId());
            req.setBuyerId(updatePointsEnvelope.getBuyerId());
            req.setTotalAmount(updatePointsEnvelope.getPoints());
            req.setValid(true);
            sender.sendMessage(
                    gson.toJson(req),
                    "paymentService",
                    RabbitmqConfig.ROUTING_PAYMENT_REQUEST,
                    serviceConfig.name
            );
        }

    }


    @RabbitListener(queues = {
            RabbitmqConfig.QUEUE_LOGGGING_RESPONSE
    })
    public void receivedMessageFromLoggingService(String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel ch) throws IOException {
        LogMessageEnvelope logMessageEnvelope = gson.fromJson(msg, LogMessageEnvelope.class);
        ch.basicAck(deliveryTag, false);
        System.out.println("receive msg from Logging service" + logMessageEnvelope);


    }





}
