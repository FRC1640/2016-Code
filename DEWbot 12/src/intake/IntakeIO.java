
package intake;

import utilities.Controllers;
import drivetrain.DriveIO;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeIO{
	private static IntakeIO intakeIO;
	private Solenoid intake;
	private CANTalon intakeCANTalon;
	private boolean shooting = false; //true;
	private long start = System.nanoTime() / 1000000;
	private double wait = 500;
	
	private IntakeIO(){
		intake = new Solenoid(1);
		intakeCANTalon = new CANTalon(6);
	}
	
	public static IntakeIO getInstance(){
		if(intakeIO == null) 
			intakeIO = new IntakeIO();
		return intakeIO;
	}
	
	public void update(){
		if(shooting){ //put the intake down if the catapult is going to be fired
			intake.set(true);
		}
		else{
			//determine speed of intake motor & state of cylinder
			double reduction = 0.57;
			double leftTrigger = DriveIO.getInstance().getLeftTrigger() * reduction;
			double rightTrigger = DriveIO.getInstance().getRightTrigger() * reduction;
			double speed = leftTrigger > rightTrigger ? -leftTrigger : rightTrigger;
			boolean out = leftTrigger > rightTrigger ? false : true;
			
			if(Math.abs(speed) > 0.23){ //only run the intake if its speed is greater than .23
				intake.set(out); //set cylinder
				
				//wait to intake to avoid the intake running against the catapult
				if((System.nanoTime() / 1000000 - start > wait && speed > 0) || speed < 0){ 
					intakeCANTalon.set(speed);
				}
			}
			else{
				intake.set(false);
				intakeCANTalon.set(0);
				start = System.nanoTime() / 1000000;
			}
		}
	}
	
	public void setShooting(boolean shooting){
		this.shooting = shooting;
	}
}
