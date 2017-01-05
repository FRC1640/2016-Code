package utilities;

import static constants.IOConstants.JOYSTICK_DRIVER;
import static constants.IOConstants.JOYSTICK_OPERATOR;
import static utilities.Utilities.deadband;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers { //class for accessing driver & operator controllers
	private static Controllers io = null; 
	
	private Joystick driver;
	private Joystick operator;
	
	private Controllers(){
		driver = new Joystick(JOYSTICK_DRIVER){
			@Override //deadband axis 
			public double getRawAxis(int axis){
				if (axis == 0 || axis == 1) {
					return Utilities.deadband2(super.getRawAxis(0), super.getRawAxis(1), 0.23)[axis];
				} else if (axis == 4 || axis == 5) {
					return Utilities.deadband2(super.getRawAxis(4), super.getRawAxis(5), 0.23)[axis-4];
				} else {
					return deadband(super.getRawAxis(axis), 0.23);
				}
			}
		};
		operator = new Joystick(JOYSTICK_OPERATOR){
			@Override //deadband axis
			public double getRawAxis(int axis){
				if (axis == 0 || axis == 1) {
					return Utilities.deadband2(super.getRawAxis(0), super.getRawAxis(1), 0.23)[axis];
				} else if (axis == 4 || axis == 5) {
					return Utilities.deadband2(super.getRawAxis(4), super.getRawAxis(5), 0.23)[axis-4];
				} else {
					return deadband(super.getRawAxis(axis), 0.23);
				}
			}
		};
	}
		
	public static void initIO (){ //initialize singleton instance
		if (io == null)
			io = new Controllers();
		
	}
	
	public static Joystick getDriverJoystick(){ //return driver controller object
		return io.driver;
	}
	
	public static Joystick getOperatorJoystick(){ //return operator controller object
		return io.operator;
	}

}

