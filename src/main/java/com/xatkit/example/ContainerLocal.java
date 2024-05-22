package com.xatkit.example;

import java.io.IOException;
import java.util.*;
import java.lang.Thread;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.xatkit.example.DemoBot;
import com.xatkit.example.helpers.Config ;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

public final class ContainerLocal {

  private static final int PORT = 5569;
  public  static Config current = new Config(/* default */);

  // @works well tested with the following command : curl http://localhost:5569/restart && curl http://localhost:5000/ping
  public static void main(String[] args) throws Exception {
    System.out.println("@Running web service @" + PORT);
    System.out.println("@Threads currently running " + Thread.getAllStackTraces().keySet());

    DemoBot.main(new String[]{});
    System.out.println("@Running instantiating and initializing demoBot for the demoContainer.");

    HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
    server.createContext("/restart", new restartHandler());

    server.setExecutor(null); // creates a default executor
    server.start();
  }


  static class restartHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      System.out.println("@handling restartHandler");

      DemoBot.re();

      t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
      if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
        System.out.println("@handling restartHandler @OPTIONS");
        t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
        t.sendResponseHeaders(204, -1);
        return;
      }

      String response = "Restarting the server..."; // how do we check the if the thing failed ?
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();

    }
  }
}
