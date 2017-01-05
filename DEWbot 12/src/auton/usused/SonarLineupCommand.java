package auton.usused;

import auton.AutonCommand;
import drivetrain.DriveIO;

public class SonarLineupCommand implements AutonCommand{ //not currently in use
	private DriveIO driveIO = DriveIO.getInstance();
	private boolean done;
	private String name;
	
	public SonarLineupCommand(String name){
		this.name = name;
	}

	@Override
	public void execute() {
		double difference = driveIO.getLeftInches() - driveIO.getRightInches();
		double turn = - (Math.abs(difference) > 0.35 ? 0.25 * difference : 0);
		driveIO.setX2(turn);
		if(turn == 0) 
			done = true;
	}

	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void reset() {
		
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}

}
