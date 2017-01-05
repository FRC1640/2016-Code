package drivetrain.unused;

import drivetrain.DriveControl;
import drivetrain.DriveControlDecorator;

public class InertiaDecorator extends DriveControlDecorator { //drive decorator that accounts for the inertia of the robot, not currently in use
	private DriveControl driveControl;
	private double prevX1 = 0, prevY1 = 0, prevX2 = 0, prevY2 = 0;
	
	public InertiaDecorator(DriveControl driveControl) {
		super(driveControl);
		this.driveControl = driveControl;
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		double newX1 = inertia(x1, prevX1);
		double newY1 = inertia(y1, prevY1);
		double newX2 = inertia(x2, prevX2);
		double newY2 = inertia(y2, prevY2);
		
		prevX1 = x1;
		prevY1 = y1;
		prevX2 = x2;
		prevY2 = y2;
		
		super.execute(newX1, newY1, newX2, newY2);
	}
	
	private double inertia(double joystick, double prevJoy){
		return (joystick == 0 ? 0 : joystick + ((joystick - prevJoy) * (5 + (0.5 * Math.pow(Math.abs(joystick), 2)))/*((0.75 / Math.abs(joystick)))*/));
		
	}
}
