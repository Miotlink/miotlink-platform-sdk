package com.cncrit.qiaoqiao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class Tools {
	static private String tag = Tools.class.getName();

	static public boolean GetFileByHttp(String url, String path,
			String fileName, boolean recoverOldFile) {
		try {
			// String FileName = "";
			if (fileName == null || fileName == "")
				fileName = url.substring(url.lastIndexOf("/") + 1);
			if (fileName == "") {
				throw new RuntimeException(
						"GetFileByHttp: filename can't be empty after parse url!");
			}

			String fileFullName = path + fileName;
			File f = new File(fileFullName);
			if (f.exists()) {
				if (recoverOldFile)
					f.delete();
				else
					return true;
			}
			URL Url = new URL(url);
			URLConnection conn = Url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			int fileSize = conn.getContentLength();// �����Ӧ��ȡ�ļ���С
			if (fileSize <= 0) { // ��ȡ���ݳ���Ϊ0
				throw new RuntimeException("�޷���֪�ļ���С ");
			}
			if (is == null) { // û��������
				throw new RuntimeException("�޷���ȡ�ļ�");
			}
			FileOutputStream FOS = new FileOutputStream(fileFullName);
			// ����д���ļ��ڴ�����ͨ�������Ŀ��д�ļ�
			byte buf[] = new byte[1024];
			int downLoadFilePosition = 0;
			int numread;
			while ((numread = is.read(buf)) != -1) {
				FOS.write(buf, 0, numread);
				downLoadFilePosition += numread;
			}
			is.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	static public void Alert(String title, String msg, Context c) {
		AlertDialog.Builder alert = new AlertDialog.Builder(c);
		alert.setTitle(title);
		alert.setMessage(msg);
		alert.show();
	}

	static public void AlertError(String msg, Context c) {
		Alert("�����ˣ�", msg, c);
	}

	static public void AlertWarning(String msg, Context c) {
		Alert("��ע��:", msg, c);
	}

	static public void StartUriActivity(Context c, String uriString) {
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		c.startActivity(intent);
	}

	static public void StartActivity(Context c, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(c, cls);
		c.startActivity(intent);
	}

	static public int GetTickTime() {
		return (int) ((new Date()).getTime() / 1000);
	}

	private static String rootDir = "";

	public static String getRootDir() {
		return rootDir;
	}

	public static void setRootDir(String rootDir) {
		try {
			File f = new File(rootDir);
			if (!f.exists()) {
				if (!f.mkdirs()) {
					Log.e(tag, "setRootDir: create rootDir false!");
				}
			}
			Tools.rootDir = rootDir;
		} catch (Exception e) {
			Log.e(tag, "setRootDir: exception raised! " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static String configFileName = "";

	static public String GetProfileString(String section, String variable,
			String defaultValue) {
		String configFileFullName = rootDir + configFileName;
		return IniFile.getProfileString(configFileFullName, section, variable,
				defaultValue);
	}

	static public boolean SetProfileString(String section, String variable,
			String value) {
		String configFileFullName = rootDir + configFileName;
		return IniFile.setProfileString(configFileFullName, section, variable,
				value);
	}

	static public int BytesToInt16LE(byte[] buff, int startPos) {
		short a = (short) ((buff[startPos + 1] & 0xff) << 8);
		short c = (short) (buff[startPos] & 0xff);
		return (short) (a + c);
	}

	static public int BytesToInt16BE(byte[] buff, int startPos) {
		short a = (short) ((buff[startPos] & 0xff) << 8);
		short c = (short) (buff[startPos + 1] & 0xff);
		return (short) (a + c);
	}

	static int toInt(byte[] b, int offset) {
	    int value= 0;
	    for (int i = 0; i < 4; i++) {
	        int shift= (4 - 1 - i) * 8;
	        value +=(b[i + offset] & 0x000000FF) << shift;
	    }
	    return value;
}
	// 256*256*256 = 16777216, 256*256=65536
	static public void Int16ToBytesLE(int n, byte[] buff, int startPos) {
		int m = n % 65536;
		buff[startPos + 1] = (byte) (m / 256);
		buff[startPos] = (byte) (m % 256);
	}

	static public void Int16ToBytesBE(int n, byte[] buff, int startPos) {
		int m = n % 65536;
		buff[startPos] = (byte) (m / 256);
		buff[startPos + 1] = (byte) (m % 256);
	}

	static public int BytesToInt32BE(byte[] buff, int startPos) {
		int a = (int) ((buff[startPos] & 0xff) << 24);
		System.out.println(a);
		int b = (int) ((buff[startPos + 1] & 0xff) << 16);
		System.out.println(b);
		int c = (int) ((buff[startPos + 2] & 0xff) << 8);
		System.out.println(c);
		int d = (int) (buff[startPos + 3] & 0xff);
		System.out.println("d=" + d + "buff[15]=" + buff[startPos + 3]);
		return (int) (a + b + c + d);
	}

	static public void Int32ToBytesBE(int n, byte[] buff, int startPos) {
		buff[startPos] = (byte) (n / 16777216);// 21
		buff[startPos + 1] = (byte) ((n % 16777216) / 65536);// 1
		buff[startPos + 2] = (byte) ((n % 65536) / 256);// 0
		buff[startPos + 3] = (byte) (n % 256);// 0
	}

	static public void LongToBytesBE(long n, byte[] buff, int startPos) {
		buff[startPos] = (byte) (n / 16777216);// 21
		buff[startPos + 1] = (byte) ((n % 16777216) / 65536);// 1
		buff[startPos + 2] = (byte) ((n % 65536) / 256);// 0
		buff[startPos + 3] = (byte) (n % 256);// 0
	}
	
	static public int BytesToInt32LE(byte[] buff, int startPos) {
		int a = (int) ((buff[startPos + 3] & 0xff) << 24);
		int b = (int) ((buff[startPos + 2] & 0xff) << 16);
		int c = (int) ((buff[startPos + 1] & 0xff) << 8);
		int d = (int) (buff[startPos] & 0xff);
		return (int) (a + b + c + d);
	}

	static public void Int32ToBytesLE(int n, byte[] buff, int startPos) {
		buff[startPos + 3] = (byte) (n / 16777216);
		buff[startPos + 2] = (byte) ((n % 16777216) / 65536);
		buff[startPos + 1] = (byte) ((n % 65536) / 256);
		buff[startPos] = (byte) (n % 256);
	}

	static public String LongIp2String(long nIp) {
		// Log.d(tag,"LongIp2String: "+(nIp/16777216)+","+((nIp%16777216)/65536));
		String sIp = ((nIp / 16777216) & 0xFF) + "."
				+ (((nIp % 16777216) / 65536) & 0xFF) + "."
				+ (((nIp % 65536) / 256) & 0xFF) + "." + ((nIp % 256) & 0xFF);
		System.out.println("sIp=" + sIp);
		return sIp;
	}

	static public long String2LongIp(String ip) {
		// Log.d(tag,"LongIp2String: "+(nIp/16777216)+","+((nIp%16777216)/65536));
		String[] ns = ip.split("\\.");
		if ( ns.length != 4 ){
			Log.e(tag,"String2LongIp: wrong ip string["+ip+"],will return 0l!");
			return 0l;
		}
		
		return Long.parseLong(ns[0])*16777216
				+Long.parseLong(ns[1])*65536
				+Long.parseLong(ns[2])*256
				+Long.parseLong(ns[3]);
	}

	static public String Bin2HexString(byte[] buff, int startPos, int length) {
		String sRet = "{";
		int endPos = (startPos + length) > buff.length ? (buff.length - startPos)
				: length;
		if (endPos > 10240)
			return "{ length > 10240!!! }";
		int count = 0;
		for (int i = startPos; i < endPos; i++) {
			String hex = Integer.toHexString(buff[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sRet += hex;
			if ((++count) % 8 == 0)
				sRet += ".";
			else if (count % 4 == 0)
				sRet += " ";
		}

		return sRet + "}";
	}

	static public void encode(byte[] bs,int startPos){
		if ( bs == null)
			return;
		for(int i=startPos;i<bs.length;i++){
			bs[i] = (byte) (bs[i] ^ 48);
		}
	}
	
	static public String FormatZeroString(int count) {
		String s = "";
		for (int i = 0; i < count; i++)
			s += "\0";
		return s;
	}

	static public String FormatFixLengthString(int length, String content) {
		byte[] ba = new byte[length];
		byte[] bc = content.getBytes();
		int len = bc.length > length ? length : bc.length;
		System.arraycopy(bc, 0, ba, 0, len);
		for (int i = len; i < length; i++)
			ba[i] = 0;
		return new String(ba);
	}

	static public String ParseXmlElement(String xml, String elementName) {
		String value = "";
		try {
			int leftPos = xml.indexOf("<" + elementName);
			if (leftPos == -1)
				return "";
			leftPos = xml.indexOf('>', leftPos);
			if (leftPos == -1)
				return "";
			leftPos++;
			int rightPos = xml.indexOf("</" + elementName + ">");
			if (rightPos == -1)
				return "";
			if (rightPos <= leftPos)
				return "";
			value = xml.substring(leftPos, rightPos);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	static public String TickTime2DateString(String tickTime) {
		try {
			long tt = Long.parseLong(tickTime);
			Date dt = new Date();
			dt.setTime(tt * 1000);
			return (dt.getMonth() >= 9 ? dt.getMonth() : "0"
					+ (dt.getMonth() + 1))
					+ "/"
					+ (dt.getDate() > 9 ? dt.getDate() : "0" + dt.getDate())
					+ " "
					+ (dt.getHours() > 9 ? dt.getHours() : "0" + dt.getHours())
					+ ":"
					+ (dt.getMinutes() > 9 ? dt.getMinutes() : "0"
							+ dt.getMinutes());
		} catch (Exception e) {
			e.printStackTrace();
			return "00/00 00:00";
		}
	}
}
