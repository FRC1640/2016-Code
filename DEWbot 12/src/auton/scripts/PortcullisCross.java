package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;
import auton.DriveCommand;
import auton.DriveTimeCommand;
import auton.PortcullisCommand;
import auton.TimeCommand;
import auton.WaitCommand;

public class PortcullisCross extends AutonScript { //script to cross portcullis
	private static PortcullisCross portcullisCross;
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
	
	private PortcullisCross(){
		//lower portcullis arm
		script.add(new PortcullisCommand(true, "portcullisDown"));
		script.add(new TimeCommand(1000, "wait1"));
		script.add(new WaitCommand(script.get(script.indexOf("wait1")), "waitCross"));
		
		//approach portcullis
		script.add(new DriveTimeCommand(0.4, 2500, "drive1"));
		script.add(new WaitCommand(script.get(script.indexOf("drive1")), "waitDrive1"));
		
		//lift portcullis arm
		script.add(new PortcullisCommand(false, "portcullisUp"));
		script.add(new TimeCommand(250, "wait2"));
		script.add(new WaitCommand(script.get(script.indexOf("wait2")), "waitCross"));
		
		//drive under portcullis
		script.add(new DriveCommand(100, 0.5, true, "drive2"));
		script.add(new WaitCommand(script.get(script.indexOf("drive2")), "waitDrive2"));
	}
	
	public static PortcullisCross getInstance(){//get singelton instance
		if(portcullisCross == null)
			portcullisCross = new PortcullisCross();
		return portcullisCross;
	}
	
	@Override
	public ArrayList<AutonCommand> getScript() {
		return script;
	}

}
