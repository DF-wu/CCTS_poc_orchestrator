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
import tw.dfder.ccts_poc_orchestrator.Entity.PaymentMessageEnvelope;

import java.util.HashMap;
import java.util.UUID;

@Provider("orchestrator")
@Consumer("paymentService")
@PactBroker(url = "http://23.dfder.tw:10141")
public class ToPaymentServiceTest {


    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
        context.verifyInteraction();
    }


    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
        System.setProperty("pact.verifier.publishResults", "true");
        System.setProperty("pact.provider.version", "v0.3");
    }


    @PactVerifyProvider("t-orc-payment-01")
    public MessageAndMetadata verifyMessageOfPayment() {

        Gson gson = new Gson();
        PaymentMessageEnvelope msg = new PaymentMessageEnvelope();
        msg.setPaymentId(UUID.randomUUID().toString());
        msg.setBuyerId(UUID.randomUUID().toString());
        msg.setTotalAmount((int) (Math.random()*1000));
        msg.setValid(true);
        msg.setMethod("request");

        HashMap<String, String> props = new HashMap<>();
        props.put("provider", "orchestrator");
        props.put("consumer","paymentService");
        return new MessageAndMetadata(gson.toJson(msg).getBytes(), props);
    }



    @PactVerifyProvider("t-orc-payment-02")
    public MessageAndMetadata verifyMessageOfPayment02() {

        Gson gson = new Gson();
        PaymentMessageEnvelope msg = new PaymentMessageEnvelope();
        msg.setPaymentId(UUID.randomUUID().toString());
        msg.setBuyerId(UUID.randomUUID().toString());
        msg.setTotalAmount((int) (Math.random()*1000));
        msg.setValid(true);
        msg.setMethod("request");

        HashMap<String, String> props = new HashMap<>();
        props.put("provider", "orchestrator");
        props.put("consumer","paymentService");
        return new MessageAndMetadata(gson.toJson(msg).getBytes(), props);
    }


    @PactVerifyProvider("t-orc-payment-03")
    public MessageAndMetadata verifyMessageOfPayment03() {

        Gson gson = new Gson();
        PaymentMessageEnvelope msg = new PaymentMessageEnvelope();
        msg.setPaymentId(UUID.randomUUID().toString());
        msg.setBuyerId(UUID.randomUUID().toString());
        msg.setTotalAmount((int) (Math.random()*1000));
        msg.setValid(true);
        msg.setMethod("request");

        HashMap<String, String> props = new HashMap<>();
        props.put("provider", "orchestrator");
        props.put("consumer","paymentService");
        return new MessageAndMetadata(gson.toJson(msg).getBytes(), props);
    }



}
