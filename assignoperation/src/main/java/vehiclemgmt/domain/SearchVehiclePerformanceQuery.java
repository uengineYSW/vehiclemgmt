package vehiclemgmt.domain;

import java.util.Date;
import lombok.Data;

@Data
public class SearchVehiclePerformanceQuery {

    private Date registrationDate;
    private Period period;
}
