<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/mq_table.css"/>"
	type="text/css"/>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"/>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function queryMqDetail() {
	var mqId = $("select[@name='mqId']").val();
	var topicName = $.trim($("input[@name='topicName']").val());
	var starttime = $.trim($("input[@name='starttime']").val());
	var endtime = $.trim($("input[@name='endtime']").val());
	var frm = document.getElementById("frm");
	document.all.mqTable.style.display = 'none';
	 
	frm.action = "<s:url value='/itms/report/queryMq!queryMqDetail.action'/>";
	frm.submit();
	document.all("tr_deviceinfo").style.display = "";
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//如果用户的浏览器是NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//如果用户的浏览器是IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(window).resize(function() {
	dyniframesize();
});
</script>
<body>
<Table>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<th>MQ详情列表查询</th>
						<td align="left"><img
							src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<form name="frm" id="frm" target="dataForm">
					<table class="querytable">
						<tr>
							<th colspan=4>MQ查询</th>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=column align="center" width="15%">开始时间</td>
							<td width="35%"><input type="text" name="starttime"
								class='bk' readonly value="<s:property value='starttime'/>" /> <img
								name="shortDateimg"
								onclick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" /></td>
							<td class=column align="center" width="15%">结束时间</td>
							<td width="35%"><input type="text" name="endtime"
								class='bk' readonly value="<s:property value='endtime'/>" /> <img
								name="shortDateimg"
								onclick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" /></td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=column align="center" width="15%">MQ设备</td>
							<td width="35%"><s:select list="mqList" name="mqId"
									headerKey="-1" headerValue="全部" listKey="id"
									listValue="mq_ipaddress" cssClass="select" style="width:120px;"></s:select>
							</td>
							<td class=column align="center" width="15%">MQ主题</td>
							<td width="35%"><input type="text" name="topicName"/></td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=foot colspan=4 align=right>
								<button id="queryButton" onclick="queryMqDetail();">&nbsp;查询&nbsp;</button>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		
		<tr style="display: none" id="tr_deviceinfo">
			<td>
				<div id="div_device" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</td>
		</tr>
	</Table>
<br/>
<div class="content">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
	</div>
<table class="listtable" id="mqTable">
	<caption>
		设备列表
	</caption>
	<thead>
		<tr>
			<th>
				订阅者Id
			</th>
			<th>
				订阅者名称
			</th>
			<th>
				订阅主题
			</th>
			<th>
				挂起队列大小
			</th>
			<th>
				调度队列大小
			</th>
			<th>
				调度计数器
			</th>
			<th>
				入列计数器
			</th>
			<th>
				出列计数器
			</th>
			<th>
				时间
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="mqDetailList.size()>0">
			<s:iterator value="mqDetailList">
				<tr align="center">
					<td>
						<s:property value="client_id" />
					</td>
					<td>
						<s:property value="subscripion_name" />
					</td>
					<td>
						<s:property value="destination_topic" />
					</td>
					<td>
						<s:property value="pending_queue_size" />
					</td>
					<td>
						<s:property value="dispatched_queue_size" />
					</td>
					<td>
						<s:property value="dispatched_counter" />
					</td>
					<td>
						<s:property value="enqueue_counter" />
					</td>
					<td>
						<s:property value="dequeue_counter" />
					</td>
					<td>
						<s:property value="gathertime" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>
					系统没有相关的用户信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9" align="right" height="15">[ 统计总数 : <s:property value='queryCount'/> ]&nbsp;<lk:pages
						url="/itms/report/queryMq!getMqDetail.action" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>

		<tr>
			<td align="center" colspan="9">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</td>
		</tr>
	</tfoot>

</table>

	
</body>
<%@ include file="/foot.jsp"%>
