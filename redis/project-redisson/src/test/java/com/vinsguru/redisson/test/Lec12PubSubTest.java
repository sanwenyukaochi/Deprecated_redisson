package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RTopic;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.client.codec.StringCodec;

public class Lec12PubSubTest extends BaseTest {

    @Test
    public void subscriber1() {
        RTopic topic = this.client.getTopic("slack-room1", StringCodec.INSTANCE);
        topic.addListener(String.class, (channel, msg) -> {
            IO.println(msg);
        });
        sleep(600_000);
    }

    @Test
    public void subscriber2_blocking() {
        RPatternTopic patternTopic = this.client.getPatternTopic("slack-room*", StringCodec.INSTANCE);
        patternTopic.addListener(String.class, new PatternMessageListener<String>() {
            @Override
            public void onMessage(CharSequence pattern, CharSequence topic, String msg) {
                IO.println(pattern + " : " + topic + " : " + msg);
            }
        });
        sleep(600_000);
    }
}