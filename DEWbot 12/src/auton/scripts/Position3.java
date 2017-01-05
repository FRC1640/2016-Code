package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.TurnCommand;
import auton.WaitCommand;

public class Position3 extends AutonScript{ //script for obstacles in position 3
	private static Position3 positionThree;
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
	
	private Position3(){
		//turn roughly towards the tower so the camera can see the target
		script.add(new TurnCommand(350, "roughTurn"));
		script.add(new WaitCommand(script.get(script.indexOf("roughTurn")), "waitRoughTurn"));
	}
	
	public static Position3 getInstance(){ //get singleton instance
		if(positionThree == null)
			positionThree = new Position3();
		return positionThree;
	}
	@Override
	public ArrayList<AutonCommand> getScript() {
		return script;
	}
}
