<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="../../timelater.jsp"%>
<input type="hidden" value="<%=request.getAttribute("mycityId")%>"
	id="mycityId">
<input type="hidden" value="<s:property value="prox_serv"/>"
	id="prox_serv">
<input type="hidden" value="<s:property value="city_id"/>" id="id">
<input type="hidden" value="<s:property value="city_id"/>" id="city_id">
<input type="hidden" value="<%=request.getAttribute("a")%>" id="MGCQueryUrl">
<input type="hidden" value="<%=request.getAttribute("b")%>" id="MGCPaceUrl">
<input type="hidden" value="<%=request.getAttribute("c")%>" id="MGCDownLoadUrl">
<input type="hidden" value="<%=curUser.getUser().getAccount()%>" id="curUserAccount">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户查询</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/bootstrap.min.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	function closeBg() {
		$("#fullbg").css("display", "none");
		$("#dialog").css("display", "none");
	}
    
	function ListToExcel() {
		var curUserAccount = $("#curUserAccount").val();
		$("div[@id='success']").hide();
		$("div[@id='exporting']").show();
		$("div[@id='download']").hide();
		$("div[@id='close']").hide();
		var MGCQueryUrl = $("#MGCQueryUrl").val();
		var MGCPaceUrl = $("#MGCPaceUrl").val();
		var MGCDownLoadUrl = $("#MGCDownLoadUrl").val();

		var bh = $("body").height();
		var bw = $("body").width();
		$("#fullbg").css({
			height : bh,
			width : bw,
			display : "block"
		});
		$("#dialog").show();

		var prox_serv = $("#prox_serv").val();
		var fileType = "xls";
		var modelName = 'model1';
		var cityIds = $("#mycityId").val();
		var cityId = $("#city_id").val();
		var prox_serv = $("#prox_serv").val();
		var fileName = "MGCInfo_"+curUserAccount+"_"+cityId+"_"+prox_serv;
		//var url = 'http://202.102.39.141:7070/dataexport/demos/exportData.do';
		var sql = "select a.city_id,a.username as loid,c.prox_serv,c.prox_port,c.stand_prox_serv,c.stand_prox_port from  tab_hgwcustomer a,tab_voip_serv_param b,tab_sip_info c where a.user_id = b.user_id and b.sip_id = c.sip_id and c.prox_serv='"
				+ prox_serv + "' and a.city_id in (" + cityIds + ")"
		$
				.post(
						MGCQueryUrl,
						{
							modelName : modelName,
							fileName : fileName,
							fileType : fileType,
							sql : sql,
							sqlColumn : 'city_id,loid,prox_serv,prox_port,stand_prox_serv,stand_prox_port',
							column : '属地,LOID,主用MGC地址,主要MGC端口,备用MGC地址,备用MGC端口',
							cityName : '1'
						},
						function(data) {
							if (data == -1) {
								//show progress bar
								$(".progress").show();
								pgbFunction(modelName, fileName, fileType);
							}
							else
							{
								$("div[@id='success']").show();
								$("div[@id='exporting']").hide();
								$(".progress").hide();
								tmpDate = new Date();
								month = tmpDate.getMonth() + 1;
								date = tmpDate.getDate() + ".xls";
								var end = month + date;
								$("#download")
										.html(
												"<a href='"+MGCDownLoadUrl+fileName+end+"'>下载</a>");
								$("div[@id='download']").show();
								$("div[@id='close']").show();
							}
						});

		var timeOutId = 0;
		var pgbFunction = function(modelName, fileName, fileType) {
			//var url = "http://202.102.39.141:7070/dataexport/demos/getPercent.do";
			$.post(MGCPaceUrl, {
				modelName : modelName,
				fileName : fileName,
				type : fileType
			}, function(data) {
				percent = data;
				$(".progress-bar").css("width", data + "%");
				if (data <= 99) {
					timeOutId = setTimeout(function() {
						pgbFunction(modelName, fileName, fileType)
					}, 300);
					console.log(timeOutId);
				}
				if (data == 100) {
					$("div[@id='success']").show();
					$("div[@id='exporting']").hide();
					//隐藏进度条
					$(".progress").hide();
					//设置宽度为0 方便下次调用
					$(".progress-bar").css("width", "0%");
					//remove disabled
					$("button").attr("disabled", false);
					clearTimeout(timeOutId);
					tmpDate = new Date();
					month = tmpDate.getMonth() + 1;
					date = tmpDate.getDate() + ".xls";
					var end = month + date;
					$("#download")
							.html(
									"<a href='"+MGCDownLoadUrl+fileName+end+"'>下载</a>");
					$("div[@id='download']").show();
					$("div[@id='close']").show();
				}
			});
		}
	}
