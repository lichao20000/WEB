
package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.service.dao.BssSheetByHand4SXLTDAO;
import com.linkage.module.itms.service.obj.SXLTSheetObj;

/**
 * @author banyr (Ailk No.)
 * @version 1.0
 * @category com.linkage.module.itms.service.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2018-8-23
 */
public class BssSheetByHand4SXLTBIO {

    private static Logger logger = LoggerFactory
            .getLogger(BssSheetByHand4HBLTBIO.class);
    private BssSheetByHand4SXLTDAO dao;
    private static final String endPointReference = LipossGlobals
            .getLipossProperty("webServiceUri");


    public HashMap checkLoidNew(String loid, String userType) {
        boolean result = this.dao.isLoidExists(loid, userType);
        if (result) {
            SXLTSheetObj userInfoObj = new SXLTSheetObj();
            HashMap hashMap = new HashMap();
            userInfoObj.setUserType(userType);
            Map userInfo = this.dao.getUserInfo(loid, userType);
            userInfoToObj(userInfo, userInfoObj, userType);
            hashMap.put("userInfoObj", userInfoObj);
            List list = this.dao.getServInfo(loid, userType);
            servInfoToObjList(list, userType, hashMap);
            return hashMap;
        }
        return null;
    }


    public SXLTSheetObj checkStbLoid(String loid) {
        boolean result = this.dao.isLoidStbExists(loid);
        if (result) {
            SXLTSheetObj obj = new SXLTSheetObj();
            Map userInfo = this.dao.getStbUserInfo(loid);
            stbUserInfoToObj(userInfo, obj);
            return obj;
        }
        return null;
    }


