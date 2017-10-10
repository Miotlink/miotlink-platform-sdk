package com.miot.android.listener;

public interface MmwDevice {
	
	public void reciverDevice(Integer pid, String uart);
	
	public void logout();
	
	public void online(String pid, String State, String sequece);

}
