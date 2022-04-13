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
@PactTestFor(providerName = "pointService", providerType = ProviderType.ASYNCH)
public class RecievedFromPointServiceTest {


    @Pact(consumer = "orchestrator")
    public MessagePact validateReceivedFromPointServiceMessageBuilder(MessagePactBuilder builder){
        return builder
                .expectsToReceive("t-point-orc-01")
                .withMetadata( m -> {
                    m.add("provider", "pointService");
                    m.add("consumer", "orchestrator");
                })
                .toPact();

    }

    @Test
    @PactTestFor(pactMethod = "validateReceivedFromPointServiceMessageBuilder")
    public void validateReceivedFromPointServiceMessageBuilderTest(List<Message> messages){

        // 起碼有上面的案例吧
        assertThat(messages).isNotEmpty();
        // 驗header
        messages.forEach(m -> {
            assertThat(m.getMetadata()).hasFieldOrProperty("provider");
            assertThat(m.getMetadata()).hasFieldOrProperty("consumer");
        });

    }


}
