package com.hyd.redisdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: hayden
 * @Date: 2022-10-26
 * @Description:
 */
@RestController
@RequestMapping("/redis")
public class RedisDemoController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/stringTest")
    public String stringTest() {
        redisTemplate.delete("name");
        redisTemplate.opsForValue().set("name", "路人");
        String name = redisTemplate.opsForValue().get("name");
        return name;
    }

    @GetMapping("/listTest")
    public List<String> listTest() {
        redisTemplate.delete("names");
        redisTemplate.opsForList().rightPushAll("names", "刘德华", "张学友", "郭富城", "黎明");
        List<String> courses = redisTemplate.opsForList().range("names", 0, -1);
        return courses;
    }

    @GetMapping("setTest")
    public Set<String> setTest() {
        redisTemplate.delete("courses");
        redisTemplate.opsForSet().add("courses", "java", "spring", "springboot");
        Set<String> courses = redisTemplate.opsForSet().members("courses");
        return courses;
    }

    @GetMapping("hashTest")
    public Map<Object, Object> hashTest() {
        redisTemplate.delete("userMap");
        Map<String, String> map = new HashMap<>();
        map.put("name", "路人");
        map.put("age", "30");
        redisTemplate.opsForHash().putAll("userMap", map);
        Map<Object, Object> userMap = redisTemplate.opsForHash().entries("userMap");
        return userMap;
    }

    @GetMapping("zsetTest")
    public Set<String> zsetTest() {
        redisTemplate.delete("languages");
        redisTemplate.opsForZSet().add("languages", "java", 100d);
        redisTemplate.opsForZSet().add("languages", "c", 95d);
        redisTemplate.opsForZSet().add("languages", "php", 70);
        Set<String> languages = redisTemplate.opsForZSet().range("languages", 0, -1);
        return languages;
    }
}
