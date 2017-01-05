package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.CorrectSensorCommand;
import auton.DriveCommand;
import auton.TimeCommand;
import auton.TurnCommand;
import auton.WaitCommand;
import auton.usused.RecordSensorCommand;
import auton.usused.SonarShieldCommand;

public class ReachScript extends AutonScript{ //script to simply reach the defenses
	private static ReachScript defenseCross;
	private ArrayList<AutonCommand> script = new ArrayList<AutonCommand>(){
		@Override//change indexOf so it compares the names of the objects
		public int indexOf(Object o){
			if (o == null) {
		        for (int i = 0; i < script.size(); i++)
		            if (script.get(i) == null)
		                return i;
		    } else {
		        for (int i = 0; i < script.size(); i++)
		            if (script.get(i).equals(o))
		                return i;
		    }
			return -1;
		}
	};
	
	private ReachScript(){						
		//drive towards defenses
		script.add(new DriveCommand(35, 0.5, false, "test"));
		script.add(new WaitCommand(script.get(script.indexOf("test")), "testWait"));
		
	}
	
	public static ReachScript getInstance(){//get singelton instance
		if(defenseCross == null)
			defenseCross = new ReachScript();
		return defenseCross;
	}
	
	@Override
	public ArrayList<AutonCommand> getScript(){
		return script;
	}
}