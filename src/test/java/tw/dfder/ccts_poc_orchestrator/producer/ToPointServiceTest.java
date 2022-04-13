package tw.dfder.ccts_poc_orchestrator.producer;


import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import tw.dfder.ccts_poc_orchestrator.Entity.UpdatePointsEnvelope;
import tw.dfder.ccts_poc_orchestrator.Message.CCTSMessageSender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Provider("orchestrator")
@Consumer("pointService")
@PactBroker(url = "http://23.dfder.tw:10141")
//@PactFolder("target/pacts")
public class ToPointServiceTest {

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
        context.verifyInteraction();
    }


    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
        System.setProperty("pact.verifier.publishResults", "true");
        System.setProperty("pact.provider.version", "v0.2");
    }


    @PactVerifyProvider("t-orc-point-01")
    public MessageAndMetadata verifyMessageForOrder() {

        Gson gson = new Gson();
        UpdatePointsEnvelope updatePointsEnvelope = new UpdatePointsEnvelope();
        updatePointsEnvelope.setPaymentId(UUID.randomUUID().toString());
        updatePointsEnvelope.setBuyerId(UUID.randomUUID().toString());
        updatePointsEnvelope.setPoints((int) (Math.random()*1000));
        updatePointsEnvelope.setValid(true);
        updatePointsEnvelope.setCommunicationType("request");

        HashMap<String, String> props = new HashMap<>();
        props.put("provider", "orchestrator");
        props.put("consumer","pointService");
        return new MessageAndMetadata(gson.toJson(updatePointsEnvelope).getBytes(), props);
    }


}
