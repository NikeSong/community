package com.example.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTests {

    @Autowired
    private KafkaProducer producer;

    @Test
    public void testKafka() throws InterruptedException {
       // Thread.sleep(1000*1);

        producer.sendMessage("test","@@@@@@@@@@@@@2");
        producer.sendMessage("test","####################");
        Thread.sleep(1000*10);
    }

}

@Component
class KafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic,String content){
        kafkaTemplate.send(topic,content);

    }
}

@Component
class KafkaConsumer {
    @KafkaListener(topics = ("test"))
    public void handleMessage(ConsumerRecord record)
    {
        System.out.println(record.value());
    }
}