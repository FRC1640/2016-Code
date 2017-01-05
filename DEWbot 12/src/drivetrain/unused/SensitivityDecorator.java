package drivetrain.unused;

import drivetrain.DriveControl;
import drivetrain.DriveControlDecorator;

public class SensitivityDecorator extends DriveControlDecorator{//change senstivity of driver inputs (not currently in use)
	private DriveControl driveControl;

	public SensitivityDecorator(DriveControl driveControl) {
		super(driveControl);
		this.driveControl = driveControl;
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		super.execute(changeSensitivity(x1), changeSensitivity(y1), changeSensitivity(x2), changeSensitivity(y2));
	}
	
	private double changeSensitivity(double joystick){
		//return Math.pow(joystick, 3);
		//return Math.sin(1.5 * joystick);
		//return 0.72 * Math.atan(5 * joystick);
		return -(2.02 * Math.pow(joystick, 5) - 8.78 * Math.pow(.7 * joystick, 3));
	}

}
