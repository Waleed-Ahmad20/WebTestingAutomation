package com.reqres;

import javax.swing.*;

import com.reqres.payloads.ApiPayloads;
import com.reqres.utils.RestClient;

import java.awt.*;
import java.awt.event.*;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceApiTests {

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

        JFrame frame = new JFrame("Resource API Tests");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));

        JButton listResourcesButton = new JButton("Test List Resources");
        listResourcesButton.addActionListener(e -> testListResources());
        buttonPanel.add(listResourcesButton);

        JButton createResourceButton = new JButton("Test Create Resource");
        createResourceButton.addActionListener(e -> testCreateResource());
        buttonPanel.add(createResourceButton);

        JButton updateResourceButton = new JButton("Test Update Resource");
        updateResourceButton.addActionListener(e -> testUpdateResource());
        buttonPanel.add(updateResourceButton);

        JButton deleteResourceButton = new JButton("Test Delete Resource");
        deleteResourceButton.addActionListener(e -> testDeleteResource());
        buttonPanel.add(deleteResourceButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        frame.setLayout(new BorderLayout(10, 10));
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static void testListResources() {
        String url = "https://reqres.in/api/unknown";
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

        String message = "=== Test: List Resources ===\n" +
                         "Input:\n" + input + "\n\n" +
                         "Output:\n" + output + "\n" +
                         "Test Status: " + status + "\n\n";

        resultArea.append(message);
    }

    public static void testCreateResource() {
        String url = "https://reqres.in/api/unknown";
        String payload = ApiPayloads.createResourcePayload(
            "1",
            "Green Widget",
            "2023",
            "#00FF00",
            "15-4020",
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

        String message = "=== Test: Create Resource ===\n" +
                         "Input:\n" + input + "\n\n" +
                         "Output:\n" + output + "\n" +
                         "Test Status: " + status + "\n\n";

        resultArea.append(message);
    }

    public static void testUpdateResource() {
        String url = "https://reqres.in/api/unknown/2";
        String payload = ApiPayloads.createResourcePayload(
            "2",
            "Blue Widget",
            "2023",
            "#0000FF",
            "19-4057",
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

        String message = "=== Test: Update Resource ===\n" +
                         "Input:\n" + input + "\n\n" +
                         "Output:\n" + output + "\n" +
                         "Test Status: " + status + "\n\n";

        resultArea.append(message);
    }

    public static void testDeleteResource() {
        String url = "https://reqres.in/api/unknown/2";

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

        String message = "=== Test: Delete Resource ===\n" +
                         "Input:\n" + input + "\n\n" +
                         "Output:\n" + output + "\n" +
                         "Test Status: " + status + "\n\n";

        resultArea.append(message);
    }
}