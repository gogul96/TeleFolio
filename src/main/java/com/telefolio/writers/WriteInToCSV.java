package com.telefolio.writers;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.telefolio.utils.ConfigProperties;

public class WriteInToCSV {

	private static Logger logger = Logger.getLogger(WriteInToCSV.class);
	
	public static void createCSVFile(String fileName,Collection<?> collection,String header){
		FileWriter fileWriter = null;
		try{
			logger.info("Writing Data in to CSV File {"+fileName+"}");
			fileWriter = new FileWriter(new File(ConfigProperties.BASE_RESULT_PATH+"/datasets/CSV/"+fileName));
			if(!header.isEmpty()){
				fileWriter.append(header).append("\n");
			}
			for(Object object:collection){
				fileWriter.append(object.toString()).append("\n");
			}
			fileWriter.close();
		}
		catch(Exception e){
			logger.fatal("Writing Failed : " +fileName);
			logger.warn(e+","+e.getCause());
		}
	}
}
