<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

$(function(){
	var queryFlag = '<s:property value="queryFlag" />';
	if("query"==queryFlag){
		$("tr[@id='trData']").show();
	}else{
		$("tr[@id='trData']").hide();
	}
});

function doQuery(){
	var username = $.trim($("input[@name='username']").val());
	var mac = $.trim($("input[@name='mac']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());  
   
    var url = '<s:url value='/itms/resource/hgwByMac.action'/>'; 
	document.frm.submit();
	
}


</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						DSLAM侧MAC账号查询
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
						对IPOSS提供的用户账号和MAC地址进行查询
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm"
				action="<s:url value="/itms/resource/hgwByMac.action"/>">
				<input type="hidden" name="queryFlag" value="query" />
				<table class="querytable">
					<tr>
						<th colspan=4>
							DSLAM侧MAC账号查询
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							用户账号
						</td>
						<td>
							<input type="text" name="username"
								value="<s:property value="username" />">
						</td>

						<td class=column align=center width="15%">
							设备MAC地址
						</td>
						<td>
							<input type="text" name="mac" value="<s:property value="mac" />">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value="starttime" />">
							<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value="endtime" />">
							<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;查 询&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<table class="listtable">
				<caption>
					查询结果
				</caption>
				<thead>
					<tr>
						<th>
							属地
						</th>
						<th>
							用户账号
						</th>
						<th>
							设备MAC地址
						</th>
						<th>
							采集时间
						</th>
						<th>
							DSLAM的IP
						</th>
						<th>
							DSLAM端口信息
						</th>
					</tr>
				</thead>
				<tbody>
					<s:if test="hgwList!=null">
						<s:if test="hgwList.size()>0">
							<s:iterator value="hgwList">
								<tr>
									<td>
										<s:property value="city_name" />
									</td>
									<td>
										<s:property value="username" />
									</td>
									<td>
										<s:property value="cpe_mac" />
									</td>
									<td>
										<s:property value="gather_time" />
									</td>
									<td>
										<s:property value="dslam_ip" />
									</td>
									<td>
										<s:property value="dslam" />
									</td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
							<td colspan=6>
								系统没有相关的用户信息!
							</td>
						</s:else>
					</s:if>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="6">
							<lk:pages url="/itms/resource/hgwByMac.action" styleClass=""
								showType="" isGoTo="true" changeNum="true" />
						</td>
					</tr>
				</tfoot>
				<tr STYLE="display: none">
					<td colspan="6">
						<iframe id="childFrm" src=""></iframe>
					</td>
				</tr>

			</table>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
