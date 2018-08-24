package ru.ipolynkina.server.entity;

public class ProgramEntity {

    private int idVersion;
    private String textVersion;
    private String nameProgram;
    private boolean isFree;

    public ProgramEntity(int idVersion, String textVersion, String nameProgram, boolean isFree) {
        this.idVersion = idVersion;
        this.textVersion = textVersion;
        this.nameProgram = nameProgram;
        this.isFree = isFree;
    }

    public int getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(int idVersion) {
        this.idVersion = idVersion;
    }

    public String getTextVersion() {
        return textVersion;
    }

    public void setTextVersion(String textVersion) {
        this.textVersion = textVersion;
    }

    public String getNameProgram() {
        return nameProgram;
    }

    public void setNameProgram(String nameProgram) {
        this.nameProgram = nameProgram;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean free) {
        isFree = free;
    }

    @Override
    public String toString() {
        return idVersion + " " + textVersion + " " + nameProgram + " " + isFree + "\n";
    }
}
