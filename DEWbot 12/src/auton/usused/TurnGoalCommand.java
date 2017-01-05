package auton.usused;

import auton.AutonCommand;
import drivetrain.DriveIO;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class TurnGoalCommand implements AutonCommand {//not currently in use
	private boolean done;
	private double angle, buffer;
	private double turn, speed = 0.4;
	private DriveIO driveIO;
	private String name;
	private NetworkTable table;
	private double[] defaultValue = {};
	
	public TurnGoalCommand(String name){
		this.name = name;
		driveIO = DriveIO.getInstance();
		table = NetworkTable.getTable("GRIP/vision");
	}

	@Override
	public void execute() {
		driveIO.setX2(speed);
		if(table.getNumberArray("area", defaultValue).length > 0)
			done = true;
	}

	@Override
	public boolean isRunning() {
		if(done){
			driveIO.setX2(0);
		}
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}
	
	public String getName(){
		return name;
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}

	@Override 
	public void reset() {
		done = false;
	}
}
