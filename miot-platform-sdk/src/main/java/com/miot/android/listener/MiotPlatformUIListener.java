package com.miot.android.listener;

public interface MiotPlatformUIListener {
	
	/**
	 * 设备返回数据
	 * @param cuId  用户id
	 * @param pid   设备id
	 * @param uart  设备返回的串口数据
	 */
	public void onReceiverDevice(String pid, String uart)throws Exception;
	/**
	 * 设备上线下状态
	 * @param pid 设备id
	 * @param State 上下线状态
	 * @param sequece 上下线顺序
	 */
	public void onReceiverDeviceState(String pid, String State, String sequece)throws Exception;
	
	/**
	 * 与平台断开连接,需要重新调用登录接口
	 * @param error
	 * @param message
	 * @param time
	 */
	 public void serverDisconnected(int error, String message, String time)throws Exception;
	/**
	 * 用户登出被别人登出
	 * @param id
	 */
	 public void onLogot(String id)throws Exception;

}
