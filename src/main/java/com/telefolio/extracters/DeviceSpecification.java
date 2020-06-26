package com.telefolio.extracters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.telefolio.databean.DeviceDetailed;
import com.telefolio.utils.DeviceMappingProperties;

public class DeviceSpecification{

	private Map<String, String> specs = null;
	private static Logger logger = Logger.getLogger(DeviceSpecification.class);
	private DeviceDetailed deviceDetailed = null;
	
	public DeviceDetailed getDeviceDetailed() {
		return deviceDetailed;
	}

	public Map<String,String> parseValues(String deviceName, Map<String, String> specs) {
		this.specs = specs;
		deviceDetailed = new DeviceDetailed();
		deviceDetailed.manufactureName = deviceName.split(" ")[0];
		deviceDetailed.deviceName = deviceName.substring(deviceName.indexOf(deviceDetailed.manufactureName)+
				deviceDetailed.manufactureName.length()+1);
		
		setConnectivity(); // Neworks, Comms, Sound
		setBody(); // Body, Display
		setHardwares(); // Battery,Memory
		setCamera(); // MainCamera,SelfieCamera
		setOthers(); // Launch, Misc, Platform, Features
		setVersions(); // Versions
		return this.specs;
	}

	private Set<String> initilizeSetValues(Set<String> value) {
		return value == null ? new HashSet<String>() : value;
	}

	private void setHardwares() {

		// Set Battery Values
		setBattery();
		// Set Memory
		setMemory();
	}

	private void setCamera() {
		// Set Main Camera
		setMainCamera();
		// Set Selfie Camera
		setSelfieCamera();
	}

	private void setOthers() {
		// Set Launch Details
		setLaunch();
		// Set Misc
		setMisc();
		// Set Platform
		setPlatform();
		// Set Features
		setFeatures();
	}

	private void setBody() {

		// Set Body Dimensions
		setBodyDimensions();

		// Set Display
		setDisplay();
	}

	private void setConnectivity() {
		// Set Network
		setNetwork();

		// Set Commons
		setCommons();

		// Set Sound
		setSound();
	}

