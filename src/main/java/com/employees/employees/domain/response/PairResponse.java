package com.employees.employees.domain.response;

import java.io.Serializable;

public record PairResponse(
        Integer emp1ID,
        Integer emp2ID,
        Integer projectID,
        Long daysWorked
) implements Serializable {
}
