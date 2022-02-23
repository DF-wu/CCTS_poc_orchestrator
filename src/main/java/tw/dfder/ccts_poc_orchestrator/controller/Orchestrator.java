package tw.dfder.ccts_poc_orchestrator.controller;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.dfder.ccts_poc_orchestrator.Entity.PaymentMessageEnvelope;
import tw.dfder.ccts_poc_orchestrator.Message.CCTSMessageSender;
import tw.dfder.ccts_poc_orchestrator.configuration.RabbitmqConfig;
import tw.dfder.ccts_poc_orchestrator.configuration.ServiceConfig;

import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class Orchestrator {
    private final RabbitTemplate rabbitTemplate;
    private final CCTSMessageSender sender;
    private final Gson gson;
    @Autowired
    public Orchestrator(RabbitTemplate rabbitTemplate, CCTSMessageSender sender, Gson gson){
        this.rabbitTemplate = rabbitTemplate;
        this.sender = sender;
        this.gson = gson;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startANewSaga(@RequestBody Map<String, String> body) {
        PaymentMessageEnvelope envelope = new PaymentMessageEnvelope();
        envelope.setMethod("pay");
        envelope.setPaymentId(UUID.randomUUID().toString());
        envelope.setBuyerId(UUID.randomUUID().toString());
        envelope.setValid(Boolean.parseBoolean(body.get("isValid")));
        envelope.setTotalAmount(Integer.parseInt(body.get("price")));

        sender.sendMessage(
                gson.toJson(envelope),
                "paymentService",
                RabbitmqConfig.ROUTING_PAYMENT_REQUEST,
                ServiceConfig.serviceName
        );
        return new ResponseEntity<>(envelope,HttpStatus.OK);

    }


    //TODO
    @GetMapping("/find/{pid}")
    public ResponseEntity<?> queryCertainPaymentMessage(@PathVariable String pid){
        PaymentMessageEnvelope paymentMessageEnvelope = new PaymentMessageEnvelope();
        paymentMessageEnvelope.setPaymentId(pid);

        sender.sendMessage(
                gson.toJson(paymentMessageEnvelope),
                ServiceConfig.destinations.get(0),
                RabbitmqConfig.ROUTING_PAYMENT_REQUEST,
                ServiceConfig.serviceName
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
