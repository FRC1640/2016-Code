package shooter;

import intake.IntakeIO;
import utilities.AutoAlignFile;
import utilities.Controllers;
import utilities.RobotDashboard;
import drivetrain.DriveIO;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class ShooterIO{
	private static ShooterIO shooterIO;
	
	private CANTalon shooterMotor;
	private Solenoid shooterSolenoid;
	private DigitalInput set;
	private boolean doneAligning, aligning, shooting;
	private final double speed = -0.6, intakeDelay = 1500, shootingDelay = 250;
	private enum ShooterState {SETTING("SETTING"), IDLING("IDLING"), INTAKE_DOWN("INTAKE_DOWN"), AUTO_ALIGN("AUTO_ALIGN"), WAITING("WAITING"), SHOOTING("SHOOTING");
	    private String enumVal;

	    ShooterState(String enumVal) {
	        this.enumVal = enumVal;
	    }

	    public String getEnumVal() {
	        return enumVal;
	    }
	};
	private ShooterState state = ShooterState.INTAKE_DOWN, prevState = ShooterState.INTAKE_DOWN;
	private boolean initialized = false;
	private long start;
	private boolean prevSet, prevOpOverride, opOverride;
	private int prevPOV;
	
	private boolean prevDriver, prevOperator;
	private double[] array = {999.0};
	
	private NetworkTable table;
	
	private AutoAlignFile file = AutoAlignFile.getInstance();
	
	private ShooterIO(){
		//initalize IO
		shooterMotor = new CANTalon(5);
		shooterSolenoid = new Solenoid(2);
		set = new DigitalInput(2);
		table = NetworkTable.getTable("GRIP/vision");
	}
	
	public static ShooterIO getInstance(){ //get Singleton instance
		if(shooterIO == null)
			shooterIO = new ShooterIO();
		return shooterIO;
	}
	
	public void update(){
		if(!initialized) //if the robot has just turned on, put the intake down to lower the catapult
			IntakeIO.getInstance().setShooting(true);
		
		switch(state){
			case SETTING: { //bring the shooter down
				shooterSolenoid.set(false);
				shooterMotor.set(speed);
				
				//when the limit switch is hit or the operator overrides the LS, the shooter is set and the state moves to idling
				if(!set.get() || opOverride){
					state = ShooterState.IDLING;
				}
				break;
			}
			case IDLING: { //shooter isn't doing anything
				initialized = true;
				
				//bring up the intake and stop the motor
				IntakeIO.getInstance().setShooting(false);
				shooterMotor.set(0);
				shooting = false;
				doneAligning = false;
				
				//adjust auto aim target
				int operatorPOV = Controllers.getOperatorJoystick().getPOV();
				if(operatorPOV != prevPOV){ 
					if(operatorPOV == 270){ //shoot further to the left
						file.rewriteFile(file.offsetAim(1));
					}
					else if(operatorPOV == 90){ //shoot further to the right
						file.rewriteFile(file.offsetAim(-1));
					}
				}
				
				//begin auto align
				if(DriveIO.getInstance().getRightBumper() && !prevDriver){ 
					aligning = true;
					state = ShooterState.INTAKE_DOWN;
					prevDriver = true;
				}
				else if(!DriveIO.getInstance().getRightBumper())
					prevDriver = false;
				
				//begin manual shooting (no auto aim)
				if(Controllers.getOperatorJoystick().getRawAxis(2) > 0.5 && !prevOperator){ 
					aligning = false;
					state = ShooterState.INTAKE_DOWN;
					prevOperator = true;
				}
				else if(Controllers.getOperatorJoystick().getRawAxis(2) < 0.5)
					prevOperator = false;
				break;
			}
			case INTAKE_DOWN: {//put the intake down before shooting
				//put the intake down
				IntakeIO.getInstance().setShooting(true);
				
				
				if(initialized && aligning){ //if aligning, begin aligning
					state = ShooterState.AUTO_ALIGN;
					start = System.nanoTime() / 1000000;
				}
				else{ //if not aligning, begin waiting
					state = ShooterState.WAITING;
					start = System.nanoTime() / 1000000;
				}
				break;
			}
			case AUTO_ALIGN: {//begin auto aiming
				if(doneAligning)  //when auto aim is done, transition to waiting
					state = ShooterState.WAITING; 
				break;
			}
			case WAITING: {//wait for specified time, either for intake to go down or the catapult to release
				if(initialized && !shooting && System.nanoTime() / 1000000 - start > intakeDelay)
					state = ShooterState.SHOOTING; //wait for intake to go down if the robot is about to shoot
				else if(!initialized || (shooting && System.nanoTime() / 1000000 - start > shootingDelay)) 
					state = ShooterState.SETTING; //wait for catapult to shoot when the robot is initializing or shooting
				break;
			}
			case SHOOTING: {//release the catapult
				shooting = true;
				shooterSolenoid.set(true);
				state = ShooterState.WAITING;
				start = System.nanoTime() / 1000000;
				break;
			}
		}
		
		//if the catapult comes off the limit switch, reinitialize the shooter
		if(state == ShooterState.IDLING && set.get() && !opOverride){
			state = ShooterState.INTAKE_DOWN;
			aligning = false;
			initialized = false;
		}
		
		//if the driver / operators release the button, stop the shooting process
		if(!DriveIO.getInstance().getRightBumper() && Controllers.getOperatorJoystick().getRawAxis(2) < 0.5 && state != ShooterState.SETTING && initialized)
			state = ShooterState.IDLING;
		
		//debugging
		if(state != prevState)
			System.out.println("Shooter state: " + state + " Prev State: " + prevState);
		if(set.get() != prevSet)
			System.out.println("Limit Switch: " + set.get());
		
		//operator override in case limit switch isn't triggered
		if(Controllers.getOperatorJoystick().getRawButton(7) && !prevOpOverride){
			opOverride = !opOverride;
			System.out.println("OP Override: " + opOverride);
		}
		
		prevOpOverride = Controllers.getOperatorJoystick().getRawButton(7);
		prevState = state;
		prevSet = set.get();
		prevPOV = Controllers.getOperatorJoystick().getPOV(0);
		RobotDashboard.putString("ShooterState", state.getEnumVal());
	}
	
	public void setAutoAligned(boolean aligned){
		doneAligning = aligned;
		System.out.println("Set Aligning: " + aligned);
	} 
	
	public boolean isShooting(){
		return state != ShooterState.IDLING;
	}
	
	public boolean endAutoAligned(){
		return shooting;
	}
}
