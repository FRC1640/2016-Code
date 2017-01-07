package opencv_gui;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Panel implements ActionListener{
	private static Panel panel;
	private JLabel[] labels;
	private JSlider[] sliders;
	private JFrame frame = new JFrame();
	private JPanel jPanel = new JPanel();
	private JButton xml = new JButton("XML");
	private ArrayList<Filter> filters = new ArrayList<Filter>();
	
	private final int BORDER_SIZE = 20;
	private final int PICTURE_SIZE = 100;
	private final int FRAME_WIDTH = 640;
	private final int FRAME_HEIGHT = 640;
	private final int SLIDER_HEIGHT = 30;
	private final int SLIDER_LENGTH = 100;
	private final int XML_HEIGHT = 30;
	private final int XML_WIDTH = 100;
	

	private Panel(){}
	
	public static Panel getInstance(){
		if(panel == null)
			panel = new Panel();
		return panel;
	}
	
	public void generatePanel(int numLabels, int numSliders, ArrayList<String> sliderNames){
		labels = new JLabel[numLabels];
		sliders = new JSlider[numSliders];
		
		initPanel();
		
		int x = 3 * PICTURE_SIZE + 2 * BORDER_SIZE;
		x = x + ((FRAME_WIDTH - x - SLIDER_LENGTH) / 2);
		
		//xml.setLayout(null);
		System.out.println(x + " : " + SLIDER_HEIGHT * numSliders);
		xml.setBounds(x, SLIDER_HEIGHT * numSliders, XML_WIDTH, XML_HEIGHT);
		xml.addActionListener(this);
		xml.setEnabled(true);
		jPanel.add(xml);
		
		for(int i = 0; i < numLabels; i++){
			labels[i] = new JLabel();
			jPanel.add(labels[i]);
		}
		
		for(int i = sliderNames.size(); i < sliders.length; i++){
			sliderNames.add("");
		}
		

		int y = 0;
		for(int i = 0; i < numSliders; i++){
			int upperBound = 0;
			if(i <= 5) upperBound = 255;
			if(i == 6) upperBound = 5;
			if(i > 6) upperBound = 2000; //TODO: Better solution, new upperbound for filter contours
			JSlider slider = new JSlider(0, upperBound);
			slider.setBounds(x, y, 100, 30);
			sliders[i] = slider;
			jPanel.add(slider);
			
			JLabel label = new JLabel(sliderNames.get(i));
			label.setBounds(x + SLIDER_LENGTH, y, 100, 30);
			jPanel.add(label);
			
			y += SLIDER_HEIGHT;
		}
		
		jPanel.repaint();
	}
	
	
	public void updateLabel(Mat mat, int index, boolean color, int width, int height){
		jPanel.remove(labels[index]);

		Imgproc.resize(mat, mat, new Size(width, height));
		int x = (index % 3) * (PICTURE_SIZE + BORDER_SIZE);
		int y = (index / 3) * (PICTURE_SIZE + BORDER_SIZE);
		labels[index] =  imageToLabel(matToImage(mat, color), x, y, PICTURE_SIZE, PICTURE_SIZE);
		
		jPanel.add(labels[index]);
		jPanel.repaint();
	}
	
	public int[] getSliderValues(){
		int[] sliderValues = new int[sliders.length];
		for(int i = 0; i < sliders.length; i++){
			sliderValues[i] = sliders[i].getValue();
		}
		return sliderValues;
	}
	
	public void loadSliders(ArrayList<Integer> sliderValues){
		for(int i = sliderValues.size(); i < sliders.length; i++){
			sliderValues.add(0);
		}
		for(int i = 0; i < sliders.length; i++){
			sliders[i].setValue(sliderValues.get(i));
		}
	}
	
	public void generateFile(ArrayList<Filter> filters){
		this.filters = filters;
	}

	private void initPanel(){
		frame.getContentPane().add(jPanel);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(jPanel);
		
		jPanel.setLayout(null);
		//FlowLayout border = new FlowLayout();
		//jPanel.setLayout(border);
		//border.setAlignment(FlowLayout.LEFT);
	}
	
	private JLabel imageToLabel(BufferedImage image, int x, int y, int width, int height){
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon((Image) image));
		label.setBounds(x, y, width, height);
		return label;
	}
	
	private BufferedImage matToImage(Mat mat, boolean color){
		int type = color ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
		byte[] data = new byte[mat.cols() * mat.rows() * (int) mat.elemSize()];
		mat.get(0, 0, data);
		byte b;  
		if(color){
	        for(int i=0; i<data.length; i=i+3) {  
	          b = data[i];  
	          data[i] = data[i+2];  
	          data[i+2] = b;  
	        }  
		}
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return image;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		FileGenerator.getInstance(true).createFile(filters);
	} 

}
