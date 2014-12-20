package reader;

import java.util.Scanner;

public class YahooReaderMessager {

	
	/**
	 * Konstruktor eines Objekts, welches die Kommunikation im bezug auf das auslesen von Yahoodaten steuern soll.
	 */
	public YahooReaderMessager(){}
	
	
	/**
	 * Holt sich vom Nutzer die Konfiguration fuer den gewuenschten Datenimport aus Yahoo.
	 * Die einzelnen Konfigurationdaten werden mit "," getrennt.
	 * 
	 * @param scanner
	 * @return
	 */
	public String getYahooConfig(Scanner scanner)
	{
		String configString = "";
		
		System.out.println("\n Welches Timeframe soll betrachtet werden? \n");
		configString += scanner.next() + ",";
		
		System.out.println("\n Geben Sie an, nach wievielen Sekunden erneut nach neuen Yahoodaten gesucht werden soll: \n");
		configString += scanner.next() + ",";
		
		System.out.println("\n Ab welchem Datum sollen die Daten geladen werden? \n");
		configString += scanner.next() +",";
		
		System.out.println("\n Ab welchem Datum sollen keine weiteren Daten geladen werden? (Dieses Feld darf freigelassen werden.) \n");
		configString += scanner.next() + ",";
		
		System.out.println("\n Welches Asset (Symbol) m�chten Sie importieren? \n");
		configString += scanner.next() + ",";
		
		System.out.println("\n Geben Sie den Namen der Tabelle an, in welchen die Daten gespeichert werden sollen: \n");
		configString += scanner.next();
		
		return configString;
	}
}
