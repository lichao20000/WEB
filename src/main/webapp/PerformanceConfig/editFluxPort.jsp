<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%--
/**
 * �༭�˿�;
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-29 AM11:27:21
 * 
 * @��Ȩ �Ͼ���������Ƽ� ���ܲ�Ʒ��;
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�༭�˿�</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">	
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<script type="text/javascript">
	$(function(){
		var ajax="<s:property value="ajax"/>";
		if(null != ajax && ""!=ajax){
			if(ajax=="0"){
				alert("�༭�ɹ���");
			}else if(ajax=="-1"){
				alert("�༭ʧ�ܣ�");
			}else if(ajax=="-2"){
				alert("���ݱ༭�ɹ���֪ͨ��̨ʧ�ܣ�");
			}else{
				alert("��ʱ�������ԣ�");
			}
			window.close();
		}
		//��ʾ�������ø澯��	
		$("td[@name='td_show']").toggle(function(){
			$(this).html("�������� <img src='<s:url value="/images/ico_9_up.gif"/>' width='10' hight='12' align='center' border='0' alt='����������ø澯' >");
			$("#tr_show").show();
			getWarn();
		},function(){
			$(this).html("���ø澯 <img src='<s:url value="/images/ico_9.gif"/>' width='10' hight='12' align='center' border='0' alt='������ø澯' >");
			$("#tr_show").hide();
		});
		
	});
	//��ȡ�澯ҳ��
	function getWarn(){
		if($("#td_warn").html()==""){
			$("#td_warn").html("���ڻ�ȡ�澯�������ȴ�......");
			$.post(
				"<s:url value="/performance/configFlux!getWarnJSP.action"/>",
				function(data){
					$("#td_warn").html(data);
					setWarn();
				}
			);
		}
	}
	//setwarn
	function setWarn(){
		//*****************�̶���ֵһ***********************************************************************
		var ifinoct_maxtype     ="<s:property value="ifinoct_maxtype"/>";		//�˿�������������ֵһ�Ƚϲ�����
		var ifinoctetsbps_max   ="<s:property value="ifinoctetsbps_max"/>";		//�˿�������������ֵһ(%)
		var ifoutoct_maxtype	="<s:property value="ifoutoct_maxtype"/>";		//�˿�������������ֵһ�Ƚϲ�����
		var ifoutoctetsbps_max 	="<s:property value="ifoutoctetsbps_max"/>";	//�˿�������������ֵһ(%)
		var ifindiscardspps_max ="<s:property value="ifindiscardspps_max"/>";	//�˿����붪������ֵ(%)
		var ifoutdiscardspps_max="<s:property value="ifoutdiscardspps_max"/>";	//�˿�������������ֵ(%)
		var ifinerrorspps_max 	="<s:property value="ifinerrorspps_max"/>";		//�˿�����������ֵ(%)
		var ifouterrorspps_max 	="<s:property value="ifouterrorspps_max"/>";	//�˿������������ֵ(%)
		var warningnum 			="<s:property value="warningnum"/>";			//������ֵ�Ĵ��������澯��
		var warninglevel 		="<s:property value="warninglevel"/>";			//������ֵ�澯ʱ�ĸ澯����
		var reinstatelevel 		="<s:property value="reinstatelevel"/>";		//�ָ��澯����
		//*****************�̶���ֵ��***********************************************************************
		var ifinoct_mintype 	="<s:property value="ifinoct_mintype"/>";		//�˿�������������ֵ���Ƚϲ�����
		var ifinoctetsbps_min 	="<s:property value="ifinoctetsbps_min"/>";		//�˿�������������ֵ��(%)
		var ifoutoct_mintype 	="<s:property value="ifoutoct_mintype"/>";		//�˿�������������ֵ���Ƚϲ�����
		var ifoutoctetsbps_min 	="<s:property value="ifoutoctetsbps_min"/>";	//�˿�������������ֵ��(%)
		var warningnum_min 		="<s:property value="warningnum_min"/>";		//������ֵ���Ĵ��������澯��
		var warninglevel_min 	="<s:property value="warninglevel_min"/>";		//������ֵ���澯ʱ�ĸ澯����
		var reinlevel_min 		="<s:property value="reinlevel_min"/>";			//��ֵ���ָ��澯����
		//*****************��̬��ֵһ***********************************************************************
		var overmax 			="<s:property value="overmax"/>";				//��̬��ֵһ������
		var overper 			="<s:property value="overper"/>";				//��̬��ֵһ(%)
		var overnum 			="<s:property value="overnum"/>";				//������̬��ֵһ�Ĵ���(���澯)
		var overlevel 			="<s:property value="overlevel"/>";				//������̬��ֵһ�澯ʱ�ĸ澯����
		var reinoverlevel		="<s:property value="reinoverlevel"/>";			//�����ָ��澯ʱ�ļ���
		//*****************��̬��ֵ��************************************************************************
		var overmin 			="<s:property value="overmin"/>";				//��̬��ֵ��������
		var overper_min 		="<s:property value="overper_min"/>";			//��̬��ֵ��(%)
		var overnum_min 		="<s:property value="overnum_min"/>";			//������̬��ֵ������(���澯)
		var overlevel_min 		="<s:property value="overlevel_min"/>";			//������̬��ֵ���澯ʱ�ĸ澯����
		var reinoverlevel_min 	="<s:property value="reinoverlevel_min"/>";		//�����ָ��澯ʱ�ļ���
		var com_day 			="<s:property value="com_day"/>";				//���ɶ�̬��ֵһ������(��)
		//*****************ͻ����ֵ**************************************************************************
		var intbflag 			="<s:property value="intbflag"/>";				//�ж��Ƿ���������ͻ��澯����
		var ifinoctets 			="<s:property value="ifinoctets"/>";			//�������ʱ仯����ֵ(%)
		var inoperation 		="<s:property value="inoperation"/>";			//��������ͻ��澯������
		var inwarninglevel 		="<s:property value="inwarninglevel"/>";		//��������ͻ��澯����
		var inreinstatelevel 	="<s:property value="inreinstatelevel"/>";		//�������ʻָ�ͻ��澯����
		var outtbflag 			="<s:property value="outtbflag"/>";				//�Ƿ���������ͻ��澯����
		var ifoutoctets 		="<s:property value="ifoutoctets"/>";			//�������ʱ仯����ֵ(%)
		var outoperation 		="<s:property value="outoperation"/>";			//��������ͻ��澯������
		var outwarninglevel 	="<s:property value="outwarninglevel"/>";		//��������ͻ��澯����
		var outreinstatelevel 	="<s:property value="outreinstatelevel"/>";		//�������ʻָ�ͻ��澯����
		//*************************************************************************************************
		//*********************************������Ŀ��***********************************************
		if($("select[@name='ifinoct_maxtype']").length>0){
			//�˿�������������ֵһ�Ƚϲ�����
			checkSelectDataOne(ifinoct_maxtype,ifinoctetsbps_max,"ifinoct_maxtype",$("input[@name='ifinoctetsbps_max']"));
			//�˿�������������ֵһ�Ƚϲ�����
			checkSelectDataOne(ifoutoct_maxtype,ifoutoctetsbps_max,"ifoutoct_maxtype",$("input[@name='ifoutoctetsbps_max']"));
			checkTextData(ifindiscardspps_max,$("#c_disinmax"),$("input[@name='ifindiscardspps_max']"));//�˿����붪������ֵ(%)
			checkTextData(ifoutdiscardspps_max,$("#c_disoutmax"),$("input[@name='ifoutdiscardspps_max']"));//�˿�������������ֵ(%)
			checkTextData(ifinerrorspps_max,$("#c_errinmax"),$("input[@name='ifinerrorspps_max']"));//�˿�����������ֵ(%)
			checkTextData(ifouterrorspps_max,$("#c_erroutmax"),$("input[@name='ifouterrorspps_max']"));//�˿������������ֵ(%)
			$("input[@name='warningnum']").val(warningnum);//������ֵ�Ĵ��������澯��
			//������ֵ�澯ʱ�ĸ澯����
			$("select[@name='warninglevel'] option").each(function(){
				if($(this).val()==warninglevel){
					$(this).attr("selected",true);
					return;
				}
			});
			//�ָ��澯����
			$("select[@name='reinstatelevel'] option").each(function(){
				if($(this).val()==reinstatelevel){
					$(this).attr("selected",true);
					return;
				}
			});
			//�˿�������������ֵ���Ƚϲ�����
			checkSelectDataOne(ifinoct_mintype,ifinoctetsbps_min,"ifinoct_mintype",$("input[@name='ifinoctetsbps_min']"));
			//�˿�������������ֵ���Ƚϲ�����
			checkSelectDataOne(ifoutoct_mintype,ifoutoctetsbps_min,"ifoutoct_mintype",$("input[@name='ifoutoctetsbps_min']"));
			$("input[@name='warningnum_min']").val(warningnum_min);//������ֵ���Ĵ��������澯��
			//������ֵ���澯ʱ�ĸ澯����
			$("select[@name='warninglevel_min'] option").each(function(){
				if($(this).val()==warninglevel_min){
					$(this).attr("selected",true);
					return;
				}
			});
			//��ֵ���ָ��澯����
			$("select[@name='reinlevel_min'] option").each(function(){
				if($(this).val()==reinlevel_min){
					$(this).attr("selected",true);
					return;
				}
			});
			//��̬��ֵ������
			checkSelectDataTwo(overmax,overper,overnum,overlevel,reinoverlevel,"overmax",$("input[@name='overper']"),$("input[@name='overnum']"),"overlevel","reinoverlevel");
			//��̬��ֵ������
			checkSelectDataTwo(overmin,overper_min,overnum_min,overlevel_min,reinoverlevel_min,"overmin",$("input[@name='overper_min']"),$("input[@name='overnum_min']"),"overlevel_min","reinoverlevel_min");
			//���ɶ�̬��ֵһ������(��)
			$("input[@name='com_day']").val(com_day);
			//������������ͻ��澯����  
			if(intbflag=="1"){
				$("#c_intbflag").attr("checked",true);
				//�������ʱ仯����ֵ(%)
				$("input[@name='ifinoctets']").val(ifinoctets);
				$("input[@name='ifinoctets']").attr("readonly",false);
				$("input[@name='ifinoctets']").attr("class","");
				//��������ͻ��澯������
				$("select[@name='inoperation']").attr("disabled",false);
				$("select[@name='inoperation'] option").each(function(){
					if($(this).val()==inoperation){
						$(this).attr("selected",true);
						return;
					}
				});
				//��������ͻ��澯����
				$("select[@name='inwarninglevel']").attr("disabled",false);
				$("select[@name='inwarninglevel'] option").each(function(){
					if($(this).val()==inwarninglevel){
						$(this).attr("selected",true);
						return;
					}
				});
				//�������ʻָ�ͻ��澯����
				$("select[@name='inreinstatelevel']").attr("disabled",false);
				$("select[@name='inreinstatelevel'] option").each(function(){
					if($(this).val()==inreinstatelevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
			//������������ͻ��澯����
			if(outtbflag=="1"){
				$("#c_outtbflag").attr("checked",true);
				//�������ʱ仯����ֵ(%)
				$("input[@name='ifoutoctets']").val(ifinoctets);
				$("input[@name='ifoutoctets']").attr("readonly",false);
				$("input[@name='ifoutoctets']").attr("class","");
				//��������ͻ��澯������
				$("select[@name='outoperation']").attr("disabled",false);
				$("select[@name='outoperation'] option").each(function(){
					if($(this).val()==outoperation){
						$(this).attr("selected",true);
						return;
					}
				});
				//��������ͻ��澯����
				$("select[@name='outwarninglevel']").attr("disabled",false);
				$("select[@name='outwarninglevel'] option").each(function(){
					if($(this).val()==outwarninglevel){
						$(this).attr("selected",true);
						return;
					}
				});
				//�������ʻָ�ͻ��澯����
				$("select[@name='outreinstatelevel']").attr("disabled",false);
				$("select[@name='outreinstatelevel'] option").each(function(){
					if($(this).val()==outreinstatelevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
		}
		//*********************************������Ŀ��***********************************************
		if($("#c_inmax").length>0){
			checkTextData(ifinoctetsbps_max,$("#c_inmax"),$("input[@name='ifinoctetsbps_max']"));//�˿����������������ֵ(%)
			checkTextData(ifoutoctetsbps_max,$("#c_outmax"),$("input[@name='ifoutoctetsbps_max']"));//�˿�����������������ֵ(%)
			checkTextData(ifindiscardspps_max,$("#c_disinmax"),$("input[@name='ifindiscardspps_max']"));//�˿����붪������ֵ(%) 
			checkTextData(ifoutdiscardspps_max,$("#c_disoutmax"),$("input[@name='ifoutdiscardspps_max']"));//�˿�������������ֵ(%)  
			checkTextData(ifinerrorspps_max,$("#c_errinmax"),$("input[@name='ifinerrorspps_max']"));//�˿�����������ֵ(%) 
			checkTextData(ifouterrorspps_max,$("#c_erroutmax"),$("input[@name='ifouterrorspps_max']"));//�˿������������ֵ(%) 
			$("input[@name='warningnum']").val(warningnum);//������ֵ�Ĵ��������澯��
			//������ֵ�澯ʱ�ĸ澯����
			$("select[@name='warninglevel'] option").each(function(){
				if($(this).val()==warninglevel){
					$(this).attr("selected",true);
					return;
				}
			});
			//�ָ��澯����
			$("select[@name='reinstatelevel'] option").each(function(){
				if($(this).val()==reinstatelevel){
					$(this).attr("selected",true);
					return;
				}
			});
			if($.trim(overper)!="" && eval(overper)>0){
				$("#c_usedynamic").attr("checked",true);//���ö�̬��ֵ�澯
				$("input[@name='overper']").val(overper);//������̬��ֵ�İٷֱ�(%)
				$("input[@name='overper']").attr("class","");
				$("input[@name='overper']").attr("readonly",false);
				$("input[@name='overnum']").val(overnum);//�����ٷֱȴ���(���澯)
				$("input[@name='overnum']").attr("class","");
				$("input[@name='overnum']").attr("readonly",false);
				$("input[@name='com_day']").val(com_day);//���ɶ�̬��ֵһ������(��)
				$("input[@name='com_day']").attr("class","");
				$("input[@name='com_day']").attr("readonly",false);
				$("select[@name='overlevel']").attr("disabled",false);//������̬��ֵ�澯ʱ�ĸ澯����
				$("select[@name='overlevel'] option").each(function(){
					if($(this).val()==overlevel){
						$(this).attr("selected",true);
						return;
					}
				});
				$("select[@name='reinoverlevel']").attr("disabled",false);//�����ָ��澯ʱ�ļ���
				$("select[@name='reinoverlevel'] option").each(function(){
					if($(this).val()==reinoverlevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
			//������������ͻ��澯����  
			if(intbflag=="1"){
				$("#c_intbflag").attr("checked",true);
				//�������ʱ仯����ֵ(%)
				$("input[@name='ifinoctets']").val(ifinoctets);
				$("input[@name='ifinoctets']").attr("readonly",false);
				$("input[@name='ifinoctets']").attr("class","");
				//��������ͻ��澯������
				$("select[@name='inoperation']").attr("disabled",false);
				$("select[@name='inoperation'] option").each(function(){
					if($(this).val()==inoperation){
						$(this).attr("selected",true);
						return;
					}
				});
				//��������ͻ��澯����
				$("select[@name='inwarninglevel']").attr("disabled",false);
				$("select[@name='inwarninglevel'] option").each(function(){
					if($(this).val()==inwarninglevel){
						$(this).attr("selected",true);
						return;
					}
				});
				//�������ʻָ�ͻ��澯����
				$("select[@name='inreinstatelevel']").attr("disabled",false);
				$("select[@name='inreinstatelevel'] option").each(function(){
					if($(this).val()==inreinstatelevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
			//������������ͻ��澯����
			if(outtbflag=="1"){
				$("#c_outtbflag").attr("checked",true);
				//�������ʱ仯����ֵ(%)
				$("input[@name='ifoutoctets']").val(ifinoctets);
				$("input[@name='ifoutoctets']").attr("readonly",false);
				$("input[@name='ifoutoctets']").attr("class","");
				//��������ͻ��澯������
				$("select[@name='outoperation']").attr("disabled",false);
				$("select[@name='outoperation'] option").each(function(){
					if($(this).val()==outoperation){
						$(this).attr("selected",true);
						return;
					}
				});
				//��������ͻ��澯����
				$("select[@name='outwarninglevel']").attr("disabled",false);
				$("select[@name='outwarninglevel'] option").each(function(){
					if($(this).val()==outwarninglevel){
						$(this).attr("selected",true);
						return;
					}
				});
				//�������ʻָ�ͻ��澯����
				$("select[@name='outreinstatelevel']").attr("disabled",false);
				$("select[@name='outreinstatelevel'] option").each(function(){
					if($(this).val()==outreinstatelevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
		}
	}
	//���Text�����Ƿ���Ч
	function checkTextData(data,chk_target,text_target){
		if(data!=null && $.trim(data)!="" && eval(data)>0){
			chk_target.attr("checked",true);
			text_target.attr("readonly",false);
			text_target.attr("class","");
			text_target.val(data);
		}
	}
	//���Select�����Ƿ���Ч
	function checkSelectDataOne(sel_data,text_data,sel_name,text_target){
		if(eval(sel_data)>0){
			text_target.attr("readonly",false);
			text_target.attr("class","");
			text_target.val(text_data);
			$("select[@name="+sel_name+"] option").each(function(){
				if($(this).val()==sel_data){
					$(this).attr("selected",true);
					return;
				}
			});
		}
	}
	//���Select�����Ƿ���Ч
	function checkSelectDataTwo(sel_data,text1_data,text2_data,sel1_data,sel2_data,sel_name,text1_target,text2_target,sel1_name,sel2_name){
		if(eval(sel_data)>0){
			text1_target.attr("readonly",false);
			text1_target.attr("class","");
			text1_target.val(text1_data);
			text2_target.attr("readonly",false);
			text2_target.attr("class","");
			text2_target.val(text2_data);
			$("select[@name='"+sel1_name+"']").attr("disabled",false);
			$("select[@name='"+sel2_name+"']").attr("disabled",false);
			$("select[@name='"+sel_name+"'] option").each(function(){
				if($(this).val()==sel_data){
					$(this).attr("selected",true);
					return;
				}
			});
			$("select[@name='"+sel2_name+"'] option").each(function(){
				if($(this).val()==sel2_data){
					$(this).attr("selected",true);
					return;
				}
			});
			$("select[@name='"+sel1_name+"'] option").each(function(){
				if($(this).val()==sel1_data){
					$(this).attr("selected",true);
					return;
				}
			});
		}
	}
	//���ѡ��ѡ����
	function chkSelData(sel_name,sel_val){
		$("select[@name='"+sel_name+"' option]").each(function(){
			if($(this).val()==sel_val){
				$(this).attr("selected",true);
			}
		});
	}
	//����˿�
	function SavePort(){
		if($("#td_warn").html()!=""){
			if(!$.checkFluxWarn()){
				return false;
			}
		}
		$("form").submit();
		return false;
	}
</script>
<style type="text/css">
   td.no{
		color:red;
	}
</style>
</head>
<body>
	<form action="configFlux!SavePort.action">
		<br>
		<input type="hidden" name="device_id" value="<s:property value="device_id"/>">
		<input type="hidden" name="port_info" value="<s:property value="port_info"/>"/>
		<table width="94%" class="querytable" align="center">
			<tr>
				<th colspan="4" class="title_1"><s:property value="device_name+'��'+loopback_ip+'��'"/>�˿���Ϣ</th>
			</tr>
			<tr>
				<td class="title_2" width="20%">�豸IP</td>
				<td width="30%">
					<input type="text" name="loopback_ip" readonly class="onread" value="<s:property value="loopback_ip"/>">
				</td>
				<td class="title_2" width="20%">�˿�����</td>
				<td width="30%">
					<input type="text" name="ifindex" readonly class="onread" value="<s:property value="ifindex"/>">
				</td>
			</tr>
			<tr>
				<td class="title_2">�˿�����</td>
				<td>
					<input type="text" name="ifdescr" readonly class="onread" value="<s:property value="ifdescr"/>">
				</td>
				<td class="title_2">�˿�����</td>
				<td>
					<input type="text" name="ifname" readonly class="onread" value="<s:property value="ifname"/>">
				</td>
			</tr>
			<tr>
				<td class="title_2">�˿ڱ���</td>
				<td>
					<input type="text" name="ifnamedefined" readonly class="onread" value="<s:property value="ifnamedefined"/>">
				</td>
				<td class="title_2">�˿�����</td>
				<td>
					<input type="text" name="iftype" readonly class="onread" value="<s:property value="iftype"/>"> 
				</td>
			</tr>
			<tr>
				<td class="title_2">�˿�����(bps)</td>
				<td>
					<input type="text" name="ifspeed" readonly class="onread" value="<s:property value="ifspeed"/>">
				</td>
				<td class="title_2">�˿�����䵥Ԫ</td>
				<td>
					<input type="text" name="ifmtu" readonly class="onread" value="<s:property value="ifmtu"/>">
				</td>
			</tr>
			<tr>
				<td class="title_2">���ٶ˿�����(bps)</td>
				<td colspan="3">
					<input type="text" name="ifhighspeed" readonly class="onread" value="<s:property value="ifhighspeed"/>">
				</td>
			</tr>
			<tr>
				<td class="title_2">�Ƿ�ɼ��˿�������Ϣ</td>
				<td>
					<select name="gatherflag">
						<option value="1" "<s:property value="gatherflag==1?'selected':''"/>">�ɼ�</option>
						<option value="0" "<s:property value="gatherflag==0?'selected':''"/>">���ɼ�</option>
					</select>
				</td>
				<td class="title_2">ԭʼ�����Ƿ����</td>
				<td>
					<select name="intodb">
						<option value="1" "<s:property value="intodb==1?'selected':''"/>">���</option>
						<option value="0" "<s:property value="intodb==0?'selected':''"/>">�����</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="title_1" name="td_show">
					���ø澯
					<img src="<s:url value="/images/ico_9.gif"/>" width="10" hight="12" align="center" border="0" alt="������ø澯" >
				</td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
			<tr id="tr_show" style="display:none;">
				<td id="td_warn" colspan="4">
					
				</td>
			</tr>
			<tr>
				<td class="foot" colspan="4" align="right">
					<button onclick="SavePort()">����</button>
					<button onclick="window.close();">�ر�</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>