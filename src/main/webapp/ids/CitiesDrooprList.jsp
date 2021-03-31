<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<%
	/**
	 *  地市设备光衰详细信息
	 * 
	 * @author songjj
	 * @version 1.0
	 * @since 2015-05-08
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
$(function() {
	$("#trData",parent.document).hide();
	$("#btn",parent.document).attr('disabled',false);
	parent.dyniframesize();
});

function ListToExcel(starttime,endtime,cityId,operType){
	var page = "<s:url value='/ids/cityDroop!queryByDroopExcel.action'/>?"
		+"starttime="+starttime
		+ "&endtime=" + endtime
		+ "&cityId=" + cityId
		+ "&operType=" + operType;
	document.all("childFrm").src=page;		
}

function getDetail(query_type){
	var url = "<s:url value='/ids/cityDroop!getDetail.action'/>";
    if(query_type == 1){
    	var queryVal = $.trim($("input[@name='loid']").val());
    }else{
    	var queryVal = $.trim($("input[@name='device_serialnumber']").val());
    }
	$.post(url,{
		query_type:query_type,
		queryVal:queryVal
	},function(ajax){
		var page,user_id,device_id;
		if(query_type == 1){
			if(ajax == 0){
				alert("设备暂无用户");
				return;
			}else{
				page = "<s:url value='/bbms/customerDetail.jsp' />?user_id=" + ajax;
			}
		}else{
			if(ajax == 0){
				alert("暂无设备信息");
				return;
			}else{
				page = "<s:url value='/Resource/DeviceShow.jsp' />?device_id=" + ajax;
			}
		} 
		window.open(page,"","left=200,top=20,width=850,height=600,resizable=no,scrollbars=yes");
	});
}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th >属 地</th>
				<th >逻辑SN</th>
				<th >设备序列号</th>
				<th >上报时间</th>
				<th >入库时间</th>
				<th >光衰</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="rlist!=null">
				<s:if test="rlist.size()>0">
					<s:iterator value="rlist">
						<tr>
							<td>
								<s:property value="city_name" />
							</td>
							<td>
								<a href="javascript:getDetail(1);">
									<s:property value='loid'/>
									<input style="display: none;" type="text" name="loid" value="<s:property value='loid'/>" />
								</a>
							</td>
							<!-- '<s:property value="device_serialnumber"/>' -->
							<td id="device_serialnumber">
								<a href="javascript:getDetail(2);">
									<s:property value='device_serialnumber'/>
									<input style="display: none;" type="text" name="device_serialnumber" value="<s:property value='device_serialnumber'/>" />
								</a>
							</td>
							<td>
								<s:property value="upload_time" />
							</td>
							<td>
								<s:property value="add_time" />
							</td>
							<td >
								<s:property value="droop" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6>系统没有匹配到相应信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>系统没有匹配到相应信息!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="6"><span style="float:right"><lk:pages
						url="/ids/cityDroop!queryByDroopList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></span>
					<s:if test="operType==1">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
							style='cursor: hand;float: left'
							onclick="ListToExcel('<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="cityId"/>','1')">	
					</s:if>		
					<s:elseif test="operType==2">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
							style='cursor: hand;float: left'
							onclick="ListToExcel('<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="cityId"/>','2')">	
					</s:elseif>
				</td>
			</tr>
			<tr>
			<td align="center" colspan="7">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</td>
		</tr>
		<tr STYLE="display: none">
				<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
		</tr>
			</tfoot>
	</table>
</body>
</html>