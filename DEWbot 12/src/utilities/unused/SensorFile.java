package utilities.unused;

import utilities.FileManager;

public class SensorFile extends FileManager{ //not currently in use
	private int numSensors, currentSensor = 1;
	private long start = System.nanoTime() / 1000000;
	
	public SensorFile(int numSensors, String filePath){
		super(6, filePath);
		rewriteFile("");
		writeFile("dt\t");
		for(int i = 1; i < numSensors; i++){
			writeFile("Sensor " + i + " \t");
		}
		writeFile("Sensor " + numSensors + "\n");
		writeFile((System.nanoTime() / 100000 - start) + "\t");
		this.numSensors = numSensors;
	}
	
	protected SensorFile(String filePath) {
		super(100, filePath);
	}
	
	public void writeSensor(double sensorValue){
		if(currentSensor > numSensors){
			currentSensor = 1;
			writeFile("\n" + (System.nanoTime() / 1000000 - start) + " \t");
			start = System.nanoTime() / 1000000;
		}
		writeFile(sensorValue + "\t");
		currentSensor++;
	}

}
