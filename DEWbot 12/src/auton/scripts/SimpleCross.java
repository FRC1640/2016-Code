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

public class SimpleCross extends AutonScript{ //script to cross simple obstacles (rock wall)
	private static SimpleCross defenseCross;
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
	
	private SimpleCross(){						
		//drive over obstacle
		script.add(new DriveCommand(200, 1, true, "test"));
		script.add(new WaitCommand(script.get(script.indexOf("test")), "testWait"));
		
	}
	
	public static SimpleCross getInstance(){ //get singleton instance
		if(defenseCross == null)
			defenseCross = new SimpleCross();
		return defenseCross;
	}
	
	@Override
	public ArrayList<AutonCommand> getScript(){
		return script;
	}
}