package auton;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import drivetrain.DriveIO;
import edu.wpi.first.wpilibj.PIDController;

public class DriveTimeCommand implements AutonCommand { //auton command for driving straight (for a certain time)
	private boolean done;
	private double speed;
	private DriveIO driveIO;
	private long start, delay;
	private String name;
	private boolean firstIteration = true;
	
	public DriveTimeCommand(double speed, long delay, String name){
		//record inputs
		this.name = name;
		this.speed = speed;
		this.delay = delay;
		driveIO = DriveIO.getInstance();
	}

	@Override
	public void execute() {
		if(firstIteration){ //record start time on first iteration
			firstIteration = false;
			start = System.nanoTime() / 1000000;
		}
		if(!done){//if not done, set speed of drive motors
			driveIO.setY1(-speed);
		}
		if(System.nanoTime() / 1000000.0 - start > delay){ //if specified time has passed, stop
			done = true;
			driveIO.setY1(0);
		}
	}

	@Override
	public boolean isRunning() { //is the command still running
		if(done){
			driveIO.setY1(0);
		}
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
	public boolean equals(Object o){ //equal if names are equal
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
