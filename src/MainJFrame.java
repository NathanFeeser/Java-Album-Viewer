import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.awt.image.DirectColorModel;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;



/**
 * @author Nathan
 * @version 1.0
 * @created 17-Oct-2013 9:50:21 PM
 */
public class MainJFrame extends JFrame {

		//Public Global Variables:
		public static int currentAlbum;
		
	    static JLabel label;
	    //This is the panel on bottom
	    //Params: rows, cols, hgap, vgap
	    JPanel bottomPanel = new JPanel(new GridLayout(0,2,5,10));
	    
	    //Create the plus image for adding albums/images
	    ImageIcon plus = new ImageIcon(MainJFrame.class.getResource ("plus.png"));
	    //Create minus image for deleting albums/images
	    ImageIcon minus = new ImageIcon(MainJFrame.class.getResource ("minus.png"));
	    
	    //Create add/minus album buttons
	    JButton addImageButton = new JButton(plus);
	    JButton deleteImageButton = new JButton(minus);
	    
	    //The uploaded image file
	    File[] imageFiles = null;
	    private final JLabel label_1 = new JLabel("");
	    
	    //For the creation of images
	    private static final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
	    private static final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
	    
	    //Labels for the resolution and name:
	    public static JLabel lblPhotoName = new JLabel("None");
	    public static JLabel lblResol = new JLabel("None");
	    
	    //Directory for images
	    File dir;
	    
	    //The global values for slideshow settings
	    public static int INTERVAL = 1;
	    public static boolean SHUFFLE = false;

