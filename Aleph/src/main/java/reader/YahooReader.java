package reader;

import java.util.Properties;
import java.util.Timer;

import trigger.TriggerYahooReader;


public class YahooReader {

	
	/**
	 * Ruft den Konstruktor auf.
	 * config hat folgendes Format: Timeframe,UpdateIntervall,StartDay,EndDay,Assetname,Tabellenname
	 * 
	 * @param config
	 * @param dbconfig
	 */
	public static void startYahooReader(String config, String[] dbconfig)
	{
		String[] c = config.split(",");
		long update = Long.valueOf(c[1]);
		
		Properties prop = new Properties();
		prop.setProperty("Symbol", c[4]);
		prop.setProperty("startDate", c[2]);
		prop.setProperty("endDate", c[3]);
		prop.setProperty("tablename", c[5]);
		
		new YahooReader(dbconfig, update, prop);
	}
	
	
	public YahooReader(String[] dbconfig, long update, Properties prop)
	{
		Timer timer = new Timer();
		timer.schedule(new TriggerYahooReader(prop, timer, dbconfig), 1000 * update);
	}
}
