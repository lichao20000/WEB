<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
//报废终端详情
function getSrcapDevDetailList(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!querySrcapDevDetailList.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
//第一批未完成量详情
function getOldDevDetail(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryOldDevDetailList.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
//完成量总量详情
function getOldCompleteDevDetail(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryOldCompleteDevDetailList.action'/>?"+
			"cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

//日完成量详情
function getOldDayCompleteDevDetail(city_id,needCount){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryOldDayCompleteDevDetailList.action'/>?"
			+ "cityId=" + city_id+"&needCount="+needCount;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

//PT921G未完量详情
function getPt921gDetail(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryPt921gDetail.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

//第三批未完成量详情
function getThrdDetail(city_id){
	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryThrdDetail.action'/>?"+ "cityId=" + city_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
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
                        <TD width="164" align="center" class="title_bigwhite">老旧终端更换</TD>
                        <td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
                                        width="15" height="12"> &nbsp;老旧终端报废报表
                        </td>
                    </TR>
                </TABLE>
            </TD>
    </TR>
    <tr>
  <td>
  <form name="frm" action="/gwms/report/terminalNoMatchReport!devMisMatch.action" method='POST'>
  	 <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999" style="margin-top: 5px">
  	 	<%-- <tr>
  	 		<TH colspan="4">老旧终端报废报表</TH>
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
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th rowspan="3" width="6%">地州</th>
						<th colspan="13" width="74%">2021年老旧终端更换</th>
                   <!-- <th colspan="2" width="20%">报废终端再次上线（4月30日后禁止上线）</th> -->
                        <th colspan="2" width="20%">报废终端再次上线（2021年）</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<th colspan='7'>第一批老旧终端</th>
						<th colspan='3'>PT921G老旧终端</th>
						<th colspan='3'>第三批老旧终端(11种型号)</th>
						<th rowspan='2'>数量</th>
						<th rowspan='2'>详情</th>
					</tr>
					<tr bgcolor="#FFFFFF">
				        <!-- 第一批 -->
						<th>T0值</th>
						<th>未完成量</th>
						<th>未完成量详单</th>
						<th>每天完成量</th>
						<th>日完成量详单</th>
						<th>完成总量</th>
                        <th>完成总量详单</th>
                        <!-- 第一批end-->
                        
                        <!-- PT921G -->
                        <th>现网在用T0</th>
						<th>未完成量</th>
						<th>详情</th>
                        <!-- PT921G end-->
                        
                        <!-- 第三批老旧终端(11种型号) -->
                        <th>现网在用T0</th>
						<th>未完成量</th>
						<th>详情</th>
                        <!-- 第三批老旧终端(11种型号) end-->
					</tr>
					<s:iterator value="data">
						<tr bgcolor="#FFFFFF">
						<td class=column >
								<strong><s:property value="city_name" /></strong>
							</td>
						    <!-- 第一批 -->
							<td>
                            	<s:property value="first_oldt0" />
                            </td>
                            <td>
                                <s:property value="first_oldnum" />
                            </td>
                            <td>
                                <a href="javascript:getOldDevDetail('<s:property value="city_id" />');">
                                    		详细信息
                                </a>
                            </td>
                            <td>
                                <s:property value="first_day_complete_num" />
                            </td>
                            <td>
                                <a href="javascript:getOldDayCompleteDevDetail('<s:property value="city_id" />','<s:property value="first_day_complete_num" />');">
                                    		详细信息
                                </a>
                            </td>
                            <td>
                                <s:property value="first_old_complete_num" />
                            </td>
                            <td>
                                <a href="javascript:getOldCompleteDevDetail('<s:property value="city_id" />');">
                                    		详细信息
                                </a>
                            </td>
                             <!-- 第一批end -->
                             
                              <!-- PT921G -->
                              <td>
                            	<s:property value="sec_t0" />
                              </td>
                              <td>
                                <s:property value="sec_nocplt_num" />
                              </td>
                              <td>
                                <a href="javascript:getPt921gDetail('<s:property value="city_id" />');">详细信息
                                </a>
                              </td>
                              <!-- PT921G end -->
                              
                              <!-- 第三批 -->
                              <td>
                            	<s:property value="thrd_t0" />
                              </td>
                              <td>
                                <s:property value="thrd_nocplt_num" />
                              </td>
                              <td>
                                <a href="javascript:getThrdDetail('<s:property value="city_id" />');">
                                    		详细信息
                                </a>
                              </td>
                              <!-- 第三批end -->
                              <!-- 报废终端 -->
							<td>
								<s:property value="scrapdevnum" />
							</td>
							<td>
								<a href="javascript:getSrcapDevDetailList('<s:property value="city_id" />');">
					 				详细信息
						 		</a>
							</td>
							<!-- 报废终端end -->
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
