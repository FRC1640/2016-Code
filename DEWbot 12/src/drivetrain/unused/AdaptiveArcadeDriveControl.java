package drivetrain.unused;

import drivetrain.DriveControl;

public class AdaptiveArcadeDriveControl extends DriveControl{ //not currently in use
	private static double max = 1;
	private static AdaptiveArcadeDriveControl adaptive;
	
	private AdaptiveArcadeDriveControl(){}
	
	public static AdaptiveArcadeDriveControl getInstance(){
		if(adaptive == null)
			adaptive = new AdaptiveArcadeDriveControl();
		return adaptive;
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		Double left = y1 + x2;
		Double right = y1 - x2;
		
		band(left, right);
		band(right, left);
		
		super.execute(left, right, 0, 0);
	}
	
	private void band(Double banded, Double corrected){
		if(banded > max || banded < -max){
			corrected += max * (Math.abs(banded) / banded) - corrected;
			banded = max * Math.abs(banded) / banded;
		}
	}
}
