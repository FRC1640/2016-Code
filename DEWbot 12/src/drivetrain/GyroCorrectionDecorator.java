package drivetrain;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;

public class GyroCorrectionDecorator extends DriveControlDecorator{ //allows robot to drive straight using gyro (mostly for auton)
	private PIDSourceDouble shortestDistance;
	private PIDOutputDouble correctedX2;
	private DriveIO driveIO = DriveIO.getInstance();
	private double gyroSetpoint, x2Prev;
	private PIDController gyroPID;
	

	public GyroCorrectionDecorator(DriveControl driveControl) {
		super(driveControl);
		shortestDistance = new PIDSourceDouble(0); //error for PID
		correctedX2 = new PIDOutputDouble(); //output to turn robot
		gyroPID = new PIDController(0.0075, 0.00015, 0.0015, shortestDistance, correctedX2, 0.02); //create PID controller
		gyroPID.setOutputRange(-0.5, 0.5);
		gyroPID.enable();
		gyroSetpoint = driveIO.getYaw();
	}
	
	public void execute(double x1, double y1, double x2, double y2){
		if(driveIO.getStart()){ //if gyro is reset, new setpoint is 0
			gyroSetpoint = 0;
			System.out.println("reset");
		}
		if(driveIO.getX2() == 0){ //only correct if robot isn't currently turning
			if(x2Prev != 0){ //if the robot just finished turning, reset the setpoint
				gyroSetpoint = driveIO.getYaw();
				System.out.println("Setpoint: " + gyroSetpoint);
			}
			
			shortestDistance.setValue(Utilities.shortestAngleBetween(driveIO.getYaw(), gyroSetpoint));
			
			//only turn the robot if the PID output is greater than 0.1
			x2 = -(correctedX2.getValue() < 0.1 && correctedX2.getValue() > -0.1 ? 0 : correctedX2.getValue()); 
		}
		super.execute(x1,y1,x2,y2);
		
		x2Prev = driveIO.getX2();
		System.out.println("Turn: " + x2);
	}

}
