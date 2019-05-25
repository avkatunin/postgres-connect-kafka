package ru.andreykatunin.postgresconnect;

import com.impossibl.postgres.api.jdbc.PGConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.andreykatunin.postgresconnect.postgres.PGListener;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class PostgresConnectApplication {

	@Autowired
	public DataSource dataSource;

	@Autowired
	public PGListener pgListener;

	@Autowired
	public PGConnection connection;

	@Value(value = "${listener.channel}")
	public String channel;

	public static void main(String[] args) {
		SpringApplication.run(PostgresConnectApplication.class, args);
	}

	@PostConstruct
	public void init() throws SQLException {

		connection = dataSource.getConnection().unwrap(PGConnection.class);
		System.out.println(dataSource.toString());
		connection.addNotificationListener(pgListener);

		Statement stmt = connection.createStatement();
		stmt.execute("LISTEN " + channel);
		stmt.close();
	}

}
