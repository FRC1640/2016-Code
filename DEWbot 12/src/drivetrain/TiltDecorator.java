package drivetrain;

import utilities.Controllers;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

public class TiltDecorator extends DriveControlDecorator{ //vibrates controllers when the robot is tilted
	private DriveIO driveIO = DriveIO.getInstance();
	private final double rollAngle = 5, pitchAngle = 5;
	
	public TiltDecorator(DriveControl driveControl){
		super(driveControl);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		//if gyro reads a large enough roll / pitch, rumble controllers
		if(Math.abs(driveIO.getRoll()) > rollAngle || Math.abs(driveIO.getPitch()) > pitchAngle){
			Controllers.getDriverJoystick().setRumble(RumbleType.kRightRumble, (float) 0.5);
			Controllers.getOperatorJoystick().setRumble(RumbleType.kRightRumble, (float) 0.5);
			Controllers.getDriverJoystick().setRumble(RumbleType.kLeftRumble, (float) 0.5);
			Controllers.getOperatorJoystick().setRumble(RumbleType.kLeftRumble, (float) 0.5);
		}
		else{ //turn off rumble
			Controllers.getDriverJoystick().setRumble(RumbleType.kRightRumble, (float) 0);
			Controllers.getOperatorJoystick().setRumble(RumbleType.kRightRumble, (float) 0);
			Controllers.getDriverJoystick().setRumble(RumbleType.kLeftRumble, (float) 0);
			Controllers.getOperatorJoystick().setRumble(RumbleType.kLeftRumble, (float) 0);
		
		}
		
		super.execute(x1, y1, x2, y2);
	}
}
