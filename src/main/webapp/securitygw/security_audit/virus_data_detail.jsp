<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>���յ�������ר��ϵͳ</title>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_ico.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/menu_list.css"/>" rel="stylesheet" type="text/css">

<style type="text/css">
<!--
@import url(../css/css_ico.css);
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
a{margin-bottom:-1px;}
-->
</style>

<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/edittable.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/Calendar.js"/>"></script>
<script language="JavaScript" type="text/JavaScript">
<!--
 $(function(){
               var curPage = "<s:property value="curPage_splitPage"/>";
               var num = "<s:property value="num_splitPage"/>";
               var maxPage = "<s:property value="maxPage_splitPage"/>";
               var paramList = "<s:property value="paramList_splitPage"/>";

            //��ʼ����ҳ��ť
            $.initPage(
              "<s:url value="/securitygw/entSecStat!getVirusOrginData.action"/>",
              "#toolbar",
              curPage,
              num,
              maxPage,
              paramList);
       });






//-->
</script>

<style type="text/css">
<!--
.style5 {color: #FF0000}
-->
</style>
</head>

<body>
<form name="addfrm" action="<s:url value="/securitygw/entSecStat!getVirusOrginData.action"/>" method="post" id="addfrm">

<br>
<table width="95%"  border="1" align="center" cellpadding="0" cellspacing="0" class="table">
  <tr  class="tab_title">
    <td colspan="4" class="text_white"> <s:property value="deviceName"/></td>
  </tr>
  <tr>
    <td class="tr_yellow"><span class="text">��ѡ��ʱ���</span></td>
    <td class="tr_white">
          <span class="text">��
      <input name="starttime" type="text" class="form_kuang" size="18" value="<s:property value="starttime"/>"/>
      <input name="button2322" type="button" class="jianbian" onClick="showCalendar('all')" value="��">
      </span>
      <span class="text">��
      <input name="endtime" type="text" class="form_kuang" value="<s:property value="endtime"/>" size="18"/>
      <input name="button23222" type="button" class="jianbian" onClick="showCalendar('all')" value="��">
    ��</span> </td>
    <!-- td class="tr_yellow" width="10%"><span class="text">��������</span></td>
    <td class="tr_white" width="35%"><select name="virusType" class="form_xialakuang" />
      <option selected value="-1">--��ѡ��--</option>
      <option value="1">ľ����</option>
      <option value="2">....</option>
      <option value="3">....</option>
    </select></td >
    <script language = "javascript">
	    addfrm.virusType.value=<s:property value="virusType"/>;
	</script> -->
  </tr>
  <!-- <tr>
    <td class="tr_yellow"><span class="text">�����û� IP</span>
    </td>
    <td class="tr_white"><span class="text">��
        <input name="srcIp" type="text" class="form_kuang" size="18" value="<s:property value="srcIp"/>"/>
    </span></td>
    <td class="tr_yellow"><span class="text">����ʽ</span></td>
    <td class="tr_white"><span class="text">
      <select name="opType" class="form_xialakuang"/>
        <option selected value="-1">--��ѡ��--</option>
        <option value="1">....</option>
        <option value="2">....</option>
      </select>
    </span></td>
    <script language = "javascript">
	    addfrm.opType.value=<s:property value="opType"/>;
	</script>
  </tr> -->
  <tr>
    <td colspan="4" class="tr_glory"><div align="right"><span class="text">
        </span><span class="text">
        <input name="deviceId" type="hidden" value="<s:property value="deviceId"/>"/>
        <input name="Submit" type="submit" class="jianbian" value="�� ѯ">&nbsp;&nbsp;
    </span></div></td>
  </tr>
</table>
</form>
<br>
<table width="95%"  border="1" align="center" cellpadding="0" cellspacing="0" class="table">
  <tr class="tab_title">
    <td colspan="5" class="text"><span class="title_white">�����¼�����</span></td>
  </tr>
  <tr class="trOver_blue">
    <td class="text">�����û�IP</td>
    <td class="text">��ʼʱ��</td>
    <td class="text">��������</td>
    <td class="text">��������</td>
    <td class="text">����ʱ��</td>
  </tr>
	<s:iterator var="map" value="securityDetails" status="st">
		<tr  class="<s:property value="#st.odd==true?'tr_white':'tr_glory'"/>" onmouseover="className='trOver_blue'"
					onmouseout="className='<s:property value="#st.odd==true?'tr_white':'tr_glory'"/>'">
			<td class="text"><s:property value="#map.srcip" /></td>
			<td class="text"><s:property value="#map.stime" /></td>
			<td class="text"><s:property value="#map.virusname" /></td>
			<td class="text"><s:property value="#map.virustimes" /></td>
			<td class="text"><s:property value="#map.etime" /></td>
		</tr>
	</s:iterator>
</table>
<div id="toolbar" width="98%" align="right" style="margin-right: 10px; margin-top: 2px;"></div>
<table width="95%"  border="1" align="center" cellpadding="0" cellspacing="0" class="table">
</table>

</body>
</html>
