package com.miot.android;

import android.util.Log;

import com.cncrit.qiaoqiao.VspOperation;
import com.cncrit.qiaoqiao.WebService;
import com.google.gson.Gson;
import com.miot.orm.Cu;
import com.miot.orm.Pu;

import java.util.List;

public class PlatformBind extends Bind {

	private WebService webService = null;
	Gson gson = new Gson();

	
	public String getResultCode(String errorCode,String errormessage){
		
		String s="[{\"data\":\"\",\"errorMsg\":\""+errormessage+"\",\"resultCode\":"+errorCode+"\"}]";
		
		return s;
	}
	public PlatformBind(WebService webService) {
		this.webService = webService;
	}

	@Override
	public Result addObject(String objectName, List<Object> objects) {
		Result result = new Result();
		String methodName = "addObject";
		
		String sObjects = gson.toJson(objects);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", sObjects },
				new Object[] { "objectName", objectName } };

		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			String so = s.substring(10, s.indexOf("\",\"errorMsg"));
			so = so.replaceAll("\\\\", "");

			result.success(so);
		}

		return result;
	}

	@Override
	public Result updateObject(String objectName, List<Object> objects) {
		Result result = new Result();
		String methodName = "updateObject";
		String sObjects = gson.toJson(objects);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", sObjects },
				new Object[] { "objectName", objectName } };

		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			String so = "";
			so = s.replaceAll("\\\\", "");

			result.success(so);
		}
		return result;
	}

	@Override
	public Result deleteObject(String objectName, List<Object> objects) {
		Result result = new Result();

		String methodName = "deleteObject";
		String sObjects = gson.toJson(objects);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", sObjects },
				new Object[] { "objectName", objectName } };

		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			String so = s.substring(10, s.indexOf("\",\"errorMsg"));
			so = so.replaceAll("\\\\", "");

			result.success(so);
		}

		return result;
	}
	
	
	@Override
	public Result updatePuName(Cu cu, Pu pu) {
		Result result = new Result();
		String methodName = "updateObject";
		Object[][] params = new Object[][] {
				new Object[] {
						"requestJson",
						"[{\"cuId\":\"" + cu.getId() + "\",\"puId\":\""
								+ pu.getId() + "\",\"puName\":\""
								+ pu.getName() + "\"}]" },
				new Object[] { "objectName", "updatePuName" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {
			result.success(s);
		}
		return result;
	}

	@Override
	public Result getObject(String objectName) {

		return null;
	}

	@Override
	public Result sendVerifyCode(Cu cu) {
		Result result = new Result();
		String methodName = "addObject";
		Object[][] params = new Object[][] {
				new Object[] {
						"requestJson",
						"[{\"phone\":\"" + cu.getMobile() + "\",\"sign\":\""
								+ cu.getUserData() +"\",\"nationCode\":\""+cu.getNickname()+ "\"}]" },
				new Object[] { "objectName", "sendValidateCode" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			result.success(s);
		}
		return result;
	}

	@Override
	public Result deleteDevice(Cu cu, Pu pu) {
		Result result = new Result();
		String methodName = "deleteObject";
		Object[][] params = new Object[][] {
				new Object[] {
						"requestJson",
						"[{\"cuId\":\"" + cu.getId() + "\",\"puId\":\""
								+ pu.getId() + "\"}]" },
				new Object[] { "objectName", "deletePu" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {
			result.success(s);
		}
		return result;
	}

	@Override
	public Result validatePwdCode(Cu cu) {
		Result result = new Result();
		String methodName = "getObject";
		 Object[][] params = { 
			      { 
			      "requestJson", 
			      "[{\"phone\":\"" + cu.getMobile() + "\",\"sign\":\"" + 
			      cu.getUserData() + "\",\"nationCode\":\"" + cu.getNickname() + "\"}]" }, 
			      { "objectName", "findPwdUnsave" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			result.success(s);
		}
		return result;
	}
	/**
	 * if (map.containsKey("sign"))
				cu.setUserData(map.get("sign").toString());
			else {
				cu.setUserData("miotlink");
			}
			if (map.containsKey("nationCode")) {
				cu.setNickname(map.get("nationCode").toString());
			}
	 */
	@Override
	public Result validatePasswordCode(Cu cu) {

		Result result = new Result();
		String methodName = "addObject";
		Object[][] params = new Object[][] {
				new Object[] {
						"requestJson",
						"[{\"phone\":\"" + cu.getMobile() + "\",\"msg\":\""
								+ cu.getPassword() + "\",\"sign\":\""
								+ cu.getUserData() + "\",\"nationCode\":\""
								+ cu.getNickname() + "\"}]" },
				new Object[] { "objectName", "validatePwdCode" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {
			result.success(s);
		}
		return result;
	}
	
	
	/**
	 * 找回密码的验证码
	 * 
	 * 
	 * 
	 * 现在过时的
	 */

	@Override
	public Result sendPasswordCode(Cu cu) {

		Result result = new Result();
		String methodName = "addObject";
		Object[][] params = new Object[][] {
				new Object[] {
						"requestJson",
						"[{\"phone\":\"" + cu.getMobile() + "\",\"sign\":\""
								+ cu.getUserData() + "\",\"nationCode\":\"" + cu.getNickname()
								+ "\"}]" },
				new Object[] { "objectName", "sendPwdCode" } };
		String s = webService.call(methodName, params);
		
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {
			result.success(s);
		}
		return result;
	}


	@Override
	public Result AddObjectShareCu(String objectName, List<Object> objects) {
		Result result = new Result();
		String methodName = "addObject";
		String sObjects = gson.toJson(objects);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", sObjects },
				new Object[] { "objectName", objectName } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			String so = s.substring(10, s.indexOf("\",\"errorMsg"));
			so = so.replaceAll("\\\\", "");

			result.success(so);
		}
		return result;
	}

	@Override
	public Result GetObjectShareCu(String objectName, List<Object> objects) {
		Result result = new Result();
		String methodName = "getObject";
		String sObjects = gson.toJson(objects);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", sObjects },
				new Object[] { "objectName", objectName } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			String so = s.substring(10, s.indexOf("\",\"errorMsg"));
			so = so.replaceAll("\\\\", "");

			result.success(so);
		}
		return result;
	}

	@Override
	public Result DeleteObjectShareCu(String objectName, List<Object> objects) {
		Result result = new Result();
		String methodName = "deleteObject";
		String sObjects = gson.toJson(objects);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", sObjects },
				new Object[] { "objectName", objectName } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			
			String so = s.replaceAll("\\\\", "");

			result.success(so);
		}
		return result;
	}

	@Override
	public Result loginCu(Cu cu) {
		Result result = new Result();
		if (VspOperation.Login(cu.getName(), cu.getPassword())) {
			cu.setId("" + VspOperation.cuId);
			result.success(gson.toJson(cu));
		} else {
			result.fail(getResultCode(VspOperation.loginFailCode+"",VspOperation.loginFailErrorMess));
		}
		return result;
	}

	@Override
	public Result updateAllCu(Cu cu) {
		Result result = new Result();
		String methodName = "updateObject";
		String sObjects = gson.toJson(cu);
		sObjects = sObjects.replaceAll("\\\\", "");
		sObjects = sObjects.replace("\"{", "{");
		sObjects = sObjects.replace("}\"", "}");
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", sObjects },
				new Object[] { "objectName", "updateAllCu" } };

		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			String so = s.substring(10, s.indexOf("\",\"errorMsg"));
			so = so.replaceAll("\\\\", "");

			result.success(so);
		}

		return result;
	}

	@Override
	public Result regiestCu(Cu cu) {
		Result result = new Result();
		String methodName = "updateCu";
		Object[][] params = new Object[][] {
				new Object[] { "opType", Integer.valueOf(1) },
				// [{\"username\":\"测试用户\",\"password\":\"123456\",\"nickName\":\"你好\",\"mobile\":\"13456900526\"}]
				new Object[] {
						"requestJson",
						"[{\"username\":\"" + cu.getName()
								+ "\",\"password\":\"" + cu.getPassword()
								+ "\",\"nickName\":\"" + cu.getNickname()
								+ "\",\"mobile\":\"" + cu.getMobile()
								+ "\",\"code\":\"" + cu.getUserData() + "\"}]" },
				new Object[] { "reserve", "" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			result.success(cu);
		}
		return result;
	}

	@Override
	public Result getPuList(Cu cu) {
		Result result = new Result();
		String methodName = "getPu";
		Object[][] params = new Object[][] {
				new Object[] { "opType", Integer.valueOf(1) },
				new Object[] { "requestJson", "[{\"cuID\":" + cu.getId() + "}]" },
				new Object[] { "reserve", "" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			String so = s.substring(10, s.indexOf("\",\"errorMsg"));
			so = so.replaceAll("\\\\", "");
			result.success(so);
		}
		return result;
	}

	@Override
	public Result updateCuPwd(Cu cu) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Result send(Pu pu, String content) {
		Result result = new Result();
		Log.e("", "发送设备的PuId" + pu.getId());
		if (VspOperation.toPu(Integer.parseInt(pu.getId()), content, 1, 0)) {
			result.success("发送数据到设备成功");
		} else {
			result.fail(getResultCode(-1+"","发送数据到设备失败!"));
		}
		return result;
	}

	@Override
	public Result logoutCu(Cu cu) {
		VspOperation.Logout(null);
		Result result = new Result();
		result.success("已经登出平台!");
		return result;
	}

	@Override
	public Result getqRCodeResult(List<Object> objects) {
		Result result = new Result();
		String methodName = "getObject";
		String sObjects = gson.toJson(objects);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", sObjects },
				new Object[] { "objectName", "parseKindModel" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {

			String so = s.substring(10, s.indexOf("\",\"errorMsg"));
			so = so.replaceAll("\\\\", "");

			result.success(so);
		}
		return result;
	}

	@Override
	public Result getUserRegistionCode(String userName) {
		Result result = new Result();
		String methodName = "getObject";
		Object[][] params = new Object[][] {
				new Object[] { "requestJson",
						"{\"username\":\"" + userName + "\"}" },
				new Object[] { "objectName", "getUserRegistionCode" } };
		String s = webService.call(methodName, params);
		result.success(s);
		return result;
	}

	@Override
	public Result getUserRegistration(List<Object> objects) {
		Result result = new Result();
		String methodName = "addObject";
		String objectName = "userRegistration";
		String sObjects = gson.toJson(objects);
		String objectdata = sObjects.substring(1, sObjects.length() - 1);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", objectdata },
				new Object[] { "objectName", objectName } };
		String s = webService.call(methodName, params);
		result.success(s);
		return result;
	}

	@Override
	public Result getupdatePwd(List<Object> objects) {
		Result result = new Result();
		String methodName = "updateObject";
		String objectName = "updatePwd";
		String sObjects = gson.toJson(objects);
		String objectdata = sObjects.substring(1, sObjects.length() - 1);
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", objectdata },
				new Object[] { "objectName", objectName } };

		String s = webService.call(methodName, params);
		result.success(s);
		return result;
	}

	@Override
	public Result sendCu(Cu pu, String content) {
		Result result = new Result();
		Log.e("", "发送设备的PuId" + pu.getId());
		if (VspOperation.toCu(Integer.parseInt(pu.getId()), content, 1, 0)) {
			result.success(getResultCode(1+"", "发送数据成功"));
		} else {
			result.fail(getResultCode(-1+"", "发送数据失败"));
		}
		return result;
	}

	@Override
	public Result getNewPuList(Cu cu) {
		Result result = new Result();
		String client="ANDROID";
		String methodName = "getObject";
		Object[][] params = new Object[][] {
				new Object[] { "requestJson", "[{\"cuId\":\"" + cu.getId() + "\",\"client\":\"" + client
						+ "\"}]" },
				new Object[] { "objectName", "getH5ModePuList" } };
		String s = webService.call(methodName, params);
		if (!WebService.isCallSuccess(s)) {
			result.fail(s);
		} else {
			String so = s.substring(10, s.indexOf("\",\"errorMsg"));
			so = so.replaceAll("\\\\", "");
			result.success(so);
		}
		return result;
	}

}
