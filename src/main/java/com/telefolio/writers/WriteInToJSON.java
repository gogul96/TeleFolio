package com.telefolio.writers;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.telefolio.databean.Device;
import com.telefolio.databean.DeviceDetailed;
import com.telefolio.databean.Manufacture;
import com.telefolio.utils.ConfigProperties;
import com.telefolio.utils.TeleFolioFile;

public class WriteInToJSON {

	private static Logger logger = Logger.getLogger(WriteInToJSON.class);
	
	public static void createJSON(String fileName, Collection<?> values) throws IOException{
		String data = "";
		for(Object entry:values){
			if(entry instanceof Manufacture){
				data += checkJSON(((Manufacture) entry).toJSONString());
			} else if(entry instanceof Device){
				data += checkJSON(((Device) entry).toJSONString());
			} else if(entry instanceof DeviceDetailed){
				data += checkJSON(((DeviceDetailed) entry).toJSONString());
			} else {
				logger.error("Writing data for value instance of " + entry.getClass());
			}
			data +=  "\n";
		}
		TeleFolioFile.writeFile(null,ConfigProperties.BASE_RESULT_PATH+"/datasets/JSON/"+fileName, data);
	}

	private static String checkJSON(String data) {
		try{
			String value =  new JSONObject(data).toString();
			logger.debug(value);
			return value;
		}
		catch(Exception e){
			logger.error("Data is not valid JSON");
			logger.debug(data);
		}
		return "";
	}
}
