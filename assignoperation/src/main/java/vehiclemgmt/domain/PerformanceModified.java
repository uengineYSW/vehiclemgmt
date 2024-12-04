package vehiclemgmt.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import vehiclemgmt.domain.*;
import vehiclemgmt.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PerformanceModified extends AbstractEvent {

    private String registrationId;

    public PerformanceModified(VehiclePerformance aggregate) {
        super(aggregate);
    }

    public PerformanceModified() {
        super();
    }
}
//>>> DDD / Domain Event
