package com.cncrit.qiaoqiao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.google.gson.Gson;

public class WebService {
	private String nameSpace = "";
	private String endPoint = "";
	public WebService(String nameSpace,String endPoint){
		this.nameSpace = nameSpace;
		this.endPoint = endPoint;
	}
	public synchronized String call(String methodName,Object[][] params){
        String soapAction = nameSpace + methodName;
		// 指定WebService的命名空间和调用的方法名  
        SoapObject rpc = new SoapObject(nameSpace, methodName);  
  
        if ( params != null ){
        	for(Object[] objs : params){
        		rpc.addProperty((String)objs[0],objs[1]);
        	}
        }
//        rpc.addProperty("opType", 1);  
//        rpc.addProperty("requestJson", "");  
//        rpc.addProperty("reserve","");
        
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版�? 
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);  
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;  
        // 设置是否调用的是dotNet�?��的WebService  
        envelope.dotNet = false;  
        // 等价于envelope.bodyOut = rpc;  
        envelope.setOutputSoapObject(rpc);  
  
        HttpTransportSE transport = new HttpTransportSE(endPoint);  
        try {  
            // 调用WebService  
            transport.call(soapAction, envelope);  
            // 获取返回的数�? 
	//          SoapObject object = (SoapObject)envelope.bodyIn;
	          Object object = envelope.getResponse();
	          // 获取返回的结�? 
	//          03-11 07:30:37.280: I/System.out(444): [{"data":"[{\"deviceType\":{\"columnNames\":[\"id\",\"name\"],\"rows\":[[\"1\",\"4004_v1.0\"],[\"2\",\"4004_v1.2\"]]}}]","errorMsg":"success","resultCode":1}]
	          return object.toString();
        } catch (Exception e) {  
            e.printStackTrace();  
            return "";
        }  
  
	}
	
	public static boolean isCallSuccess(String responseString){
		return responseString.indexOf("resultCode\":1}]")>=0;
	}
	
	public static boolean als_isCallSuccess(String responseString){
		return responseString.indexOf("resultCode\":1}")>=0;
	}
	
	public static String getDataJsonString(String responceString){
        String so = responceString.substring(11,responceString.indexOf("]\",\"errorMsg"));
        so = so.replaceAll("\\\\", "");
        System.out.println(so);
        return so;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String nameSpace = "http://www.buz.org/buzService/";
////		WebService ws = new WebService(nameSpace, endPoint);
//		String endPoint = "http://192.168.10.50:91/axis2/services/buzService?wsdl";
//		String methodName = "init";
//		String soapAction = nameSpace + methodName;
//		
//		// 指定WebService的命名空间和调用的方法名  
//        SoapObject rpc = new SoapObject(nameSpace, methodName);  
//  
//        // 设置�?��用WebService接口�?��传入的两个参数mobileCode、userId  
//        rpc.addProperty("opType", 1);  
//        rpc.addProperty("requestJson", "");  
//        rpc.addProperty("reserve","");
//        
//        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版�? 
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);  
//  
//        envelope.bodyOut = rpc;  
//        // 设置是否调用的是dotNet�?��的WebService  
//        envelope.dotNet = false;  
//        // 等价于envelope.bodyOut = rpc;  
//        envelope.setOutputSoapObject(rpc);  
//  
//        HttpTransportSE transport = new HttpTransportSE(endPoint);  
//        try {  
//            // 调用WebService  
//            transport.call(soapAction, envelope);  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//  
//        // 获取返回的数�? 
//        SoapObject object = (SoapObject)envelope.bodyIn;  
//        // 获取返回的结�? 
////        03-11 07:30:37.280: I/System.out(444): [{"data":"[{\"deviceType\":{\"columnNames\":[\"id\",\"name\"],\"rows\":[[\"1\",\"4004_v1.0\"],[\"2\",\"4004_v1.2\"]]}}]","errorMsg":"success","resultCode":1}]
//        System.out.println(object.getProperty(0));
        
        String nameSpace = "http://www.buz.org/buzService/";
		String endPoint = "http://192.168.10.83:91/axis2/services/buzService?wsdl";
		WebService ws = new WebService(nameSpace, endPoint);
//		String methodName = "init";
//		Object[][] params = new Object[][]{
//        	new Object[]{"opType", Integer.valueOf(1)},  
//			new Object[]{"requestJson", ""},  
//			new Object[]{"reserve",""}};
//		[{"data":"[{\"deviceType\":{\"columnNames\":[\"id\",\"name\"],\"rows\":[[\"1\",\"4004_v1.0\"],[\"2\",\"4004_v1.2\"]]}}]","errorMsg":"success","resultCode":1}]
		
		String methodName = "getPu";
		Object[][] params = new Object[][]{
        	new Object[]{"opType", Integer.valueOf(1)},  
			new Object[]{"requestJson", "[{\"cuID\":"+10+"}]"},  
			new Object[]{"reserve",""}};
//		[{"data":"[{\"homes\":[],\"id\":\"10\",\"mobile\":\"13412345678\",\"name\":\"liyang\",\"nickname\":\"李阳\",\"password\":\"\",\"resourceUrl\":\"\",\"userData\":\"\"}]","errorMsg":"success","resultCode":1}]
		
        String s = ws.call(methodName, params);
        
        System.out.println(s);
        
        String so = s.substring(11,s.indexOf("]\",\"errorMsg"));
        so = so.replaceAll("\\\\", "");
        System.out.println(so);
        Gson gson = new Gson();
//        Cu cu = gson.fromJson(so, Cu.class);
//        System.out.println(gson.toJson(cu));
//        "[{\"data\":\"[","]\",\"errorMsg\"";
  	}

}
