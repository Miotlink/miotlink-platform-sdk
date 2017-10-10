package com.cncrit.qiaoqiao.vsp;

import com.cncrit.qiaoqiao.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class VspMessage {
	public static final String tag = VspMessage.class.getName();
	
	public static final int MSG_VERSION_POS = 0;
	
	public static final int MSG_CODE_POS = 1;
	
	public static final int MSG_LENGTH_POS = 2;
	
	public static final int MSG_SESSION_ID_POS = 4;
	
	public static final int MSG_HEAD_LENGTH = 8;
	
	public static final int MSG_MAX_LENGTH = 2048;
		
	public int getLength() {
		return length;
	}

	public boolean setLength(int length) {
		if(length<MSG_HEAD_LENGTH || length >=MSG_MAX_LENGTH) {

			return false;
		}
		this.length = length;
		return true;
	}

	public int getCode() {
		return code;
	}

	public boolean setCode(int code) {
		if(code<0 || code>=VspDefine.MAX_MSG_CODE) {

			return false;
		}
		this.code = code;
		return true;
	}

	public int getSesId() {
		return sesId;
	}

	public byte[] getBuff() {
		return buff;
	}

	private int version = VspDefine.VERSION;
	private int code = VspDefine.INVALID_ID;
	private int sesId = VspDefine.NONE_SES_ID;
	private int length = MSG_HEAD_LENGTH;
	private HashMap<Integer,VspProperty> props = new HashMap<Integer,VspProperty>();
	private List<VspProperty> propList = new ArrayList<VspProperty>();
	private boolean isVariableMsg = false;
	
	public boolean isVariableMsg() {
		return isVariableMsg;
	}

	public boolean setVariableMsg(boolean isVariableMsg) {
		if( isVariableMsg && this.isVariableMsg){

		}
		this.isVariableMsg = isVariableMsg;
		return true;
	}

	private byte []buff = new byte[MSG_MAX_LENGTH];
	
	public VspMessage(){
	}
	
	public VspMessage(int code, int sesId ) {
		this.setCode(code);
		this.sesId = sesId;		
	}
	
	public static VspMessage parse(byte []recvBuff, int vmLen){
		if(recvBuff == null) {

			return null;			
		}
		
		int code = (int)(recvBuff[ MSG_CODE_POS ]);
		int sesId = Tools.BytesToInt32BE(recvBuff, MSG_SESSION_ID_POS);
		VspMessage vm = new VspMessage(code,sesId);
		vm.copyBuff(recvBuff, vmLen);
		return vm;
	}
	
	public boolean copyBuff(byte[] recvBuff,int vmLen){
		System.arraycopy(recvBuff, 0, buff, 0, vmLen);
		this.version = (int)(recvBuff[MSG_VERSION_POS]);
		this.setLength(vmLen);
		int propPos = MSG_HEAD_LENGTH;
		int propBuffLen = vmLen - propPos;
		while(propBuffLen>0){
			VspProperty prop = VspProperty.parse(recvBuff, propPos, propBuffLen);
			if(prop==null)
				break;
			this.props.put(prop.getType(), prop);
			this.propList.add(prop);
			propBuffLen -= prop.getLength();
			propPos += prop.getLength();
		}
		return true;
	}
	
	public int getVersion() {
		return version;
	}

	public VspProperty getPropertyByIndex(int index){
		synchronized(this){
			return this.propList.get(index);
		}
	}
	
	public VspProperty getProperty(int type){
		synchronized(this){
			return this.props.get(type);
		}
	}
	
	// variable prop should be added finally.
	public VspProperty addProperty(int type){
		VspProperty prop = new VspProperty();
		System.out.println("buff="+buff+"type="+type+"length="+length);
		int propLength = prop.initial(buff, type, length);
		System.out.println("proplenth="+propLength);
		synchronized(this){
			if(this.isVariableMsg()){

				return null;
			}
			if (VspDefine.isVariableProp(type))
				if(!this.setVariableMsg(true))
					return null;		
			this.setLength(propLength+length);
			this.props.put(type, prop);
			this.propList.add(prop);
		}
		return prop;
	}
	
	public void encode(){
		length = MSG_HEAD_LENGTH;
//		Iterator<VspProperty> it = props.values().iterator();
		Iterator<VspProperty> it = propList.iterator();
		while(it.hasNext()) {
			VspProperty vp = it.next();
			vp.encode();
			int propLength = vp.getLength();
			this.setLength(length+propLength);
		}
			
		buff[MSG_VERSION_POS] = (byte)VspDefine.VERSION;
		buff[MSG_CODE_POS] = (byte)code;		
		Tools.Int16ToBytesBE(length, buff, MSG_LENGTH_POS); 
		Tools.Int32ToBytesBE(sesId, buff, MSG_SESSION_ID_POS); 
		
	}
}
