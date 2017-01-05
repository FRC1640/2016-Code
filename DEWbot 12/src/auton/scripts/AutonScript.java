package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;

public abstract class AutonScript {//abstract class that holds a script (or a list of commands to complete in auton)
	
	public abstract ArrayList<AutonCommand> getScript();

}
