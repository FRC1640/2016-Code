package auton.usused;

import auton.AutonCommand;
import drivetrain.DriveIO;

public class SonarDriveCommand implements AutonCommand{ //not currently in use
	private double setpoint;
	private boolean done;
	private String name;
	private DriveIO driveIO = DriveIO.getInstance();

	public SonarDriveCommand(double setpoint, String name){
		//record inputs
		this.setpoint = setpoint;
		this.name = name;
	}
	
	@Override
	public void execute() {
		double driveMotor = 0.0175 * (driveIO.getLeftInches() - setpoint);
		double deadband = 0.25;
		driveMotor = (driveMotor < deadband ? deadband : driveMotor) > 1 ? 1 : driveMotor;
		DriveIO.getInstance().setY1(-driveMotor);
		System.out.println(driveMotor + " : " + driveIO.getLeftInches());
		done = driveIO.getLeftInches() < setpoint;
	}

	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}
	
	public String getName(){
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
	}
}
