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
public class DistanceCalculationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        DistanceCalculationTest.class
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

        entity.setRegistrationId("1");
        entity.setVehicleNumber("가1234");
        entity.setRegistrationDate("2021-01-01");
        entity.setDeparture("출발지");
        entity.setDepartureTime("10:00");
        entity.setAccumulatedDistanceBefore(5000L);
        entity.setDestination("도착지");
        entity.setArrivalTime("12:00");
        entity.setAccumulatedDistanceAfter(8000L);
        entity.setDrivingDistance(3000L);
        entity.setPurpose("운행목적");
        entity.setPeriod("운행기간");

        repository.save(entity);

        //when:
        try {
            DistanceCalculationCommand command = new DistanceCalculationCommand();

            command.setRegistrationId("1");
            command.setAccumulatedDistanceBefore(5000L);
            command.setAccumulatedDistanceAfter(8000L);

            entity.distanceCalculation(command);

            //then:

            VehiclePerformance result = repository
                .findById(entity.getRegistrationId())
                .get();

            LOGGER.info("Response received: {}", result);

            assertEquals(result.getRegistrationId(), "1");
            assertEquals(result.getVehicleNumber(), "가1234");
            assertEquals(result.getRegistrationDate(), "2021-01-01");
            assertEquals(result.getDeparture(), "출발지");
            assertEquals(result.getDepartureTime(), "10:00");
            assertEquals(result.getAccumulatedDistanceBefore(), 5000L);
            assertEquals(result.getDestination(), "도착지");
            assertEquals(result.getArrivalTime(), "12:00");
            assertEquals(result.getAccumulatedDistanceAfter(), 8000L);
            assertEquals(result.getDrivingDistance(), 3000L);
            assertEquals(result.getPurpose(), "운행목적");
            assertEquals(result.getPeriod(), "운행기간");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }
}
