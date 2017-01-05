package auton.usused;

import auton.AutonCommand;
import utilities.unused.CorrectSensorFile;
import utilities.unused.SensorFile;
import drivetrain.DriveIO;

public class RecordSensorCommand implements AutonCommand{ //not currently in use
	private boolean done, above, firstIteration = true, startRecording;
	private String name;
	private double threshold;
	private SensorFile sonarFile;
	private DriveIO driveIO = DriveIO.getInstance();
	
	public RecordSensorCommand(boolean above, double threshold, String name){
		this.above = above;
		this.threshold = threshold;
		this.name = name;
		sonarFile = CorrectSensorFile.getInstance();
	}
	
	@Override
	public void execute() {
		double inches = driveIO.getSideSonarInches();
		
		
		if(above){
			if(inches < threshold)
				startRecording = true;
			else if(startRecording){
				done = true; 
			}
		}
		
		else{
			if(inches > threshold)
				startRecording = true;
			else if(startRecording)
				done = true; 
		}
		
		if(!done)
			sonarFile.writeSensor(inches);
	}

	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public String getName() {
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
		firstIteration = true;
		done = false;
	}
}