</script>
<style type="text/css">
#fullbg {
	background-color: Gray;
	left: 0px;
	opacity: 0.5;
	position: absolute;
	top: 0px;
	z-index: 3;
	filter: alpha(opacity =                         50); /* IE6 */
	-moz-opacity: 0.5; /* Mozilla */
	-khtml-opacity: 0.5; /* Safari */
}

#dialog {
background-color: #CCDDFF;
	border: 1px solid #888;
	display: none;
	height: 30%;
	width: 60%;
	margin: -10% 0 0 -30%;
	padding: 12px;
	position: fixed !important; /* 浮动对话框 */
	position: absolute;
	z-index: 5;
	position: absolute;
}

#dialog p {
	margin: 0 0 12px;
}

#dialog p.close {
	text-align: right;
}
</style>
</head>
<body>
	<div id="main">
		<table class="listtable">
			<caption>主用MGC统计信息详单</caption>
			<thead>
				<tr>
					<th>属地</th>
					<th>LOID</th>
					<th>主用MGC地址</th>
					<th>主要MGC端口</th>
					<th>备用MGC地址</th>
					<th>备用MGC端口</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="devList.size()>0">
					<s:iterator value="devList">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="loid" /></td>
							<td><s:property value="prox_serv" /></td>
							<td><s:property value="prox_port" /></td>
							<td><s:property value="stand_prox_serv" /></td>
							<td><s:property value="stand_prox_port" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="6">系统没有相关的设备信息!</td>
					</tr>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="6"><span style="float: right;"> <lk:pages
								url="/itms/report/statisticalLordMGCAddressQuery!getDevListForWbdTerminal.action"
								styleClass="" showType="" isGoTo="true" changeNum="true" />
					</span></td>
				</tr>
				<tr>
					<td colspan="6" align="right"><input type="hidden" name="time"
						id="time" value="<s:property value='time' />" /> <IMG
						SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand' onclick="ListToExcel()"></td>
				</tr>
				<TR>
					<TD align="center" colspan="6">
						<button onclick="javascript:window.close();">&nbsp;关
							闭&nbsp;</button>
					</TD>
				</TR>
			</tfoot>

			<tr STYLE="display: none">
				<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</table>
	</div>
	<div id="fullbg"></div>
	<div id="dialog">
		<table>
		<tr>
				<td colspan="2">&nbsp;</td>
			</tr><tr>
				<td colspan="2">&nbsp;</td>
			</tr><tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="progress progress-striped active">
						<div class="progress-bar" role="progressbar" aria-valuenow="45"
							aria-valuemin="0" aria-valuemax="100" style="width: 0%">
							<span class="sr-only">0% Complete</span>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;"><div id="success" style="display: none;"><font color="red">您需要导出的数据文件已生成，请点击下面连接进行下载</font></div>
				<div id="exporting" style="display: none;"><font color="red">正在为您生成所需要的数据文件，请耐心等待......</font></div>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td style="text-align: center;"><div id="download"
						style="display: none;"></div></td>
				<td style="text-align: center;"><div id="close"
						style="display: none;">
						<a onclick="closeBg();" href="#">关闭</a>
					</div></td>
			</tr>
		</table>
	</div>
</body>
</html>


<%@ include file="/foot.jsp"%>