<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 获取设备端口信息;
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-28 AM11:41:38
 *
 * @版权 南京联创网络科技 网管产品部;
 *
 */
 --%>
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">
   td.no{
		color:red;
	}
</style>
<script type="text/javascript">
	$(function(){
		//全选
		$("#sel_1").click(function(){
			var chk=$(this).attr("checked");
			chk=chk?chk:false;
			$("input[@name='chk']").attr("checked",chk);
		});
		//页面完成后返回数据到上页面
		//parent.dualPort();
		parent.document.all("div_port").innerHTML=child.innerHTML;
	});
</script>
</head>
<body>
	<div id="child" style="width:100%;">
	<table width="100%" class="listtable">
		<thead>
		<tr>
			<th width="10%">
				<input type="checkbox" id="sel_1"><label for="sel_1">全选</label>
			</th>
			<th width="45%">采集方式</th>
			<th width="45%">采集参数</th>
		</tr>
		</thead>
		<tbody>
			<s:iterator var="pl" value="portList">
				<tr>
					<td>
						<input type="checkbox" name="chk" value="<s:property value="#pl.getIfindex()+'|||'+#pl.getIfdescr()+'|||'+#pl.getIfname()+'|||'+#pl.getIfnamedefined()+'|||'+#pl.getIfportip()+'|||'+#pl.getGetway()+'|||'+#pl.getPort_info()"/>">
					</td>
					<td>
						端口IP:<s:property value="#pl.getIfportip()"/><br>
						端口描述:<s:property value="#pl.getIfdescr()"/><br>
						端口名字:<s:property value="#pl.getIfname()"/><br>
						端口别名:<s:property value="#pl.getIfnamedefined()"/><br>
						端口索引:<s:property value="#pl.getIfindex()"/><br>
					</td>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
						<s:iterator var="pil" value="portInfoList" status="rowstatus">
							<td>
								<input type="checkbox"
									   checked
									   id="i_<s:property value="#pl.getIfindex()+'_'+#pil.getOrder()"/>"
								       value="<s:property value="#pil.getOrder()"/>">
								<label for="i_<s:property value="#pl.getIfindex()+'_'+#pil.getOrder()"/>"><s:property value="#pil.getDesc()"/></label>
							</td>
							<s:if test="(#rowstatus.index+1)%4==0">
								</tr><tr>
							</s:if>
						</s:iterator>
						</tr>
						</table>
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	</div>
</body>
</html>
