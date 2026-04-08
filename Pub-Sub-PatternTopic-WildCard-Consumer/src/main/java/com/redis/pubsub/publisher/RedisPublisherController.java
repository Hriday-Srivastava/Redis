package com.redis.pubsub.publisher;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//https://www.youtube.com/watch?v=uPMVIEfpjzo&list=PLdUn31k8Q721tgtkv1sfPJrvmi6-t3ijp&index=6
@RestController
@RequestMapping("/publish")
public class RedisPublisherController {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisPublisherController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostMapping("/news")
    public Long publishNews(@RequestParam String message){
        return stringRedisTemplate.convertAndSend("News-Live", message);
    }
    @PostMapping("/sports")
    public Long publishSportsNews(@RequestParam String message){
        return stringRedisTemplate.convertAndSend("News-Sports", message);
    }
}
