package vehiclemgmt.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import vehiclemgmt.config.kafka.KafkaProcessor;
import vehiclemgmt.domain.*;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PerformanceRegistered'"
    )
    public void wheneverPerformanceRegistered_DistanceCalculation(
        @Payload PerformanceRegistered performanceRegistered
    ) {
        PerformanceRegistered event = performanceRegistered;
        System.out.println(
            "\n\n##### listener DistanceCalculation : " +
            performanceRegistered +
            "\n\n"
        );

        // Comments //
        //운행 km는 운행 후 누적km 에서 운행 전 누적 km를 subtract 한 값이 output 되어야 해.

        // Sample Logic //
        Vehicle.distanceCalculation(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
