
package auton;

import java.util.ArrayList;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.RobotDashboard;
import auton.scripts.ChevalDeFriseCross;
import auton.scripts.HighGoalScript;
import auton.scripts.MoatCross;
import auton.scripts.PortcullisCross;
import auton.scripts.Position2;
import auton.scripts.Position3;
import auton.scripts.Position4;
import auton.scripts.Position5;
import auton.scripts.PowerTurnTestScript;
import auton.scripts.ReachScript;
import auton.scripts.RoughTerrainCross;
import auton.scripts.SimpleCross;

public class ScriptSelector { //class that selects the correct script based on dashboard inputs
	private static ScriptSelector scriptSelector; //singleton instance
	private String prevObstacle = "", prevPosition = "", prevGoal = "" ;
	private ArrayList<AutonCommand> prevScript = new ArrayList<AutonCommand>();

	private ArrayList<AutonCommand> script = new ArrayList<AutonCommand>() {
		@Override //change indexOf so it compares the names of the objects
		public int indexOf(Object o) {
			if (o == null) {
				for (int i = 0; i < script.size(); i++)
					if (script.get(i) == null)
						return i;
			} else {
				for (int i = 0; i < script.size(); i++) {
					System.out.println(script.get(i));
					if (script.get(i).equals(o))
						return i;
				}
			}
			return -1;
		}
	};

	private ScriptRunner scriptRunner = ScriptRunner.getInstance(true); 
	private String positionSelection, obstacleSelection, goalSelection;
	
	private ScriptSelector(){}

	public static ScriptSelector getInstance() {
		if (scriptSelector == null)
			scriptSelector = new ScriptSelector();
		return scriptSelector;
	}

	public void update() {
		//if the robot is disabled and the dashboard inputs change
		if (Robot.getState() == State.DISABLED && (!RobotDashboard.getAutonObstacle().equals(obstacleSelection)
				|| !RobotDashboard.getAutonPosition().equals(positionSelection)
				|| !RobotDashboard.getAutonShooterSetting().equals(goalSelection))) {

			script = new ArrayList<AutonCommand>();
			
			//add the correct obstacle script
			switch (RobotDashboard.getAutonObstacle()) {
				case "Drawbridge":
					script.addAll(ReachScript.getInstance().getScript());
					obstacleSelection = "Drawbridge";
					break;
					
				case "Cheval de Frise":
					script.addAll(ChevalDeFriseCross.getInstance().getScript());
					obstacleSelection = "Cheval de Frise";
					break;
					
				case "Moat":
					script.addAll(MoatCross.getInstance().getScript());
					obstacleSelection = "Moat";
					break;
					
				case "Rough Terrain":
					script.addAll(RoughTerrainCross.getInstance().getScript());
					obstacleSelection = "Rough Terrain";
					break;
					
				case "Ramparts":
				case "Rock Wall":
					script.addAll(SimpleCross.getInstance().getScript());
					obstacleSelection = RobotDashboard.getAutonObstacle();
					break;
					
				case "Portcullis":
					script.addAll(PortcullisCross.getInstance().getScript());
					obstacleSelection = "Portcullis";
					break;
	
				case "default":
				default:
					obstacleSelection = "default";
					break;
			}
			System.out.print(obstacleSelection.equals(prevObstacle) ? "" : obstacleSelection + "\n");
			prevObstacle = obstacleSelection;

			//add correct position script
			switch (RobotDashboard.getAutonPosition()) {
				case "2":
					script.addAll(Position2.getInstance().getScript());
					positionSelection = "Position 2";
					break;
	
				case "3":
					script.addAll(Position3.getInstance().getScript());
					positionSelection = "Position 3";
					break;
	
				case "4": {
					script.addAll(Position4.getInstance().getScript());
					positionSelection = "Position 4";
					break;
				}
				case "5":
					script.addAll(Position5.getInstance().getScript());
					positionSelection = "Position 5";
					break;
	
				case "default":
				default:
					positionSelection = "default";
					break;	
			}
			System.out.print(positionSelection.equals(prevPosition) ? "" : positionSelection + "\n");
			prevPosition = positionSelection;

			//add correct shooiting setting
			switch (RobotDashboard.getAutonShooterSetting()) {
				case "High Goal":
					script.addAll(HighGoalScript.getInstance().getScript());
					goalSelection = "High Goal";
					break;
	
				case "Low Goal":
					goalSelection = "Low Goal";
					break;
	
				case "Stop":
					goalSelection = "Stop";
					break;
	
				case "default":
				default:
					goalSelection = "default";
					break;
			}
			System.out.print(goalSelection.equals(prevGoal) ? "" : goalSelection + "\n");
			prevGoal = goalSelection;

			//if the script has changed, print the new script for debugging purposes
			boolean printScript = false;
			if (prevScript.size() == script.size()) {
				for (int i = 0; i < prevScript.size(); i++) {
					if (prevScript.get(i) != script.get(i))
						printScript = true;
				}
			} else
				printScript = true;
			System.out.print(printScript ? "Setting script: " + script + "\n" : "");
		 	
			//set the script
			scriptRunner.setScript(script);
			prevScript = script;
		}
	}
}
