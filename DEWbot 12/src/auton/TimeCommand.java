package auton;

public class TimeCommand implements AutonCommand{ //command used to delay for certain time
	private long delay, startTime;
	private boolean firstIteration = true;
	private String name;
	
	public TimeCommand(long delay, String name){
		//record inputs
		this.delay = delay;
		this.name = name;
	}

	@Override
	public void execute() {
	}

	@Override
	public boolean isRunning() {
		if (firstIteration){ //reset start time on first iteration
			startTime = System.nanoTime() / 1000000;
			firstIteration = false;
		}
		return !((System.nanoTime() / 1000000) - startTime >= delay); //command is done if the specified time has passed
	}

	@Override
	public boolean isInitialized() { //is the command initialized
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
		firstIteration = true;
	}
}
