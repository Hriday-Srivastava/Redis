package com.redis.pubsub.config;

import com.redis.pubsub.Consumer.RedisAllNewsConsumer;
import com.redis.pubsub.Consumer.RedisNewsConsumer;
import com.redis.pubsub.Consumer.RedisSportNewsConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer configConsumer(
            RedisConnectionFactory redisConnectionFactory,
            RedisNewsConsumer newsConsumer,
            RedisSportNewsConsumer sportsNewsConsumer,
            RedisAllNewsConsumer redisAllNewsConsumer){
        RedisMessageListenerContainer redisMessageListenerContainer
                = new RedisMessageListenerContainer();

        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(newsConsumer, new ChannelTopic("News-Live"));
        redisMessageListenerContainer.addMessageListener(sportsNewsConsumer, new ChannelTopic("News-Sports"));
        redisMessageListenerContainer.addMessageListener(redisAllNewsConsumer, new PatternTopic("News*"));
        return redisMessageListenerContainer;
    }

}
