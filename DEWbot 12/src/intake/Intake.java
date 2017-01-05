package intake;

import utilities.Subsystem;

public class Intake extends Subsystem{ //thread for intake 
	//not currently in use, intake is controlled from portcullis thread
	private IntakeIO intakeIO;
	
	@Override
	public void update(){
		//intakeIO.update();	
	}

	@Override
	public void init(){
		intakeIO = IntakeIO.getInstance();
	}
}
