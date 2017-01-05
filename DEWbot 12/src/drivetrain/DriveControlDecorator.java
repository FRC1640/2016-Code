package drivetrain;

public abstract class DriveControlDecorator extends DriveControl {
	//defines the interface for a decorator of DriveControl
	//anything that extends this class can add functionality to other forms of drive control
	
	//this is the original object that the decorator will add functionality to
	private DriveControl driveControl;
	
	public DriveControlDecorator(DriveControl driveControl){
		this.driveControl = driveControl;
	}
	
	public void execute(double x1, double y1, double x2, double y2){
		//default implementation is to execute the original object with no extra functionality
		driveControl.execute(x1, y1, x2, y2);
	}
}
