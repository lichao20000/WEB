package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.service.dao.BssSheetByHand4NXLTDAO;
import com.linkage.module.itms.service.obj.SheetObj;

public class BssSheetByHand4NXLTBIO
{
  private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4NXLTBIO.class);
  private BssSheetByHand4NXLTDAO dao;
  private static final String endPointReference = LipossGlobals.getLipossProperty("webServiceUri");

  public List<HashMap<String, String>> getSpecId() {
		List<HashMap<String, String>> list= dao.getSpecId();
		List<HashMap<String, String>> lis=new ArrayList<HashMap<String, String>>();
		if(list!=null){
			for(Map<String, String> map:list){
				HashMap<String, String> m=new HashMap<String, String>();
				String lNum=StringUtil.getStringValue(map, "lan_num", "");
				String wNum=StringUtil.getStringValue(map, "wlan_num", "");
				String spec_name=StringUtil.getStringValue(map, "spec_name", "");
				String id=StringUtil.getStringValue(map, "id", "");
				
				m.put("spec_name", spec_name);
				m.put("spec",lNum+"|"+wNum+"|"+id+"("+spec_name+")");
				 
				lis.add(m);
				m=null;
				map=null;
			}
		}
		return lis;
	}
  
  public SheetObj checkLoid(String loid, String userType)
  {
    boolean result = this.dao.isLoidExists(loid, userType);
    if (result)
    {
      SheetObj obj = new SheetObj();
      obj.setUserType(userType);
      Map userInfo = this.dao.getUserInfo(loid, userType);
      userInfoToObj(userInfo, obj, userType);
      List list = this.dao.getServInfo(loid, userType);
      servInfoToObj(list, obj, userType);
      return obj;
    }

    return null;
  }
  
  
  public SheetObj checkStbLoid(String loid)
  {
    boolean result = this.dao.isLoidStbExists(loid);
    if (result)
    {
      SheetObj obj = new SheetObj();
      Map userInfo = this.dao.getStbUserInfo(loid);
      stbUserInfoToObj(userInfo, obj);
      return obj;
    }

    return null;
  }
  
  

  private void servInfoToObj(List<HashMap<String, String>> list, SheetObj obj, String userType)
  {
    for (HashMap hashMap : list)
    {
      if ("10".equals(hashMap.get("serv_type_id")))
      {
        obj.setNetServTypeId("22");
        obj.setNetOperateId("2");
        obj.setNetOltFactory(StringUtil.getStringValue(hashMap, "oltfactory", ""));
        obj.setNetUsername(StringUtil.getStringValue(hashMap, "username", ""));
        obj.setNetPasswd(StringUtil.getStringValue(hashMap, "passwd", ""));
        obj.setNetVlanId((String)hashMap.get("vlanid"));
        obj.setNetWanType((String)hashMap.get("wan_type"));
        obj.setNetPort(StringUtil.getStringValue(hashMap, "bind_port", ""));
        obj.setNetIpaddress(StringUtil.getStringValue(hashMap, "ipaddress", ""));
        obj.setNetIpmask(StringUtil.getStringValue(hashMap, "ipmask", ""));
        obj.setNetGateway(StringUtil.getStringValue(hashMap, "gateway", ""));
        obj.setNetIpdns(StringUtil.getStringValue(hashMap, "adsl_ser", ""));
        obj.setStandNetIpdns(getIpTypeReverse(StringUtil.getStringValue(hashMap, "ip_type", "")));
        obj.setNetSpeed(StringUtil.getStringValue(hashMap, "speed", ""));
        logger.warn("obj=" + obj.toString());
      }

      if ("14".equals(hashMap.get("serv_type_id")))
      {
        Map paramInfo = this.dao.getVoipParaInfo((String)hashMap.get("user_id"), userType, "14");

        if ((paramInfo != null) && ("2".equals(paramInfo.get("protocol"))))
        {
          obj.setHvoipServTypeId("15");
          obj.setHvoipOperateId("2");
          obj.setHvoipOltFactory(StringUtil.getStringValue(hashMap, "oltfactory", ""));
          obj.setHvoipPhone(StringUtil.getStringValue(paramInfo, "voip_phone", ""));
          obj.setHvoipRegId(StringUtil.getStringValue(paramInfo, "reg_id", ""));
          obj.setHvoipRegIdType(StringUtil.getStringValue(paramInfo, "reg_id_type", ""));
          obj.setHvoipMgcIp(StringUtil.getStringValue(paramInfo, "prox_serv", ""));
          obj.setHvoipMgcPort(StringUtil.getStringValue(paramInfo, "prox_port", ""));
          obj.setHvoipStandMgcIp(StringUtil.getStringValue(paramInfo, "stand_prox_serv", ""));
          obj.setHvoipStandMgcPort(StringUtil.getStringValue(paramInfo, "stand_prox_port", ""));
          obj.setHvoipPort(StringUtil.getStringValue(paramInfo, "voip_port", ""));
          obj.setHvoipVlanId(StringUtil.getStringValue(hashMap, "vlanid", ""));
          obj.setHvoipWanType(StringUtil.getStringValue(hashMap, "wan_type", ""));
          obj.setHvoipIpaddress(StringUtil.getStringValue(hashMap, "ipaddress", ""));
          obj.setHvoipIpmask(StringUtil.getStringValue(hashMap, "ipmask", ""));
          obj.setHvoipGateway(StringUtil.getStringValue(hashMap, "gateway", ""));
          obj.setHvoipIpdns(StringUtil.getStringValue(hashMap, "adsl_ser", ""));
          obj.setHvoipEid(StringUtil.getStringValue(hashMap, "eid", ""));
          obj.setVoipPwd(StringUtil.getStringValue(hashMap, "voip_passwd", ""));
        }

        if ((paramInfo != null) && (("0".equals(paramInfo.get("protocol"))) || ("1".equals(paramInfo.get("protocol")))))
        {
          obj.setSipServTypeId("14");
          obj.setSipOperateId("2");
          obj.setSipVoipPhone(StringUtil.getStringValue(paramInfo, "voip_phone", ""));
          obj.setSipVoipUsername(StringUtil.getStringValue(paramInfo, "voip_username", ""));
          obj.setSipVoipPwd(StringUtil.getStringValue(paramInfo, "voip_passwd", ""));
          obj.setSipProxServ(StringUtil.getStringValue(paramInfo, "prox_serv", ""));
          obj.setSipProxPort(StringUtil.getStringValue(paramInfo, "prox_port", ""));
          obj.setSipStandProxServ(StringUtil.getStringValue(paramInfo, "stand_prox_serv", ""));
          obj.setSipStandProxPort(StringUtil.getStringValue(paramInfo, "stand_prox_port", ""));
          obj.setSipVoipPort(StringUtil.getStringValue(paramInfo, "voip_port", ""));
          obj.setSipRegiServ(StringUtil.getStringValue(paramInfo, "regi_serv", ""));
          obj.setSipRegiPort(StringUtil.getStringValue(paramInfo, "regi_port", ""));
          obj.setSipStandRegiServ(StringUtil.getStringValue(paramInfo, "stand_regi_serv", ""));
          obj.setSipStandRegiPort(StringUtil.getStringValue(paramInfo, "stand_regi_port", ""));
          obj.setSipOutBoundProxy(StringUtil.getStringValue(paramInfo, "out_bound_proxy", ""));
          obj.setSipOutBoundPort(StringUtil.getStringValue(paramInfo, "out_bound_port", ""));
          obj.setSipStandOutBoundProxy(StringUtil.getStringValue(paramInfo, "stand_out_bound_proxy", ""));
          obj.setSipStandOutBoundPort(StringUtil.getStringValue(paramInfo, "stand_out_bound_port", ""));
          obj.setSipProtocol(StringUtil.getStringValue(paramInfo, "protocol", ""));

          obj.setSipVlanId(StringUtil.getStringValue(hashMap, "vlanid", ""));
          obj.setSipWanType(StringUtil.getStringValue(hashMap, "wan_type", ""));
          obj.setSipIpaddress(hashMap.get("ipaddress") == null ? "" : (String)hashMap.get("ipaddress"));
          obj.setSipIpmask(hashMap.get("ipmask") == null ? "" : (String)hashMap.get("ipmask"));
          obj.setSipGateway(hashMap.get("gateway") == null ? "" : (String)hashMap.get("gateway"));
          obj.setSipIpdns(hashMap.get("adsl_ser") == null ? "" : (String)hashMap.get("adsl_ser"));
          obj.setVoipPwd(StringUtil.getStringValue(hashMap, "voip_passwd", ""));
          if ("0".equals(paramInfo.get("protocol")))
          {
            obj.setSipVoipUri((String)paramInfo.get("uri"));
            obj.setSipUserAgentDomain((String)paramInfo.get("user_agent_domain"));
          }
        }
      }

      if ("11".equals(hashMap.get("serv_type_id")))
      {
        obj.setIptvServTypeId("21");
        obj.setIptvOperateId("2");
        obj.setIptvOltFactory(StringUtil.getStringValue(hashMap, "oltfactory", ""));
        obj.setIptvWanType((String)hashMap.get("wan_type"));
        obj.setIptvUserName(StringUtil.getStringValue(hashMap, "username", ""));
        obj.setIptvPasswd(StringUtil.getStringValue(hashMap, "passwd", ""));
        obj.setIptvPort(StringUtil.getStringValue(hashMap, "bind_port", ""));
        obj.setIptvNum(StringUtil.getStringValue(hashMap, "serv_num", ""));
        obj.setIptvVlanId(StringUtil.getStringValue(hashMap, "vlanid", ""));
        obj.setMulticastVlan(StringUtil.getStringValue(hashMap, "multicast_vlanid", ""));
        obj.setIptvDestIp(StringUtil.getStringValue(hashMap, "ipaddress", ""));
        obj.setIptvDestMark(StringUtil.getStringValue(hashMap, "ipmask", ""));
      }
    }
  }

  private String getIpTypeReverse(String ipType)
  {
    String result = "0";

    if (!StringUtil.IsEmpty(ipType))
    {
      if ("1".equals(ipType))
      {
        result = "0";
      }
      if ("3".equals(ipType))
      {
        result = "1";
      }
      if ("2".equals(ipType))
      {
        result = "5";
      }
    }

    return result;
  }

  private void userInfoToObj(Map<String, String> userInfo, SheetObj obj, String userType)
  {
    obj.setUserOperateId("2");
    obj.setCityId(StringUtil.getStringValue(userInfo, "city_id", ""));
    obj.setOfficeId(StringUtil.getStringValue(userInfo, "office_id", ""));
    obj.setAreaId(StringUtil.getStringValue(userInfo, "zone_id", ""));
    obj.setAccessStyle(StringUtil.getStringValue(userInfo, "access_style_id", ""));
    obj.setLinkman(StringUtil.getStringValue(userInfo, "linkman", ""));
    obj.setLinkPhone(StringUtil.getStringValue(userInfo, "linkphone", ""));
    obj.setEmail(StringUtil.getStringValue(userInfo, "email", ""));
    obj.setMobile(StringUtil.getStringValue(userInfo, "mobile", ""));
    obj.setLinkAddress(StringUtil.getStringValue(userInfo, "linkaddress", ""));
    obj.setLinkmanCredno(StringUtil.getStringValue(userInfo, "credno", ""));
    obj.setDeviceType(StringUtil.getStringValue(userInfo, "type_id", "1"));
    
    //终端规格
	String id=StringUtil.getStringValue(userInfo, "spec_id", "");
	String specId = "";
	Map<String,String> map= dao.getSpecId(id);
	if(null != map && !map.isEmpty()){
		String lNum=StringUtil.getStringValue(map, "lan_num", "");
		String wNum=StringUtil.getStringValue(map, "wlan_num", "");
		String spec_name=StringUtil.getStringValue(map, "spec_name", "");
		specId = lNum+","+wNum+","+id+"("+spec_name+")";
	}
	logger.warn("specId:{}",specId);
    obj.setSpecId(StringUtil.getStringValue(specId));
    
    if ("2".equals(userType))
    {
      obj.setCustomerId(StringUtil.getStringValue(userInfo, "customer_id", ""));
      obj.setCustomerAccount(StringUtil.getStringValue(userInfo, "linkman", ""));
    }
  }
  
  private void stbUserInfoToObj(Map<String, String> userInfo, SheetObj obj)
  {
    obj.setCityId(StringUtil.getStringValue(userInfo, "city_id", ""));
    obj.setStbUserID(StringUtil.getStringValue(userInfo, "serv_account", ""));
    obj.setStbUserPwd(StringUtil.getStringValue(userInfo, "serv_pwd", ""));
    obj.setStbNTP1(StringUtil.getStringValue(userInfo, "ntp_server1", ""));
    obj.setStbNTP2(StringUtil.getStringValue(userInfo, "ntp_server2", ""));
    obj.setStbBrowserURL1(StringUtil.getStringValue(userInfo, "browser_url1", ""));
  }
  
  

  public BssSheetByHand4NXLTDAO getDao()
  {
    return this.dao;
  }

  public void setDao(BssSheetByHand4NXLTDAO dao)
  {
    this.dao = dao;
  }

  public String delBusiness(SheetObj obj, UserRes curUser)
  {
    logger.debug("BssSheetByHand4NXLTBIO.delBusiness(SheetObj obj)");
    String resultMessage = "";
    StringBuffer buffer = new StringBuffer();
    if ((StringUtil.IsEmpty(obj.getHvoipServTypeId())) && (StringUtil.IsEmpty(obj.getNetServTypeId())) && (StringUtil.IsEmpty(obj.getIptvServTypeId()))) {
      resultMessage = delUserInfo(obj);
      buffer.append("资料工单销户处理结果：" + getMessage(resultMessage, "resultDes"));
    }
    else
    {
      try {
        Thread.sleep(1000L);
        if (!StringUtil.IsEmpty(obj.getHvoipServTypeId()))
        {
          String voipResult = delH248VoipSheet(obj);
          buffer.append("\nH248语音销户工单处理结果：" + getMessage(voipResult, "resultDes"));
        }
        if (!StringUtil.IsEmpty(obj.getNetServTypeId()))
        {
          String netResult = delNetSheetResult(obj);
          buffer.append("\n宽带业务销户工单处理结果：" + getMessage(netResult, "resultDes"));
        }
        if (!StringUtil.IsEmpty(obj.getIptvServTypeId()))
        {
          String itvResult = delItvSheetResult(obj);
          buffer.append("\nITV业务销户工单处理结果：" + getMessage(itvResult, "resultDes"));
        }
      }
      catch (Exception e)
      {
        logger.error("页面发送工单失败：" + e.getMessage());
      }
    }
    return buffer.toString();
  }
  
  
  
  
  
  
  public String delStbBusiness(SheetObj obj, UserRes curUser)
  {
    logger.debug("BssSheetByHand4AHBIO.delStbBusiness(SheetObj obj)");
    StringBuffer buffer = new StringBuffer();
    
  try {
    if (!StringUtil.IsEmpty(obj.getStbServ()))
    {
    	String stbResult = delStbSheet(obj);
    	buffer.append("机顶盒销户工单处理结果：" + getMessage(stbResult, "resultDes"));
    	this.dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getStbServ()).intValue(), 3, 
    			"000".equals(getMessage(stbResult, "resultCode")) ? 1 : 0, getMessage(stbResult, "resultDes"));
    }
  }
  catch (Exception e)
  {
    logger.error("页面发送工单失败：" + e.getMessage());
  }
    
    return buffer.toString();
  }
  
  
  
  
  

  private String delUserInfo(SheetObj order)
  {
    logger.debug("delUserInfo ==> 方法开始：{}", order);

    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText("1");

    root.addElement("authPwd").addText("1");

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText("3");

    root.addElement("servTypeId").addText("20");

    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("del user xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }

  private String delNetSheetResult(SheetObj order)
  {
    logger.debug("delNetSheetResult ==> 方法开始：{}", order);

    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText("1");

    root.addElement("authPwd").addText("1");

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText("3");

    root.addElement("servTypeId").addText("22");
    param.addElement("userName").addText("");
    param.addElement("wanType").addText(order.getNetWanType());

    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("del net xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }

  private String delItvSheetResult(SheetObj order)
  {
    logger.debug("delUserInfo ==> 方法开始：{}", order);

    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText("1");

    root.addElement("authPwd").addText("1");

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText("3");

    root.addElement("servTypeId").addText("21");
    param.addElement("userName").addText("");
    param.addElement("wanType").addText(order.getIptvWanType());

    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("del iptv xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }

  private String delH248VoipSheet(SheetObj order)
  {
    logger.debug("delUserInfo ==> 方法开始：{}", order);

    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText("1");

    root.addElement("authPwd").addText("1");

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText("3");

    root.addElement("servTypeId").addText("15");
    param.addElement("userName").addText("");
    param.addElement("voipPhone").addText(order.getHvoipPhone());

    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("del voip xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }

  
  
  
  private String delStbSheet(SheetObj order)
  {
    logger.debug("delStbSheet ==> 方法开始：{}", order);

    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText("1");

    root.addElement("authPwd").addText("1");

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText("3");

    root.addElement("servTypeId").addText("25");
    param.addElement("servaccount").addText(order.getStbServ());
    
    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("del stb xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }
  
  
  
  
  /**
   * 家庭网关开户
   * @param obj
   * @param curUser
   * @return
   */
  public String doBusiness(SheetObj obj, UserRes curUser)
  {
    logger.debug("BssSheetByHand4AHBIO.doBusiness(SheetObj obj)");
    String resultMessage = doUserInfo(obj);
    StringBuffer buffer = new StringBuffer();
    buffer.append("资料工单处理结果：" + getMessage(resultMessage, "resultDes"));
    if ("000".equals(getMessage(resultMessage, "resultCode")))
	{
	    try
	    {
	      Thread.sleep(1000L);
	
	      if (!StringUtil.IsEmpty(obj.getHvoipServTypeId()))
	      {
	        String voipResult = doH248VoipSheet(obj);
	        buffer.append(";\nH248语音开户工单处理结果：" + getMessage(voipResult, "resultDes"));
	      }
	      if (!StringUtil.IsEmpty(obj.getNetServTypeId()))
	      {
	        String netResult = doNetSheetResult(obj);
	        buffer.append(";\n宽带业务开户工单处理结果：" + getMessage(netResult, "resultDes"));
	      }
	      if (!StringUtil.IsEmpty(obj.getIptvServTypeId()))
	      {
	        String itvResult = doItvSheetResult(obj);
	        buffer.append(";\nITV业务开户工单处理结果：" + getMessage(itvResult, "resultDes"));
	      }
	    }
	    catch (Exception e)
	    {
	      logger.error("页面发送工单失败：" + e.getMessage());
	    }
	}

    return buffer.toString();
  }

  private String doItvSheetResult(SheetObj order)
  {
    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText("1");

    root.addElement("authPwd").addText("1");

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText("1");
    param.addElement("orderNo").addText(order.getDealDate());

    root.addElement("servTypeId").addText("21");

    if ("1".equals(order.getIptvWanType())) {
      //param.addElement("oltFactory").addText(order.getIptvOltFactory());
      param.addElement("wanType").addText("1");

      /*if ("243".equals(order.getIptvOltFactory())) {
        param.addElement("vlanId").addText("9");
      }
      else {
        param.addElement("vlanId").addText(order.getIptvVlanId());
      }*/
      param.addElement("vlanId").addText(order.getIptvVlanId());
      param.addElement("multicastVlan").addText(order.getMulticastVlan());

      param.addElement("userName").addText("");
      param.addElement("password").addText("");
      param.addElement("iptvPort").addText(order.getIptvPort());
      param.addElement("ipaddress").addText("");
      param.addElement("ipmask").addText("");

      param.addElement("iptvNum").addText("1");
      param.addElement("gateway").addText("");
      param.addElement("ipdns").addText("");
    }
    else if ("2".equals(order.getIptvWanType()))
    {
      //param.addElement("oltFactory").addText(order.getIptvOltFactory());
      param.addElement("wanType").addText("2");

      /*if ("243".equals(order.getIptvOltFactory())) {
        param.addElement("vlanId").addText("9");
      }
      else {
        param.addElement("vlanId").addText(order.getIptvVlanId());
      }*/
      param.addElement("vlanId").addText(order.getIptvVlanId());
      param.addElement("multicastVlan").addText(order.getMulticastVlan());

      param.addElement("userName").addText(order.getIptvUserName());
      param.addElement("password").addText(order.getIptvPasswd());
      param.addElement("ipaddress").addText(order.getIptvDestIp());
      param.addElement("ipmask").addText(order.getIptvDestMark());
      param.addElement("iptvPort").addText("");

      param.addElement("iptvNum").addText("1");
      param.addElement("gateway").addText("");
      param.addElement("ipdns").addText("");
    }

    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("IPTV xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }

  private String getMessage(String xmlStr, String node)
  {
    String result = "";
    Document doc = null;
    try
    {
      doc = DocumentHelper.parseText(xmlStr);
      Element rootElt = doc.getRootElement();
      result = rootElt.elementTextTrim(node);
    }
    catch (Exception e)
    {
      logger.error("exception:" + e.getMessage());
    }
    return result;
  }

  private String doNetSheetResult(SheetObj order)
  {
    logger.debug("doUserInfo(UserInfoSheet4AHOBJ doNetSheetResult)");

    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText("1");

    root.addElement("authPwd").addText("1");

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText("1");
    param.addElement("orderNo").addText(order.getDealDate());

    root.addElement("servTypeId").addText("22");

    if ("1".equals(order.getNetWanType())) {
      //param.addElement("oltFactory").addText(order.getNetOltFactory());
      param.addElement("userName").addText(order.getNetUsername());
      param.addElement("password").addText(order.getNetPasswd());
      param.addElement("speed").addText(order.getNetSpeed());
      param.addElement("wanType").addText("1");

      /*if ("243".equals(order.getNetOltFactory())) {
        param.addElement("vlanId").addText("7");
      }
      else {
        param.addElement("vlanId").addText(order.getNetVlanId());
      }*/
      param.addElement("vlanId").addText(order.getNetVlanId());
      
      param.addElement("ipaddress").addText("");
      param.addElement("ipmask").addText("");
      param.addElement("gateway").addText("");
      param.addElement("ipdns").addText("");
      param.addElement("bindPort").addText(order.getNetPort());
    }
    else if ("2".equals(order.getNetWanType()))
    {
      //param.addElement("oltFactory").addText(order.getNetOltFactory());
      param.addElement("userName").addText(order.getNetUsername());
      param.addElement("password").addText(order.getNetPasswd());
      param.addElement("speed").addText(order.getNetSpeed());
      param.addElement("wanType").addText("2");

      /*if ("243".equals(order.getNetOltFactory())) {
        param.addElement("vlanId").addText("7");
      }
      else {
        param.addElement("vlanId").addText(order.getNetVlanId());
      }*/
      param.addElement("vlanId").addText(order.getNetVlanId());
      
      param.addElement("ipaddress").addText("");
      param.addElement("ipmask").addText("");
      param.addElement("gateway").addText("");
      param.addElement("ipdns").addText("");
      param.addElement("bindPort").addText(order.getNetPort());
    }

    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("NET xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }

  private String doH248VoipSheet(SheetObj order)
  {
    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText(order.getAuthUser());

    root.addElement("authPwd").addText(order.getAuthPwd());

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText(StringUtil.IsEmpty(order.getHvoipOperateId()) ? "1" : order.getHvoipOperateId());
    param.addElement("orderNo").addText(order.getDealDate());

    root.addElement("servTypeId").addText("15");

    param.addElement("mgcIp").addText(order.getHvoipMgcIp());
    param.addElement("mgcPort").addText(order.getHvoipMgcPort());
    param.addElement("standMgcIp").addText(order.getHvoipStandMgcIp());
    param.addElement("standMgcPort").addText(order.getHvoipStandMgcPort());
    param.addElement("vlanId").addText(order.getHvoipVlanId());
    
    param.addElement("regId").addText(order.getHvoipRegId());
    if ("1".equals(order.getHvoipRegIdType())) {
      param.addElement("regIdType").addText("0");
    }
    else if ("2".equals(order.getHvoipRegIdType())) {
      param.addElement("regIdType").addText("1");
    }

    param.addElement("wanType").addText(order.getHvoipWanType());
    param.addElement("ipaddress").addText(order.getHvoipIpaddress());
    param.addElement("ipmask").addText(order.getHvoipIpmask());
    param.addElement("gateway").addText(order.getHvoipGateway());
    param.addElement("ipdns").addText(order.getHvoipIpdns());
    param.addElement("voipPhone").addText(order.getHvoipPhone());
    param.addElement("voipPort").addText(StringUtil.IsEmpty(order.getHvoipPort()) ? "A0" : order.getHvoipPort());
    param.addElement("dscpMark").addText("0");
    param.addElement("deviceName").addText(order.getHvoipIpaddress());
    param.addElement("voipPwd").addText(order.getVoipPwd());

    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("VOIP xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }
  
  
  private String doStbSheet(SheetObj order)
  {
    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText("1");

    root.addElement("authPwd").addText("1");

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText("1");
    param.addElement("orderNo").addText(order.getDealDate());

    root.addElement("servTypeId").addText("25");

    param.addElement("servaccount").addText(order.getStbUserID());
    param.addElement("servpwd").addText(order.getStbUserPwd());

    param.addElement("browserurl1").addText(order.getStbBrowserURL1());
    param.addElement("ntp1").addText(order.getStbNTP1());
    param.addElement("ntp2").addText(order.getStbNTP2());
    param.addElement("mac").addText(order.getLoid());

    param.addElement("stbaccessStyle").addText("1");
    
    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("stb xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("stb结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }
  
  

  private String doUserInfo(SheetObj order)
  {
    logger.debug("doUserInfo(UserInfoSheet4AHOBJ userInfoSheet)");
    Document document = DocumentHelper.createDocument();
    document.setXMLEncoding("UTF-8");
    Element root = document.addElement("root");

    root.addElement("cmdId").addText(order.getDealDate());

    root.addElement("authUser").addText(order.getAuthUser());

    root.addElement("authPwd").addText(order.getAuthPwd());

    Element param = root.addElement("param");
    param.addElement("dealDate").addText(order.getDealDate());
    param.addElement("userType").addText("1");
    param.addElement("loid").addText(order.getLoid());
    param.addElement("cityId").addText(order.getCityId());

    root.addElement("operateId").addText(StringUtil.IsEmpty(order.getUserOperateId()) ? "1" : order.getUserOperateId());

    root.addElement("servTypeId").addText("20");
    param.addElement("officeId").addText(order.getOfficeId());
    param.addElement("areaId").addText(order.getAreaId());
    param.addElement("accessStyle").addText(order.getAccessStyle());
    param.addElement("linkman").addText(order.getAccessStyle());
    param.addElement("linkPhone").addText(order.getLinkPhone());
    param.addElement("email").addText(order.getEmail());
    param.addElement("mobile").addText(order.getMobile());
    param.addElement("linkAddress").addText(order.getLinkAddress());
    param.addElement("linkmanCredno").addText(order.getLinkmanCredno());
    param.addElement("customerId").addText("");
    param.addElement("customerAccount").addText("");
    param.addElement("customerPwd").addText("");
    param.addElement("specId").addText(order.getSpecId());

    String sendXml = document.asXML();
    logger.info("xml:" + sendXml);
    logger.warn("资料 xml:" + sendXml);

    String returnParam = "";
    try
    {
      Service service = new Service();
      Call call = null;
      call = (Call)service.createCall();
      call.setTargetEndpointAddress(new URL(endPointReference));

      call.setOperationName("call");
      returnParam = (String)call.invoke(new Object[] { sendXml });
      logger.warn("结果：" + returnParam);
    }
    catch (Exception e)
    {
      logger.error("err:" + e.getMessage());
    }
    return returnParam;
  }

  public String checkCustomerId(String customerId)
  {
    boolean result = this.dao.isExistCustomer(customerId);
    if (result)
    {
      return "-1";
    }

    return "000";
  }

  public String getServInfo(String loid, String userType, String sipVoipPort)
  {
    Map map = this.dao.getUserInfo(loid, userType);
    String userId = "";
    StringBuffer result = new StringBuffer();
    if ((map != null) && (map.size() > 0))
    {
      userId = StringUtil.getStringValue(map, "user_id");
    }
    String lineId = String.valueOf(getLineId(sipVoipPort));
    List list = this.dao.getServInfoByVoipPort(userId, userType, sipVoipPort, "14");
    if ((list != null) && (list.size() > 0))
    {
      HashMap paramMap = (HashMap)list.get(0);

      if ("2".equals(paramMap.get("protocol")))
      {
        result.append("2").append("|");
        result.append(StringUtil.getStringValue(paramMap, "voip_phone", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "reg_id", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "reg_id_type", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "prox_serv", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "prox_port", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "stand_prox_serv", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "stand_prox_port", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "vlanid", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "wan_type", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "ipaddress", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "ipmask", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "gateway", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "adsl_ser", "")).append("|");
      }
      else
      {
        result.append("2").append("|");
        result.append(StringUtil.getStringValue(paramMap, "voip_phone", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "voip_username", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "voip_passwd", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "prox_serv", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "prox_port", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "stand_prox_serv", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "stand_prox_port", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "regi_serv", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "regi_port", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "stand_regi_serv", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "stand_regi_port", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "out_bound_proxy", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "out_bound_port", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "stand_out_bound_proxy", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "stand_out_bound_port", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "vlanid", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "wan_type", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "ipaddress", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "ipmask", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "gateway", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "adsl_ser", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "uri", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "user_agent_domain", "")).append("|");
        result.append(StringUtil.getStringValue(paramMap, "protocol", ""));
      }
      return result.toString();
    }

    return "-1";
  }

  
  
  /**
   * 机顶盒开户
   * @param obj
   * @param curUser
   * @return
   */
  public String stbDoBusiness(SheetObj obj, UserRes curUser)
  {
    logger.debug("BssSheetByHand4AHBIO.stbDoBusiness(SheetObj obj)");
    StringBuffer buffer = new StringBuffer();
	try
	{
	      if (!StringUtil.IsEmpty(obj.getStbServ()))
	      {
	        String stbResult = doStbSheet(obj);
	        buffer.append("机顶盒开户工单处理结果：" + getMessage(stbResult, "resultDes"));
	        this.dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getStbServ()).intValue(), 1, 
	          "000".equals(getMessage(stbResult, "resultCode")) ? 1 : 0, getMessage(stbResult, "resultDes"));
	      }
    }
    catch (Exception e)
    {
      logger.error("页面发送工单失败：" + e.getMessage());
    }

    return buffer.toString();
  }
  
  
  
  public int getLineId(String linePort)
  {
    logger.debug("getLineId({})", linePort);
    int lineId = 1;

    if (!StringUtil.IsEmpty(linePort))
    {
      lineId = Integer.valueOf(linePort.substring(linePort.length() - 1, linePort
        .length())).intValue() + 
        1;
    }
    return lineId;
  }
}