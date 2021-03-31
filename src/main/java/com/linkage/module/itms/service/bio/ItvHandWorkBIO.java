package com.linkage.module.itms.service.bio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.itms.service.dao.ItvHandWorkDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-4-26
 * @category com.linkage.module.itms.service.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class ItvHandWorkBIO
{
	public static Logger logger = LoggerFactory.getLogger(ItvHandWorkBIO.class);
	private ItvHandWorkDAO dao;
	private String spPara = "=";
	public String doBusiness(String itvInf,String starttime)
	{
		
		logger.warn("doBusiness()====>");
		StringBuffer hand=new StringBuffer();
		
		String[] itvArr = itvInf.split(spPara);
		
		hand.append(itvArr[0])
		.append("|||")
		.append(LipossGlobals.getLipossProperty("ITVTerminal.authUser"))
		//.append("abcdef")
		.append("|||")
		.append(LipossGlobals.getLipossProperty("ITVTerminal.authPwd"))
		//.append("123456")
		.append("|||")
		.append(LipossGlobals.getLipossProperty("ITVTerminal.servTypeId"))
		//.append("21")
		.append("|||")
		.append(LipossGlobals.getLipossProperty("ITVTerminal.operateId"))
		//.append("4")
		.append("|||")
		.append(starttime)
		.append("|||")
		.append(itvArr[1])
		.append("|||")
		.append(itvArr[2])
		.append("|||")
		.append(itvArr[3])
		.append("|||")
		.append(itvArr[4])
		.append("|||")
		.append(itvArr[5])
		.append("|||")
		.append(itvArr[6])
		.append("|||")
		.append(itvArr[7])
		.append("|||")
		.append(itvArr[8])
		.append("|||")
		.append(itvArr[9])
		.append("|||")
		.append(itvArr[10])
		.append("|||")
		.append(itvArr[11])
		.append("|||")
		.append(itvArr[12])
		.append("|||")
		.append(itvArr[13])
		.append("|||")
		.append(itvArr[14])
		.append("|||")
		.append(itvArr[15])
		.append("|||")
		.append(itvArr[16])
		.append("|||")
		.append(itvArr[17])
		.append("|||")
		.append(itvArr[18])
		.append("LINKAGE");
		logger.warn("hand==="+hand.toString());
		return this.sendSheet(hand.toString());
	}

	public String sendSheet(String hand)
	{
		String    receivedStrUser="";
		String 	StrUser="";
		logger.debug("sendSheet({})", hand);
		if(StringUtil.IsEmpty(hand))
		{
			logger.warn("sendSheet is null");
			return null;
		}
		String server=LipossGlobals.getLipossProperty("ITVTerminal.socketIP");
		int port = Integer.valueOf( LipossGlobals.getLipossProperty("ITVTerminal.socketpwd"));
		   receivedStrUser = SocketUtil.sendStrMesg(server,port,hand.toString()+ "\n");
		   String[] receive=receivedStrUser.split("\\|\\|\\|");
			StrUser=receive[receive.length-1].replace("<RETNMSG>", "").replace("</RETNMSG>", "");
		if(!StringUtil.IsEmpty(StrUser))
		{
			receivedStrUser=StrUser;
		}
		else
		{
			receivedStrUser="成功";
		}
		return receivedStrUser;
	}
	public ItvHandWorkDAO getDao()
	{
		return dao;
	}

	
	public void setDao(ItvHandWorkDAO dao)
	{
		this.dao = dao;
	}


	
	public String getSpPara()
	{
		return spPara;
	}


	
	public void setSpPara(String spPara)
	{
		this.spPara = spPara;
	}
	
}
