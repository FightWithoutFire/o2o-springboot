package com.fightwithoutfire.o2o.config.redis;


import com.fightwithoutfire.o2o.cache.JedisPoolWriper;
import com.fightwithoutfire.o2o.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author xxx
 * @create 2020-02-15 15:09
 */
@Configuration
public class RedisConfiguration {

    @Value("${redis.hostname}")
    private String hostname;

    @Value("${redis.port}")
    private int port;


    @Value("${redis.pool.maxActive}")
    private int maxTotal;

    @Value("${redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${redis.pool.maxWait}")
    private int maxWaitMillis;

    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Autowired
    private JedisPoolWriper  jedisWritePool;

    @Autowired
    private JedisUtil jedisUtil;

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }

    @Bean(name = "jedisWritePool")
    public JedisPoolWriper createJedisWriper(){
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
        return jedisPoolWriper;
    }

    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil(){
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisWritePool);
        return jedisUtil;
    }

    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys(){
        return jedisUtil.new Keys();
    }

    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStrings(){
        return jedisUtil.new Strings();
    }

    @Bean(name = "jedisHash")
    public JedisUtil.Hash createJedisHash(){
        return jedisUtil.new Hash();
    }

    @Bean(name = "jedisLists")
    public JedisUtil.Lists createJedisLists(){
        return jedisUtil.new Lists();
    }

    @Bean(name = "jedisSets")
    public JedisUtil.Sets createJedisSets(){
        return jedisUtil.new Sets();
    }

}
