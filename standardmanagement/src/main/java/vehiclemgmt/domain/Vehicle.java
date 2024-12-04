package vehiclemgmt.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import vehiclemgmt.StandardmanagementApplication;
import vehiclemgmt.domain.DriverDistanceUpdated;
import vehiclemgmt.domain.VehicleDeleted;
import vehiclemgmt.domain.VehicleDetailsUpdated;
import vehiclemgmt.domain.VehicleRegistered;

@Entity
@Table(name = "Vehicle_table")
@Data
//<<< DDD / Aggregate Root
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private VehicleStatus status;

    private String registrationNumber;

    private String make;

    private String model;

    private Integer year;

    private Double driverDistance;

    @PostPersist
    public void onPostPersist() {
        VehicleRegistered vehicleRegistered = new VehicleRegistered(this);
        vehicleRegistered.publishAfterCommit();

        VehicleDetailsUpdated vehicleDetailsUpdated = new VehicleDetailsUpdated(
            this
        );
        vehicleDetailsUpdated.publishAfterCommit();

        VehicleDeleted vehicleDeleted = new VehicleDeleted(this);
        vehicleDeleted.publishAfterCommit();

        DriverDistanceUpdated driverDistanceUpdated = new DriverDistanceUpdated(
            this
        );
        driverDistanceUpdated.publishAfterCommit();
    }

    @PrePersist
    public void onPrePersist() {}

    @PreUpdate
    public void onPreUpdate() {}

    public static VehicleRepository repository() {
        VehicleRepository vehicleRepository = StandardmanagementApplication.applicationContext.getBean(
            VehicleRepository.class
        );
        return vehicleRepository;
    }

    //<<< Clean Arch / Port Method
    public void updateVehicleStatus(
        UpdateVehicleStatusCommand updateVehicleStatusCommand
    ) {
        //implement business logic here:

        VehicleStatusUpdated vehicleStatusUpdated = new VehicleStatusUpdated(
            this
        );
        vehicleStatusUpdated.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void distanceCalculation(
        PerformanceRegistered performanceRegistered
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Vehicle vehicle = new Vehicle();
        repository().save(vehicle);

        */

        /** Example 2:  finding and process
        
        repository().findById(performanceRegistered.get???()).ifPresent(vehicle->{
            
            vehicle // do something
            repository().save(vehicle);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
