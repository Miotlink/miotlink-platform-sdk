package com.cncrit.qiaoqiao.vsp;

public class VspDefine {
	public static final int VERSION = 0x30;
	public static final int APP_ID = 1;
	public static final String DEFAULT_CHARSET = "ISO-8859-1";
	public static final int INVALID_ID = -1;
	public static final int INVALID_LENGTH = -1;
	public static final int NONE_SES_ID = 0;
	public static final int MAX_MSG_CODE = 256;
	public static final int MAX_PROP_TYPE = 256;
	public static final String LIST_DATA_ROW_SEPARATOR = "~";
	public static final String LIST_DATA_COLUMN_SEPARATOR = "`";
	
	private static final int aPropFieldLength[][] = new int[MAX_PROP_TYPE][];
	private static final int aPropFieldPos[][] = new int[MAX_PROP_TYPE][];
	private static final int aPropLength[] = new int[MAX_PROP_TYPE];
	private static final boolean aPropIsHasVariable[] = new boolean[MAX_PROP_TYPE];
	
	public static final int getFieldCount(int type){
		assert(type>0 && type<MAX_PROP_TYPE);
		return aPropFieldLength[type].length;
	}
	public static final int getFieldLength(int type, int idx){
		assert(type>0 && type<MAX_PROP_TYPE);
		return aPropFieldLength[type][idx];
	}
	public static final int getPropLength(int type){
		assert(type>0 && type<MAX_PROP_TYPE);
		return aPropLength[type];
	}
	// include prop head's length
	public static final int getFieldPos(int type, int idx){
		assert(type>0 && type<MAX_PROP_TYPE);
		return aPropFieldPos[type][idx];
	}
	public static final boolean isVariableProp(int type){
		assert(type>0 && type<MAX_PROP_TYPE);
		return aPropIsHasVariable[type];
	}
	public static final boolean isVariableField(int type, int idx){
		assert(type>0 && type<MAX_PROP_TYPE);
		return aPropFieldLength[type][idx] == 0;
	}
	
	private static boolean hasBeenInitialized = false;	
	public static final void initial() {
		synchronized (VspDefine.class) {
			if (!hasBeenInitialized) {
				initialaPropFieldLength();
								
				for(int i=0;i<MAX_PROP_TYPE;i++){
					int []fields = aPropFieldLength[i];					
					if(fields != null){
						aPropFieldPos[i] = new int[fields.length];
						aPropLength[i] = VspProperty.PROP_HEAD_LENGTH;
						if(fields.length > 0 ){
							int j;
							for (j=0;j<fields.length;j++) {
								aPropFieldPos[i][j] = aPropLength[i]; // include prop head length  
								aPropLength[i] += fields[j];
							}
							aPropIsHasVariable[i] = (fields[j-1]==0);						
						} else {
							aPropIsHasVariable[i] = false;
						}
					} else {
						aPropFieldLength[i] = new int[0];
						aPropFieldPos[i] = new int[0];
						aPropLength[i] = 0;
						aPropIsHasVariable[i] = false;
					}
				}
				hasBeenInitialized = true;
			}
		}
	}

