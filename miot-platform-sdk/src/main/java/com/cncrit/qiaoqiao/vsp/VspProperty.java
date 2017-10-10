package com.cncrit.qiaoqiao.vsp;

import java.io.UnsupportedEncodingException;

import com.cncrit.qiaoqiao.Tools;

import android.util.Log;

public class VspProperty {
	public static final String tag = VspProperty.class.getName();

	public static final int PROP_TYPE_POS = 0;
	public static final int PROP_RESERVED_POS = 1;
	public static final int PROP_LENGTH_POS = 2;

	public static final int PROP_HEAD_LENGTH = 4;
	public static final int PROP_PACK_SIZE = 4;
	public static final int PROP_MAX_LENGTH = 2000;

	VspProperty() {
	}

	public void setLongValue(int idx, long value){
		System.out.println("VspDefine.getFieldPos(type, idx)="
				+ VspDefine.getFieldPos(type, idx));
		int startPos = this.propPos + VspDefine.getFieldPos(type, idx);
		Tools.LongToBytesBE(value, buff, startPos);
	}
	public void setIntValue(int idx, int value) {
		int startPos = this.propPos + VspDefine.getFieldPos(type, idx);
		System.out.println("VspDefine.getFieldPos(type, idx)="
				+ VspDefine.getFieldPos(type, idx));
		switch (VspDefine.getFieldLength(type, idx)) {
		case 1:
			this.buff[startPos] = (byte) (value % 256);
			break;
		case 2:
			Tools.Int16ToBytesBE(value, buff, startPos);
			break;
		case 4:
			System.out.println("value=" + value + "buff=" + buff + "startpos="
					+ startPos);
			Tools.Int32ToBytesBE(value, buff, startPos);
			break;
		case 0:
		default:
			Log.e(tag,
					"setIntValue: invalid fieldLength, should be one of (1,2,4) !");
			return;
		}
	}

	public void setStringValue(int idx, String value) {
		int startPos = this.propPos + VspDefine.getFieldPos(type, idx);
		int fieldLength = VspDefine.getFieldLength(type, idx);
		System.out.println("value.getBytes()=" + value.getBytes()
				+ "value.length()" + value.length() + "fieldLength="
				+ fieldLength);
		System.arraycopy(value.getBytes(), 0, buff, startPos, // System.arraycopy��Ҫѧϰ
				value.length() > fieldLength ? fieldLength : value.length());
	}

	public String getStringIpValue(int idx) {
		System.out.println("getIntValue here is why��type=" + type + "idx="
				+ idx);
		int startPos = this.propPos + VspDefine.getFieldPos(type, idx);

		System.out.println("startPos" + startPos + "this.propPos"
				+ this.propPos);

		int[] ns = new int[]{(int)this.buff[startPos+0]
				,(int)this.buff[startPos+1]
				,(int)this.buff[startPos+2]
				,(int)this.buff[startPos+3]};
		return ""+(ns[0]>=0?ns[0]:256+ns[0])+"."
				+(ns[1]>=0?ns[1]:256+ns[1])+"."
				+(ns[2]>=0?ns[2]:256+ns[2])+"."
				+(ns[3]>=0?ns[3]:256+ns[3]);
	}
	
	// return 0 if wrong!
	public int getIntValue(int idx) {
		System.out.println("getIntValue here is why��type=" + type + "idx="
				+ idx);
		int startPos = this.propPos + VspDefine.getFieldPos(type, idx);

		System.out.println("startPos" + startPos + "this.propPos"
				+ this.propPos);
		System.out.println("Tools.BytesToInt32BE(buff, startPos)="
				+ Tools.BytesToInt32BE(buff, startPos));
		System.out.println("VspDefine.getFieldLength(type, idx)="
				+ VspDefine.getFieldLength(type, idx));
		switch (VspDefine.getFieldLength(type, idx)) {
		case 1:
			return (int) (this.buff[startPos]);
		case 2:
			return Tools.BytesToInt16BE(buff, startPos);
		case 4:
			return Tools.BytesToInt32BE(buff, startPos);
		case 0:
		default:
			Log.e(tag,
					"getIntValue: invalid fieldLength, should be one of (1,2,4) !");
			return 0;
		}
	}

	public String getStringValue(int idx) {
		int startPos = this.propPos + VspDefine.getFieldPos(type, idx);
		int fieldLength = VspDefine.getFieldLength(type, idx);
		byte[] tmp = new byte[fieldLength];
		System.arraycopy(tmp, 0, buff, startPos, fieldLength);
		String str = "";
		try {
			str = new String(tmp, VspDefine.DEFAULT_CHARSET);
		} catch (Exception e) {
			Log.e(tag, "getStringValue: Exception Raised! " + e.getMessage());
			str = "";
		}
		return str;
	}

