package com.employees.employees.controller;

import com.employees.employees.domain.response.PairResponse;
import com.employees.employees.service.employee.EmployeeRecordsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeeRecordsService employeeRecordsService;

    public EmployeesController(EmployeeRecordsService employeeRecordsService) {
        this.employeeRecordsService = employeeRecordsService;
    }

    @GetMapping("/{filename:.+}")
    public List<PairResponse> getLongestWorkingPairs(@PathVariable String filename){
        return this.employeeRecordsService.longestWorkingPairs(filename);
    }

}
