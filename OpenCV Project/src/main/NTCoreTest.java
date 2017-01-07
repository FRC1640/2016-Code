package main;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables.*;


public class NTCoreTest {
	

	public static void main(String[] args){
		System.out.println(System.getProperty("java.library.path"));
		//System.loadLibrary("ntcore");
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("10.0.0.111");
		NetworkTable table = NetworkTable.getTable("/Root");
		while(!table.isConnected()){}
		System.out.println(table.getNumber("test", 5));
		
	}
}
