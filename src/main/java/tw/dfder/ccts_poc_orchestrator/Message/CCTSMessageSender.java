package tw.dfder.ccts_poc_orchestrator.Message;


import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import tw.dfder.ccts_poc_orchestrator.Entity.MessageEnvelope;
import tw.dfder.ccts_poc_orchestrator.configuration.RabbitmqConfig;
import tw.dfder.ccts_poc_orchestrator.configuration.ServiceConfig;

@EnableRabbit
public class CCTSMessageSender {
    private final RabbitTemplate rabbitTemplate;
    private final Gson gson;

    @Autowired
    public CCTSMessageSender(RabbitTemplate rabbitTemplate, Gson gson) {
        this.rabbitTemplate = rabbitTemplate;
        this.gson = gson;
    }

    public boolean sendRequestMessage(MessageEnvelope message, String destination, String routingKey, String pactName){
//        routingKey : routing key defined in RabbitmqConfig.java
//        destination is the corresponding service name
//        pactName is what the contract of the message belonging for
        try {
            rabbitTemplate.convertAndSend(
                    RabbitmqConfig.EXCHANG_ORCHESTRATOR,
                    routingKey,
                    message,
                    m -> {
                        m.getMessageProperties().getHeaders().put("source", ServiceConfig.serviceName);
                        m.getMessageProperties().getHeaders().put("destination", destination );
                        m.getMessageProperties().getHeaders().put("pact", pactName);
                        return m;
                    }
            );
            return true;
        } catch (Exception e){
            System.out.println("ERROR : sendRequestMessage. " + e);
            e.printStackTrace();
            return false;
        }
    }

}
