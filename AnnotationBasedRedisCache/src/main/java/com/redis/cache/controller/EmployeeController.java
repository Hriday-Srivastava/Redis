package com.redis.cache.controller;

import com.redis.cache.entity.Employee;
import com.redis.cache.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/redis")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<Employee>(employeeService.save(employee), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<List<Employee>> addAllEmployees(@RequestBody List<Employee> employeeList) {
        return new ResponseEntity<List<Employee>>(employeeService.saveAll(employeeList), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        return new ResponseEntity<Employee>(employeeService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return new ResponseEntity<List<Employee>>(employeeService.findAll(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
        return new ResponseEntity<Employee>(employeeService.updateEmployee(employee), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String>  deleteEmployeeById(int id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<String>("Employee is deleted successfully of ID :"+id, HttpStatus.OK);
    }


}
