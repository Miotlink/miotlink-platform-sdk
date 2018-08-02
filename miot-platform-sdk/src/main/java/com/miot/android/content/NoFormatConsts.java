package com.miot.android.content;


public class NoFormatConsts {
	
    public static  String PLATFORM_EXTERNAL_IP = "www.51miaomiao.com";
	public static  String PLATFORM_EXTERNAL_URL ="base.51miaomiao.com";
	public static  String NAMESPACE = "http://www.buz.org/buzService/";
	public static  String ENDPOINT = "http://" + NoFormatConsts.PLATFORM_EXTERNAL_URL
			+ ":80/axis2/services/buzService?wsdl";
	public static final String SERVICE_ACTION = "com.miot.android.mmw.SERVICE";
	public static final String MOIT_PU_BROADCAST_MSG_ACTION = "com.miot.android.mmw.ON_PU_TTCONTENT_RECEIVED";
	public static final String NOTICE_LOGIN_OUT = "com.miot.android.NOTICE_LOGIN_OUT";
	public static final String MIOT_PLATFORM_RECONNECTED = "com.miot.android.mmw.MIOT_PLATFORM_RECONNECTED";
	public static final String MIOT_PU_STATED_CHANAGED="com.miot.android.mmw.MIOT_PU_STATED_CHANAGED";
	public static String PLATFORM_IP = "118.190.67.214";
	
	
	
	
	public static final void setPlarFormAddRess(int type) {
		if (type == 1) {
			PLATFORM_EXTERNAL_IP = "112.124.115.125";
			PLATFORM_EXTERNAL_URL = "112.124.115.125";
			ENDPOINT = "http://" + NoFormatConsts.PLATFORM_EXTERNAL_URL
					+ ":80/axis2/services/buzService?wsdl";
		} else if (type == 2) {
			PLATFORM_IP="118.190.67.214";
			PLATFORM_EXTERNAL_IP = "www.51miaomiao.com";
			PLATFORM_EXTERNAL_URL = "base.51miaomiao.com";
			ENDPOINT = "http://" + NoFormatConsts.PLATFORM_EXTERNAL_URL
					+ ":80/axis2/services/buzService?wsdl";
		} else if (type == 3) {
			PLATFORM_EXTERNAL_IP = "us.51miaomiao.com";
			PLATFORM_EXTERNAL_URL = "us.51miaomiao.com";
			ENDPOINT = "http://" + NoFormatConsts.PLATFORM_EXTERNAL_URL
					+ ":80/axis2/services/buzService?wsdl";
		}else if (type ==4) {
			PLATFORM_EXTERNAL_IP = "60.191.23.28";
			PLATFORM_EXTERNAL_URL = "60.191.23.28";
			ENDPOINT = "http://" + NoFormatConsts.PLATFORM_EXTERNAL_URL
					+ ":88/axis2/services/buzService?wsdl";
		}
	}
}
