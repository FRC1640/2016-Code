package auton;

import shooter.ShooterIO;
import drivetrain.DriveIO;

public class ShootingCommand implements AutonCommand{ //auton command for shooting the boulder (included auto-align)
	private boolean done, prevShooting = false; 
	private String name;
	
	public ShootingCommand(String name){
		this.name = name;
	}
	
	@Override
	public void execute() {
		DriveIO.getInstance().setRightBumper(true); //start shooting
		if(!ShooterIO.getInstance().isShooting() && prevShooting){ //when the robot is done shooting, this command is done and the buton can be reset
			done = true;
			DriveIO.getInstance().setRightBumper(false);
		}
		prevShooting = ShooterIO.getInstance().isShooting();
	}

	@Override
	public boolean isRunning() { //is the command still running
		return !done;
	}

	@Override
	public boolean isInitialized() { //has the command been started
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}
	
	@Override 
	public void reset() {
		done = false;
	}

}
