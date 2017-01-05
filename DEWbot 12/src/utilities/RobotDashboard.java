package utilities;

import drivetrain.DriveIO;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotDashboard { //class to put connect to smart dashboard
	
	private static RobotDashboard dashboard;
	
	public static void init() {
		
	}
	
	public static void update() {
		//update gyro values
		SmartDashboard.putNumber("getYaw", DriveIO.getInstance().getYaw());
		SmartDashboard.putNumber("getPitch", DriveIO.getInstance().getPitch());
		SmartDashboard.putNumber("getRoll", DriveIO.getInstance().getRoll());
			
	}
	
	
	public static RobotDashboard getInstance() { //get singleton instance
		if(dashboard == null) {
			init();
		}
		return dashboard;
	}
	
	public static String getAutonPosition(){ //get which position auton will cross

		return SmartDashboard.getString("Position", "");
	}

	
	public static String getAutonObstacle(){ //get which obstacle auton will cross
		
		return SmartDashboard.getString("Obstacle", "");
	}
	
	public static String getAutonShooterSetting() { //get whether auton will shoot
		
		return SmartDashboard.getString("Shooter Setting", "");
	}
	
	public static void putNumber(String key, int value) { //put number on dashboard
		SmartDashboard.putNumber(key, value);
	}
	
	public static void putString(String key, String value) { //put string on dashboard
		SmartDashboard.putString(key, value);
	}
	
	public static void putBoolean(String key, boolean value) { //put boolean on dashboard
		SmartDashboard.putBoolean(key, value);
	}
}
