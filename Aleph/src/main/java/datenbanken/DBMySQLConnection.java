package datenbanken;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DBMySQLConnection{

	
	private Connection conn;
	
	
	/**
	 * Erstellt eine MySQL Conection.
	 * 
	 * @param database
	 * @param host
	 * @param port
	 * @param user
	 * @param password
	 */
	public DBMySQLConnection(String database, String host, String port, String user, String password)
	{System.out.println(database + "\t"+host + "\t"+port+"\t"+user+"\t"+password);
		try
		{
			// Datenbanktreiber f�r ODBC Schnittstellen laden.
			Class.forName("com.mysql.jdbc.Driver");
		 
		    // Verbindung zur ODBC-Datenbank
		     conn = DriverManager.getConnection("jdbc:mysql://" + host + ":"
		          + port + "/" + database + "?" + "user=" + user + "&"
		          + "password=" + password);
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Konstruktur. Treiber nicht gefunden");
		}
		catch(SQLException e)
		{
			System.out.println("Konstruktor. Connect nicht moeglich");
		}
	}
	
	
	/**
	 * Schliesst die Datenbank Verbindung.
	 */
	public void close()
	{
		try
		{
			conn.close();
		}
		catch(SQLException e)
		{
			System.out.println("Close von Connection fehlgeschlagen.");
		}
	}
	
	
	/**
	 * Gibt alle Zeilen der Tabelle table zurueck, welche in der Spalte key_column den Wert key haben.
	 * Die einzelnen Werte der Zeile in der ArrayList, werden durch ";" getrennt.
	 * 
	 * @param table
	 * @param key_column
	 * @param key
	 * @param header
	 * @return
	 */
	public ArrayList<String> getData(String table, String[] key_column, String[] key, String header)
	{
		try
		{
			if(!tableExist(table))
				return null;
				
			String sql = "select * from " + table + " where " + constructANDStatement(key_column, key);
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			ArrayList<String> list = new ArrayList<String>();
			String[] h = header.split(";");
			while(rs.next())
			{
				String data = rs.getString(h[0]);
				
				for(int i=1; i<h.length; i++)
					data += ";" + rs.getString(h[i]);
				
				list.add(data);
			}
			
			statement.close();
			rs.close();
			
			if(list.size() < 1)
				return null;
			
			return list;
		
		}
		catch(SQLException e)
		{
			System.out.println("Fehler in getData.");
		}
		
		return null;
	}
	
	
	/**
	 * Schreibt die Zeile newRow in die Tabelle table.
	 * Die Einzelnen Werte der Zeile sind durch ";" getrennt.
	 * 
	 * Erstellt neue Tabelle table, falls table nicht existiert.
	 * 
	 * @param table
	 * @param newRow
	 * @param header
	 */
	public void setData(String table, String newRow, String header)
	{				
		try
		{
			if(!tableExist(table))
				createTable(table, header);
			
			String sql = "insert into " + table + " (" + semikolonZuKomma(header, "") + ")" + " values " + "(" + semikolonZuKomma(hochKomma(newRow), "") + ")";
				System.out.println(sql);
			PreparedStatement statment = conn.prepareStatement(sql);
			statment.executeUpdate();
			statment.close();
		}
		catch(SQLException e)
		{
			System.out.println("Fehler in setData.");
		}
	}
	
	
	/**
	 * Loescht alle Zeilen in table, welche den Wert key in der Spalte key_column haben.
	 * 
	 * @param table
	 * @param key_column
	 * @param key
	 */
	public void removeRow(String table, String[] key_column, String[] key)
	{
		try
		{
			if(tableExist(table))
			{
				String sql = "delete from "+ table + " where " + constructANDStatement(key_column, key);
				Statement query = conn.createStatement();
				query.executeUpdate(sql);
				query.close();
			}
		}
		catch(SQLException e)
		{
			System.out.println("Fehler in removeRow.");
		}
	}
	
	
	/**
	 * Gibt alle Elemente einer Spalte columnName einer Tabelle table zurueck.
	 * 
	 * @param table
	 * @param columnName
	 * @return
	 */
	public ArrayList<String> getColumn(String table, String columnName)
	{
		try
		{
			if(tableExist(table))
			{
				String sql = "select " + columnName + " from " + table;
				
				Statement query = conn.createStatement();
				ResultSet rs = query.executeQuery(sql);
			
				ArrayList<String> list = new ArrayList<String>();
				while(rs.next())
					list.add(rs.getString(columnName));
				
				query.close();
				rs.close();
				
				return list;
			}
			
			return null;
		}
		catch(SQLException e)
		{
			System.out.println("Fehler in getCloumn.");
		}
		
		return null;
	}
	
	
	/**
	 * Loescht die Tabelle table.
	 * 
	 * @param table
	 * @throws SQLException
	 */
	public void clearDB(String table) throws SQLException
	{
		if(tableExist(table))
		{
			String sql = "drop table " + table;
			Statement statement = conn.createStatement();
			statement.executeUpdate(sql);
			statement.close();
		}
	 }
	
	
	/**
	 * Kontrolliert, ob eine Tabelle namens table existiert.
	 * 
	 * @param table
	 * @return
	 */
	private boolean tableExist(String table)
	{
		try
		{
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery("show tables like '" + table + "'");
			boolean b = false;
			
			if(rs.next())
				b = true;
			  
			query.close();
			rs.close();
			
			return b;
		}
		catch(SQLException e)
		{
			System.out.println("Fehler in tableExist");
		}
		
		return false;
	}
	
	
	/**
	 * Erstellt eine Tabelle namens table mit Header header.
	 * 
	 * @param table
	 * @param header
	 */
	private void createTable(String table, String header)
	{
		try
		{
			String sql = "create table " + table + " (" + semikolonZuKomma(header, " text (100) ") + ")";
			
			System.out.println(sql);
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		}
		catch(SQLException e)
		{
			System.out.println("Fehler in createTable.");
		}
	}
	
	
	/**
	 * Der String semikolon enthaelt Daten, welchen durch ";" getrennt sind.
	 * In dieser Methode wird ein String analog zu semikolon erstellt, welcher anstelle des ";" ein "," + norm gesetzt.
	 * 
	 * @param semikolon
	 * @param norm
	 * @return
	 */
	private String semikolonZuKomma(String semikolon, String norm)
	{
		String[] data = semikolon.split(";");
		String komma = data[0] + norm;
		
		for(int i=1; i<data.length; i++)
			komma += "," + data[i] + norm;
		
		return komma;
	}

	
	
	/**
	 * Erstellt einen String der Form:
	 * key_column[0]=key[0] and key_column[1]=key[1] and ... key_column[n]=key[n]
	 * 
	 * @param key_column
	 * @param key
	 * @return
	 */
	private String constructANDStatement(String[] key_column, String[] key)
	{
		String statment = key_column[0] + "='" + key[0] + "'";
		
		for(int i=1; i<key.length; i++)
			statment += " and " + key_column[i] + "='" + key[i] + "'";
		
		return statment;
	}

	
	/**
	 * s ist ein String, welcher Daten durch "," trennt.
	 * Es wird ein String zurueckgegeben, welcher die Daten durch Hochkommata umrahmt, dass Trennzeichen wirv beibehalten.
	 * 
	 * @param s
	 * @return
	 */
	private String hochKomma(String s)
	{
		String[] daten = s.split(";");
		String hochKomma = "'" + daten[0] + "'";
		
		for(int i=1; i<daten.length; i++)
			hochKomma += ";'" + daten[i] + "'";
		
		
		return hochKomma;
	}

}
