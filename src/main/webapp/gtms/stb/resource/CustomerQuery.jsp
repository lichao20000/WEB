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

<title>�û�ҵ���ѯ</title>
<lk:res />
<script type="text/javascript">

var iframeids=["resultFrame"];

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";

function dyniframesize()
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
	   			dyniframe[i].style.display="block";
	   			//����û����������NetScape
	   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight){
	   				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight+20;
	   			}
	    		//����û����������IE
	   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) {
	   				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
	   			}

 			 }
 		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
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

		//����
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
	 *��֤��ʼ�ͽ���ʱ��
	 *��ʼʱ����ڽ���ʱ�䷵��false
	 */
	function validateTime() {
		var beginTime = $("#startTime").val();
		var endTime = $("#endTime").val();

		if (beginTime > endTime) {
			alert("��ʼʱ�䲻���ڽ���ʱ��֮��");
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
	            	����ǰ��λ�ã��û�ҵ���ѯ
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
						<td colspan="4" class="title_1">�û�ҵ���ѯ</td>
					</tr>
				</thead>
				<tr>
					<td width="15%" class="title_2">���أ�</td>
					<td width="35%"><select name="customerDTO.cityId" id="cityId" style="width: 150px;">
							<option value=''>��ѡ��</option>
							<s:iterator value="cityList" var="city">
								<option value="<s:property value="#city.city_id"/>">
									<s:property value="#city.space" escapeHtml="false" />
									<s:property value="#city.city_name" />
								</option>
							</s:iterator>
					</select></td>
					<td class="title_2">�Ƿ�����¼����أ�</td>
					<td><input type="radio" name="customerDTO.subordinate"
						value="1" checked="checked" />�� <input type="radio"
						name="customerDTO.subordinate" value="2" />��</td>
				</tr>
				<tr>
					<td class="title_2">��ʼʱ�䣺</td>
					<td><lk:date id="startTime" name="customerDTO.startTime"
							defaultDate="%{customerDTO.startTime}" /></td>
					<td class="title_2">����ʱ�䣺</td>
					<td><lk:date id="endTime" name="customerDTO.endTime"
							defaultDate="%{customerDTO.endTime}" /></td>
				</tr>
				<tr>
					<td class="title_2">ҵ���ʺţ�</td>
					<td><input type="text" style="width: 150px;"
						name="customerDTO.servAccount" id="servAccount" /></td>
				<s:if test="'sx_lt'!=instArea">
					<td class="title_2">�����˺ţ�</td>
					<td><input type="text" style="width: 150px;"
						name="customerDTO.pppoeUser" id="pppoeUser" /></td>
				</s:if>
				<s:else>
					<td class="title_2">��ͨ״̬��</td>
					<td>
						<select name="customerDTO.userStatus" style="width: 150px;">
							<option value="">��ѡ��</option>
							<option value="-1">ʧ��</option>
							<option value="0">δ��</option>
							<option value="1">�ɹ�</option>
						</select>
					</td>
				</s:else>
				</tr>
				<s:if test="'hn_lt'!=instArea && 'sx_lt'!=instArea">
				<tr>
					<td class="title_2">��ͨ״̬��</td>
					<td>
						<select name="customerDTO.userStatus">
							<option value="">��ѡ��</option>
							<option value="-1">ʧ��</option>
							<option value="0">δ��</option>
							<option value="1">�ɹ�</option>
						</select>
					</td>
					<s:if test="%{instAreaName=='sd_lt'}">
					<td class="title_2">���뷽ʽ��</td>
					<td>
						<select name="customerDTO.stbuptyle">
							<option value="-1">��ѡ��</option>
							<option value="1">FTTH</option>
							<option value="2">FTTB</option>
							<option value="3">LAN</option>
							<option value="4">HGW</option>
						</select>
					</td>
					</s:if>
					<s:if test="instAreaName=='jl_dx'">
					<td class="title_2">�ͻ����ͣ�</td>
					<td>
						<select name="customerDTO.custType">
							<option value="-1">��ѡ��</option>
							<option value="4">��ͥ������</option>
							<option value="5">���������</option>
						</select>
					</td>
					</s:if>
				</tr>
				</s:if>

				<s:if test="instAreaName=='jl_dx' || instAreaName=='hb_lt' || instAreaName=='sx_lt'">
				<tr>
					<td class="title_2">
						<ms:inArea areaCode="sx_lt">
							Ψһ��ʶ:
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="true">
							LOID:
						</ms:inArea>
					</td>
					<td><input type="text" style="width: 150px;" name="customerDTO.loid" id="loid" /></td>

					<ms:inArea areaCode="sx_lt">
						<td class="title_2">���������к�</td>
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
							<button id="query">��ѯ</button>
							<button id="export">����</button>
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
				���ڼ�������,�����ĵȴ�......
			</div>
		</td>
	</tr>
	</table>
</body>
</html>
