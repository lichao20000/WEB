
package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.service.dao.BssSheetByHand4STBGSDXDAO;
import com.linkage.module.itms.service.obj.Result;
import com.linkage.module.itms.service.obj.SheetObj4GSStb;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2021年1月27日
 * @category com.linkage.module.itms.service.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BssSheetByHand4STBGSDXBIO {

	private static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4STBGSDXBIO.class);
    private BssSheetByHand4STBGSDXDAO dao;
    


	private static final String ENDPOINTREFERENCE = LipossGlobals
            .getLipossProperty("webStbServiceUri");


    public SheetObj4GSStb checkStbServAccount(String servAccount) {
        boolean result = this.dao.isServStbExists(servAccount);
        if (result) {
        	SheetObj4GSStb obj = new SheetObj4GSStb();
            Map<String, String> userInfo = this.dao.getStbUserInfo(servAccount);
            stbUserInfoToObj(userInfo, obj);
            return obj;
        }
        return null;
    }

 
    private void stbUserInfoToObj(Map<String, String> userInfo, SheetObj4GSStb obj) {
        obj.setCityId(StringUtil.getStringValue(userInfo, "city_id", ""));
        obj.setUserID(StringUtil.getStringValue(userInfo, "serv_account", ""));
        obj.setPassword(StringUtil.getStringValue(userInfo, "serv_pwd", ""));
        obj.setMacAddress(StringUtil.getStringValue(userInfo, "cpe_mac"));
        obj.setPlatform(StringUtil.getIntValue(userInfo, "platform"));
    }

    private Result doStbSheet(SheetObj4GSStb order) {
    	if(null == order){
    		return null;
    	}
    	String paramLog = JSON.toJSONString(order);
        logger.warn("doStbSheet param : {} " ,new Object[]{paramLog});
        Result returnParam = new Result();
        try {
            Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			
			//请求序列化
			QName qnReq = new QName("urn:BeanService", "SyncUserReq");
			call.registerTypeMapping(SheetObj4GSStb.class, qnReq, new BeanSerializerFactory(
					SheetObj4GSStb.class, qnReq), new BeanDeserializerFactory(SheetObj4GSStb.class, qnReq));
			
			//应答序列化,不写会报 could not find deserializer for type xxx
			QName qnRet = new QName("urn:BeanService", "Result");
			call.registerTypeMapping(Result.class, qnRet, new BeanSerializerFactory(
					Result.class, qnRet), new BeanDeserializerFactory(Result.class, qnRet));
			
			
			call.setOperationName(new QName(ENDPOINTREFERENCE, "syncUser"));
			call.setTargetEndpointAddress(new URL(ENDPOINTREFERENCE));
			//不写会报错 must call addParameter() for all parameters if you have called setReturnType()
			call.addParameter("SyncUserReq", qnReq, ParameterMode.IN );

			call.setTimeout(1000 * 5);
			
			//返回类型
			call.setReturnClass(Result.class);
 			returnParam = ((Result) call.invoke(new Object[] { order }));
 			String returnLog = JSON.toJSONString(returnParam);
            logger.warn("doStbSheet return : {} " ,new Object[]{returnLog});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnParam;
    }
 
    /**
     * 机顶盒开户
     *
     * @param obj
     * @param curUser
     * @return
     */
    public String stbDoBusiness(SheetObj4GSStb obj, UserRes curUser) {
        logger.debug("BssSheetByHand4AHBIO.stbDoBusiness(SheetObj4GSStb obj)");
        StringBuilder buffer = new StringBuilder();
        try {
        	Result stbResult = doStbSheet(obj);
            buffer.append("机顶盒开户工单处理结果：");
            
            if(null == stbResult){
            	logger.error("工单返回为null,调用异常");
            	return null;
            }
            buffer.append(stbResult.toString());
            this.dao.addHandSheetLog(obj, curUser, obj.getAction(), 1,
                    "000".equals(""+stbResult.getResultCode()) ? 1 : 0,stbResult.getResultDesc());
        } catch (Exception e) {
            logger.error("页面发送工单失败：[]" ,e);
        }
        return buffer.toString();
    }
	public BssSheetByHand4STBGSDXDAO getDao()
	{
		return dao;
	}


	
	public void setDao(BssSheetByHand4STBGSDXDAO dao)
	{
		this.dao = dao;
	}
}
