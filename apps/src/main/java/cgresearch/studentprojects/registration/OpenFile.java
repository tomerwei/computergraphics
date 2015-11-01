package cgresearch.studentprojects.registration;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenFile {
	
	 // JFileChooser-Objekt erstellen
	public OpenFile(){
		
	}
    
    public String Open(){
    	
    	String file = "";
    	
    	FileFilter filter = new FileNameExtensionFilter("Pointcloud", "points");
    	String path = ResourcesLocator.getInstance().getPathToResource("pointclouds");
    	JFileChooser chooser = new JFileChooser(path);
    	  	 
    	chooser.addChoosableFileFilter(filter);
    	JFrame JFrame = new JFrame("Open Pointcloud");
    	JFrame.setSize(450,300);
    	JFrame.getContentPane().add(chooser);
    	JFrame.setVisible(true); 
    	int temp = chooser.showOpenDialog(null);
    	if(temp == JFileChooser.APPROVE_OPTION)
    	{   
    	    file = chooser.getSelectedFile().getAbsolutePath();     
    	}else{
    		   Logger.getInstance().error("File not found");
    	   }
    	    
    		return file;	   
    }
    
    
    
	
}
