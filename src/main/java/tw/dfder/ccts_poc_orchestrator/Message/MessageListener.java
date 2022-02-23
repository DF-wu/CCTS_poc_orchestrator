package tw.dfder.ccts_poc_orchestrator.Message;


import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import tw.dfder.ccts_poc_orchestrator.Entity.PaymentMessageEnvelope;
import tw.dfder.ccts_poc_orchestrator.configuration.RabbitmqConfig;

@EnableRabbit
@Service("MessageListener")
public class MessageListener {
    private final Gson gson;

    @Autowired
    public MessageListener(Gson gson) {
        this.gson = gson;
    }


    @RabbitListener(queues = {
            RabbitmqConfig.QUEUE_PAYMENT_RESPONSE
    })
    public void receivedMessageFromPayment(String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel ch){
//        decode the message
        PaymentMessageEnvelope messageEnvelope = gson.fromJson(msg, PaymentMessageEnvelope.class);

        if (messageEnvelope.isValid()){
//            next step
        }


    }
}
