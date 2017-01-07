package opencv_gui;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class FilterContours extends Filter{
	
	private final int NUM_INPUTS = 8;
	private int[] inputs;
	private Mat mat;
	private ArrayList<MatOfPoint> contours;
	private List<Rect> rectangles;
	private List<Rect> prevRect;
	private NetworkTable table;
	
	public FilterContours(){
		this(new int[0]);
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("10.16.40.2");
		table = NetworkTable.getTable("/OpenCV");
	}
	
	public FilterContours(int[] inputs){
		super();
		this.inputs = new int[NUM_INPUTS];
		setInputs(inputs);
		contours = new ArrayList<MatOfPoint>();
		prevRect = new ArrayList<Rect>();
	}
	
	@Override
	public void execute(Mat src, Mat dst, int picWidth, int picHeight){
		int minArea = inputs[0];
		int maxArea = inputs[1];
		int minWidth = inputs[2];
		int maxWidth = inputs[3];
		int minHeight = inputs[4];
		int maxHeight = inputs[5];
		int minPerim = inputs[6];
		int maxPerim = inputs[7];
		contours = new ArrayList<MatOfPoint>();
		Imgproc.resize(src, dst, new Size(picWidth, picHeight));
		Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		dst = Mat.zeros(new Size(100, 100), CvType.CV_8UC1);
		Imgproc.drawContours(dst, contours, -1, new Scalar(255));
		
		//Find rectangles
		rectangles = new ArrayList<Rect>();
		for(MatOfPoint contour: contours){
			rectangles.add(Imgproc.boundingRect(contour));
		}

		int i = 0;
		while(i < rectangles.size()){
			double area = rectangles.get(i).area();
			int width = rectangles.get(i).width;
			int height = rectangles.get(i).height;
			int perimeter = 2 * rectangles.get(i).width + 2 * rectangles.get(i).height;
			if(removeRectangle(area, minArea, maxArea) 
					|| removeRectangle(width, minWidth, maxWidth) 
					|| removeRectangle(height, minHeight, maxHeight)
					|| removeRectangle(perimeter, minPerim, maxPerim)){
				rectangles.remove(i);
			}else{
				i++;
			}
		}
		drawRectangles(dst, rectangles);
		for(int j = 0; j < rectangles.size(); j++){
			if(prevRect.size() > j && !prevRect.get(j).equals(rectangles.get(j))){
				System.out.println("X: " + rectangles.get(j).x + " Y: " + rectangles.get(j).y);
				try{
					table.putNumber("X", rectangles.get(j).x);
					table.putNumber("Y", rectangles.get(j).y);
					table.putNumber("Area", rectangles.get(j).area());
				}catch(Exception e){e.printStackTrace();}
			}
		}
		mat = dst;
		prevRect = rectangles;
	}
	
	@Override
	public Mat getMat(){
		return mat;
	}
	
	
	private void drawRectangles(Mat img, List<Rect> rectangles){
		for(Rect rectangle: rectangles){
			Core.rectangle(img, rectangle.tl(), rectangle.br(), new Scalar(255));
		}
	}
	
	private boolean removeRectangle(double dimension, int min, int max){
		if(dimension < min || dimension > max){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		String filter = "FilterContours: ";
		for(int input : inputs){
			filter += " " + input + ", ";
		}
		return filter;
	}

	@Override
	public void setInputs(int[] inputs){
		for(int i = 0; i < this.inputs.length; i++){
			this.inputs[i] = inputs[i];
		}
	}
	
	@Override
	public int[] getInputs(){
		return inputs;
	}

}
