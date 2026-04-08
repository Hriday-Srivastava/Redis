package com.redis.pubsub.Consumer;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisAllNewsConsumer implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Async Test
        System.out.println("AllNewsConsumer Received Messages. Wait pls message will be shown !");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String body = new String(message.getBody());
        String channel = new String(message.getChannel());
        System.out.println("AllNewsConsumer received message : "+body+" From Pattern/Topic : "+channel);
    }
}
