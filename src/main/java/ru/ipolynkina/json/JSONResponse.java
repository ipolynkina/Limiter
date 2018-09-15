package ru.ipolynkina.json;

import org.json.JSONArray;
import org.json.JSONObject;

import ru.ipolynkina.entity.ProgramEntity;

public class JSONResponse {

    private JSONArray array;

    public JSONResponse(ProgramEntity entity) {
        array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("versionId", entity.getVersionId());
        jsonObject.put("versionText", entity.getVersionText());
        jsonObject.put("programName", entity.getProgramName());
        jsonObject.put("isFree", entity.getIsFree());
        array.put(jsonObject);
    }

    public void addEntity(ProgramEntity entity) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("versionId", entity.getVersionId());
        jsonObject.put("versionText", entity.getVersionText());
        jsonObject.put("programName", entity.getProgramName());
        jsonObject.put("isFree", entity.getIsFree());
        array.put(jsonObject);
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
