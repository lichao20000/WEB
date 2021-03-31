package action.hgwip;
import static action.cst.AJAX;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.resource.ItvConfigBIO;
import dao.hgwip.SubnetOperationDAO;


public class BatchModifyIPAction
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ItvConfigBIO.class);
	private SubnetOperationDAO sod;
	private String ajax;
	private String modcityid;//所属地市
	private String modcountry;//所属县
	private String allot;//分配时间
	private String modu1;//用途1
	private String modu2;//用途2
	private String modu3;//用途3
	private String remark;//备注:
	private String chk;//所选中的设备
	public String execute() throws Exception {
		logger.debug("==========getData===============");
		logger.debug("chk="+chk);
		logger.debug("cityid="+modcityid);
		logger.debug("SelCCity="+modcountry);
		logger.debug("used1="+modu1);
		logger.debug("used2="+modu2);
		logger.debug("used3="+modu3);
		logger.debug("remark="+remark);
		ArrayList list=new ArrayList();
		HashMap map=new HashMap();
		ajax="";
		//处理map
		if(null!=chk && !"".equals(chk)){
			String tmp[]=chk.split(",");
			for(int i=0;i<tmp.length;i++){
				map.put("subnet",(String)tmp[i].split("/")[0]);
				map.put("inetmask",(String)tmp[i].split("/")[1]);
				map.put("subnetgrp",(String)tmp[i].split("/")[2]);
				map.put("city_id",modcityid);
				map.put("country",modcountry);
				map.put("purpose1",modu1);
				map.put("purpose2",modu2);
				map.put("purpose3",modu3);
				map.put("subnetcomment",remark);
				list.add(map);
				map=new HashMap();
			}
			//logger.debug(list);
			ArrayList getList=sod.batchModifyToCitySubnet(list);
			//logger.debug(getList);
			if(getList.size()==0){
				ajax="IP配置成功！";
			}else{
				for(int i=0;i<getList.size();i++){
					ajax+="第"+(i+1)+"条记录"+(String)tmp[i]+"配置不成功-";
				}
			}
			list=null;
		}
		
		return AJAX;
	}
	public String getAjax()
	{
		return ajax;
	}
	public void setSod(SubnetOperationDAO sod)
	{
		this.sod = sod;
	}
	public void setModcityid(String modcityid)
	{
		this.modcityid = modcityid;
	}
	public void setModcountry(String modcountry)
	{
		// 通过ajax传递中文，需要将字符集合转码的。
		try
			{
				this.modcountry = java.net.URLDecoder.decode(modcountry, "UTF-8");
			} catch (Exception e)
			{
				this.modcountry = modcountry;
			}
	}
	public void setAllot(String allot)
	{
		this.allot = allot;
	}
	public void setModu1(String modu1)
	{
		// 通过ajax传递中文，需要将字符集合转码的。
		try
			{
				this.modu1 = java.net.URLDecoder.decode(modu1, "UTF-8");
			} catch (Exception e)
			{
				this.modu1 = modu1;
			}
	}
	public void setModu2(String modu2)
	{
		// 通过ajax传递中文，需要将字符集合转码的。
		try
			{
				this.modu2 = java.net.URLDecoder.decode(modu2, "UTF-8");
			} catch (Exception e)
			{
				this.modu2 = modu2;
			}
	}
	public void setModu3(String modu3)
	{
		// 通过ajax传递中文，需要将字符集合转码的。
		try
			{
				this.modu3 = java.net.URLDecoder.decode(modu3, "UTF-8");
			} catch (Exception e)
			{
				this.modu3 = modu3;
			}
	}
	public void setRemark(String remark)
	{
		// 通过ajax传递中文，需要将字符集合转码的。
		try
			{
				this.remark = java.net.URLDecoder.decode(remark, "UTF-8");
			} catch (Exception e)
			{
				this.remark = remark;
			}
	}
	public void setChk(String chk)
	{
		this.chk = chk;
	}
}