	private static final void initialaPropFieldLength(){
		aPropFieldLength[propDataAck] = new int[6];
		aPropFieldLength[propDataAck][0] = 1;
		aPropFieldLength[propDataAck][1] = 1;
		aPropFieldLength[propDataAck][2] = 1;
		aPropFieldLength[propDataAck][3] = 1;
		aPropFieldLength[propDataAck][4] = 4;
		aPropFieldLength[propDataAck][5] = 4;
		
		aPropFieldLength[propCommonRes] = new int[3];
		aPropFieldLength[propCommonRes][0] = 2;
		aPropFieldLength[propCommonRes][1] = 2;
		aPropFieldLength[propCommonRes][2] = 0;

		aPropFieldLength[propLoginKey] = new int[3];
		aPropFieldLength[propLoginKey][0] = 4;
		aPropFieldLength[propLoginKey][1] = 48;
		aPropFieldLength[propLoginKey][2] = 48;

		aPropFieldLength[propSessionInfo] = new int[4];
		aPropFieldLength[propSessionInfo][0] = 4;
		aPropFieldLength[propSessionInfo][1] = 4;
		aPropFieldLength[propSessionInfo][2] = 4;
		aPropFieldLength[propSessionInfo][3] = 4;

		aPropFieldLength[propLogout] = new int[2];
		aPropFieldLength[propLogout][0] = 4;
		aPropFieldLength[propLogout][1] = 4;

		aPropFieldLength[propHeartBeat] = new int[2];
		aPropFieldLength[propHeartBeat][0] = 4;
		aPropFieldLength[propHeartBeat][1] = 4;

		aPropFieldLength[propSockAddr] = new int[3];
		aPropFieldLength[propSockAddr][0] = 4;
		aPropFieldLength[propSockAddr][1] = 2;
		aPropFieldLength[propSockAddr][2] = 2;

		aPropFieldLength[propPuId] = new int[1];
		aPropFieldLength[propPuId][0] = 4;

		aPropFieldLength[propCuId] = new int[1];
		aPropFieldLength[propCuId][0] = 4;

		aPropFieldLength[propTokenModeToken] = new int[1];
		aPropFieldLength[propTokenModeToken][0] = 4;

		aPropFieldLength[propConsumeInfo] = new int[2];
		aPropFieldLength[propConsumeInfo][0] = 4;
		aPropFieldLength[propConsumeInfo][1] = 4;

		aPropFieldLength[propCommonReason] = new int[2];
		aPropFieldLength[propCommonReason][0] = 4;
		aPropFieldLength[propCommonReason][1] = 0;

		aPropFieldLength[propPuChannelId] = new int[2];
		aPropFieldLength[propPuChannelId][0] = 4;
		aPropFieldLength[propPuChannelId][1] = 4;

		aPropFieldLength[propHoleFlag] = new int[1];
		aPropFieldLength[propHoleFlag][0] = 4;

		aPropFieldLength[propHoleAction] = new int[2];
		aPropFieldLength[propHoleAction][0] = 4;
		aPropFieldLength[propHoleAction][1] = 4;

		aPropFieldLength[propPassword] = new int[1];
		aPropFieldLength[propPassword][0] = 8;

		aPropFieldLength[propHoleToken] = new int[1];
		aPropFieldLength[propHoleToken][0] = 4;

		aPropFieldLength[propConToken] = new int[1];
		aPropFieldLength[propConToken][0] = 4;

		aPropFieldLength[propTTContent] = new int[3];
		aPropFieldLength[propTTContent][0] = 4;
		aPropFieldLength[propTTContent][1] = 4;
		aPropFieldLength[propTTContent][2] = 0;

		aPropFieldLength[propPuLog] = new int[7];
		aPropFieldLength[propPuLog][0] = 2;
		aPropFieldLength[propPuLog][1] = 1;
		aPropFieldLength[propPuLog][2] = 1;
		aPropFieldLength[propPuLog][3] = 4;
		aPropFieldLength[propPuLog][4] = 4;
		aPropFieldLength[propPuLog][5] = 4;
		aPropFieldLength[propPuLog][6] = 0;

		aPropFieldLength[propPuAlarm] = new int[6];
		aPropFieldLength[propPuAlarm][0] = 16;
		aPropFieldLength[propPuAlarm][1] = 1;
		aPropFieldLength[propPuAlarm][2] = 1;
		aPropFieldLength[propPuAlarm][3] = 1;
		aPropFieldLength[propPuAlarm][4] = 1;
		aPropFieldLength[propPuAlarm][5] = 4;

		aPropFieldLength[propNetRecordParam] = new int[4];
		aPropFieldLength[propNetRecordParam][0] = 2;
		aPropFieldLength[propNetRecordParam][1] = 2;
		aPropFieldLength[propNetRecordParam][2] = 32;
		aPropFieldLength[propNetRecordParam][3] = 0;

		aPropFieldLength[propNetPictureParam] = new int[4];
		aPropFieldLength[propNetPictureParam][0] = 2;
		aPropFieldLength[propNetPictureParam][1] = 2;
		aPropFieldLength[propNetPictureParam][2] = 64;
		aPropFieldLength[propNetPictureParam][3] = 0;

		aPropFieldLength[propFtpUrl] = new int[1];
		aPropFieldLength[propFtpUrl][0] = 64;

		aPropFieldLength[propPuPictureParam] = new int[3];
		aPropFieldLength[propPuPictureParam][0] = 4;
		aPropFieldLength[propPuPictureParam][1] = 32;
		aPropFieldLength[propPuPictureParam][2] = 0;

		aPropFieldLength[propPuRecordParam] = new int[5];
		aPropFieldLength[propPuRecordParam][0] = 4;
		aPropFieldLength[propPuRecordParam][1] = 4;
		aPropFieldLength[propPuRecordParam][2] = 32;
		aPropFieldLength[propPuRecordParam][3] = 32;
		aPropFieldLength[propPuRecordParam][4] = 0;

		aPropFieldLength[propAlarmConfigParam] = new int[10];
		aPropFieldLength[propAlarmConfigParam][0] = 4;
		aPropFieldLength[propAlarmConfigParam][1] = 4;
		aPropFieldLength[propAlarmConfigParam][2] = 4;
		aPropFieldLength[propAlarmConfigParam][3] = 4;
		aPropFieldLength[propAlarmConfigParam][4] = 4;
		aPropFieldLength[propAlarmConfigParam][5] = 4;
		aPropFieldLength[propAlarmConfigParam][6] = 4;
		aPropFieldLength[propAlarmConfigParam][7] = 4;
		aPropFieldLength[propAlarmConfigParam][8] = 20;
		aPropFieldLength[propAlarmConfigParam][9] = 0;

		aPropFieldLength[propDirectConnectInfo] = new int[4];
		aPropFieldLength[propDirectConnectInfo][0] = 1;
		aPropFieldLength[propDirectConnectInfo][1] = 1;
		aPropFieldLength[propDirectConnectInfo][2] = 2;
		aPropFieldLength[propDirectConnectInfo][3] = 4;

		aPropFieldLength[propVersionInfo] = new int[4];
		aPropFieldLength[propVersionInfo][0] = 4;
		aPropFieldLength[propVersionInfo][1] = 1;
		aPropFieldLength[propVersionInfo][2] = 1;
		aPropFieldLength[propVersionInfo][3] = 2;

		aPropFieldLength[propUpdateInfo] = new int[2];
		aPropFieldLength[propUpdateInfo][0] = 128;
		aPropFieldLength[propUpdateInfo][1] = 128;

		aPropFieldLength[propAppCuInfo] = new int[20];
		aPropFieldLength[propAppCuInfo][0] = 4;
		aPropFieldLength[propAppCuInfo][1] = 20;
		aPropFieldLength[propAppCuInfo][2] = 32;
		aPropFieldLength[propAppCuInfo][3] = 4;
		aPropFieldLength[propAppCuInfo][4] = 50;
		aPropFieldLength[propAppCuInfo][5] = 100;
		aPropFieldLength[propAppCuInfo][6] = 15;
		aPropFieldLength[propAppCuInfo][7] = 20;
		aPropFieldLength[propAppCuInfo][8] = 1;
		aPropFieldLength[propAppCuInfo][9] = 100;
		aPropFieldLength[propAppCuInfo][10] = 19;
		aPropFieldLength[propAppCuInfo][11] = 200;
		aPropFieldLength[propAppCuInfo][12] = 1;
		aPropFieldLength[propAppCuInfo][13] = 1;
		aPropFieldLength[propAppCuInfo][14] = 19;
		aPropFieldLength[propAppCuInfo][15] = 4;
		aPropFieldLength[propAppCuInfo][16] = 1;
		aPropFieldLength[propAppCuInfo][17] = 1;
		aPropFieldLength[propAppCuInfo][18] = 3;
		aPropFieldLength[propAppCuInfo][19] = 0;

		aPropFieldLength[propList] = new int[6];
		aPropFieldLength[propList][0] = 4;
		aPropFieldLength[propList][1] = 4;
		aPropFieldLength[propList][2] = 4;
		aPropFieldLength[propList][3] = 1;
		aPropFieldLength[propList][4] = 3;
		aPropFieldLength[propList][5] = 0;

		aPropFieldLength[propAdvInfo] = new int[4];
		aPropFieldLength[propAdvInfo][0] = 4;
		aPropFieldLength[propAdvInfo][1] = 1;
		aPropFieldLength[propAdvInfo][2] = 3;
		aPropFieldLength[propAdvInfo][3] = 36;

		aPropFieldLength[propId] = new int[2];
		aPropFieldLength[propId][0] = 4;
		aPropFieldLength[propId][1] = 4;

		aPropFieldLength[propUserData] = new int[1];
		aPropFieldLength[propUserData][0] = 0;

		aPropFieldLength[propTimeout] = new int[2];
		aPropFieldLength[propTimeout][0] = 4;
		aPropFieldLength[propTimeout][1] = 4;

		
	}
	
