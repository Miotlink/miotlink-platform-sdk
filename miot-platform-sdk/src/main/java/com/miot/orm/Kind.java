package com.miot.orm;

public class Kind extends Object {
	private String onlineImage;
	private String offlineImage;
	private String kindImage;

	private String packageName;
	private String className;

	public String getOnlineImage() {
		return onlineImage;
	}

	public void setOnlineImage(String onlineImage) {
		this.onlineImage = onlineImage;
	}

	public String getOfflineImage() {
		return offlineImage;
	}

	public void setOfflineImage(String offlineImage) {
		this.offlineImage = offlineImage;
	}

	public String getKindImage() {
		return kindImage;
	}

	public void setKindImage(String kindImage) {
		this.kindImage = kindImage;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
