package utilities;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class SonarPulse { 
	//manually control sonars through digital output to prevent interference between them
	
	private AnalogInput sonar;
	private DigitalOutput pulse;
	private long iterations = 1;
	private int initDelay, delay, digitalPort;
	private boolean read = false;
	private double voltage;
	
	public SonarPulse(int analogPort, int digitalPort, int initDelay, int delay){
		//initialize sonar & digital output to pulse sonar
		sonar = new AnalogInput(analogPort);
		pulse = new DigitalOutput(analogPort);
		this.digitalPort = digitalPort;
		this.delay = delay;
		this.initDelay = initDelay;
	}
	
	public void update(){
		//only read or pulse after a given delay (in iterations)
		//also wait an initial delay
		if(iterations > initDelay && (iterations - 1) % delay == 0){
			if(read){ //read
				voltage = sonar.getVoltage();
			}
			else{ //pulse
				pulse.pulse(digitalPort, (float) 5.0);
			}
			read = !read; //switch from pulse to read or vice versa
		}
		iterations++;
	}
	
	public double getVoltage(){ //return last read voltage
		return voltage;
	}
}