    private void servInfoToObjList(List<HashMap<String, String>> list, String userType, HashMap serInfoMap) {
        logger.warn("list:[{}]", list);
        List netInfoList = new LinkedList();
        List h248VoipInfoList = new LinkedList();
        List sipVoipInfoList = new LinkedList();
        List iptvInfoList = new LinkedList();
        for (HashMap hashMap : list) {
            if ("10".equals(hashMap.get("serv_type_id"))) {
                SXLTSheetObj obj = new SXLTSheetObj();
                obj.setNetServTypeId("22");
                obj.setNetOperateId("2");
                obj.setNetOltFactory(StringUtil.getStringValue(hashMap, "oltfactory", ""));
                obj.setOldNetOltFactory(StringUtil.getStringValue(hashMap, "oltfactory", ""));
                obj.setNetUsername(StringUtil.getStringValue(hashMap, "username", ""));
                obj.setOldNetUsername(StringUtil.getStringValue(hashMap, "username", ""));
                obj.setNetPasswd(StringUtil.getStringValue(hashMap, "passwd", ""));
                obj.setNetVlanId((String) hashMap.get("vlanid"));
                obj.setOldNetVlanId((String) hashMap.get("vlanid"));
                // 判断是否为全路由
                if ("1".equals((String) hashMap.get("all_route"))) {
                    obj.setNetWanType("3");
                } else {
                    obj.setNetWanType((String) hashMap.get("wan_type"));
                }
                obj.setNetPort(StringUtil.getStringValue(hashMap, "bind_port", ""));
                obj.setOldNetPort(StringUtil.getStringValue(hashMap, "bind_port", ""));
                obj.setNetIpaddress(StringUtil.getStringValue(hashMap, "ipaddress", ""));
                obj.setNetIpmask(StringUtil.getStringValue(hashMap, "ipmask", ""));
                obj.setNetGateway(StringUtil.getStringValue(hashMap, "gateway", ""));
                obj.setNetIpdns(StringUtil.getStringValue(hashMap, "adsl_ser", ""));
                obj.setStandNetIpdns(getIpTypeReverse(StringUtil.getStringValue(hashMap,
                        "ip_type", "")));
                obj.setNetSpeed(StringUtil.getStringValue(hashMap, "speed", ""));
                netInfoList.add(obj);
//				logger.warn("obj=" + obj.toString());
            }
            if ("14".equals(hashMap.get("serv_type_id"))) {

                // h248 业务
                List<HashMap> list1 = this.dao.getVoipParaInfo((String) hashMap.get("user_id"),
                        userType, "14", "2");

                if (!org.springframework.util.CollectionUtils.isEmpty(list1)) {
                    for (HashMap paramInfo : list1) {
                        logger.warn("paramInfo:[{}],protocol:[{}]", paramInfo, paramInfo.get("protocol"));
                        SXLTSheetObj obj = new SXLTSheetObj();
                        obj.setHvoipServTypeId("15");
                        obj.setHvoipOperateId("2");
                        obj.setHvoipOltFactory(StringUtil.getStringValue(hashMap,
                                "oltfactory", ""));
                        obj.setHvoipPhone(StringUtil.getStringValue(paramInfo, "voip_phone",
                                ""));
                        obj.setHvoipRegId(StringUtil.getStringValue(paramInfo, "reg_id", ""));
                        obj.setHvoipRegIdType(StringUtil.getStringValue(paramInfo,
                                "reg_id_type", ""));
                        obj.setHvoipMgcIp(StringUtil.getStringValue(paramInfo, "prox_serv",
                                ""));
                        obj.setHvoipMgcPort(StringUtil.getStringValue(paramInfo, "prox_port",
                                ""));
                        obj.setHvoipStandMgcIp(StringUtil.getStringValue(paramInfo,
                                "stand_prox_serv", ""));
                        obj.setHvoipStandMgcPort(StringUtil.getStringValue(paramInfo,
                                "stand_prox_port", ""));
                        obj.setHvoipPort(StringUtil
                                .getStringValue(paramInfo, "voip_port", ""));
                        obj.setHvoipVlanId(StringUtil.getStringValue(hashMap, "vlanid", ""));
                        obj.setHvoipWanType(StringUtil
                                .getStringValue(paramInfo, "wan_type", ""));
                        obj.setHvoipIpaddress(StringUtil.getStringValue(paramInfo, "ipaddress",
                                ""));
                        obj.setHvoipIpmask(StringUtil.getStringValue(paramInfo, "ipmask", ""));
                        obj.setHvoipGateway(StringUtil.getStringValue(paramInfo, "gateway", ""));
                        obj.setHvoipIpdns(StringUtil.getStringValue(paramInfo, "adsl_ser", ""));
                        obj.setHvoipEid(StringUtil.getStringValue(paramInfo, "eid", ""));
                        h248VoipInfoList.add(obj);
                    }
                }

                // sip 语音业务
                List<HashMap> list2 = this.dao.getVoipParaInfo((String) hashMap.get("user_id"),
                        userType, "14", "1");
                if (!org.springframework.util.CollectionUtils.isEmpty(list2)) {
                    for (HashMap paramInfo : list2) {
                        SXLTSheetObj obj = new SXLTSheetObj();
                        obj.setSipServTypeId("14");
                        obj.setSipOperateId("2");
                        obj.setSipVoipPhone(StringUtil.getStringValue(paramInfo,
                                "voip_phone", ""));
                        obj.setSipVoipUsername(StringUtil.getStringValue(paramInfo,
                                "voip_username", ""));
                        obj.setSipVoipPwd(StringUtil.getStringValue(paramInfo, "voip_passwd",
                                ""));
                        obj.setSipProxServ(StringUtil.getStringValue(paramInfo, "prox_serv",
                                ""));
                        obj.setSipProxPort(StringUtil.getStringValue(paramInfo, "prox_port",
                                ""));
                        obj.setSipStandProxServ(StringUtil.getStringValue(paramInfo,
                                "stand_prox_serv", ""));
                        obj.setSipStandProxPort(StringUtil.getStringValue(paramInfo,
                                "stand_prox_port", ""));
                        obj.setSipVoipPort(StringUtil.getStringValue(paramInfo, "voip_port",
                                ""));
                        obj.setSipRegiServ(StringUtil.getStringValue(paramInfo, "regi_serv",
                                ""));
                        obj.setSipRegiPort(StringUtil.getStringValue(paramInfo, "regi_port",
                                ""));
                        obj.setSipStandRegiServ(StringUtil.getStringValue(paramInfo,
                                "stand_regi_serv", ""));
                        obj.setSipStandRegiPort(StringUtil.getStringValue(paramInfo,
                                "stand_regi_port", ""));
                        obj.setSipOutBoundProxy(StringUtil.getStringValue(paramInfo,
                                "out_bound_proxy", ""));
                        obj.setSipOutBoundPort(StringUtil.getStringValue(paramInfo,
                                "out_bound_port", ""));
                        obj.setSipStandOutBoundProxy(StringUtil.getStringValue(paramInfo,
                                "stand_out_bound_proxy", ""));
                        obj.setSipStandOutBoundPort(StringUtil.getStringValue(paramInfo,
                                "stand_out_bound_port", ""));
                        obj.setSipProtocol(StringUtil.getStringValue(paramInfo, "protocol",
                                ""));
                        obj.setSipVlanId(StringUtil.getStringValue(hashMap, "vlanid", ""));
                        obj.setSipWanType(StringUtil.getStringValue(hashMap, "wan_type", ""));
                        obj.setSipIpaddress(hashMap.get("ipaddress") == null ? ""
                                : (String) hashMap.get("ipaddress"));
                        obj.setSipIpmask(hashMap.get("ipmask") == null ? ""
                                : (String) hashMap.get("ipmask"));
                        obj.setSipGateway(hashMap.get("gateway") == null ? ""
                                : (String) hashMap.get("gateway"));
                        obj.setSipIpdns(hashMap.get("adsl_ser") == null ? ""
                                : (String) hashMap.get("adsl_ser"));
                        if ("1".equals(paramInfo.get("protocol"))) {
                            obj.setSipVoipUri((String) paramInfo.get("uri"));
                            obj.setSipUserAgentDomain((String) paramInfo
                                    .get("user_agent_domain"));
                        }
                        sipVoipInfoList.add(obj);
                    }
                }


            }
            if ("11".equals(hashMap.get("serv_type_id"))) {
                SXLTSheetObj obj = new SXLTSheetObj();
                obj.setIptvServTypeId("21");
                obj.setIptvOperateId("2");
                obj.setIptvOltFactory(StringUtil
                        .getStringValue(hashMap, "oltfactory", ""));
                obj.setIptvWanType((String) hashMap.get("wan_type"));
                obj.setIptvUserName(StringUtil.getStringValue(hashMap, "username", ""));
                obj.setIptvPasswd(StringUtil.getStringValue(hashMap, "passwd", ""));
                obj.setIptvPort(StringUtil.getStringValue(hashMap, "bind_port", ""));
                obj.setIptvNum(StringUtil.getStringValue(hashMap, "serv_num", ""));
                obj.setIptvVlanId(StringUtil.getStringValue(hashMap, "vlanid", ""));
                obj.setMulticastVlan(StringUtil.getStringValue(hashMap,
                        "multicast_vlanid", ""));
                obj.setIptvDestIp(StringUtil.getStringValue(hashMap, "ipaddress", ""));
                obj.setIptvDestMark(StringUtil.getStringValue(hashMap, "ipmask", ""));
                iptvInfoList.add(obj);
            }
        }
        serInfoMap.put("netInfoList", netInfoList);
        serInfoMap.put("h248VoipInfoList", h248VoipInfoList);
        serInfoMap.put("sipVoipInfoList", sipVoipInfoList);
        serInfoMap.put("iptvInfoList", iptvInfoList);
    }


