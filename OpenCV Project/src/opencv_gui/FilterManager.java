package opencv_gui;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

public class FilterManager {
	private static final boolean gui = false;
	private static ArrayList<Filter> filters;
	private static final int PIC_WIDTH = 640, PIC_HEIGHT = 480, PIC_WIDTH_GUI = 100, PIC_HEIGHT_GUI = 100;
	
	public static void main(String args[]){
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		int picWidth = gui ? PIC_WIDTH_GUI : PIC_WIDTH;
		int picHeight = gui ? PIC_HEIGHT_GUI : PIC_HEIGHT;
		Panel p;// = Panel.getInstance();
		FileGenerator fileGenerator = FileGenerator.getInstance(gui);
		filters = new ArrayList<Filter>();
		filters.addAll(fileGenerator.loadFilters());
		
		if(gui){
			p = Panel.getInstance();
			p.generatePanel(filters.size(), fileGenerator.getInputs().size(), fileGenerator.getSliderNames()); 
			p.loadSliders(fileGenerator.getInputs());
		}
		
		//Mat mat = Highgui.imread("C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\20.jpg");
		Mat mat = Mat.zeros(new Size(picWidth, picHeight), CvType.CV_8UC1);
		
		int begin = 0, end = 0;
		while(true){
			for(int i = 0; i < filters.size(); i++){
				if(gui){
					if(i == 1){begin = 0; end = 5;}
					if(i == 2){begin = 6; end = 6;}
					if(i == 3){begin = 7; end = 14;} //TODO: better solution
					filters.get(i).setInputs(getInputs(p.getSliderValues(), begin, end));
				}
				filters.get(i).execute(mat, mat, picWidth, picHeight);
				mat = filters.get(i).getMat();
				if(gui){
					p.generateFile(filters);
					p.updateLabel(filters.get(i).getMat(), i, i == 0, PIC_WIDTH_GUI, PIC_HEIGHT_GUI);
				}
			}
			try{
				Thread.sleep(20);
			}catch(Exception e){e.printStackTrace();}
		}
		
	}
	
	private static int[] getInputs(int[] allInputs, int beginIndex, int endIndex){
		int[] inputs = new int[endIndex - beginIndex + 1];
		for(int i = beginIndex; i <= endIndex; i++){
			inputs[i - beginIndex] = allInputs[i];
		}
		return inputs;
	}
}
