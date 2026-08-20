package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.redisson.api.RGeo;
import org.redisson.api.geo.GeoEntry;
import org.redisson.api.geo.GeoPosition;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.api.geo.GeoUnit;

public class 六63GeospatialBucketTest extends BaseTest {

    @Test
    public void GeospatialBucketTest01() {
        RGeo<String> geo = redissonClient.getGeo("test");
        // 添加地理位置
        geo.add(
                new GeoEntry(13.361389, 38.115556, "Palermo"),
                new GeoEntry(15.087269, 37.502669, "Andy"));
        geo.addAsync(37.618423, 55.751244, "Moscow");
        // 计算距离
        Double distance = geo.dist("Palermo", "Andy", GeoUnit.METERS);
        // 获取位置坐标
        Map<String, GeoPosition> positions = geo.pos("test2", "Palermo", "test3", "Andy", "test1");
        // 半径查询
        List<String> cities =
                geo.search(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS));
        // 半径查询
        Map<String, GeoPosition> citiesWithPositions =
                geo.searchWithPosition(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS));

        IO.println(String.format("Distance: %.2f", distance) + " km");
        positions.forEach((name, pos) -> IO.println(name + ": " + pos));
        IO.println("Cities: " + cities);
        citiesWithPositions.forEach((name, pos) -> IO.println(name + ": " + pos));
    }
}
