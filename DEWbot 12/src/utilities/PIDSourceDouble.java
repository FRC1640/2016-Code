package utilities;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDSourceDouble implements PIDSource{ //allow PID to take an unput of a double
	private double value;
	private PIDSourceType type = PIDSourceType.kDisplacement;
	
	public PIDSourceDouble(double value){
		this.value = value;
	}
	
	public void setValue(double value){ //change the value
		this.value = value;
	}

	@Override
	public double pidGet() { //PID will call this to get the value
		return value;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		type = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return type;
	}

}
