package ru.ipolynkina.entity;

public class ProgramEntity {

    private int versionId;
    private String versionText;
    private String programName;
    private boolean isFree;

    public ProgramEntity(int versionId, String versionText, String programName, boolean isFree) {
        this.versionId = versionId;
        this.versionText = versionText;
        this.programName = programName;
        this.isFree = isFree;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getVersionText() {
        return versionText;
    }

    public void setVersionText(String versionText) {
        this.versionText = versionText;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean free) {
        isFree = free;
    }

    @Override
    public String toString() {
        return versionId + " " + versionText + " " + programName + " " + isFree + "\n";
    }
}
