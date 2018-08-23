package ru.ipolynkina.server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import ru.ipolynkina.server.db.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManagerTest {

    private static DBManager dbManager;

    @BeforeClass
    public static void init() {
        dbManager = new DBManager("limiter");
    }

    @Test
    public void testDataProgram() {
        String[] correctValues = {"GraphsForSapHR", "PomidoroManager", "SettingsGenerator", "Converter", "Limiter"};
        int counter = 0;

        try (ResultSet resultSet = dbManager.executeQuery("SELECT * FROM program")) {
            while(resultSet.next()) {
                Assert.assertEquals(resultSet.getInt(1), counter + 1);
                Assert.assertEquals(resultSet.getString(2).trim(), correctValues[counter++]);
            }
        } catch(SQLException exc) {
            Assert.fail();
        }
    }

    @Test
    public void testDataIsFree() {
        String[] correctValues = {"true", "false"};
        int counter = 0;

        try (ResultSet resultSet = dbManager.executeQuery("SELECT * FROM is_free")) {
            while(resultSet.next()) {
                Assert.assertEquals(resultSet.getInt(1), counter + 1);
                Assert.assertEquals(resultSet.getString(2).trim(), correctValues[counter++]);
            }
        } catch(SQLException exc) {
            Assert.fail();
        }
    }

    @Test
    public void testDataVersionAmount() {
        try (ResultSet resultSet = dbManager.executeQuery("SELECT * FROM version")) {
            int counter = 0;
            while(resultSet.next()) counter++;
            Assert.assertEquals(counter, 8);
        } catch(SQLException exc) {
            Assert.fail();
        }
    }

    @Test
    public void testDataVersionValue() {
        try (ResultSet resultSet = dbManager.executeQuery("SELECT * FROM version WHERE id_program = 5")) {
            while(resultSet.next()) {
                Assert.assertEquals(resultSet.getInt(1), 8);
                Assert.assertEquals(resultSet.getString(2).trim(), "1.0.1");
                Assert.assertEquals(resultSet.getInt(3), 5);
                Assert.assertEquals(resultSet.getInt(4), 2);
            }
        } catch(SQLException exc) {
            Assert.fail();
        }
    }

    @AfterClass
    public static void drop() throws SQLException {
        try {
            dbManager.executeUpdate("DROP TABLE version");
            dbManager.executeUpdate("DROP TABLE program");
            dbManager.executeUpdate("DROP TABLE is_free");
            dbManager.close();
        } catch(SQLException exc) {
            Assert.fail();
        }
    }
}
