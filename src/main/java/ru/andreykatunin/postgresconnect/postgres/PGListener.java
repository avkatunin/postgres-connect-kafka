package ru.andreykatunin.postgresconnect.postgres;

import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.andreykatunin.postgresconnect.kafka.MessageProducer;

@Component
public class PGListener implements PGNotificationListener {

    @Value(value = "${producer.topic.name}")
    private String topicName;

    @Autowired
    MessageProducer producer;

    @Override
    public void notification(int processId, String channelName, String payload) {
        System.out.println("Received Notification: " + processId + ", " + channelName + ", " + payload);
        producer.sendMessage(topicName, payload);
    }

    @Override
    public void closed() {
        // initiate reconnection & restart listening
    }
}
