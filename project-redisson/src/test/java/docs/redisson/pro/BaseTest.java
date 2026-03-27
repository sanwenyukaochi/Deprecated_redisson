package docs.redisson.pro;

import docs.redisson.pro.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private final RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonClient redissonClient;

    @BeforeAll
    public void setClient() {
        this.redissonClient = redissonConfig.redissonClient();
    }

    @AfterAll
    public void shutdown() {
        this.redissonClient.shutdown();
    }
}
