package com.cncrit.qiaoqiao.vsp;



import com.cncrit.qiaoqiao.Tools;

public class VspCodec implements IReceiver {
	static public String tag = VspCodec.class.getName();
	private TcpSocket<VspCodec> ts = null;
	private String name = "unnamed_VspCodec";
	
	public interface IVspMessageListener {
		public boolean onMessageReceived(VspMessage vm);
		public void onRecvError();  
	}
	
	private IVspMessageListener vml = null;
	public boolean initial(String name, String ip, int port, IVspMessageListener vml){
		VspDefine.initial();
		this.vml = vml;
		this.setName(name);		
		ts = new TcpSocket<VspCodec>(this);
		if ( ts != null){

			return ts.connect(ip, port);
		}
		else{

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

			return false;
		}
		if(vm == null){

			return false;
		}
		vm.encode();
		byte[] s = vm.getBuff();

		Tools.encode(s, 8);
		return ts.send(vm.getBuff(),vm.getLength());
	}
	
	@Override
	public void onReceive(byte[] recvData, int recvLen) {		

		byte[] a = recvData;
		pos = 0;
		while(recvLen > 0){
			if(leavedLen+recvLen < VspMessage.MSG_SESSION_ID_POS) {
				recvLen -= pullRecvData(recvData,recvLen);

				return;
			}			
			if(leavedLen < VspMessage.MSG_SESSION_ID_POS) 
				recvLen -= pullRecvData(recvData,VspMessage.MSG_SESSION_ID_POS-leavedLen);			
			int vmLen = Tools.BytesToInt16BE(leavedBuff, VspMessage.MSG_LENGTH_POS);
			if ( vmLen < 0 || vmLen > VspMessage.MSG_MAX_LENGTH){

				this.onRecvError();
				return;
			}				
			if ( leavedLen+recvLen < vmLen ){
				recvLen -= pullRecvData(recvData,recvLen);

				return;
			}
			recvLen -= pullRecvData(recvData,vmLen-leavedLen);
			if(vml!=null) {

				Tools.encode(leavedBuff, 8);
				VspMessage vm = VspMessage.parse(leavedBuff, vmLen);

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
