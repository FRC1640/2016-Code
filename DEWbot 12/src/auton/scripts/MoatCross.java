package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.DriveCommand;
import auton.WaitCommand;

public class MoatCross extends AutonScript{ //script to cross moat obstacle
	private static MoatCross defenseCross;
	private ArrayList<AutonCommand> script = new ArrayList<AutonCommand>(){
		@Override//change indexOf so it compares the names of the objects
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
	
	private MoatCross(){		
		//drive across the moat
		script.add(new DriveCommand(200, 0.75, false, "test"));
		script.add(new WaitCommand(script.get(script.indexOf("test")), "testWait"));
	}
	
	public static MoatCross getInstance(){ //get singleton instance
		if(defenseCross == null)
			defenseCross = new MoatCross();
		return defenseCross;
	}
	
	@Override
	public ArrayList<AutonCommand> getScript(){
		return script;
	}
}