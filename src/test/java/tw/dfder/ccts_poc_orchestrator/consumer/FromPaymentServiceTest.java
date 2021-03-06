package tw.dfder.ccts_poc_orchestrator.consumer;


import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "paymentService", providerType = ProviderType.ASYNCH)
public class FromPaymentServiceTest {


    @Pact(consumer = "orchestrator")
    public MessagePact validateMessageFromPaymentService(MessagePactBuilder builder) {
        return builder
                .expectsToReceive("t-payment-orc-01")
                .withMetadata(m -> {
                    m.add("provider", "paymentService");
                    m.add("consumer", "orchestrator");
                })
                .toPact();
    }

    @Pact(consumer = "orchestrator")
    public MessagePact validateMessageFromPaymentService02(MessagePactBuilder builder) {
        return builder
                .expectsToReceive("t-payment-orc-02")
                .withMetadata(m -> {
                    m.add("provider", "paymentService");
                    m.add("consumer", "orchestrator");
                })
                .toPact();
    }

    @Pact(consumer = "orchestrator")
    public MessagePact validateMessageFromPaymentService03(MessagePactBuilder builder) {
        return builder
                .expectsToReceive("t-payment-orc-03")
                .withMetadata(m -> {
                    m.add("provider", "paymentService");
                    m.add("consumer", "orchestrator");
                })
                .toPact();
    }



    @Test
    @PactTestFor(pactMethod = "validateMessageFromPaymentService")
    public void validateMessageFromPaymentServiceTest(List<Message> messages) {
        // ???????????????????????????
        assertThat(messages).isNotEmpty();
        // ???header
        messages.forEach(m -> {
            assertThat(m.getMetadata()).hasFieldOrProperty("provider");
            assertThat(m.getMetadata()).hasFieldOrProperty("consumer");
        });
    }


    @Test
    @PactTestFor(pactMethod = "validateMessageFromPaymentService02")
    public void validateMessageFromPaymentServiceTest02(List<Message> messages) {
        // ???????????????????????????
        assertThat(messages).isNotEmpty();
        // ???header
        messages.forEach(m -> {
            assertThat(m.getMetadata()).hasFieldOrProperty("provider");
            assertThat(m.getMetadata()).hasFieldOrProperty("consumer");
        });
    }


    @Test
    @PactTestFor(pactMethod = "validateMessageFromPaymentService03")
    public void validateMessageFromPaymentServiceTest03(List<Message> messages) {
        // ???????????????????????????
        assertThat(messages).isNotEmpty();
        // ???header
        messages.forEach(m -> {
            assertThat(m.getMetadata()).hasFieldOrProperty("provider");
            assertThat(m.getMetadata()).hasFieldOrProperty("consumer");
        });
    }

}
