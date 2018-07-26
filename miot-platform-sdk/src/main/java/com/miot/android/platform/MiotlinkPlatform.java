package com.miot.android.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.cncrit.qiaoqiao.VspOperation;
import com.miot.android.Bind;
import com.miot.android.BindService;
import com.miot.android.Result;
import com.miot.android.Service;

import com.miot.android.content.NoFormatConsts;
import com.miot.android.listener.MiotPlatformUIListener;
import com.miot.android.receiver.MmwBroadCast;
import com.miot.android.util.ACache;
import com.miot.android.util.Mhost;
import com.miot.android.util.MiotMacParseUitls;
import com.miot.android.util.MiotQrcodeParseUitls;
import com.miot.android.util.MiotlinkUtil;
import com.miot.orm.Cu;
import com.miot.orm.Pu;

/**
 * 此类用于妙联平台所有的调用方法 创建于2015年9月10号 创建人QZ
 * 
 * @author 
 * 
 */
public class MiotlinkPlatform {


	public static MiotlinkPlatform instance = null;

	public static MiotlinkPlatform getInstance(Context context) {
		if (instance == null) {
			synchronized (MiotlinkPlatform.class) {
				instance = new MiotlinkPlatform(context);
			}
		}
		return instance;
	}
	private Bind bind = null;

	
	private BindService bindService=null;
	
	private Context context=null;


	public MiotlinkPlatform(Context context) {
		this.context=context;
		bindService=BindService.getInstance(context);
		bind = bindService.getBind();
	}
	
	
	
	public void setMiotPlatformUIListener(
			MiotPlatformUIListener miotPlatformUIListener) {
		MmwBroadCast.miotPlatformUIListener = miotPlatformUIListener;
	}

	public Result init() {
		Result result = new Result();
		result.success("Versions_V2.2.7");
		return result;

	}

