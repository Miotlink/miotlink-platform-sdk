package com.miot.android.util;

import java.io.UnsupportedEncodingException;

public class AgentUtil {
	
	public static String rfc(String oldCom) {
		String newCom = "00";
		oldCom = oldCom.replace(" ", "");
		String[] oldComArr = splitStrs(oldCom.replace(
				"F1F1", "").replace("xx7E", ""));
		for (int i = 0; i < oldComArr.length; i++) {
			newCom = hexAdd(newCom, oldComArr[i]);
		}
		String reStr = oldCom.replace("xx7E", "") + newCom + "7E";
		return reStr;
	}
	
	public static String bytesToHexString(byte[] src, int len) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0 || len > src.length) {
			return null;
		}
		for (int i = 0; i < len; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	public static  String hexAdd(String a,String b){
		String re="";
		int shi=Integer.parseInt(a,16)+Integer.parseInt(b,16);
		String shiliu=Integer.toHexString(shi).toUpperCase();
		if(shiliu.length()<2)
		{
			re="0"+shiliu;
			}
		else if(shiliu.length()>2){
			re=shiliu.substring(shiliu.length()-2, shiliu.length());
			}
		else{
			re=shiliu;
		}
		return re;
	}
	
	public static String[] splitStrs(String str){
		int m = str.length()/2;
		if (m * 2 < str.length()) {
			m++;
		}
		String[] strs = new String[m];
		int j = 0;
		for (int i = 0; i < str.length(); i++) {if (i % 2 == 0) {
				strs[j] = "" + str.charAt(i);
			} else {
				strs[j] = strs[j] + "" + str.charAt(i);
				j++;
			}
		}
		return strs;
	}
	
	public static String hexString2Byte(byte[] bs){
		String string=null;
		try {
			string=new String(bs,"ISO-8859-1");
			return string;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;
	}
	
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public static byte[] hexString2Bytes(String src) {
		src = src.replace(" ", "");
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp;
		try {
			tmp = src.getBytes("ISO-8859-1");
			for (int i = 0; i < src.length() / 2; i++) {
				ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public static int getInt(byte[] bytes) {
		return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8))
				| (0xff0000 & (bytes[2] << 16))
				| (0xff000000 & (bytes[3] << 24));
	}
	public static float getFloat(byte[] bytes) {
		String s = "";
		for (int i = 0; i < bytes.length; i++) {
			s += bytes[i] + " ";
		}
		System.out.println(s);
		return Float.intBitsToFloat(getInt(bytes));
	}
	
	public static byte[] stringTo16Byte(String temp) {

		int len = temp.length();
		for (int i = 0; i < 16 - len; i++) {
			if (temp.length() == 16) {
				break;
			}
			temp = temp + "0";

		}
		return temp.getBytes();
	}

}

