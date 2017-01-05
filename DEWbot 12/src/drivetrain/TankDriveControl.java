package drivetrain;

public class TankDriveControl extends DriveControl{//control robot in tank mode
	private static TankDriveControl tankdrivecontrol; //singleton instance
	private TankDriveControl() {
		
	}
	public static TankDriveControl getInstance(){ //get singleton instance
		if(tankdrivecontrol == null) {
			tankdrivecontrol = new TankDriveControl();
		}
		return tankdrivecontrol;
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		super.execute(y1, y2, 0, 0); //set motors based on two y-axis
	}
}
