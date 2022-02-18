package tw.dfder.ccts_poc_orchestrator.controller;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@EnableRabbit
public class Orchestrator {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public Orchestrator(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }




}
