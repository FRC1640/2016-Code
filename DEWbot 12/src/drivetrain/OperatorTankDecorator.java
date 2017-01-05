package drivetrain;

import utilities.Controllers;

public class OperatorTankDecorator extends DriveControlDecorator { //allow operator to control the in arcade robot when it is in tank mode
	private final double reduction = 0.4;
	
	public OperatorTankDecorator(DriveControl driveControl) {
		super(driveControl);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		//read (and negate) operator joysticks
		double operatorX1 = Controllers.getOperatorJoystick().getRawAxis(0);
		double operatorY1 = -Controllers.getOperatorJoystick().getRawAxis(1);
		double operatorX2 = Controllers.getOperatorJoystick().getRawAxis(4);
		double operatorY2 = -Controllers.getOperatorJoystick().getRawAxis(5);
		
		//find max axis value for operator & driver
		double maxOperator = Math.max(Math.abs(operatorX1), Math.max(Math.abs(operatorY1), Math.max(Math.abs(operatorX2), Math.abs(operatorY2))));
		double maxDriver = Math.max(Math.abs(x1), Math.max(Math.abs(y1), Math.max(Math.abs(x2), Math.abs(y2))));
		
		if(maxOperator > maxDriver){ //only use the controller with the highest value
			//convert operator arcade values to tank
			super.execute(0, reduction * (operatorY1 + operatorX2), 0, reduction * (operatorY1 - operatorX2));
		}
		else
			super.execute(x1, y1, x2, y2); //use driver joysticks
	}

}