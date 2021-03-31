<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<table width="99%" border="0" cellpadding="0" cellspacing="0" align="center">
 <tr>
  <td width="45%" align="center" valign="middle">
  <table width="99%" border="1" cellpadding="0" cellspacing="0" class="table">
   <tr class="tab_title">
    <td colspan="2" class="title_white"><s:property value="deviceName" /></td>
   </tr>
   <tr class="tr_white">
    <td width="33%" class="tr_yellow">设备 IP</td>
    <td width="67%" class="text"><s:property value="deviceIp" /></td>
   </tr>
   <tr class="tr_glory">
    <td class="tr_yellow">设备型号</td>
    <td class="text"><s:property value="deviceModel" /></td>
   </tr>
   <tr class="tr_white">
    <td class="tr_yellow">备注</td>
    <td class="text"><s:property value="remark" /></td> 
   </tr>
   <tr class="tr_glory">
    <td class="tr_yellow">&nbsp;</td>
    <td class="text">&nbsp;</td>
   </tr>
   <tr class="tr_white">
    <td class="tr_yellow">&nbsp;</td>
    <td class="">&nbsp;</td>
   </tr>
   <tr class="tr_glory">
    <td class="tr_yellow">&nbsp;</td>
    <td class="text">&nbsp;</td>
   </tr>
  </table>
  </td>
  <td width="55%" align="center" valign="middle">
  <table width="99%" border="1" cellpadding="0" cellspacing="0" class="table">
   <tr class="tab_title">
    <td colspan="3" class="title_white">上网行为分析</td>
   </tr>
   <tr class="tr_white">
    <td width="35%" nowrap class="text"><a
     href="NetFluxAnalyse!toNetFluxAnalyse.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
    <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13"
     border="0" alt=""> 网络流量分析 </a></td>
    <td width="35%" nowrap class="text"><a
     href="PBMailTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>">
    <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13"
     border="0" alt=""> 员工邮件收发TopN </a></td>
    <td width="29%" nowrap class="text"></td>
   </tr>
   <tr class="tr_glory">
    <td class="text"><a
     href="EntWebTopn!toEntWebTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
    <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13"
     border="0" alt=""> WEB浏览TopN </a></td>
    <td class="text">－－－－－－－－－</td>
    <td class="text"></td>
   </tr>
   <tr class="tr_white">
    <td class="text"><a
     href="EntFtpTopn!toEntFtpSiteTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
    <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13"
     border="0" alt=""> FTP站点TopN </a></td>
    <td class="text"><a
     href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=0"> <img
     src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0"
     alt=""> HTTP详情 </a></td>
    <td class="text">&nbsp;</td>
   </tr>
   <tr class="tr_glory">
    <td class="text">－－－－－－－－－</td>
    <td class="text"><a
     href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=2"> <img
     src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0"
     alt="" /> SMTP详情 </a></td>
    <td class="text">&nbsp;</td>
   </tr>
   <tr class="tr_white">
    <td class="text"><a
     href="PBTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
    <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13"
     border="0" alt=""> 上网行为分析 </a></td>
    <td nowrap class="text"><a
     href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=3"> <img
     src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0"
     alt=""> POP3详情 </a></td>
    <td nowrap class="text">&nbsp;</td>
   </tr>
   <tr class="tr_glory">
    <td class="text"><a
     href="PBWebTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>">
    <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13"
     border="0" alt=""> 员工WEB浏览TopN </a></td>
    <td class="text"></td>
    <td class="text">&nbsp;</td>
   </tr>
  </table>
  </td>
 </tr>
</table>

