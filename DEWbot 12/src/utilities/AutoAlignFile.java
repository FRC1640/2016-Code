package utilities;

public class AutoAlignFile extends FileManager{
	//stores the aim (measured in pixels from the left side of the camera image) in a file so that it can be reused between power cycles
	//the aim can be offset to the right or left manually so that the aim lines up with where the ball hits the high goal on the camera.
	
	private static String filePath = "/home/lvuser/autoAlignFile.csv"; 
	private static AutoAlignFile autoAlignFile;
	
	public static AutoAlignFile getInstance(){ //get singleton instance
		if(autoAlignFile == null)
			autoAlignFile = new AutoAlignFile(filePath);
		return autoAlignFile;
	}
	
	private AutoAlignFile(String filePath){
		super(6, filePath);
	}
	
	@Override 
	public String readLine(){ //read the aim value
		String line = super.readLine().trim();
		super.resetFile(); //reset file for the next time it is read
		return line;
	}
	
	public String offsetAim(double offset){ //returns <value in file> + offset
		String file;
		try{
			//add difference to current value
			file = new Double(Double.parseDouble(AutoAlignFile.getInstance().readLine()) + offset).toString();
		}catch(Exception e){
			file = "149";
			System.out.println("Auto align read exception: " + e);
		}
		return file;
	}
}
