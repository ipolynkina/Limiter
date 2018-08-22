package ru.ipolynkina.server;

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
            } catch(SQLException exc) {
                exc.printStackTrace();
            }
        }

        try {
            addDataProgram();
            addDataIsFree();
            addDataVersion();
        } catch(SQLException exc) {
            exc.printStackTrace();
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
                "id_program INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "name_program CHAR(64)," +
                "PRIMARY KEY(id_program))");
    }

    private void createIsFreeTable() throws SQLException {
        executeUpdate("CREATE TABLE is_free(" +
                "id_is_free INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "text_is_free CHAR(5)," +
                "PRIMARY KEY(id_is_free))");
    }

    private void createVersionTable() throws SQLException {
        executeUpdate("CREATE TABLE version(" +
                "id_version INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "text_version CHAR(16)," +
                "id_program INTEGER," +
                "id_is_free INTEGER," +
                "FOREIGN KEY(id_program) REFERENCES program(id_program), " +
                "FOREIGN KEY(id_is_free) REFERENCES is_free(id_is_free))");
    }

    private void addDataProgram() throws SQLException {
       executeUpdate("INSERT INTO program (name_program) VALUES('GraphsForSapHR')");
        executeUpdate("INSERT INTO program (name_program) VALUES('PomidoroManager')");
        executeUpdate("INSERT INTO program (name_program) VALUES('SettingsGenerator')");
        executeUpdate("INSERT INTO program (name_program) VALUES('Converter')");
        executeUpdate("INSERT INTO program (name_program) VALUES('Limiter')");
    }

    private void addDataIsFree() throws SQLException {
        executeUpdate("INSERT INTO is_free (text_is_free) VALUES(true)");
        executeUpdate("INSERT INTO is_free (text_is_free) VALUES(false)");
    }

    private void addDataVersion() throws SQLException {
        executeUpdate("INSERT INTO version (text_version, id_program, id_is_free) VALUES('1 beta', 1, 1)");
        executeUpdate("INSERT INTO version (text_version, id_program, id_is_free) VALUES('2', 1, 1)");
        executeUpdate("INSERT INTO version (text_version, id_program, id_is_free) VALUES('3', 1, 2)");

        executeUpdate("INSERT INTO version (text_version, id_program, id_is_free) VALUES('1', 2, 1)");
        executeUpdate("INSERT INTO version (text_version, id_program, id_is_free) VALUES('2', 2, 1)");

        executeUpdate("INSERT INTO version (text_version, id_program, id_is_free) VALUES('1 beta', 3, 1)");

        executeUpdate("INSERT INTO version (text_version, id_program, id_is_free) VALUES('1', 4, 2)");

        executeUpdate("INSERT INTO version (text_version, id_program, id_is_free) VALUES('1.0.1', 5, 2)");
    }
}
