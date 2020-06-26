package com.telefolio.initializer;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.telefolio.extracters.ExtractDeviceDetailed;
import com.telefolio.extracters.ExtractDevices;
import com.telefolio.extracters.ExtractManufactures;
import com.telefolio.utils.ConfigProperties;

public class TeleFolioInitializer {

	private static Logger logger = Logger.getLogger(ExtractManufactures.class);

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		try {
			TeleFolioInitializer initializer = new TeleFolioInitializer();
			DOMConfigurator.configure(initializer.getClass().getClassLoader().getResource("Logger.xml"));
			logger.info("Start Time " + new Date(startTime));
			logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			logger.info("~~~~~~~~~~~~~~~~~~~~~~STARTED~~~~~~~~~~~~~~~~~~~~~~~~~~");
			logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ExtractManufactures manufactures = null;
			ExtractDevices devices = null;

			if (ConfigProperties.IS_MANUFACTURE_TOBE_PARSED || ConfigProperties.IS_MANUFACTURE_FILE_TOBE_REFRESHED) {
				manufactures = new ExtractManufactures();
				manufactures.extractManufactures();
			}

			if (ConfigProperties.IS_DEVICE_FILES_TOBE_REFRESHED || ConfigProperties.IS_DEVICE_TOBE_PARSED) {
				devices = new ExtractDevices();
				devices.extractDevices(manufactures);
			}

			if (ConfigProperties.IS_DEVICE_FILES_TOBE_REFRESHED || ConfigProperties.IS_DEVICE_DETAILED_TOBE_PARSED) {
				new ExtractDeviceDetailed().extractDevices(devices);
			}
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			logger.trace(e);
		} finally {
			logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			logger.info("~~~~~~~~~~~~~~~~~~~~~~ENDED~~~~~~~~~~~~~~~~~~~~~~~~~~");
			logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			long endTime = System.currentTimeMillis();
			logger.info("End Time : " + new Date(endTime));
			logger.info("Total executed Time " + ((endTime - startTime)) + " milli seconds");
		}
	}

}
