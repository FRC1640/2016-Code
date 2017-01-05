package auton;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;
import drivetrain.DriveIO;
import utilities.Subsystem;


public class Auton extends Subsystem{ //class to run auton thread
	private AutonCommand autonCommand;
	private boolean reset;

	@Override
	public void update() { //update method of thread
		
		if(Robot.getState() == State.AUTON && autonCommand.isRunning()){
			if(reset) //if first instance, reset gyro
				DriveIO.getInstance().resetGyro();
			if(autonCommand.isRunning()) //if auton isn't finished, run it
				autonCommand.execute();
			reset = false;
		}
		
		if(Robot.getState() == State.DISABLED && !autonCommand.isRunning() && !reset){ //if auton is finished, reset (only once)
			ScriptRunner.getInstance(true).reset(); //reset script
			DriveIO.getInstance().resetGyro(); //reset gyro
			reset = true;
		}
		
	}

	@Override
	public void init() { //init method of thread
		autonCommand = ScriptRunner.getInstance(true); //get instance of script runner
		ScriptSelector.getInstance(); //initialize script selector
		DriveIO.getInstance().resetGyro(); //reset gyro
	}


}
