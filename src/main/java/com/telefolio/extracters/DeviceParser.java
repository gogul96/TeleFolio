package com.telefolio.extracters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.telefolio.databean.Device;
import com.telefolio.utils.ConfigProperties;
import com.telefolio.utils.TeleFolioFile;

public class DeviceParser {
	private static Logger logger = Logger.getLogger(DeviceParser.class);
	private String manufactureName;
	private Properties lastDeviceToFetchprop = null;
	private List<Device> devices = null;
	public Map<String,Boolean> isManufactureParseCompleted = null; 
	
	public DeviceParser() {
		lastDeviceToFetchprop = TeleFolioFile.getProperties(ConfigProperties.LAST_DEVICE_TO_FETCH);
		isManufactureParseCompleted = new HashMap<String,Boolean>();
	}

	public List<Device> extractElement(Element makerElement) {
		try{
			devices = new ArrayList<Device>();
			createFolderForMakers();
			Elements deviceList = makerElement.children().first().children();
			for(Element device:deviceList){
				if(!addDeviceFromList(device.children().first())){
					isManufactureParseCompleted.put(manufactureName, true);
					break;
				}
			}
			isManufactureParseCompleted.putIfAbsent(manufactureName, false);
			return devices;
		}
		catch(Exception e){
			logger.fatal("Failed to parsing " + manufactureName);
			logger.error(e);
		}
		return null;
	}
	
	private boolean addDeviceFromList(Element deviceElement) {
		Device device = new Device();
		device.setUrl(ConfigProperties.BASE_URL + deviceElement.attr("href"));
		device.setManufactureName(manufactureName);
		device.setName(deviceElement.text());
		device.setImagesrcURL(deviceElement.children().first().attr("src"));
		logger.info("Parsing Device " + device.getManufactureName()+":"+device.getName());
		devices.add(device);
		if(device.getName().equals(lastDeviceToFetchprop.getOrDefault(manufactureName,ConfigProperties.EMPTY_STRING))){ 
			return false;
		}
		return true;
	}

	private void createFolderForMakers() {
		File makerFolder = new File(ConfigProperties.BASE_RESULT_PATH+"Devices\\"+manufactureName);
		if(!makerFolder.exists()){
			makerFolder.mkdirs();
		}
	}

	public String extractManufactureName(String title) {
		try{
			if(title.contains("- p")){
				title = title.substring(0,title.lastIndexOf("- p")-1);
			}
			String[] titles = title.split(" ");
			if(titles.length>3){
				manufactureName = titles[1] + " " + titles[2]; 
			}
			else{
				manufactureName = titles[1];
			}	
			return manufactureName;
		}
		catch(Exception e){
			logger.fatal(String.format("Unable to parse Manufacture Name {%s}",manufactureName));
		}
		return null;
	}
}
