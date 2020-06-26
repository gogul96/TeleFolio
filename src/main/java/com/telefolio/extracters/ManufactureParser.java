package com.telefolio.extracters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.telefolio.databean.Manufacture;
import com.telefolio.utils.ConfigProperties;

public class ManufactureParser {

	private static Logger logger = Logger.getLogger(ManufactureParser.class);
	private static List<Manufacture> manufactureList = null;

	public List<Manufacture> extractElement(Element makers) throws Exception {
		try{
		// Element Makers has a div which holds the whole list
		Element table = makers.children().first();
		Element tableBody = table.children().first();
		manufactureList = new ArrayList<Manufacture>();
		Elements rowElements = tableBody.children();
		for(Element row:rowElements){
			for(Element subRow: row.children()){
				extractMakerFromTD(subRow.children());
			}
		}
		}
		catch(Exception e){
			logger.fatal(e.getMessage());
			logger.trace(e +"\n" + e.getCause());
			throw e;
		}
		logger.debug("Manufactures Extracted : " + manufactureList.stream().map((e)->e.getName()).collect(Collectors.toList()));
		logger.info(manufactureList.size() + " no.of Manufacture extracted");
		return manufactureList;
	}

	private void extractMakerFromTD(Elements subRow) {
		try{
			Element element = subRow.first();
			Manufacture manufacture = new Manufacture();
			String[] text = element.text().split(" ");
			manufacture.setName(text.length > 3 ? text[0] + " " + text[1] : text[0]);
			manufacture.setAbsoluteURL(ConfigProperties.BASE_URL + element.attr("href"));
			if (ConfigProperties.MAKERS_TOBE_EXTRACTED.contains(manufacture.getName())) {
				manufactureList.add(manufacture);
			}
		}
		catch(Exception e){
			logger.trace(e.getCause());
			throw e;
		}
	}
}