	public static final int codeChallengeResponse = 3;
	public static final int codeAccessAccept = 4;
	public static final int codeAccessReject = 5;
	public static final int codeGetVideo = 6;
	public static final int codeGetAudio = 7;
	public static final int codeGetResponse = 8;
	public static final int codeSendVideoData = 9;
	public static final int codeSendAudioData = 10;
	public static final int codeStopVideo = 11;
	public static final int codeStopAudio = 12;
	public static final int codeACK = 13;
	public static final int codeAudioSetupRequest = 14;
	public static final int codeAudioSetupAccept = 15;
	public static final int codeAudioSetupReject = 16;
	public static final int codeAudioRELRequest = 18;
	public static final int codeCommonRes = 22;
	public static final int codeLogin = 23;
	public static final int codeLoginRes = 24;
	public static final int codeLogout = 25;
	public static final int codeEnforceLogout = 26;
	public static final int codeGetCMSRoute = 27;
	public static final int codeGetCMSRouteRes = 28;
	public static final int codeGetVTDURoute = 29;
	public static final int codeGetVTDURouteRes = 30;
	public static final int codeHeartBeat = 31;
	public static final int codeHeartBeatRes = 32;
	public static final int codeHoleAction = 33;
	public static final int codeConInfo = 34;
	public static final int codeConStop = 35;
	public static final int codeHoleInfo = 36;
	public static final int codeHoleReady = 37;
	public static final int codeHoleResult = 38;
	public static final int codeTokenMode = 39;
	public static final int codeSessionInfo = 40;
	public static final int codeNetRecordStart = 41;
	public static final int codeNetRecordStop = 42;
	public static final int codeNetPictureStart = 43;
	public static final int codeNetPictureStop = 44;
	public static final int codeToCu = 45;
	public static final int codeToPu = 46;
	public static final int codeSavePuLog = 47;
	public static final int codeSavePuAlarm = 48;
	public static final int codeSavePuPicture = 49;
	public static final int codeSavePuRecord = 50;
	public static final int codeSetAlarmConfig = 51;
	public static final int codeGetAlarmConfig = 52;
	public static final int codeDirectConnectInfo = 53;
	public static final int codeVersionInfo = 54;
	public static final int codeVersionInfoRes = 55;
	public static final int codeAppCuLogin = 56;
	public static final int codeGetPuList = 57;
	public static final int codeGetAdvList = 64;
	public static final int codeTTBinary = 101;
	
