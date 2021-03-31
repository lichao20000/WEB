
package com.linkage.module.itms.midware.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.MidWareCorba;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.midware.dao.MidWareDAO;

@SuppressWarnings("unchecked")
public class MidWareBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(MidWareBIO.class);
	private Map resultMap;

	MidWareDAO midWareDAO = null;
	
	/**
	 * 增加到中间件，先调D1,然后调C1,最后入库
	 * 
	 * @param deviceId
	 * @param oui
	 * @param deviceSn
	 * @param deviceModel
	 * @param adNumber
	 * @param status
	 * @param area
	 * @param group
	 * @param phone
	 * @param des
	 * @return
	 */
	public String addMidWareDevice(String cityId,String deviceId, String oui, String deviceSn, String deviceModel,
			String adNumber, String status, String area, String group, String phone,
			String des){
		
		String result = "";
		resultMap = new HashMap();
//		1.	0   设备添加成功
//		2.	1   服务器连接失败
//		3.	10001   数据格式错误
//		4.	10002   设备已经存在

		resultMap.put(0, "设备添加成功");
		resultMap.put(1, "服务器连接失败");
		resultMap.put(10001, "数据格式错误");
		resultMap.put(10002, "设备已经存在");
		
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				int temp_ = add(deviceId, oui, deviceSn, deviceModel, adNumber, status, area, group, phone);
				
				if(0==temp_){
					temp_ = addDeviceCity(deviceId, oui, deviceSn, cityId);
					if(0==temp_){
						List list_ = midWareDAO.getMidWareDevice(deviceId);
						if(list_.size()>0){
							midWareDAO.updateMidWareDevice(deviceId, cityId, oui, deviceSn, deviceModel, group, phone, status, adNumber, "");
						}else{
							midWareDAO.insertMidWareDevice(deviceId, cityId, oui, deviceSn, deviceModel, group, phone, status, adNumber, "");
						}
					}
				}
				logger.debug("－－－－－－－中间件接口调用结果temp_：{}！－－－－－－－－－",temp_);
				if(null==resultMap.get(temp_)){
					result = String.valueOf(temp_);
				}else{
					result = StringUtil.getStringValue(resultMap.get(temp_));
				}
				logger.debug("－－－－－－－中间件接口调用结果temp_：{}！－－－－－－－－－",result);
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = "通知中间件失败!";
			
		}
		return result;
	}
	
	/**
	 * 中间件增加设备
	 *
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param 
	 * @return String
	 */
	public int add(String deviceId, String oui, String deviceSn, String deviceModel,
			String adNumber, String status, String area, String group, String phone)
	{
		int result =0;
//		1.	0   设备添加成功
//		2.	1   服务器连接失败
//		3.	10001   数据格式错误
//		4.	10002   设备已经存在

		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DeviceObject devObject = new MidWare.DeviceObject();
				devObject.device_id = deviceId;
				devObject.oui = oui;
				devObject.device_serialnumber = deviceSn;
				// 其他参数
				MidWare.Param[] paramArr = new MidWare.Param[6];
				paramArr[0] = new MidWare.Param();
				paramArr[0].param_name = "TYPE";
				paramArr[0].param_type = "String";
				paramArr[0].param_value = deviceModel;
				
				paramArr[1] = new MidWare.Param();
				paramArr[1].param_name = "AREA";
				paramArr[1].param_type = "String";
				paramArr[1].param_value = area == null ? "" : area;
				
				paramArr[2] = new MidWare.Param();
				paramArr[2].param_name = "GROUP";
				paramArr[2].param_type = "String";
				paramArr[2].param_value = group == null ? "" : group;
				
				paramArr[3] = new MidWare.Param();
				paramArr[3].param_name = "PHONE";
				paramArr[3].param_type = "String";
				paramArr[3].param_value = phone == null ? "" : phone;
				
				paramArr[4] = new MidWare.Param();
				paramArr[4].param_name = "STATUS";
				paramArr[4].param_type = "String";
				paramArr[4].param_value = status;
				
				paramArr[5] = new MidWare.Param();
				paramArr[5].param_name = "AD";
				paramArr[5].param_type = "String";
				paramArr[5].param_value = adNumber;
				
				MidWare.DeviceListObject[] devListObjectArr = new MidWare.DeviceListObject[1];
				devListObjectArr[0] = new MidWare.DeviceListObject();
				devListObjectArr[0].deviceObject = new MidWare.DeviceObject();
				devListObjectArr[0].deviceObject = devObject;
				devListObjectArr[0].paramArr = new MidWare.Param[6];
				devListObjectArr[0].paramArr = paramArr;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DeviceObjectRep[] devObjectRepArr = null;
				logger.debug("－－－－－－－－调用中间件接口！－－－－－－－－－－");
				devObjectRepArr = midCorba.addDevice(devListObjectArr);
				
				if (devObjectRepArr != null && devObjectRepArr.length > 0)
				{
					logger.debug("－－－－－－－－中间件接口调用成功！－－－－－－－－－－");
					result = devObjectRepArr[0].result_code;
				}
				else
				{
					logger.debug("－－－－－－－－中间件接口调用失败！－－－－－－－－－－");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
			
		}
		return result;
	}

	/**
	 * 中间件更新设备
	 * 
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param
	 * @return String
	 */
	public String update(String deviceId, String oui, String deviceSn,
			String deviceModel, String adNumber, String status, String area,
			String group, String phone, String des)
	{
		String result = "";
//		1.	0   设备修改成功
//		2.	1   服务器连接失败
//		3.	10001   数据格式错误
//		4.	10003   设备不存在
		resultMap = new HashMap();
		resultMap.put(0, "设备修改成功");
		resultMap.put(1, "服务器连接失败");
		resultMap.put(10001, "数据格式错误");
		resultMap.put(10003, "设备不存在");
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DeviceObject devObject = new MidWare.DeviceObject();
				devObject.device_id = deviceId;
				devObject.oui = oui;
				devObject.device_serialnumber = deviceSn;
				// 其他参数
				MidWare.Param[] paramArr = new MidWare.Param[6];
				paramArr[0] = new MidWare.Param();
				paramArr[0].param_name = "TYPE";
				paramArr[0].param_type = "String";
				paramArr[0].param_value = deviceModel;
				
				paramArr[1] = new MidWare.Param();
				paramArr[1].param_name = "AREA";
				paramArr[1].param_type = "String";
				paramArr[1].param_value = area == null ? "" : area;
				
				paramArr[2] = new MidWare.Param();
				paramArr[2].param_name = "GROUP";
				paramArr[2].param_type = "String";
				paramArr[2].param_value = group == null ? "" : group;
				
				paramArr[3] = new MidWare.Param();
				paramArr[3].param_name = "PHONE";
				paramArr[3].param_type = "String";
				paramArr[3].param_value = phone == null ? "" : phone;
				
				paramArr[4] = new MidWare.Param();
				paramArr[4].param_name = "STATUS";
				paramArr[4].param_type = "String";
				paramArr[4].param_value = status;
				
				paramArr[5] = new MidWare.Param();
				paramArr[5].param_name = "AD";
				paramArr[5].param_type = "String";
				paramArr[5].param_value = adNumber;
				
				MidWare.DeviceListObject[] devListObjectArr = new MidWare.DeviceListObject[1];
				devListObjectArr[0] = new MidWare.DeviceListObject();
				devListObjectArr[0].deviceObject = new MidWare.DeviceObject();
				devListObjectArr[0].deviceObject = devObject;
				devListObjectArr[0].paramArr = new MidWare.Param[6];
				devListObjectArr[0].paramArr = paramArr;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DeviceObjectRep[] devObjectRepArr = null;	
				devObjectRepArr = midCorba.updateDevice(devListObjectArr);
				if (devObjectRepArr != null && devObjectRepArr.length > 0)
				{
					logger.debug("－－－－－－－－中间件接口调用成功！－－－－－－－－－－");
					result = StringUtil.getStringValue(resultMap.get(devObjectRepArr[0].result_code));
				}
				else
				{
					logger.debug("－－－－－－－－中间件接口调用失败！－－－－－－－－－－");
					result = "中间件接口调用失败！";
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = "通知中间件失败";
		}
		return result;
	}

	public String deleteMidWareDevice(String deviceId, String oui, String deviceSn) {
		String result = "";
		resultMap = new HashMap();
//		1.	0   设备添加成功
//		2.	1   服务器连接失败
//		3.	10001   数据格式错误
//		4.	10002   设备已经存在

		resultMap.put(0, "设备删除成功");
		resultMap.put(1, "服务器连接失败");
		resultMap.put(10001, "数据格式错误");
		resultMap.put(10002, "设备已经存在");
		
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				int temp_ =0;
				List list_ = midWareDAO.getMidWareDevice(deviceId);
				if(list_.size()>0){
					Map map_ = (Map) list_.get(0);
					String cityId_ = String.valueOf(map_.get("city_id"));
					temp_ = deleteDeviceCity(deviceId, oui, deviceSn, cityId_);
					if(0==temp_){
						temp_ = delete(deviceId, oui, deviceSn);
						if(0==temp_){
							midWareDAO.deleteMidWareDevice(deviceId);
						}
					}
				}else{
					result = "设备没有添加到中间件平台！";
					return result;
				}
				
				result = StringUtil.getStringValue(resultMap.get(temp_));
				if(null==result){
					result = String.valueOf(temp_);
				}
				logger.debug("－－－－－－－中间件接口调用结果：{}！－－－－－－－－－",result);
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = "通知中间件失败!";
			
		}
		return result;
	}
	
	/**
	 * 中间件删除设备
	 * 
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param
	 * @return String
	 */
	public int delete(String deviceId, String oui, String deviceSn)
	{
		int result = 0;
		// 1. 0 设备删除成功
		// 2. 1 服务器连接失败
		// 3. 10001 数据格式错误
		// 4. 10003 设备不存在

		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DeviceObject[] devObjectArr = new MidWare.DeviceObject[1];
				MidWare.DeviceObject devObj = new MidWare.DeviceObject();
				devObj.device_id = deviceId;
				devObj.oui = oui;
				devObj.device_serialnumber = deviceSn;
				devObjectArr[0] = new MidWare.DeviceObject();
				devObjectArr[0] = devObj;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DeviceObjectRep[] devObjArr = midCorba.delDevice(devObjectArr);
				if (devObjArr != null && devObjArr.length > 0)
				{
					logger.debug("删除设备调用中间件接口成功！");
					result = devObjArr[0].result_code;
				}
				else
				{
					logger.debug("删除设备调用中间件接口失败！");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
		}
		return result;
	}
	
	/**
	 * 中间件设备添加到域
	 * 
	 * @author qixueqi
	 * @date 5 5, 2010
	 * @param
	 * @return String
	 */
	public int addDeviceCity(String deviceId, String oui, String deviceSn,String cityId){
		int result = 0;
		// 1. 0 设备删除成功
		// 2. 1 服务器连接失败
		// 3. 10001 数据格式错误
		// 4. 10003 设备不存在

		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DevAreaObject[] devAreaObjectArr = new MidWare.DevAreaObject[1];
				devAreaObjectArr[0] = new MidWare.DevAreaObject();
				devAreaObjectArr[0].area_id = cityId;
				devAreaObjectArr[0].deviceObjectArr = new MidWare.DeviceObject[1];
				devAreaObjectArr[0].deviceObjectArr[0] = new MidWare.DeviceObject();
				devAreaObjectArr[0].deviceObjectArr[0].device_id = deviceId;
				devAreaObjectArr[0].deviceObjectArr[0].oui = oui;
				devAreaObjectArr[0].deviceObjectArr[0].device_serialnumber = deviceSn;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DevAreaObjectRep[] devObjArr = midCorba.addAreaDev(devAreaObjectArr);
				if (devObjArr != null && devObjArr.length > 0)
				{
					logger.debug("删除设备调用中间件接口成功！");
					result = devObjArr[0].result_code;
				}
				else
				{
					logger.debug("删除设备调用中间件接口失败！");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
		}
		return result;
	}
	
	/**
	 * 中间件设备添加到域
	 * 
	 * @author qixueqi
	 * @date 5 5, 2010
	 * @param
	 * @return String
	 */
	public int deleteDeviceCity(String deviceId, String oui, String deviceSn,String cityId){
		int result = 0;
		// 1. 0 设备删除成功
		// 2. 1 服务器连接失败
		// 3. 10001 数据格式错误
		// 4. 10003 设备不存在
		
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DevAreaObject[] devAreaObjectArr = new MidWare.DevAreaObject[1];
				devAreaObjectArr[0] = new MidWare.DevAreaObject();
				devAreaObjectArr[0].area_id = cityId;
				devAreaObjectArr[0].deviceObjectArr = new MidWare.DeviceObject[1];
				devAreaObjectArr[0].deviceObjectArr[0] = new MidWare.DeviceObject();
				devAreaObjectArr[0].deviceObjectArr[0].device_id = deviceId;
				devAreaObjectArr[0].deviceObjectArr[0].oui = oui;
				devAreaObjectArr[0].deviceObjectArr[0].device_serialnumber = deviceSn;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DevAreaObjectRep[] devObjArr = midCorba.delAreaDev(devAreaObjectArr);
				if (devObjArr != null && devObjArr.length > 0)
				{
					logger.debug("删除设备调用中间件接口成功！");
					result = devObjArr[0].result_code;
				}
				else
				{
					logger.debug("删除设备调用中间件接口失败！");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
		}
		return result;
	}

	/**
	 * @return the midWareDAO
	 */
	public MidWareDAO getMidWareDAO() {
		return midWareDAO;
	}

	/**
	 * @param midWareDAO the midWareDAO to set
	 */
	public void setMidWareDAO(MidWareDAO midWareDAO) {
		this.midWareDAO = midWareDAO;
	}
	
}
