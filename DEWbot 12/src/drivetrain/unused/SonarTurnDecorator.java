package drivetrain.unused;

import java.io.File;
import java.io.FileWriter;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import drivetrain.DriveControl;
import drivetrain.DriveControlDecorator;
import drivetrain.DriveIO;

public class SonarTurnDecorator extends DriveControlDecorator { //turn robot until distance from sonars are the same, ensuring the robot is straight (not currently in use)
	private DriveIO driveIO = DriveIO.getInstance();
	private File f;
	private FileWriter file;
	private boolean left;
	
	public SonarTurnDecorator(DriveControl driveControl) {
		super(driveControl);
		try{ //save sonars to file for debugging
			String filePath = "/home/lvuser/filter3.csv";
			f = new File(filePath);
			if(f.exists()){
				f.delete();
			}
			f.createNewFile();
			file = new FileWriter(f, true);
		}catch(Exception e){System.out.println("exception: " + e);}
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		try{
			file.write(driveIO.getLeftInches() + "\t");
			file.write(driveIO.getRightInches() + "\n");
		}catch(Exception e){ System.out.println(e); }
		
		driveIO.updateSonars();//update sonars manually
		
		//turn robot based on difference in sonars
		double difference = driveIO.getLeftInches() - driveIO.getRightInches();
		double turn = - (Math.abs(difference) > 0.35 ? 0.25 * difference : 0);
		super.execute(x1, y1, turn, y2);

	}
	


}
