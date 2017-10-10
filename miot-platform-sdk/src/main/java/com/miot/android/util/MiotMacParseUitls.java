package com.miot.android.util;

import android.annotation.SuppressLint;
import java.util.regex.Pattern;

public class MiotMacParseUitls {
	/**
	 * 判断MAC正确
	 * @param mac
	 * @return
	 */
	public static boolean isMAC(String mac){
		String patternMac="^[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}$";
        Pattern pa= Pattern.compile(patternMac);
        return pa.matcher(mac).find();
	}
	
	public static String getIMIEBuilder(String imie){
		String result="";
		if (imie.length()==15) {
			try {
				result =getSimAlgorithm(imie);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (imie.length()==29) {
			result=imie;
		}
		
		return result;
	}
	
	

    @SuppressLint("DefaultLocale")
    private static String getSimAlgorithm(String IMIE) throws Exception{
        String mac = "";
        try {
            if (IMIE.equals("")) {
                return "";
            }
            if (IMIE.length() != 15) {
                return "";
            }
            mac = IMIE.substring(2, IMIE.length());
            String imieString = mac.substring(0, 3);
            StringBuffer stringBuffer = new StringBuffer();
            if (Integer.parseInt(imieString)>255){
                String firstMacCode=Integer.toHexString(Integer.parseInt(imieString)).toUpperCase();
                if (firstMacCode.length()>1){
                    firstMacCode=firstMacCode.substring(1,firstMacCode.length());
                }
                stringBuffer.append(firstMacCode).append(":");
            }else{
                stringBuffer.append(Integer.toHexString(Integer.parseInt(imieString)).toUpperCase()).append(":");
            }
            String imieLast = mac.substring(3, mac.length());
            for (int i = 0; i < splitStrs(imieLast).length; i++) {
                if (Integer.parseInt(splitStrs(imieLast)[i]) < 16) {
                    stringBuffer.append("0" + Integer.toHexString(Integer.parseInt(splitStrs(imieLast)[i]))).append(":");
                } else {
                    stringBuffer.append(Integer.toHexString(Integer.parseInt(splitStrs(imieLast)[i]))).append(":");
                }
            }
            mac = stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
            return mac.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }
    
    
    public static String[] splitStrs(String str) {
        int m = str.length() / 2;
        if (m * 2 < str.length()) {
            m++;
        }
        String[] strs = new String[m];
        int j = 0;

        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) {
                strs[j] = "" + str.charAt(i);
            } else {
                strs[j] = strs[j] + "" + str.charAt(i);
                j++;
            }
        }
        return strs;
    }
}
