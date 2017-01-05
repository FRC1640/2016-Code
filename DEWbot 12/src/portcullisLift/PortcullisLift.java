package portcullisLift;

import intake.IntakeIO;
import utilities.Subsystem;

public class PortcullisLift extends Subsystem{ //thread for portcullis and intake
	private PortcullisIO portcullisIO;
	private IntakeIO intakeIO = IntakeIO.getInstance();
	
	@Override
	public void update() {
		portcullisIO.update(); //update portcullis
		intakeIO.update(); //update intake
	}

	@Override
	public void init() {
		portcullisIO = PortcullisIO.getInstance();
	}
	
}
