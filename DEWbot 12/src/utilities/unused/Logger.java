package utilities.unused;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Logger { //not currently in use

	private static boolean initialize = false;
	private static FileWriter fw;
	
	public static void init(String filename){
		if (!initialize) {
			try{
				File f = new File("/home/lvuser/" + filename);
				System.out.println(f.getAbsolutePath());
				if (!f.exists()){
					f.createNewFile();
				}
				fw = new FileWriter(f, true);
				initialize = true;
				System.out.println("Initialized logger: " + filename);
			} catch(Exception E){ System.out.println("Unable to initialize logger: " + filename); E.printStackTrace(System.out); }
		} else {
			System.out.println("A logger already exists");
		} 
	}
	
	public static void destroy(){
		if (initialize){
			try {
				fw.close();
				initialize = false;
				System.out.println("Closed logger");
			} catch (IOException e) {}
		}
	}
	
	public static void write(String print){
		if (initialize){
			try {
				fw.write(print + "\n");
				fw.flush();
			} catch (IOException e) { System.out.println("Unable to print: " + print); }
		}
	}
}