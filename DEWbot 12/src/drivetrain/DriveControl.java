package drivetrain;


public abstract class DriveControl { //abstract class for drive controls. Part of the decorator pattern
	private DriveIO driveIO = DriveIO.getInstance();
	
	protected void execute(double x1, double y1, double x2, double y2){
		//set motor speeds
		driveIO.setLeftMotors(x1);
		driveIO.setRightMotors(y1);
	}
}
