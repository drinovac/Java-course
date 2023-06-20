package hr.fer.zemris.java.webserver;

public class Main {

    public static void main(String[] args) {
        SmartHttpServer smartHttpServer = new SmartHttpServer("./config/server.properties");
        smartHttpServer.start();
    }
}
