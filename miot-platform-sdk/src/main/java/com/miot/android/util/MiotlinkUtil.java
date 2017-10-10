package com.miot.android.util;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;

/**
 * 用于发送数据到设备组装数据和解析设备返回的数据的工具
 * 
 * @author Administrator
 * 
 */

public class MiotlinkUtil {
	public static String doLinkBindMake(String UserBinaryData) {
		String CodeName = "";
		String Chn = "";
		int len = 0;
		String sMlcc = "";
		try {
			CodeName = "GetUartData";
			Chn = "0";
			byte[] by = AgentUtil.hexString2Bytes(UserBinaryData);
			len = by.length;
			sMlcc = "CodeName=" + CodeName + "&Chn=" + Chn + "&Len=" + len
					+ "&UserBinaryData=";
			byte[] mlcc = sMlcc.getBytes("ISO-8859-1");
			byte[] bs = new byte[by.length + mlcc.length];
			System.arraycopy(mlcc, 0, bs, 0, mlcc.length);
			System.arraycopy(by, 0, bs, mlcc.length, by.length);
			sMlcc = new String(bs, "ISO-8859-1");
			return sMlcc;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sMlcc;

	}

	

	public static String doInfraredLinkBindMake(int[] patternsInArray) {
		StringBuffer buffer=new StringBuffer();
		if(patternsInArray.length>0){
			for (int i = 0; i < patternsInArray.length; i++) {
				buffer.append(stringFill(Integer.toHexString(patternsInArray[i]),4,'0',true));
			}
		}
		
		String CodeName = "";
		String Chn = "";
		int len = 0;
		String sMlcc = "";
		try {
			CodeName = "IRSet";
			Chn = "0";
			byte[] by = AgentUtil.hexString2Bytes(buffer.toString());
			len = by.length;
			sMlcc = "CodeName=" + CodeName +  "&Len=" + len
					+ "&UserBinaryData=";
			byte[] mlcc = sMlcc.getBytes("ISO-8859-1");
			byte[] bs = new byte[by.length + mlcc.length];
			System.arraycopy(mlcc, 0, bs, 0, mlcc.length);
			System.arraycopy(by, 0, bs, mlcc.length, by.length);
			sMlcc = new String(bs, "ISO-8859-1");
			return sMlcc;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sMlcc;
		
	}
	

	public static String stringFill(String source, int fillLength, char fillChar, boolean isLeftFill) {
	    if (source == null || source.length() >= fillLength) return source;
	     
	    StringBuilder result = new StringBuilder(fillLength);
	    int len = fillLength - source.length();
	    if (isLeftFill) {
	        for (; len > 0; len--) {
	            result.append(fillChar);
	        }
	        result.append(source);
	    } else {
	        result.append(source);
	        for (; len > 0; len--) {
	            result.append(fillChar);
	        }
	    }
	    return result.toString();
	}

	public static String doLinkBindByteMake(byte[] by) {
		String CodeName = "";
		String Chn = "";
		int len = 0;
		String sMlcc = "";
		try {
			CodeName = "GetUartData";
			Chn = "0";
			len = by.length;
			sMlcc = "CodeName=" + CodeName + "&Chn=" + Chn + "&Len=" + len
					+ "&UserBinaryData=";
			byte[] mlcc = sMlcc.getBytes("ISO-8859-1");
			byte[] bs = new byte[by.length + mlcc.length];
			System.arraycopy(mlcc, 0, bs, 0, mlcc.length);
			System.arraycopy(by, 0, bs, mlcc.length, by.length);
			sMlcc = new String(bs, "ISO-8859-1");
			return sMlcc;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sMlcc;

	}

	public static String doLinkBindParse(String string) {
		String smlcc = "";
		try {
			if (string.startsWith("UserBinaryData=",
					string.indexOf("UserBinaryData="))) {
				smlcc = string.substring(string.indexOf("UserBinaryData=")
						+ "UserBinaryData=".length(), string.length());
				int length = Integer.parseInt(string.substring(
						string.indexOf("Len=") + "Len=".length(),
						string.indexOf("&UserBinaryData=")));
				if (length > smlcc.length()) {
					return "";
				}
				smlcc = smlcc.substring(0, length);
			} else {
				smlcc = string;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smlcc;
	}

	public static String doLinkBindByteToString(String string) {
		String mlccString = "";
		String lwSmlcc = "";
		try {
			mlccString = string.substring(string.indexOf("UserBinaryData=")
					+ "UserBinaryData=".length(), string.length());
			lwSmlcc = byteToStr(mlccString);
			return lwSmlcc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lwSmlcc;

	}

	@SuppressLint("DefaultLocale")
	public static String byteToStr(String msgIn) {
		String byteStr = "";
		try {
			byte[] bytes;
			bytes = msgIn.getBytes("iso-8859-1");
			byteStr = Bytes2HexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byteStr = byteStr.toUpperCase();
		return byteStr;
	}

	// 从字节数组到十六进制字符串转换
	public static String Bytes2HexString(byte[] b) {
		byte[] hex = "0123456789ABCDEF".getBytes();
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	
}
