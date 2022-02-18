package tw.dfder.ccts_poc_orchestrator;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.dfder.ccts_poc_orchestrator.configuration.RabbitmqConfig;
import tw.dfder.ccts_poc_orchestrator.configuration.ServiceConfig;

@Service
public class CCTSMessageSender {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CCTSMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public boolean sendRequestMessage(String message, String destination){
        try {
            rabbitTemplate.convertAndSend(
                    RabbitmqConfig.EXCHANG_ORCHESTRATOR,
                    destination,
                    message,
                    m -> {
                        m.getMessageProperties().getHeaders().put("source", ServiceConfig.serviceName);
                        m.getMessageProperties().getHeaders().put("destination", ServiceConfig.correspondingPact);
                        m.getMessageProperties().getHeaders().put("pact", "pact of " + destination);
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
