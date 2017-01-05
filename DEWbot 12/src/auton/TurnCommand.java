package auton;

import utilities.Utilities;
import drivetrain.DriveIO;

public class TurnCommand implements AutonCommand { //auton command to turn robot
	private boolean done, freed;
	private double angle, buffer;
	private double turn, speed = 0.35;
	private DriveIO driveIO;
	private String name;
	private boolean firstIteration = true;
	
	public TurnCommand(double angle, String name){
		//record inputs
		this.angle = angle;
		this.name = name;
		buffer = 1;
		driveIO = DriveIO.getInstance();
		//System.out.println("Angle: " + angle + " Current: " + driveIO.getYaw());
		//System.out.println("Shortest Distance: " + Utilities.shortestDistanceAbsolute(driveIO.getYaw(), angle));
	}

	@Override
	public void execute() {
		if(firstIteration){
			System.out.println("Gyro on init: " + driveIO.getYaw());
			firstIteration = false;
		}
		
		if(!done){
			turn = Utilities.shortestAngleBetween(driveIO.getYaw(), angle) > 0 ? speed : -speed; //select the correct direction
			driveIO.setX2(done ? 0 : turn); //turn the robot if it hasn't finished turning
			done = Math.abs(Utilities.shortestAngleBetween(driveIO.getYaw(), angle)) < buffer; //done turning if gyro is within buffer
		}
		else{
			driveIO.setX2(0);
		}
	}

	@Override
	public boolean isRunning() { //is the command still running
		if(done && !freed){
			freed = true;
			driveIO.setX2(0); //stop robot if not done already
		}
		return !done;
	}

	@Override
	public boolean isInitialized() { //is the command initialized
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
		done = false;
		freed = false;
	}
}
