package com.cncrit.qiaoqiao;

import android.content.Context;

import com.cncrit.qiaoqiao.vsp.VspCodec;
import com.cncrit.qiaoqiao.vsp.VspDefine;
import com.cncrit.qiaoqiao.vsp.VspMessage;
import com.cncrit.qiaoqiao.vsp.VspProperty;
import com.miot.android.Service;
import com.miot.android.content.NoFormatConsts;

import java.io.File;

public class VspOperation {
	public static String tag = VspOperation.class.getName();
	public static int CU_VERSION_ID = 21 * 256 * 256 * 256 + 1 * 256 * 256;
	public static String userName = "";
	public static String password = "";
	public static int cuId = 0;
	public static int sessionId = VspDefine.NONE_SES_ID;
	private static int appCuId = 0;
	public static int userId = 0;
	public static String nickName = "";
	public static int selectedPuId = 0;
	public static String rsIp = "118.190.61.214";
	public static int rsPort = 28001;
	protected static String cmsIp = "";   
	protected static int cmsPort = 28101;
	
	public static int loginFailCode=2002;
	public static String loginFailErrorMess="获取服务器地址失败";
	public static boolean HasAppLogin() {
		return (userId != 0 && appCuId != 0);
	}

	public static boolean HasLogin(Context c) {
		boolean bLogin = (cuId != 0);
		if (c != null && !bLogin) {
		
			Tools.AlertError("请先登录系统！", c);
		}
		return bLogin;
	}


	public static boolean HasLogin() {
		return HasLogin(null);
	}

	protected static boolean HasGotCmsAddress() {

		return cmsVc!=null && cmsVc.hasConnect();
	}

	public static void Reset() {
	
		cuId = 0;
		sessionId = VspDefine.NONE_SES_ID;
		appCuId = 0;
		nickName = "";
		userName = "";
		password = "";
		selectedPuId = 0;

		cmsIp = "";
		userId = 0;

		if (rsVc != null) {
			
			rsVc.destroy();
			rsVc = null;
		}
		if (cmsVc != null) {
			cmsVc.destroy();
			cmsVc = null;
			idleCount = 0;
		}
	}

	private static String RS_VC_NAME = "rs";
	private static VspCodec rsVc = null;
	private static String CMS_VC_NAME = "cms";
	private static VspCodec cmsVc = null;
	private static int WAIT_VSP_RESPONSE_INTEVAL = 40; // ms
	private static int WAIT_VSP_RESPONSE_COUNT = 250; // 200x20ms = 4s

	private static Thread heartbeatThread = null;
	private static int HEARTBEAT_INTEVAL = 30000; // 30s
	public  static int idleCount = 0;
	private static int IDLE_TIMEOUT_COUNT =4; // IDLE_TIMEOUT_COUNT x
		
	private static boolean isLogout=true;// HEARTBEAT_INTEVAL

	private static void doHeartbeat() {
		int serial = 0;
		while (true) {
			try {
				Thread.sleep(HEARTBEAT_INTEVAL);
				if (HasLogin() && cmsVc != null) {
					if ((++idleCount) > IDLE_TIMEOUT_COUNT) {
						
						if (isLogout) {
							Service.sendBroadcast("com.miot.android.MIOT_PLATFORM_RECONNECTED", new String []{"1",""});
						}
						Logout(null);
						continue;
					}

					
					VspMessage vm = new VspMessage(VspDefine.codeHeartBeat,
							sessionId);
					VspProperty vp = vm.addProperty(VspDefine.propHeartBeat);

					vp.setIntValue(VspDefine.HeartBeat_serial_idx, serial++);
					vp.setIntValue(VspDefine.HeartBeat_actionTime_idx,
							Tools.GetTickTime());
					cmsVc.send(vm);
					
							
				} else {
					
				}
			} catch (Exception e) {
				
			}

		}
	}

	private static Thread advFilesThread = null;
	

