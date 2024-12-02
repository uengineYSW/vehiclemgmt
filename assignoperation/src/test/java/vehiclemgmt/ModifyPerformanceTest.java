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
public class ModifyPerformanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        ModifyPerformanceTest.class
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
        entity.setVehicleNumber("123456");
        entity.setRegistrationDate("2021-01-01");
        entity.setDeparture("City A");
        entity.setDepartureTime("08:00");
        entity.setAccumulatedDistanceBefore(10000L);
        entity.setDestination("City B");
        entity.setArrivalTime("10:00");
        entity.setAccumulatedDistanceAfter(15000L);
        entity.setDrivingDistance(5000L);
        entity.setPurpose("Business");
        entity.setPeriod("Daily");

        repository.save(entity);

        //when:
        try {
            ModifyPerformance command = new ModifyPerformanceCommand();

            command.setRegistrationId("ABC123");
            command.setVehicleNumber("123456");
            command.setRegistrationDate("2021-01-01");
            command.setDeparture("City A");
            command.setDepartureTime("08:00");
            command.setAccumulatedDistanceBefore(10000L);
            command.setDestination("City B");
            command.setArrivalTime("10:00");
            command.setAccumulatedDistanceAfter(15000L);
            command.setDrivingDistance(5000L);
            command.setPurpose("Business");
            command.setPeriod("Daily");

            entity.modifyperformance(command);

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

            LOGGER.info("Response received: {}", outputEvent);

            assertEquals(outputEvent.getRegistrationId(), "ABC123");
            assertEquals(outputEvent.getVehicleNumber(), "123456");
            assertEquals(outputEvent.getRegistrationDate(), "2021-01-01");
            assertEquals(outputEvent.getDeparture(), "City A");
            assertEquals(outputEvent.getDepartureTime(), "08:00");
            assertEquals(outputEvent.getAccumulatedDistanceBefore(), 10000L);
            assertEquals(outputEvent.getDestination(), "City B");
            assertEquals(outputEvent.getArrivalTime(), "10:00");
            assertEquals(outputEvent.getAccumulatedDistanceAfter(), 15000L);
            assertEquals(outputEvent.getDrivingDistance(), 5000L);
            assertEquals(outputEvent.getPurpose(), "Business");
            assertEquals(outputEvent.getPeriod(), "Daily");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }
}
