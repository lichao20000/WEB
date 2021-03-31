<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
String instName = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<title>用户业务查询</title>
<lk:res />
<script type="text/javascript">

var iframeids=["resultFrame"];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes";

function dyniframesize()
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
	   			dyniframe[i].style.display="block";
	   			//如果用户的浏览器是NetScape
	   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight){
	   				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight+20;
	   			}
	    		//如果用户的浏览器是IE
	   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) {
	   				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
	   			}

 			 }
 		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
  		tempobj.style.display="block";
		}
	}
}


	$(function() {
		//dyniframesize();
		$("#query")
				.click(
						function() {
							if (!validateTime()) {
								return;
							}
							$("#resultFrame").hide("");
							$("tr[@id='trqueryConfig']").css("display","none");
							//$("#tip_loading").show();
							var url = "<s:url value='/gtms/stb/resource/stbCustomer!listCustomer.action'/>";
							$("#queryForm").attr("action", url);
							$("#queryForm").attr("target", "resultFrame");
							$("#queryForm").submit();
						});

		//导出
		$("#export")
				.click(
						function() {

							if (!validateTime()) {
								return;
							}
							$("#queryForm")
									.attr("action",
											"<s:url value='/gtms/stb/resource/stbCustomer!exportCustomer.action'/>");
							$("#queryForm").submit();
						});

		if (!validateTime()) {
			return;
		}
		$("#resultFrame").hide("");
		$("tr[@id='trqueryConfig']").css("display","none");
		//$("#tip_loading").show();
		var url = "<s:url value='/gtms/stb/resource/stbCustomer!listCustomer.action'/>";
		$("#queryForm").attr("action", url);
		$("#queryForm").attr("target", "resultFrame");
		$("#queryForm").submit();
		//dyniframesize();

	})


	/* $(window).resize(function(){
		dyniframesize();
	}); */


	/**
	 *验证开始和结束时间
	 *开始时间大于结束时间返回false
	 */
	function validateTime() {
		var beginTime = $("#startTime").val();
		var endTime = $("#endTime").val();

		if (beginTime > endTime) {
			alert("开始时间不能在结束时间之后");
			return false;
		}
		return true;
	}

	function setDataSize(dataHeight) {
		$("#resultFrame").height(dataHeight);
	}

	function showIframe() {
		//$("#tip_loading").hide();
		$("#resultFrame").show();
	}

	function config(device_id,deviceSn){
		$('#trqueryConfig').hide();
		$('#bssSheetInfo').hide();
		//$('#tip_loading').hide();
		var url = "<s:url value='/gtms/stb/resource/stbInst!getConfigInfoStb.action'/>";
		$.post(url,{
			deviceId: device_id,
			deviceSN: deviceSn
		},function(ajax){
		    $("div[@id='div_config']").html("");
			$("div[@id='div_config']").append(ajax);
			var province = '<%=instName %>';
			if(province="xj_dx"){
				$(".listtable").css("width","100%");
			}
			$("tr[@id='trqueryConfig']").css("display","");
		});
	}

	function configInfoClose(){
		$("tr[@id='trqueryConfig']").css("display","none");
	}
	function bssSheetClose() {
		$('#bssSheetInfo').hide();
	}

	function configDetailInfo(deviceId, deviceSN){
		var page = "<s:url value='/gtms/stb/resource/stbInst!getConfigDetail.action'/>?deviceId=" + deviceId + "&deviceSN=" + deviceSN;
		window.open(page, "", "left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}

	function solutions(resultId, deviceSN){
		var url = "<s:url value='/gtms/stb/resource/stbInst!getSolution.action'/>?deviceSN=" + deviceSN + "&resultId=" + resultId;
		window.open(url, "", "left=50,top=50,width=600,height=450,resizable=yes,scrollbars=yes");
	}

	function configLog(deviceSN, deviceId){
		var page = "<s:url value='/gtms/stb/resource/stbInst!getConfigLogInfo.action'/>?deviceSN=" + deviceSN + "&deviceId=" + deviceId;
		window.open(page, "", "left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
	}
</script>
</head>
<body>
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD >
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
	            	您当前的位置：用户业务查询
			</TD>
		</TR>
	</TABLE>

	<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<form id="queryForm" name="queryForm" method="POST">
			<table id="tblQuery" width="100%" class="querytable">
				<thead>
					<tr>
						<td colspan="4" class="title_1">用户业务查询</td>
					</tr>
				</thead>
				<tr>
					<td width="15%" class="title_2">属地：</td>
					<td width="35%"><select name="customerDTO.cityId" id="cityId" style="width: 150px;">
							<option value=''>请选择</option>
							<s:iterator value="cityList" var="city">
								<option value="<s:property value="#city.city_id"/>">
									<s:property value="#city.space" escapeHtml="false" />
									<s:property value="#city.city_name" />
								</option>
							</s:iterator>
					</select></td>
					<td class="title_2">是否包含下级属地：</td>
					<td><input type="radio" name="customerDTO.subordinate"
						value="1" checked="checked" />是 <input type="radio"
						name="customerDTO.subordinate" value="2" />否</td>
				</tr>
				<tr>
					<td class="title_2">开始时间：</td>
					<td><lk:date id="startTime" name="customerDTO.startTime"
							defaultDate="%{customerDTO.startTime}" /></td>
					<td class="title_2">结束时间：</td>
					<td><lk:date id="endTime" name="customerDTO.endTime"
							defaultDate="%{customerDTO.endTime}" /></td>
				</tr>
				<tr>
					<td class="title_2">业务帐号：</td>
					<td><input type="text" style="width: 150px;"
						name="customerDTO.servAccount" id="servAccount" /></td>
				<s:if test="'sx_lt'!=instArea">
					<td class="title_2">接入账号：</td>
					<td><input type="text" style="width: 150px;"
						name="customerDTO.pppoeUser" id="pppoeUser" /></td>
				</s:if>
				<s:else>
					<td class="title_2">开通状态：</td>
					<td>
						<select name="customerDTO.userStatus" style="width: 150px;">
							<option value="">请选择</option>
							<option value="-1">失败</option>
							<option value="0">未做</option>
							<option value="1">成功</option>
						</select>
					</td>
				</s:else>
				</tr>
				<s:if test="'hn_lt'!=instArea && 'sx_lt'!=instArea">
				<tr>
					<td class="title_2">开通状态：</td>
					<td>
						<select name="customerDTO.userStatus">
							<option value="">请选择</option>
							<option value="-1">失败</option>
							<option value="0">未做</option>
							<option value="1">成功</option>
						</select>
					</td>
					<s:if test="%{instAreaName=='sd_lt'}">
					<td class="title_2">接入方式：</td>
					<td>
						<select name="customerDTO.stbuptyle">
							<option value="-1">请选择</option>
							<option value="1">FTTH</option>
							<option value="2">FTTB</option>
							<option value="3">LAN</option>
							<option value="4">HGW</option>
						</select>
					</td>
					</s:if>
					<s:if test="instAreaName=='jl_dx'">
					<td class="title_2">客户类型：</td>
					<td>
						<select name="customerDTO.custType">
							<option value="-1">请选择</option>
							<option value="4">家庭机顶盒</option>
							<option value="5">政企机顶盒</option>
						</select>
					</td>
					</s:if>
				</tr>
				</s:if>

				<s:if test="instAreaName=='jl_dx' || instAreaName=='hb_lt' || instAreaName=='sx_lt'">
				<tr>
					<td class="title_2">
						<ms:inArea areaCode="sx_lt">
							唯一标识:
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="true">
							LOID:
						</ms:inArea>
					</td>
					<td><input type="text" style="width: 150px;" name="customerDTO.loid" id="loid" /></td>

					<ms:inArea areaCode="sx_lt">
						<td class="title_2">机顶盒序列号</td>
						<td><input type="text" style="width: 150px;" name="customerDTO.deviceSerialnumber" /></td>
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						<td class="title_2"></td>
						<td></td>
					</ms:inArea>
				</tr>
				</s:if>
				<tr>
					<td class="foot" colspan="4">
						<div style="float: right">
							<button id="query">查询</button>
							<button id="export">导出</button>
						</div>
					</td>
				</tr>
			</table>

			<iframe id="resultFrame" name="resultFrame" height="0" frameborder="0"
					scrolling="no" width="100%" src=""></iframe>
		</form>
		</td>
	</tr>
	<TR id="trqueryConfig" style="display: none">
		<td>
			<div  align="center" id="div_config"></div>
		</td>
	</TR>
	<tr>
		<td id="bssSheetInfo">

		</td>
	</tr>
	<tr>
		<td>
			<div style="width: 100%; display: none; text-align: center;"
				id="tip_loading">
				正在加载数据,请耐心等待......
			</div>
		</td>
	</tr>
	</table>
</body>
</html>
