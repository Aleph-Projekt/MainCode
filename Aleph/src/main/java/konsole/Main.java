package konsole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import reader.YahooReader;
import datenbanken.MySQLKonfigDB;


public class Main {

	
	private Scanner scanner;
	
	
	/**
	 * Startet das Konsolenprogramm.
	 * 
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{
		System.out.println("\n MarktAnalysierer gestartet. \n");
		new Main();
	}
	
	
	/**
	 * Hauptmethode des Konsolenprogramms. 
	 */
	public Main()
	{
		Messages messages = new Messages();
		scanner = new Scanner(System.in);
		String m;
		
		String[] db = messages.getDBdata(scanner);
		
		if(db.length > 0)
		{
			do
			{
				System.out.println(messages.getStartMassage());
				
				m = scanner.next();
				String[] trueMessages = {"0", "1", "2", "3"};
				
				if(!m.equals("3"))
				{
					if(messages.trueMessage(m, trueMessages))
						System.out.println("-> " + doJob(m, db));
					else
						System.out.println(messages.falscheEintageError());
				}
			}
			while(!m.equals("3"));
		}
	}
	
	
	/**
	 * Liesst den Job aus und bearbeitet ihn.
	 * 
	 * @param job
	 * @return
	 */
	private String doJob(String job, String[] dbconfig)
	{
		String message = "";
		MySQLKonfigDB config = new MySQLKonfigDB(dbconfig);
		
		if(job.equals("0"))
			message = config.getKonfigList();
		
		else if(job.equals("1"))
			message = setKonfig(config);
		
		else if(job.equals("2"))
			message = startJob(config, dbconfig);
		
		return message;
	}
	
	
	/**
	 * Erstellt eine neue Konfiguration, verwaltet die dazugehoerige Kommunikation mit dem Programanwender.
	 * 
	 * @return
	 */
	private String setKonfig(MySQLKonfigDB config)
	{
		Messages m = new Messages();
		String[] jobConfig = m.getJobKonfig(scanner);
		config.setKonfig(jobConfig);
		
		return "Job " + jobConfig[0] + " erstellt.";
	}
	
	
	/**
	 * Gibt die Konfiguration einer bestimmten JobID wieder.
	 * 
	 * @return
	 */
	private String startJob(MySQLKonfigDB config, String[] dbconfig)
	{
		Messages m = new Messages();
		String jobID = m.getJobID(scanner);
		String[] jobConfig = config.getKonfig(jobID);
		
		if(jobConfig[1].equals("YahooReader"))
			YahooReader.startYahooReader(jobConfig[2], dbconfig);
		
		return "Job " + jobID + " wird ausgef�hrt.";
	}
}
