package ru.andreykatunin.postgresconnect.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impossibl.postgres.api.jdbc.PGConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andreykatunin.postgresconnect.model.PayloadQuery;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Service
public class MessageConsumer {
    private static final Logger logger = LogManager.getLogger();

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${node.name}")
    private String nodeName;

    @Value(value = "${node.id}")
    private String nodeID;

    @Value(value = "${consumer.topic.name}")
    private String topicName;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MessageProducer producer;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public PGConnection connection;

    public final static String disableTriggers = "ALTER TABLE schema.table DISABLE TRIGGER USER";
    public final static String enableTriggers = "ALTER TABLE schema.table ENABLE TRIGGER USER";

    @Transactional
    public void batchUpdateWithoutTriggers(String... queries) {
        jdbcTemplate.batchUpdate(queries);
    }

    @KafkaListener(topics = "${consumer.topic.name}", containerFactory = "stringKafkaListenerContainerFactory")
    public void receive(@Payload List<String> messages) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> queries = new ArrayList<>(messages.size());
        String[] array = new String[queries.size()];
        HashSet<String> tables = new HashSet<>();
        for (String message : messages) {
            JsonNode jsonNode = objectMapper.readTree(message);
            PayloadQuery payloadQuery = objectMapper.treeToValue(jsonNode, PayloadQuery.class);

            System.out.println("*** QUERY START ***");
            System.out.println(payloadQuery.getQuery());
            System.out.println("*** QUERY END ***");


            queries.add(payloadQuery.getQuery().replaceAll(payloadQuery.getSchema(), nodeName));
            tables.add(payloadQuery.getTable());


        }
        ArrayList<String> disable = new ArrayList<>();
        ArrayList<String> enable = new ArrayList<>();
        ArrayList<String> queriesAll = new ArrayList<>();
        tables.forEach((table)->{
            disable.add(disableTriggers.replaceAll("schema", nodeName).replaceAll("table", table));
            enable.add(enableTriggers.replaceAll("schema", nodeName).replaceAll("table", table));
        });
        queriesAll.addAll(disable);
        queriesAll.addAll(queries);
        queriesAll.addAll(enable);
        System.out.println("*** BATCH QUERIES START ***");
        queriesAll.forEach(System.out::println);
        System.out.println("*** BATCH QUERIES END ***");
        batchUpdateWithoutTriggers(queriesAll.toArray(array));
    }
}