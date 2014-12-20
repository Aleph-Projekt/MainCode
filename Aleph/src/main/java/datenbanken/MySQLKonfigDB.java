package datenbanken;

import java.util.ArrayList;


public class MySQLKonfigDB {

	private String[] dbconfig;
	
	
	/**
	 * Konstruktor fuer das Objekt, welches neue MySQL Konfigurationen anlegen,
	 * auslesen und bestehendes Konfigurationen aufliesten kann.
	 */
	public MySQLKonfigDB(String[] dbconfig)
	{
		this.dbconfig = dbconfig;
	}
	
	
	/**
	 * Schreibt eine Konfiguration in die MySQL KonfigDB, erstellt die KonfigDB falls noetig.
	 * 
	 * @param jonfig
	 */
	public void setKonfig(String[] jobConfig)
	{
		DBMySQLConnection c = getConnection();
		
		String[] key_column = {"JobID"};
		String[] key = new String[1];
		key[0] = jobConfig[0];
		c.removeRow(dbconfig[0], key_column, key);
		
		String newRow = jobConfig[0] + ";" + jobConfig[1] + ";" + jobConfig[2];
		c.setData(dbconfig[0], newRow, "JobID;JobKlasse;LokalJobConfig");
		
		c.close();
	}
	
	
	/**
	 * Gibt die Konfiguration des Job mit der jobID aus.
	 * 
	 * @param jobID
	 * @return
	 */
	public String[] getKonfig(String jobID)
	{
		DBMySQLConnection c = getConnection();
		
		String[] key_column = {"JobID"};
		String[] key = new String[1];
		key[0] = jobID;
		String[] jobConfig = c.getData(dbconfig[0], key_column, key, "JobID;JobKlasse;LokalJobConfig").get(0).split(";");
		
		c.close();
		return jobConfig;
	}
	
	
	/**
	 * Listet alle Konfigurationen aus, die in der MySQL JobKonfig stehen.
	 * 
	 * @return
	 */
	public String getKonfigList()
	{
		DBMySQLConnection c = getConnection();
		String list = "";
		
		ArrayList<String> ids = c.getColumn(dbconfig[0], "JobID");
		for(int i=0; i<ids.size(); i++)
		{
			String[] key_column = {"JobID"};
			String[] key = new String[1];
			key[0] = ids.get(i);
			list += c.getData(dbconfig[0], key_column, key, "JobID;JobKlasse;LokalJobConfig").get(0);
		}
		
		c.close();
		return list;
	}
	
	
	/**
	 * Gibt ein Datenbankobjekt zurueck.
	 * 
	 * @return
	 */
	private DBMySQLConnection getConnection()
	{
		return new DBMySQLConnection(dbconfig[0], dbconfig[1], dbconfig[2], dbconfig[3], dbconfig[4]);
	}
}
