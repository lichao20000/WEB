<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="../../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>��ͥ���ز�ƥ��������������</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

function detail(cityId){
	var page="<s:url value='/gwms/report/MismathSpeed!queryAddDetail.action'/>?cityId=" + cityId;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function toExcel(){
	var page="<s:url value='/gwms/report/MismathSpeed!addExcel.action'/>";
	document.all("childFrm").src=page; 
}
</script>
</head>
<body>
<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
  <td>
  <table width="100%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="left" class="title_bigwhite">
					��ͥ���ز�ƥ����������
				</td>
			</tr>
 </table>
  </td>
  </tr>  
  <tr>
    <td><table class="listtable" id="listTable">
	  <tr bgcolor='#ffffff'><td colspan='2' align='center'>��ͥ���ز�ƥ���������ϴζԱ������嵥</td></Tr>
      <tr>
        <TH style='text-align:right !important'>����</TH>
        <TH>������è��ƥ������</TH>
      </tr>
      <s:if test="null != data">
      <s:iterator value="data">
      <s:if test="parent_id !='-1' and parent_id !='00'">
      <tr>
        <td class=column align='center' bgcolor="#FFFFFF">
           <s:property value="city_name" />
		</td>
		<td class=column style='text-align:center !important' bgcolor='#ffffff'>
		  <s:if test="total != 0" >
            <a href="javascript:detail('<s:property value="city_id" />');">
			    <s:property value="total" />
		    </a>
		   </s:if>
		   <s:else>
		      <s:property value="total" />
		   </s:else>
		  </td>
		</tr>
	   </s:if>
      </s:iterator>
      <tfoot>
		<tr>
			<td colspan="2">
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="toExcel()">
			</td>
		</tr>
	</tfoot>
      </s:if>
      <s:else>
       <tr bgcolor="#FFFFFF">
      <td class=column  colspan='2'>
                        ���������ʲ�ƥ����������
		</td>
		</tr>
      </s:else>
    </table>
    </td>
  </tr>
</table>
<iframe id="childFrm" name="childFrm" src="about:blank" style="display:none;width:500;height:500"></iframe>
</body>
</html>