	/**
	 * 登录
	 * 
	 * @param map
	 */
	public Result miotlinkPlatform_getLogin(Map<String, Object> map) {
		
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setName(map.get("username").toString());
			cu.setPassword(map.get("password").toString());
			if (ACache.get(context).getAsString(NoFormatConsts.PLATFORM_EXTERNAL_URL)==null) {
				new Mhost.HostAddress().execute(NoFormatConsts.PLATFORM_EXTERNAL_URL);
			}
			if (bind==null) {
				bind=bindService.getBind();
			}
			result = bind.loginCu(cu);
			if (result.getCode()==Result.CODE_SUCCESS) {
				if (ACache.get(context).getAsString(NoFormatConsts.PLATFORM_EXTERNAL_URL)==null) {
					ACache.get(context).put(NoFormatConsts.PLATFORM_EXTERNAL_URL, VspOperation.rsIp,24*60*60);
				}
			}
		} catch (Exception e) {
			result.fail(getResultCode(23+"","map数据异常"));
		}
		return result;
	}

	/**
	 * 获取设备列表
	 * 
	 * @param map
	 * @return
	 */
	@Deprecated
	public Result miotlinkPlatform_getDeviceList(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setId(map.get("cuId").toString());
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.getPuList(cu);
		} catch (Exception e) {
			result.fail(getResultCode(23+"","map数据异常"));
		}

		return result;

	}
	
	/**
	 * 获取设备列表
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_getNewDeviceList(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setId(map.get("cuId").toString());
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.getNewPuList(cu);
		} catch (Exception e) {
			result.fail(getResultCode(23+"","map数据异常"));
		}

		return result;

	}

	/**
	 * 获取验证码
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_getVerificationCode(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setMobile(map.get("phoneNumber").toString());
			if (map.containsKey("sign"))
				cu.setUserData(map.get("sign").toString());
			else {
				cu.setUserData("miotlink");
			}
			if (map.containsKey("nationCode")) {
				cu.setNickname(map.get("nationCode").toString());
			}
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			if (bind != null)
				result = bind.sendVerifyCode(cu);
		} catch (Exception e) {
			result.fail(getResultCode(23+"","map数据异常"));
		}
		return result;

	}

	/**
	 * 注册
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_getRegirster(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setName(map.get("username").toString());
			cu.setPassword(map.get("password").toString());
			cu.setMobile(map.get("username").toString());
			cu.setNickname(map.get("username").toString());
			cu.setUserData(map.get("verificationCode").toString());
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.regiestCu(cu);
		} catch (Exception e) {
			result.fail(getResultCode(23+"","map数据异常"));
		}
		return result;
	}

	/**
	 * 注册
	 * 
	 * @param map
	 * @return
	 */
	private Result miotlinkPlatform_sendCuToCu(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setId(map.get("id").toString());
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.sendCu(cu, map.get("content").toString());
		} catch (Exception e) {
			result.fail(getResultCode(23+"","map数据异常"));
		}
		return result;
	}
	
	

	/**
	 * 找回密码
	 * 
	 * @param map
	 * @return
	 */
	
	@Deprecated
	public Result miotlinkPlatform_getFindPassword(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setMobile(map.get("phoneNumber").toString());
			if (map.containsKey("sign"))
				cu.setUserData(map.get("sign").toString());
			else {
				cu.setUserData("miotlink");
			}
			if (map.containsKey("nationCode")) {
				cu.setNickname(map.get("nationCode").toString());
			}
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.validatePwdCode(cu);
		} catch (Exception e) {
			return result.fail(getResultCode(23+"","map数据异常"));
		}
		return result;
	}
	
	public Result miotlinkPlatform_getValidatePasswordCode(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setMobile(map.get("phoneNumber").toString());
			if (map.containsKey("sign"))
				cu.setUserData(map.get("sign").toString());
			else {
				cu.setUserData("miotlink");
			}
			if (map.containsKey("nationCode")) {
				cu.setNickname(map.get("nationCode").toString());
			}
			if (map.containsKey("verificationPwdCode")) {
				cu.setPassword(map.get("verificationPwdCode").toString());
			}
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.validatePasswordCode(cu);
		} catch (Exception e) {
			return result.fail(getResultCode(23+"","map数据异常"));
		}
		return result;
	}
	public Result miotlinkPlatform_getSendPasswordCode(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			cu.setMobile(map.get("phoneNumber").toString());
			if (map.containsKey("sign"))
				cu.setUserData(map.get("sign").toString());
			else {
				cu.setUserData("miotlink");
			}
			if (map.containsKey("nationCode")) {
				cu.setNickname(map.get("nationCode").toString());
			}
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.sendPasswordCode(cu);
		} catch (Exception e) {
			return result.fail(getResultCode(23+"","map数据异常"));
		}
		return result;
	}

	public Result miotlinkPlatform_updateUser(Map<String, Object> map) {
		Result result = new Result();
		String objectName = "updateCuPwd";
		List<Object> objects = new ArrayList<Object>();
		objects.add(map);
		try {
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.updateObject(objectName, objects);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	/**
	 * 更新设备名称
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_updatePuName(Map<String, Object> map) {
		Result result = new Result();
		Cu cu = new Cu();
		Pu pu = new Pu();
		if (map != null) {
			if (map.containsKey("cuId") && map.containsKey("puId")
					&& map.containsKey("puName")) {
				cu.setId(map.get("cuId").toString());
				pu.setId(map.get("puId").toString());
				pu.setName(map.get("puName").toString());
				try {
					if (bind == null) {
						bind=bindService.getBind();
						if (bind==null) {
							
							return result.fail(getResultCode(-10+"","初始化错误"));
						}
					}
					result = bind.updatePuName(cu, pu);
				} catch (Exception e) {

				}
			}
			return result;
		}

		return result.fail("参数错误");
	}

	/**
	 * 添加设备
	 * 
	 * @return
	 */
	public Result miotlinkPlatform_addDevice(Map<String, Object> map) {
		Result result = new Result();
		String objectName = "getPuByMac";
		if (map.containsKey("qrcode")) {
			String qrcode=map.get("qrcode").toString();
			if (qrcode!=null) {
				qrcode=MiotQrcodeParseUitls.getQrcode(qrcode);
				if (!qrcode.equals("")) {
					map.remove("qrcode");
					map.put("qrcode", qrcode);
				}
			}
		}
		if (map.containsKey("macCode")) {
			String mac=map.get("macCode").toString();
			if (!MiotMacParseUitls.isMAC(mac)) {
				String parseMac=MiotQrcodeParseUitls.getQrcode(mac);
				if (parseMac.equals("")) {
					if (MiotMacParseUitls.getIMIEBuilder(mac).equals("")) {
						result.fail("mac Address error");
					}
					mac=MiotMacParseUitls.getIMIEBuilder(mac);
					
					map.remove("macCode");
					map.put("macCode", mac);
				}
				if (parseMac.length()==29) {
					map.remove("qrcode");
					map.put("qrcode", parseMac);
					map.remove("macCode");
					map.put("macCode", parseMac);
				}
			}
			
		}
		List<Object> objects = new ArrayList<Object>();
		objects.add(map);
		try {
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.addObject(objectName, objects);
		} catch (Exception e) {

		}
		return result;

	}

	/**
	 * 删除设备
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_deleteDevice(Map<String, Object> map) {
		Result result = new Result();
		try {
			Cu cu = new Cu();
			Pu pu = new Pu();
			cu.setId(map.get("cuId").toString());
			pu.setId(map.get("puId").toString());
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.deleteDevice(cu, pu);
		} catch (Exception e) {
		}
		return result;

	}

	/**
	 * 向设备发送数据
	 */
	public Result miotlinkPlatform_sendDevice(Map<String, Object> map) {
		Result result = new Result();
		try {
			String HexadecimalString = "";
			Pu pu = new Pu();
			pu.setId(map.get("puId").toString());
			String content = "";
			HexadecimalString = map.get("hexCode").toString();
			if (HexadecimalString.equals("")
					|| HexadecimalString.equals("hexString")) {
				content = map.get("uart").toString();
				content = MiotlinkUtil.doLinkBindMake(content);
			} else if (HexadecimalString.equals("hexByte")) {
				byte[] by = (byte[]) map.get("uart");
				content = MiotlinkUtil.doLinkBindByteMake(by);
			}else if(HexadecimalString.equals("infrared")){
				int [] iInfrare=(int[]) map.get("uart");
				content = MiotlinkUtil.doInfraredLinkBindMake(iInfrare);
			}
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.send(pu, content);

		} catch (Exception e) {
			return result.fail("数据异常");
		}
		return result;

	}

	/**
	 * 获取分享用户
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_getShareList(Map<String, Object> map) {
		Result result = new Result();
		String objectName = "getShareCu";
		List<Object> objects = new ArrayList<Object>();
		objects.add(map);
		try {
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.GetObjectShareCu(objectName, objects);
		} catch (Exception e) {
		}
		return result;

	}

	/**
	 * 添加分享用户
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_addShare(Map<String, Object> map) {
		Result result = new Result();
		String objectName = "addShareCu";
		List<Object> objects = new ArrayList<Object>();
		objects.add(map);
		try {
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.AddObjectShareCu(objectName, objects);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 删除分享用户
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_deleteShare(Map<String, Object> map) {
		Result result = new Result();
		String objectName = "deleteShareCu";
		List<Object> objects = new ArrayList<Object>();
		objects.add(map);
		try {
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.DeleteObjectShareCu(objectName, objects);
		} catch (Exception e) {

		}
		return result;
	}
	
    private  String getResultCode(String errorCode,String errormessage){
		
		String s="[{\"data\":\"\",\"errorMsg\":\""+errormessage+"\",\"resultCode\":\""+errorCode+"\"}]";
		
		return s;
	}

	/**
	 * 二维码信息
	 * 
	 * @param qrcode
	 * @return
	 */
	public Result miotlinkPlatform_qRcode(String qrcode) {
		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		if (qrcode.equals("")) {
			return result.fail(getResultCode(20+"","二维码不能为空"));
		}
		String qrcodeResult = MiotQrcodeParseUitls.getQrcode(qrcode);
		if (qrcodeResult.equals("")) {
			return result.fail(getResultCode(21+"","二维码错误"));
		}
		try {

			int kindId = Integer.parseInt(qrcodeResult.substring(1, 4));
			int modelId = Integer.parseInt(qrcodeResult.substring(5, 8));
			String fcMode = qrcodeResult.substring(8, 9);
			map.put("kindId", kindId);
			map.put("modelId", modelId);
			map.put("fcMode", fcMode);
			List<Object> objects = new ArrayList<Object>();
			objects.add(map);
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.getqRCodeResult(objects);
			if (result.getCode() == Result.CODE_SUCCESS) {
				String data = result.getData().toString();
				JSONObject jsonObject = new JSONObject(data);
				if (jsonObject != null) {
					jsonObject.put("fcMode", fcMode);
					jsonObject.put("qrcode", qrcodeResult);
					jsonObject.put("kindId", kindId);
					jsonObject.put("modelId", modelId);
					if (qrcodeResult.length()>11) {
						jsonObject.put("macCode", qrcodeResult);
					}
				}
				result.success(jsonObject.toString());
			}
		} catch (Exception e) {
			return result.fail(getResultCode(21+"","二维码错误"));
		}
		return result;
	}

	public Result miotlinkPlatform_logout() {
		Result result = new Result();
		if (bind == null) {
			bind=bindService.getBind();
			if (bind==null) {
				return result.fail(getResultCode(-10+"","初始化错误"));
			}
		}
		result = bind.logoutCu(null);
		return result;
	}

	public Result miotlinkPlatform_updatePassword(Map<String, Object> map) {
		Result result = new Result();
		String objectName = "updateCuPwd";
		try {
			List<Object> objects = new ArrayList<Object>();
			objects.add(map);
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.updateObject(objectName, objects);

		} catch (Exception e) {

		}
		return result;

	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public Result miotlinkPlatform_getUserRegistionCode(Map<String, Object> map) {
		Result result = new Result();
		String username = map.get("username").toString();
		try {
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.getUserRegistionCode(username);

		} catch (Exception e) {
			return result.fail(getResultCode("23","数据异常"));
		}
		return result;
	}

	public Result miotlinkPlatform_getUserRegistration(Map<String, Object> map) {
		Result result = new Result();
		try {
			List<Object> objects = new ArrayList<Object>();
			objects.add(map);
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.getUserRegistration(objects);

		} catch (Exception e) {

		}
		return result;
	}

	public Result miotlinkPlatform_getUpdataPassword(Map<String, Object> map) {
		Result result = new Result();
		try {
			List<Object> objects = new ArrayList<Object>();
			objects.add(map);
			if (bind == null) {
				bind=bindService.getBind();
				if (bind==null) {
					return result.fail(getResultCode(-10+"","初始化错误"));
				}
			}
			result = bind.getupdatePwd(objects);

		} catch (Exception e) {

		}
		return result;
	}

	public Result onDistroy() {
		Result result = new Result();
		if (bindService!=null) {
			bindService.stopBind();
		}
		result.success("onDistory is success");
		return result;

	}
}
