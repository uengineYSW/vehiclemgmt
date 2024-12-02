package vehiclemgmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class DistanceCalculationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        DistanceCalculationTest.class
    );

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MessageVerifier<Message<?>> messageVerifier;

    @Autowired
    public VehicleRepository repository;

    @Test
    @SuppressWarnings("unchecked")
    public void test0() {
        //given:
        Vehicle entity = new Vehicle();

        entity.setModel("그랜저");
        entity.setId(1L);
        entity.setStatus("ACTIVE");
        entity.setRegistrationNumber("서울1234");
        entity.setMake("현대");
        entity.setYear(2020L);
        entity.setDriverDistance(1000L);

        repository.save(entity);

        //when:

        PerformanceRegistered event = new PerformanceRegistered();

        event.setRegistrationId("12345");
        event.setVehicleNumber("서울1234");
        event.setAccumulatedDistanceAfter(2000L);
        event.setDrivingDistance(500L);
        event.setPurpose("BUSINESS");
        event.setPeriod("WEEKLY");
        event.setRegistrationDate("2022-07-01");

        StandardmanagementApplication.applicationContext = applicationContext;

        ObjectMapper objectMapper = new ObjectMapper()
            .configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false
            );
        try {
            this.messageVerifier.send(
                    MessageBuilder
                        .withPayload(event)
                        .setHeader(
                            MessageHeaders.CONTENT_TYPE,
                            MimeTypeUtils.APPLICATION_JSON
                        )
                        .setHeader("type", event.getEventType())
                        .build(),
                    "vehiclemgmt"
                );

            //then:

            Vehicle result = repository.findById(entity.getId()).get();

            LOGGER.info("Response received: {}", result);

            assertEquals(result.getId(), 1L);
            assertEquals(result.getStatus(), "ACTIVE");
            assertEquals(result.getRegistrationNumber(), "서울1234");
            assertEquals(result.getMake(), "현대");
            assertEquals(result.getModel(), "그랜저");
            assertEquals(result.getYear(), 2020L);
            assertEquals(result.getDriverDistance(), 1500L);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }
}
