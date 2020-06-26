package com.telefolio.extracters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.telefolio.databean.Device;
import com.telefolio.databean.DeviceDetailed;
import com.telefolio.utils.ConfigProperties;
import com.telefolio.utils.TeleFolioFile;

public class ExtractDeviceDetailed {
	private static Logger logger = Logger.getLogger(ExtractDeviceDetailed.class);
	public List<DeviceDetailed> deviceList = null;
	public List<String> failToParseDevices = null;
	
	public ExtractDeviceDetailed() {
		deviceList = new ArrayList<DeviceDetailed>();
		failToParseDevices = new ArrayList<String>();
	}
	
	public static void main(String[] args){
		long startTime = System.currentTimeMillis();
		try {
			ExtractDeviceDetailed extractDevices = new ExtractDeviceDetailed();
			DOMConfigurator.configure(extractDevices.getClass().getClassLoader().getResource("Logger.xml"));
			logger.info("Start Time " + new Date(startTime));
			extractDevices.extractDevices(null);
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			logger.trace(e);
		} finally {
			long endTime = System.currentTimeMillis();
			logger.info("End Time : " + new Date(endTime));
			logger.info("Total executed Time " + ((endTime - startTime)) + " milliseconds");
		}
	}

	public void extractDevices(ExtractDevices devices) throws Exception {
		try {
			if (ConfigProperties.IS_DEVICE_FILES_TOBE_REFRESHED) {
				createDeviceDetailDocument(devices);
			}
			if (ConfigProperties.IS_DEVICE_DETAILED_TOBE_PARSED) {
				parseDeviceDetailDocument();
			}
			TeleFolioFile.writeInToFile("DevicesDetails", deviceList, DeviceDetailed.getHeader());
		} catch (Exception e) {
			throw e;
		}
	}

	private void createDeviceDetailDocument(ExtractDevices devices) throws Exception{
		try{
			if(devices==null){
				devices = new ExtractDevices();
				devices.parseDevicesDocument();
			}
			for(Device device: devices.deviceList){
				String deviceFileName = "Devices\\" + device.getManufactureName() + "\\" + device.getName();
				File newFile = new File(ConfigProperties.BASE_RESULT_PATH+deviceFileName+".html"); 
				if(newFile.exists()){
					continue;
				}
				String requestURL = ConfigProperties.BASE_URL + device.getUrl();
				Document deviceDoc = Jsoup.connect(requestURL).get();
				TeleFolioFile.writeFile(newFile, null, deviceDoc.html());
			}
		}
		catch(Exception e){
			logger.trace(e);
			throw e;
		}
	}

	private void parseDeviceDetailDocument() {
		try{
			DeviceDetailParser deviceDetailParser = new DeviceDetailParser();
			int devices=0,parsedDevices = 0, notValidDevices = 0, failedDevices = 0;
			for (String maker : ConfigProperties.MAKERS_TOBE_EXTRACTED) {
				File makersDirectoryPath = null;
				try {
					makersDirectoryPath = new File(ConfigProperties.BASE_RESULT_PATH + "Devices\\" + maker);
					if (!makersDirectoryPath.exists()) {
						throw new FileNotFoundException();
					}
				} catch (Exception e) {
					logger.fatal(maker + " :: maker Folder is missing ");
					logger.trace(e);
					continue;
				}
				for (String subFilePath : makersDirectoryPath.list()) {
					devices++;
					try {
						Document deviceDoc = Jsoup.parse(
								new File(makersDirectoryPath.getPath() + "\\" + subFilePath), "UTF-8");
						String deviceName = deviceDoc.title().split(" - ")[0];
						logger.debug("Parsing Device Spec of " + deviceName);
						deviceDetailParser.deviceName = deviceName;
						if (!checkValidPhone(deviceDoc.getElementsByClass("specs-brief-accent"))) {
							logger.info(deviceName + " is not a Smartphone ");
							notValidDevices++;
							continue;
						} else {
							parsedDevices++;
						}
						Element specList = deviceDoc.getElementById("specs-list");
						DeviceDetailed deviceDetailed = deviceDetailParser.extractElement(specList);
						if(deviceDetailed!=null){
							deviceList.add(deviceDetailed);
						} else{
							logger.error(String.format("Device {%s} is not added ",deviceName));
						}
					} catch (Exception e) {
						failToParseDevices.add(subFilePath);
						logger.fatal(maker + " " + subFilePath + " :: device File is missing to parse");
						logger.trace(e);
						failedDevices++;
						continue;
					}
				}
			}
			logger.info("Total Device to be parsed : " + devices);
			logger.info("Total " + (deviceList.size()) + " no.of devices successfully parsed");
			logger.info("Total " + notValidDevices + " no.of devices not a smartphone");
			logger.info("Total " + (failedDevices + (parsedDevices - deviceList.size()))
					+ " no.of devices failed in parsing ");
			if(failToParseDevices.size()>0){
				logger.error(failToParseDevices.toString());
			}
		}
		catch(Exception e){
			logger.fatal(e.getMessage());
		}
	}
	
	public boolean checkValidPhone(Elements elements) {
		try {
			Element checkElement = elements.get(2);
			String text = checkElement.text().toLowerCase();
			if (text.contains("feature phone") || text.contains("watchos") || text.contains("wearable")) {
				return false;
			}
		} catch (Exception e) {
			logger.warn("Unable to validate the phone type");
		}
		return true;
	}
}
