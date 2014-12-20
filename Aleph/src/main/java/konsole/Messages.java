package konsole;

import java.util.Scanner;

import reader.YahooReaderMessager;


public class Messages {

	
	/**
	 * Konstruktor fuer das Objekt zum Verwalten der Konsolennachrichten.
	 */
	public Messages(){}
	
	
	/**
	 * Uebersicht der Grundbefehle zur Steuerung des Programms.
	 * 
	 * @return
	 */
	public String getStartMassage()
	{
		return "\n Was m�chten Sie tun? \n [0] Konfigurationen einsehen \n [1] Konfiguration anlegen"
				+ "\n [2] Konfigurierten Job starten \n [3] Programmschlie�en \n";
	}
	
	
	/**
	 * Untersucht, ob die vom Nutzer uebergebene Massege zulaessig ist.
	 * 
	 * @param m
	 * @return
	 */
	public boolean trueMessage(String m, String[] trues)
	{
		for(int i=0; i<trues.length; i++)
			if(m.equals(trues[i]))
				return true;
		
		return false;
	}
	
	
	/**
	 * Fehlermeldung fuer den Programmnutzer, welche bei falscher Befehlseingabe angezeigt wird.
	 * 
	 * @return
	 */
	public String falscheEintageError()
	{
		return "Ihre Eingabe war fehlerhaft.";
	}
	
	
	/**
	 * Fragt beim Nutzer die Konfiguration fuer die MySQL Datenbank ab, auf der gearbeitet werden soll.
	 * 
	 * @return
	 */
	public String[] getDBdata(Scanner scanner)
	{
		String[] dbData = new String[5];
		
		System.out.println("\n Wie lautet der Name der MySQL Datenbank auf der Sie arbeiten wollen? \n");
		dbData[0] = scanner.next();
		
		System.out.println("\n Auf welchem Host liegt die Datenbank? \n");
		dbData[1] = scanner.next();
		
		System.out.println("\n Welchen Port nutzt die Datenbank? \n");
		dbData[2] = scanner.next();
		
		System.out.println("\n Geben Sie einen Usernamen an, mit welchem ein Login in die Datenbank m�glich ist. \n");
		dbData[3] = scanner.next();
		
		System.out.println("\n Welcher Passwort verwendet die Datenbank? \n");
		dbData[4] = scanner.next();
		
		return dbData;
	}
	
	
	/**
	 * Gibt die Daten der neuen JobKonfiguration zurueck, kommuniziert diese mit dem Anwender des Programms.
	 * 
	 * @param scanner
	 * @return
	 */
	public String[] getJobKonfig(Scanner scanner)
	{
		String[] config = new String[3];
		
		System.out.println("\n Geben Sie eine JobID ein (ACHTUNG: Geben Sie eine bekannte ID ein, �berschreiben Sie damit die bestehende Konfiguration dieser ID): \n");
		config[0] = scanner.next();
		
		System.out.println("\n Definieren Sie die JobKlasse: \n");
		config[1] = scanner.next();
		
		if(config[1].equals("YahooReader"))
		{
			YahooReaderMessager yahoo = new YahooReaderMessager();
			config[2] = yahoo.getYahooConfig(scanner);
		}
		
		return config;
	}
	
	
	/**
	 * Gibt die JobID des auszufuehrenden Jobs zurueck, kommuniziert diese mit dem Programmanwender.
	 * 
	 * @param scanner
	 * @return
	 */
	public String getJobID(Scanner scanner)
	{
		System.out.println("\n Geben Sie die ID des Jobs an, den Sie ausf�hren m�chten: \n");
		return scanner.next();
	}
}
