package com.fpt.sba301.slot17.service;

import com.fpt.sba301.slot17.domain.Department;
import com.fpt.sba301.slot17.domain.Employee;
import com.fpt.sba301.slot17.exception.DuplicateResourceException;
import com.fpt.sba301.slot17.exception.ResourceNotFoundException;
import com.fpt.sba301.slot17.repository.DepartmentRepository;
import com.fpt.sba301.slot17.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Department createDepartment(String name) {
        String normalized = name == null ? "" : name.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Department name is required");
        }
        // Bai 3: Service rule: Duplicate check in Service layer
        if (departmentRepository.existsByNameIgnoreCase(normalized)) {
            throw new DuplicateResourceException("Department already exists with name: " + normalized);
        }
        return departmentRepository.save(new Department(normalized));
    }

    @Transactional
    public Employee addEmployeeToDepartment(Long departmentId, String fullName, String email) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + departmentId));

        if (employeeRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Employee email already exists: " + email);
        }

        Employee employee = new Employee(fullName, email, department);
        // Using helper method to maintain bidirectional relationship consistency
        department.addEmployee(employee);
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
