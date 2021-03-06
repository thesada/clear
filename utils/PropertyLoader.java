package clear.utils;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import clear.driver.TestDriver;
import clear.driver.TestDriver.LogType;


@SuppressWarnings("unused")
/**
* This class reads all the properties file in test.xxx.or and creates one properties object
* @author ptt4kor
* @version 1.0
*/
public class PropertyLoader {

	Properties appOR = null;
	/**
	 * This method loads all properties files to appOR object
	 * @param Folder where properties are located
	 * @return Properties 
	 * @throws 
	 * @author ptt4kor
	 */
	public Properties loadOR(final File folder) {
		appOR = new Properties();
		//from the or folder pass properties files one by one
	    for (final File file: folder.listFiles()) {
	    	String fileName = file.getName().trim();
	    	String fileType = "";
	    	if(fileName.length()>10){
	    	fileType = fileName.substring(fileName.length()-10,fileName.length());
	    	}
	    	
	    	//load only if file type is properties
	    	if(fileType.equalsIgnoreCase("Properties"))
	    	loadPropertyFile(file);
	    }
	    
	    return appOR;
	}
	
	/**
	 * Method to load perticular property file
	 * @param Property file
	 * @return
	 * @throws 
	 * @author ptt4kor
	 */
	public void loadPropertyFile(final File pFile) {
		Properties fileProp = new Properties();
		String fullFileName[] = null;
		String fileName, fileType = null;
		
		try {
			fullFileName = pFile.getName().split("\\.");
			//get file name and file type
			fileName = fullFileName[0].toString();
			fileType = fullFileName[1].toString();
			
			//load file
			InputStream flStream = new FileInputStream(pFile);
			fileProp.load(flStream);
			
			//move all properties to appOR property
			putPropertiesToOR(fileProp,fileName );
			
			//close stream
			flStream.close();
		} 
		
		catch (IOException e) {
			System.out.println("Couldn't load "+pFile.getName());
			e.printStackTrace();
		}
	    
	}
	
	
	/**
	 * Method to copy all properties from one file to ORapp object
	 * @param Properties, propertyFileName
	 * @return
	 * @throws 
	 * @author ptt4kor
	 */
	public void putPropertiesToOR(final Properties prop, String propertyFileName) {
		System.out.println("Loading " + propertyFileName + " prop file");
		TestDriver.ReportLog("Loading " + propertyFileName + " property file", LogType.TEXTLOGONLY);
		String key, value = null;
		
		// get set-view of keys
		Enumeration<?> e = prop.propertyNames();
		while (e.hasMoreElements()) {
		      key = (String) e.nextElement();
		      value = prop.getProperty(key);
		      
		      TestDriver.ReportLog("Adding " + propertyFileName + "." + key, LogType.TEXTLOGONLY);
		      appOR.setProperty(propertyFileName + "." + key, value);
		    }
	}
}
