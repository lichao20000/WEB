<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	function deleteStrategy(id,device_id)
	{
		if(!confirm("���Ҫɾ������������")){
			return false;
		}
		
		var url = "<s:url value='/gtms/stb/resource/stbSetParamACT!deleteStrategy.action'/>";
	    $.post(url,{
	    	strategy_id:id,
	    	device_id:device_id
		},function(ajax){
			var s = ajax.split(",");
			alert(s[1]);
			
	    	if(s[0]=="1"){
	    		var strpage = "<s:url value='/gtms/stb/resource/stbSetParamACT!getStrategyResult.action'/>"
	    						+"?device_id="+device_id;
	    		window.location.href=strpage;
	    	}
	    });
	}
</script>

<div>
<table width="100%" class="querytable" align="center">
	<s:if test="list!=null && list.size()>0">
		<tr>
			<th colspan="4" class="title_1">�豸������Ϣ</th>
		</tr>
		<tr>
			<td nowrap class="title_2" width="15%">�豸���к�</td>
			<td width="35%">
				<s:property value="list.get(0).device_serialnumber" />
			</td>
			<td class="title_2" width="15%">����</td>
			<td width="35%">
				<s:property value="list.get(0).city_name" />
			</td>
		</tr>
		<tr>
			<td class="title_2" width="15%">�豸����</td>
			<td width="35%">
				<s:property value="list.get(0).vendor_name" />
			</td>
			<td class="title_2" width="15%">�ͺ�</td>
			<td width="35%">
				<s:property value="list.get(0).device_model" />
			</td>
		</tr>
		<tr>
			<td class="title_2" width="15%">MAC��ַ</td>
			<td width="35%">
				<s:property value="list.get(0).cpe_mac" /></td>
			<td class="title_2" width="15%">ҵ���˺�</td>
			<td width="35%">
				<s:property value="list.get(0).serv_account" />
			</td>
		</tr>
		<tr>
			<td class="title_2" width="15%">Ӳ���汾</td>
			<td width="35%">
				<s:property value="list.get(0).hardwareversion" />
			</td>
			<td class="title_2" width="15%">����汾</td>
			<td width="35%">
				<s:property value="list.get(0).softwareversion" />
			</td>
		</tr>
		<tr>
			<td class="title_2" width="15%">APK�汾����</td>
			<td width="35%">
				<s:property value="list.get(0).apk_version_name" />
			</td>
			<td class="title_2" width="15%">EPG�汾</td>
			<td width="35%">
				<s:property value="list.get(0).epg_version" />
			</td>
		</tr>
		<tr>
			<td class="title_2" width="15%">�״ε�¼ʱ��</td>
			<td width="35%">
				<s:property value="list.get(0).complete_time" />
			</td>
			<td class="title_2" width="15%">�����¼ʱ��</td>
			<td width="35%">
				<s:property value="list.get(0).cpe_currentupdatetime" />
			</td>
		</tr>
		<tr>
			<td class="title_2" width="15%">��������</td>
			<td width="35%">
				<s:property value="list.get(0).network_type" />
			</td>
			<td class="title_2" width="15%">��������</td>
			<td width="35%">
				<s:property value="list.get(0).addressing_type" />
			</td>
		</tr>
		<tr>
			<td class="title_2" width="15%">�豸IP</td>
			<td width="35%">
				<s:property value="list.get(0).loopback_ip" />
			</td>
			<td class="title_2" width="15%">����IP</td>
			<td width="35%">
				<s:property value="list.get(0).public_ip" />
			</td>
		</tr>
	</s:if>
</table>
</br>

<table width="100%" class="listtable" align="center">
	<thead>
		<tr>
			<th colspan="7" class="title_1">�豸���Բ�ѯͳ��</th>
		</tr>
		<tr bgcolor="#FFFFFF">
			<th align="center" width="15%">��������</th>
			<th align="center" width="15%">���Զ���ʱ��</th>
			<th align="center" width="15%">����ʱ��</th>
			<th align="center" width="15%">���ʱ��</th>
			<th align="center" width="15%">������Ϣ</th>
			<th align="center" width="10%">����״̬</th>
			<th align="center" width="5%">����</th>
		</tr>
	</thead>
	<tbody>
	<s:if test="list!=null && list.size()>0">
		<s:iterator value="list">
			<tr>
				<td align="center"><s:property value="strategy_type" /></td>
				<td align="center"><s:property value="time" /></td>
				<td align="center"><s:property value="start_time" /></td>
				<td align="center"><s:property value="end_time" /></td>
				<td align="center" title="<s:property value='strategy_info' />"><s:property value="strategy_type" /></td>
				<td align="center"><s:property value="strategy_status" /></td>
				<td align="center">
					<label onclick="javascript:deleteStrategy(<s:property value='strategy_id'/>,<s:property value='device_id'/>);">
						<img src="<s:url value="/images/del.gif"/>" border='0' alt='ɾ��' style='cursor: hand'> 
					</label>
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td colspan="7"><font color="red">���豸û��������¼</font></td>
		</tr>
	</s:else>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="7" align="right">
				<lk:pages url="/gtms/stb/resource/stbSetParamACT!getStrategyResult.action" styleClass="" showType=""
					isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
</div>
