<!-- 新疆机顶盒零配置 -->
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">

<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<script language="JavaScript">

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//如果用户的浏览器是NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
    			//如果用户的浏览器是IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

function doQuery(){
	var user_info = $("input[@name='user_info']").val().trim();
	var device_sn = $("input[@name='device_sn']").val().trim();
	var service_type = $("select[@name='service_type']").val().trim();
	var operate_type = $("select[@name='operate_type']").val().trim();
	var cityId = $("select[@name='cityId']").val().trim();
	var starttime = $("input[@name='starttime']").val().trim();
	var endtime = $("input[@name='endtime']").val().trim();
	var cpe_mac = $("input[@name='cpe_mac']").val().trim();
	
	if(""==user_info && ""==device_sn && ""==cpe_mac){
		alert("账号/LOID和设备序列号至少填一项！");
		return false;
	}
	
	if(service_type=="-1"){
		alert("请选择业务类型！");
		return false;
	}
	
	var sValue=starttime.substring(5)+"-"+starttime.substring(0,4);	
	var s_date=new Date(Date.parse(sValue));
	var s_day=s_date.getDate();
	var s_month=s_date.getMonth()+1;
	var s_year=s_date.getFullYear();
	
	var eValue=endtime.substring(5)+"-"+endtime.substring(0,4);	
	var e_date=new Date(Date.parse(eValue));
	var e_day=e_date.getDate();
	var e_month=e_date.getMonth()+1;
	var e_year=e_date.getFullYear();

	if(e_year-s_year==1){
		e_month=e_month+12;
	}
	
	if((e_year-s_year>1) || (e_month-s_month>1) || (e_month>s_month && e_day>s_day)){
		alert("接收日期跨度不能大于一个月！");
		return false;
	}
	
	var url="<s:url value='itms/service/zeroconfiguration_XJ!query.action'/>";
	document.frm.submit();
}


</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>新疆机顶盒零配置</th>
					<td align="left">
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">机顶盒零配置
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" method="post" action="<s:url value='itms/service/zeroconfiguration_XJ!query.action'/>" 
				onSubmit="return false" target="dataForm" >
				<table class="querytable">
					<tr>
						<th colspan=4>机顶盒零配置</th>
					</tr>
					<tr bgcolor="#ffffff">
						<td class="column" align="center" width="15%">
							账号/LOID
						</td>
						<td>
							<input type="text" name="user_info" />
						</td>
						<td class="column" align="center" width="15%">
							设备序列号
						</td>
						<td>
							<input type="text" name="device_sn" /><span style="color:red">(若同时输入LOID和设备序列号查询，只按LOID查询)</span>
						</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td class="column" align="center" width="15%">
							业务类型
						</td>
						<td>
							<select name="service_type" class="bk">
								<option value="4">机顶盒</option>								
						</select>
							<font color="red">*</font>
						</td>
						<td class="column" align="center" width="15%">
							操作类型
						</td>
						<td>
							<s:select list="operate_typeList" name="operate_type" headerKey="-1"
									headerValue="请选择" listKey="operate_type" listValue="operate_type_name"
									value="operate_type" cssClass="bk">
							</s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" align="center" width="15%">
								属地
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
									headerValue="请选择属地" listKey="city_id" listValue="city_name"
									value="cityId" cssClass="bk">
							</s:select>
						</td>
						<td class=column align=center width="25%">
							接收日期
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
							&nbsp;~&nbsp;
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" align="center" width="15%">
							MAC地址
						</td>
						<td colspan="3">
							<input type="text" name="cpe_mac" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="foot" colspan="4" align="right">
							<button onclick="doQuery();" name="subBtn">&nbsp;查询&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999 id="idData" >
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""/>
		</td>
	</tr>
	
</table>

<%@ include file="/foot.jsp"%>