	public MainJFrame(){
		super("MainJFrame");
        //Create an instance of TopPanel
        TopPanel topPanel = new TopPanel();
        //This gets the two panes at the top
        JSplitPane top = topPanel.getSplitPane();

        //XXXX: Bug #4131528, borders on nested split panes accumulate.
        //Workaround: Set the border on any split pane within
        //another split pane to null. Components within nested split
        //panes need to have their own border for this to work well.
        top.setBorder(null);
        //Create a regular old label
        label = new JLabel("Click on an image name in the list.",
                           JLabel.LEFT);
        bottomPanel.add(label);
        
        //Create a split pane and put "top" (a split pane)
        //and JLabel instance in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              top, bottomPanel);
        splitPane.setBounds(0, 0, 784, 399);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(500);

        //Provide minimum sizes for the two components in the split pane
        top.setMinimumSize(new Dimension(1000, 600));
        //This label is for the single area on bottom area.
        bottomPanel.setMinimumSize(new Dimension(1000, 600));
        getContentPane().setLayout(null);

        //Add the split pane to this frame
        getContentPane().add(splitPane);
        //topPanel.getAlbumList().addListSelectionListener(this);
        
        //Create the panel for the add/remove selected image column
        JPanel addRemoveImagePanel = new JPanel();
        addRemoveImagePanel.setBounds(10, 410, 187, 90);
        getContentPane().add(addRemoveImagePanel);
        addImageButton.setBounds(37, 31, 50, 41);
        
        //Add event listener to the image buttons
        addImageButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e)
            {
                //button is pressed
            	JFileChooser chooser = new JFileChooser();
            	chooser.setMultiSelectionEnabled(true);
            	//display the open file dialog
                int returnValue = chooser.showOpenDialog( null );
                //if it is successful...
                if( returnValue == JFileChooser.APPROVE_OPTION ) {
                	imageFiles = chooser.getSelectedFiles();
                	try {
                		for(int i=0; i<imageFiles.length; i++)
                		{
							Image img = Toolkit.getDefaultToolkit().createImage(imageFiles[i].getPath());
							PixelGrabber pg = new PixelGrabber(img, 0, 0, -1, -1, true);
							pg.grabPixels();
							int width = pg.getWidth(), height = pg.getHeight();
							
							DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
							WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
							BufferedImage bi = new BufferedImage(RGB_OPAQUE, raster, false, null);
							
							String albumName = TopPanel.getAlbums().albumList.getSelectedValue();
							ImageIO.write(bi, "jpg",new File("Albums/" + albumName + "/" + imageFiles[i].getName()));
							dir = new File("Albums/" + albumName);
							
                		}
                		Album.loadAlbumImages(dir);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }});
        deleteImageButton.setBounds(97, 31, 50, 41);
        deleteImageButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e)
            {
                //button is pressed
            	
            	String albumName = TopPanel.getAlbums().albumList.getSelectedValue();
            	int selectedImageIndex = Album.iconList.getSelectedIndex();
            	File file = new File("Albums/" + albumName + "/" + Album.iconNames.get(selectedImageIndex));
            	file.delete();
            	
            	dir = new File("Albums/" + albumName);
            	Album.loadAlbumImages(dir);
            }});
        
              addRemoveImagePanel.setLayout(null);
        
              //Set up the add/remove images panel
              JLabel label_2 = new JLabel("Add/Remove Selected Image");
              label_2.setBounds(10, 0, 167, 20);
              addRemoveImagePanel.add(label_2);
              JLabel label_3 = new JLabel("");
              label_3.setBounds(133, 0, 133, 46);
              addRemoveImagePanel.add(label_3);
              JLabel label_4 = new JLabel("");
              label_4.setBounds(266, 0, 133, 46);
              addRemoveImagePanel.add(label_4);
              addRemoveImagePanel.add(addImageButton);
              addRemoveImagePanel.add(deleteImageButton);
              label_1.setBounds(266, 46, 133, 46);
              
              addRemoveImagePanel.add(label_1);
              
              JLabel lblPhotoInfo = new JLabel("Photo Info:");
              lblPhotoInfo.setBounds(257, 412, 69, 14);
              getContentPane().add(lblPhotoInfo);
              
              JLabel lblName = new JLabel("Name:");
              lblName.setBounds(219, 437, 46, 14);
              getContentPane().add(lblName);
              
              JLabel lblResolution = new JLabel("Resolution:");
              lblResolution.setBounds(219, 462, 63, 14);
              getContentPane().add(lblResolution);
              
              lblPhotoName = new JLabel("None");
              lblPhotoName.setBounds(258, 437, 120, 14);
              getContentPane().add(lblPhotoName);
              
              lblResol = new JLabel("None");
              lblResol.setBounds(283, 462, 95, 14);
              getContentPane().add(lblResol);
              
              JLabel lblSlideshow = new JLabel("Slideshow:");
              lblSlideshow.setBounds(538, 412, 69, 14);
              getContentPane().add(lblSlideshow);
              
              JButton btnStart = new JButton("Start");
              btnStart.setBounds(419, 433, 89, 23);
              getContentPane().add(btnStart);
              
              JButton btnStop = new JButton("Stop");
              btnStop.setBounds(518, 433, 89, 23);
              getContentPane().add(btnStop);
              
              JButton btnSettings = new JButton("Settings");
              btnSettings.setBounds(617, 433, 89, 23);
              getContentPane().add(btnSettings);
              
            //When the Start slideshow button is clicked...
          	btnStart.addActionListener(new ActionListener() {  
                  public void actionPerformed(ActionEvent e)
                  {
                	//if there is an album with no images or no album selected
              		if ((Album.albumList.getSelectedValue() == null) || (Album.iconList.getModel().getSize() <= 0))
              		{
              			JOptionPane.showMessageDialog(null, "Please select an album or upload images to current album.", "alert", JOptionPane.ERROR_MESSAGE); 
              		}
              		else 
              		{
              		  Slideshow slideshow = new Slideshow();
                	  slideshow.startSlideshow();
              		}
                  }});
          	
            //When the Stop slideshow button is clicked...
          	btnStop.addActionListener(new ActionListener() {  
                  public void actionPerformed(ActionEvent e)
                  {
                	  if (Slideshow.getJFrame() != null)
                	  {
                		  Slideshow.timer.cancel();
                		  Slideshow.getJFrame().setVisible(false);
                		  Slideshow.getJFrame().dispose();
                	  }
                  }});
          	
          //When the Slideshow Settings button is clicked...
          	btnSettings.addActionListener(new ActionListener() {  
                  public void actionPerformed(ActionEvent e)
                  {
                	Slideshow.setSlideshowSettings();
                  }});
	}

	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new MainJFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 550));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}