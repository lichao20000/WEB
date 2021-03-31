<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%--
	/**
	 * 规范版本查询页面
	 *
	 * @author 姓名(工号) Tel:电话
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>
 
<%
	UserRes current_user = (UserRes) session.getAttribute("curUser");
	String city_id = current_user.getUser().getCityId();
%>
 
<html>
	<head>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		
		<lk:res />
		
		<script language="JavaScript">
		
			var isFiber = '<s:property value="isFiber" />';
		
			function doQuery(){
			    var cityId =  $("select[@name='cityId']").val();
			    var starttime = $.trim($("input[@name='starttime']").val());// 开始时间(注册时间)
			    var endtime = $.trim($("input[@name='endtime']").val());    // 结束时间(注册时间)
			    
			    if(starttime > endtime){
			    	alert("开始时间不能大于结束时间");
			    	return;
			    }
			    
			    $("tr[@id='trData']").show();
			    $("div[@id='QueryData']").html("正在统计，请稍等....");
			    var url = "<s:url value='/gtms/report/nxFtthBindReport!countAll.action'/>"; 
				$.post(url,{
					cityId:cityId,
					starttime:starttime,
					endtime:endtime,
					isFiber:isFiber
				},function(ajax){
				    $("div[@id='QueryData']").html("");
					$("div[@id='QueryData']").append(ajax);
				});
			}
			
			function countBycityId(cityId,starttime,endtime, isFiber){
			
			    $("tr[@id='trData']").show();
			    $("div[@id='QueryData']").html("正在统计，请稍等....");
			    var url = "<s:url value='/gtms/report/nxFtthBindReport!countAll.action'/>"; 
				$.post(url,{
					cityId:cityId,
					starttime:starttime,
					endtime:endtime,
					isFiber:isFiber
				},function(ajax){	
				    $("div[@id='QueryData']").html("");
					$("div[@id='QueryData']").append(ajax);
				});
			}
			
			function ToExcel(cityId,starttime1,endtime1, isFiber) {
				var page="<s:url value='/gtms/report/nxFtthBindReport!getAllBindWayExcel.action'/>?"
					+ "cityId=" + cityId
					+ "&starttime1=" + starttime1
					+ "&endtime1=" + endtime1
					+ "&isFiber=" + isFiber;
				document.all("childFrm").src=page;
			}
			
			
			function openHgw(bindFlag, cityId, starttime1, endtime1, isFiber){
				
				var  page="<s:url value='/gtms/report/nxFtthBindReport!getUserList.action'/>?"
						+ "bindFlag=" + bindFlag 
						+ "&cityId=" + cityId 
						+ "&starttime1=" +starttime1
						+ "&endtime1=" +endtime1
						+ "&isFiber=" + isFiber;
				window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
			}
		
		</script>
	
	</head>
	
	<TABLE>
		<tr>
		<td>
			<table class="green_gargtd">
				<tr>
				<th>FTTH用户绑定情况统计 </th>
				<td>
					<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
					只统计光纤用户，接入方式为epon和gpon
				</td>
				</tr>
			</table>
		</td>
		</tr>
		<tr>
		<td>
			<form name=frm>
			<table class="querytable">
				<tr> <th colspan=4> FTTH用户绑定情况统计 </th> </tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center width="15%"> 开始时间 </td>
					<td>
						<input type="text" name="starttime" class='bk' readonly value="<s:property value="starttime"/>">
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15" height="12"
							border="0" alt="选择" />
					</td>
					<td class=column align=center width="15%">
						结束时间
					</td>
					<td>
						<input type="text" name="endtime" class='bk' readonly value="<s:property value="endtime"/>">
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择" />
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center width="15%">
							属 地
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=foot colspan=4 align=right>
						<button onclick="doQuery('<%=city_id %>')">&nbsp;统 计&nbsp;</button>
					</td>
				</tr>
			</table>
			</form>
		</td>
		</tr>
	
		<tr id="trData" style="display: none">
			<td class="colum">
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
					正在统计，请稍等....
				</div>
			</td>
		</tr>
		<tr>
			<td height="20"> </td>
		</tr>
	</TABLE>
	<%@ include file="/foot.jsp"%>
</html>
