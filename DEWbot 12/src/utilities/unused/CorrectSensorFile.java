package utilities.unused;


public class CorrectSensorFile { //holds file to log sonars, not currently in use
	private static SensorFile file;
	
	public static SensorFile getInstance(){
		if(file == null){
			file = new SensorFile(1, "/home/lvuser/RecordSonar.csv");
			System.out.println("Creating file");
		}
		return file;
	}

}
