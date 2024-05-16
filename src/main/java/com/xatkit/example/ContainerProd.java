package com.xatkit.example;

import      io.github.cdimascio.dotenv.Dotenv;
import java.io.OutputStream;
import java.io.IOException;
import java.util.*;
import java.lang.Thread;
import java.net.InetSocketAddress;

import com.xatkit.example.DemoBot;
import com.xatkit.example.helpers.Config ;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

public final class ContainerProd {

  private static final int PORT            = 5569;
  private static final String token        = Dotenv.configure().directory(".").filename("env").load().get("token").trim(); 

  private static void ignore(){}
  // @works well tested with the following command : curl http://localhost:5569/restart && curl http://localhost:5000/ping
  public static void main(String[] args) throws Exception {
    System.out.println("@Running web service @" + PORT);

    System.out.println("@Running instantiating and initializing ProdBot for the ProdContainer.");
    ProdBot.main(new String[]{});

    HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
    server.createContext("/restart", new restartHandler());

    server.setExecutor(null); // creates a default executor
    server.start();
  }

  private static HashMap<String, String> parseURI(String uri) {
    if ( uri.contains("?") || uri.contains("api_key") | uri.contains("=") ) {
      uri         = uri.substring("/restart?".length());
      String key  = uri.split("=")[0];
      String pair = uri.split("=")[1];

      HashMap<String, String> ret = new HashMap<>();
      ret.put(key, pair);
      return ret;
    }
    else {
      return null;
    }
  }

  static class restartHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      System.out.println("@handling restartHandler");

      t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
      if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
        System.out.println("@handling restartHandler @OPTIONS");
        t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
        t.sendResponseHeaders(204, -1);
        return;
      }

      OutputStream os = t.getResponseBody();

      if (parseURI(t.getRequestURI().toString()).get("api_key").equals(token)) {
        String response = "Restarting the server..."; // how do we check the if the thing failed ?
        t.sendResponseHeaders(200, response.length());
        os.write(response.getBytes());
      }
      else {
        String response = "Api key is invalid. Contact admin."; // how do we check the if the thing failed ?
        t.sendResponseHeaders(200, response.length());
        os.write(response.getBytes());
      }

      os.close();
    }
  }

}
