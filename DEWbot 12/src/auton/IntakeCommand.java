package auton;

import drivetrain.DriveIO;

public class IntakeCommand implements AutonCommand{//auton command to change the state of the intake
	private boolean done; 
	private double in, out;
	private String name;
	
	public IntakeCommand(double in, double out, String name){
		//record inputs
		this.name = name;
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void execute() {
		if(out > in) //set the correct intake trigger based on the greater input
			DriveIO.getInstance().setLeftTrigger(out);
		else
			DriveIO.getInstance().setRightTrigger(in);
		System.out.println("Set to: " + in + " : " + out);
		done = true;
	}

	@Override
	public boolean isRunning() {
		return !done;
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
	public boolean equals(Object o){ //equal if names are equal
		if(o instanceof String){
			return o == name;
		}
		return false;
	}
	
	@Override 
	public void reset() {}
}
