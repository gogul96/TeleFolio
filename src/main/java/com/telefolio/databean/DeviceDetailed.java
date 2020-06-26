package com.telefolio.databean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.telefolio.utils.ConfigProperties;

public class DeviceDetailed {
	public String deviceName = "";
	public String manufactureName = "";
	// Battery, setBattery
	public int batterySize = 0;
	public String batteryType = "";

	// Battery/Charging, setBatteryFeatures
	public boolean hasFastCharging = false;
	public int fastChargingWatt = 0; 
	public boolean hasReverseCharging = false;
	public String infoBattery = "";
	
	// Body/Build
	public String bodyBuild = "";

	// Body/Dimensions, setDimensions
	public float bodyLength = 0;
	public float bodyWidth = 0;
	public float bodyBreadth = 0;

	// Body/SIM, setConnectivity
	public String simType = "";
	public String otherConnectivityFeatures = "";

	// Body/Weight
	public float bodyWeight = 0;

	// Comms/Bluetooth
	public String infoBluetooth = "";

	// Comms/GPS
	public String infoGPS = "";

	// Comms/NFC
	public String infoNFC = "";

	// Comms/Radio
	public String infoRadio = "";

	// comms/USB
	public String infoUSB = "";

	// comms/WLAN
	public String infoWifi = "";

	// comms/Infraredport
	public String infoInfraredport = "";

	// Display/Protection
	public String displayProtection = "";

	// Display/Resolution, setDisplayResolution
	public String displayResolution = "";
	public String displayRatio = "";
	public String displayDensity = "";

	// Display/Size, setDisplaySize
	public String displaySize = "";
	public String displayScreenToBodyRatio = "";

	// Display/Type
	public String displayType = "";

	// Features/Sensors
	public String otherFeatures = "";

	// Launch/Announced, setLaunchDate
	public String annoucedDate = "";
	public Date launchDate;

	// Launch/Status
	public String sellingStatus = "";

	// MainCamera/Triple, MainCamera/Dual, MainCamera/Single, MainCamera/Quad,
	// MainCamera/Five,
	// MainCamera/Features, setMainCamera
	public String mainCameraType = "";
	public String mainCameraFeatures = "";

	// MainCamera/Video
	public String mainCameraVideo = "";

	// Selfiecamera/Single,Selfiecamera/Dual,Selfiecamera/Triple,
	// Selfiecamera/Features, setSelfieCamera
	public String selfieCameraType = "";
	public String selfieCameraFeatures = "";

	// Selfiecamera/Video
	public String selfieCameraVideo = "";

	// Memory/Cardslot
	public String infoMMCCardSlot = "";

	// Memory/Internal
	public Set<String> internalMemorySize = new HashSet<String>();
	public Set<String> ramSize = new HashSet<String>();
	public String infoMemoryFeatures = "";

	// Misc/Colors
	public Set<String> colors = new HashSet<String>();

	// Misc/Models
	public Set<String> models = new HashSet<String>();

	// Network/2Gbands
	public boolean has2G = false;
	public String info2Gbands = "";

	// Network/3Gbands
	public boolean has3G = false;
	public String info3Gbands = "";

	// Network/4Gbands
	public boolean has4G = false;
	public String info4Gbands = "";

	// Network/5Gbands
	public boolean has5G = false;
	public String info5Gbands = "";

	// Network/Speed
	public String infoNetworkSpeed = "";

	// Network/Technology
	public String infoNetworkTechnology = "";

	// Platform/CPU
	public String infoCPU = "";

	// Platform/Chipset,
	public Set<String> infoChipsets = new HashSet<String>();

	// Platform/GPU
	public Set<String> infoGPU = new HashSet<String>();

	// Platform/OS, setOSDetails
	public String infoOS = "";
	public String osType = "";
	public String osVersion = "";

	// Sound/3.5mmjack, setHeadphoneJack
	public boolean hasHeadphoneJack = false;
	public String infoHeadphoneJack = "";

	// Sound/Loudspeaker, setLoudspeaker
	public boolean hasLoudspeaker = false;
	public String infoLoudspeaker = "";

