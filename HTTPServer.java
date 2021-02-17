package com.sql;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class HTTPServer {

	private static Connection connect;
	
	  public static void Servcreate(Connection connect) throws IOException {
		  HTTPServer.connect = connect;
	      HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
	      HttpContext context = server.createContext("/");
	      context.setHandler(HTTPServer::handleRequest);
	      server.start();
	  }

	  private static void handleRequest(HttpExchange exchange) throws IOException {
		  String query = "SELECT * FROM T_USER "
		  		+ "LEFT JOIN NIVEAU_GAME ON T_USER.ID_USER = NIVEAU_GAME.ID_USER "
		  		+ "LEFT JOIN PSEUDO ON T_USER.ID_USER = PSEUDO.ID_USER "
		  		+ "LEFT JOIN ROLE_GAME ON PSEUDO.ID_PSEUDO  = ROLE_GAME.ID_PSEUDO "
		  		+ "LEFT JOIN ROLE ON ROLE.ID_RG = ROLE_GAME.ID_RG "
		  		+ "LEFT JOIN PERSONNAGE ON PERSONNAGE.ID_ROLE = ROLE.ID_ROLE WHERE "; 
		  Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
		  if (params.containsKey("game")) {
			  query += "NIVEAU_GAME.ID_GAME = " + params.get("game") + " AND " ;
		  }
		  if (params.containsKey("role")) {
		  query += "ROLE_GAME.ID_RG = " + params.get("role") + " AND " ;
		  }
		  if (params.containsKey("plateforme")) {
		  query += "T_USER.ID_PLATEFORME = " + params.get("plateforme") + " AND ";
		  }
		  if (params.containsKey("personnage")) {
		  query += "PERSONNAGE.ID_PERSO = " + params.get("personnage") + " AND ";
		  }
		  query = query.substring(0,query.length()-" AND ".length());
		  System.out.println(query);
		  
		  try {
			PreparedStatement statement = connect.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			JSONArray result = convert(rs);
			exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
	        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
	        exchange.sendResponseHeaders(200, result.toString().length());


	        Writer out = new OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8);
	        out.write(result.toString());
	        out.flush();
	        out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	  public static Map<String, String> queryToMap(String query) {
		    Map<String, String> result = new HashMap<>();
		    for (String param : query.split("&")) {
		        String[] entry = param.split("=");
		        if (entry.length > 1) {
		            result.put(entry[0], entry[1]);
		        }else{
		            result.put(entry[0], "");
		        }
		    }
		    return result;
		}
	  
	  public static JSONArray convert(ResultSet resultSet) throws Exception {
		  
		    JSONArray jsonArray = new JSONArray();
		 
		    while (resultSet.next()) {
		 
		        int columns = resultSet.getMetaData().getColumnCount();
		        JSONObject obj = new JSONObject();
		 
		        for (int i = 0; i < columns; i++)
		            obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
		 
		        jsonArray.put(obj);
		    }
		    return jsonArray;
		}
	  
	  public static void main(String[] args) {
		  
	  }
}
