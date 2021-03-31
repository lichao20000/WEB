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
						document.write("未操作");
						break;
					case 1:
						document.write("更新服务器地址修改成功");
						break;
					case 2:
						document.write("软件升级成功");
						break;
					case -1:
						document.write("更新服务器地址修改失败");
						break;
					default:
						document.write("");
				}
				
			}
			function dateConvert(dat)
			{
				var d = new Date(dat*1000);
				document.write(d.getFullYear()+"/"+d.getMonth()+"/"+d.getDay()+" "+d.getHours()+":" + d.getMinutes()+":"+d.getSeconds());
			}


			function exportData()
            {
            	var taskId = '<s:property value="taskId" />';
                var cityId = '<s:property value="cityId" />';
                var upResult = '<s:property value="upResult" />';
            	var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!exportUpRecordByTaskIdHnlt.action'/>?taskId=" + taskId + "&upResult=" + upResult+"&cityId="+cityId;
            	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
            }

		</script>
	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=34>
					您当前的位置：升级策略结果查询
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="softUpgrade!exportUpRecordByTaskIdHnlt.action" method="post" enctype="multipart/form-data" name="batchexform">
			<input type="hidden" name="vendorPathId" value=""/>
			<table width="98%" class="listtable" align="center"">
				<thead>
					<tr>
					    <th align="center" width="7%">
                            业务帐号
                        </th>
                        <th align="center" width="7%">
                            属地
                        </th>
                        <th align="center" width="7%">
                            机顶盒MAC
                        </th>
                        <th align="center" width="7%">
                            机顶盒序列号
                        </th>
						<th align="center" width="7%">
							厂商
						</th>
						<th align="center" width="7%">
							型号
						</th>
						<th align="center" width="7%">
							软件版本
						</th>
						<th align="center" width="7%">
							硬件版本
						</th>
						<th align="center" width="7%">
                            认证APK版本
                        </th>
						<th align="center" width="7%">
							EPG版本
						</th>
						<th align="center" width="7%">
                            接入类型
                        </th>

                        <th align="center" width="7%">
                            网络类型
                        </th>

                        <th align="center" width="7%">
                            行串
                        </th>
						<th align="center" width="7%">
							最近上线时间
						</th>
					</tr>
				</thead>
				<s:if test="upRecordList!=null">
					<s:if test="upRecordList.size()>0">
						<tbody>
							<s:iterator value="upRecordList">
								<tr>
									<td align="center">
										<s:property value="serv_account" />
									</td>
									<td align="center">
                                        <s:property value="city_name" />
                                    </td>
									<td align="center">
										<s:property value="cpe_mac" />
									</td>
									<td align="center">
										<s:property value="device_serialnumber" />
									</td>
									<td align="center">
										<s:property value="vendor_add" />
									</td>
									<td align="center">
										<s:property value="device_model" />
									</td>
									<td align="center">
                                        <s:property value="softwareversion" />
                                    </td>
									<td align="center">
										<s:property value="hardwareversion" />
									</td>
									<td align="center">
                                        <s:property value="apk_version_name" />
                                    </td>
                                    <td align="center">
                                        <s:property value="epg_version" />
                                    </td>
                                    <td align="center">
                                        <s:property value="network_type" />
                                    </td>
                                    <td align="center">
                                        <s:property value="addressing_type" />
                                    </td>
                                    <td align="center">
                                        <s:if test='category==0'>
                                        行
                                        </s:if>
                                        <s:elseif test='category==1'>
                                        串
                                        </s:elseif>
                                        <s:else>
                                        未知
                                        </s:else>
                                    </td>
									<td align="center">
										<s:property value="cpe_currentupdatetime" />
									</td>	
								</tr>
							</s:iterator>
						</tbody>
						<tfoot>
							<tr>
							    <td colspan="4">
							    <div align="left">
							        <button onclick="exportData()">
                                        导出
                                    </button>
                                </div>
                                </td>
								<td colspan="10"><div align="right"><lk:pages url='/gtms/stb/resource/stbSoftUpgrade!queryUpRecordByTaskIdHnlt.action' changeNum="true"/></div></td>
							</tr>
						</tfoot>
					</s:if>
				</s:if>
				<s:else>
                        <tbody>
                            <tr bgcolor="#FFFFFF">
                                <td colspan="14">
                                   <font color="red">没有相关数据</font>
                                </td>
                            </tr>
                        </tbody>
                    </s:else>
			</table>
		</s:form>
		<br>
		<br>
	</body>