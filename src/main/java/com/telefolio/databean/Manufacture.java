package com.telefolio.databean;

public class Manufacture {
	private String name;
	private String absoluteURL;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbsoluteURL() {
		return absoluteURL;
	}
	public void setAbsoluteURL(String absoluteURL) {
		this.absoluteURL = absoluteURL;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append("|").append(absoluteURL);
		return builder.toString();
	}
	
	public String toJSONString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ 'Name':'").append(name).append("', 'URL':'").append(absoluteURL).append("' }");
		return builder.toString();
	}
	public static String getHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append("Name").append("|URL");
		return builder.toString();
	}
}
