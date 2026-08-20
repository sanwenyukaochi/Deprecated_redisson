package com.sanwenyukaochi.redisson.六分布式对象;

import com.sanwenyukaochi.redisson.BaseTest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBinaryStream;
import tools.jackson.databind.json.JsonMapper;

public class 六62BinaryStreamTest extends BaseTest {

    @Test
    public void BinaryStreamTest01() {
        record AnyObject(int x) {}
        RBinaryStream stream = redissonClient.getBinaryStream("anyStream");
        byte[] content = JsonMapper.shared().writeValueAsBytes(new AnyObject(1));
        stream.set(content);
        byte[] data = stream.get();
        AnyObject jsonObject = JsonMapper.shared().readValue(data, AnyObject.class);
        IO.println("read value: " + data.length);
        IO.println("object value: " + jsonObject);
    }

    @Test
    public void BinaryStreamTest02() throws IOException {
        record AnyObject(int x) {}
        RBinaryStream stream = redissonClient.getBinaryStream("anyStream");
        byte[] content = JsonMapper.shared().writeValueAsBytes(new AnyObject(1));
        try (OutputStream os = stream.getOutputStream()) {
            os.write(content);
        }
        try (InputStream is = stream.getInputStream()) {
            byte[] readBuffer = new byte[content.length];
            int read = is.read(readBuffer);
            AnyObject jsonObject = JsonMapper.shared().readValue(readBuffer, AnyObject.class);
            IO.println("read value: " + read);
            IO.println("object value: " + jsonObject);
        }
    }
}
