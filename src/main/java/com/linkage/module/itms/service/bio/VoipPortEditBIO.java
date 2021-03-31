package com.linkage.module.itms.service.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.service.dao.VoipPortEditDAO;
public class VoipPortEditBIO
{
  private static Logger logger = LoggerFactory.getLogger(VoipPortEditBIO.class);
  private VoipPortEditDAO dao;

  public List<Map> getAllVoipInfo(int curPage_splitPage, int num_splitPage,String dev_sn,String loid,String gwType) {
		  return dao.getAllVoipInfo(curPage_splitPage, num_splitPage, dev_sn, loid,gwType);
	}
  
  public int getAllVoipInfoCount(String dev_sn,String loid,String gwType,int curPage_splitPage, int num_splitPage) {
	  return dao.getAllVoipInfoCount(dev_sn, loid, gwType, curPage_splitPage, num_splitPage);
}
  
  public String changeVoipPort(String userId,String gw_type,String lineId,String tmpPort,String oldLine){
	    logger.warn("checkIsUsed ： {},{},{},{},{}",new Object[]{userId,lineId,gw_type,tmpPort,oldLine});
	  String ret = dao.checkIsUsed(userId, lineId, gw_type);
	  if(StringUtil.IsEmpty(ret)){
		  ret = dao.changeVoipPort(userId, gw_type, lineId, tmpPort,oldLine);
	  }
	  return ret;
  }
  
  public VoipPortEditDAO getDao()
  {
    return this.dao;
  }

  public void setDao(VoipPortEditDAO dao)
  {
    this.dao = dao;
  }
 
}