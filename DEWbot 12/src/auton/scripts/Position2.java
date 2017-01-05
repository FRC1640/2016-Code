package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.DriveCommand;
import auton.PortcullisCommand;
import auton.TimeCommand;
import auton.TurnCommand;
import auton.WaitCommand;
import auton.usused.SonarDriveCommand;
import auton.usused.SonarLineupCommand;

public class Position2 extends AutonScript{ //script for obstacles in position 2

	private static Position2 positionTwo;
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
	
	private Position2(){
		//turn generally towards tower (so the camera can see the target)
		script.add(new TurnCommand(340, "roughTurn"));
		script.add(new WaitCommand(script.get(script.indexOf("roughTurn")), "waitRoughTurn"));
	}
	
	public static Position2 getInstance(){ //get singleton instance
		if(positionTwo == null)
			positionTwo = new Position2();
		return positionTwo;
	}
	@Override
	public ArrayList<AutonCommand> getScript() {
		return script;
	}

}
