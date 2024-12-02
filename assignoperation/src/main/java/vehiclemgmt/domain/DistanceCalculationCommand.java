package vehiclemgmt.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class DistanceCalculationCommand {

    private String registrationId;
    private int accumulatedDistanceBefore;
    private int accumulatedDistanceAfter;
}
