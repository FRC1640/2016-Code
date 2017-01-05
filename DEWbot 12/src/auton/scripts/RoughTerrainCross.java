package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.DriveCommand;
import auton.GyroDriveCommand;
import auton.IntakeCommand;
import auton.ShootingCommand;
import auton.TimeCommand;
import auton.WaitCommand;

public class RoughTerrainCross extends AutonScript{ //script to cross rough tereain
	private static RoughTerrainCross defenseCross;
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
	
	private RoughTerrainCross(){		
		//drive over rough terrain
		script.add(new DriveCommand(165, 1, false, "test"));
		script.add(new WaitCommand(script.get(script.indexOf("test")), "testWait"));
	}
	
	public static RoughTerrainCross getInstance(){//get singleton instance
		if(defenseCross == null)
			defenseCross = new RoughTerrainCross();
		return defenseCross;
	}
	
	@Override
	public ArrayList<AutonCommand> getScript(){
		return script;
	}
}