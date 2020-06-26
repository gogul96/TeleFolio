package com.telefolio.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telefolio.extracters.ManufactureParser;
import com.telefolio.writers.WriteInToCSV;
import com.telefolio.writers.WriteInToJSON;

public class TeleFolioFile {
	
	private static Logger logger = Logger.getLogger(ManufactureParser.class);
	
	public static Properties getProperties(String fileName) {
		Properties properties = new Properties();
		try{
			logger.debug(String.format("Loading %s Properties File",fileName));
			properties.load(new FileInputStream(new File(ConfigProperties.BASE_CONFIG_PATH+fileName+".properties")));
		}
		catch(Exception e ){
			logger.error("Failed to load Properties {"+fileName+ "} File");
			logger.warn(e.getMessage());
		}
		return properties;
	}
	
	public static void writeFile(File file, String fileName, String data) throws IOException {
		try{
			if (file == null) {
				file = new File(fileName);
			}
			logger.debug(String.format("Writing data in to %s",file.getName()));
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(data);
			fileWriter.flush();
			fileWriter.close();
		}catch(IOException e){
			logger.fatal("Writing data Failed : " + fileName);
			logger.warn(e+","+e.getCause());
		}
	}

	public static void writeInToFile(String fileName,Collection<?> collection,String header){
		try{
			if(ConfigProperties.IS_CSV_FILE_TOBE_CREATED){
				WriteInToCSV.createCSVFile(fileName+".csv", collection, header);
			}
			if(ConfigProperties.IS_JSON_FILE_TOBE_CREATED){
				WriteInToJSON.createJSON(fileName+".json", collection);
			}
		}
		catch(Exception e){
			logger.fatal(e.getMessage());
			logger.warn(e+","+e.getCause());
			
		}
		
	}
}
