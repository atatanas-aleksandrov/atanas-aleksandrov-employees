package com.employees.employees.repository.employee;

import com.employees.employees.domain.entity.Entry;

import java.util.List;

public interface EmployeeRepository {

    List<Entry> getEntries(String fileName);
}
