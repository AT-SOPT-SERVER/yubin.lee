package org.sopt.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
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

    @Value("${spring.data.redis.databases.comment-like-cache}")
    private int databaseCommentLikeCache;

    @Bean(name = "commentLikeCacheRedisTemplate")
    public RedisTemplate<String, String> commentLikeCacheRedisTemplate() {
        return createRedisTemplate(databaseCommentLikeCache);
    }

    @Bean(name = "refreshTokenRedisTemplate")
    public RedisTemplate<String, String> refreshTokenRedisTemplate() {
        return createRedisTemplate(databaseRefreshToken);
    }

    @Bean(name = "blacklistTokenRedisTemplate")
    public RedisTemplate<String, String> blacklistTokenRedisTemplate() {
        return createRedisTemplate(databaseBlacklistToken);
    }

    public RedisConnectionFactory redisConnectionFactory(int index) {
        final RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setDatabase(index);
        // Spring에서 Redis 서버와의 연결을 생성하고 관리
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private RedisTemplate<String, String> createRedisTemplate(int dbIndex) {
        RedisConnectionFactory factory = redisConnectionFactory(dbIndex);

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