	public static final int codePuState=105;
	
	public static final int propDataAck = 6;
	public static final int propCommonRes = 22;
	public static final int propLoginKey = 23;
	public static final int propSessionInfo = 24;
	public static final int propLogout = 25;
	public static final int propHeartBeat = 26;
	public static final int propSockAddr = 27;
	public static final int propPuId = 28;
	public static final int propCuId = 29;
	public static final int propTokenModeToken = 30;
	public static final int propConsumeInfo = 31;
	public static final int propCommonReason = 32;
	public static final int propPuChannelId = 33;
	public static final int propHoleFlag = 34;
	public static final int propHoleAction = 35;
	public static final int propPassword = 36;
	public static final int propHoleToken = 37;
	public static final int propConToken = 38;
	public static final int propTtpContentOld = 39;
	public static final int propPuLog = 40;
	public static final int propPuAlarm = 41;
	public static final int propNetRecordParam = 42;
	public static final int propNetPictureParam = 43;
	public static final int propFtpUrl = 44;
	public static final int propPuPictureParam = 45;
	public static final int propPuRecordParam = 46;
	public static final int propAlarmConfigParam = 47;
	public static final int propDirectConnectInfo = 48;
	public static final int propVersionInfo = 49;
	public static final int propUpdateInfo = 50;
	public static final int propAppCuInfo = 51;
	public static final int propList = 52;
	public static final int propAdvInfo = 56;
	public static final int propTTContent = 101;
	
