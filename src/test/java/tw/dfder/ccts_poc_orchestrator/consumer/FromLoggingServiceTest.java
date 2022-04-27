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
@PactTestFor(providerName = "loggingService", providerType = ProviderType.ASYNCH)
public class FromLoggingServiceTest {


    @Pact(consumer = "orchestrator")
    public MessagePact validateMessageFromLoggingService(MessagePactBuilder builder) {
        return builder
                .expectsToReceive("t-logging-orc-01")
                .withMetadata(m -> {
                    m.add("provider", "loggingService");
                    m.add("consumer", "orchestrator");
                })
                .toPact();

    }



    @Test
    @PactTestFor(pactMethod = "validateMessageFromLoggingService")
    public void validateMessageFromLoggingServiceTest(List<Message> messages) {

        // 起碼有上面的案例吧
        assertThat(messages).isNotEmpty();
        // 驗header
        messages.forEach(m -> {
            assertThat(m.getMetadata()).hasFieldOrProperty("provider");
            assertThat(m.getMetadata()).hasFieldOrProperty("consumer");
        });

    }
}
