package drivetrain;

public class ArcadeDriveControl extends DriveControl{//basic drive control, arcade style
	
	private static ArcadeDriveControl arcadedrivecontrol;
	
	private ArcadeDriveControl(){}
	
	public static ArcadeDriveControl getInstance(){//get singleton instance
		if(arcadedrivecontrol == null) {
			arcadedrivecontrol = new ArcadeDriveControl();
		}
		return arcadedrivecontrol;
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		super.execute(y1 + x2, y1 - x2, 0, 0); //set motor speeds based on joystick values
	}
}
