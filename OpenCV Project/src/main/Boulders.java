package main;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Boulders {
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Highgui.imread("C:\\Users\\Laura\\Documents\\10th Grade Work\\2016 FRC Stronghold\\Boulders.jpg");
		Mat circles = new Mat();
		//Imgproc.resize(image, image, new Size(320, 240));
		Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
		Imgproc.HoughCircles(image, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 1);
		//Core.circle(image, , radius, color);
		//System.out.println("test");
		Mat newImage = Mat.zeros(new Size(320, 240), CvType.CV_8UC1);
		for(int i = 0; i < circles.rows(); i++){
			Core.circle(newImage, new Point(circles.get(0, 0)[i], circles.get(1, 0)[i]), 50, new Scalar(127));
		}
		Highgui.imwrite("C:\\Users\\Laura\\pictures\\OpenCV\\Boulder.jpg", newImage);
		System.out.println(circles);
	}
}
