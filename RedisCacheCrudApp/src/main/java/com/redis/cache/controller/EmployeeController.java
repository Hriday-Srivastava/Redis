package com.redis.cache.controller;

import com.redis.cache.entity.Employee;
import com.redis.cache.entity.EmployeeRepo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class EmployeeController {

    private final EmployeeRepo employeeRepo;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String EMPLOYEE_LIST_KEY = "employeeList";

    public EmployeeController(EmployeeRepo employeeRepo, RedisTemplate<String, Object> redisTemplate) {
        this.employeeRepo = employeeRepo;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {

        return new ResponseEntity<Employee>(employeeRepo.save(employee), HttpStatus.OK);

    }

    @PostMapping("/save")
    public ResponseEntity<List<Employee>> addAllEmployees(@RequestBody List<Employee> employeeList) {
        return new ResponseEntity<List<Employee>>(employeeRepo.saveAll(employeeList), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found With " + id));

    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {

        // Try to get from Redis first
        List<Employee> employees = (List<Employee>) redisTemplate.opsForValue().get(EMPLOYEE_LIST_KEY);

        if (employees != null) {
            // Cache hit
            System.out.println("Returning data from Redis cache");
            return new ResponseEntity<>(employees, HttpStatus.OK);
        }

        // Cache miss → fetch from DB
        employees = employeeRepo.findAll();

        // Store in Redis for future requests (TTL optional)
        redisTemplate.opsForValue().set(EMPLOYEE_LIST_KEY, employees, 20, TimeUnit.SECONDS);

        System.out.println("Returning data from DB and storing in Redis cache");
        return new ResponseEntity<>(employees, HttpStatus.OK);


    }


}
