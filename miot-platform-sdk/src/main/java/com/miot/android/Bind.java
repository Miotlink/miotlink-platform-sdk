
package com.miot.android;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Binder;

import com.miot.orm.Cu;
import com.miot.orm.Pu;

@SuppressLint("NewApi")
public abstract class Bind extends Binder {

    public abstract Result loginCu(Cu cu);

    public abstract Result updateAllCu(Cu cu);

    public abstract Result regiestCu(Cu dbCu);

    public abstract Result getPuList(Cu cu);
    
    public abstract Result getNewPuList(Cu cu);

    public abstract Result updateCuPwd(Cu cu);

    public abstract Result updatePuName(Cu cu, Pu pu);
    /**
     * 发送数据 到平台接口
     * @param pu
     * @param content
     * @return
     */
    public abstract Result send(Pu pu, String content);
    public abstract Result sendCu(Cu pu, String content);

    /**
     * 登陆接口
     * @param cu
     * @return
     */
    public abstract Result logoutCu(Cu cu);

    /**
     * 添加Pu信息接口
     * @param objectName
     * @param objects
     * @return
     */
    public abstract Result addObject(String objectName, List<Object> objects);

    /**
     * 更新数据接口
     * @param objectName
     * @param objects
     * @return
     */
    public abstract Result updateObject(String objectName, List<Object> objects);

    /**
     * 删除
     * @param objectName
     * @param objects
     * @return
     */
    public abstract Result deleteObject(String objectName, List<Object> objects);

    /**
     * 获取
     * @param objectName
     * @return
     */
    public abstract Result getObject(String objectName);
    

    /**
     * 验证码
     * @param cu
     * @return
     */
    public abstract Result sendVerifyCode(Cu cu);

    /**
     * 删除设备
     * @param cu
     * @param pu
     * @return
     */
    public abstract Result deleteDevice(Cu cu, Pu pu);

    /**
     * 获取密码验证码
     * 
     * @param cu
     * @return
     */

    public abstract Result validatePwdCode(Cu cu);
    
    public abstract Result validatePasswordCode(Cu cu);
    
    public abstract Result sendPasswordCode(Cu cu);
    
    

    
    
    
    /**
     * 添加用户分享
     * @param objectName
     * @param objects
     * @return
     */
    public abstract Result AddObjectShareCu(String objectName,List<Object> objects);
    
    /**
     * 获取用户分享
     * @param objectName
     * @param objects
     * @return
     */
    public abstract Result GetObjectShareCu(String objectName,List<Object> objects);
    
    
    
    /**
     * 删除用户分享
     * @param objectName
     * @param objects
     * @return
     */
    public abstract Result DeleteObjectShareCu(String objectName,List<Object> objects);
    
    public abstract Result getqRCodeResult(List<Object> objects);
    
    public abstract Result getUserRegistionCode(String userName);
    
    public abstract Result getUserRegistration(List<Object> objects);
    
    public abstract Result getupdatePwd(List<Object> objects);
}
