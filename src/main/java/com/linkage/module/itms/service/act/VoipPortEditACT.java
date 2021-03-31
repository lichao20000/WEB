package com.linkage.module.itms.service.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.service.bio.VoipPortEditBIO;

import action.splitpage.splitPageAction;

public class VoipPortEditACT extends splitPageAction implements ServletRequestAware 
{
  
  private static final long serialVersionUID = -5026642328812387613L;
  public static Logger logger = LoggerFactory.getLogger(VoipPortEditACT.class);
  private String ajax = null;
  private List<Map> data =null;
  private String dev_sn;
  private String loid;
  private String gw_type;
  private String userId;
  private String lineId;
  private String oldLine;
  private String voipPort; //查询出来的语音口
  private VoipPortEditBIO bio;
  
  public String execute()
  {
	data = bio.getAllVoipInfo(curPage_splitPage, num_splitPage, dev_sn, loid,gw_type);
	maxPage_splitPage = bio.getAllVoipInfoCount(dev_sn, loid, gw_type, curPage_splitPage, num_splitPage);
    return "success";
  }
  

  public String changeVoipPort()
  {
   this.ajax = this.bio.changeVoipPort(userId, gw_type, lineId, voipPort,oldLine);
   return "ajax";
  }
 
  public String getAjax()
  {
    return this.ajax;
  }

  public void setAjax(String ajax)
  {
    this.ajax = ajax;
  }


  public VoipPortEditBIO getBio()
  {
    return this.bio;
  }

  public void setBio(VoipPortEditBIO bio)
  {
    this.bio = bio;
  }

public List<Map> getData()
{
	return data;
}
public void setData(List<Map> data)
{
	this.data = data;
}

public String getDev_sn()
{
	return dev_sn;
}



public void setDev_sn(String dev_sn)
{
	this.dev_sn = dev_sn;
}



public String getLoid()
{
	return loid;
}



public void setLoid(String loid)
{
	this.loid = loid;
}


@Override
public void setServletRequest(HttpServletRequest arg0)
{
	// TODO Auto-generated method stub
	
}


public String getGw_type()
{
	return gw_type;
}


public void setGw_type(String gw_type)
{
	this.gw_type = gw_type;
}


public String getUserId()
{
	return userId;
}



public void setUserId(String userId)
{
	this.userId = userId;
}



public String getLineId()
{
	return lineId;
}



public void setLineId(String lineId)
{
	this.lineId = lineId;
}



public String getVoipPort()
{
	return voipPort;
}



public void setVoipPort(String voipPort)
{
	this.voipPort = voipPort;
}


public String getOldLine()
{
	return oldLine;
}


public void setOldLine(String oldLine)
{
	this.oldLine = oldLine;
}

}