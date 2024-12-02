package vehiclemgmt.domain;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vehiclemgmt.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "vehiclePerformances",
    path = "vehiclePerformances"
)
public interface VehiclePerformanceRepository
    extends PagingAndSortingRepository<VehiclePerformance, String> {
    @Query(
        value = "select vehiclePerformance " +
        "from VehiclePerformance vehiclePerformance " +
        "where(:registrationDate is null or vehiclePerformance.registrationDate = :registrationDate) and (:period is null or vehiclePerformance.period = :period)"
    )
    List<VehiclePerformance> searchVehiclePerformance(
        Date registrationDate,
        Period period,
        Pageable pageable
    );
}
