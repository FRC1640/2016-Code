package auton;

import drivetrain.DriveIO;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class GyroDriveCommand implements AutonCommand { //not currently in use
	private boolean done, pitch;
	private DriveIO driveIO;
	private String name;
	private double speed, lowerLimit, upperLimit;
	private int iterations, doneIterations;
	
	
	public GyroDriveCommand(boolean pitch, double speed, double lowerLimit, double upperLimit, int iterations, String name){
		this.name = name;
		driveIO = DriveIO.getInstance();
		this.pitch = pitch;
		this.speed = speed;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.iterations = iterations;
	}

	@Override
	public void execute() {
		double gyro = pitch ? driveIO.getPitch() : driveIO.getRoll();
		driveIO.setY1(-speed);
		if(gyro > lowerLimit && gyro < upperLimit){
			doneIterations++;
		}
		else{
			driveIO.setY1(-speed);
			doneIterations = 0;
		}
		
		if(doneIterations > iterations){
			driveIO.setY1(0);
			done = true;
		}
	}

	@Override
	public boolean isRunning() {
		if(done){
			driveIO.setY1(0);
		}
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
		done = false;
	}
}
