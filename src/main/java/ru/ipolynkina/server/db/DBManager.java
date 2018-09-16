package ru.ipolynkina.server.db;

import java.sql.*;

public class DBManager {

    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String url = "jdbc:derby:";

    private String dbName;
    private static Connection connection;

    // TODO: logger
    public DBManager(String dbName) {
        this.dbName = dbName;

        if(!dbExist()) {
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url + dbName + ";create=true");
            } catch(ClassNotFoundException | SQLException exc) {
                exc.printStackTrace();
            }
        }

        if(!tablesExist()) {
            try {
                createProgramTable();
                createIsFreeTable();
                createVersionTable();

                addDataProgram();
                addDataIsFree();
                addDataVersion();
            } catch(SQLException exc) {
                exc.printStackTrace();
            }
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

    private boolean dbExist() {
        boolean dbExist = false;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url + dbName);
            dbExist = true;
        } catch(Exception exc) {
            // Ignore it. Database does not exist.
        }
        return dbExist;
    }

    private boolean tablesExist() {
        boolean tableExist = false;
        try (ResultSet resultSet = executeQuery("SELECT * FROM program")) {
            if(resultSet.next()) tableExist = true;
        } catch(Exception exc) {
            // Ignore it. Tables does not exist.
        }
        return tableExist;
    }

    //  TODO: private
    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    //  TODO: private
    public void executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    private void createProgramTable() throws SQLException {
        executeUpdate("CREATE TABLE program(" +
                "program_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "program_name VARCHAR(64)," +
                "PRIMARY KEY(program_id))");
    }

    private void createIsFreeTable() throws SQLException {
        executeUpdate("CREATE TABLE is_free(" +
                "is_free_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "is_free_text VARCHAR(5)," +
                "PRIMARY KEY(is_free_id))");
    }

    private void createVersionTable() throws SQLException {
        executeUpdate("CREATE TABLE version(" +
                "version_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "version_text VARCHAR(16)," +
                "program_id INTEGER," +
                "is_free_id INTEGER," +
                "FOREIGN KEY(program_id) REFERENCES program(program_id), " +
                "FOREIGN KEY(is_free_id) REFERENCES is_free(is_free_id))");
    }

    private void addDataProgram() throws SQLException {
        executeUpdate("INSERT INTO program (program_name) VALUES('GraphsForSapHR')");
        executeUpdate("INSERT INTO program (program_name) VALUES('PomidoroManager')");
        executeUpdate("INSERT INTO program (program_name) VALUES('SettingsGenerator')");
        executeUpdate("INSERT INTO program (program_name) VALUES('Converter')");
        executeUpdate("INSERT INTO program (program_name) VALUES('Limiter')");
    }

    private void addDataIsFree() throws SQLException {
        executeUpdate("INSERT INTO is_free (is_free_text) VALUES(true)");
        executeUpdate("INSERT INTO is_free (is_free_text) VALUES(false)");
    }

    private void addDataVersion() throws SQLException {
        executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) VALUES('1 beta', 1, 1)");
        executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) VALUES('2', 1, 1)");
        executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) VALUES('3', 1, 2)");

        executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) VALUES('1', 2, 1)");
        executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) VALUES('2', 2, 1)");

        executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) VALUES('1 beta', 3, 1)");

        executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) VALUES('1', 4, 2)");

        executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) VALUES('1.0.1', 5, 2)");
    }
}
