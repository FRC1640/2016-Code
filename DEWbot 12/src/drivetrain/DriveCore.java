package drivetrain;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import drivetrain.unused.SonarTurnDecorator;
import utilities.Controllers;
import utilities.RobotDashboard;
import utilities.unused.SensorFile;
import edu.wpi.first.wpilibj.Joystick.RumbleType;


public class DriveCore{ //main class of the drive code
	
	private static DriveCore driveCore;//singleton instance
	
	private DriveIO driveIO = DriveIO.getInstance();
	private boolean antiTip, antiTipPrev, prevGyro, prevA, prevArcade, prevVision, arcade, gyro, portcullis, prevAdaptive, prevStart, prevBack;
	private int prevDriveState;
		
	private	DriveControl driveFunct = (new DesensitizedOperatorDecorator(ArcadeDriveControl.getInstance())); //initial drive mode
	
	private DriveCore(){
		driveIO.resetGyro(); 
	}
	
	public static DriveCore getInstance() {
		//if the singleton instance hasn't been created yet, create one
		if (driveCore == null) driveCore = new DriveCore();
		return driveCore;
	}
	
	public void setAntiTip(boolean antitip) { //not currently in use
		this.antiTip = antitip;
	}
	
	public void update(){ 
		if(driveIO.getStart() && !prevStart){ //reset gyro by pressing button on controller
			driveIO.resetGyro();
			System.out.println("Resetting Gyro Drive Core");
		}
		
		if(driveIO.getBack() && !prevBack){ //reset encoders by pressing button on controller
			driveIO.resetEncoders();
		}
		
		//Priority: Anti Tip - Vision - Gyro - Arcade
		//A Button: Tank vs Drive toggle
		//B Button: Vision hold
		//X Button: Gyro
		boolean auton = Robot.getState() == State.AUTON;
		
		boolean vision = driveIO.getRightBumper(); //turn on autoalign 
		gyro = driveIO.getLeftBumper(); //turn on gyro correction
		if(driveIO.getA() && !prevArcade) //toggle between arcade and tank drive
			arcade = !arcade;
		
		//ramp motors to avoid brownouts
		int rampRate = 55;
		int noRampRate = 0;
		
		//turn booleans into int using bits (a convenient way to save the state of the drive)
		int driveState = (antiTip ? 16 : 0) | (vision ? 8 : 0) | (portcullis ? 4 : 0) | (gyro || auton ? 2 : 0) | (arcade ? 1 : 0);
		
		//if the drive state has changed, update the drive control
		if(driveState != prevDriveState){
			if(driveState >= 16){
				//would be anti tip, but it is not currently in use
			}
			else if(driveState >= 8){ //Vision - RB
				driveIO.setRampRate(noRampRate);
				driveFunct = new VisionDriveDecorator(ArcadeDriveControl.getInstance());
			}
			else if(driveState >= 4){ //Portcullis - X
				//not currently in use
				driveIO.setRampRate(noRampRate);
				driveFunct = new SonarTurnDecorator(ArcadeDriveControl.getInstance());
			}
			else if(driveState >= 2){ //Gyro - Auton or LB
				//use the gyro to make sure robot stays straight
				driveIO.setRampRate(noRampRate);
				driveFunct = new GyroCorrectionDecorator(new DesensitizedOperatorDecorator(ArcadeDriveControl.getInstance()));
			}
			else if(driveState == 1){ //arcade - A
				driveIO.setRampRate(rampRate);
				driveFunct = new OperatorTankDecorator(TankDriveControl.getInstance());
				
				//turn off rumble in case they were on. 
				Controllers.getDriverJoystick().setRumble(RumbleType.kRightRumble, (float) 0);
				Controllers.getOperatorJoystick().setRumble(RumbleType.kRightRumble, (float) 0);
				Controllers.getDriverJoystick().setRumble(RumbleType.kLeftRumble, (float) 0);
				Controllers.getOperatorJoystick().setRumble(RumbleType.kLeftRumble, (float) 0);
			}
			else{ //tank - A
				driveIO.setRampRate(rampRate);
				driveFunct = new TiltDecorator(new DesensitizedOperatorDecorator(ArcadeDriveControl.getInstance()));
			}
			System.out.println(driveFunct);
		}
		
		double slow = 1;
		driveFunct.execute(driveIO.getX1() * slow , -driveIO.getY1() * slow, driveIO.getX2() * slow, -driveIO.getY2() * slow); 

		antiTipPrev = antiTip;
		prevVision = vision;
		prevGyro = driveIO.getLeftBumper();
		prevArcade = driveIO.getA();
		prevAdaptive = driveIO.getY();
		prevStart = driveIO.getStart();
		prevBack = driveIO.getBack();
		prevDriveState = driveState;
		
		//manually update sonars so they do not interfere with each other
		driveIO.updateSonars();

		//update dashboard with drive state for debugging
		RobotDashboard.putString("DriveState", String.valueOf(driveState));
		
	}
}
