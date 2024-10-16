package com.employees.employees.domain.entity;

import org.springframework.lang.NonNull;

import java.time.LocalDate;

public class Entry {
    private final Integer empID;

    private final Integer projectID;

    private final LocalDate dateFrom;

    private final LocalDate dateTo;

    public Entry(@NonNull Integer empID,@NonNull Integer projectID,@NonNull LocalDate dateFrom, LocalDate dateTo) {
        this.empID = empID;
        this.projectID = projectID;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo == null? LocalDate.now() : dateTo;
    }

    public Integer getEmpID() {
        return this.empID;
    }

    public Integer getProjectID() {
        return this.projectID;
    }

    public LocalDate getDateFrom() {
        return this.dateFrom;
    }

    public LocalDate getDateTo() {
        return this.dateTo;
    }


    @Override
    public String toString() {
        return "EmpID: " + this.empID
                + "ProjectID: " + this.projectID
                + "DateFrom: " + this.dateFrom
                + "DateTo: " + this.dateTo;

    }
}
