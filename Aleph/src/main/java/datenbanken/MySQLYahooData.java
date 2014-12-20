package datenbanken;

import java.util.ArrayList;


public class MySQLYahooData {

	
	private String[] dbconfig;
	
	
	/**
	 * Objekt zum einpflegen und auslesen von Yahoo Datensaetze in eine MySql Tabelle.
	 * 
	 * @param dbconfig
	 */
	public MySQLYahooData(String[] dbconfig)
	{
		this.dbconfig = dbconfig;
	}
	
	
	/**
	 * Schreibt eine neue Zeile in die Datentabelle.
	 * 
	 * @param tablename
	 * @param row
	 */
	public void setData(String tablename, String[] row)
	{
		DBMySQLConnection c = getConnection();
		
		String newRow = row[0];
		for(int i=1; i<row.length; i++)
			newRow += ";" + row[i];
		System.out.println("-> " + newRow);
		c.setData(tablename, newRow, "Date;Open;High;Low;Close;Volume;AdjClose");
		
		c.close();
	}
	
	
	/**
	 * Liesst alle Zeilen der Tabelle aus, welche zwischen den Zeitpunkten startDate und endDate liegen.
	 * Idee: Datum eines Tages steht in Date Spalte, so dass beim einfachen Vergleich ALLE Daten eines jeden Tages ausgelesen werden.
	 * 
	 * @param tablename
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ArrayList<String> getData(String tablename, String startDate, String endDate)
	{
		ArrayList<String> list = new ArrayList<String>();
		DBMySQLConnection c = getConnection();
		
		String[] key_column = {"Date"};
		String[] key = new String[1];
		int diff = getDateMetrik(startDate, endDate);
		
		for(int i=0; i<=diff; i++)
		{
			key[0] = String.valueOf( Integer.valueOf(startDate) + i );
			ArrayList<String> result = c.getData(tablename, key_column, key, "Date;Open;High;Low;Close;Volume;AdjClose");
			
			if(result != null)
			{
				for(int j=0; j<result.size(); j++)
					list.add(result.get(j));
			}
		}
		
		c.close();
		return list;
	}
	
	
	public ArrayList<String> getColumn(String tablename, String column)
	{
		DBMySQLConnection c = getConnection();
		
		ArrayList<String> list = c.getColumn(tablename, column);
		
		c.close();
		return list;
	}
	
	/**
	 * Erstellt eine Verknuepfung zur Datenbank.
	 * 
	 * @return
	 */
	private DBMySQLConnection getConnection()
	{
		return new DBMySQLConnection(dbconfig[0], dbconfig[1], dbconfig[2], dbconfig[3], dbconfig[4]);
	}
	
	
	/**
	 * date1 und 2 sind Jahr-Monat-Tag woraus eine eindeutige Zahl gebildet wird, zb 20121001, diese Zahlen werden voneinander subtrahiert und der Betrag ausgegeben.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	private int getDateMetrik(String date1, String date2)
	{
		int d = Integer.valueOf( date2.split("-")[0] + date2.split("-")[1] + date2.split("-")[2] );
		d -= Integer.valueOf( date1.split("-")[0] + date1.split("-")[1] + date1.split("-")[2] );
		
		return Math.round(d);
	}
	
}
