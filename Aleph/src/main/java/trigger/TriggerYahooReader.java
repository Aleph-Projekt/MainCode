package trigger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import datenbanken.MySQLYahooData;


public class TriggerYahooReader extends TimerTask {

	
	Properties config;
	Timer timer;
	String[] db_config;
	
	
	public TriggerYahooReader(Properties prop, Timer t, String[] dbconfig)
	{
		config = prop;
		timer = t;
		db_config = dbconfig;
	}
	
	
	@Override
	public void run()
	{
		String uri = buildURI(config.getProperty("Symbol"), config.getProperty("startDate"), config.getProperty("endDate"));
		String responseBody = doCall(uri);
		String[] data = responseBody.split("\n");
		
		MySQLYahooData db = new MySQLYahooData(db_config);
		ArrayList<String> dateInDB = db.getColumn(config.getProperty("tablename"), "Date");
		
		for(int i=1; i<data.length; i++)
		{
			String[] row = data[i].split(",");
			
			if(dateInDB == null || !dateInDB.contains(row[0]))
				db.setData(config.getProperty("tablename"), row);
		}		
	}	
	
	
	private String buildURI(String symbol, String dateStart, String dateEnd)
	{
		String[] start = dateStart.split("-");
		String[] end = dateEnd.split("-");
		
		StringBuilder uri = new StringBuilder();
		uri.append("http://ichart.finance.yahoo.com/table.csv");
		uri.append("?s=").append(symbol);
		uri.append("&a=").append(start[1]);
		uri.append("&b=").append(start[2]);
		uri.append("&c=").append(start[0]);
		uri.append("&d=").append(end[1]);
		uri.append("&e=").append(end[2]);
		uri.append("&f=").append(end[0]);
		uri.append("&g=d");
		uri.append("&ignore=.csv");
 
		return uri.toString();
	}
	
	
	private String doCall(String uri)
	{
		HttpClient httpClient = new HttpClient();
		HttpMethod getMethod = new GetMethod(uri);
 
		try {
			int response = httpClient.executeMethod(getMethod);
 
 
			InputStream stream = getMethod.getResponseBodyAsStream();
			String responseText = responseToString(stream);
 
			return responseText;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		return null;
	}
 
	
	private String responseToString(InputStream stream) throws IOException
	{
		BufferedInputStream bi = new BufferedInputStream(stream); 
		StringBuilder sb = new StringBuilder();
 
		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		
		while ((bytesRead = bi.read(buffer)) != -1)
		{
			sb.append(new String(buffer, 0, bytesRead));
		}
		
		return sb.toString();
	}

	
}
