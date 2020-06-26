package com.telefolio.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeviceMappingProperties {
	private static String[] igonorePathList = { "Misc/Price", "Misc/SAR", "Misc/SAREU", "Battery/Talktime",
			"Battery/Musicplay", "Battery/Stand-by", "MainCamera", "Selfiecamera","Camera","Network/GPRS","Network/EDGE"
			,"Features/Browser","Sound/Alerttypes","MainCamera/DualorTriple"};
	public static final Set<String> IGNORE_PATH_LIST = new HashSet<String>(Arrays.asList(igonorePathList));
	private static String[] dateFormats = {"yyyy, MMMM dd","yyy, MMMM"};
	public static final Set<String> DATE_FORMATS = new HashSet<String>(Arrays.asList(dateFormats));
	
	public static final String EMPTY_STRING = "";
	public static final String VALUE_CAMERA_1 = "Single";
	public static final String VALUE_CAMERA_2 = "Dual";
	public static final String VALUE_CAMERA_3 = "Triple";
	public static final String VALUE_CAMERA_4 = "Quad";
	public static final String VALUE_CAMERA_5 = "Five";
	public static final String PATH_BATTERY = "Battery";
	public static final String PATH_INFOBATTERY = "Battery/Charging";

	public static final String PATH_BODYBUILD = "Body/Build";
	public static final String PATH_BODYDIMENSION = "Body/Dimensions";
	public static final String PATH_BODYSIM = "Body/SIM";
	public static final String PATH_BODYWEIGHT = "Body/Weight";
	public static final String PATH_INFOBLUETOOTH = "Comms/Bluetooth";
	public static final String PATH_INFOGPS = "Comms/GPS";
	public static final String PATH_INFONFC = "Comms/NFC";
	public static final String PATH_INFORADIO = "Comms/Radio";
	public static final String PATH_INFOUSB = "Comms/USB";
	public static final String PATH_INFOWIFI = "Comms/WLAN";
	public static final String PATH_INFOINFRAREDPORT = "Comms/Infraredport";

	public static final String PATH_DISPLAYPROTECTION = "Display/Protection";
	public static final String PATH_DISPLAYRESOLUTION = "Display/Resolution";
	public static final String PATH_DISPLAYSIZE = "Display/Size";
	public static final String PATH_DISPLAYTYPE = "Display/Type";
	public static final String PATH_FEATURESENSOR = "Features/Sensors";
	public static final String PATH_LAUNCHDATE = "Launch/Announced";
	public static final String PATH_SELLINGSTATUS = "Launch/Status";

	public static final String PATH_MAINCAMERA1 = "MainCamera/Single";
	public static final String PATH_MAINCAMERA2 = "MainCamera/Dual";
	public static final String PATH_MAINCAMERA3 = "MainCamera/Triple";
	public static final String PATH_MAINCAMERA4 = "MainCamera/Quad";
	public static final String PATH_MAINCAMERA5 = "MainCamera/Five";
	public static final String PATH_MAINCAMERAFEATURE = "MainCamera/Features";
	public static final String PATH_MAINCAMERAVIDEO = "MainCamera/Video";

	public static final String PATH_SELFIECAMERA1 = "Selfiecamera/Single";
	public static final String PATH_SELFIECAMERA2 = "Selfiecamera/Dual";
	public static final String PATH_SELFIECAMERA3 = "Selfiecamera/Triple";
	public static final String PATH_SELFIECAMERAFEATURES = "Selfiecamera/Features";
	public static final String PATH_SELFIECAMERAVIDEO = "Selfiecamera/Video";

	public static final String PATH_MEMORYMMCCARD = "Memory/Cardslot";
	public static final String PATH_MEMORYINTERNAL = "Memory/Internal";
	public static final String PATH_COLORS = "Misc/Colors";
	public static final String PATH_MODELS = "Misc/Models";

	public static final String PATH_2GBANDS = "Network/2Gbands";
	public static final String PATH_3GBANDS = "Network/3Gbands";
	public static final String PATH_4GBANDS = "Network/4Gbands";
	public static final String PATH_5GBANDS = "Network/5Gbands";
	public static final String PATH_NETWORKSPEED = "Network/Speed";
	public static final String PATH_INFONETWORK = "Network/Technology";

	public static final String PATH_INFOCPU = "Platform/CPU";
	public static final String PATH_INFOCHIPSET = "Platform/Chipset";
	public static final String PATH_INFOGPU = "Platform/GPU";
	public static final String PATH_INFOOS = "Platform/OS";

	public static final String PATH_INFOHEADPHONEJACK = "Sound/3.5mmjack";
	public static final String PATH_INFOLOUDSPEAKER = "Sound/Loudspeaker";
	public static final String PATH_VERSIONS = "Versions";

}
