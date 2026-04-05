package com.redis.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class Controller {

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    @GetMapping
    public String saveKeyValue(@RequestParam String key, @RequestParam String value){
        redisTemplate.opsForValue().set(key, value);
        return redisTemplate.opsForValue().get(key);

    }

}
