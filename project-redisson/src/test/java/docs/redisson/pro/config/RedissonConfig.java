package docs.redisson.pro.config;

import lombok.SneakyThrows;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonConfig {

    @SneakyThrows
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setUsername("default").setPassword("Syf#20011015#");
        config.useSingleServer().setAddress("redis://10.0.0.1:6379").setDatabase(0);
        return Redisson.create(config);
    }
}
