package auton;

import utilities.Utilities;
import drivetrain.DriveIO;

import java.util.Arrays;

public class DriveCommand implements AutonCommand{ //auton command for driving straight
	private double distance, speed;
	private boolean done, fullSpeed;
	private boolean firstIteration = true;
	private String name;
	private int muConversion = 3, sigmaConversion = 4; //constants for ramping speed

	public DriveCommand(int distance, double speed, boolean fullSpeed, String name){
		//record inputs
		this.distance = distance;
		this.speed = speed;
		this.name = name;
		this.fullSpeed = fullSpeed; //if true, it will ignore the ramping speed for the first 3/4 of distance and go full speed
	}
	
	@Override
	public void execute() {
		if(firstIteration){ //reset encoders on first iteration
			DriveIO.getInstance().resetEncoders();
			System.out.println("Reset Encoders");
		}
		int[] encoders = DriveIO.getInstance().getEncoders(); //read encoders
		
		//convert encoder to inches
		int encoder = encoders[1]; 
		double revolutions = (-encoder / (2048.0 * 4));
		double inches = revolutions * 11.81;

		//ramp speed with gaussian curve
		double driveMotor =  speed * Math.pow(Math.E, -(Math.pow(inches - (distance / muConversion), 2)  / Math.pow(2 * (distance / sigmaConversion), 2)));

		//never go below 0.25 speed
		double deadband = 0.25;
		driveMotor = (driveMotor < deadband ? deadband : driveMotor) > 1 ? 1 : driveMotor;
		
		//apply full speed (if applicable)
		driveMotor = fullSpeed && inches <= distance * 3 / 4 ? 1 : driveMotor;
		
		//stop motors if past desired distance
		driveMotor = inches >= distance ? 0 : driveMotor;
		
		//set speed
		DriveIO.getInstance().setY1(-driveMotor);
		done = inches >= distance;
	}

	@Override
	public boolean isRunning() { //is the command still running
		return !done;
	}

	@Override
	public boolean isInitialized() { //is the command started
		return true;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public boolean equals(Object o){ //object is equal if name is equal 
		if(o instanceof String){
			return o == name;
		}
		return false;
	}
	
	@Override 
	public void reset() { 
		done = false;
		firstIteration = true;
	}

}
