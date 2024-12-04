package vehiclemgmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierObjectMapper;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;
import vehiclemgmt.domain.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMessageVerifier
public class RegisterPerformanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        RegisterPerformanceTest.class
    );

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MessageVerifier<Message<?>> messageVerifier;

    @Autowired
    public VehiclePerformanceRepository repository;

    @Test
    @SuppressWarnings("unchecked")
    public void test0() {
        //given:
        VehiclePerformance entity = new VehiclePerformance();

        entity.setRegistrationId("ABC123");
        entity.setVehicleNumber("12345");
        entity.setRegistrationDate("2022-01-01");
        entity.setDeparture("City A");
        entity.setDepartureTime("08:00");
        entity.setAccumulatedDistanceBefore(100L);
        entity.setDestination("City B");
        entity.setArrivalTime("12:00");
        entity.setAccumulatedDistanceAfter(200L);
        entity.setDrivingDistance(100.5D);
        entity.setPurpose("Personal");
        entity.setPeriod("Morning");

        repository.save(entity);

        //when:
        try {
            VehiclePerformance entity = new VehiclePerformance();

            entity.setRegistrationId("ABC123");
            entity.setVehicleNumber("12345");
            entity.setRegistrationDate("2022-01-01");
            entity.setDeparture("City A");
            entity.setDepartureTime("08:00");
            entity.setAccumulatedDistanceBefore(100L);
            entity.setDestination("City B");
            entity.setArrivalTime("12:00");
            entity.setAccumulatedDistanceAfter(200L);
            entity.setDrivingDistance(100.5D);
            entity.setPurpose("Personal");
            entity.setPeriod("Morning");

            repository.save(entity);

            //then:
            this.messageVerifier.send(
                    MessageBuilder
                        .withPayload(entity)
                        .setHeader(
                            MessageHeaders.CONTENT_TYPE,
                            MimeTypeUtils.APPLICATION_JSON
                        )
                        .build(),
                    "vehiclemgmt"
                );

            Message<?> receivedMessage =
                this.messageVerifier.receive(
                        "vehiclemgmt",
                        5000,
                        TimeUnit.MILLISECONDS
                    );
            assertNotNull("Resulted event must be published", receivedMessage);

            String receivedPayload = (String) receivedMessage.getPayload();

            PerformanceRegistered outputEvent = objectMapper.readValue(
                receivedPayload,
                PerformanceRegistered.class
            );

            LOGGER.info("Response received: {}", outputEvent);

            assertEquals(outputEvent.getRegistrationId(), "ABC123");
            assertEquals(outputEvent.getVehicleNumber(), "12345");
            assertEquals(outputEvent.getAccumulatedDistanceAfter(), 200L);
            assertEquals(outputEvent.getDrivingDistance(), 100.5D);
            assertEquals(outputEvent.getPurpose(), "Personal");
            assertEquals(outputEvent.getPeriod(), "Morning");
            assertEquals(outputEvent.getRegistrationDate(), "2022-01-01");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }
}
