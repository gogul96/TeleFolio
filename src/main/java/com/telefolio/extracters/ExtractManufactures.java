package com.telefolio.extracters;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.telefolio.databean.Manufacture;
import com.telefolio.utils.ConfigProperties;
import com.telefolio.utils.TeleFolioFile;

public class ExtractManufactures {
	
	private static Logger logger = Logger.getLogger(ExtractManufactures.class);
	public List<Manufacture> manufactureList = null;
	public static void main(String[] args){
		long startTime = System.currentTimeMillis();
		try {
			ExtractManufactures extractManufactures = new ExtractManufactures();
			DOMConfigurator.configure(extractManufactures.getClass().getClassLoader().getResource("Logger.xml"));
			logger.info("Start Time " + new Date(startTime));
			extractManufactures.extractManufactures();
		}catch (Exception e) {
			logger.fatal(e.getMessage());
			logger.trace(e);
		}finally{
			long endTime = System.currentTimeMillis();
			logger.info("End Time : " + new Date(endTime));
			logger.info("Total executed Time " + ((endTime-startTime)/1000) + " seconds");
		}
	}

	public void extractManufactures() throws Exception {
		try{
			if(ConfigProperties.IS_MANUFACTURE_FILE_TOBE_REFRESHED){
				createManufactureDocument();
			}
			if(ConfigProperties.IS_MANUFACTURE_TOBE_PARSED){
				parseManufactureDocument();
			}
			TeleFolioFile.writeInToFile("Manufacture",manufactureList,Manufacture.getHeader());
		}
		catch(Exception e){
			throw e;
		}
	}
	
	private void createManufactureDocument() throws Exception {
		// TODO Auto-generated method stub
		Document makersListDoc = null;
		String requestURL = ConfigProperties.BASE_URL + "makers.php3";
		makersListDoc = Jsoup.connect(requestURL).get();
		TeleFolioFile.writeFile(null, ConfigProperties.BASE_RESULT_PATH + "Manufacture.html", makersListDoc.html());
	}

	public void parseManufactureDocument() throws Exception {
		File baseHTML = new File(ConfigProperties.BASE_RESULT_PATH + "Manufacture.html");
		if(!baseHTML.exists()){
			createManufactureDocument();
			baseHTML = new File(ConfigProperties.BASE_RESULT_PATH + "Manufacture.html");
		}
		else{
			logger.debug("Manufacture HTML File is available, proceeds to parsing");
		}
		Document makersListDoc = Jsoup.parse(baseHTML, "UTF-8");
		Element makersElement = makersListDoc.getElementsByClass("st-text").first();
		manufactureList = new ManufactureParser().extractElement(makersElement);
	}
	
}
