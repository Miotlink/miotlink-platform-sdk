package com.cncrit.qiaoqiao.vsp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.cncrit.qiaoqiao.Tools;
import com.cncrit.qiaoqiao.VspOperation;
import com.miot.android.Service;

import android.util.Log;

public class TcpSocket<IReceiver> implements Runnable{
	public static String tag = TcpSocket.class.getName();
    public static String DefaultCharSetName = "ISO-8859-1"; // "UTF-8"; // "ISO-8859-1";

	interface IReceiver {
		public void onReceive(byte[] recvData, int recvLen);
	}

	TcpSocket(IReceiver tsr){
		this.tsr = tsr;
	}
	protected void finalize() {
		this.disconnect();
	}
	
	public boolean hasConnect()	{
		return sc.isConnected();
	}
	private IReceiver tsr = null;
	private Socket sc = null;
	private InputStream is = null;
	private DataOutputStream os = null;
	private Thread recvThread = null;
	boolean isRunning = false;
	byte[] recvBuff = new byte[10240];
	
	private String ip=null;
	
	private int port=0;

	public boolean send(String sendString) {
		try {			
			return send(sendString.getBytes(DefaultCharSetName),sendString.length());
		} catch (UnsupportedEncodingException e) {
			Log.e(tag, "send: UnsupportedEncodingException raised!");
			e.printStackTrace();
			return false;
		}
	}

	public Socket getSc() {
		return sc;
	}

	public boolean send(byte[] sendBuff,int length) {
		try {
			Log.e(tag, "send: "+Tools.Bin2HexString(sendBuff,0,length)+"length"+length);
			synchronized (this)
			{ 
				if( sc != null && os != null ){
					os.write(sendBuff, 0, length);
					os.flush();
				}
			}
		} catch (Exception e) {
			Log.e(tag, "send: Exception raised!");
			e.printStackTrace();
			return false;
		}

		return true;
	}
	private boolean isConnect=false;
	
	private int i=0;

	
	private static Boolean isNetWorkOk=null;
	private static byte[] syn = new byte[0];
	public static synchronized Boolean isNetWorkOk(){
		return isNetWorkOk;
	}
	public boolean connect(String strServerIP, int nPort) {
		try {
			this.ip=strServerIP;
			this.port=nPort;
			sc = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(strServerIP,nPort);
			sc.connect(socketAddress, 10000);
			isConnect=true;
			synchronized (syn) {
				isNetWorkOk = true;
			}
			System.out.println("Connect Ok! "+strServerIP+":"+nPort);
			is = sc.getInputStream();
			System.out.println("QQ---is="+is);
			os = new DataOutputStream(sc.getOutputStream());
			recvThread = new Thread(null, this);
			recvThread.start();
			
		} catch (UnknownHostException e) {
			isConnect=false;
			VspOperation.loginFailCode=-1;
			VspOperation.loginFailErrorMess="连接服务器失败,检查网络";
			Log.e(tag, "connect:["+strServerIP+":"+nPort+"] UnknownHostException " + e.getMessage());
			return false;
		} catch (IOException e) {
			isConnect=false;
			VspOperation.loginFailErrorMess="连接服务器失败,检查网络";
			Log.e(tag, "connect:["+strServerIP+":"+nPort+"] IOException " + e.getMessage());
			VspOperation.loginFailCode=-1;
			return false;
		} catch (Exception e) {
			isConnect=false;
			VspOperation.loginFailErrorMess="连接服务器失败,检查网络";
			VspOperation.loginFailCode=-1;
			Log.e(tag, "connect:["+strServerIP+":"+nPort+"] Exception " + e.getMessage());
			return false;
		}
		return true;
	}

	void finnalize(){
		this.disconnect();
	}
	
	public void disconnect() {
		isConnect=false;
		isRunning = false;
		try {
			if ( sc != null ) {
				if (sc.isConnected()) {
					sc.shutdownInput();			
					synchronized (this) { 
						sc.shutdownOutput();
						sc.close();
					}
				}
				sc = null;
				is = null;
				os = null;
			}
		} catch (IOException e) {
			Log.e(tag,"disconnect: Exception " + e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			i++;
			try {
				int recvLen = is.read(recvBuff);
				byte[] a = recvBuff;
				if (recvLen <= 0) {
					Thread.sleep(2);
				} else {
					if(tsr != null){
						((IReceiver) tsr).onReceive(recvBuff, recvLen);
					}
				}
//				System.gc();
//				Runtime.getRuntime().gc();
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(tag, "Receive Error: " + e.toString());
				break;
			}
			
		}
		if (isConnect) {
			Service.sendBroadcast("com.miot.android.MIOT_PLATFORM_RECONNECTED", new String []{"1",""});
			isConnect=false;
		 }
		Log.e(tag, "Socket Client Exit");
	
		
	}
}