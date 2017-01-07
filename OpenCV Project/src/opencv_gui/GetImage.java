package opencv_gui;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class GetImage extends Filter{
	private final int NUM_INPUTS = 0;
	private int[] inputs;
	private Mat mat;
	private static VideoCapture video;
	
	public GetImage(){
		this(new int[0]);
		if(video == null){
			video = new VideoCapture();
			video.open("http://FRC:FRC@10.16.40.181/mjpg/video.mjpg");
		}
	}
	
	public GetImage(int[] inputs){
		super();
		this.inputs = new int[NUM_INPUTS];
		setInputs(inputs);
	}
	
	@Override
	public void execute(Mat src, Mat dst, int width, int height){
		//dst = Mat.zeros(new Size(100,100), CvType.CV_8UC1);
		dst = Highgui.imread("C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\20.jpg");
		//video.read(dst);
		Imgproc.resize(dst, dst, new Size(width, height));
		mat = dst;
	}
	
	@Override
	public Mat getMat(){
		return mat;
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
	
	@Override
	public String toString(){
		String getImage = "GetImage: ";
		for(int input : inputs){
			getImage += " " + input + ", ";
		}
		return getImage;
	}


}
