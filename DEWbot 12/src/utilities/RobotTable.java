package utilities;

import static constants.Constants.JS_AXIS_LEFT_STICK_X;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import drivetrain.DriveIO;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RobotTable { //not currently in use
	
	private static NetworkTable table;
	
	public static void init() {
		table = NetworkTable.getTable("dashboard");
		table.putString("Position", "default");
		table.putString("Obstacle", "default");
		table.putString("ShooterSetting", "default");
		
	}
	
	public static void update() {
	
		RobotTable.getInstance().putNumber("getYaw", DriveIO.getInstance().getYaw());
		RobotTable.getInstance().putNumber("getPitch", DriveIO.getInstance().getPitch());
		RobotTable.getInstance().putNumber("getRoll", DriveIO.getInstance().getRoll());	
	}
	
	
	public static NetworkTable getInstance() {
		if(table == null) {
			init();
		}
		return table;
	}
	
	public static String getAutonPosition(){ 

		return table.getString("Position", "");
	}

	
	public static String getAutonObstacle(){ 
		
		return table.getString("Obstacle", "");
	}
	
	public static String getAutonShooterSetting() {
		
		return table.getString("ShooterSetting", "");
	}
	

}
