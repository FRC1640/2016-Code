package utilities;

import edu.wpi.first.wpilibj.PIDOutput;

public class PIDOutputDouble implements PIDOutput{ //allow PIDs to output directly to a double
	private double value;
	
	public double getValue(){ //return the current value
		return value;
	}

	@Override
	public void pidWrite(double output) { //PID will set the value
		value = output;
	}

}
