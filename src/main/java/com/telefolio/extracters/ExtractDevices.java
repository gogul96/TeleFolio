package com.telefolio.extracters;

import java.io.File;
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
import com.telefolio.databean.Manufacture;
import com.telefolio.utils.ConfigProperties;
import com.telefolio.utils.TeleFolioFile;

public class ExtractDevices {
	private static Logger logger = Logger.getLogger(ExtractDevices.class);
	public List<Device> deviceList = null;

	public ExtractDevices() {
		deviceList = new ArrayList<Device>();
	}
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		try {
			ExtractDevices extractDevices = new ExtractDevices();
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

	public void extractDevices(ExtractManufactures extractManufactures) throws Exception {
		try {
			if (ConfigProperties.IS_DEVICE_FILES_TOBE_REFRESHED) {
				createDevicesDocument(extractManufactures);
			}
			if (ConfigProperties.IS_DEVICE_TOBE_PARSED) {
				parseDevicesDocument();
			}
			TeleFolioFile.writeInToFile("Devices", deviceList, Device.getHeader());
		} catch (Exception e) {
			throw e;
		}
	}

	private void createDevicesDocument(ExtractManufactures extractManufactures) throws Exception {
		try {
			if(extractManufactures==null){
				extractManufactures = new ExtractManufactures();
				extractManufactures.parseManufactureDocument();
			}
			for (Manufacture manufacture : extractManufactures.manufactureList) {
				Document makersDoc = null;
				String makerFileName = ConfigProperties.BASE_RESULT_PATH + "Manufacture/" + manufacture.getName();
				String requstMakerURL = ConfigProperties.BASE_URL + manufacture.getAbsoluteURL();
				makersDoc = Jsoup.connect(requstMakerURL).get();
				TeleFolioFile.writeFile(null, makerFileName + ".html", makersDoc.html());
				Element subPageElement = makersDoc.getElementsByClass("nav-pages").first();
				if (subPageElement != null) {
					ArrayList<String> makersSubPageURL = getSubPageURL(subPageElement);
					int i = 1;
					for (String subURL : makersSubPageURL) {
						Document makersSubDoc = Jsoup.connect(ConfigProperties.BASE_URL + subURL).get();
						String subMakerFileName = makerFileName + "_" + (++i);
						TeleFolioFile.writeFile(null, subMakerFileName + ".html", makersSubDoc.html());
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void parseDevicesDocument() throws Exception {
		try{
			File makersDirectoryPath = new File(ConfigProperties.BASE_RESULT_PATH + "Manufacture/");
			DeviceParser deviceParser = new DeviceParser();
			for (String subFilePath : makersDirectoryPath.list()) {
				Document deviceDoc = Jsoup.parse(new File(makersDirectoryPath.getAbsolutePath() + "\\" + subFilePath),
						"UTF-8");
				Element makerDeviceElement = deviceDoc.getElementsByClass("makers").first();
				String manufactureName = deviceParser.extractManufactureName(deviceDoc.title());
				if(manufactureName==null || deviceParser.isManufactureParseCompleted.getOrDefault(manufactureName, false)){
					continue;
				}
				List<Device> devices = deviceParser.extractElement(makerDeviceElement);
				if(devices!=null){
					deviceList.addAll(devices);
				}
				else{
					logger.info(String.format("Device {%s} is not added",manufactureName));
				}
			}
			logger.info("Total Device Parsed : " + deviceList.size());
		}
		catch(Exception e){
			logger.fatal(e.getMessage());
			logger.fatal(e.getCause());
			throw e;
		}
	}
	
	private static ArrayList<String> getSubPageURL(Element subPageElement) {
		ArrayList<String> subPageURLs = new ArrayList<>();
		Elements subLink = subPageElement.getElementsByTag("a");
		for(Element element : subLink){
			subPageURLs.add(element.attr("href"));
		}
		return subPageURLs;
	}

}
