package ru.ipolynkina.client.admin.dialogs;

import ru.ipolynkina.entity.ProgramEntity;

public class DialogInfo {

    private static boolean statusIsOk;
    private static ProgramEntity programEntity;

    public DialogInfo() {
        statusIsOk = false;
        programEntity = new ProgramEntity(0, "", "", false);
    }

    public static void setDefaultInfo() {
        statusIsOk = false;
        programEntity = new ProgramEntity(0, "", "", false);
    }

    public static void setStatusIsOk(boolean statusIsOk) {
        DialogInfo.statusIsOk = statusIsOk;
    }

    public static void setProgramEntity(ProgramEntity entity) {
        programEntity = entity;
    }

    public static boolean getStatusIsOk() {
        return statusIsOk;
    }

    public static ProgramEntity getProgramEntity() {
        return programEntity;
    }
}
