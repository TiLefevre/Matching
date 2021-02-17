package com.sql;

import java.sql.SQLException;
import java.io.IOException;
import java.sql.Connection;



public class main 
{

	public static void main(String[] args) throws SQLException, IOException
	{
			Connection connect = null;
			
			try 
			{
				initialize.Charge();
				connect = initialize.getConnection();
				if (connect != null) {
					System.out.println("Connection établie");
					HTTPServer.Servcreate(connect);
							}
				}
			catch(ClassNotFoundException e)
			{
				System.out.println("echec du chargement des drivers");
			}
			catch(SQLException e)
			{
				System.out.println("erreur de type SQL");
				e.printStackTrace();
			}
	}
}
