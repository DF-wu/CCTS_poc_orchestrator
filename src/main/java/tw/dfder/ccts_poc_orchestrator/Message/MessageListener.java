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

    @Autowired
    public MessageListener(Gson gson, CCTSMessageSender sender) {
        this.gson = gson;
        this.sender = sender;
    }


    @RabbitListener(queues = {
            RabbitmqConfig.QUEUE_PAYMENT_RESPONSE
    })
    public void receivedMessageFromPayment(String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel ch) throws IOException {
//        decode the message
        PaymentMessageEnvelope paymentMessageEnvelope = gson.fromJson(msg, PaymentMessageEnvelope.class);
        ch.basicAck(deliveryTag,false);
        System.out.println("receive msg from payment service" + paymentMessageEnvelope);
        if (paymentMessageEnvelope.isValid()){
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
                    ServiceConfig.serviceName
            );

        }else {
            System.out.println("Fail!!" + paymentMessageEnvelope );
        }

    }



    @RabbitListener(queues = {
            RabbitmqConfig.QUEUE_UPDATEPOINT_RESPONSE
    })
    public void receivedMessageFromPointService(String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel ch) throws IOException {
        UpdatePointsEnvelope updatePointsEnvelopeRequest = gson.fromJson(msg, UpdatePointsEnvelope.class);
        ch.basicAck(deliveryTag, false);
        System.out.println("receive msg from Point service" + updatePointsEnvelopeRequest);

        LogMessageEnvelope logMessageEnvelope = new LogMessageEnvelope();
        if(updatePointsEnvelopeRequest.isValid())
        {
            logMessageEnvelope.setPaymentId(updatePointsEnvelopeRequest.getPaymentId());
            logMessageEnvelope.setBuyerId(updatePointsEnvelopeRequest.getBuyerId());
            logMessageEnvelope.setPoints(updatePointsEnvelopeRequest.getPoints());
            sender.sendMessage(
                    gson.toJson(logMessageEnvelope),
                    "loggingService",
                    RabbitmqConfig.ROUTING_LOGGING_REQUEST,
                    ServiceConfig.serviceName
            );
            System.out.println("msg sent " + logMessageEnvelope);
        }else{
            System.out.println("Fail!! " + logMessageEnvelope);
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
