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
    	
    	FileFilter filter = new FileNameExtensionFilter("Pointcloud", "obj");
    	String path = ResourcesLocator.getInstance().getPathToResource("meshes");
    	JFileChooser chooser = new JFileChooser(path);
    	  	 
    	chooser.addChoosableFileFilter(filter);
    	int temp = chooser.showOpenDialog(null);
    	if(temp == JFileChooser.APPROVE_OPTION)
    	{   
    	    file = chooser.getSelectedFile().getAbsolutePath();  
    	    int index = file.indexOf("assets\\");
    	    file = file.substring(index+7);
    	    
    	}else{
    		   Logger.getInstance().error("File not found");
    	   }
    	    
    		return file;	   
    }
    
    
    
	
}
