package ru.andreykatunin.postgresconnect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static ru.andreykatunin.postgresconnect.kafka.MessageConsumer.disableTriggers;
import static ru.andreykatunin.postgresconnect.kafka.MessageConsumer.enableTriggers;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresConnectApplicationTests {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	public void contextLoads() {
		triggerOff();
	}

	@Transactional
	public void triggerOff(){
		jdbcTemplate.update("ALTER TABLE public.first_table DISABLE TRIGGER USER");
		jdbcTemplate.update("insert into public.first_table (id, field1) values (157, 'd3')");
		jdbcTemplate.update("ALTER TABLE public.first_table ENABLE TRIGGER USER");
		jdbcTemplate.update("insert into public.first_table (id, field1) values (158, 'd3')");
	}

}
