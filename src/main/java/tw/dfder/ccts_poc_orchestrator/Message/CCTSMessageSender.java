package tw.dfder.ccts_poc_orchestrator.Message;


import com.google.gson.Gson;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.dfder.ccts_poc_orchestrator.configuration.RabbitmqConfig;
import tw.dfder.ccts_poc_orchestrator.configuration.ServiceConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@EnableRabbit
@Service("CCTSMessageSender")
public class CCTSMessageSender {
    private final RabbitTemplate rabbitTemplate;
    private final ServiceConfig serviceConfig;
    @Autowired
    public CCTSMessageSender(RabbitTemplate rabbitTemplate, Gson gson, ServiceConfig serviceConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.serviceConfig = serviceConfig;
    }

    public boolean sendMessage(String message, String destination, String routingKey, String testCaseId){
//        routingKey : routing key defined in RabbitmqConfig.java
//        destination is the corresponding service name
//        pactName is what the contract of the message belonging for


        try {
            rabbitTemplate.convertAndSend(
                    RabbitmqConfig.EXCHANG_ORCHESTRATOR,
                    routingKey,
                    message,
                    m -> {
                        m.getMessageProperties().getHeaders().put("provider", serviceConfig.serviceName);
                        m.getMessageProperties().getHeaders().put("consumer", destination );
                        m.getMessageProperties().getHeaders().put("testCaseId", testCaseId);
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
