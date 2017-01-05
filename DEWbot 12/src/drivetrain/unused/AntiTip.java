package drivetrain.unused;

import drivetrain.DriveCore;
import drivetrain.DriveIO;

public class AntiTip {//not currently in use
	//this class will tell drive core when we start tipping
	
	//singleton instance
	private static AntiTip antiTip;
	
	private AntiTip(){}
	
	//return (and initalize if necessary) the singleton instance
	public static AntiTip getInstance(){
		if(antiTip == null)
			antiTip = new AntiTip();
		return antiTip;
	}
	
	public void update(){
		DriveIO driveIO = DriveIO.getInstance();
		if(driveIO.getRoll() < -17) //if we have tipped passed 17 degrees, then we are tipping
			DriveCore.getInstance().setAntiTip(true); 
		else DriveCore.getInstance().setAntiTip(false);
	}
}
