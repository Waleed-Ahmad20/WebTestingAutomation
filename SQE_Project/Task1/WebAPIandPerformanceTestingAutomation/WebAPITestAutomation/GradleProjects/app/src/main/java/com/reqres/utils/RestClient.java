package com.reqres.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestClient {

    public static Response performGet(String url) {
        return RestAssured
                .given()
                .get(url)
                .then()
                .extract()
                .response();
    }

    public static Response performPost(String url, String payload) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post(url)
                .then()
                .extract()
                .response();
    }

    public static Response performPut(String url, String payload) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(payload)
                .put(url)
                .then()
                .extract()
                .response();
    }

    public static Response performDelete(String url) {
        return RestAssured
                .given()
                .delete(url)
                .then()
                .extract()
                .response();
    }
}