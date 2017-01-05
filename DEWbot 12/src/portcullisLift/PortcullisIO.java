package portcullisLift;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.Controllers;
import utilities.RobotDashboard;
import edu.wpi.first.wpilibj.Solenoid;

public class PortcullisIO {
	private static PortcullisIO portcullisIO;
	private boolean prevButton, state, autonBumper;
	private Solenoid portcullisLift;
	
	private PortcullisIO(){
		//initialize solenoid 
		portcullisLift = new Solenoid(0);
	}
	
	public static PortcullisIO getInstance(){ //get singleton instance
		if(portcullisIO == null)
			portcullisIO = new PortcullisIO();
		return portcullisIO;
	}
	
	public void update(){
		//on rising edge of the button, change the solenoid state
		boolean button = getLeftBumper();
		if(button != prevButton){ 
			state = !state;
			portcullisLift.set(state);
			System.out.println("Left Bumper was hit: " + state);
		}
		prevButton = button; 
		RobotDashboard.putString("LiftState", String.valueOf(state));
	}
	
	private boolean getLeftBumper(){
		if(Robot.getState() == State.TELEOP) //if teleop, use the controller
			return Controllers.getOperatorJoystick().getRawButton(2);
		else if (Robot.getState() == State.AUTON) //if auton, use the set value
			return autonBumper;
		else
			return false;
	}
	
	public void setLeftBumper(boolean bumper){
		autonBumper = bumper;
	}
	
}
