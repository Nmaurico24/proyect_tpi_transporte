package ar.edu.utnfc.backend.ms_recursos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() throws SQLException {
        assertNotNull(dataSource);
        
        try (Connection connection = dataSource.getConnection()) {
            assertTrue(connection.isValid(2));
            System.out.println("✅ Conexión a PostgreSQL exitosa!");
        }
    }
}