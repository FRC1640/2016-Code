package hangers;

import utilities.Subsystem;

public class Hanger extends Subsystem { //thread for hangers
	private HangerIO hangerIO;
	
	@Override
	public void update(){
		hangerIO.update();
	}
	
	@Override
	public void init(){
		hangerIO = HangerIO.getInstance();
	}

}
