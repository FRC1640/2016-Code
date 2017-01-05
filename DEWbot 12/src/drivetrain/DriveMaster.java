package drivetrain;

import drivetrain.unused.AntiTip;
import utilities.Subsystem;


public class DriveMaster extends Subsystem{ //controlls drive thread
	private DriveCore driveCore = DriveCore.getInstance();
	private AntiTip antiTip = AntiTip.getInstance();
	
	public void init(){	}
	
	public void update() {
		//antiTip.update();
		driveCore.update(); //update drive code
	}
}