    private String getIpTypeReverse(String ipType) {
        String result = "0";
        if (!StringUtil.IsEmpty(ipType)) {
            if ("1".equals(ipType)) {
                result = "0";
            }
            if ("3".equals(ipType)) {
                result = "1";
            }
            if ("2".equals(ipType)) {
                result = "5";
            }
        }
        return result;
    }

    private void userInfoToObj(Map<String, String> userInfo, SXLTSheetObj obj, String userType) {
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
        if ("2".equals(userType)) {
            obj.setCustomerId(StringUtil.getStringValue(userInfo, "customer_id", ""));
            obj.setCustomerAccount(StringUtil.getStringValue(userInfo, "linkman", ""));
        }
    }

    private void stbUserInfoToObj(Map<String, String> userInfo, SXLTSheetObj obj) {
        obj.setCityId(StringUtil.getStringValue(userInfo, "city_id", ""));
        obj.setStbUserID(StringUtil.getStringValue(userInfo, "serv_account", ""));
        obj.setStbUserPwd(StringUtil.getStringValue(userInfo, "serv_pwd", ""));
    }

    public BssSheetByHand4SXLTDAO getDao() {
        return dao;
    }

    public void setDao(BssSheetByHand4SXLTDAO dao) {
        this.dao = dao;
    }

