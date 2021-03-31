<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="../../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>家庭网关不匹配速率报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

function detail(cityId){
	var page="<s:url value='/gwms/report/MismathSpeed!queryDetail.action'/>?cityId=" + cityId;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
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
					家庭网关不匹配速率
				</td>
			</tr>
 </table>
  </td>
  </tr>  
  <tr>
    <td><table width="100%"  border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
	  <tr bgcolor='#ffffff'><td colspan='2' align='center'>家庭网关不匹配速率报表</td></Tr>
      <tr>
        <TH>分公司</TH>
        <TH>数量</TH>
      </tr>
      <s:if test="null != data">
      <s:iterator value="data">
      <s:if test="parent_id !='-1' and parent_id !='00'">
      <tr>
        <td class=column align='center' bgcolor="#FFFFFF">
           <s:property value="city_name" />
		</td>
		<s:if test="city_name=='合计'">
		  <td class=column align='center' bgcolor='#ffffff'>
			 <s:property value="total" />
		  </td>
		</s:if>
		<s:else>
		 <td class=column align='center' bgcolor='#ffffff'>
		  <s:if test="total != 0" >
            <a href="javascript:detail('<s:property value="parent_id" />');">
			    <s:property value="total" />
		    </a>
		   </s:if>
		   <s:else>
		      <s:property value="total" />
		   </s:else>
		  </td>
		</s:else>
		</tr>
	   </s:if>
      </s:iterator>
      </s:if>
      <s:else>
       <tr bgcolor="#FFFFFF">
      <td class=column  colspan='2'>
                        不存在速率不匹配数据
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
