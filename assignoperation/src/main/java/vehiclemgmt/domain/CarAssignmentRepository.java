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
    collectionResourceRel = "carAssignments",
    path = "carAssignments"
)
public interface CarAssignmentRepository
    extends PagingAndSortingRepository<CarAssignment, Long> {
    @Query(
        value = "select carAssignment " +
        "from CarAssignment carAssignment " +
        "where(:carAssignmentId is null or carAssignment.carAssignmentId = :carAssignmentId) and (:registrationId is null or carAssignment.registrationId like %:registrationId%)"
    )
    List<CarAssignment> searchCarAssignment(
        Long carAssignmentId,
        String registrationId,
        Pageable pageable
    );
}
