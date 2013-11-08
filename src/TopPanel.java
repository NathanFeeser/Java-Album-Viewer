import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

//TopPanel itself is not a visible component.
public class TopPanel extends JPanel
        implements ListSelectionListener {
    private JLabel picture;
    
    private static JSplitPane splitPane;
    
    private static Album albums;
    
    
    
    public TopPanel() {
        //Loads the albums menu
        albums = new Album();

        //Create a split pane with the two scroll panes in it.
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        		albums.getListPane(), albums.getPicturePane());
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(180);

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(1000, 600));
    }

    /**
     * Creates an ImageIcon if the path is valid.
     * @param String - resource path
     * @param String - description of the file
     */
    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    //Listens to the list
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList)e.getSource();
        //Update the currently selected album
        if (list.getSelectedIndex() != -1)
        {
        	MainJFrame.currentAlbum = list.getSelectedIndex();
        	//updateLabel(albumNames[list.getSelectedIndex()]);
        }
    }

    public static JSplitPane getSplitPane() {
        return splitPane;
    }
    
    public static Album getAlbums() {
    	return albums;
    }
}
