package vehiclemgmt.domain;

import java.util.Date;
import lombok.Data;

@Data
public class SearchCarAssignmentQuery {

    private Long carAssignmentId;
    private String registrationId;
}
