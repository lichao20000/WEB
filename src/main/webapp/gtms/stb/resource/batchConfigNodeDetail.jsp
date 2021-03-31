<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript">
			function resultConvert(res)
			{
				switch(res)
				{
					case 0:
						document.write("否");
						break;
					case 1:
						document.write("是");
						break;
					default:
						document.write("");
				}
				
			}
			
			function resultConvert1(res)
			{
				switch(res)
				{
					case 0:
						document.write("失效");
						break;
					case 1:
						document.write("生效");
						break;
					case 2:
						document.write("失效");
						break;
					default:
						document.write("");
				}
				
			}
			
			function downloadHotel(taskId){
				var url =  "<s:url value='/gtms/stb/resource/softUpgrade!exportHotelUser.action'/>?taskId="+taskId;
				document.all("childFrm").src=url;
			}
			function downloadVIP(taskId){
				var url =  "<s:url value='/gtms/stb/resource/softUpgrade!exportVIPUser.action'/>?taskId="+taskId;
				document.all("childFrm").src=url;
			}
		</script>
	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					您当前的位置：批量配置参数节点
				</TD>
			</TR>
		</TABLE>
		<br>
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">
					批量配置参数详细信息
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					任务名称
				</td>
				<TD width="85%" colspan="3">
					<s:property value="taskDetailMap.task_name" />
				</td>
			</tr>	
			<tr>
				<td colspan="4" class="title_1">
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					属地
				</td>
				<TD width="85%" colspan="3">
					<s:property value="taskDetailMap.cityName" />
				</td>
			</tr>	
			<tr>
				<TD class="title_2" align="center" width="15%">
					厂商
				</td>
				<TD width="85%" colspan="3">
					<s:property value="taskDetailMap.vendorName" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					已选择型号
				</td>
				<TD width="85%" colspan="3">
					<s:property value="taskDetailMap.deviceModelName" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					已选择版本
				</td>
				<TD width="85%" colspan="3">
					<s:property value="taskDetailMap.deviceTypeName" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					任务状态
				</td>
				<TD width="85%" colspan="3">
					<script type="text/javascript">resultConvert1(<s:property value='taskDetailMap.status'/>)</script>	
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					定制人
				</td>
				<TD width="85%" colspan="3">
					<s:property value="taskDetailMap.acc_loginname" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					定制时间
				</td>
				<TD width="85%" colspan="3">
					<s:property value="taskDetailMap.add_time" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					修改时间
				</td>
				<TD width="85%" colspan="3">
					<s:property value="taskDetailMap.update_time" />
				</td>
			</tr>
			<tr>
				<TD class="title_2" align="center" width="15%">
					启用ip地址段
				</td>
				<TD width="85%" colspan="3">
					<script type="text/javascript">resultConvert(<s:property value='taskDetailMap.check_ip'/>)</script>	
				</td>
			</tr>
			<s:if test="ipList!=null">
                <s:if test="ipList.size()>0">
                        <tbody>
                                <s:iterator value="ipList">
                                        <tr>
                                               <TD class="title_2" align="center" width="15%">
													起始Ip：
												</td>	
												<td width="35%"><s:property value="start_ip" /></td>
												<TD class="title_2" align="center" width="15%">
													结束Ip：
												</td>	
												<td width="35%"><s:property value="end_ip" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
            </s:if>
            <tr>
				<TD class="title_2" align="center" width="15%">
					启用MAC地址段
				</td>
				<TD width="85%" colspan="3">
					<script type="text/javascript">resultConvert(<s:property value='taskDetailMap.check_mac'/>)</script>	
				</td>
			</tr>
            <s:if test="macList!=null">
                <s:if test="macList.size()>0">
                        <tbody>
                                <s:iterator value="macList">
                                        <tr>
                                               <TD class="title_2" align="center" width="15%">
													起始mac：
												</td>	
												<td width="35%"><s:property value="start_mac" /></td>
												<TD class="title_2" align="center" width="15%">
													结束mac：
												</td>	
												<td width="35%"><s:property value="end_mac" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
            </s:if>
			<s:if test="paramList!=null">
                <s:if test="paramList.size()>0">
                        <tbody>
                                <s:iterator value="paramList">
                                        <tr>
                                               <TD class="title_2" align="center" width="15%">
													参数节点路径：
												</td>	
												<td width="35%"><s:property value="para_path" /></td>
												<TD class="title_2" align="center" width="15%">
													参数值：
												</td>	
												<td width="35%"><s:property value="para_value" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
            </s:if>
			<tr STYLE="display: none">
					<td colspan="4">
						<iframe id="childFrm" src=""></iframe>
					</td>
		   </tr>	
		</table>
		<br>
		<br>
	</body>