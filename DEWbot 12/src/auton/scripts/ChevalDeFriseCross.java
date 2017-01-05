package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.DriveCommand;
import auton.IntakeCommand;
import auton.PortcullisCommand;
import auton.ShootingCommand;
import auton.TimeCommand;
import auton.WaitCommand;

public class ChevalDeFriseCross extends AutonScript { //auton script for crossing the cheval
	private static ChevalDeFriseCross cdfCross;
	private ArrayList<AutonCommand> script = new ArrayList<AutonCommand>(){
		@Override //change indexOf so it compares the names of the objects
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
	
	private ChevalDeFriseCross(){
		//drive to cheval
		script.add(new DriveCommand(24, 0.75, false, "approach"));
		script.add(new WaitCommand(script.get(script.indexOf("approach")), "waitApproach"));
		
		//put cheval arms down
		script.add(new PortcullisCommand(true, "portcullisDown"));
		script.add(new TimeCommand(1000, "wait2"));
		script.add(new WaitCommand(script.get(script.indexOf("wait2")), "waitCross2"));
		
		//drive over cheval
		script.add(new DriveCommand(100, 0.75, false, "cross"));
		script.add(new WaitCommand(script.get(script.indexOf("cross")), "waitTestTurn"));
		
		//raise cheval arms
		script.add(new PortcullisCommand(false, "portcullisUp"));
	}
	
	public static ChevalDeFriseCross getInstance(){ //get singleton instance
		if(cdfCross == null)
			cdfCross = new ChevalDeFriseCross();
		return cdfCross;
	}
	@Override
	public ArrayList<AutonCommand> getScript() {
		return script;
	}

}
