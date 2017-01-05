package drivetrain.unused;

import drivetrain.DriveControl;

public class AntiTipDriveControl extends DriveControl {//not currently in use
	//this is a form of drive control that will be called when we start tipping
	private static AntiTipDriveControl antitipdrivecontrol;
	private AntiTipDriveControl() {
		
	}
	public static AntiTipDriveControl getInstance(){
		if(antitipdrivecontrol == null) {
			antitipdrivecontrol = new AntiTipDriveControl();
		}
		return antitipdrivecontrol;
	}
	public void execute(double x1, double y1, double x2){
		//TODO: Make sure these are supposed to be negative
		super.execute(-1, -1, 0, 0); //ignore the inputs and run with anti-tip values
	}
}
