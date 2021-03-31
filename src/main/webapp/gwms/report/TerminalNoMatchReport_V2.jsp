<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

//新增终端详单 
function getAddDetailList(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryDetailList.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

//不匹配终端详单
function getNoMatchDetailList(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryNoMatchDetailList.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}	

//不匹配终端当日完成量详单
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
                        <TD width="164" align="center" class="title_bigwhite">终端不匹配</TD>
                        <td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
                                        width="15" height="12"> &nbsp;终端能力与套餐不匹配报表
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
  	 		<TH colspan="4">终端能力与套餐不匹配报表</TH>
  	 	</tr>
  	    <tr bgcolor=#ffffff>
  	   	  <td class=column align=right>地市</td>
  	   	     <s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="请选择属地" listKey="city_id" listValue="city_name"
										value="cityId" cssClass="bk"></s:select>
  	   	  <td>
		  </td>
		  <td class=column colspan='2' align=right>
		    <input type="button" value=" 统 计 " name="button" onclick="doQuery()" class=jianbian>
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
						<th rowspan='2'>地州</th>
						<th colspan='5'>2021年终端能力与套餐不匹配</th>
						<th colspan='2'>终端能力与套餐不匹配新增量</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<th>T0值</th>
						<th>当日完成量</th>
						<th>当日完成详单</th>
						<th>累计完成量</th>
						<th>不匹配终端详单</th>
						<th>新增量</th>
						<th>新增终端详单</th>
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
					 				详细信息
						 		</a>
							</td>
							<td width="7%">
								<s:property value="allnomatchnum" />
							</td>
							<td width="8%">
								<a href="javascript:getNoMatchDetailList('<s:property value="city_id" />');">
					 				详细信息
						 		</a>
							</td>
							<td width="5%">
								<s:property value="alladdnomatchnum" />
							</td>
							<td width="8%">
								<a href="javascript:getAddDetailList('<s:property value="city_id" />');">
					 				详细信息
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
