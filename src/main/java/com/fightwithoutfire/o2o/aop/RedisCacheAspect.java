package com.fightwithoutfire.o2o.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fightwithoutfire.o2o.annotation.RedisCache;
import com.fightwithoutfire.o2o.cache.JedisUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;

@Aspect
@Component
public class RedisCacheAspect {

	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
    @Pointcut("@annotation(com.fightwithoutfire.o2o.annotation.RedisCache)")
    public void webAspect() {
    }

    @SuppressWarnings("unchecked")
    @Around("webAspect()")
    public Object redisCache(ProceedingJoinPoint pjp) throws Throwable {
        //得到类名、方法名和参数
        String redisResult;
        Object[] args = pjp.getArgs();

        //得到被代理的方法
        Signature signature = pjp.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException();
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = pjp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        //得到被代理的方法上的注解
        String cacheKey = method.getAnnotation(RedisCache.class).cacheKey();
        String key = cacheKey;
        

        Object result = null;
        if (!jedisKeys.exists(key)) {
            //缓存不存在，则调用原方法，并将结果放入缓存中
            result = pjp.proceed(args);
            redisResult = JSON.toJSONString(result);
            jedisStrings.set(key, redisResult);
            System.out.println("mysql"+key);
        } else {
            //缓存命中
            redisResult = JSONObject.toJSON(jedisStrings.get(key)).toString();
            //得到被代理方法的返回值类型
            Class returnType = method.getReturnType();
            result = JSON.parseObject(redisResult, returnType);
            System.out.println("jedis"+key);
        }
        return result;
    }
}
