package trigger;

import java.sql.SQLException;

import org.rosuda.REngine.*;
import org.rosuda.REngine.JRI.*;

public class RTrigger {

	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{
		
		RCallbacks call = new RCallbacks();
		REngine re = new JRIEngine(new String[] { "--no-save" }, call, false);
		re.parseAndEval("source(\"/scriptname.R\")");
		re.close();
	}
	
}