	private static void doGetAdvFiles() {
		try {
			String advDir = "";
			File dir = new File(advDir);
			if (!dir.exists())
				dir.mkdirs();
			for (String row : advList.split(VspDefine.LIST_DATA_ROW_SEPARATOR)) {
			
				if (row.equals(""))
					break;
				String[] columns = row
						.split(VspDefine.LIST_DATA_COLUMN_SEPARATOR);
				if (columns.length < 4) {
				
					continue;
				}
				int vaType = Integer.parseInt(columns[0]);
				String vaPlayPeriod = columns[1];
				String vaPath = columns[2];
				String vaRelationUrl = columns[3];
				String fileName = vaPath.substring(vaPath.lastIndexOf("/") + 1);
				boolean bRet = Tools.GetFileByHttp(vaPath, advDir, fileName,
						false);
			
				if (bRet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}

	public static boolean StartHeartbeat() {
	
		if (heartbeatThread == null) {
			heartbeatThread = new Thread(new Runnable() {
				@Override
				public void run() {
					VspOperation.doHeartbeat();
				}

			});
			heartbeatThread.start();
		}
		return true;
	}

	private static boolean stopVspRequestWaiting = false;
	private static int vspCommonResReqcode = 0;
	private static int vspCommonResResult = 0;
	private static String vspCommonResReason = "";

	private static void showCommonResInfo(int reqcode) {
		if (stopVspRequestWaiting && reqcode == vspCommonResReqcode) {
			switch (vspCommonResReqcode) {
			case VspDefine.codeGetCMSRoute:
			case VspDefine.codeAppCuLogin:
			case VspDefine.codeLogin:
				updateLoginActTitle("[" + vspCommonResReqcode + ","
						+ vspCommonResResult + "] " + vspCommonResReason);
				break;
			case VspDefine.codeGetPuList:
				break;
			default:
			
			}
		}
	}

	private static boolean procCommonRes(VspMessage vm) {
		VspProperty vp = vm.getProperty(VspDefine.propCommonRes);
		int vspCommonResReqcode = vp
				.getIntValue(VspDefine.CommonRes_reqCode_idx);
		int vspCommonResResult = vp.getIntValue(VspDefine.CommonRes_result_idx);
		String vspCommonResReason = vp
				.getVariableValue(VspDefine.CommonRes_reason_idx);

		
		if(vspCommonResReqcode==VspDefine.codeLogin){
			VspOperation.loginFailCode=2001;
			VspOperation.loginFailErrorMess="用户名或密码错误";
		}
		if(vspCommonResReqcode==27){
			VspOperation.loginFailCode=2002;
			VspOperation.loginFailErrorMess="获取服务器地址失败";
		}
		stopVspRequestWaiting = true;
		return true;
	}

	private static boolean procGetCMSRouteRes(VspMessage vm) {
		VspProperty vp = vm.getProperty(VspDefine.propSockAddr);
		int a = vp.getIntValue(VspDefine.SockAddr_ip_idx);
		cmsIp = vp.getStringIpValue(VspDefine.SockAddr_ip_idx);
		cmsPort = vp.getIntValue(VspDefine.SockAddr_port_idx);
		
		if (cmsVc != null)
			cmsVc.destroy();
		
		VspCodec vc = new VspCodec();
		updateLoginActTitle(cmsIp);

		if (!vc.initial(CMS_VC_NAME, cmsIp, cmsPort, cmsVml)) {

			return false;
		}
		updateLoginActTitle("获取CMS地址完毕!");

		cmsVc = vc;
		return true;
	}

	private static boolean procVersionInfo(VspMessage vm) {
	
		return true;
	}

	private static VspCodec.IVspMessageListener rsVml = new VspCodec.IVspMessageListener() {
		private String tag = "rsVml";

		@Override
		public boolean onMessageReceived(VspMessage vm) {
			// Log.d(tag,"vm [code="+vm.getCode()+"] comes in!");
			switch (vm.getCode()) {
			case VspDefine.codeCommonRes:
				return procCommonRes(vm);
			case VspDefine.codeGetCMSRouteRes:
				return procGetCMSRouteRes(vm);
			case VspDefine.codeVersionInfo:
				return procVersionInfo(vm);
			default:
				
			}
			return false;
		}

		@Override
		public void onRecvError() {
		
			// Reset();
		}
	};
	private static int domId;
	private static int puId;

	protected static boolean GetCmsAddress(Context c) {

		/*
		 * 表示如果CU与RS，RS与CU已经连上， CMS已经成功返回给CU端IP了，
		 * 那么就直接返回。否则RS和CU进行连接，RS控制CMS，返回IP
		 */
		if (HasGotCmsAddress())
			return true;

		if (rsVc != null)
			rsVc.destroy(); // should do this to colse ts's connection
		rsVc = new VspCodec();


		if (!rsVc.initial(RS_VC_NAME, rsIp, rsPort, rsVml))
			return false;
		VspMessage vm = new VspMessage(VspDefine.codeGetCMSRoute,
				VspDefine.NONE_SES_ID);
		
		VspProperty vp = vm.addProperty(VspDefine.propSockAddr);
		vp.setLongValue(VspDefine.SockAddr_ip_idx,Tools.String2LongIp(rsIp));
		vp.setIntValue(VspDefine.SockAddr_port_idx, 0);
		vp.setIntValue(VspDefine.SockAddr_protocal_idx, 0);
		if (!rsVc.send(vm))
			return false;

		int waitCount = 0;
		stopVspRequestWaiting = false;
		while (!HasGotCmsAddress() && !stopVspRequestWaiting) {
			try {
				// Tools.AlertError("QQ:szf---GetCmsAddress-5", c);
				Thread.sleep(WAIT_VSP_RESPONSE_INTEVAL);
				updateLoginActTitle("等待获取CMS地址 "
						+ (WAIT_VSP_RESPONSE_COUNT - waitCount));
				if (waitCount++ > WAIT_VSP_RESPONSE_COUNT) {
					
					return false;
				}
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
		}
		showCommonResInfo(VspDefine.codeGetCMSRoute);
	
		return HasGotCmsAddress();
	}

	private static boolean procAppCuLogin(VspMessage vm) {
		VspProperty vp = vm.getProperty(VspDefine.propAppCuInfo);
		appCuId = vp.getIntValue(VspDefine.AppCuInfo_cuId_idx);
		nickName = vp.getStringValue(VspDefine.AppCuInfo_nickName_idx);
		userId = vp.getIntValue(VspDefine.AppCuInfo_userId_idx);
		
		return true;
	}

	private static boolean procLoginRes(VspMessage vm) {
		for ( int i=0;i<3;i++){
			VspProperty vp = vm.getPropertyByIndex(i);
			int idType = vp.getIntValue(VspDefine.Id_type_idx);
			switch (idType ){
			case 0:
				VspOperation.puId = vp.getIntValue(VspDefine.Id_id_idx);
				break;
			case 1:
				VspOperation.cuId = vp.getIntValue(VspDefine.Id_id_idx);
				break;
			case 2:
				VspOperation.sessionId = vp.getIntValue(VspDefine.Id_id_idx);
				break;
			case 3:
				VspOperation.domId = vp.getIntValue(VspDefine.Id_id_idx);
				break;
			default:
			
			}
		}
		VspProperty vp = vm.getPropertyByIndex(3);
		VspOperation.HEARTBEAT_INTEVAL = vp.getIntValue(VspDefine.Timeout_interval_idx)*1000;
		VspOperation.IDLE_TIMEOUT_COUNT = vp.getIntValue(VspDefine.Timeout_timeout_idx)/vp.getIntValue(VspDefine.Timeout_interval_idx);
		

		return true;
	}


	private static int advUpdateTime = 0;
	private static String advList = "";

	private static boolean procGetAdvList(VspMessage vm) {
	

		VspProperty vp1 = vm.getProperty(VspDefine.propAdvInfo);
		advUpdateTime = vp1.getIntValue(VspDefine.AdvInfo_advUpdateTime_idx);
		VspProperty vp = vm.getProperty(VspDefine.propList);
		advList = vp.getVariableValue(VspDefine.List_data_idx);
		if (!advList.equals("")) {
			if (advFilesThread != null) {
				advFilesThread.stop();
				advFilesThread = null;
			}
			advFilesThread = new Thread(new Runnable() {
				@Override
				public void run() {
					VspOperation.doGetAdvFiles();
				}

			});
			advFilesThread.start();
		}
		return true;
	}

	private static boolean doRaiseAlarm(String xmlAlarm) {
	
		xmlAlarm = xmlAlarm.toLowerCase();
		String puId = Tools.ParseXmlElement(xmlAlarm, "puid");
		if (puId.equals("")) {
		
			return false;
		}
		int nPuId = Integer.parseInt(puId);
		if (nPuId != selectedPuId) {
		
			return false;
		}
		int at = Integer.parseInt(Tools.ParseXmlElement(xmlAlarm, "at"));
		String timestamp = Tools.ParseXmlElement(xmlAlarm, "tt");
		if (timestamp.equals("")) {
		
			return false;
		}
		String eliminated = Tools.ParseXmlElement(xmlAlarm, "as");
		if (eliminated.equals("")) {
		
			return false;
		}
		return true;
	}


	private static VspCodec.IVspMessageListener cmsVml = new VspCodec.IVspMessageListener() {
		private String tag = "cmsVml";

		@Override
		public boolean onMessageReceived(VspMessage vm) {
			int vmCode = vm.getCode();
		
			//心跳包的判断
			if (vmCode == VspDefine.codeHeartBeatRes ){
				VspOperation.idleCount = 0;
			}

			switch (vmCode) {
			case VspDefine.codeCommonRes:
				return procCommonRes(vm);
			case VspDefine.codeLoginRes:
				return procLoginRes(vm);
			case VspDefine.codeGetPuList:
//				return procGetPuList(vm);
			case VspDefine.codeHeartBeatRes:
			
				return true;
			case VspDefine.codeGetAdvList:
				return procGetAdvList(vm);
			case VspDefine.codeTTBinary:
				return procTTBinary(vm);
			case VspDefine.codeEnforceLogout:
				return procEnforceLogout(vm);
			case VspDefine.codePuState:
				return procPuState(vm);
			default:
			
			}
			return false;
		}
		/**
		 * 自动上下线 通知  
		 * 发送广播的参数 是MIOT_PU_STATED_CHANAGED
		 * 数组 数据 PuID 上下线状态 0或1 通知的时间戳
		 * @param vm
		 * @return
		 */
		private boolean procPuState(VspMessage vm) {
			VspProperty vpPuId = vm.getPropertyByIndex(0);
			String s = "PuStateOnLine: {"+
					vpPuId.getIntValue(VspDefine.Id_type_idx)+","+
					vpPuId.getIntValue(VspDefine.Id_id_idx)+"}";

			int puId = vpPuId.getIntValue(VspDefine.Id_id_idx);
			VspProperty vpOnline=vm.getPropertyByIndex(1);
			byte[] onlineStateBytes = new byte[4];
			byte[] seqBytes = new byte[4];
			System.arraycopy(vpOnline.getBuff(), vpOnline.getPropPos()+4, onlineStateBytes, 0, 4);
			System.arraycopy(vpOnline.getBuff(), vpOnline.getPropPos()+4+4, seqBytes, 0, 4);
			int puState = Tools.toInt(onlineStateBytes,0);
			int notifySequece = Tools.toInt(seqBytes,0);
			Service.sendBroadcast(NoFormatConsts.MIOT_PU_STATED_CHANAGED
					, new String[]{""+puId,puState+"",notifySequece+""});
			return true;
		}
		private boolean procEnforceLogout(VspMessage vm) {
			VspProperty vp = vm.getPropertyByIndex(0);
			int id = vp.getIntValue(VspDefine.Id_id_idx);
			Service.sendBroadcast(NoFormatConsts.NOTICE_LOGIN_OUT
					, new String[]{""+id,""});
			isLogout=false;
			return true;
		}

		private boolean procTTBinary(VspMessage vm) {
			VspProperty vp = vm.getPropertyByIndex(0);
			String s = "TTBinary: {"+
				vp.getIntValue(VspDefine.Id_type_idx)+","+
				vp.getIntValue(VspDefine.Id_id_idx)+"}";
			int puId = vp.getIntValue(VspDefine.Id_id_idx);
			vp = vm.getPropertyByIndex(1);
			s += ",{"+
				vp.getIntValue(VspDefine.Id_type_idx)+","+
				vp.getIntValue(VspDefine.Id_id_idx)+"}";
			vp = vm.getProperty(VspDefine.propTTContent);
			String content = vp.getVariableValue(VspDefine.TTContent_data_idx);
			s += ",{"+
				vp.getIntValue(VspDefine.TTContent_type_idx)+","+
				vp.getIntValue(VspDefine.TTContent_userTag_idx)+","+
				content+"}";
		
			Service.sendBroadcast(NoFormatConsts.MOIT_PU_BROADCAST_MSG_ACTION
				, new String[]{""+puId,content});
			return true;
		}

		@Override
		public void onRecvError() {
		}
	};

	protected static boolean AppCuLogin(Context c, String userName,
			String password) {
		if (!HasGotCmsAddress()) {
		
			return false;
		}
		updateLoginActTitle("获取CMS地址完毕-1!");
		if (cmsVc != null)
			cmsVc.destroy();
		cmsVc = new VspCodec();
		cmsIp = "192.168.10.83";
		updateLoginActTitle(cmsIp);

		if (!cmsVc.initial(CMS_VC_NAME, cmsIp, cmsPort, cmsVml)) {

			return false;
		}
		updateLoginActTitle("获取CMS地址完毕-3!");
		VspMessage vm = new VspMessage(VspDefine.codeAppCuLogin,
				VspDefine.NONE_SES_ID);
		VspProperty vp = vm.addProperty(VspDefine.propAppCuInfo);
		vp.setIntValue(VspDefine.AppCuInfo_appId_idx, VspDefine.APP_ID);
		vp.setStringValue(VspDefine.AppCuInfo_loginName_idx, userName);
		vp.setStringValue(VspDefine.AppCuInfo_password_idx, password);
		if (!cmsVc.send(vm))
			return false;

		int waitCount = 0;
		stopVspRequestWaiting = false;
		while (!HasAppLogin() && !stopVspRequestWaiting) {
			try {
				Thread.sleep(WAIT_VSP_RESPONSE_INTEVAL);
				updateLoginActTitle("等待应用登录 "
						+ (WAIT_VSP_RESPONSE_COUNT - waitCount));
				if (waitCount++ > WAIT_VSP_RESPONSE_COUNT) {
				
					return false;
				}
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
		}
		
		showCommonResInfo(VspDefine.codeAppCuLogin);
		

		return HasAppLogin();
	}

	protected static boolean CuLogin(Context c,String userName, String password) {
		if (cmsVc == null) {
		
			return false;
		}
		VspMessage vm = new VspMessage(VspDefine.codeLogin,
				VspDefine.NONE_SES_ID);
		VspProperty vp = vm.addProperty(VspDefine.propLoginKey);
		vp.setIntValue(VspDefine.LoginKey_type_idx, 1);
		vp.setStringValue(VspDefine.LoginKey_userName_idx,userName);
		vp.setStringValue(VspDefine.LoginKey_password_idx,password);
		VspProperty vp1 = vm.addProperty(VspDefine.propUserData);
		vp1.setVariableValue(VspDefine.UserData_data_idx, "hello,world!");
		if (!cmsVc.send(vm))
			return false;
		int waitCount = 0;
		stopVspRequestWaiting = false;
		while (!HasLogin() && !stopVspRequestWaiting) {
			try {
				Thread.sleep(WAIT_VSP_RESPONSE_INTEVAL);
				updateLoginActTitle("等待交换平台登录 "
						+ (WAIT_VSP_RESPONSE_COUNT - waitCount));
				if (waitCount++ > WAIT_VSP_RESPONSE_COUNT) {
				
					return false;
				}
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
		}
		showCommonResInfo(VspDefine.codeLogin);
	
		return HasLogin();
	}

	private static void updateLoginActTitle(String title) {
	
	}

	static String lastUserName = "";
	static String lastUserPassword="";
	public static boolean Login(String userName, String password) {
		lastUserName=userName;
		lastUserPassword=password;
		Context c = null;		
		Reset();
		updateLoginActTitle("清理完毕!");
		if (GetCmsAddress(c)) {
			updateLoginActTitle("获取CMS地址完毕!");
			if (CuLogin(c,userName,password)) {
				updateLoginActTitle("登录成功!");
				VspOperation.userName = userName;
				lastUserName=userName;
				lastUserPassword=password;
				VspOperation.password = password;
				StartHeartbeat();
				isLogout=true;
				return true;
			}
		}
	
		Reset();
		return false;
	}
	
	static boolean logout=false;
	
	static int send=0;
	
	public static boolean toPu(int puId, String content, int type, int userTag ) {
		
		if (cmsVc == null) {
			if (!isLogout) {
				if (send<1) {
					Service.sendBroadcast("com.miot.android.MIOT_PLATFORM_RECONNECTED", new String []{"1",""});
					send++;
				}
			}
			return false;
		}
		send=0;
		VspMessage vm = new VspMessage(VspDefine.codeTTBinary, sessionId);
		VspProperty vp = vm.addProperty(VspDefine.propId);
		vp.setIntValue(VspDefine.Id_type_idx, 1);
		vp.setIntValue(VspDefine.Id_id_idx, cuId);
		vp = vm.addProperty(VspDefine.propId);
		vp.setIntValue(VspDefine.Id_type_idx, 0);
		vp.setIntValue(VspDefine.Id_id_idx, puId);
		vp = vm.addProperty(VspDefine.propTTContent);
		vp.setIntValue(VspDefine.TTContent_type_idx, type);
		vp.setIntValue(VspDefine.TTContent_userTag_idx, userTag);
		vp.setVariableValue(VspDefine.TTContent_data_idx, content);
		cmsVc.send(vm);
		return true;
	}
	public static boolean toCu(int puId, String content, int type, int userTag ) {
	
		if (cmsVc == null) {
			if (!isLogout) {
				if (send<1) {
					Service.sendBroadcast("com.miot.android.MIOT_PLATFORM_RECONNECTED", new String []{"1",""});
					send++;
				}
			}
			return false;
		}
		send=0;
		VspMessage vm = new VspMessage(VspDefine.codeTTBinary, sessionId);
		VspProperty vp = vm.addProperty(VspDefine.propId);
		vp.setIntValue(VspDefine.Id_type_idx, 1);
		vp.setIntValue(VspDefine.Id_id_idx, cuId);
		vp = vm.addProperty(VspDefine.propId);
		vp.setIntValue(VspDefine.Id_type_idx, 1);
		vp.setIntValue(VspDefine.Id_id_idx, puId);
		vp = vm.addProperty(VspDefine.propTTContent);
		vp.setIntValue(VspDefine.TTContent_type_idx, type);
		vp.setIntValue(VspDefine.TTContent_userTag_idx, userTag);
		vp.setVariableValue(VspDefine.TTContent_data_idx, content);
		cmsVc.send(vm);
		return true;
	}
	

	public static void Logout(Context c) {
	
		if (cmsVc == null) {
		
			return;
		}
		isLogout=false;
		VspMessage vm = new VspMessage(VspDefine.codeLogout, sessionId);
		VspProperty vp = vm.addProperty(VspDefine.propId);
		vp.setIntValue(VspDefine.Id_type_idx, 1);
		vp.setIntValue(VspDefine.Id_id_idx, cuId);
		cmsVc.send(vm);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Reset();
	}

	public static boolean RefreshCameras(Context c) {
		if (!HasLogin(c)) {
		
			return false;
		}
		if (cmsVc == null) {
			
			return false;
		}
		VspMessage vm = new VspMessage(VspDefine.codeGetPuList, sessionId);
		VspProperty vp = vm.addProperty(VspDefine.propCuId);
		vp.setIntValue(VspDefine.CuId_cuId_idx, cuId);
		VspProperty vp1 = vm.addProperty(VspDefine.propList);
		vp1.setIntValue(VspDefine.List_startRowNum_idx, 0);
		vp1.setIntValue(VspDefine.List_endRowNum_idx, 50);
		return cmsVc.send(vm);
	}

	public static boolean AutoLogin(Context c) {
	
		if (HasLogin())
			return true;
		String sAutoLogin = Tools.GetProfileString("login", "autoLogin",
				"false");
		if (sAutoLogin.toLowerCase().equals("false"))
			return false;
		String sUserName = Tools.GetProfileString("login", "userName", "");
		if (sUserName.equals(""))
			return false;
		String sPassword = Tools.GetProfileString("login", "password", "");
		if (sPassword.equals(""))
			return false;
		return VspOperation.Login( sUserName, sPassword);
	}

	public static boolean GetAdvList() {
	
		if (cmsVc == null) {
		
			return false;
		}
		VspMessage vm = new VspMessage(VspDefine.codeGetAdvList,
				VspDefine.NONE_SES_ID);
		return cmsVc.send(vm);
	}
}
