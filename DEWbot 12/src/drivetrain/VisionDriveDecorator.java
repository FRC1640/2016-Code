package drivetrain;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import shooter.ShooterIO;
import utilities.AutoAlignFile;
import utilities.PIDFile;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;

public class VisionDriveDecorator extends DriveControlDecorator { //turn robot based on camera to align with goal (i.e. "auto align")
	private DriveControl driveControl;
	private NetworkTable networkTable;
	private double picWidth = 320, buffer = 0.75, forwardSpeed = .35, forwardCenter = 110, forwardBuffer = 5;
	private final double[] defaultValue = {12345};	

	private PIDController turnPID;
	private PIDSourceDouble angleToTurn;
	private PIDOutputDouble turnSpeed;
	private DriveIO driveIO = DriveIO.getInstance();
	private boolean isDone, isInitialized;
	private double doneIterations, gripIterations, prevCenterX, prevAngle;
	
	private double minTurnSpd = 0, gyroTargetAngle;
	private double pClose = 0.0275, iClose = 0.002, dClose = 0.3;
	private double pFar = 0.05, iFar = 0.015, dFar = 0.65;
	private double pReallyFar = 0.05, iReallyFar = 0.015, dReallyFar = 0.65;
	private boolean missing;
	
	private static double DEFAULT_TARGET = 155;
	
	private AutoAlignFile autoAlignFile;
	
	private PIDFile visionFile;
	
	public VisionDriveDecorator(DriveControl driveControl){
		super(driveControl);
		this.driveControl = driveControl;
		
		//initialize pid for turning
		angleToTurn = new PIDSourceDouble(0);
		turnSpeed = new PIDOutputDouble();
		turnPID = new PIDController(pClose, iClose, dClose, 0, angleToTurn, turnSpeed, 0.02); //Westtown: 0.037, 0.001, 0.002
		turnPID.setOutputRange(-0.4, 0.4);
		turnPID.setSetpoint(0.0);

		//get network table that holds information from processed image
		networkTable = NetworkTable.getTable("GRIP/vision");
		
		//file for helping tuning PID
		visionFile = new PIDFile("/home/lvuser/visionPIDFile.csv", turnPID, angleToTurn, turnSpeed, true);
		
		//access file that saves the target value
		try{
			autoAlignFile = AutoAlignFile.getInstance();
		}catch(Exception e){
			missing = true;
			System.out.println("Exception on getInstance()");
		}
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		
		double turnSpd = 0;
		double gyroCurrentAngle = driveIO.getYaw(); //get the current gyroscope angle
		
		if(!isInitialized) //make sure the network tables are up to date
			init();
		
		else if(!ShooterIO.getInstance().endAutoAligned()){ //only auto align while button is being held
			
			if(!isDone){ //if auto align has not yet finished aligning, continue aligning
				
				turnPID.enable(); //turn on PID
				
				angleToTurn.setValue(Utilities.shortestAngleBetween(gyroCurrentAngle, gyroTargetAngle)); //set the input of the PID to the angle we need to turn using shortestAngleBetween ensures the PID works over the 359-0 gyro jump
				
				turnSpd = turnSpeed.getValue(); //get speed to turn at from PID output
				if (Math.abs(turnSpd) < minTurnSpd) { //if the PID wants the robot to turn at a speed lower than the robot's minimum turn speed...
					turnSpd = minTurnSpd * Math.signum(turnSpd); //...set the turn speed to the minimum turn speed
				}
				
				System.out.println("Speed: " + turnSpd);
				doneIterations = 0; // resets a counter variable that keeps track of the number of iterations after auto align has finished aligning
			}
			else{
				doneIterations++;
				turnPID.disable(); //stop PID
				if(doneIterations >  10){ //only shoot if the PID has been done for 10 iterations
					ShooterIO.getInstance().setAutoAligned(true); //tell the shooter to fire the catapult
					System.out.println("Done Shooting");
				}
			}
			isDone = Math.abs(Utilities.shortestAngleBetween(gyroCurrentAngle, gyroTargetAngle)) < buffer; //finish auto align if the angle between robot's current angle and the target angle is less than a prespecified buffer.
		}
		visionFile.write(); //save vision info
		super.execute(0, 0, -turnSpd, 0); //turn robot; ignore other joystick inputs
	}
	
