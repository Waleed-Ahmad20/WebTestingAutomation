package com.reqres;

import javax.swing.*;

import com.reqres.payloads.ApiPayloads;
import com.reqres.utils.RestClient;

import java.awt.*;
import java.awt.event.*;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserApiTests {

    private static JTextArea resultArea;

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("User API Tests");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));

        JButton listUsersButton = new JButton("Test List Users");
        listUsersButton.addActionListener(e -> testListUsers());
        buttonPanel.add(listUsersButton);

        JButton createUserButton = new JButton("Test Create User");
        createUserButton.addActionListener(e -> testCreateUser());
        buttonPanel.add(createUserButton);

        JButton updateUserButton = new JButton("Test Update User");
        updateUserButton.addActionListener(e -> testUpdateUser());
        buttonPanel.add(updateUserButton);

        JButton deleteUserButton = new JButton("Test Delete User");
        deleteUserButton.addActionListener(e -> testDeleteUser());
        buttonPanel.add(deleteUserButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        frame.setLayout(new BorderLayout(10, 10));
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static void testListUsers() {
        String url = "https://reqres.in/api/users?page=2";
        Response response = RestClient.performGet(url);

        String input = "URL: " + url;
        String output = "Response Code: " + response.getStatusCode() + "\n" +
                        "Response Body: " + response.getBody().asString();
        String status;

        try {
            assertEquals(200, response.getStatusCode());
            status = "Passed";
        } catch (AssertionError e) {
            status = "Failed";
            output += "\nAssertion Error: " + e.getMessage();
        }

        String message = "=== Test: List Users ===\n" +
                         "Input:\n" + input + "\n\n" +
                         "Output:\n" + output + "\n" +
                         "Test Status: " + status + "\n\n";

        resultArea.append(message);
    }

    public static void testCreateUser() {
        String url = "https://reqres.in/api/users";
        String payload = ApiPayloads.createUserPayload(
                "1",
                "george.bluth@reqres.in",
                "George",
                "Bluth",
                "https://reqres.in/img/faces/1-image.jpg",
                "https://contentcaddy.io",
                "Let Content Caddy generate content for you."
        );

        Response response = RestClient.performPost(url, payload);

        String input = "URL: " + url + "\nPayload: " + payload;
        String output = "Response Code: " + response.getStatusCode() + "\n" +
                        "Response Body: " + response.getBody().asString();
        String status;

        try {
            assertEquals(201, response.getStatusCode());
            status = "Passed";
        } catch (AssertionError e) {
            status = "Failed";
            output += "\nAssertion Error: " + e.getMessage();
        }

        String message = "=== Test: Create User ===\n" +
                         "Input:\n" + input + "\n\n" +
                         "Output:\n" + output + "\n" +
                         "Test Status: " + status + "\n\n";

        resultArea.append(message);
    }

    public static void testUpdateUser() {
        String url = "https://reqres.in/api/users/2";
        String payload = ApiPayloads.createUserPayload(
                "2",
                "janet.weaver@reqres.in",
                "Janet",
                "Weaver",
                "https://reqres.in/img/faces/2-image.jpg",
                "https://contentcaddy.io",
                "Let Content Caddy generate content for you."
        );

        Response response = RestClient.performPut(url, payload);

        String input = "URL: " + url + "\nPayload: " + payload;
        String output = "Response Code: " + response.getStatusCode() + "\n" +
                        "Response Body: " + response.getBody().asString();
        String status;

        try {
            assertEquals(200, response.getStatusCode());
            status = "Passed";
        } catch (AssertionError e) {
            status = "Failed";
            output += "\nAssertion Error: " + e.getMessage();
        }

        String message = "=== Test: Update User ===\n" +
                         "Input:\n" + input + "\n\n" +
                         "Output:\n" + output + "\n" +
                         "Test Status: " + status + "\n\n";

        resultArea.append(message);
    }

    public static void testDeleteUser() {
        String url = "https://reqres.in/api/users/2";

        Response response = RestClient.performDelete(url);

        String input = "URL: " + url;
        String output = "Response Code: " + response.getStatusCode();
        String status;

        try {
            assertEquals(204, response.getStatusCode());
            status = "Passed";
        } catch (AssertionError e) {
            status = "Failed";
            output += "\nAssertion Error: " + e.getMessage();
        }

        String message = "=== Test: Delete User ===\n" +
                         "Input:\n" + input + "\n\n" +
                         "Output:\n" + output + "\n" +
                         "Test Status: " + status + "\n\n";

        resultArea.append(message);
    }
}