package com.telefolio.databean;

public class Device {

	private String manufactureName;
	private String name;
	private String url;
	private String imagesrcURL;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(manufactureName).append("|").append(name).append("|").append(url).append("|")
				.append(imagesrcURL);
		return builder.toString();
	}
	
	public String toJSONString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ 'Manufacture':'").append(manufactureName).append("', 'Name':'").append(name)
				.append("', 'URL':'").append(url).append("', 'ImagesrcURL':'").append(imagesrcURL).append("' }");
		return builder.toString();
	}

	public static String getHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append("manufactureName").append("|name").append("|url").append("|imagesrcURL");
		return builder.toString();
	}

	public String getManufactureName() {
		return manufactureName;
	}

	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImagesrcURL() {
		return imagesrcURL;
	}

	public void setImagesrcURL(String imagesrcURL) {
		this.imagesrcURL = imagesrcURL;
	}
	
}