	public static VspProperty parse(byte[] recvBuff, int propPos,
			int propBuffLen) {
		Log.d(tag, "parse: " + recvBuff + "," + propPos + "," + propBuffLen);
		if (recvBuff == null) {
			Log.e(tag, "parse: recvBuff can not be null!");
			return null;
		}
		if (propBuffLen < PROP_HEAD_LENGTH) {
			Log.e(tag, "parse: propBuffLen[" + propBuffLen
					+ "] should large than " + PROP_HEAD_LENGTH);
			return null;
		}
		int len = Tools.BytesToInt16BE(recvBuff, propPos + PROP_LENGTH_POS);
		if (len < PROP_HEAD_LENGTH || len > PROP_MAX_LENGTH) {
			Log.e(tag, "parse: prop len[" + len + "] should large than "
					+ PROP_HEAD_LENGTH + " and " + PROP_MAX_LENGTH);
			return null;
		}
		VspProperty prop = new VspProperty();
		prop.setType((int) (recvBuff[propPos + PROP_TYPE_POS]));
		prop.setLength(len);
		prop.setBuff(recvBuff);
		prop.setPropPos(propPos);
		return prop;
	}

	public void setVariableValue(int idx, String value) {
		int startPos = this.propPos + VspDefine.getFieldPos(type, idx);
		int fieldLength = value.length();
		try {
			byte [] bs=value.getBytes("ISO-8859-1");
			if (this.setLength(length + fieldLength))
				System.arraycopy(bs, 0, buff, startPos,
						value.length());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getVariableValue(int idx) {
		int fieldLength = length - VspDefine.getPropLength(type);
		if (fieldLength > 0) {
			int startPos = this.propPos + VspDefine.getFieldPos(type, idx);
			byte[] tmp = new byte[fieldLength];
			System.arraycopy(buff, startPos, tmp, 0, fieldLength);
			Log.d(tag,
					"getVariableValue: [" + fieldLength + "]"
							+ Tools.Bin2HexString(tmp, 0, fieldLength));
			try {
				return new String(tmp, VspDefine.DEFAULT_CHARSET);
			} catch (Exception e) {
				Log.e(tag,
						"getVariableValue: Exception raised! " + e.getMessage());
				return "EXECPTION_RAISED";
			}
		} else
			return "EMPTY_VAR_VALUE";
	}

	// For the prop having var field, the length should be reset when setting
	// var field.
	// return the prop's length
	public int initial(byte[] buff, int type, int propPos) {
		System.out.println("VspDefine.getPropLength(type)="
				+ VspDefine.getPropLength(type));
		if (this.setBuff(buff) && this.setType(type)
				&& this.setPropPos(propPos)
				&& this.setLength(VspDefine.getPropLength(type)))
			return this.getLength();
		else
			return VspDefine.INVALID_LENGTH;
	}

	private byte[] buff = null;

	public byte[] getBuff() {
		return buff;
	}

	public boolean setBuff(byte[] buff) {
		if (buff == null) {
			Log.e(tag, "setBuff: buff should not be null!");
			return false;
		} else {
			this.buff = buff;
			return true;
		}
	}

	private int type = VspDefine.INVALID_ID;

	public int getType() {
		return type;
	}

	public boolean setType(int type) {
		if (type < 0 || type >= VspDefine.MAX_PROP_TYPE) {
			Log.e(tag, "setType: invalid type " + type
					+ " , should between 0 and " + VspDefine.MAX_PROP_TYPE);
			return false;
		} else {
			this.type = type;
			return true;
		}
	}

	private int propPos = 0;

	public int getPropPos() {
		return propPos;
	}

	public boolean setPropPos(int propPos) {
		if (propPos < VspMessage.MSG_HEAD_LENGTH
				|| propPos >= VspMessage.MSG_MAX_LENGTH) {
			Log.e(tag, "setPropPos: invalid propPos " + propPos
					+ ", should between " + VspMessage.MSG_HEAD_LENGTH
					+ " and " + VspMessage.MSG_MAX_LENGTH + "!");
			return false;
		} else if ((propPos % PROP_PACK_SIZE) != 0) {
			Log.e(tag, "setPropPos: invalid propPos " + propPos
					+ ", should be packed in " + PROP_PACK_SIZE + " bytes!");
			return false;
		} else {
			this.propPos = propPos;
			return true;
		}
	}

	private int length = 0;

	public int getLength() {
		return length;
	}

	public boolean setLength(int length) { // auto pack prop in PROP_PACK_SIZE
											// bytes
		if (length < PROP_HEAD_LENGTH || length >= PROP_MAX_LENGTH) {
			Log.e(tag, "setLength: invalid length " + length
					+ ", should between " + PROP_HEAD_LENGTH + " and "
					+ PROP_MAX_LENGTH + "!");
			return false;
		}
		if ((length % PROP_PACK_SIZE) != 0)
			length += (4 - (length % PROP_PACK_SIZE));
		this.length = length;
		return true;
	}

	public void encode() {
		buff[propPos + PROP_TYPE_POS] = (byte) type;
		buff[propPos + PROP_RESERVED_POS] = (byte) 0;
		Tools.Int16ToBytesBE(length, buff, propPos + PROP_LENGTH_POS);
	}
}
