package auton;

import utilities.unused.CorrectSensorFile;
import drivetrain.DriveIO;

public class CorrectSensorCommand implements AutonCommand{ //not currrently in use
	private boolean done, firstIteration = true, fullSpeed;
	private DriveCommand drive;
	private String name;
	private DriveIO driveIO = DriveIO.getInstance();
	private final int numValues = 5;
	private Double[] sonarValues;
	private int index;
	private double average, speed;
	
	public CorrectSensorCommand(double speed, boolean fullSpeed, String name){
		this.name = name;
		this.speed = speed;
		this.fullSpeed = fullSpeed;
		sonarValues = new Double[numValues];
	}
	
	@Override
	public void execute() {
		if(firstIteration){
			CorrectSensorFile.getInstance().readLine();
			while(true){
				String line = CorrectSensorFile.getInstance().readLine();
				if(line == null)
					break;
				sonarValues[index] = Double.parseDouble(line.split("\t")[1]);
				index++;
				if(index == numValues)
					index = 0;
			}
			double sum = 0;
			for(int i = 0; i < sonarValues.length; i++){
				sum += sonarValues[i];
			}
			average = sum / numValues;
			double distance = (average - (51 / 2) + (25.75 / 2)) / -Math.sin(Math.toRadians(DriveIO.getInstance().getYaw()));
			drive = new DriveCommand((int) Math.abs(distance), Math.abs(distance) / distance * speed, fullSpeed, "Drive");
			System.out.println("Going distance: " + distance);
			firstIteration = false;
		}
		drive.execute();
		System.out.println("in correctSensor");
	}

	@Override
	public boolean isRunning() {
		return drive.isRunning();
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
