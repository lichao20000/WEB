<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
      <table width="98%" border="1" cellpadding="0" cellspacing="0" class="table">
        <tr class="tab_title">
          <td colspan="3" class="title_white">������Ϊ����</td>
        </tr>
        <tr class="tr_white">
          <td width="35%" nowrap class="text">
            <a href="NetFluxAnalyse!toNetFluxAnalyse.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              ������������
            </a>
          </td>
          <td width="39%" nowrap class="text">
            <a href="PBMailTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              Ա���ʼ��շ�TopN
            </a>
          </td>
          <td width="26%" nowrap class="text">          </td>
        </tr>
        <tr class="tr_glory">
          <td class="text">
            <a href="EntWebTopn!toEntWebTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              WEB���TopN
            </a>
          </td>
          <td class="text">������������������</td>
          <td class="text">          </td>
        </tr>
        <tr class="tr_white">
          <td class="text">
            <a href="EntFtpTopn!toEntFtpSiteTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              FTPվ��TopN
            </a>
          </td>
          <td class="text">
            <a href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=0">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              HTTP����
            </a>
          </td>
          <td class="text">&nbsp;</td>
        </tr>
        <tr class="tr_glory">
          <td class="text">������������������</td>
          <td class="text">
            <a href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=2">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt=""/>
              SMTP����
            </a>
          </td>
          <td class="text">&nbsp;</td>
        </tr>
        <tr class="tr_white">
          <td class="text">
            <a href="PBTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              ������Ϊ����
            </a>
          </td>
          <td nowrap class="text">
            <a href="PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=3">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              POP3����
            </a>
          </td>
          <td nowrap class="text">&nbsp;</td>
        </tr>
        <tr class="tr_glory">
          <td class="text">
            <a href="PBWebTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>">
              <img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0" alt="">
              Ա��WEB���TopN
            </a>
          </td>
          <td class="text">          </td>
          <td class="text">&nbsp;</td>
        </tr>
      </table>
	  <%--
<table width="98%"  border="1" cellpadding="0" cellspacing="0" class="table">
          <tr class="tab_title">
            <td colspan="3" class="title_white">������Ϊ����</td>
          </tr>
          <tr class="tr_white">
            <td class="text"><a href="/epsgwms/securitygw/NetFluxAnalyse!toNetFluxAnalyse.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">������������</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">������Ϊ����</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=0"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">HTTP����</a></td>
          </tr>
          <tr class="tr_glory">
            <td class="text"><a href="/epsgwms/securitygw/EntWebTopn!toEntWebTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">WEB���TopN</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBWebTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">Ա��WEB���TopN</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=2"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">SMTP����</a></td>
          </tr>
          <tr class="tr_white">
            <td class="text"><a href="/epsgwms/securitygw/EntFtpTopn!toEntFtpSiteTopn.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">FTPվ��TopN</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBMailTopN.action?deviceid=<s:property value="deviceid"/>&remark=<s:property value="remark"/>&start=<s:property value="start"/>"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">Ա���ʼ��շ�TopN</a></td>
            <td class="text"><a href="/epsgwms/securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&prot_type=3"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">POP3����</a></td>
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
		
            <%--<td nowrap class="text"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">IMAP����</td>--%>
			
            <%--<td class="text"><a href="inside_snmp2.htm"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">��ʱͨѶTopN</a></td>--%>
            <%--<td class="text"><img src="<s:url value="/securitygw/images/bullet_main_02.gif"/>" width="17" height="13" border="0">FTP����</td>--%>