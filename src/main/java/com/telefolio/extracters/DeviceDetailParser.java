package com.telefolio.extracters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.telefolio.databean.DeviceDetailed;
import com.telefolio.utils.ConfigProperties;
import com.telefolio.utils.DeviceMappingProperties;

public class DeviceDetailParser {

	static Logger logger = Logger.getLogger(DeviceDetailParser.class);

	public String deviceName;
	public String manufactureName;
	private Map<String,String> specValues = null;
	private DeviceSpecification deviceSpec = null;
	public List<String> failToParseDevices = null;
	
	public DeviceDetailParser() {
		specValues = new HashMap<String,String>();
		deviceSpec = new DeviceSpecification();
		failToParseDevices = new ArrayList<String>();
	}
	
	public DeviceDetailed extractElement(Element element) {
		try {
			Elements specsTables = element.getElementsByTag("table");
			for (Element specsTable : specsTables) {
				parseTables(specsTable);
			}
			if (element.getElementsByTag("p").size() > 0) {
				String[] versions = element.getElementsByTag("p").first().text().split(":");
				specValues.put("Versions", versions.length > 1 ? versions[1] : ConfigProperties.EMPTY_STRING);
			} else {
				specValues.put("Versions", ConfigProperties.EMPTY_STRING);
			}
			logger.debug("Total SpecValues " + specValues.size());
			logger.debug(deviceName + " : "+ specValues);
			specValues = deviceSpec.parseValues(deviceName, specValues);
			logger.debug("Spec values not parsed " + specValues.size());

			if (specValues.size() > 0) {
				logger.warn(deviceName + " : Remaining Spec Values " + specValues.toString());
			}
			logger.info(deviceName + " :: Parsing Completed");
			logger.debug(deviceName + " Parsing Details [ " + deviceSpec + " ]");
			return deviceSpec.getDeviceDetailed();
		} catch (Exception e) {
			failToParseDevices.add(deviceName);
			logger.trace(e);
			logger.fatal("Exception occrured in parsing " + deviceName);
		}
		return null;
	}

	private void parseTables(Element first) {
		Elements rows = first.children().first().children();
		Element headerElement = rows.first().getElementsByTag("th").first();
		String mainHeading = headerElement.text() + "/";
		String subHeading = "/";
		for (Element row : rows) {
			Elements subElements = row.getElementsByTag("td");
			for (Element subElement : subElements) {
				if (subElement.hasClass("ttl")) {
					if (!subElement.text().isEmpty())
						subHeading = subElement.text();
				} else if (subElement.hasClass("nfo") && !mainHeading.startsWith("Tests")) {
					String value = "";
					String header = (mainHeading + subHeading).replaceAll(" ", "");
					if (header.endsWith("//")) {
						header = header.substring(0, header.length() - 2);
					}
					if (!DeviceMappingProperties.IGNORE_PATH_LIST.contains(header)) {
						if (specValues.get(header) != null) {
							value += specValues.get(header).toString() + "::";
						}
						value += subElement.text();
						specValues.put(header, value.trim());
					}
				}
			}
		}
	}
}
