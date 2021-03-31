<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

//�����ն��굥 
function getAddDetailList(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryDetailList.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

//��ƥ���ն��굥
function getNoMatchDetailList(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryNoMatchDetailList.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}	

//��ƥ���ն˵���������굥
function getNoMatchDayDetailList(city_id,needCount){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!getNoMatchDayDetailList.action'/>?"+ "cityId=" + city_id+"&needCount="+needCount;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}		

function doQuery(){
	 frm.submit();
}
</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="90%" align="center" style="margin-top: 20px">
	 <TR>
         <TD>
                <TABLE width="100%" height="30" border="0" cellspacing="0"
                       cellpadding="0" class="green_gargtd">
                    <TR>
                        <TD width="164" align="center" class="title_bigwhite">�ն˲�ƥ��</TD>
                        <td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
                                        width="15" height="12"> &nbsp;�ն��������ײͲ�ƥ�䱨��
                        </td>
                    </TR>
                </TABLE>
            </TD>
    </TR>
 <tr>
  <td >
  <form name="frm" action="/gwms/report/terminalNoMatchReport!devMisMatch.action" method='POST'>
  	 <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
  	 	<%-- <tr>
  	 		<TH colspan="4">�ն��������ײͲ�ƥ�䱨��</TH>
  	 	</tr>
  	    <tr bgcolor=#ffffff>
  	   	  <td class=column align=right>����</td>
  	   	     <s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="��ѡ������" listKey="city_id" listValue="city_name"
										value="cityId" cssClass="bk"></s:select>
  	   	  <td>
		  </td>
		  <td class=column colspan='2' align=right>
		    <input type="button" value=" ͳ �� " name="button" onclick="doQuery()" class=jianbian>
		  </td>
   	    </tr> --%>
  	 </table>
  	 </form>
  </td>
 </tr>
 <!-- end -->
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th rowspan='2'>����</th>
						<th colspan='5'>2021���ն��������ײͲ�ƥ��</th>
						<th colspan='2'>�ն��������ײͲ�ƥ��������</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<th>T0ֵ</th>
						<th>���������</th>
						<th>��������굥</th>
						<th>�ۼ������</th>
						<th>��ƥ���ն��굥</th>
						<th>������</th>
						<th>�����ն��굥</th>
					</tr>
					<s:iterator value="data">
						<tr bgcolor="#FFFFFF">
							<td class=column width="8%">
								<strong><s:property value="city_name" /></strong>
							</td>
							<td width="6%">
								<s:property value="t0num" />
							</td>
							<td width="7%">
								<s:property value="daynomatchnum" />
							</td>
							<td width="6%">
								<a href="javascript:getNoMatchDayDetailList('<s:property value="city_id" />','<s:property value="daynomatchnum" />');">
					 				��ϸ��Ϣ
						 		</a>
							</td>
							<td width="7%">
								<s:property value="allnomatchnum" />
							</td>
							<td width="8%">
								<a href="javascript:getNoMatchDetailList('<s:property value="city_id" />');">
					 				��ϸ��Ϣ
						 		</a>
							</td>
							<td width="5%">
								<s:property value="alladdnomatchnum" />
							</td>
							<td width="8%">
								<a href="javascript:getAddDetailList('<s:property value="city_id" />');">
					 				��ϸ��Ϣ
						 		</a>
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
