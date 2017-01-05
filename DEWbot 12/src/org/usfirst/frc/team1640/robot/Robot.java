package org.usfirst.frc.team1640.robot;

import auton.Auton;
import auton.ScriptSelector;
import drivetrain.DriveIO;
import drivetrain.DriveMaster;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import hangers.Hanger;
import intake.Intake;
import portcullisLift.PortcullisLift;
import shooter.Shooter;
import utilities.AutoAlignFile;
import utilities.Controllers;
import utilities.RobotDashboard;

public class Robot extends SampleRobot {
	/*This is 1640's Robot class. This class itself does very little
	*Each subsystem has its own thread which runs its code
	*
	*There are a few common design patterns that we worked into this code
	*First, there are quite a few "Singletons" throughout the code
	*Second, the drive code contains a "decorator" pattern
	*
	*For more information, please see below:
	*https://www.tutorialspoint.com/design_pattern/singleton_pattern.htm
	*https://www.tutorialspoint.com/design_pattern/decorator_pattern.htm
	*
	*We complete our vision processing on a raspberry pi
	*The results are then published to the network tables
	*The code for turning the robot towards the goal can be found in the "VisionDriveDecorator" class
	*/
	
	public static enum State { DISABLED, AUTON, TELEOP };
	
	private static State state, pState;


	private double delay;
	private double startTime;
	private DriveMaster drive;
	private Auton auton;
	private int i;
	private boolean init = false;
	private DriveIO driveIO;
	private PortcullisLift portcullis;
	private Intake intake;
	private Shooter shooter;
	private Hanger hangers;
	
  
    public Robot() {
    	state = pState = State.DISABLED;
    	Controllers.initIO();
    	
    	//start all threads
    	drive = new DriveMaster();
    	drive.start(20);
    	auton = new Auton();
    	auton.start(20);
    	portcullis = new PortcullisLift();
    	portcullis.start(40);
    	shooter = new Shooter();
    	shooter.start(20);
    	hangers = new Hanger();
    	hangers.start(20);
    	Compressor c = new Compressor();
    	c.start();
    }	
    
    public void robotMain(){
    	try{ //initialize auto align file that holds the target for auto aim
    		AutoAlignFile.getInstance().rewriteFile(AutoAlignFile.getInstance().offsetAim(0));
    	}catch(Exception e){System.out.println("Auto Align File Error");}
    	
    	while (true) {
    		startTime = (double) System.nanoTime() / 1000000000;
    		
    		
    		try{ //update dashboard
    			RobotDashboard.update();
    		}catch(Exception e){System.out.println("Error with RobotDashboard: " + e);}
    		
    		if (super.isAutonomous() && super.isEnabled()){
    			state = State.AUTON;
        	}
        	else if (super.isOperatorControl() && super.isEnabled()){
        		state = State.TELEOP;
        		
        	}
        	else {
        		state = State.DISABLED;
        		
        		try{//initalize robot dashboard
        			RobotDashboard.getInstance();
        		}catch(Exception e){System.out.println("Error with Robot Dashboard getInstance(): " + e);}
    			
        		try{//update script selector
    				ScriptSelector.getInstance().update();
    			}catch(Exception e){System.out.println("Error with Script Selector: " + e);} 

        	}
    		
    		//run robot no more than every 20ms
    		pState = state;
    		delay = 0.02 - ((double) System.nanoTime() / 1000000000 - startTime);
    		delay = delay <= 0 ? 0 : delay;
    		Timer.delay(delay);
    		
    		//reset gyro
    		i++;
    		if(i > 100 && !init){
    	    	DriveIO.getInstance().resetGyro();
    	    	init = true;
    		}
    		
    	}
    }
    
   public static State getState(){
	   return state;
   }
    
}
