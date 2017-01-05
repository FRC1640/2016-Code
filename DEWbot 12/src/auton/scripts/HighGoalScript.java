package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.IntakeCommand;
import auton.ShootingCommand;
import auton.TimeCommand;
import auton.WaitCommand;
import auton.usused.TurnGoalCommand;

public class HighGoalScript extends AutonScript{ //script to shoot in high goal
	private static HighGoalScript highGoalScript;
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
	
	private HighGoalScript(){
		//intake the ball
		script.add(new IntakeCommand(1, 0, "intake"));
		script.add(new TimeCommand(2500, "intakeWait"));
		script.add(new WaitCommand(script.get(script.indexOf("intakeWait")), "waitIntake"));
		
		//shoot the ball
		script.add(new ShootingCommand("Shoot"));
		script.add(new WaitCommand(script.get(script.indexOf("Shoot")), "testWait"));
		
		//lift the intake
		script.add(new IntakeCommand(0, 0, "up"));
	}
	
	public static HighGoalScript getInstance(){ //get singelton instance
		if(highGoalScript == null)
			highGoalScript = new HighGoalScript();
		return highGoalScript;
	}
	@Override
	public ArrayList<AutonCommand> getScript() {
		return script;
	}

}
