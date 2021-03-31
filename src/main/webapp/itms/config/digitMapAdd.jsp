<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%
	String action = request.getParameter("action");
	String map_id = request.getParameter("map_id");
	String vendor_id = request.getParameter("vendor_id");
	String device_model_id = request.getParameter("device_model_id");
	String devicetype_id = request.getParameter("devicetype_id");
%>
<html>
	<head>
	    <title>������ͼ����ģ��</title>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
	</head>
	
	<script type="text/javascript">
		var add_upd = "";
		function Init(action){
			add_upd = action;
			// ��ʼ���豸����
			if(action=="add"){// ���
				gwShare_change_select("vendor","-1","");
			}else{
				gwShare_change_select("vendor","<%=vendor_id%>","");
				gwShare_change_select("deviceModel","<%=vendor_id%>","<%=device_model_id%>");
				gwShare_change_select("devicetype","<%=device_model_id%>","<%=devicetype_id%>");
			}
		}

		/*------------------------------------------------------------------------------
		//������:		deviceSelect_change_select
		//����  :	type 
			            vendor      �����豸����
			            deviceModel �����豸�ͺ�
			            devicetype  �����豸�汾
		//����  :	����ҳ������̡��ͺż�����
		//����ֵ:		
		//˵��  :	
		//����  :	Create 2009-12-25 of By qxq
		------------------------------------------------------------------------------*/
		function gwShare_change_select(type,selectvalue,selectvalue_upd){
			switch (type){
				case "city":
					var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
					$.post(url,{
					},function(ajax){
						gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
					});
					break;
				case "vendor":
					var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
					$.post(url,{
					},function(ajax){
						gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
						$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
						$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
					});
					break;
				case "deviceModel":
					var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
					var vendorId = $("select[@name='gwShare_vendorId']").val();
					if(add_upd == "update" && selectvalue_upd != ''){  // ����
						vendorId = selectvalue;
					}
					if("-1"==vendorId){
						$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
						$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
						break;
					}
					$.post(url,{
						gwShare_vendorId:vendorId
					},function(ajax){
						if(add_upd == "update"){  // ����
							gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue_upd);
						}else{ // ����
							gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
							$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
						}
						if(selectvalue_upd == ''){
							$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
						}
					});
					break;
				case "devicetype":
					var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
					var vendorId = $("select[@name='gwShare_vendorId']").val();
					var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
					if(add_upd == "update" && selectvalue_upd != ''){  // ����
						deviceModelId = selectvalue;
					}
					if("-1"==deviceModelId){
						$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
						break;
					}
					$.post(url,{
						gwShare_vendorId:vendorId,
						gwShare_deviceModelId:deviceModelId
					},function(ajax){
						if(add_upd == "update"){  // ����
							gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue_upd);
						}else{  // ����
							gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
						}
					});
					break;	
				default:
					alert("δ֪��ѯѡ�");
					break;
			}	
		}
		
		/*------------------------------------------------------------------------------
		//������:		deviceSelect_parseMessage
		//����  :	ajax 
		            	������XXX$XXX#XXX$XXX
		            field
		            	��Ҫ���ص�jquery����
		//����  :	����ajax���ز���
		//����ֵ:		
		//˵��  :	
		//����  :	Create 2009-12-25 of By qxq
		------------------------------------------------------------------------------*/
		//������ѯ�豸�ͺŷ���ֵ�ķ���
		function gwShare_parseMessage(ajax,field,selectvalue){
			var flag = true;
			if(""==ajax){
				return;
			}
			var lineData = ajax.split("#");
			if(!typeof(lineData) || !typeof(lineData.length)){
				return false;
			}
			field.html("");
			option = "<option value='-1' selected>==��ѡ��==</option>";
			field.append(option);
			for(var i=0;i<lineData.length;i++){
				var oneElement = lineData[i].split("$");
				var xValue = oneElement[0];
				var xText = oneElement[1];
				if(selectvalue==xValue){
					flag = false;
					//����ÿ��value��text��ǵ�ֵ����һ��option����
					option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
				}else{
					//����ÿ��value��text��ǵ�ֵ����һ��option����
					option = "<option value='"+xValue+"'>=="+xText+"==</option>";
				}
				try{
					field.append(option);
				}catch(e){
					alert("�豸�ͺż���ʧ�ܣ�");
				}
			}
			if(flag){
				field.attr("value","-1");
			}
		}
	</script>

	<body onLoad="init()">
		<TABLE id="addTable">

			<tr>
				<td HEIGHT=20>&nbsp;
					
				</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								��ͼ����ģ�����
							</td>
							<td>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
								<font color="#FF0000">ע��ģ�����ơ��豸���ء��豸���̡��豸�ͺ�Ϊ������</font>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
			<TR>
				<TD>
					<TABLE class="querytable" width="100%">
						<TR>
							<th colspan="4" id="page_title">
							</th>
						</TR>
						<TR>
							<TD class=column width="15%">
								ģ�����ƣ�
							</TD>
							<TD width="35%">
								<!--  xxx ����ʹ��struts��ǩ -->
								<input type="text" name="map_name" id="map_name" value='<s:property value="digitMap.map_name" />' maxlength="100" />
							</TD>
							<TD class=column width="15%">
								�豸���أ�
							</TD>
							<TD width="35%">
								<s:select list="cityList" name="cityId" headerKey="-1"
									headerValue="��ѡ������" listKey="city_id" listValue="city_name"
									value="digitMap.city_id" cssClass="bk"></s:select>
							</TD>
						</TR>
						<TR>
							<TD class=column>
								�豸���̣�
							</TD>
							<TD>
								<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1','')">
									<option value="-1">==��ѡ��==</option>
								</select>&nbsp;
							</TD>
							<TD class=column>
								�豸�ͺţ�
							</TD>
							<TD>
								<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1','')">
									<option value="-1">����ѡ����</option>
								</select>&nbsp;
							</TD>
						</TR>
						<TR>
							<TD class=column>
								����汾��
							</TD>
							<TD >
								<select name="gwShare_devicetypeId" class="bk"">
									<option value="-1">����ѡ���豸�ͺ�</option>
								</select>&nbsp;
							</TD>
							<TD class=column>
								�Ƿ�Ĭ�ϣ�
							</TD>
							<TD >
								<select name="is_default" class="bk"" value ="1">
									<option value="0">��</option>
									<option value="1">��</option>
								</select>&nbsp;
							</TD>
						</TR>
						<TR>
							<TD class=column>
								ģ���������
							</TD>
							<TD colspan="3">
								<textarea name="map_content" id="map_content" rows="10" cols="100"><s:property value='digitMap.map_content' /></textarea>
								<font color="#FF0000">* ��಻�ܳ���1024���ַ����Ҳ��������������ַ���</font>
							</TD>
						</TR>
						<tr>
							<td colspan=4 class=foot align="right">
								<button onClick="save()" id="save_button"></button>
							</td>
						</tr>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</body>

	<script>
		
		var action = "<%=action%>";
		var map_id = "<%=map_id%>";
		
		Init(action);
		
		function init(){
			if(action == "add"){
				page_title.innerHTML = "����ģ��";
				save_button.innerHTML = "��������";
			}else{
				page_title.innerHTML = "�༭ģ��";
				save_button.innerHTML = "�༭����";
			}
		}
		
		function save(){
			var map_name = $.trim($("input[@id='map_name']").val());
			var map_content = document.getElementById("map_content");
			
			if(map_name ==""){		
				alert("ģ�����Ʋ���Ϊ�գ�");
				$("input[@id='map_name']").focus();
				$("input[@id='map_name']").select();
				return;
			}
			if($.trim(map_content.value)==""){
				alert("ģ�����ݲ���Ϊ�գ�");
				map_content.focus();
				map_content.select();
				return;
			}
			var cityId = $("select[@name='cityId']").val();
			if(cityId==""||cityId=="-1"){
				alert("��ѡ������!");
				return false;
			}
			
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if(vendorId == "" || vendorId == "-1"){
				alert("��ѡ���豸���̣�");
				return false;
			}
			
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if(deviceModelId == "" || deviceModelId == "-1"){
				alert("��ѡ���豸�ͺţ�");
				return false;
			}
			
			var deviceTypeId = $("select[@name='gwShare_devicetypeId']").val();
			//if(deviceTypeId == "" || deviceTypeId == "-1"){
			//	alert("��ѡ������汾��");
			//	return false;
			//}
			var is_default = $("select[@name='is_default']").val();

			// �����ύ
			if(action=="add"){// ���
				var url = "<s:url value='/itms/config/digitMapConfig!add.action'/>";
				$.post(url,{
					map_name:encodeURIComponent(map_name) , 
					map_content:$.trim(map_content.value),
					cityId:cityId,
					vendorId:vendorId,
					deviceModelId:deviceModelId,
					deviceTypeId:deviceTypeId,
					is_default:is_default
				},function(ajax){
					var result = parseInt(ajax);
					if (result == -2)
					{
						alert("�Ѵ���Ĭ��ģ�壡");
						return;
					}
					if(result == -1){
						alert("���ʧ�ܣ�");
						return;
					}
					if(result > 0){
						alert("��ӳɹ���");
						
						var form = window.opener.parent.document.getElementById("mainForm");;
						form.action = "<s:url value='/itms/config/digitMapConfig!queryList.action'/>";
						form.submit();
						
						window.close();
					}
				});
			}else{ // �޸�
				var url = "<s:url value='/itms/config/digitMapConfig!update.action'/>";
				$.post(url,{
					map_name:encodeURIComponent(map_name), 
					map_content:$.trim(map_content.value), 
					map_id:map_id,
					cityId:cityId,
					vendorId:vendorId,
					deviceModelId:deviceModelId,
					deviceTypeId:deviceTypeId,
					is_default:is_default
				},function(ajax){
					var result = parseInt(ajax);
					if (result == -2)
					{
						alert("�Ѵ���Ĭ��ģ�壡");
						return;
					}
					if(result == -1){
						alert("�༭ʧ�ܣ�");
						return;
					}
					if(result > 0){
						alert("�༭�ɹ���");
						
						var form = window.opener.parent.document.getElementById("mainForm");;
						form.action = "<s:url value='/itms/config/digitMapConfig!queryList.action'/>";
						form.submit();
						
						window.close();
					}
				});
			}
			
		}
		
		
		/** ���߷��� **/
		/*LTrim(string):ȥ����ߵĿո�*/
		function LTrim(str){
		    var whitespace = new String("�� \t\n\r");
		    var s = new String(str);   
		
		    if (whitespace.indexOf(s.charAt(0)) != -1){
		        var j=0, i = s.length;
		        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
		            j++;
		        }
		        s = s.substring(j, i);
		    }
		    return s;
		}
		/*RTrim(string):ȥ���ұߵĿո�*/
		function RTrim(str){
		    var whitespace = new String("�� \t\n\r");
		    var s = new String(str);
		 
		    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
		        var i = s.length - 1;
		        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
		            i--;
		        }
		        s = s.substring(0, i+1);
		    }
		    return s;
		}
		/*Trim(string):ȥ���ַ������ߵĿո�*/
		function trim(str){
		    return RTrim(LTrim(str)).toString();
		}
</script>
</html>