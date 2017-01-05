package auton.usused;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import auton.AutonCommand;
import drivetrain.DriveIO;

public class PowerTurnTestCommand implements AutonCommand{ //test for data on turning, not currently in use
	private long startTime;
	private String name;
	private boolean done, firstIteration;

	public PowerTurnTestCommand(String name){
		this.name = name;
	}
	
	@Override
	public void execute() {
		if(firstIteration){
			startTime = System.nanoTime();
		}
		DriveIO driveIO = DriveIO.getInstance();
		String filePath = "C:\\Users\\Laura\\Desktop\\powerTest.csv";
		File f = new File(filePath);
		FileWriter file;
		if(f.exists()){
			f.delete();
		}
		try {
			f.createNewFile();
			file = new FileWriter(f, true);
			
			for(int power = 50; power <= 60; power += 10){
				System.out.println(System.nanoTime());
				for(int time = 1; time <= 2; time++){
					for(int trial = 1; trial <= 1; trial++){
						startTime = System.nanoTime();
						driveIO.setX2(power);
						System.out.println(power);
						while((System.nanoTime() - startTime) / 1000000 < time){
							file.write((int) (System.nanoTime() - startTime));
							file.write((int) driveIO.getYaw());
							file.write(driveIO.getEncoders()[0]);
							file.write(driveIO.getEncoders()[1]);
							file.flush();
						}
						driveIO.setX2(0);
						startTime = System.nanoTime();
						while((System.nanoTime() - startTime) / 1000000 < 2){}
					}
				}
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}
	
	@Override 
	public void reset() {
		done = false;
		firstIteration = true;
	}

}