    public String delBusiness(SXLTSheetObj obj, UserRes curUser) {
        logger.debug("BssSheetByHand4AHBIO.delBusiness(SXLTSheetObj obj)");
        String resultMessage = "";
        StringBuffer buffer = new StringBuffer();
        if ((StringUtil.IsEmpty(obj.getHvoipServTypeId()))
                && (StringUtil.IsEmpty(obj.getNetServTypeId()))
                && (StringUtil.IsEmpty(obj.getIptvServTypeId()))
                && (StringUtil.IsEmpty(obj.getSipServTypeId()))) {
            resultMessage = delUserInfo(obj);
            buffer.append("资料工单销户处理结果：" + getMessage(resultMessage, "resultDes"));
            this.dao.addHandSheetLog(obj, curUser, 20, 3,
                    "000".equals(getMessage(resultMessage, "resultCode")) ? 1 : 0,
                    getMessage(resultMessage, "resultDes"));
        } else {
            try {
                Thread.sleep(1000L);
                if (!StringUtil.IsEmpty(obj.getHvoipServTypeId())) {
                    String voipResult = delH248VoipSheet(obj);
                    buffer.append("\nH248语音销户工单处理结果："
                            + getMessage(voipResult, "resultDes"));
                    this.dao.addHandSheetLog(obj, curUser,
                            StringUtil.parseInt(obj.getHvoipServTypeId(),0), 3,
                            "000".equals(getMessage(voipResult, "resultCode")) ? 1 : 0,
                            getMessage(voipResult, "resultDes"));
                }
                if (!StringUtil.IsEmpty(obj.getSipServTypeId())) {
                    String voipResult = delSIPVoipSheet(obj);
                    buffer.append("\nSIP语音销户工单处理结果："
                            + getMessage(voipResult, "resultDes"));
                    this.dao.addHandSheetLog(obj, curUser,
                            StringUtil.parseInt(obj.getHvoipServTypeId(),0), 3,
                            "000".equals(getMessage(voipResult, "resultCode")) ? 1 : 0,
                            getMessage(voipResult, "resultDes"));
                }
                if (!StringUtil.IsEmpty(obj.getNetServTypeId())) {
                    String netResult = delNetSheetResult(obj);
                    buffer.append("\n宽带业务销户工单处理结果：" + getMessage(netResult, "resultDes"));
                    this.dao.addHandSheetLog(obj, curUser,
                            StringUtil.parseInt(obj.getNetServTypeId(),0), 3,
                            "000".equals(getMessage(netResult, "resultCode")) ? 1 : 0,
                            getMessage(netResult, "resultDes"));
                }
                if (!StringUtil.IsEmpty(obj.getIptvServTypeId())) {
                    String itvResult = delItvSheetResult(obj);
                    buffer.append("\nIPTV业务销户工单处理结果：" + getMessage(itvResult, "resultDes"));
                    this.dao.addHandSheetLog(obj, curUser,
                            StringUtil.parseInt(obj.getIptvServTypeId(),0), 3,
                            "000".equals(getMessage(itvResult, "resultCode")) ? 1 : 0,
                            getMessage(itvResult, "resultDes"));
                }
            } catch (Exception e) {
                logger.error("页面发送工单失败：",e );
            }
        }
        return buffer.toString();
    }

    public String delStbBusiness(SXLTSheetObj obj, UserRes curUser) {
        logger.debug("BssSheetByHand4AHBIO.delStbBusiness(SXLTSheetObj obj)");
        StringBuffer buffer = new StringBuffer();
        try {
            if (!StringUtil.IsEmpty(obj.getStbServ())) {
                String stbResult = delStbSheet(obj);
                buffer.append("机顶盒销户工单处理结果：" + getMessage(stbResult, "resultDes"));
                this.dao.addHandSheetLog(obj, curUser, StringUtil.parseInt(obj.getStbServ(),0), 3,
                        "000".equals(getMessage(stbResult, "resultCode")) ? 1 : 0,
                        getMessage(stbResult, "resultDes"));
            }
        } catch (Exception e) {
            logger.error("页面发送工单失败：" , e);
        }
        return buffer.toString();
    }

