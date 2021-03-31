<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
      <table width="98%" border="1" cellpadding="0" cellspacing="0" class="table">
        <tr class="tab_title">
          <td colspan="3" class="title_white">上网行为分析</td>
        </tr>
        <tr class="tr_white">
          <td width="35%" nowrap class="text">
            <a href="NetFluxAnalyse!toNetFluxAnalyse.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              网络流量分析
            </a>
          </td>
          <td width="39%" nowrap class="text">
            <a href="PBMailTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              员工邮件收发TopN
            </a>
          </td>
          <td width="26%" nowrap class="text">          </td>
        </tr>
        <tr class="tr_glory">
          <td class="text">
            <a href="EntWebTopn!toEntWebTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              WEB浏览TopN
            </a>
          </td>
          <td class="text">－－－－－－－－－</td>
          <td class="text">          </td>
        </tr>
        <tr class="tr_white">
          <td class="text">
            <a href="EntFtpTopn!toEntFtpSiteTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              FTP站点TopN
            </a>
          </td>
          <td class="text">
            <a href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=0">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              HTTP详情
            </a>
          </td>
          <td class="text">&nbsp;</td>
        </tr>
        <tr class="tr_glory">
          <td class="text">－－－－－－－－－</td>
          <td class="text">
            <a href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=2">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt=""/>
              SMTP详情
            </a>
          </td>
          <td class="text">&nbsp;</td>
        </tr>
        <tr class="tr_white">
          <td class="text">
            <a href="PBTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              上网行为分析
            </a>
          </td>
          <td nowrap class="text">
            <a href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=3">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              POP3详情
            </a>
          </td>
          <td nowrap class="text">&nbsp;</td>
        </tr>
        <tr class="tr_glory">
          <td class="text">
            <a href="PBWebTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              员工WEB浏览TopN
            </a>
          </td>
          <td class="text">          </td>
          <td class="text">&nbsp;</td>
        </tr>
      </table>
	  <%--
<table width="98%"  border="1" cellpadding="0" cellspacing="0" class="table">
          <tr class="tab_title">
            <td colspan="3" class="title_white">上网行为分析</td>
          </tr>
          <tr class="tr_white">
            <td class="text"><a href="/epsgwms/securitygw/NetFluxAnalyse!toNetFluxAnalyse.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">网络流量分析</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">上网行为分析</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=0"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">HTTP详情</a></td>
          </tr>
          <tr class="tr_glory">
            <td class="text"><a href="/epsgwms/securitygw/EntWebTopn!toEntWebTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">WEB浏览TopN</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBWebTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">员工WEB浏览TopN</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=2"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">SMTP详情</a></td>
          </tr>
          <tr class="tr_white">
            <td class="text"><a href="/epsgwms/securitygw/EntFtpTopn!toEntFtpSiteTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">FTP站点TopN</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBMailTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">员工邮件收发TopN</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=3"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">POP3详情</a></td>
          </tr>
          <tr class="tr_glory">
            <td class="text" colspan="3">&nbsp;</td>
          </tr>
          <tr class="tr_white">
            <td class="text" colspan="3">&nbsp;</td>
          </tr>
          <tr class="tr_glory">
            <td class="text" colspan="3">&nbsp;</td>
          </tr>
        </table>--%>
		
            <%--<td nowrap class="text"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">IMAP详情</td>--%>
			
            <%--<td class="text"><a href="inside_snmp2.htm"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">即时通讯TopN</a></td>--%>
            <%--<td class="text"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">FTP详情</td>--%>