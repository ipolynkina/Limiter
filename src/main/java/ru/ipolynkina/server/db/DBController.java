package ru.ipolynkina.server.db;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.ipolynkina.server.entity.ProgramEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<ProgramEntity> getAllProgram() {
        List<ProgramEntity> programs = new ArrayList<>();
        try(ResultSet resultSet = dbManager.executeQuery("SELECT * FROM version")) {
            while(resultSet.next()) {
                int idVersion = resultSet.getInt(1);
                String textVersion = resultSet.getString(2);
                int idProgram = resultSet.getInt(3);
                String nameProgram = getNameProgram(idProgram);
                int idIsFree = resultSet.getInt(4);
                boolean isFree = getIsFree(idIsFree);
                programs.add(new ProgramEntity(idVersion, textVersion, nameProgram, isFree));
            }
        } catch(SQLException exc) {
            exc.printStackTrace();
        }
        return programs;
    }

    private String getNameProgram(int idProgram) {
        String nameProgram = "";
        try(ResultSet resultSet = dbManager.executeQuery("SELECT name_program FROM program " +
                "WHERE id_program = " + idProgram )) {
            resultSet.next();
            nameProgram = resultSet.getString(1);
        } catch(SQLException exc) {
            exc.printStackTrace();
        }
        return nameProgram;
    }

    private boolean getIsFree(int idIsFree) {
        boolean isFree = false;
        try(ResultSet resultSet = dbManager.executeQuery("SELECT text_is_free FROM is_free " +
                "WHERE id_is_free = " + idIsFree)) {
            resultSet.next();
            isFree = resultSet.getString(1).trim().equals("true");
        } catch(SQLException exc) {
            exc.printStackTrace();
        }
        return isFree;
    }
}
