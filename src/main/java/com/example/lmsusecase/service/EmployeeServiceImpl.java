package com.example.lmsusecase.service;

import com.example.lmsusecase.dto.EmployeeDTO;
import com.example.lmsusecase.entity.Employee;
import com.example.lmsusecase.exception.EmployeeNotFoundException;
import com.example.lmsusecase.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapToEmployee(employeeDTO);
        Employee createdEmployee = employeeRepository.save(employee);
        return mapToEmployeeDTO(createdEmployee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::mapToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            return mapToEmployeeDTO(employee);
        }
        throw new EmployeeNotFoundException("Employee not found with id: " + id);
    }


    private EmployeeDTO mapToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setEmailId(employee.getEmailId());
        employeeDTO.setDesignation(employee.getDesignation());
        return employeeDTO;
    }

    private Employee mapToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setEmailId(employeeDTO.getEmailId());
        employee.setDesignation(employeeDTO.getDesignation());
        return employee;
    }
}
