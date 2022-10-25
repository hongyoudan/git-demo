# SpringBoot 整合 Redis
## 1 引入 Redis 的依赖

```xml
<!-- redis 缓存操作 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
## 2 在 application.yml 中配置 Redis 信息

```yml
# redis 配置
redis:
  # 地址
  host: localhost
  # 端口，默认为6379
  port: 6379
  # 数据库索引
  database: 0
  # 密码
  password:
  # 连接超时时间
  timeout: 10s
  lettuce:
    pool:
      # 连接池中的最小空闲连接
      min-idle: 0
      # 连接池中的最大空闲连接
      max-idle: 8
      # 连接池的最大数据库连接数
      max-active: 8
      # #连接池最大阻塞等待时间（使用负值表示没有限制）
      max-wait: -1ms
```

## 3 使用 RedisTemplate 工具类操作 Redis
SpringBoot 中使用 RedisTemplate 来操作 Redis，需要在我们的 Bean 中注入这个对象，代码如下：

```java
@Autowired 
private RedisTemplate<String, String> redisTemplate; 

// 用下面5个对象来操作对应的类型 
this.redisTemplate.opsForValue(); //提供了操作string类型的所有方法 
this.redisTemplate.opsForList(); // 提供了操作list类型的所有方法 
this.redisTemplate.opsForSet(); //提供了操作set的所有方法 
this.redisTemplate.opsForHash(); //提供了操作hash表的所有方法 
this.redisTemplate.opsForZSet(); //提供了操作zset的所有方法
```
## 4 实例代码

```java
@RestController
@RequestMapping("/redis")
public class RedisDemoController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/stringTest")
    public String stringTest() {
        this.redisTemplate.delete("name");
        this.redisTemplate.opsForValue().set("name", "路人");
        String name = this.redisTemplate.opsForValue().get("name");
        return name;
    }

    @GetMapping("/listTest")
    public List<String> listTest() {
        this.redisTemplate.delete("names");
        this.redisTemplate.opsForList().rightPushAll("names", "刘德华", "张学友", "郭富城", "黎明");
        List<String> courses = this.redisTemplate.opsForList().range("names", 0, -1);
        return courses;
    }

    @GetMapping("setTest")
    public Set<String> setTest() {
        this.redisTemplate.delete("courses");
        this.redisTemplate.opsForSet().add("courses", "java", "spring", "springboot");
        Set<String> courses = this.redisTemplate.opsForSet().members("courses");
        return courses;
    }

    @GetMapping("hashTest")
    public Map<Object, Object> hashTest() {
        this.redisTemplate.delete("userMap");
        Map<String, String> map = new HashMap<>();
        map.put("name", "路人");
        map.put("age", "30");
        this.redisTemplate.opsForHash().putAll("userMap", map);
        Map<Object, Object> userMap = this.redisTemplate.opsForHash().entries("userMap");
        return userMap;
    }

    @GetMapping("zsetTest")
    public Set<String> zsetTest() {
        this.redisTemplate.delete("languages");
        this.redisTemplate.opsForZSet().add("languages", "java", 100d);
        this.redisTemplate.opsForZSet().add("languages", "c", 95d);
        this.redisTemplate.opsForZSet().add("languages", "php", 70);
        Set<String> languages = this.redisTemplate.opsForZSet().range("languages", 0, -1);
        return languages;
    }
}
```


