package ru.ipolynkina.server.db;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBController {

    // TODO: logger
    private static final Logger LOGGER = LogManager.getLogger(DBController.class.getSimpleName());
    private DBManager dbManager;

    public DBController(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean isFree(String name_program, String text_version) {
        boolean isFree = false;

        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT id_is_free FROM version, program " +
                    "WHERE RTRIM(text_version) = '" + text_version + "' " +
                    "AND version.id_program = program.id_program " +
                    "AND RTRIM(program.name_program) = '" + name_program + "'");

            try {
                resultSet.next();
                isFree = resultSet.getInt(1) == 1;
            } catch(SQLException exc) {
                exc.printStackTrace();
            }

            resultSet.close();
            System.out.println(name_program + ";" + text_version + " : " + isFree);

        } catch(SQLException exc) {
            exc.printStackTrace();
        }

        return isFree;
    }
}
