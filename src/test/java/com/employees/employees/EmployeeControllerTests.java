package com.employees.employees;

import com.employees.employees.controller.EmployeesController;
import com.employees.employees.domain.response.PairResponse;
import com.employees.employees.service.employee.EmployeeRecordsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeRecordsService employeeRecordsService;

    @Test
    public void shouldListAllPairs() throws Exception {
        PairResponse pair1 = new PairResponse(41,17,1, 527L);
        PairResponse pair2 = new PairResponse(9,37,2, 850L);

        given(this.employeeRecordsService.longestWorkingPairs("employees.csv"))
                .willReturn(List.of(pair1,pair2));

        this.mvc.perform(get("/employees/employees.csv"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].emp1ID").value(41))
                .andExpect(jsonPath("$[0].emp2ID").value(17))
                .andExpect(jsonPath("$[0].projectID").value(1))
                .andExpect(jsonPath("$[0].daysWorked").value(527L))
                .andExpect(jsonPath("$[1].emp1ID").value(9))
                .andExpect(jsonPath("$[1].emp2ID").value(37))
                .andExpect(jsonPath("$[1].projectID").value(2))
                .andExpect(jsonPath("$[1].daysWorked").value(850L));

    }
}
