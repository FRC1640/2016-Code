package auton;

import portcullisLift.PortcullisIO;

public class PortcullisCommand implements AutonCommand{ //auton command to change state of the portcullis / cheval arms
	private boolean done, out;
	private String name;
	
	public PortcullisCommand(boolean out, String name){
		//record inputs
		this.name = name;
		this.out = out;
	}

	@Override
	public void execute() { 
		PortcullisIO.getInstance().setLeftBumper(out); //set portcullis state
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
	
	public String getName(){
		return name;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}
	
	@Override 
	public void reset() {
		done = false;
	}

}
