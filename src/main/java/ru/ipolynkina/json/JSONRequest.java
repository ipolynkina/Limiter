package ru.ipolynkina.json;

import org.json.JSONObject;

public class JSONRequest {

    private JSONObject jsonObject;

    public JSONRequest(String profile) {
        jsonObject = new JSONObject();
        jsonObject.put("profile", profile);
    }

    public void addPassword(String password) {
        jsonObject.put("password", password);
    }

    public void addCommand(String command) {
        jsonObject.put("command", command);
    }

    public void addVersionId(int versionId) {
        jsonObject.put("versionId", versionId);
    }

    public void addProgram(String programName) {
        jsonObject.put("programName", programName);
    }

    public void addVersion(String versionText) {
        jsonObject.put("versionText", versionText);
    }

    public void addIsFree(Boolean isFree) {
        jsonObject.put("isFree", isFree);
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
