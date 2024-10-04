package org.ecosyssimulator;

import org.ecosyssimulator.util.SimpleHttpServer;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        try {
            SimpleHttpServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
