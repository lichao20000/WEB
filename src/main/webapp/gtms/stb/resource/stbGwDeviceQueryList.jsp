<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
	$(function() {
		parent.dyniframesize();
	});
	
	function showDevice(deviceId,deviceSerialnumber,oui,cpeMac,vendorId,deviceModelId,devicetypeId,cityId,hardwareversion){
		var citynext =$(window.parent.document.getElementById("citynext")).val();
		var parentcity =$(window.parent.document.getElementById("cityId")).val();
		var strpage = "<s:url value='/gtms/stb/resource/stbGwDeviceQuery!queryDeviceById.action'/>?deviceId=" + deviceId;
// 		var strpage = "<s:url value='/gtms/stb/resource/stbGwDeviceQueryUpdate.jsp'/>?deviceId=" + deviceId + "&deviceSerialnumber="
// 				+ deviceSerialnumber + "&oui=" +oui + "&cpeMac=" +cpeMac+ "&vendorId=" +vendorId+ "&deviceModelId=" +deviceModelId
// 				+ "&devicetypeId=" +devicetypeId+ "&cityId=" +cityId+ "&hardwareversion=" +hardwareversion+ "&citynext=" +citynext+ "&parentcity=" +parentcity;
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
	function detailDevice(deviceId){
		var strpage = "<s:url value='/gtms/stb/resource/stbGwDeviceQuery!queryDeviceInfoById.action'/>?"
						+"deviceId=" + deviceId;
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
	function detailServAccount(serv_account){
		var strpage = "<s:url value='/gtms/stb/resource/userMessage!queryLook.action'/>?"
			+"servaccount=" + serv_account;
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
	function deleteDevice(deviceId,cpeMac,servAccount)
	{
		if(servAccount!="" && servAccount!=null){
			alert("�û����д��ڰ󶨹�ϵ���޷�ֱ��ɾ�������Ƚ��");
			return;
		}
		
		if (!confirm('��ȷ���Ƿ�ɾ����ǰ�豸����')) {
			return;
		}
	
		var url = "<s:url value='/gtms/stb/resource/stbGwDeviceQuery!deleteDevice.action'/>"; 
		$.post(url,{
			deviceId : deviceId,
			deviceMac : cpeMac
		},function(ajax){
			if("2"== ajax){
				alert("���ڰ󶨹�ϵ�����Ƚ��");
			}else if("1"== ajax){
				alert("ɾ���ɹ���");
			}else{
				alert("ɾ��ʧ�ܣ�");
			}
			window.location.reload();
		});

	}

</script>
<div>
	<table class="listtable">
		<caption>ͳ�ƽ��</caption>
		<thead>
			<tr>
				<th width="10%">�豸���к�</th>
				<th width="10%">ҵ���˺�</th>
				<th width="8%">MAC��ַ</th>
				<th width="6%">����</th>
				<th width="8%">�ͺ�</th>
				<th width="9%">Ӳ���汾</th>
				<th width="9%">����汾</th>
				<th width="8%">����</th>
				<th width="8%">�״��ϱ�ʱ��</th>
				<s:if test="instArea=='hn_lt'">
					<th width="8%">����ϱ�ʱ��</th>
				</s:if>
				<th width="9%">����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="deviceList != null ">
				<s:if test="deviceList.size() > 0">
					<s:iterator value="deviceList">
						<tr align="center">
							<td><s:property value="deviceSerialnumber" /></td>
							<td>
								<s:if test="instArea=='hn_lt'">
									<a href="javascript:detailServAccount('<s:property value="servAccount" />')">
										<s:property value="servAccount" />
									</a>
								</s:if>
								<s:else>
									<s:property value="servAccount" />
								</s:else>
							</td>
							<td><s:property value="cpeMac" /></td>
							<td><s:property value="vendorAdd" /></td>
							<td><s:property value="deviceModel" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="cityName" /></td>
							<td><s:property value="completeTime" /></td>
							<s:if test="instArea=='hn_lt'">
								<td><s:property value="cpe_currentupdatetime" /></td>
							</s:if>
							<td>
								<a href="javascript:detailDevice('<s:property value="deviceId"/>')">��ϸ��Ϣ</a>
								<s:if test="showType!=1" >
									<a href="javascript:showDevice('<s:property value="deviceId"/>',
																'<s:property value="deviceSerialnumber"/>',
																'<s:property value="oui"/>',
																'<s:property value="cpeMac"/>',
																'<s:property value="vendorId"/>',
																'<s:property value="deviceModelId"/>',
																'<s:property value="devicetypeId"/>',
																'<s:property value="cityId"/>',
																'<s:property value="hardwareversion"/>')">|�༭</a>
									<s:if test="showType!=2" >
										<a href="javascript:deleteDevice('<s:property value="deviceId"/>',
																	'<s:property value="cpeMac"/>',
																	'<s:property value="servAccount" />')">|ɾ��</a>
									</s:if>
								</s:if>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<s:if test="instArea=='hn_lt'">
							<td colspan=11 align=left>û�в�ѯ��������ݣ�</td>
						</s:if>
						<s:else>
							<td colspan=10 align=left>û�в�ѯ��������ݣ�</td>
						</s:else>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<s:if test="instArea=='hn_lt'">
						<td colspan=11 align=left>û�в�ѯ��������ݣ�</td>
					</s:if>
					<s:else>
						<td colspan=10 align=left>û�в�ѯ��������ݣ�</td>
					</s:else>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<s:if test="instArea=='hn_lt'">
					<td colspan="11" align="right" height="15">
						[ ͳ������ : <s:property value='queryCount' /> ]&nbsp;
						<lk:pages url="/gtms/stb/resource/stbGwDeviceQuery!getStbDeviceList.action" showType=""
							isGoTo="true" changeNum="true" />
					</td>
				</s:if>
				<s:else>
					<td colspan="10" align="right" height="15">
						[ ͳ������ : <s:property value='queryCount' /> ]&nbsp;
						<lk:pages url="/gtms/stb/resource/stbGwDeviceQuery!getStbDeviceList.action" showType=""
							isGoTo="true" changeNum="true" />
					</td>
				</s:else>
			</tr>
		</tfoot>
	</table>

</div>
