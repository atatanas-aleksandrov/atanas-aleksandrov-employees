package com.employees.employees.service.employee;

import com.employees.employees.domain.response.PairResponse;

import java.util.List;

public interface EmployeeRecordsService {

    List<PairResponse> longestWorkingPairs(String fileName);
}
