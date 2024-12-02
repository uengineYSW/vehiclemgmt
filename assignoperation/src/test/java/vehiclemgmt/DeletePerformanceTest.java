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
public class DeletePerformanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        DeletePerformanceTest.class
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

        entity.setRegistrationId("AB123");
        entity.setVehicleNumber("XYZ456");
        entity.setRegistrationDate("2022-01-01");
        entity.setDeparture("City A");
        entity.setDepartureTime("08:00");
        entity.setAccumulatedDistanceBefore(1000L);
        entity.setDestination("City B");
        entity.setArrivalTime("10:00");
        entity.setAccumulatedDistanceAfter(2000L);
        entity.setDrivingDistance(100L);
        entity.setPurpose("Business");
        entity.setPeriod("Daily");

        repository.save(entity);

        //when:
        try {
            VehiclePerformance entity = new VehiclePerformance();

            entity.setId(123L);

            repository.delete(entity);

            //then:

            VehiclePerformance result = repository
                .findById(entity.getRegistrationId())
                .get();

            LOGGER.info("Response received: {}", result);

            assertEquals(result.getRegistrationId(), "N/A");
            assertEquals(result.getVehicleNumber(), "N/A");
            assertEquals(result.getRegistrationDate(), "N/A");
            assertEquals(result.getDeparture(), "N/A");
            assertEquals(result.getDepartureTime(), "N/A");
            assertEquals(result.getAccumulatedDistanceBefore(), 0L);
            assertEquals(result.getDestination(), "N/A");
            assertEquals(result.getArrivalTime(), "N/A");
            assertEquals(result.getAccumulatedDistanceAfter(), 0L);
            assertEquals(result.getDrivingDistance(), 0L);
            assertEquals(result.getPurpose(), "N/A");
            assertEquals(result.getPeriod(), "N/A");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }
}
