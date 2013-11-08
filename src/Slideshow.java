import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;




/**
 * @author Nathan
 * @version 1.0
 * @created 17-Oct-2013 9:50:21 PM
 */
public class Slideshow {
	//Values for the interval setting
	public static final String[] intervals = { "1", "2", "3", "4", "5", "6", "7", "8" };
	
	//The main JFrame
	public static JFrame frame;
	private String album = Album.albumList.getSelectedValue();
	private Vector<String> imageNames = Album.iconNames;
	
	//panel for the images
	JPanel panel = new JPanel(new BorderLayout());
	
	JLabel img;
	
	static Timer timer = new Timer();
	int imageCounter = 0;
	int oldRandom = 0;

	public Slideshow(){
		//Create the main label
		JLabel mainLabel = new JLabel();
		Dimension dim = new Dimension(1024, 768);
		mainLabel.setPreferredSize(dim);
		frame = new JFrame("Slideshow");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(mainLabel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.add(panel);
		frame.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent e) {
			        //This will only be seen on standard output.
					timer.cancel();
			    }
		});
	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

	}

	public static void setSlideshowSettings(){
		String settings = (String) JOptionPane.showInputDialog(null, "Set Interval In Seconds", "Slideshow Settings", JOptionPane.QUESTION_MESSAGE, null, intervals, intervals[0]);
		if(settings != null)
		{
			MainJFrame.INTERVAL = Integer.parseInt(settings);
		}
		JCheckBox cb = new JCheckBox("Shuffle Photos?");
		String msg = "Do you want to shuffle the photos?";
		Object[] msgContent = {msg, cb};
		int n = JOptionPane.showConfirmDialog ( null,  msgContent,  "Shuffle", JOptionPane.OK_CANCEL_OPTION);
		if((n != 2) && (n != -1))
		{
			MainJFrame.SHUFFLE = cb.isSelected();
		}
	}

	public void startSlideshow(){
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				//if the frame was closed or slideshow was stopped...
				if (frame == null)
				{
					timer.cancel();
			    	panel.getRootPane().removeAll();
			    	frame.setVisible(false); //you can't see me!
			    	frame.dispose(); //Destroy the JFrame object
				}
				  
			    if (imageCounter<imageNames.size())
			    {
			    	if (MainJFrame.SHUFFLE == true)
			    	{
			    		int newRandom = getRandomInt(imageNames.size());
			    		//this is to prevent the same image being shown twice.
			    		while (newRandom == oldRandom)
			    		{
			    			//Generate a new number until they are different
			    			newRandom = getRandomInt(imageNames.size());
			    		}
			    		//make the new image the old image
			    		oldRandom = newRandom;
			    		
			    		img = new JLabel(new ImageIcon("Albums/" + album + "/" + imageNames.get(oldRandom)));
						panel.removeAll();
						panel.setBackground(Color.BLACK);
						panel.add(img, BorderLayout.CENTER);
						panel.getRootPane().revalidate();
			            panel.repaint();
			    		imageCounter++;
			    	}
			    	else
			    	{
				    	img = new JLabel(new ImageIcon("Albums/" + album + "/" + imageNames.get(imageCounter)));
						panel.removeAll();
						panel.setBackground(Color.BLACK);
						panel.add(img, BorderLayout.CENTER);
						panel.getRootPane().revalidate();
			            panel.repaint();
			            imageCounter++;
			    	}
			    }
			    else
			    {
			    	timer.cancel();
			    	panel.getRootPane().removeAll();
			    	frame.setVisible(false); //you can't see me!
			    	frame.dispose(); //Destroy the JFrame object
			    }
			  }
			}, 100, MainJFrame.INTERVAL * 1000);
	}
	public int getRandomInt(int max) {
		Random generator = new Random();
		int r = generator.nextInt(max);
		return r;
	}
	
	public static JFrame getJFrame(){
		return frame;
	}

}