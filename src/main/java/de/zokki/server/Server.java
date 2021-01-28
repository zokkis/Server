package de.zokki.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import de.zokki.server.HttpHandler.MainContextHandler;

public class Server {

    public static void main(String[] args) throws IOException {
	System.out.println("Server starting...");

	HttpServer server = HttpServer.create(new InetSocketAddress(1337), 0);
	server.setExecutor(Executors.newCachedThreadPool());
	server.createContext("/", new MainContextHandler());
	server.start();
	System.out.println("Server started...");
    }
}
