package drivetrain;

import utilities.Controllers;

public class DesensitizedOperatorDecorator extends DriveControlDecorator { //drive feature that allows opertor to have fine control over the robot
	private final double reduction = 0.5;
	
	public DesensitizedOperatorDecorator(DriveControl driveControl) { //must be added to another drive control
		super(driveControl);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		//read (and negate)operator joysick axis
		double operatorX1 = Controllers.getOperatorJoystick().getRawAxis(0);
		double operatorY1 = -Controllers.getOperatorJoystick().getRawAxis(1);
		double operatorX2 = Controllers.getOperatorJoystick().getRawAxis(4);
		double operatorY2 = -Controllers.getOperatorJoystick().getRawAxis(5);
		
		//use the controller that has the largest value
		double maxOperator = Math.max(Math.abs(operatorX1), Math.max(Math.abs(operatorY1), Math.max(Math.abs(operatorX2), Math.abs(operatorY2))));
		double maxDriver = Math.max(Math.abs(x1), Math.max(Math.abs(y1), Math.max(Math.abs(x2), Math.abs(y2))));
		if(maxOperator > maxDriver)
			super.execute(operatorX1 * reduction, operatorY1 * reduction, operatorX2 * reduction, operatorY2 * reduction);
		else
			super.execute(x1, y1, x2, y2);
	}

}
