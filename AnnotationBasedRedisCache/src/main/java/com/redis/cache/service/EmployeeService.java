package com.redis.cache.service;

import com.redis.cache.entity.Employee;
import com.redis.cache.entity.EmployeeRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String EMPLOYEE_LIST_KEY = "employeeList";

    public EmployeeService(EmployeeRepo employeeRepo, RedisTemplate<String, Object> redisTemplate) {
        this.employeeRepo = employeeRepo;
        this.redisTemplate = redisTemplate;
    }

    // ---------------- POST / SAVE ----------------
    // Save Employee and cache it
    @CachePut(value = "employees", key = "#result.empId")
    public Employee save(Employee employee) {
        return employeeRepo.save(employee);
    }

    // ---------------- GET / FIND BY ID ----------------
    // Retrieve Employee from cache if available, otherwise fetch from DB
    @Cacheable(value = "employees", key = "#id")
    public Employee findById(int id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Employee Not Found With ID: " + id));
    }

    // ---------------- DELETE ----------------
    // Delete Employee from DB and remove cache
    @CacheEvict(value = "employees", key = "#id")
    public void deleteEmployee(int id){
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Employee not found with id: " + id));
        employeeRepo.deleteById(employee.getEmpId());
    }

    // ---------------- PUT / UPDATE ----------------
    // Update Employee in DB and update cache
    @CachePut(value = "employees", key = "#employee.empId")
    public Employee updateEmployee(Employee employee){
        Employee existing = employeeRepo.findById(employee.getEmpId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Employee not found with id: " + employee.getEmpId()));

        existing.setAddress(employee.getAddress());
        existing.setEmpName(employee.getEmpName());
        existing.setMobNo(employee.getMobNo());

        return employeeRepo.save(existing);
    }

    //Manual implement of caching using RedisTemplate i.e without annotation.
    public List<Employee> saveAll(List<Employee> employeeList) {
        // Try to get from Redis first
        List<Employee> employees = (List<Employee>) redisTemplate.opsForValue().get(EMPLOYEE_LIST_KEY);
        if (employees != null) {
            // Cache hit
            System.out.println("Returning data from Redis cache");
            return employees;
        }
        // Cache miss → fetch from DB
        employees = employeeRepo.saveAll(employeeList);
        // Store in Redis for future requests (TTL optional)
        redisTemplate.opsForValue().set(EMPLOYEE_LIST_KEY, employees, 20, TimeUnit.SECONDS);
        System.out.println("Returning data from DB and storing in Redis cache");
        return employees;
    }


    //Manual implement of caching using RedisTemplate i.e without annotation.
    public List<Employee> findAll() {
        // Try to get from Redis first
        List<Employee> employees = (List<Employee>) redisTemplate.opsForValue().get(EMPLOYEE_LIST_KEY);
        if (employees != null) {
            // Cache hit
            System.out.println("Returning data from Redis cache");
            return employees;
        }
        // Cache miss → fetch from DB
        employees = employeeRepo.findAll();
        // Store in Redis for future requests (TTL optional)
        redisTemplate.opsForValue().set(EMPLOYEE_LIST_KEY, employees, 20, TimeUnit.SECONDS);
        System.out.println("Returning data from DB and storing in Redis cache");
        return employees;
    }





}
