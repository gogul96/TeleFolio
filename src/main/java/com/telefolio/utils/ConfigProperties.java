package com.telefolio.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigProperties {

	public static final String EMPTY_STRING = "";
	public static final String BASE_URL = "https://www.gsmarena.com/";
	public static final String BASE_CONFIG_PATH = "config/";
	public static final String BASE_RESULT_PATH = "results/";
	private static String[] makersList = { "Apple", "Google","OnePlus","Samsung",
			 "Oppo", "Realme","Xiaomi", "vivo", "Motorola", "Nokia",
			"Honor","Asus","LG","Huawei","Sony","Lenovo" };
	public static final Set<String> MAKERS_TOBE_EXTRACTED = new HashSet<String>(Arrays.asList(makersList));
	
	public static final boolean IS_MANUFACTURE_FILE_TOBE_REFRESHED = false;
	public static final boolean IS_MANUFACTURE_TOBE_PARSED = true;

	public static final boolean IS_DEVICE_FILES_TOBE_REFRESHED = true;
	public static final boolean IS_DEVICE_TOBE_PARSED = true;
	public static final boolean IS_DEVICE_DETAILED_TOBE_PARSED = true;

	public static final boolean IS_CSV_FILE_TOBE_CREATED = true;
	public static final boolean IS_JSON_FILE_TOBE_CREATED = true;
	
	public static final String LAST_DEVICE_TO_FETCH = "LastDeviceToFetch";
	
	
}
