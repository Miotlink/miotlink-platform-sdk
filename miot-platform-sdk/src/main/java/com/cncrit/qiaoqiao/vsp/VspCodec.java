package com.cncrit.qiaoqiao.vsp;

import android.util.Log;

import com.cncrit.qiaoqiao.Tools;

public class VspCodec implements TcpSocket.IReceiver{
	static public String tag = VspCodec.class.getName();
	private TcpSocket<VspCodec> ts = null;
	private String name = "unnamed_VspCodec";
	
	public interface IVspMessageListener {
		public boolean onMessageReceived(VspMessage vm);
		public void onRecvError();  
	}
	
	private IVspMessageListener vml = null;
	public boolean initial(String name, String ip, int port, IVspMessageListener vml){
		System.out.println("initial is right--1");
		VspDefine.initial();
		System.out.println("initial is right--2");
		this.vml = vml;
		this.setName(name);		
		System.out.println("initial is right--3 name="+name);
		ts = new TcpSocket<VspCodec>(this);
//		System.out.println("tcpsocket is right--4"+ts.connect(ip, port));
		if ( ts != null){
			System.out.println("ts is not null");
			return ts.connect(ip, port);
		}
		else{
			System.out.println("ts is null");
			Log.e(tag,"initial: can not new tcpSocket!");
			return false;
		}
	}	
	
	public void destroy(){
		if(ts!=null ){
			try {
				ts.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ts.isRunning = false;			
		}
	}
	
	public boolean hasConnect(){
		if ( ts == null )
			return false;
		else
			return ts.hasConnect();
	}
	
	protected void finalize() {
		this.destroy();
	}
	
	private byte []leavedBuff = new byte[VspMessage.MSG_MAX_LENGTH];
	private int leavedLen = 0;
	private int pos = 0;
	
	private int pullRecvData(byte []recvData, int len){
		System.arraycopy(recvData, pos, leavedBuff, leavedLen, len);
		leavedLen += len;
		pos += len;
		return len;
	}
	
	private void onRecvError(){
		this.destroy();
		if(vml!=null)
			vml.onRecvError();
	}
	
	public boolean send (VspMessage vm){
		
		if( ts == null ){
			Log.e(tag,"send: ts should not be null!");
			return false;
		}
		if(vm == null){
			Log.e(tag,"send: vm should not be null!");
			return false;
		}
		vm.encode();
		byte[] s = vm.getBuff();
		Log.d(tag,"send: "+ Tools.Bin2HexString(vm.getBuff(), 0, vm.getLength()));
		Tools.encode(s, 8);
		return ts.send(vm.getBuff(),vm.getLength());
	}
	
	@Override
	public void onReceive(byte[] recvData, int recvLen) {		
		Log.d(tag,"OnReceive: recvLen=" + recvLen + ",leavedLength="+leavedLen);
		byte[] a = recvData;
		pos = 0;
		while(recvLen > 0){
			if(leavedLen+recvLen < VspMessage.MSG_SESSION_ID_POS) {
				recvLen -= pullRecvData(recvData,recvLen);
				Log.d(tag,"onReceive: should wait for more bytes when judging vmLen!");
				return;
			}			
			if(leavedLen < VspMessage.MSG_SESSION_ID_POS) 
				recvLen -= pullRecvData(recvData,VspMessage.MSG_SESSION_ID_POS-leavedLen);			
			int vmLen = Tools.BytesToInt16BE(leavedBuff, VspMessage.MSG_LENGTH_POS);
			if ( vmLen < 0 || vmLen > VspMessage.MSG_MAX_LENGTH){
				Log.e(tag,"onReceive: vmLen is wrong![vmLen="+vmLen+"]");
				this.onRecvError();
				return;
			}				
			if ( leavedLen+recvLen < vmLen ){
				recvLen -= pullRecvData(recvData,recvLen);
				Log.d(tag,"onReceive: should wait for more bytes [vmLen="+vmLen
						+"][warntingLen="+(vmLen-leavedLen)+"]!");
				return;
			}
			recvLen -= pullRecvData(recvData,vmLen-leavedLen);
			if(vml!=null) {
				Log.d(tag,"OnReceive: ["+vmLen+"]" 
						+ Tools.Bin2HexString(leavedBuff, 0, vmLen));
				Tools.encode(leavedBuff, 8);
				VspMessage vm = VspMessage.parse(leavedBuff, vmLen);
				Log.d(tag,"OnReceive: ["+vmLen+"]" 
						+ Tools.Bin2HexString(leavedBuff, 0, vmLen));
				if ( vm != null)
					vml.onMessageReceived(vm);
			}
			leavedLen = 0;			
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