	private void setNetwork() {

		try {
			deviceDetailed.has2G = get(DeviceMappingProperties.PATH_2GBANDS) == null ? false : true;
			deviceDetailed.info2Gbands = getOrDefault(DeviceMappingProperties.PATH_2GBANDS, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.has3G = get(DeviceMappingProperties.PATH_3GBANDS) == null ? false : true;
			deviceDetailed.info3Gbands = getOrDefault(DeviceMappingProperties.PATH_3GBANDS, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.has4G = get(DeviceMappingProperties.PATH_4GBANDS) == null ? false : true;
			deviceDetailed.info4Gbands = getOrDefault(DeviceMappingProperties.PATH_4GBANDS, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.has5G = get(DeviceMappingProperties.PATH_5GBANDS) == null ? false : true;
			deviceDetailed.info5Gbands = getOrDefault(DeviceMappingProperties.PATH_5GBANDS, DeviceMappingProperties.EMPTY_STRING);

			deviceDetailed.infoNetworkSpeed = getOrDefault(DeviceMappingProperties.PATH_NETWORKSPEED, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.infoNetworkTechnology = getOrDefault(DeviceMappingProperties.PATH_INFONETWORK, DeviceMappingProperties.EMPTY_STRING);
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Network Bands");
		}
	}

	private void setBattery() {
		try {
			deviceDetailed.batteryType = getOrDefault(DeviceMappingProperties.PATH_BATTERY, DeviceMappingProperties.EMPTY_STRING);
			if (!deviceDetailed.batteryType.isEmpty()) {
				Pattern pattern = Pattern.compile("([0-9]+) mAh");
				Matcher matcher = pattern.matcher(deviceDetailed.batteryType);
				if (matcher.find()) {
					deviceDetailed.batterySize = Integer.parseInt(matcher.group(1));
				} 
			}

			deviceDetailed.infoBattery = getOrDefault(DeviceMappingProperties.PATH_INFOBATTERY, DeviceMappingProperties.EMPTY_STRING);
			if(deviceDetailed.infoBattery.toLowerCase().contains("fast charging")){
				deviceDetailed.hasFastCharging = true;
				Pattern pattern = Pattern.compile("([0-9]+)W");
				Matcher matcher = pattern.matcher(deviceDetailed.infoBattery.toUpperCase());
				if (matcher.find()) {
					deviceDetailed.fastChargingWatt = Integer.parseInt(matcher.group(1));
				}
			}
			if(deviceDetailed.infoBattery.toLowerCase().contains("reverse charging")){
				deviceDetailed.hasReverseCharging = true;
			}
			
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Battery");
		}
	}

	private void setMemory() {

		try {
			deviceDetailed.infoMMCCardSlot = getOrDefault(DeviceMappingProperties.PATH_MEMORYMMCCARD, DeviceMappingProperties.EMPTY_STRING);
			String memoryInteral = getOrDefault(DeviceMappingProperties.PATH_MEMORYINTERNAL, DeviceMappingProperties.EMPTY_STRING);
			String internalValues = memoryInteral.split("::")[0];
			deviceDetailed.internalMemorySize = initilizeSetValues(deviceDetailed.internalMemorySize);
			for (String value : internalValues.split(",")) {
				if (value.contains("RAM")) {
					value = value.trim();
					deviceDetailed.internalMemorySize.add(value.split(" ")[0].trim());
					deviceDetailed.ramSize.add(value.split(" ")[1].trim());
				}
			}

			deviceDetailed.infoMemoryFeatures = memoryInteral;
			deviceDetailed.infoMemoryFeatures = deviceDetailed.infoMemoryFeatures.contains("::")
					? deviceDetailed.infoMemoryFeatures.substring(deviceDetailed.infoMemoryFeatures.indexOf("::") + 2) : 
						DeviceMappingProperties.EMPTY_STRING;

		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Memory");
		}
	}

	private void setBodyDimensions() {
		try {
			deviceDetailed.bodyBuild = getOrDefault(DeviceMappingProperties.PATH_BODYBUILD, DeviceMappingProperties.EMPTY_STRING);
			try{
				deviceDetailed.bodyWeight = Float.parseFloat(getOrDefault(DeviceMappingProperties.PATH_BODYWEIGHT, "0").split(" ")[0]);
			} catch(Exception e){
				deviceDetailed.bodyWeight = 0;
			}
			String bodySim = getOrDefault(DeviceMappingProperties.PATH_BODYSIM, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.otherConnectivityFeatures = bodySim;
			deviceDetailed.otherConnectivityFeatures = deviceDetailed.otherConnectivityFeatures.contains("::")
					? deviceDetailed.otherConnectivityFeatures.substring(deviceDetailed.otherConnectivityFeatures.indexOf("::") + 2)
					: DeviceMappingProperties.EMPTY_STRING;
			deviceDetailed.simType = bodySim.split("::")[0];

			String dimension = getOrDefault(DeviceMappingProperties.PATH_BODYDIMENSION, DeviceMappingProperties.EMPTY_STRING);
			dimension = dimension.contains("Folded:") ? dimension.substring(dimension.indexOf("Folded:")+8) : dimension; 
			if (!dimension.isEmpty() && dimension.contains("mm")) {
				String[] dimensions = dimension.substring(0, dimension.indexOf("mm")).trim().split("x");
				deviceDetailed.bodyLength = Float.parseFloat(dimensions[0].trim());
				deviceDetailed.bodyWidth = Float.parseFloat(dimensions[1].trim());
				deviceDetailed.bodyBreadth = Float.parseFloat(dimensions[2].trim());
			}
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Body Dimensions");
		}
	}

	private void setDisplay() {
		try {

			deviceDetailed.displayProtection = getOrDefault(DeviceMappingProperties.PATH_DISPLAYPROTECTION, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.displayType = getOrDefault(DeviceMappingProperties.PATH_DISPLAYTYPE, DeviceMappingProperties.EMPTY_STRING);

			String display = getOrDefault(DeviceMappingProperties.PATH_DISPLAYSIZE, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.displaySize = display.split(",")[0];
			deviceDetailed.displayScreenToBodyRatio = DeviceMappingProperties.EMPTY_STRING;
			if (display.contains("~") && display.contains("%")) {
				display = display.substring(display.indexOf("~") + 1);
				deviceDetailed.displayScreenToBodyRatio = display.substring(0, display.indexOf("%"));
			}

			String[] displayValue = getOrDefault(DeviceMappingProperties.PATH_DISPLAYRESOLUTION, DeviceMappingProperties.EMPTY_STRING).split(",");
			deviceDetailed.displayResolution = displayValue.length >= 1 ? displayValue[0] : DeviceMappingProperties.EMPTY_STRING;
			if (displayValue.length >= 2) {
				deviceDetailed.displayRatio = displayValue[1].contains("\\(") ? displayValue[1].split("\\(")[0].trim()
						: displayValue[1];
				deviceDetailed.displayDensity = displayValue[1].contains("\\(") ? "(" + displayValue[1].split("\\(")[1].trim()
						: displayValue[1];
			} else {
				deviceDetailed.displayRatio = DeviceMappingProperties.EMPTY_STRING;
				deviceDetailed.displayDensity = DeviceMappingProperties.EMPTY_STRING;
			}
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Display Values");
		}
	}

	private void setCommons() {
		try {
			deviceDetailed.infoBluetooth = getOrDefault(DeviceMappingProperties.PATH_INFOBLUETOOTH, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.infoGPS = getOrDefault(DeviceMappingProperties.PATH_INFOGPS, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.infoNFC = getOrDefault(DeviceMappingProperties.PATH_INFONFC, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.infoRadio = getOrDefault(DeviceMappingProperties.PATH_INFORADIO, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.infoUSB = getOrDefault(DeviceMappingProperties.PATH_INFOUSB, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.infoWifi = getOrDefault(DeviceMappingProperties.PATH_INFOWIFI, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.infoInfraredport = getOrDefault(DeviceMappingProperties.PATH_INFOINFRAREDPORT, DeviceMappingProperties.EMPTY_STRING);
			
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Commons");
		}

	}

	private void setSound() {
		try {
			deviceDetailed.infoHeadphoneJack = getOrDefault(DeviceMappingProperties.PATH_INFOHEADPHONEJACK, DeviceMappingProperties.EMPTY_STRING);
			if (!deviceDetailed.infoHeadphoneJack.isEmpty() && deviceDetailed.infoHeadphoneJack.toLowerCase().contains("yes")) {
				deviceDetailed.hasHeadphoneJack = true;
			}
			deviceDetailed.infoLoudspeaker = getOrDefault(DeviceMappingProperties.PATH_INFOLOUDSPEAKER, DeviceMappingProperties.EMPTY_STRING);
			if (!deviceDetailed.infoLoudspeaker.isEmpty() && deviceDetailed.infoLoudspeaker.toLowerCase().contains("yes")) {
				deviceDetailed.hasLoudspeaker = true;
			}
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Sounds");
		}
	}

	private void setMainCamera() {
		try {

			if (get(DeviceMappingProperties.PATH_MAINCAMERA1) != null) {
				deviceDetailed.mainCameraType = DeviceMappingProperties.VALUE_CAMERA_1;
				deviceDetailed.mainCameraFeatures = getOrDefault(DeviceMappingProperties.PATH_MAINCAMERA1, DeviceMappingProperties.EMPTY_STRING);
			} else if (get(DeviceMappingProperties.PATH_MAINCAMERA2) != null) {
				deviceDetailed.mainCameraType = DeviceMappingProperties.VALUE_CAMERA_2;
				deviceDetailed.mainCameraFeatures = getOrDefault(DeviceMappingProperties.PATH_MAINCAMERA2, DeviceMappingProperties.EMPTY_STRING);
			} else if (get(DeviceMappingProperties.PATH_MAINCAMERA3) != null) {
				deviceDetailed.mainCameraType = DeviceMappingProperties.VALUE_CAMERA_3;
				deviceDetailed.mainCameraFeatures = getOrDefault(DeviceMappingProperties.PATH_MAINCAMERA3, DeviceMappingProperties.EMPTY_STRING);
			} else if (get(DeviceMappingProperties.PATH_MAINCAMERA4) != null) {
				deviceDetailed.mainCameraType = DeviceMappingProperties.VALUE_CAMERA_4;
				deviceDetailed.mainCameraFeatures = getOrDefault(DeviceMappingProperties.PATH_MAINCAMERA4, DeviceMappingProperties.EMPTY_STRING);
			} else if (get(DeviceMappingProperties.PATH_MAINCAMERA5) != null) {
				deviceDetailed.mainCameraType = DeviceMappingProperties.VALUE_CAMERA_5;
				deviceDetailed.mainCameraFeatures = getOrDefault(DeviceMappingProperties.PATH_MAINCAMERA5, DeviceMappingProperties.EMPTY_STRING);
			} else {
				deviceDetailed.mainCameraType = DeviceMappingProperties.EMPTY_STRING;
				deviceDetailed.mainCameraFeatures = DeviceMappingProperties.EMPTY_STRING;
			}

			deviceDetailed.mainCameraVideo = getOrDefault(DeviceMappingProperties.PATH_MAINCAMERAVIDEO, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.mainCameraFeatures += getOrDefault(DeviceMappingProperties.PATH_MAINCAMERAFEATURE, DeviceMappingProperties.EMPTY_STRING);

		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Main Camera");
		}
	}

	private void setSelfieCamera() {
		try {
			if (get(DeviceMappingProperties.PATH_SELFIECAMERA1) != null) {
				deviceDetailed.selfieCameraType = DeviceMappingProperties.VALUE_CAMERA_1;
				deviceDetailed.selfieCameraFeatures = getOrDefault(DeviceMappingProperties.PATH_SELFIECAMERA1, DeviceMappingProperties.EMPTY_STRING);
			} else if (get(DeviceMappingProperties.PATH_SELFIECAMERA2) != null) {
				deviceDetailed.selfieCameraType = DeviceMappingProperties.VALUE_CAMERA_2;
				deviceDetailed.selfieCameraFeatures = getOrDefault(DeviceMappingProperties.PATH_SELFIECAMERA2, DeviceMappingProperties.EMPTY_STRING);
			} else if (get(DeviceMappingProperties.PATH_SELFIECAMERA3) != null) {
				deviceDetailed.selfieCameraType = DeviceMappingProperties.VALUE_CAMERA_3;
				deviceDetailed.selfieCameraFeatures = getOrDefault(DeviceMappingProperties.PATH_SELFIECAMERA3, DeviceMappingProperties.EMPTY_STRING);
			} else {
				deviceDetailed.selfieCameraType = DeviceMappingProperties.EMPTY_STRING;
				deviceDetailed.selfieCameraFeatures = DeviceMappingProperties.EMPTY_STRING;
			}

			deviceDetailed.selfieCameraVideo = getOrDefault(DeviceMappingProperties.PATH_SELFIECAMERAVIDEO, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.selfieCameraFeatures += getOrDefault(DeviceMappingProperties.PATH_SELFIECAMERAFEATURES, DeviceMappingProperties.EMPTY_STRING);
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Selfie Camera");
		}
	}

	private void setLaunch() {
		try {

			deviceDetailed.annoucedDate = getOrDefault(DeviceMappingProperties.PATH_LAUNCHDATE, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.sellingStatus = getOrDefault(DeviceMappingProperties.PATH_SELLINGSTATUS, DeviceMappingProperties.EMPTY_STRING);

			deviceDetailed.launchDate = setDate(deviceDetailed.annoucedDate);

		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Launch Details");
		}
	}

	private Date setDate(String annoucedDate) {
		for(String format: DeviceMappingProperties.DATE_FORMATS){
			SimpleDateFormat dateFormat = new SimpleDateFormat(format); 
			try{
				return dateFormat.parse(annoucedDate);
			}
			catch(ParseException e){
			}
		}
		return null;
	}

	private void setMisc() {
		try {
			deviceDetailed.colors = Arrays.asList(getOrDefault(DeviceMappingProperties.PATH_COLORS, DeviceMappingProperties.EMPTY_STRING).split(",")).stream()
					.collect(Collectors.toSet());
			deviceDetailed.models = Arrays.asList(getOrDefault(DeviceMappingProperties.PATH_MODELS, DeviceMappingProperties.EMPTY_STRING).split(",")).stream()
					.collect(Collectors.toSet());
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Misc");
		}
	}

	private void setPlatform() {
		try {

			deviceDetailed.infoCPU = getOrDefault(DeviceMappingProperties.PATH_INFOCPU, DeviceMappingProperties.EMPTY_STRING);
			deviceDetailed.infoOS = getOrDefault(DeviceMappingProperties.PATH_INFOOS, DeviceMappingProperties.EMPTY_STRING).split(",")[0];

			deviceDetailed.osType = deviceDetailed.infoOS.contains("Android") ? "Android" : (deviceDetailed.infoOS.contains("iOS") ? "iOS" : "");
			
			if(!deviceDetailed.osType.isEmpty()){
				String os = deviceDetailed.infoOS.substring(deviceDetailed.osType.indexOf(deviceDetailed.osType)+deviceDetailed.osType.length());
				if(deviceDetailed.infoOS.contains(",")){
					os = os.split(",")[0]; 
				}
				Pattern pattern = Pattern.compile("([0-9]+(\\.[0-9])*)");
				Matcher matcher = pattern.matcher(os);
				if (matcher.find()) {
					deviceDetailed.osVersion = matcher.group(1);
				}
			}
			
			deviceDetailed.infoChipsets = initilizeSetValues(deviceDetailed.infoChipsets);
			String[] infoChipset = getOrDefault(DeviceMappingProperties.PATH_INFOCHIPSET, DeviceMappingProperties.EMPTY_STRING).split(" - ");
			int i = 0;
			deviceDetailed.infoChipsets.add(infoChipset[i].trim());
			for (i = 1; infoChipset.length > i; i = i + 2) {
				String value = infoChipset[i].trim();
				deviceDetailed.infoChipsets.add(value.substring(value.indexOf(" ")).trim());
			}

			deviceDetailed.infoGPU = initilizeSetValues(deviceDetailed.infoGPU);
			String[] infoGPUs = getOrDefault(DeviceMappingProperties.PATH_INFOGPU, DeviceMappingProperties.EMPTY_STRING).split(" - ");
			i = 0;
			deviceDetailed.infoGPU.add(infoGPUs[i].trim());
			for (i = 1; infoGPUs.length > i; i = i + 2) {
				String value = infoGPUs[i].trim();
				deviceDetailed.infoGPU.add(value.substring(value.indexOf(" ")).trim());
			}

		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Platforms");
		}
	}

	private void setFeatures() {
		try {
			deviceDetailed.otherFeatures = getOrDefault(DeviceMappingProperties.PATH_FEATURESENSOR, DeviceMappingProperties.EMPTY_STRING);
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Features");
		}
	}

	private void setVersions() {
		try {
			deviceDetailed.versions = Arrays.asList(getOrDefault(DeviceMappingProperties.PATH_VERSIONS, DeviceMappingProperties.EMPTY_STRING).split(";"))
					.stream().collect(Collectors.toSet());
		} catch (Exception e) {
			logger.trace(e.toString());
			logger.error(deviceDetailed.deviceName+" :: Exception Occured while Parsing Features");
		}
	}

	private String getOrDefault(String arg1, String arg2) {
		Object object = get(arg1);
		if (object == null)
			return arg2;
		else {
			specs.remove(arg1);
			return object.toString();
		}
	}
	
	private Object get(String arg1) {
		return specs.get(arg1);
	}


}
