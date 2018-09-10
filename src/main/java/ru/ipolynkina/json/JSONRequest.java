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

    public void addProgram(String nameProgram) {
        jsonObject.put("nameProgram", nameProgram);
    }

    public void addCommand(String command) {
        jsonObject.put("command", command);
    }

    public void addVersion(String version) {
        jsonObject.put("version", version);
    }

    public void addIdVersion(int idVersion) {
        jsonObject.put("idVersion", idVersion);
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