	// Versions
	public Set<String> versions = new HashSet<String>();

	public String getLaunchDate(){
		return launchDate != null ? new SimpleDateFormat("dd-MMM-yyyy").format(launchDate) : ConfigProperties.EMPTY_STRING;
	}
	
	private String getSetAsJSONString(Set<String> set){
		return "[" + set.stream().map(e-> "'"+e+"'").collect(Collectors.joining(",")) + "]";
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(manufactureName).append("|").append(deviceName).append("|").append(batterySize).append("|").append(batteryType).append("|")
				.append(hasFastCharging).append("|").append(fastChargingWatt).append("|").append(hasReverseCharging).append("|")
				.append(infoBattery).append("|").append(bodyBuild).append("|").append(bodyLength).append("|")
				.append(bodyWidth).append("|").append(bodyBreadth).append("|").append(simType).append("|")
				.append(otherConnectivityFeatures).append("|").append(bodyWeight).append("|").append(infoBluetooth)
				.append("|").append(infoGPS).append("|").append(infoNFC).append("|").append(infoRadio).append("|")
				.append(infoUSB).append("|").append(infoWifi).append("|").append(infoInfraredport).append("|")
				.append(displayProtection).append("|").append(displayResolution).append("|").append(displayRatio)
				.append("|").append(displayDensity).append("|").append(displaySize).append("|")
				.append(displayScreenToBodyRatio).append("|").append(displayType).append("|").append(otherFeatures)
				.append("|").append(annoucedDate).append("|").append(getLaunchDate()).append("|").append(sellingStatus)
				.append("|").append(mainCameraType).append("|").append(mainCameraFeatures).append("|")
				.append(mainCameraVideo).append("|").append(selfieCameraType).append("|").append(selfieCameraFeatures)
				.append("|").append(selfieCameraVideo).append("|").append(infoMMCCardSlot).append("|")
				.append(internalMemorySize).append("|").append(ramSize).append("|").append(infoMemoryFeatures)
				.append("|").append(colors).append("|").append(models).append("|").append(has2G).append("|")
				.append(info2Gbands).append("|").append(has3G).append("|").append(info3Gbands).append("|").append(has4G)
				.append("|").append(info4Gbands).append("|").append(has5G).append("|").append(info5Gbands).append("|")
				.append(infoNetworkSpeed).append("|").append(infoNetworkTechnology).append("|").append(infoCPU)
				.append("|").append(infoChipsets).append("|").append(infoGPU).append("|").append(infoOS).append("|")
				.append(osType).append("|").append(osVersion).append("|").append(hasHeadphoneJack).append("|")
				.append(infoHeadphoneJack).append("|").append(hasLoudspeaker).append("|").append(infoLoudspeaker)
				.append("|").append(versions);
		return builder.toString();
	}

