package vehiclemgmt.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import vehiclemgmt.AssignoperationApplication;
import vehiclemgmt.domain.PerformanceDeleted;
import vehiclemgmt.domain.PerformanceModified;
import vehiclemgmt.domain.PerformanceRegistered;

@Entity
@Table(name = "VehiclePerformance_table")
@Data
public class VehiclePerformance {

    @Id
    private String registrationId;

    private String vehicleNumber;
    private Date registrationDate;
    private String departure;
    private String departureTime;

    @Embedded
    private int accumulatedDistanceBefore;

    private String destination;
    private String arrivalTime;

    @Embedded
    private int accumulatedDistanceAfter;

    private Double drivingDistance;

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @Embedded
    private Period period;

    @PostPersist
    public void onPostPersist() {
        PerformanceRegistered performanceRegistered = new PerformanceRegistered(
            this
        );
        performanceRegistered.publishAfterCommit();

        PerformanceModified performanceModified = new PerformanceModified(this);
        performanceModified.publishAfterCommit();

        PerformanceDeleted performanceDeleted = new PerformanceDeleted(this);
        performanceDeleted.publishAfterCommit();
    }

    @PrePersist
    public void onPrePersist() {}

    @PreUpdate
    public void onPreUpdate() {}

    public static VehiclePerformanceRepository repository() {
        VehiclePerformanceRepository vehiclePerformanceRepository = AssignoperationApplication.applicationContext.getBean(
            VehiclePerformanceRepository.class
        );
        return vehiclePerformanceRepository;
    }

    public void registerDrivingLog(
        RegisterDrivingLogCommand registerDrivingLogCommand
    ) {
        DrivingLogRegistered drivingLogRegistered = new DrivingLogRegistered(
            this
        );
        drivingLogRegistered.publishAfterCommit();
    }

    public void distanceCalculation(
        DistanceCalculationCommand distanceCalculationCommand
    ) {
        try {
            this.accumulatedDistanceBefore =
                distanceCalculationCommand.getAccumulatedDistanceBefore();
            this.accumulatedDistanceAfter =
                distanceCalculationCommand.getAccumulatedDistanceAfter();
            this.drivingDistance =
                (double) (
                    this.accumulatedDistanceAfter -
                    this.accumulatedDistanceBefore
                );
        } catch (Exception e) {
            throw new RuntimeException("Distance Calculation failed", e);
        }
    }
}
