package drivetrain.unused;

import drivetrain.DriveControl;
import drivetrain.DriveControlDecorator;
import auton.ScriptRunner;
import auton.scripts.PortcullisCross;

public class PortcullisDecorator extends DriveControlDecorator{//not currently in use
	private ScriptRunner scriptRunner;
	
	public PortcullisDecorator(DriveControl driveControl) {
		super(driveControl);
		scriptRunner = ScriptRunner.getInstance(false);
		scriptRunner.setScript(PortcullisCross.getInstance().getScript());
		scriptRunner.reset();
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		scriptRunner.execute();
		super.execute(x1, y1, x2, y2);
	}

}