	public static String getHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append("manufactureName").append("|deviceName").append("|batterySize").append("|batteryType").append("|hasFastCharging")
				.append("|fastChargingWatt").append("|hasReverseCharging").append("|infoBattery")
				.append("|bodyBuild").append("|bodyLength").append("|bodyWidth").append("|bodyBreadth")
				.append("|simType").append("|otherConnectivityFeatures").append("|bodyWeight").append("|infoBluetooth")
				.append("|infoGPS").append("|infoNFC").append("|infoRadio").append("|infoUSB").append("|infoWifi").append("|infoInfraredport")
				.append("|displayProtection").append("|displayResolution").append("|displayRatio")
				.append("|displayDensity").append("|displaySize").append("|displayScreenToBodyRatio")
				.append("|displayType").append("|otherFeatures").append("|annoucedDate").append("|launchDate")
				.append("|sellingStatus").append("|mainCameraType").append("|mainCameraFeatures")
				.append("|mainCameraVideo").append("|selfieCameraType").append("|selfieCameraFeatures")
				.append("|selfieCameraVideo").append("|infoMMCCardSlot").append("|internalMemorySize")
				.append("|ramSize").append("|infoMemoryFeatures").append("|colors").append("|models").append("|has2G")
				.append("|info2Gbands").append("|has3G").append("|info3Gbands").append("|has4G").append("|info4Gbands")
				.append("|has5G").append("|info5Gbands").append("|infoNetworkSpeed").append("|infoNetworkTechnology")
				.append("|infoCPU").append("|infoChipsets").append("|infoGPU").append("|infoOS").append("|osType")
				.append("|osVersion").append("|hasHeadphoneJack").append("|infoHeadphoneJack").append("|hasLoudspeaker")
				.append("|infoLoudspeaker").append("|versions");
		return builder.toString();
	}
	
	public String toJSONString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ 'ManufactureName':'").append(manufactureName).append("', 'Name':'").append(deviceName)
				.append("', 'BatterySize':'").append(batterySize).append("', 'BatteryType':'").append(batteryType)
				.append("', 'FastCharging':").append(hasFastCharging).append(", 'FastChargingWatt':")
				.append(fastChargingWatt).append(", 'ReverseCharging':").append(hasReverseCharging)
				.append(", 'BodyBuild':'").append(bodyBuild)
				.append("', 'Length':").append(bodyLength).append(", 'Width':").append(bodyWidth)
				.append(", 'Breadth':").append(bodyBreadth).append(", 'bodyWeight':").append(bodyWeight)
				.append(", 'simType':'").append(simType)
				.append("', 'Bluetooth':'").append(infoBluetooth).append("', 'GPS':'")
				.append(infoGPS).append("', 'NFC':'").append(infoNFC).append("', 'Radio':'").append(infoRadio)
				.append("', 'USB':'").append(infoUSB).append("', 'Wifi':'").append(infoWifi)
				.append("', 'InfraredPort':'").append(infoInfraredport).append("', 'DisplayProtection':'")
				.append(displayProtection).append("', 'DisplayResolution':'").append(displayResolution)
				.append("', 'DisplayRatio':'").append(displayRatio).append("', 'DisplayDensity':'").append(displayDensity)
				.append("', 'DisplaySize':'").append(displaySize).append("', 'ScreenToBodyRatio':'")
				.append(displayScreenToBodyRatio).append("', 'DisplayType':'").append(displayType)
				.append("', 'Features':'").append(otherFeatures).append("', 'AnnoucedDate':'").append(annoucedDate)
				.append("', 'LaunchDate':'").append(launchDate).append("', 'SellingStatus':'").append(sellingStatus)
				.append("', 'MainCameraType':'").append(mainCameraType).append("', 'MainCameraFeatures':'")
				.append(mainCameraFeatures).append("', 'MainCameraVideo':'").append(mainCameraVideo)
				.append("', 'SelfieCameraType':'").append(selfieCameraType).append("', 'SelfieCameraFeatures':'")
				.append(selfieCameraFeatures).append("', 'SelfieCameraVideo':'").append(selfieCameraVideo)
				.append("', 'ExternalMemorySlot':'").append(infoMMCCardSlot).append("', 'InternalMemorySize':")
				.append(getSetAsJSONString(internalMemorySize)).append(", 'Ram':").append(getSetAsJSONString(ramSize))
				.append(", 'Colors':").append(getSetAsJSONString(colors)).append(", 'Models':").append(getSetAsJSONString(models))
				.append(", '2G':").append(has2G).append(", '3G':")
				.append(has3G).append(", '4G':").append(has4G)
				.append(", '5G':").append(has5G)
				.append(", 'NetworkTechnology':'").append(infoNetworkTechnology).append("', 'CPU':'")
				.append(infoCPU).append("', 'Chipsets':").append(getSetAsJSONString(infoChipsets)).append(", 'GPU':").append(getSetAsJSONString(infoGPU))
				.append(", 'InfoOS':'").append(infoOS).append("', 'OS':'").append(osType).append("', 'OSVersion':'")
				.append(osVersion).append("', 'HeadphoneJack':").append(hasHeadphoneJack).append(", 'Loudspeaker':")
				.append(hasLoudspeaker).append(", 'Versions':")
				.append(getSetAsJSONString(versions)).append(" }");
		return builder.toString();
	}
}
