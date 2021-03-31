package com.linkage.module.gtms.stb.resource.serv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.uss.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.SoftVersionDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("rawtypes")
public class SoftVersionBIO
{
	private static Logger logger = LoggerFactory.getLogger(SoftVersionBIO.class);
	private SoftVersionDAO dao;
	
	private static Map<String,String> netTypeMap=new HashMap<String,String>();
	static{
		netTypeMap.put("public_net","公 网");
		netTypeMap.put("private_net","专 网");
		netTypeMap.put("unknown_net","未 知");
	}
	
	 /**
	  * 获取最大页数
	  */
    public int getCountVersion(int curPage_splitPage,int num_splitPage,String vendorId,String versionName)
    {
        return dao.getCountVersion(curPage_splitPage,num_splitPage,vendorId,versionName);
    }

    /**
     * 获取版本文件路径数据
     */
    public List getVersion(int curPage_splitPage,int num_splitPage,String vendorId,String versionName)
    {
        return dao.getVersion(curPage_splitPage,num_splitPage,vendorId,versionName);
    }

    /**
     * 获取所有厂商
     */
    public List getVendor()
    {
        return dao.getVendor();
    }

    /**
	 * 查询设备型号
	 */
	public String getDeviceModel(String vendorId)
    {
        List list = dao.getDeviceModel(vendorId);
        StringBuffer bf = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
        {
            Map map = (Map)list.get(i);
            if(i > 0){
            	bf.append("#");
            }
            bf.append(map.get("device_model_id"));
            bf.append("$");
            bf.append(map.get("device_model"));
        }

        return bf.toString();
    }

	/**
	 * 新增版本
	 */
    public String addVersion(long accoid, String vendorId, String versionDesc, String versionPath,
    		String versionName,String deviceModelId,String fileSize,String MD5,String epg_version,String net_type)
    {
    	logger.debug("addVersion:[{}]",versionPath);
        int ier = dao.addVersion(accoid,vendorId,versionDesc,versionPath,versionName,
        							deviceModelId,fileSize,MD5,epg_version,net_type);
        if(ier == 1)
            return "1,添加记录成功！";
        else
            return "0,添加记录失败！";
    }

    /**
     * 编辑版本
     */
    public String editVersion(String id,String vendorId,String versionDesc,String versionPath,
    		String versionName,String deviceModelId,String fileSize,String MD5,
    		String epg_version,String net_type)
    {
    	logger.debug("editVersion:[{}]",id);
        int ier = dao.editVersion(id,vendorId,versionDesc,versionPath,versionName,
        		deviceModelId,fileSize,MD5,epg_version,net_type);
        if(ier == 1)
            return "1,修改记录成功！";
        else
            return "0,修改记录失败！";
    }
    
    /**
     *删除版本 
     */
    public String deleteVersion(String id)
    {
    	logger.debug("deleteVersion:[{}]",id);
        int ier = dao.deleteVersion(id);
        if(ier == 1)
            return "1,删除记录成功！";
        else
            return "0,删除记录失败！";
    }

    /**
	 * 查询版本详细信息
	 */
    @SuppressWarnings("unchecked")
	public Map<String,String> getSoftVersionDetail(String id) 
    {
		List list=dao.getSoftVersionDetail(id);
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty())
		{
			String device_models=dao.getDeviceModelById(id);
			
			Map<String,String> m=(Map<String,String>) list.get(0);
			//id,acc_loginname,vendor_name,version_desc,version_name,
			//version_path,add_time,update_time,file_size,md5,epg_version,net_type
			map.put("id",StringUtil.getStringValue(m,"id",""));
			map.put("acc_loginname", StringUtil.getStringValue(m,"acc_loginname",""));
			map.put("vendor_name", StringUtil.getStringValue(m,"vendor_name",""));
			map.put("device_models",device_models.split("#")[0]);
			map.put("version_desc", StringUtil.getStringValue(m,"version_desc",""));
			map.put("version_name", StringUtil.getStringValue(m,"version_name",""));
			map.put("version_path", StringUtil.getStringValue(m,"version_path",""));
			map.put("file_size", StringUtil.getStringValue(m,"file_size",""));
			map.put("md5", StringUtil.getStringValue(m,"md5",""));
			map.put("epg_version", StringUtil.getStringValue(m,"epg_version",""));
			map.put("net_type", netTypeMap.get(StringUtil.getStringValue(m,"net_type","")));
			
			map.put("add_time", transDate(StringUtil.getLongValue(m,"add_time",0)));
			map.put("update_time", transDate(StringUtil.getLongValue(m,"update_time",0)));
		}
		
		return map;
	}
    
    /**
	 *日期转换
	 */
	private static final String transDate(long seconds)
	{
		if (seconds>0)
		{
			try{
				return new DateTimeUtil(seconds * 1000).getLongDate();
			}catch (Exception e){
				logger.error("transDate err:"+e);
				e.printStackTrace();
			}
		}
		return "";
	}
    
	public SoftVersionDAO getDao() {
		return dao;
	}

	public void setDao(SoftVersionDAO dao) {
		this.dao = dao;
	}

}