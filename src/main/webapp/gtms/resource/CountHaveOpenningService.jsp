<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type" />';

function countBycityId(cityId){

    var url = "<s:url value='/gtms/resource/countByServTypeId!countHaveOpenningService.action'/>"; 
	$.post(url,{
		cityId:cityId,
		gw_type:gw_type
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}


function ToExcel(cityId) {
	var page="<s:url value='/gtms/resource/countByServTypeId!outputHaveOpenServiceToExcel.action'/>?"
		+ "&cityId=" + cityId
		+ "&gw_type=" + gw_type;
	document.all("childFrm").src=page;
}

function detail(cityId,servTypeId){
	var page="<s:url value='/gtms/resource/countByServTypeId!getDetail.action'/>?"
		+ "cityId=" + cityId 
		+ "&servTypeId=" +servTypeId
		+ "&isOpen=1"   // 已开通业务详细信息
		+ "&gw_type=" + gw_type;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

</SCRIPT>


<%@ include file="../../head.jsp"%>
<BR>
<BR>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							已开通业务设备统计
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD class="colum">
				<table class="listtable">
					<thead>
						<tr>
							<th> 属地 </th>
							<s:if test="gw_type==1">
								<th> 家庭网关宽带业务 </th>
								<th> 家庭网关IPTV业务 </th>
								<th> 家庭网关VOIP业务 </th>
							</s:if>
							<s:else>
								<th> 企业网关宽带业务 </th>
								<th> 企业网关IPTV业务 </th>
								<th> 企业网关VOIP业务 </th>
							</s:else>
							
						</tr>
					</thead>
					<tbody>
						<s:if test="haveResultList.size()>0">
							<s:iterator value="haveResultList">
								<tr>
									<td >
										<s:property value="city_name" />
									</td>
									<td >
										<a href="javascript:onclick=detail('<s:property value="city_id"/>','10');">
											<s:property value="internetValue" />
										</a>
									</td>
									<td >
										<a href="javascript:onclick=detail('<s:property value="city_id"/>','11');">
											<s:property value="iptvValue" />
										</a>
									</td>
									<td >
										<a href="javascript:onclick=detail('<s:property value="city_id"/>','14');">
											<s:property value="voipValue" />
										</a>
									</td>
								</tr>
							</s:iterator>
							<TR>
								<TD colspan="4">
									<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表' 
										style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>')">
							</TD>
							</TR>
						</s:if>
						<s:else>
							<TR ><TD colspan="4">系统中没有检索到相关数据！</TD></TR>
						</s:else>
					</tbody>
				</TABLE>
			</TD>
		</TR>
		<TR STYLE="display: none">
			<TD colspan="4">
				<iframe id="childFrm" src=""></iframe>
			</TD>
		</TR>
	</TABLE>
<%@ include file="../../foot.jsp"%>