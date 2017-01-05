package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public abstract class FileManager { //abstract class for reading/writing files
	private String filePath;
	private FileWriter fileWriter;
	private File f;
	private BufferedReader fileReader;
	private int markCharacters;
	
	protected FileManager(int markCharacters, String filePath){
		this.filePath = filePath;
		this.markCharacters = markCharacters;
		try{
			f = new File(filePath);
			fileWriter = new FileWriter(f, true);
			fileReader = new BufferedReader(new FileReader(filePath));
			fileReader.mark(markCharacters);
		}catch(Exception e){ System.out.println("File Manager Constructor: " + e); }	
	}
	
	public String readLine(){ //read the next line from the file
		String s = new String();
		try{
			s = fileReader.readLine();
		}catch(Exception e){System.out.println("Read Line: " + e);}
		return s;
	}
	
	public void resetFile(){ //reset the file stream
		try{
			fileReader.reset(); 
		}catch(Exception e){System.out.println("Reset File: " + e);}
	}
	
	public void writeFile(String write){ //append given parameter to the file
		try{
			//write to the file
			fileWriter.write(write);
			fileWriter.flush();
		}catch(Exception e){System.out.println("Write file: " + e);}
	}
	
	public void rewriteFile(String write){ //replace file with given parameter
		try{
			if(f.exists()){ //delete existing file
				f.delete();
			}
			
			//create a new file
			f.createNewFile();
			fileWriter = new FileWriter(f, true);
			fileReader = new BufferedReader(new FileReader(filePath));
			fileReader.mark(markCharacters);
			
			//write the parameter to the file
			writeFile(write);
		}catch(Exception e){System.out.println("Rewrite file: " + e);}
	}
}