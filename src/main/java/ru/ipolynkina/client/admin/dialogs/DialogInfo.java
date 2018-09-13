package ru.ipolynkina.client.admin.dialogs;

import ru.ipolynkina.entity.ProgramEntity;

public class DialogInfo {

    private static boolean  successfulDialog;
    private static ProgramEntity programEntity;

    public DialogInfo() {
        successfulDialog = false;
        programEntity = new ProgramEntity(0, "", "", false);
    }

    public static void setDefaultInfo() {
        successfulDialog = false;
        programEntity = new ProgramEntity(0, "", "", false);
    }

    public static void setStatusDialog(boolean statusDialog) {
        successfulDialog = statusDialog;
    }

    public static void setProgramEntity(ProgramEntity entity) {
        programEntity = entity;
    }

    public static boolean isSuccessful() {
        return successfulDialog;
    }

    public static ProgramEntity getProgramEntity() {
        return programEntity;
    }
}
