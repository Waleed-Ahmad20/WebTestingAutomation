package com.reqres.payloads;

import org.json.JSONObject;

public class ApiPayloads {

    public static String createUserPayload(String id, String email, String firstName, String lastName, String avatar, String supportUrl, String supportText) {
        JSONObject payload = new JSONObject();
        
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("email", email);
        data.put("first_name", firstName);
        data.put("last_name", lastName);
        data.put("avatar", avatar);
        
        JSONObject support = new JSONObject();
        support.put("url", supportUrl);
        support.put("text", supportText);
        
        payload.put("data", data);
        payload.put("support", support);
        
        return payload.toString();
    }

    public static String createResourcePayload(String id, String name, String year, String color, String pantoneValue, String supportUrl, String supportText) {
        JSONObject payload = new JSONObject();
        
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("name", name);
        data.put("year", year);
        data.put("color", color);
        data.put("pantone_value", pantoneValue);
        
        JSONObject support = new JSONObject();
        support.put("url", supportUrl);
        support.put("text", supportText);
        
        payload.put("data", data);
        payload.put("support", support);
        
        return payload.toString();
    }
}
