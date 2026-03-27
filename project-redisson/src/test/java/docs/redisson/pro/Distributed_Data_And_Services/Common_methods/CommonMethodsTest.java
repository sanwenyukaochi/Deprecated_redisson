package docs.redisson.pro.Distributed_Data_And_Services.Common_methods;

import docs.redisson.pro.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;

/**
 * <p>参考文档：<a href="https://redisson.pro/docs/data-and-services/common-methods/"></a></p>
 */
public class CommonMethodsTest extends BaseTest {
    @Test
    public void ObjectNameTest() {
        RMap<Object, Object> map = redissonClient.getMap("mymap");
        map.getName(); // 获取keyName
    }
}
