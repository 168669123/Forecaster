package wjh.projects.infrastructure.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    @Value("${redis.database}")
    private int database;
    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.password}")
    private String password;

    @Bean(name = "redisTemplate")
    RedisTemplate<String, Object> redisTemplate() {
        return getRedisTemplate();
    }

    private RedisTemplate<String, Object> getRedisTemplate() {
        LettuceConnectionFactory connectionFactory = getConnectionFactory();
        connectionFactory.afterPropertiesSet();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    private LettuceConnectionFactory getConnectionFactory() {
        // 设置单机模式下连接Redis的配置信息
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setDatabase(database);
        standaloneConfig.setHostName(host);
        standaloneConfig.setPort(port);
        standaloneConfig.setPassword(password);

        // 设置连接池的配置信息
        GenericObjectPoolConfig<LettuceConnection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);

        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build();
        return new LettuceConnectionFactory(standaloneConfig, clientConfig);
    }
}

