package hangers;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import utilities.Controllers;
import utilities.RobotDashboard;

public class HangerIO {
	private static HangerIO hangerIO;
	private Solenoid hangerSolenoid;
	private CANTalon hangerMotor;
	private int prevPOV;
	private boolean prevX, cylinderState;
	
	private String state = "";
	
	private HangerIO(){
		//initialize hanger outputs
		hangerSolenoid = new Solenoid(3);
		hangerMotor = new CANTalon(7);
	}
	
	public static HangerIO getInstance(){ //get singleton instance
		if(hangerIO == null)
			hangerIO = new HangerIO();
		return hangerIO;
	}
	
	public void update(){
		double speed = 1;
		if(getPOV() == 0 || getPOV() == 315 || getPOV() == 45){ //pull hanger up
			hangerMotor.set(-speed);
			state = "up";
		}
		else if(getPOV() == 180 || getPOV() == 135 || getPOV() == 225){ //unspool hanger
			if(getDriverY()){ //only unspool if driver is holding Y
				hangerMotor.set(speed);
				state = "down";
			}
		}
		else{
			hangerMotor.set(0);
			state = "not moving";
		}
		
		//when operator presses X and driver holds Y, put the hanger up/down
		if(getOpX() && !prevX){
			if(getDriverY()){
				//change cylinder state
				cylinderState = !cylinderState; 
				hangerSolenoid.set(cylinderState); 
				state = "change Solenoid";
			}
		}
		
		prevPOV = getPOV();
		prevX = getOpX();
		RobotDashboard.putString("HangState", state);	
	}
	
	private int getPOV(){
		return Controllers.getOperatorJoystick().getPOV(0);
	}
	
	private boolean getOpX(){
		return Controllers.getOperatorJoystick().getRawButton(3);
	}
	
	private boolean getDriverY(){
		return Controllers.getDriverJoystick().getRawButton(4);
	}
}
