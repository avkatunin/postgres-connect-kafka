package ru.andreykatunin.postgresconnect.postgres;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.impossibl.postgres.jdbc.PGDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class PGConfig {

    @Value(value = "${database.host}")
    String host;

    @Value(value = "${database.port}")
    int port;

    @Value(value = "${database.name}")
    String name;

    @Value(value = "${database.user}")
    String user;

    @Value(value = "${database.password}")
    String password;


    @Bean
    public DataSource dataSource(){
        DataSource dataSource = new PGDataSource();
        ((PGDataSource) dataSource).setHost(host);
        ((PGDataSource) dataSource).setPort(port);
        ((PGDataSource) dataSource).setDatabaseName(name);
        ((PGDataSource) dataSource).setUser(user);
        ((PGDataSource) dataSource).setPassword(password);
        return dataSource;
    }

    @Bean
    public PGConnection connection(){
        try {
            return dataSource().getConnection().unwrap(PGConnection.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
