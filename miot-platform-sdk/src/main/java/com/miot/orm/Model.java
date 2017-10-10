package com.miot.orm;

/**
 * 
 * 
 * 
 * @author Administrator
 * 
 */
public class Model extends Object {

	private String code;
	
	private String factoryID;
	
	private String communicationChannel;
	
	private String communicationChannelClass;
	
	private String apkPluginUrl;//插件apk下载地址
	
	private String pluginRes;//插件主页面图片
	
	private String wapPluginUrl;//ios端本地页面版本下载资源包
	
	private int pluginVerId;//插件版本号
	
	public int getPluginVerId() {
		return pluginVerId;
	}
	public void setPluginVerId(int pluginVerId) {
		this.pluginVerId = pluginVerId;
	}

	public String getApkPluginUrl() {
		return apkPluginUrl;
	}
	public void setApkPluginUrl(String apkPluginUrl) {
		this.apkPluginUrl = apkPluginUrl;
	}
	public String getPluginRes() {
		return pluginRes;
	}
	public void setPluginRes(String pluginRes) {
		this.pluginRes = pluginRes;
	}
	public String getWapPluginUrl() {
		return wapPluginUrl;
	}
	public void setWapPluginUrl(String wapPluginUrl) {
		this.wapPluginUrl = wapPluginUrl;
	}

	public String getCommunicationChannel() {
		return communicationChannel;
	}

	public void setCommunicationChannel(String communicationChannel) {
		this.communicationChannel = communicationChannel;
	}

	public String getCommunicationChannelClass() {
		return communicationChannelClass;
	}

	public void setCommunicationChannelClass(String communicationChannelClass) {
		this.communicationChannelClass = communicationChannelClass;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFactoryID() {
		return factoryID;
	}

	public void setFactoryID(String factoryID) {
		this.factoryID = factoryID;
	}

	@Override
	public String toString() {
		return "Model [code=" + code + ", factoryID=" + factoryID
				+ ", communicationChannel=" + communicationChannel
				+ ", communicationChannelClass=" + communicationChannelClass
				+ "]";
	}

}
