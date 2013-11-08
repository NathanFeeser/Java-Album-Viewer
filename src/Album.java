import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



/**
 * @author Nathan
 * @version 1.0
 * @created 17-Oct-2013 9:50:21 PM
 */
public class Album extends JPanel
	implements ListSelectionListener {
	
	public static String albumDir = "Albums/";

	public static JList<String> albumList;
	public static JList iconList;
	static DefaultListModel listModel = new DefaultListModel();
	public static Vector<String> albums;
	static JLabel albumLabel;
	static JLabel picture;
	//Vectors to hold currently loaded image icon info
	public static Vector<String> iconWidthHeight;
	public static Vector<String> iconNames;
	
	//Create the plus image for adding albums/images
    ImageIcon plus = new ImageIcon(MainJFrame.class.getResource ("plus.png"));
    //Create minus image for deleting albums/images
    ImageIcon minus = new ImageIcon(MainJFrame.class.getResource ("minus.png"));
    
    //Create add/minus album buttons
    JButton addAlbumButton = new JButton(plus);
    JButton deleteAlbumButton = new JButton(minus);
    
    //top-left list panel for the album list.
    private JScrollPane listScrollPane;
    //Top-right pane for image icons
    private static JScrollPane pictureScrollPane;
    
    //array of file extensions to search for when loading images
    static final String[] EXTENSIONS = new String[]{
        "gif", "png", "bmp", "jpg", "jpeg"
    };

	public Album(){
		//Create the vector of albums
    	albums = new Vector<String>();
    	//Create vectors for currently displayed images resolution and names
    	iconWidthHeight = new Vector<String>();
    	iconNames = new Vector<String>();
    	//Creates a panel for the album list and label:
    	JPanel albumPanel = new JPanel();
        albumPanel.setLayout(null);
        
        albumLabel = new JLabel();
        albumLabel.setBounds(54, 21, 69, 37);
        albumLabel.setText("Albums:");
        //albumLabel.setFont(picture.getFont().deriveFont(Font.ITALIC));
        albumLabel.setHorizontalAlignment(JLabel.CENTER);
        albumPanel.add(albumLabel);
        //Creates a panel for the album add/remove buttons:
        JPanel albumAddRemovePanel = new JPanel();
        albumAddRemovePanel.setBounds(0, 243, 177, 51);
        albumAddRemovePanel.add(addAlbumButton);
        albumAddRemovePanel.add(deleteAlbumButton);
        
      //Add event listener to the album buttons
        addAlbumButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e)
            {
            	createAlbum();
            }});
        deleteAlbumButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e)
            {
                //button is pressed
            	int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete the selected album along with all images?","Warning", JOptionPane.YES_NO_OPTION);
            	if(dialogResult == JOptionPane.YES_OPTION){
	            	//Remove the selected item
	            	File directory = new File(Album.albumDir + albumList.getSelectedValue());
	            	deleteFolder(directory);
	            	Album.albums.remove(albumList.getSelectedValue());
	            	albumList.setListData(Album.albums);
	            	pictureScrollPane.removeAll();
	            	pictureScrollPane.repaint();
	            	MainJFrame.lblPhotoName.setText("None");
					MainJFrame.lblResol.setText("None");
            	}
            }});
        
        	//Create the list of images and put it in a scroll pane.
        albumList = new JList<String>(Album.albums);
        albumList.setBounds(0, 53, 177, 179);
        albumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        albumList.setSelectedIndex(0);
        albumList.addListSelectionListener(this);
        	albumPanel.add(albumList);
        	albumPanel.add(albumAddRemovePanel);


		listScrollPane = new JScrollPane(albumPanel);

		picture = new JLabel();
		picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
		picture.setHorizontalAlignment(JLabel.CENTER);

		pictureScrollPane = new JScrollPane(picture);
		
		//Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        pictureScrollPane.setMinimumSize(minimumSize);
	}

	public void finalize() throws Throwable {

	}

	public static void createAlbum(){
    	String inputValue = JOptionPane.showInputDialog("Please enter the new album name:");
    	if (inputValue != null)
    	{
    		new File(albumDir + inputValue).mkdir();
    		albums.add(inputValue);
    		albumList.setListData(albums);
    		albumList.setSelectedIndex(albums.indexOf(inputValue));
    	}
	}
	
	//Function to delete a folder and its contents
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
        loadAlbumImages(folder);
    }

	public static void loadAlbumImages(File dir){
		if (dir.isDirectory()) { // make sure it's a directory
			//The panel to add all images to
			//JPanel imagePanel = new JPanel();
			int count = 0;
			listModel.clear();
			iconNames.clear();
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                try {
                    ImageIcon img = new ImageIcon(ImageIO.read(f));
                    //Update the vectors for image resolution and name
                    String iconRes = img.getIconWidth() + "x" + img.getIconHeight();
                    iconWidthHeight.add(count, iconRes);
                    iconNames.add(count, f.getName());
                    //Scale the image to 64x64
                    img = new ImageIcon(img.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
                    listModel.add(count++, img);
                } catch (final IOException e) {
                    // handle errors here
                }
            }
            iconList = new JList(listModel);
            iconList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            iconList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            iconList.setVisibleRowCount(-1);
            iconList.addListSelectionListener(iconListListener);
            //Add the images panel to the scroll pane
            pictureScrollPane.getViewport().add(iconList, null);
            pictureScrollPane.setPreferredSize(new Dimension(350, 80));
        }
	}
	// filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
	
	public JScrollPane getListPane(){
		return listScrollPane;
	}
	
	public JScrollPane getPicturePane(){
		return pictureScrollPane;
	}
	
	//Used by MainJFrame
    public JList getAlbumList() {
        return albumList;
    }

    //Listens to the abums list
	public void valueChanged(ListSelectionEvent arg0) {
		File dir = new File("Albums/" + albumList.getSelectedValue()); 
		loadAlbumImages(dir);
	}
	
	private static ListSelectionListener iconListListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			//TODO: update the selected image resolution and name.
			//Use: MainJFrame.lblPhotoName.setText(f.getName());
			//and MainJFrame.lblResol.setText(f.getName());
			int listIndex = iconList.getSelectedIndex();
			if (iconList.getSelectedIndex() != -1)
			{
				MainJFrame.lblPhotoName.setText(iconNames.get(listIndex));
				MainJFrame.lblResol.setText(iconWidthHeight.get(listIndex));
			}
		}
	};
}