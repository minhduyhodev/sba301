package com.fpt.sba301.slot17.domain;

import com.fpt.sba301.slot17.repository.DepartmentRepository;
import com.fpt.sba301.slot17.repository.EmployeeRepository;
import com.fpt.sba301.slot17.service.DepartmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DepartmentEmployeeTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Bai 1-5: Test Department entity, repository, service rules and Employee Many-to-One")
    void testDepartmentAndEmployeeRelation() {
        Department dept = departmentService.createDepartment("Finance");
        assertThat(dept.getId()).isNotNull();

        Employee emp = departmentService.addEmployeeToDepartment(dept.getId(), "Charlie Brown", "charlie@company.com");
        assertThat(emp.getId()).isNotNull();
        assertThat(emp.getDepartment().getId()).isEqualTo(dept.getId());

        List<Employee> employeesInDept = employeeRepository.findByDepartmentId(dept.getId());
        assertThat(employeesInDept).hasSize(1);
        assertThat(employeesInDept.get(0).getFullName()).isEqualTo("Charlie Brown");
    }
}