    private String delUserInfo(SXLTSheetObj order) {
        logger.debug("delUserInfo ==> 方法开始：{}", order);
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
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
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" , e);
        }
        return returnParam;
    }

    private String delNetSheetResult(SXLTSheetObj order) {
        logger.debug("delNetSheetResult ==> 方法开始：{}", order);
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
        root.addElement("authUser").addText("1");
        root.addElement("authPwd").addText("1");
        Element param = root.addElement("param");
        param.addElement("dealDate").addText(order.getDealDate());
        param.addElement("userType").addText("1");
        param.addElement("loid").addText(order.getLoid());
        param.addElement("cityId").addText(order.getCityId());
        root.addElement("operateId").addText("3");
        root.addElement("servTypeId").addText("22");
        param.addElement("userName").addText(order.getNetUsername());
        param.addElement("wanType").addText(order.getNetWanType());
        param.addElement("vlanId").addText(order.getNetVlanId());
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("del net xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" , e);
        }
        return returnParam;
    }

    private String delOldNetSheetResult(SXLTSheetObj order) {
        logger.debug("delOldNetSheetResult ==> 方法开始：{}", order);
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
        root.addElement("authUser").addText("1");
        root.addElement("authPwd").addText("1");
        Element param = root.addElement("param");
        param.addElement("dealDate").addText(order.getDealDate());
        param.addElement("userType").addText("1");
        param.addElement("loid").addText(order.getLoid());
        param.addElement("cityId").addText(order.getCityId());
        root.addElement("operateId").addText("3");
        root.addElement("servTypeId").addText("22");
        param.addElement("userName").addText(order.getOldNetUsername());
        param.addElement("vlanId").addText(order.getOldNetVlanId());
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("del oldNet xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" , e);
        }
        return returnParam;
    }
    
    
    private String delItvSheetResult(SXLTSheetObj order) {
        logger.debug("delUserInfo ==> 方法开始：{}", order);
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
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
        param.addElement("bindPort").addText(order.getIptvPort());
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("del iptv xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" , e);
        }
        return returnParam;
    }

    private String delH248VoipSheet(SXLTSheetObj order) {
        logger.debug("delUserInfo ==> 方法开始：{}", order);
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
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
        param.addElement("voipPort").addText(order.getHvoipPort());
        param.addElement("protocol").addText("2");
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("del H248 voip xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" , e);
        }
        return returnParam;
    }

    private String delSIPVoipSheet(SXLTSheetObj order) {
        logger.debug("delUserInfo ==> 方法开始：{}", order);
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
        root.addElement("authUser").addText("1");
        root.addElement("authPwd").addText("1");
        Element param = root.addElement("param");
        param.addElement("dealDate").addText(order.getDealDate());
        param.addElement("userType").addText("1");
        param.addElement("loid").addText(order.getLoid());
        param.addElement("cityId").addText(order.getCityId());
        root.addElement("operateId").addText("3");
        root.addElement("servTypeId").addText("14");
        param.addElement("userName").addText("");
        param.addElement("voipPort").addText(order.getSipVoipPort());
        param.addElement("protocol").addText("1");
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("del SIP voip xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" , e);
        }
        return returnParam;
    }

    private String delStbSheet(SXLTSheetObj order) {
        logger.debug("delStbSheet ==> 方法开始：{}", order);
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
        root.addElement("authUser").addText("1");
        root.addElement("authPwd").addText("1");
        Element param = root.addElement("param");
        param.addElement("dealDate").addText(order.getDealDate());
        param.addElement("userType").addText("1");
        param.addElement("loid").addText(order.getLoid());
        param.addElement("cityId").addText(order.getCityId());
        root.addElement("operateId").addText("3");
        root.addElement("servTypeId").addText("25");
        param.addElement("servaccount").addText(order.getStbUserID());
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("del stb xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" , e);
        }
        return returnParam;
    }

    /**
     * 家庭网关开户
     *
     * @param obj
     * @param curUser
     * @return
     */
    public String doBusiness(SXLTSheetObj obj, UserRes curUser) {
        logger.warn("BssSheetByHand4AHBIO.doBusiness(SXLTSheetObj obj)");
        logger.warn("SXLTSheetObj obj:[{}]",obj.toString());
        String resultMessage = doUserInfo(obj);
        StringBuffer buffer = new StringBuffer();
        buffer.append("资料工单处理结果：" + getMessage(resultMessage, "resultDes"));
        this.dao.addHandSheetLog(obj, curUser, StringUtil.parseInt(obj.getUserServTypeId(),0), StringUtil.parseInt(obj.getUserOperateId(),0), "000"
                        .equals(getMessage(resultMessage, "resultCode")) ? 1 : 0,
                getMessage(resultMessage, "resultDes"));
        if ("000".equals(getMessage(resultMessage, "resultCode"))) {
            try {
                Thread.sleep(1000L);
                if (!StringUtil.IsEmpty(obj.getHvoipServTypeId())) {
                    String voipResult = doH248VoipSheet(obj);
                    buffer.append(";\nH248语音开户工单处理结果："
                            + getMessage(voipResult, "resultDes"));
                    this.dao.addHandSheetLog(obj, curUser,
                            StringUtil.parseInt(obj.getHvoipServTypeId(),0), StringUtil.parseInt(obj.getHvoipOperateId(),0),
                            "000".equals(getMessage(voipResult, "resultCode")) ? 1 : 0,
                            getMessage(voipResult, "resultDes"));
                }
                if (!StringUtil.IsEmpty(obj.getNetServTypeId())) {
                	//原vlanid和现在Vlanid不一致时先销户，再开户
                	if (!StringUtil.IsEmpty(obj.getOldNetVlanId()) && !obj.getNetVlanId().equals(obj.getOldNetVlanId())) {
                		String netResult1 = delOldNetSheetResult(obj);
                		this.dao.addHandSheetLog(obj, curUser,
                				StringUtil.parseInt(obj.getNetServTypeId(),0), 3,
                				"000".equals(getMessage(netResult1, "resultCode")) ? 1 : 0,
                						getMessage(netResult1, "resultDes"));
					}
                	
                    String netResult = doNetSheetResult(obj);
                    buffer.append(";\n宽带业务开户工单处理结果：" + getMessage(netResult, "resultDes"));
                    this.dao.addHandSheetLog(obj, curUser,
                            StringUtil.parseInt(obj.getNetServTypeId(),0), StringUtil.parseInt(obj.getNetOperateId(),0), "000"
                                    .equals(getMessage(netResult, "resultCode")) ? 1 : 0,
                            getMessage(netResult, "resultDes"));
                }
                if (!StringUtil.IsEmpty(obj.getIptvServTypeId())) {
                    String itvResult = doItvSheetResult(obj);
                    buffer.append(";\nIPTV业务开户工单处理结果："
                            + getMessage(itvResult, "resultDes"));
                    this.dao.addHandSheetLog(obj, curUser,
                            StringUtil.parseInt(obj.getIptvServTypeId(),0), StringUtil.parseInt(obj.getNetOperateId(),0), "000"
                                    .equals(getMessage(itvResult, "resultCode")) ? 1 : 0,
                            getMessage(itvResult, "resultDes"));
                }
                if (!StringUtil.IsEmpty(obj.getSipServTypeId())) {
                    String itvResult = doSIPVoipSheet(obj);
                    buffer.append(";\nSIP语音业务开户工单处理结果："
                            + getMessage(itvResult, "resultDes"));
                    this.dao.addHandSheetLog(obj, curUser,
                            StringUtil.parseInt(obj.getIptvServTypeId(),0), StringUtil.parseInt(obj.getNetOperateId(),0), "000"
                                    .equals(getMessage(itvResult, "resultCode")) ? 1 : 0,
                            getMessage(itvResult, "resultDes"));
                }
            } catch (Exception e) {
                logger.error("页面发送工单失败：",e );
            }
        }
        return buffer.toString();
    }

    private String doItvSheetResult(SXLTSheetObj order) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
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
        param.addElement("wanType").addText("1");
        param.addElement("vlanId").addText(getVlan(0,order.getIptvVlanId(),order.getIptvOltFactory(),"iptv",order.getNetWanType()));
        param.addElement("oltFactory").addText(order.getIptvOltFactory());
		/*param.addElement("multicastVlan").addText(order.getMulticastVlan());
		param.addElement("userName").addText("");
		param.addElement("password").addText("");*/
        param.addElement("bindPort").addText(order.getIptvPort());


        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("IPTV xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" ,e);
        }
        return returnParam;
    }

    private String getMessage(String xmlStr, String node) {
        String result = "";
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlStr);
            Element rootElt = doc.getRootElement();
            result = rootElt.elementTextTrim(node);
        } catch (Exception e) {
            logger.error("exception:[{}]" , e);
        }
        return result;
    }
    /**
     * 1、华为光猫 iptv的vlan值固定4057、宽带vlan值（桥接、路由,全路由方式的vlan值是端口+1）
     2、其它厂家的vlan值由接口参数确定，接口参数传多少值就是多少
     * @param vlanInt
     * @param factory
     * @return
     */
    static String getVlan( int port, String realVlan, String factory, String servType,String wanType){
        if("HW".equalsIgnoreCase(factory)||"HUAWEI".equalsIgnoreCase(factory)){
            if("wband".equals(servType)){
            	if ("3".equals(wanType)) {
            		return "2";
				}else {
					return StringUtil.getStringValue(port+1);
				}
            }
            else{
                return "4057";
            }
        }
        else{
            return realVlan;
        }
    }
    private String doNetSheetResult(SXLTSheetObj order) {
        logger.warn("doUserInfo(UserInfoSheet4AHOBJ doNetSheetResult)");
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
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
        param.addElement("oltFactory").addText(order.getNetOltFactory());
        param.addElement("userName").addText(order.getNetUsername());
        param.addElement("password").addText(order.getNetPasswd());
        // 将“L2” 改为“2”
        String netPort = "";
        if(StringUtils.isNotEmpty(order.getNetPort()) && !"".equals(order.getNetPort())){
            netPort = order.getNetPort().substring(1,2);
        }
        param.addElement("vlanId").addText(getVlan(StringUtil.getIntegerValue(netPort,0),order.getNetVlanId(),order.getNetOltFactory(),"wband",order.getNetWanType()));
        // 全路由模式
        if ("3".equals(order.getNetWanType())) {
            param.addElement("wanType").addText("2");
            param.addElement("allRoute").addText("1");
            param.addElement("bindPort").addText("");
        }else {
            param.addElement("wanType").addText(order.getNetWanType());
            param.addElement("bindPort").addText(order.getNetPort());
        }
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("NET xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" ,e);
        }
        return returnParam;
    }

    private String doH248VoipSheet(SXLTSheetObj order) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
        root.addElement("authUser").addText("1");
        root.addElement("authPwd").addText("1");
        Element param = root.addElement("param");
        param.addElement("dealDate").addText(order.getDealDate());
        param.addElement("userType").addText("1");
        param.addElement("loid").addText(order.getLoid());
        param.addElement("cityId").addText(order.getCityId());
        root.addElement("operateId").addText("1");
        param.addElement("orderNo").addText(order.getDealDate());
        root.addElement("servTypeId").addText("15");
        // param.addElement("oltFactory").addText(order.getHvoipOltFactory());
        param.addElement("eid").addText(order.getHvoipEid());
        param.addElement("mgcIp").addText(order.getHvoipMgcIp());
        param.addElement("mgcPort").addText(order.getHvoipMgcPort());
        param.addElement("protocol").addText("2");
        param.addElement("voipPort").addText(order.getHvoipPort());
        param.addElement("standMgcIp").addText(order.getHvoipStandMgcIp());
        param.addElement("standMgcPort").addText(order.getHvoipStandMgcPort());
        if (StringUtils.isNotEmpty(order.getHvoipIpaddress()) && StringUtils.isNotEmpty(order.getHvoipIpmask()) && StringUtils.isNotEmpty(order.getHvoipGateway())) {
            param.addElement("wanType").addText("3");
        } else {
            param.addElement("wanType").addText("4");
        }
        param.addElement("ipaddress").addText(order.getHvoipIpaddress());
        param.addElement("ipmask").addText(order.getHvoipIpmask());
        param.addElement("gateway").addText(order.getHvoipGateway());
        /*
         * if ("243".equals(order.getHvoipOltFactory())) {
         * param.addElement("vlanId").addText("8"); } else {
         * param.addElement("vlanId").addText(order.getHvoipVlanId()); }
         */
		/*param.addElement("vlanId").addText(order.getHvoipVlanId());
		param.addElement("regId").addText(order.getHvoipRegId());
		if ("IP".equalsIgnoreCase(order.getHvoipRegIdType()))
		{
			param.addElement("regIdType").addText("0");
		}
		else if ("DomainName".equalsIgnoreCase(order.getHvoipRegIdType()))
		{
			param.addElement("regIdType").addText("1");
		}

		param.addElement("ipdns").addText("");
		param.addElement("voipPhone").addText(order.getHvoipPhone());
		param.addElement("dscpMark").addText("0");
		param.addElement("deviceName").addText(order.getHvoipIpaddress());*/
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("H248 VOIP xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" ,e);
        }
        return returnParam;
    }

    private String doSIPVoipSheet(SXLTSheetObj order) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
        root.addElement("authUser").addText("1");
        root.addElement("authPwd").addText("1");
        Element param = root.addElement("param");
        param.addElement("dealDate").addText(order.getDealDate());
        param.addElement("userType").addText("1");
        param.addElement("loid").addText(order.getLoid());
        param.addElement("cityId").addText(order.getCityId());
        root.addElement("operateId").addText("1");
        param.addElement("orderNo").addText(order.getDealDate());
        root.addElement("servTypeId").addText("14");
        param.addElement("protocol").addText("1");
        param.addElement("voipPort").addText(order.getSipVoipPort());
        String voipAuthName = order.getSipVoipUsername();
        if (null != voipAuthName) {
            if (voipAuthName.indexOf("+86") > -1 && voipAuthName.indexOf("@") > -1) {
                String voipPhone = voipAuthName.replaceAll("\\+86", "0");
                voipPhone = voipPhone.substring(0, voipPhone.indexOf("@"));
                param.addElement("voipPhone").addText(voipPhone);
            } else {
                param.addElement("voipPhone").addText(
                        order.getSipVoipUsername());
            }
        } else {
            param.addElement("voipPhone").addText("");
        }
        if (StringUtils.isNotEmpty(order.getSipIpaddress()) && StringUtils.isNotEmpty(order.getSipIpmask()) && StringUtils.isNotEmpty(order.getSipGateway())) {
            param.addElement("wanType").addText("3");
        } else {
            param.addElement("wanType").addText("4");
        }
        param.addElement("ipaddress").addText(order.getSipIpaddress());
        param.addElement("ipmask").addText(order.getSipIpmask());
        param.addElement("gateway").addText(order.getSipGateway());

        param.addElement("voipUsername").addText(
                order.getSipVoipUsername());
        param.addElement("voipPwd").addText(
                order.getSipVoipPwd());
        param.addElement("proxServ").addText(
                order.getSipProxServ());
        param.addElement("proxPort").addText(
                order.getSipProxPort());
        param.addElement("standProxServ").addText(
                order.getSipStandProxServ());
        param.addElement("standProxPort").addText(
                order.getSipStandProxPort());
        param.addElement("regiServ").addText(
                order.getSipRegiServ());
        param.addElement("regiPort").addText(
                order.getSipRegiPort());
        param.addElement("standRegiServ").addText(
                order.getSipStandRegiServ());
        param.addElement("standRegiPort").addText(
                order.getSipStandRegiPort());
        // IMS专用
        if ("0".equals(order.getSipProtocol())) {
            param.addElement("outBoundProxy").addText(
                    order.getSipOutBoundProxy());
            param.addElement("outBoundPort").addText(
                    order.getSipOutBoundPort());
            param.addElement("standOutBoundProxy").addText(
                    order.getSipStandOutBoundProxy());
            param.addElement("standOutBoundPort").addText(
                    order.getSipStandOutBoundPort());
            param.addElement("voipUri").addText(
                    order.getSipVoipUri());
            param.addElement("voipOldAuthName").addText(
                    order.getSipVoipUsername());
        }
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("SIP VOIP xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" ,e);
        }
        return returnParam;
    }

    private String doStbSheet(SXLTSheetObj order) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
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
        param.addElement("mac").addText(order.getLoid());
        param.addElement("stbaccessStyle").addText("DHCP");
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("stb xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("stb结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" ,e);
        }
        return returnParam;
    }

    private String doUserInfo(SXLTSheetObj order) {
        logger.debug("doUserInfo(UserInfoSheet4AHOBJ userInfoSheet)");
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("root");
        root.addElement("cmdId").addText(order.getCmdId());
        root.addElement("authUser").addText("1");
        root.addElement("authPwd").addText("1");
        Element param = root.addElement("param");
        param.addElement("dealDate").addText(order.getDealDate());
        param.addElement("userType").addText("1");
        param.addElement("loid").addText(order.getLoid());
        param.addElement("cityId").addText(order.getCityId());
        root.addElement("operateId").addText("1");
        root.addElement("servTypeId").addText("20");
        param.addElement("officeId").addText("");
        param.addElement("areaId").addText("");
        param.addElement("accessStyle").addText(order.getAccessStyle());
        param.addElement("linkman").addText("");
        param.addElement("linkPhone").addText("");
        param.addElement("email").addText("");
        param.addElement("mobile").addText("");
        param.addElement("linkAddress").addText("");
        param.addElement("linkmanCredno").addText("");
        param.addElement("customerId").addText("");
        param.addElement("customerAccount").addText("");
        param.addElement("customerPwd").addText("");
        param.addElement("specId").addText("GPON(hgu21)");
        param.addElement("deviceType").addText(order.getDeviceType());
        String sendXml = document.asXML();
        logger.info("xml:" + sendXml);
        logger.warn("资料 xml:" + sendXml);
        String returnParam = "";
        try {
            Service service = new Service();
            Call call = null;
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endPointReference));
            call.setOperationName("call");
            returnParam = (String) call.invoke(new Object[]{sendXml});
            logger.warn("结果：" + returnParam);
        } catch (Exception e) {
            logger.error("err:" , e);
        }
        return returnParam;
    }

    public String checkCustomerId(String customerId) {
        boolean result = this.dao.isExistCustomer(customerId);
        if (result) {
            return "-1";
        }
        return "000";
    }

    /**
     * 机顶盒开户
     *
     * @param obj
     * @param curUser
     * @return
     */
    public String stbDoBusiness(SXLTSheetObj obj, UserRes curUser) {
        logger.debug("BssSheetByHand4AHBIO.stbDoBusiness(SXLTSheetObj obj)");
        StringBuffer buffer = new StringBuffer();
        try {
            if (!StringUtil.IsEmpty(obj.getStbServ())) {
                String stbResult = doStbSheet(obj);
                buffer.append("机顶盒开户工单处理结果：" + getMessage(stbResult, "resultDes"));
                this.dao.addHandSheetLog(obj, curUser, StringUtil.parseInt(obj.getStbServ(),0), 1,
                        "000".equals(getMessage(stbResult, "resultCode")) ? 1 : 0,
                        getMessage(stbResult, "resultDes"));
            }
        } catch (Exception e) {
            logger.error("页面发送工单失败：[]" ,e);
        }
        return buffer.toString();
    }

    public int getLineId(String linePort) {
        logger.debug("getLineId({})", linePort);
        int lineId = 1;
        if (!StringUtil.IsEmpty(linePort)) {
            lineId = Integer.valueOf(
                    linePort.substring(linePort.length() - 1, linePort.length()))
                    .intValue() + 1;
        }
        return lineId;
    }
}
