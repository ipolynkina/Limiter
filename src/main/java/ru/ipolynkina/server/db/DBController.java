package ru.ipolynkina.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ru.ipolynkina.entity.ProgramEntity;

public class DBController {

    private static final Logger LOGGER = LogManager.getLogger("DB");
    private DBManager dbManager;

    public DBController(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<ProgramEntity> selectAllVersions() {
        List<ProgramEntity> programs = new ArrayList<>();
        try(ResultSet resultSet = dbManager.executeQuery("SELECT * FROM version")) {
            while(resultSet.next()) {
                int versionId = resultSet.getInt(1);
                String versionText = resultSet.getString(2);
                String programName = selectProgramName(resultSet.getInt(3));
                boolean isFree = selectIsFreeText(resultSet.getInt(4));
                programs.add(new ProgramEntity(versionId, versionText, programName, isFree));
            }
        } catch(SQLException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }

        LOGGER.info("select all: " + programs.size());
        return programs;
    }

    public ProgramEntity selectProgramByParameters(String program, String version) {
        int versionId = 0;
        String versionText = "";
        String programName = "";
        boolean isFree = false;

        try(ResultSet resultSet = dbManager.executeQuery("SELECT * FROM version, program " +
                "WHERE version_text = '" + version + "' " +
                "AND version.program_id = program.program_id " +
                "AND program.program_name = '" + program + "'")) {

            while(resultSet.next()) {
                versionId = resultSet.getInt(1);
                versionText = resultSet.getString(2);
                programName = selectProgramName(resultSet.getInt(3));
                isFree = selectIsFreeText(resultSet.getInt(4));
            }
        } catch(SQLException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }

        LOGGER.info("select by parameters: " + new ProgramEntity(versionId, versionText, programName, isFree));
        return new ProgramEntity(versionId, versionText, programName, isFree);
    }

    public void addProgramVersion(String programName, String versionText, boolean isFree) {
        try {

            if(!programNameExists(programName)) insertProgramName(programName);

            ResultSet resultSet = dbManager.executeQuery(
                    "SELECT program_id FROM program WHERE program_name = '" + programName + "'");
            resultSet.next();
            int programId = resultSet.getInt(1);
            int isFreeId = isFree ? 1 : 2;
            dbManager.executeUpdate("INSERT INTO version (version_text, program_id, is_free_id) " +
                    "VALUES('" + versionText + "', " + programId + ", " + isFreeId + ")");

            resultSet.close();
            LOGGER.info("add: " + programName + " " + versionText + " " + isFree);

        } catch(SQLException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    public void editProgramVersion(int versionId, String programName, String versionText, boolean isFree) {
        try {
            if(!programNameExists(programName)) insertProgramName(programName);

            ResultSet resultSet = dbManager.executeQuery(
                    "SELECT program_id FROM program WHERE program_name = '" + programName + "'");
            resultSet.next();
            int programId = resultSet.getInt(1);
            int isFreeId = isFree ? 1 : 2;
            dbManager.executeUpdate("UPDATE version SET version_text = '" + versionText + "', " +
                    "program_id = " + programId + ", " +
                    "is_free_id = " + isFreeId + " " +
                    "WHERE version_id = " + versionId);

            resultSet.close();
            LOGGER.info("edit: " + versionId + " " + programName + " " + versionText + " " + isFree);

        } catch(SQLException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    public void deleteProgramVersion(int versionId) {
        try {
            dbManager.executeUpdate("DELETE FROM version WHERE version_id =  " + versionId);
            LOGGER.info("delete version_id: " + versionId);
        } catch(SQLException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    private boolean programNameExists(String programName) {
        try(ResultSet resultSet = dbManager.executeQuery(
                "SELECT program_id FROM program WHERE program_name = '" + programName + "'")) {
            resultSet.next();
            resultSet.getInt(1);
        } catch(SQLException exc) {
            // Ignore it. Program Name does not exist.
            return false;
        }
        return true;
    }

    private void insertProgramName(String programName) {
        try {
            dbManager.executeUpdate("INSERT INTO program (program_name) VALUES('" + programName + "')");
            LOGGER.info("insert program_name: " + programName);
        } catch(SQLException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    private String selectProgramName(int programId) {
        String programName = "";
        try(ResultSet resultSet = dbManager.executeQuery(
                "SELECT program_name FROM program WHERE program_id = " + programId )) {
            resultSet.next();
            programName = resultSet.getString(1);
        } catch(SQLException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
        return programName;
    }

    private boolean selectIsFreeText(int isFreeId) {
        boolean isFree = false;
        try(ResultSet resultSet = dbManager.executeQuery(
                "SELECT is_free_text FROM is_free WHERE is_free_id = " + isFreeId)) {
            resultSet.next();
            isFree = resultSet.getString(1).trim().equals("true");
        } catch(SQLException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
        return isFree;
    }
}