	private void init(){
		//get correct target value
		double aim = DEFAULT_TARGET;
		if(!missing){ //if the auton align file can be found and is working...
			try{
				aim = new Double(autoAlignFile.readLine());  //read target from file
				if(Robot.getState() == State.AUTON){ // fire slightly right if in auton
					aim -= 5;
				}
			}catch(Exception e){
				System.out.println("Exception at reading auto align file!!!: " + e);
				aim = DEFAULT_TARGET;
				missing = true;
			}
		}
		else
			aim = DEFAULT_TARGET;
		System.out.println("Center From file: " + aim);
		
		double gyroCurrentAngle = driveIO.getYaw();
		
		// initialize arrays to store vision data that is used to find the target to auto align to.
		double centerX;
		double centerY = 999;
		double[] area = networkTable.getNumberArray("area", defaultValue);
		double[] centerXArray = networkTable.getNumberArray("centerX", defaultValue);
		double[] centerYArray = networkTable.getNumberArray("centerY", defaultValue);
		
		if(area.length != 0){
			int index = 0;
			for(int i = 0; i < area.length; i++) //find target with max area
				if(area[i] > area[index]) index = i;
			if(centerXArray.length > index){
				centerX = centerXArray[index]; //record center x on largest target
			}
			else{
				centerX = 999;
				centerY = 999;
			}
			System.out.println("Current: " + centerX);
			System.out.println("Gyro: " + driveIO.getYaw());
		}
		else{
			centerX = 999;
			centerY = 999;
		}

		if(centerX != 999 && centerX != 12345){ //make sure camera actually sees the target
			double cameraAngle = calculateCameraAngle(aim, centerX, picWidth, 47); //calculate how far (in degrees) the robot will have to turn to line up the aim with the high goal
			gyroTargetAngle = (gyroCurrentAngle + cameraAngle) % 360; //calculate gyro angle the robot needs to turn to
			double angle = Math.abs(Utilities.shortestAngleBetween(gyroCurrentAngle, gyroTargetAngle));
			System.out.println("Angle: " + angle);
			
			//only finish initializing if the angle between the aim and the target doesn't change for 25 iterations
			//this will ensure that the robot is using the correct data due to a delay in the processing
			if(Math.abs(prevAngle - angle) <= 1)//angle hasn't changed in the last iteration
				gripIterations++; //increase number of iterations where angle hasn't changed
			else{ //if the angle DID change...
				gripIterations = 0; //reset number of iterations
				System.out.println("Prev Center X: " + prevCenterX + " center X: " + centerX);
			}
			
			if(gripIterations >= 25){//data from GRIP should be up to date
				//use different PID gains based on the distance the robot needs to turn (due to the highstatic CoF of the tank treads)
				if(angle > 10){ //use Far PID gains
					turnPID.setPID(pFar, iFar, dFar);
				}
				if(angle > 14){ //use Really Far PID gains
					turnPID.setPID(pReallyFar, iReallyFar, dReallyFar);
				}
				isInitialized = true; //now that auto align is initialized with a target angle for the gyro, move on to execute()
			}
			
			prevAngle = angle;
		
		}else{
			gripIterations = 0;
		}
		prevCenterX = centerX;
	}
	
	//calculate angle to turn
	private double calculateCameraAngle(double target, double current, double picDimension, double fieldOfView){
		double focalLength = picDimension / (2 * Math.tan(Math.toRadians(fieldOfView / 2))); //using focal length increases accuracy of calculated angle
		return Math.toDegrees(Math.atan((target - current) / focalLength)); 
	}
}
