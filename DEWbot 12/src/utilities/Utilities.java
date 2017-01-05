package utilities;

public class Utilities {
	
	public static double deadband(double value, double limit){ //ignore values below a limit and adjust other values to compensate
		boolean neg = value < 0;
		value = Math.abs(value);
		if(value < limit) //if value is below limit, return 0
			return 0;
		else //adjust values to compensate
			return ((neg) ? -1.0 : 1.0) * Math.pow((value - limit) / (1-limit), 2);
	}
	
	public static double[] deadband2 (double value1, double value2, double limit){ //same as deadband but with 2 values returned in an array
		boolean neg1 = value1 < 0;
		boolean neg2 = value2 < 0;
		value1 = Math.abs(value1);
		value2 = Math.abs(value2);
		if (magnitude(value1, value2) < limit) { return new double[] {0.0, 0.0 }; } 
		else { return new double[] { (((neg1) ? -1.0 : 1.0) * Math.pow((value1 - limit) / (1-limit), 2)), (((neg2) ? -1.0 : 1.0) * Math.pow((value2 - limit) / (1-limit), 2)) }; }
	}
	
	public static double angle(double x, double y){ //angle between two vectors
		return Math.toDegrees(Math.atan2(-x, -y)) + 180;
	}
	
	public static double magnitude(double x, double y){ //pythagorean therom
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public static boolean inRange (double min, double max, double val) { //is value in between min and max
		return (val >= min && val <= max);
	}
	
	public static double[] polarToRect(double angle, double magnitude){ //convert polar coordinates to rectangular
		double[] rect = new double[2];
		rect[0] = Math.sin(Math.toRadians(angle)) * magnitude; //X
		rect[1] = Math.cos(Math.toRadians(angle)) * magnitude; //Y
		return rect; 
		
	}
	
	public static double shortestAngleBetween(double current, double target){ //find shortest distance between two gyro angles
		double distance = current % 360 - target;
		double shortestDistance = distance < 0 ? (360 + distance) : (distance - 360); //ensure that the 359-0 gap is counted for
		return (Math.abs(distance) >= 180 ? shortestDistance : distance); //choose shortest distances
	}

}
