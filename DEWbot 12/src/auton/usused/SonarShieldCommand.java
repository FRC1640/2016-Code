package auton.usused;

import auton.AutonCommand;
import utilities.unused.CorrectSensorFile;
import drivetrain.DriveIO;

public class SonarShieldCommand implements AutonCommand{ //not currently in use
	private boolean done, above;
	private String name;
	private double speed, threshold;
	private DriveIO driveIO = DriveIO.getInstance();
	
	public SonarShieldCommand(boolean above, double threshold, double speed, String name){
		this.name = name;
		this.speed = speed;
		this.above = above;
		this.threshold = threshold;
	}
	
	@Override
	public void execute() {
		if((above && driveIO.getSideSonarInches() > threshold)|| (!above && driveIO.getSideSonarInches() < threshold)){
			done = true;
			driveIO.setY1(0);
		}
		else
			driveIO.setY1(-speed);
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
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}
	
	@Override 
	public void reset() {
		done = false;
	}
}
