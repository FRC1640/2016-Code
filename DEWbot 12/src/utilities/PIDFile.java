package utilities;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class PIDFile extends FileManager{ //file to record data on PIDs for tuning
	private String filePath;
	private PIDController controller;
	private PIDSource source;
	private PIDOutput output;
	private long start;
	private boolean outputDouble;
	
	public PIDFile(String filePath, PIDController controller, PIDSource source, PIDOutput output, boolean outputDouble){
		//record inputs
		this(filePath);
		this.controller = controller;
		this.source = source;
		this.output = output;
		this.outputDouble = outputDouble;
	}
	
	protected PIDFile(String filePath){
		super(10, filePath);
		
		//write headers for columns
		writeFile("dt\t");
		writeFile("setpoint\t");
		if(outputDouble){
			writeFile("process variable\t");
			writeFile("output\n");
		}
		else
			writeFile("process variable\n");
	}
	
	public void write(){ //write data to the file
		//add all data in tab-separated text
		writeFile((System.nanoTime() / 1000000 - start) + "\t"); //dt
		writeFile(controller.getSetpoint() + "\t"); //setpoint
		writeFile(source.pidGet() + "\t"); //process variable
		if(outputDouble) 
			writeFile(((PIDOutputDouble)output).getValue() + "\n"); //output (if applicable)
		else
			writeFile("\n");
		
		start = System.nanoTime() / 1000000;
	}

}