	public static final int propOnline=109;
	// 0x30 prop here
	public static final int propId = 102;
	public static final int propUserData = 103;
	public static final int propTimeout = 104;
	
	public static final int Timeout_interval_idx = 0;
	public static final int Timeout_timeout_idx = 1;
	public static final int Id_type_idx = 0;
	public static final int Id_id_idx = 1;
	public static final int UserData_data_idx = 0;
	public static final int DataAck_ackType_idx = 0;
	public static final int DataAck_reserve_idx = 1;
	public static final int DataAck_recvPackets_idx = 2;
	public static final int DataAck_dropPackets_idx = 3;
	public static final int DataAck_frameId_idx = 4;
	public static final int DataAck_recommendBandwidth_idx = 5;
	public static final int CommonRes_reqCode_idx = 0;
	public static final int CommonRes_result_idx = 1;
	public static final int CommonRes_reason_idx = 2;
	public static final int LoginKey_type_idx = 0;
	public static final int LoginKey_userName_idx = 1;
	public static final int LoginKey_password_idx = 2;
	public static final int SessionInfo_sessionId_idx = 0;
	public static final int SessionInfo_cuId_idx = 1;
	public static final int SessionInfo_puId_idx = 2;
	public static final int SessionInfo_domId_idx = 3;
	public static final int Logout_cuId_idx = 0;
	public static final int Logout_puId_idx = 1;
	public static final int HeartBeat_serial_idx = 0;
	public static final int HeartBeat_actionTime_idx = 1;
	public static final int SockAddr_ip_idx = 0;
	public static final int SockAddr_port_idx = 1;
	public static final int SockAddr_protocal_idx = 2;
	public static final int PuId_puId_idx = 0;
	public static final int CuId_cuId_idx = 0;
	public static final int TokenModeToken_token_idx = 0;
	public static final int ConsumeInfo_netflow_idx = 0;
	public static final int ConsumeInfo_period_idx = 1;
	public static final int CommonReason_reasonCode_idx = 0;
	public static final int CommonReason_reason_idx = 1;
	public static final int PuChannelId_puId_idx = 0;
	public static final int PuChannelId_channel_idx = 1;
	public static final int HoleFlag_holeFlag_idx = 0;
	public static final int HoleAction_holeAction_idx = 0;
	public static final int HoleAction_token_idx = 1;
	public static final int Password_password_idx = 0;
	public static final int HoleToken_token_idx = 0;
	public static final int ConToken_token_idx = 0;
	public static final int TTContent_type_idx = 0;
	public static final int TTContent_userTag_idx = 1;
	public static final int TTContent_data_idx = 2;
	public static final int Online_state_idx=0;
	public static final int PuLog_logLevel_idx = 0;
	public static final int PuLog_majorType_idx = 1;
	public static final int PuLog_minorType_idx = 2;
	public static final int PuLog_startTime_idx = 3;
	public static final int PuLog_endTime_idx = 4;
	public static final int PuLog_puLogTime_idx = 5;
	public static final int PuLog_logContent_idx = 6;
	public static final int PuAlarm_alarmType_idx = 0;
	public static final int PuAlarm_eventType_idx = 1;
	public static final int PuAlarm_deviceNumber_idx = 2;
	public static final int PuAlarm_eliminated_idx = 3;
	public static final int PuAlarm_reserved_idx = 4;
	public static final int PuAlarm_alarmLogTime_idx = 5;
	public static final int NetRecordParam_maxPeriod_idx = 0;
	public static final int NetRecordParam_reserved_idx = 1;
	public static final int NetRecordParam_title_idx = 2;
	public static final int NetRecordParam_format_idx = 3;
	public static final int NetPictureParam_maxPeriod_idx = 0;
	public static final int NetPictureParam_interval_idx = 1;
	public static final int NetPictureParam_ftpUrl_idx = 2;
	public static final int NetPictureParam_format_idx = 3;
	public static final int FtpUrl_ftpUrl_idx = 0;
	public static final int PuPictureParam_takeTime_idx = 0;
	public static final int PuPictureParam_filename_idx = 1;
	public static final int PuPictureParam_format_idx = 2;
	public static final int PuRecordParam_startTime_idx = 0;
	public static final int PuRecordParam_endTime_idx = 1;
	public static final int PuRecordParam_filename_idx = 2;
	public static final int PuRecordParam_title_idx = 3;
	public static final int PuRecordParam_format_idx = 4;
	public static final int AlarmConfigParam_puId_idx = 0;
	public static final int AlarmConfigParam_deviceLose_idx = 1;
	public static final int AlarmConfigParam_alarmIn_idx = 2;
	public static final int AlarmConfigParam_motionDetect_idx = 3;
	public static final int AlarmConfigParam_WLalramIn1_idx = 4;
	public static final int AlarmConfigParam_WLalramIn2_idx = 5;
	public static final int AlarmConfigParam_WLalramIn3_idx = 6;
	public static final int AlarmConfigParam_WLHelpIn_idx = 7;
	public static final int AlarmConfigParam_alarmTel_idx = 8;
	public static final int AlarmConfigParam_alarmEmail_idx = 9;
	public static final int DirectConnectInfo_directCapability_idx = 0;
	public static final int DirectConnectInfo_varFlag_idx = 1;
	public static final int DirectConnectInfo_reserved_idx = 2;
	public static final int DirectConnectInfo_cuId_idx = 3;
	public static final int VersionInfo_verId_idx = 0;
	public static final int VersionInfo_enforceUpdateFlag_idx = 1;
	public static final int VersionInfo_updateAction_idx = 2;
	public static final int VersionInfo_reserved_idx = 3;
	public static final int UpdateInfo_instPkgUrl_idx = 0;
	public static final int UpdateInfo_updatePkgUrl_idx = 1;
	public static final int AppCuInfo_appId_idx = 0;
	public static final int AppCuInfo_loginName_idx = 1;
	public static final int AppCuInfo_password_idx = 2;
	public static final int AppCuInfo_userId_idx = 3;
	public static final int AppCuInfo_nickName_idx = 4;
	public static final int AppCuInfo_ceMail_idx = 5;
	public static final int AppCuInfo_phone_idx = 6;
	public static final int AppCuInfo_mobile_idx = 7;
	public static final int AppCuInfo_sex_idx = 8;
	public static final int AppCuInfo_picture_idx = 9;
	public static final int AppCuInfo_birthday_idx = 10;
	public static final int AppCuInfo_address_idx = 11;
	public static final int AppCuInfo_levels_idx = 12;
	public static final int AppCuInfo_isPublish_idx = 13;
	public static final int AppCuInfo_registerDate_idx = 14;
	public static final int AppCuInfo_cuId_idx = 15;
	public static final int AppCuInfo_needAck_idx = 16;
	public static final int AppCuInfo_skipAdv_idx = 17;
	public static final int AppCuInfo_reserved_idx = 18;
	public static final int AppCuInfo_note_idx = 19;
	public static final int List_rsId_idx = 0;
	public static final int List_startRowNum_idx = 1;
	public static final int List_endRowNum_idx = 2;
	public static final int List_EOF_idx = 3;
	public static final int List_reserved_idx = 4;
	public static final int List_data_idx = 5;
	public static final int AdvInfo_advUpdateTime_idx = 0;
	public static final int AdvInfo_advType_idx = 1;
	public static final int AdvInfo_reserved_idx = 2;
	public static final int AdvInfo_fileSuffixes_idx = 3;
	
	public static final int TTP_ControlPTZ = 7;
	public static final int TTP_RegisterAH = 1;
	public static final int TTP_CancelAH = 2;
	public static final int TTP_RaiseAlarm = 4;
	public static final int TTP_ControlVS = 10;
	
}
