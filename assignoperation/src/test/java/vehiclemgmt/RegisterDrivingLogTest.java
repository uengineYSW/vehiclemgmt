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
public class RegisterDrivingLogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        RegisterDrivingLogTest.class
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

        entity.setRegistrationId("exampleRegistrationId");
        entity.setVehicleNumber("exampleVehicleNumber");
        entity.setRegistrationDate("2022-01-01");
        entity.setDeparture("exampleDeparture");
        entity.setDepartureTime("09:00");
        entity.setAccumulatedDistanceBefore(100L);
        entity.setDestination("exampleDestination");
        entity.setArrivalTime("10:00");
        entity.setAccumulatedDistanceAfter(150L);
        entity.setDrivingDistance(50L);
        entity.setPurpose("examplePurpose");
        entity.setPeriod("examplePeriod");

        repository.save(entity);

        //when:
        try {
            RegisterDrivingLogCommand command = new RegisterDrivingLogCommand();

            command.setRegistrationId("exampleRegistrationId");
            command.setVehicleNumber("exampleVehicleNumber");
            command.setRegistrationDate("2022-01-01");
            command.setDeparture("exampleDeparture");
            command.setDepartureTime("09:00");
            command.setAccumulatedDistanceBefore(100L);
            command.setDestination("exampleDestination");
            command.setArrivalTime("10:00");
            command.setAccumulatedDistanceAfter(150L);
            command.setDrivingDistance(50L);
            command.setPurpose("examplePurpose");
            command.setPeriod("examplePeriod");

            entity.registerDrivingLog(command);

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

            DrivingLogRegistered outputEvent = objectMapper.readValue(
                receivedPayload,
                DrivingLogRegistered.class
            );

            LOGGER.info("Response received: {}", outputEvent);

            assertEquals(
                outputEvent.getRegistrationId(),
                "exampleRegistrationId"
            );
            assertEquals(
                outputEvent.getVehicleNumber(),
                "exampleVehicleNumber"
            );
            assertEquals(outputEvent.getRegistrationDate(), "2022-01-01");
            assertEquals(outputEvent.getPurpose(), "examplePurpose");
            assertEquals(outputEvent.getDeparture(), "exampleDeparture");
            assertEquals(outputEvent.getDepartureTime(), "09:00");
            assertEquals(outputEvent.getAccumulatedDistanceBefore(), 100L);
            assertEquals(outputEvent.getDestination(), "exampleDestination");
            assertEquals(outputEvent.getArrivalTime(), "10:00");
            assertEquals(outputEvent.getAccumulatedDistanceAfter(), 150L);
            assertEquals(outputEvent.getDrivingDistance(), 50L);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }
}
