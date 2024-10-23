package org.ecosyssimulator.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.ecosyssimulator.controller.EcosystemController;
import org.ecosyssimulator.view.ViewRenderer;
import org.ecosyssimulator.model.Species;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimpleHttpServer {
    private static EcosystemController controller;
    private static ViewRenderer renderer;

    public static void startServer() throws IOException {
        controller = new EcosystemController();
        renderer = new ViewRenderer();
        initializeEcosystem();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", new DashboardHandler());
        server.createContext("/simulate-day", new SimulateDayHandler());
        server.createContext("/simulate-month", new SimulateMonthHandler());
        server.createContext("/reset", new ResetHandler());
        server.createContext("/add-organism", new AddOrganismHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://localhost:8000");
    }

    private static void initializeEcosystem() {
        controller.setEvent("\uD83C\uDF05 Jour 1 : Le soleil se lève sur la simulation. \uD83C\uDF05");
        controller.addOrganism(Species.CAROTTE);
        controller.addOrganism(Species.LAPIN);
        controller.addOrganism(Species.RENARD);
        controller.addOrganism(Species.LOUP);
        controller.setEvent("\uD83C\uDF19 Jour 1 : : Fin de la journée. \uD83C\uDF19");
    }

    private static String renderDashboard() {
        return renderer.render("index",
                "currentDay", controller.getCurrentDay(),
                "speciesStats", controller.getOrganisms(),
                "totalPopulation", controller.getTotalPopulation(), "eventsList", controller.getEvents()
        );
    }

    private static void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        try {
            httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            httpExchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
            httpExchange.getResponseHeaders().set("Cache-Control", "no-cache");

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (IOException e) {
            System.err.println("La connexion a été interrompue : " + e.getMessage());
        } catch (Exception except) {
            except.printStackTrace();
            String errorMessage = "Error: " + except.getMessage();
            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            byte[] errorBytes = errorMessage.getBytes(StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(500, errorBytes.length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(errorBytes);
            }
        }
    }

    static class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = renderDashboard();
            sendResponse(httpExchange, response);
        }
    }

    static class SimulateDayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equals("POST")) {
                controller.simulateDay();
                String response = renderDashboard();
                sendResponse(httpExchange, response);
            } else {
                httpExchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class SimulateMonthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equals("POST")) {
                controller.simulateMonth();
                String response = renderDashboard();
                sendResponse(httpExchange, response);
            } else {
                httpExchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class ResetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if ("POST".equals(httpExchange.getRequestMethod())) {
                controller.resetSimulation();
                initializeEcosystem();
                String response = renderDashboard();
                sendResponse(httpExchange, response);
            } else {
                httpExchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class AddOrganismHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if ("POST".equals(httpExchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                // Extraire l'espèce du formulaire
                String speciesString = formData.split("=")[1];
                Species species = Species.valueOf(speciesString);

                // Ajouter l'organisme
                controller.addOrganism(species);

                // Rediriger vers la page principale
                httpExchange.getResponseHeaders().set("Location", "/");
                httpExchange.sendResponseHeaders(302, -1);
            } else {
                httpExchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }
}

