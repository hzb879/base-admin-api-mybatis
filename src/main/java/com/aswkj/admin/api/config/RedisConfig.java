package com.aswkj.admin.api.config;

import com.aswkj.admin.api.common.constant.CacheManagerConstant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.time.Duration;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisConfig {

  private Duration timeToLive = Duration.ZERO;
  private Jackson2JsonRedisSerializer jackson2JsonRedisSerializer;
  private RedisSerializer<String> stringRedisSerializer;
  private RedisConnectionFactory redisConnectionFactory;

  public void setTimeToLive(Duration timeToLive) {
    this.timeToLive = timeToLive;
  }

  @Autowired
  public RedisConfig(RedisConnectionFactory redisConnectionFactory, JavaTimeModule javaTimeModule) {
    this.redisConnectionFactory = redisConnectionFactory;
    this.jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    //解决查询缓存转换异常的问题
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    objectMapper.registerModule(javaTimeModule);
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    this.stringRedisSerializer = new StringRedisSerializer();
  }

  @Bean
  public RedisTemplate redisTemplate() {
    RedisTemplate redisTemplate = new RedisTemplate<>();
    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Primary
  @Bean(CacheManagerConstant.REDIS_DEFAULT)
  public CacheManager cacheManager() {
    RedisCacheConfiguration redisCacheConfiguration = getRedisCacheConfig().entryTtl(timeToLive);
    RedisCacheManager cacheManager = buildRedisCacheManager(redisCacheConfiguration);
    return cacheManager;
  }

  @Bean(CacheManagerConstant.REDIS_INFINITE)
  public CacheManager redisAlwaysLiveManager() {
    RedisCacheConfiguration redisCacheConfiguration = getRedisCacheConfig().entryTtl(Duration.ZERO);
    RedisCacheManager cacheManager = buildRedisCacheManager(redisCacheConfiguration);
    return cacheManager;
  }

  @Bean(CacheManagerConstant.REDIS_30_MINUTES)
  public CacheManager redis30MinutesManager() {
    RedisCacheConfiguration redisCacheConfiguration = getRedisCacheConfig().entryTtl(Duration.ofMinutes(30));
    RedisCacheManager cacheManager = buildRedisCacheManager(redisCacheConfiguration);
    return cacheManager;
  }

  private RedisCacheConfiguration getRedisCacheConfig() {
    return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
            .disableCachingNullValues();
  }

  private RedisCacheManager buildRedisCacheManager(RedisCacheConfiguration redisCacheConfiguration) {
    return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .build();
  }

}
