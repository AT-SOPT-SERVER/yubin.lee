package org.sopt.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.databases.refresh-token}")
    private int databaseRefreshToken;

    @Value("${spring.data.redis.databases.blacklist-token}")
    private int databaseBlacklistToken;

    @Bean
    public RedisTemplate<String, String> refreshTokenRedisTemplate() {
        return createRedisTemplate(databaseRefreshToken);
    }

    @Bean
    public RedisTemplate<String, String> blacklistTokenRedisTemplate() {
        return createRedisTemplate(databaseBlacklistToken);
    }

    private RedisTemplate<String, String> createRedisTemplate(int dbIndex) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setDatabase(dbIndex);

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
