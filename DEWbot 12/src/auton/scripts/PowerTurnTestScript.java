package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.usused.PowerTurnTestCommand;

public class PowerTurnTestScript extends AutonScript { //script to run a test for turning, not currently in use
	private static PowerTurnTestScript blank;
	private ArrayList<AutonCommand> script = new ArrayList<AutonCommand>(){
		@Override
		public int indexOf(Object o){
			if (o == null) {
		        for (int i = 0; i < script.size(); i++)
		            if (script.get(i) == null)
		                return i;
		    } else {
		        for (int i = 0; i < script.size(); i++)
		            if (script.get(i).equals(o))
		                return i;
		    }
			return -1;
		}
	};
	
	private PowerTurnTestScript(){
		script.add(new PowerTurnTestCommand("name"));
	}
	
	public static PowerTurnTestScript getInstance(){
		if(blank == null)
			blank = new PowerTurnTestScript();
		return blank;
	}

	@Override
	public ArrayList<AutonCommand> getScript() {
		return script;
	}

}
