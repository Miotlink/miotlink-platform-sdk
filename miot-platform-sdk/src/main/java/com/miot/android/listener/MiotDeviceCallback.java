package com.miot.android.listener;

public interface MiotDeviceCallback {
	
	/**
	 * 接受设备的数据
	 * @param pid 设备的ID
	 * @param uart 设备的串口数据
	 */
	public void reciverDevice(int pid, String uart);
	
	/**
	 * 用户强制登出
	 * 用户登出
	 */
	public void logout();
	
	/**
	 * 设备上下线通知
	 * @param pid 设备的
	 * @param State 在线状态 0 离线 ,1在线
	 * @param sequece 上线序列号 每次状态变化就会自增长
	 */
	public void online(String pid, String State, String sequece);

}
