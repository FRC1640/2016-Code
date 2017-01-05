package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.TurnCommand;
import auton.WaitCommand;

public class Position5 extends AutonScript{//script for obstacles in position 5
	private static Position5 positionFive;
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
	
	private Position5(){
		//rough turn towards tower so the camera can see the target
		script.add(new TurnCommand(20, "roughTurn"));
		script.add(new WaitCommand(script.get(script.indexOf("roughTurn")), "waitRoughTurn"));
	}
	
	public static Position5 getInstance(){//get singelton instance
		if(positionFive == null)
			positionFive = new Position5();
		return positionFive;
	}
	
	@Override
	public ArrayList<AutonCommand> getScript() {
		return script;
	}
}
