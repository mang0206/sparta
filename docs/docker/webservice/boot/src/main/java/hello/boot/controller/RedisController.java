package hello.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {

    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/redis/{key}/{value}")
    public String setRedisValue(@PathVariable String key, @PathVariable String value) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set(key, value);
        return "Value set successfully";
    }

    @GetMapping("/redis/{key}")
    public String getRedisValue(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        return vop.get(key);
    }
}
