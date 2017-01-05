package drivetrain;

import static constants.Constants.JS_AXIS_LEFT_STICK_X;
import static constants.Constants.JS_AXIS_LEFT_STICK_Y;
import static constants.Constants.JS_AXIS_LEFT_TRIGGER;
import static constants.Constants.JS_AXIS_RIGHT_STICK_X;
import static constants.Constants.JS_AXIS_RIGHT_STICK_Y;
import static constants.Constants.JS_AXIS_RIGHT_TRIGGER;
import static constants.Constants.JS_BUTTON_BACK;
import static constants.Constants.JS_BUTTON_START;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.Controllers;
import utilities.SonarPulse;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveIO {
	
	private AHRS ahrs; //navX
	
	//singleton instance
	private static DriveIO driveIO = null;
	
	//joystick instance
	private Joystick driver = Controllers.getDriverJoystick();
	
	private CANTalon left, right, leftSlave, rightSlave; //drive motors
	
	
	//auton variables
	private boolean autonBack, autonA, autonStart, autonB, autonX, autonY, autonRightBumper, autonLeftBumper;
	private double autonX1, autonY1, autonX2, autonY2, autonLeftTrigger, autonRightTrigger;
	
	private boolean portcullis;
	
	private final double VOLTS_PER_INCH_1240 = 0.012, VOLTS_PER_INCH_1010 = 0.0098; //conversions for sonoars
	private AnalogInput sideSonar;
	private SonarPulse leftSonar, rightSonar;
	
	private double gyroOffset; //used for resetting gyro
	
	private int leftOffset, rightOffset;

	private DriveIO() {
		ahrs = new AHRS(SerialPort.Port.kMXP);
		
		//A ramp rate of 27 will take the robot from 0 to 80% in 300 ms 41 for 200 82 for 100
		int rampRate = 55; //initial ramp rate (can be changed later)
		left = new CANTalon(1);
		left.setVoltageRampRate(rampRate);
		
		leftSlave = new CANTalon(2);
		leftSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftSlave.set(left.getDeviceID());
		
		right = new CANTalon(3);
		right.setVoltageRampRate(rampRate);

		rightSlave = new CANTalon(4);
		rightSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightSlave.set(right.getDeviceID());
		
		leftSonar = new SonarPulse(1, 1, 3, 3);
		rightSonar = new SonarPulse(0, 0, 0, 3);
		sideSonar = new AnalogInput(3);
		

		gyroOffset = ahrs.getYaw(); 

	}
	
	public static DriveIO getInstance() {
		//if the singleton class hasn't been created yet, create one
		if (driveIO == null) 
			driveIO = new DriveIO();
		return driveIO;
	}
	
	private boolean checkState(State state) {
		return state == Robot.getState();
	}
	
	
	//Motor Functions
	public void setLeftMotors(double speed) {
		speed *= 1;
		if(speed >= -1 && speed <= 1) {
			left.set(speed);
			return;
		} else if(speed < -1) {
			left.set(-1);
		} else {
			left.set(1);
		}
	}
	
	public void setRightMotors(double speed) {
		speed *= -1;
		if(speed >= -1 && speed <= 1) {
			right.set(speed);
			return;
		} else if(speed < -1) {
			right.set(-1);
		} else {
			right.set(1);
		}
	}
	
	public void setRampRate(int rampRate){ //change the rate at which the drive motors ramp up
		left.setVoltageRampRate(rampRate);
		right.setVoltageRampRate(rampRate);
	}
	
	public double getRightVoltage() {
		return right.getOutputVoltage();
	}
	
	public double getRightSlaveVoltage() {
		return rightSlave.getOutputVoltage();
	}
	
	public double getLeftVoltage() {
		return left.getOutputVoltage();
	}
	
	public double getLeftSlaveVoltage() {
		return leftSlave.getOutputVoltage();
	}
	
	public int[] getEncoders(){ //get array of current encoder values
		int[] encoder = {leftSlave.getEncPosition() - leftOffset, right.getEncPosition() - rightOffset};
		return encoder;
	}
	
	public void resetEncoders(){ //set the current encoder positions as 0
		leftOffset = leftSlave.getEncPosition();
		rightOffset = right.getEncPosition();
	}
	

	//Joystick functions
	//for all joystick functions, they will either return the value from the controller, or return the value set by auton depending on the robot state
	public boolean getBack(){
		if(checkState(State.TELEOP) && !portcullis)
			return driver.getRawButton(JS_BUTTON_BACK);
		else if (checkState(State.AUTON) || portcullis)
			return autonBack;
		else
			return false;
	}
	
	public boolean getStart(){
		if(checkState(State.TELEOP) && !portcullis)
			return driver.getRawButton(JS_BUTTON_START);
		else if (checkState(State.AUTON) || portcullis)
			return autonStart;
		else
			return false;
	}
	
	public double getX1(){
		SmartDashboard.putNumber("joyStickOneX", driver.getRawAxis(JS_AXIS_LEFT_STICK_X));
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawAxis(JS_AXIS_LEFT_STICK_X);
		else if (checkState(State.AUTON) || portcullis)
			return autonX1;
		else
			return 0;
	}
	
	public double getY1(){
		SmartDashboard.putNumber("joyStickOneY", driver.getRawAxis(JS_AXIS_LEFT_STICK_Y));
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawAxis(JS_AXIS_LEFT_STICK_Y);
		else if (checkState(State.AUTON) || portcullis)
			return autonY1;
		else
			return 0;
	}
	
	public double getX2(){
		SmartDashboard.putNumber("joyStickTwoX", driver.getRawAxis(JS_AXIS_RIGHT_STICK_X));
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawAxis(JS_AXIS_RIGHT_STICK_X);
		else if (checkState(State.AUTON) || portcullis)
			return autonX2;
		else
			return 0;
	}
	public double getY2(){
		SmartDashboard.putNumber("joyStickTwoY", driver.getRawAxis(JS_AXIS_RIGHT_STICK_Y));
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawAxis(JS_AXIS_RIGHT_STICK_Y);
		else if (checkState(State.AUTON) || portcullis)
			return autonY2;
		else
			return 0;
	}
	public double getLeftTrigger(){
		SmartDashboard.putNumber("joyStickOneLeftTrigger", (JS_AXIS_LEFT_TRIGGER));
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawAxis(JS_AXIS_LEFT_TRIGGER);
		else if (checkState(State.AUTON) || portcullis)
			return autonLeftTrigger;
		else
			return 0;
	}
	
	public double getRightTrigger(){
		SmartDashboard.putNumber("joyStickOneRightTrigger", (JS_AXIS_RIGHT_TRIGGER));
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawAxis(JS_AXIS_RIGHT_TRIGGER);
		else if (checkState(State.AUTON) || portcullis)
			return autonRightTrigger;
		else
			return 0;
	}
	
	public boolean getA(){
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawButton(1);
		else if (checkState(State.AUTON) || portcullis)
			return autonA;
		else
			return false;
	}
	
	public boolean getB(){
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawButton(2);
		else if (checkState(State.AUTON) || portcullis)
			return autonB;
		else
			return false;
	}
	
	public boolean getX(){
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawButton(3);
		else if (checkState(State.AUTON) || portcullis)
			return autonX;
		else
			return false;
	}
	
	public boolean getY(){
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawButton(4);
		else if (checkState(State.AUTON) || portcullis)
			return autonY;
		else
			return false;
	}
	
	public boolean getRightBumper(){
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawButton(6);
		else if (checkState(State.AUTON) || portcullis)
			return autonRightBumper;
		else
			return false;
	}
	
	public boolean getLeftBumper(){
		if (checkState(State.TELEOP) && !portcullis)
			return driver.getRawButton(5);
		else if (checkState(State.AUTON) || portcullis)
			return autonLeftBumper;
		else
			return false;
	}
	

	//button values can only be set if the state is auton 	
	public void setBack(boolean backButton){
		if(checkState(State.AUTON))
			autonBack = backButton;
	}
	
	public void setStart(boolean startButton){
		if(checkState(State.AUTON) || portcullis)
			autonStart = startButton;
	}
	
	public void setX1(double x1){
		if(checkState(State.AUTON) || portcullis)
			autonX1 = x1;
	}
	
	public void setY1(double y1){
		if(checkState(State.AUTON) || portcullis)
			autonY1 = y1;
	}
	
	public void setX2(double x2){
		if (checkState(State.AUTON) || portcullis)
			autonX2 = x2;
	}
	
	public void setY2(double y2){
		if(checkState(State.AUTON) || portcullis){
			autonY2 = y2;
		}
	}
	
	public void setLeftTrigger(double leftTrigger){
		if(checkState(State.AUTON) || portcullis){
			autonLeftTrigger = leftTrigger;
		}
	}
	
	public void setRightTrigger(double rightTrigger){
		if(checkState(State.AUTON) || portcullis){
			autonRightTrigger = rightTrigger;
		}
	}
	
	public void setRightBumper(boolean rightBumper){
		if(checkState(State.AUTON) || portcullis){
			autonRightBumper = rightBumper;
		}
	}
	
	
	
	//Gyro functions
	public void resetGyro() {
		gyroOffset = driveIO.ahrs.getYaw(); //use the current gyro as the offset
		System.out.println("Resetting Yaw: " + getYaw());
	}
	public double getYaw() {
		//swith direction of gyro and keep it between 0 and 360
		return -((driveIO.ahrs.getYaw() - 360) - gyroOffset) % 360;
	}
	
	public double getPitch() {
		return driveIO.ahrs.getPitch();
	}
	
	public double getRoll() {
		return driveIO.ahrs.getRoll();
	}
	
	//Sonar functions
	public double getLeftInches(){
		return leftSonar.getVoltage() / VOLTS_PER_INCH_1240;
	}
	
	public double getRightInches(){
		return rightSonar.getVoltage() / VOLTS_PER_INCH_1240;
	}
	
	public void updateSonars(){
		leftSonar.update();
		rightSonar.update();
	}
	
	public double getSideSonarInches(){
		return sideSonar.getVoltage() / VOLTS_PER_INCH_1010;
	}
	
	public void setPortcullis(boolean portcullis){ //not currently in use
		this.portcullis = portcullis;
	}

}
