package shooter;

import utilities.Subsystem;

public class Shooter extends Subsystem { //thread for shooter
	private ShooterIO shooterIO;
	@Override
	public void init() {
		shooterIO = ShooterIO.getInstance();
	}

	@Override
	public void update() {
		shooterIO.update();
	}

}
