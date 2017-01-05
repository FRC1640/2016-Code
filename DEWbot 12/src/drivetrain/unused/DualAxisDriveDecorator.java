package drivetrain.unused;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import drivetrain.DriveControl;
import drivetrain.DriveControlDecorator;
import drivetrain.DriveIO;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;

public class DualAxisDriveDecorator extends DriveControlDecorator{ //use triggers to drive arcade (not currently in use)
	
	private DriveIO driveIO = DriveIO.getInstance();

	public DualAxisDriveDecorator(DriveControl driveControl) {
		super(driveControl);
	}
	
	public void execute(double x1, double y1, double x2, double y2){
		y1 = driveIO.getRightTrigger() - driveIO.getLeftTrigger();
		super.execute(x2,y1,0,0);
	}

}
