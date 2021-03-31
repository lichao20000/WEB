<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>故障处理</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet"
	type="text/css" />
<script type="text/javascript">
	function DeviceRestart(device_id, oid) {
		if(device_id==''){
			alert('没有设备信息');
			return;
		}
		var url = "<s:url value='/itms/resource/faultTreadtMent!Devicerestart.action'/>";
		$("div[@id='right_tips']").html('');
		$("div[@id='error_tips']").hide();
		$("div[@id='right_tips']").show();
		if(oid==1){
			$("div[@id='right_tips']").html('<span class="right">正在执行重启操作，请稍后...</span>');
		}else{
			$("div[@id='right_tips']").html('<span class="right">正在执行恢复出厂操作，请稍后...</span>');
		}
		$.post(url,{
			        device_id : device_id,
					oid_type : oid,
					type : 1//TR609
					},function(ajax) {
					if (oid == 1) {
					if (ajax == "") {
						$("div[@id='error_tips']").show();
						$("div[@id='right_tips']").hide();
						$("div[@id='error_tips']").html('<span class="icon_error"></span><span class="error">获取值失败</span>');
					} else if (ajax == "重启成功") {
									$("div[@id='error_tips']").hide();
									$("div[@id='right_tips']").show();
									$("div[@id='right_tips']").html('<span class="icon_right"></span><span class="right">重启成功</span>');
					} else if (ajax == "重启失败") {
									$("div[@id='right_tips']").hide();
									$("div[@id='error_tips']").show();
									$("div[@id='error_tips']").html('<span class="icon_error"></span><span class="error">重启失败</span>');
					}
					} else {
						if (ajax == "") {
									$("div[@id='error_tips']").show();
									$("div[@id='right_tips']").hide();
									$("div[@id='error_tips']").html('<span class="icon_error"></span><span class="error">获取值失败</span>');
						} else if (ajax == "恢复出厂设置成功") {
									$("div[@id='error_tips']").hide();
									$("div[@id='right_tips']").show();
									$("div[@id='right_tips']").html('<span class="icon_right"></span><span class="right">调用后台恢复出厂设置成功，请稍后查看设备状态</span>');
						} else if (ajax == "恢复出厂设置失败") {
									$("div[@id='right_tips']").hide();
									$("div[@id='error_tips']").show();
									$("div[@id='error_tips']").html('<span class="icon_error"></span><span class="error">恢复出厂设置失败</span>');
						}
					}
					});
	}
	function softUpgrade(device_id){
		if(device_id==''){
			alert('没有设备信息');
			return;
		}
		var	width=800;    
		var height=450; 
		var url="<s:url value='/itms/resource/faultTreadtMent!querySoftVersion.action'/>"+"?device_id="+device_id;
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
		if(returnVal=="升级成功"){
			$("div[@id='error_tips']").hide();
			$("div[@id='right_tips']").show();
			$("div[@id='right_tips']").html('<span class="icon_right"></span><span class="right">升级成功</span>');
		}else if(returnVal=="升级失败"){
			$("div[@id='error_tips']").show();
			$("div[@id='right_tips']").hide();
			$("div[@id='error_tips']").html('<span class="icon_error"></span><span class="error">升级失败</span>');
		}else{
			$("div[@id='error_tips']").hide();
			$("div[@id='right_tips']").hide();
		}
	}
	//业务下发
	function doBusiness(servTypeId){
		var devId = $("input[@name='device_id']").val();
		if('' == devId){
			alert("用户未绑定设备，请先绑定设备，再下发业务！");
			return false;
		}
		
		if (confirm('确认要下发业务吗?')) {
			$("div[@id='bssError']").hide();
			$("div[@id='bssSuccess']").show();
			$("div[@id='bssSuccess']").html('</span><span class="right">正在执行业务下发操作...</span>');
			var url = "<s:url value='/gwms/config/serviceManSheet!servDone.action'/>";
			var gw_type = $("input[@name='gw_type']").val();
			$.post(url,{
				deviceId:devId,
				servTypeId:servTypeId,
				gw_type:gw_type
			},function(ajax){
				if (ajax == "1")
				{
					requery();
					setTimeout(function(){
						$("div[@id='bssError']").hide();
						$("div[@id='bssSuccess']").show();
						$("div[@id='bssSuccess']").html('<span class="icon_right"></span><span class="right">业务下发成功</span>');
					},3000);
					
				} else {
					$("div[@id='bssError']").show();
					$("div[@id='bssSuccess']").hide();
					$("div[@id='bssError']").html('<span class="icon_error"></span><span class="error">业务下发失败</span>');
					if(ajax == "-5"){
						$("div[@id='bssError']").show();
						$("div[@id='bssSuccess']").hide();
						$("div[@id='bssError']").html('<span class="icon_error"></span><span class="error">调用后台失败</span>');
					}else {
						$("div[@id='bssError']").show();
						$("div[@id='bssSuccess']").hide();
						$("div[@id='bssError']").html('<span class="icon_error"></span><span class="error">无对应用户或用户未开通该业务</span>');
					}
				}
			});
		}
		
	}
	//配置信息	
	function configInfo(userId, oui, deviceSN, deviceId, servTypeId,
			servstauts, wanType, open_status,serUsername) {
		var url = "<s:url value='/itms/resource/faultTreadtMent!getConfigInfo.action'/>";
		$.post(url, {
			device_id : deviceId,
			servType_id : servTypeId,
			servstauts : servstauts,
			wanType : wanType,
			serUsername : serUsername,
			deviceSN : deviceSN
		}, function(mesg) {
			$("div[@id='pz_content']").show();
			$("div[@id='pz_content']").html(mesg);
		});
	}
	
	// BSS工单
	function bssSheet(userId, cityId, servTypeId, type_name, serUsername) {
		// gw_type
		var gw_type = $("input[@name='gw_type']").val();
		var netServ = $("input[@name='netServUp']").val();
		var url = "<s:url value='/itms/resource/faultTreadtMent!getBssSheet.action'/>";
		$.post(url,
				{
					user_id : userId,
					city_id : cityId,
					servTypeId : servTypeId,
					type_name : type_name,
					serUsername : serUsername,
					gw_type : gw_type,
					netServUp : netServ
				}, function(mesg) {
					$("div[@id='pz_content']").show();
					$("div[@id='pz_content']").html(mesg);
				});
	}
	
	function configDetail(user_id, serv_type_id,serUsername) {
		var url = "<s:url value='/itms/resource/faultTreadtMent!getBssIssuedConfigDetail.action'/>";
		$.post(url, {
			user_id : user_id,
			serUsername : serUsername,
			servTypeId : serv_type_id
		}, function(mesg) {
			$("div[@id='pz_content']").show();
			$("div[@id='pz_content']").html(mesg);
		});
	}
	function checkUser(){
		var username =  $("input[@name='username']").val();
		var tempusername = $("input[@name='tempusername']").val();
		var deviceSN =  $("input[@name='deviceSN']").val();
		var queryType = $("input[@name='queryType']:checked").val();
// 		if(queryType!='devicesn'){
// 			alert('用户不支持绑定操作');
// 			return;
// 		}
// 		if((typeof(username)!='undefined' && typeof(deviceSN)!='undefined')||tempusername!=''){
// 			alert("设备("+deviceSN+")已绑定用户,如果重新绑定，请先解绑!\n如果已解绑，请刷新页面后重试");
// 			return 
// 		}
		if((username!='' && deviceSN != '')||tempusername!=''){
			alert("设备("+deviceSN+")已绑定用户,如果重新绑定，请先解绑!\n如果已解绑，请刷新页面后重试");
			return 
		}
		$("input[@name='userName']").val('');
		$("div[@id='bd_content']").show();
	}
	function checkUsernameEmpty() {
		var userName = $("input[@name='userName']").val();
		if(userName==""){
			alert("请输入查询值");
			return false;
		}
		return true;
	}
	function queryUser(){
		if (!checkUsernameEmpty()) {
			return;
		}
		var userName = $("input[@name='userName']").val();
		var gw_type = $("input[@name='gw_type']").val();
		var nameType = $.trim($("select[@name='nameType']").val());
		var url = "<s:url value='/itms/resource/faultTreadtMent!getInstUserInfo.action'/>";
		$.post(url,{
			userName:userName,
			nameType:nameType,
			gw_type:gw_type
		},function(ajax){
			userClear();
		    $("div[@id='div_user']").show();
			$("div[@id='div_user']").html(ajax);
		});	
		//document.all("tr_userinfo").style.display="";	
	}
	//针对选中用户时需要提交的数据
	function userOnclick(user_id,userName,city_id,deviceId,oui,deviceSN,typeName){
		$("input[@name='_userId']").val(user_id);
		$("input[@name='_username']").val(userName);
		$("input[@name='_typename']").val(typeName);
		$("input[@name='_userCityId']").val(city_id);
		$("input[@name='_oldDeviceId']").val(deviceId);
		if(""==deviceId){
			$("div[@id='bd_btn']").show();
		}else{
			if("xj_dx"==instArea){
				alert("该用户账号已绑定，请确认输入账号是否正确！\n如需要继续请点击开始绑定，原绑定设备将要被解绑！");
			}else{
				alert("该用户账号已绑定，请确认输入账号是否正确，\n如需要重新绑定请先解绑！");
			}		
		}
	}
	//针对选中设备时需要提交的数据
	function deviceOnclick(device_id,city_id,oui,device_serialnumber,IpCity_id,cpe_allocatedstatus,flag,manage,device_type){
		$("input[@name='_deviceId']").val(device_id);
		$("input[@name='_deviceCityId']").val(city_id);
		$("input[@name='_oui']").val(oui);
		$("input[@name='_deviceSN']").val(device_serialnumber);
		$("input[@name='_deviceType']").val(device_type);
		$("input[@name='IpCityId']").val(IpCity_id);
		$("input[@name='isBind']").val(cpe_allocatedstatus);
		$("input[@name='flag']").val(flag);
		if(manage=="1"){
		
			if("1"==flag){		
				$("div[@id='div_update']").html("<input type='button' class=jianbian name='save_btn' value=' 属 地 修 正 ' onclick='updateCity()'/>");		
			}else{		
				if(cpe_allocatedstatus=="1"){		
					alert("该设备不需要进行属地修正！如果设备被同本地网其他人员绑定，请联系本地网管理员进行解绑操作");	
				}else{
					$("div[@id='bd_btn']").show();
				}			
			}
			//else if("3"==flag){
			//	$("div[@id='div_update']").html("<font color='red' size='3'>该设备IP没有对应的属地！</font>");
			//}else{
			//	$("div[@id='div_update']").html("<font color='red' size='3'>该设备未上报或上报失败！</font>");
			//}
		}else{
			$("div[@id='div_update']").html("<font color='red' size='3'>该设备你无法进行管理或修正！</font>");				
		}
	}
	//绑定
	function bindBydeviceSN(){
// 		var deviceId = $("input[@name='deviceId']").val();
		
// 		if ("" == deviceId ){
// 			alert('请先选择一个设备！');
// 			return false;
// 		}
		
		var instArea = $("input[@name='instArea']").val();
		if("js_dx"==instArea){
			$("input[@name='username']").val($.trim($("input[@name='userName']").val()));
		}
		if("ehome_self"==$.trim($("input[@name='_typename']").val())){
			alert('该用户使用自备终端，不予绑定!');
			return false;
		}
		
		<%  if(!LipossGlobals.isXJDX() && !LipossGlobals.isSXLT()){%>
		if($.trim($("input[@name='deviceType']").val())!=$.trim($("input[@name='_typename']").val())){
			alert('用户开通的终端类型与待绑定的终端类型不一致，不予绑定!');
			return false;
		}
		<%} %>
		
		var message = "请确认！用户帐号："+$("input[@name='_username']").val()+"，设备序列号："+$("input[@name='deviceSN']").val();
		if (!confirm(message+'！是否继续绑定?')){
			return false;
		}
		var _userId = $("input[@name='_userId']").val();
		var _username = $("input[@name='_username']").val();
		var _typename = $("input[@name='_typename']").val();
		var _userCityId = $("input[@name='_userCityId']").val();
		var deviceCityId = $("input[@name='deviceCityId']").val();
		var _oldDeviceId = $("input[@name='_oldDeviceId']").val();
		var device_id = $("input[@name='device_id']").val();
		var oui = $("input[@name='oui']").val();
		var deviceSN = $("input[@name='deviceSN']").val();
		var gw_type = $("input[@name='gw_type']").val();
		var url = "<s:url value='/itms/resource/faultTreadtMent.action'/>";
		$.post(url,{
			_userId:_userId,
			_username:_username,
			_userCityId:_userCityId,
			device_id:device_id,
			deviceCityId:deviceCityId,
			oui:oui,
			deviceSN:deviceSN,
			gw_type:gw_type
		},function(ajax){	
			$("div[@id='db_result']").show();
			$("input[@name='username']").val(_username);
		    if(ajax=="绑定成功"){
		    	var queryParam = $("input[@name='queryParam']").val();
				var queryType = $("input[@name='queryType']:checked").val();
		    	$("div[@id='db_result']").html('<span class="icon_right"></span><span class="right">用户帐号：'+_username+'和设备序列号：'+deviceSN+ajax+' </span><br><span>正在更新信息，请稍后...</span>');
		    	requery();
		    }else{
		    	$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">用户帐号：'+_username+'和设备序列号：'+deviceSN+ajax+' </span>');
		    }
		});	
	}
	function bindByUser(){
		if("ehome_self"==$.trim($("input[@name='typename']").val())){
			alert('该用户使用自备终端，不予绑定!');
			return false;
		}
		<%  if(!LipossGlobals.isXJDX() && !LipossGlobals.isSXLT()){%>
		if($.trim($("input[@name='_deviceType']").val())!=$.trim($("input[@name='typename']").val())){
			alert('用户开通的终端类型与待绑定的终端类型不一致，不予绑定!');
			return false;
		}
		<%} %>
		
		var message = "请确认！用户帐号："+$("input[@name='username']").val()+"，设备序列号："+$("input[@name='_deviceSN']").val();
		if (!confirm(message+'！是否继续绑定?')){
			return false;
		}
		
		var _userId = $("input[@name='user_id']").val();
		var _username = $("input[@name='username']").val();
		var _typename = $("input[@name='typename']").val();
		var _userCityId = $("input[@name='city_id']").val();
		var deviceCityId = $("input[@name='_deviceCityId']").val();
		var device_id = $("input[@name='_deviceId']").val();
		var oui = $("input[@name='_oui']").val();
		var deviceSN = $("input[@name='_deviceSN']").val();
		var gw_type = $("input[@name='gw_type']").val();
		var url = "<s:url value='/itms/resource/faultTreadtMent.action'/>";
		$.post(url,{
			_userId:_userId,
			_username:_username,
			_userCityId:_userCityId,
			device_id:device_id,
			deviceCityId:deviceCityId,
			oui:oui,
			deviceSN:deviceSN,
			gw_type:gw_type
		},function(ajax){	
			$("div[@id='db_result']").show();
			$("input[@name='username']").val(_username);
		    if(ajax=="绑定成功"){
		    	var queryParam = $("input[@name='queryParam']").val();
				var queryType = $("input[@name='queryType']:checked").val();
		    	$("div[@id='db_result']").html('<span class="icon_right"></span><span class="right">用户帐号：'+_username+'和设备序列号：'+deviceSN+ajax+' </span><br><span>正在更新信息，请稍后...</span>');
		    	requery();
		    }else{
		    	$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">用户帐号：'+_username+'和设备序列号：'+deviceSN+ajax+' </span>');
		    }
		});	
	}
	//add qixueqi
	//重新查询用户时时清空已选择的用户
	function userClear(){
		$("input[@name='_userId']").val("");
		$("input[@name='_username']").val("");
		$("input[@name='_typename']").val("");
		$("input[@name='_userCityId']").val("");
		$("input[@name='_oldDeviceId']").val("");
	}
	//重新查询设备时清空已选择的设备
	function deviceClear(){
		$("input[@name='_deviceId']").val("");
		$("input[@name='_deviceCityId']").val("");
		$("input[@name='_oui']").val("");
		$("input[@name='_deviceSN']").val("");
		$("input[@name='_deviceType']").val("");
		$("input[@name='IpCityId']").val("");
		$("input[@name='isBind']").val("");
		$("input[@name='flag']").val("");
	}
	//解绑操作
	function release(){
		
		var user_id = $("input[@name='user_id']").val();
		var deviceSN = $("input[@name='deviceSN']").val();
		var device_id = $("input[@name='device_id']").val();
		var city_id = $("input[@name='city_id']").val();
		var username = $("input[@name='username']").val();
		var queryType = $("input[@name='queryType']:checked").val();
// 		if(queryType=='devicesn'){
// 			alert('设备不支持解绑操作');
// 			return;
// 		}
		if ("" == user_id ){
			alert('请先选择一个用户！');
			return false;
		}
// 		if (typeof(deviceSN)=='undefined'){
// 			alert('用户没有绑定设备！');
// 			return false;
// 		}
		if (deviceSN==''){
			alert('用户没有绑定设备！');
			return false;
		}
// 		if(typeof(username)=='undefined'){
// 			alert('没有用户绑定此设备！');
// 			return false;
// 		}
		var message = "请确认！用户帐号："+$("input[@name='username']").val()+"，设备序列号："+$("input[@name='deviceSN']").val();
		if (!confirm(message+'！是否继续解绑?')){
			return false;
		}
		var url = "<s:url value='/itms/resource/faultTreadtMent!release.action'/>";
		$.post(url,{
			user_id:user_id,
			device_id:device_id,
			city_id:city_id,
			userName:username
		},function(ajax){
			$("div[@id='db_result']").show();
			if(ajax=="解绑成功"){
		    	$("div[@id='db_result']").html('<span class="icon_right"></span><span class="right">用户帐号：'+username+'和设备序列号：'+deviceSN+ajax+' </span><br><span>正在更新信息，请稍后...</span>');
		    	requery();
		    }else{
		    	$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">用户帐号：'+username+'和设备序列号：'+deviceSN+ajax+'</span>');
		    }
		});	
	}
	function GoContent(user_id, gw_type) {
		//gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		var strpage = "<s:url value='/inmp/resource/HGWUserRelatedInfo.jsp'/>?user_id="
				+ user_id;
		if (gw_type == "2") {
			strpage = "<s:url value='/itms/resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		}
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	function DetailDevice(device_id) {
		var gw_type = "";
		var strpage = "<s:url value='/inmp/resource/DeviceShow.jsp'/>?device_id="
				+ device_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	function requery(){
		var queryParam = $("input[@name='queryParam']").val();
		var queryType = $("input[@name='queryType']:checked").val();
		var user_id = $("input[@name='user_id']").val();
		if (queryParam == "") {
			alert("请输入相关信息");
			return;
		}
		var url = "<s:url value='/itms/resource/faultTreadtMent!queryUserAndDeviceInfo.action'/>"; 
		$.post(url, {
			queryParam : queryParam,
			queryType : queryType,
			user_id : user_id
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
		});
	}
	//根据设备序列号获取设备 与用户进行绑定
	function queryDevice(){
		var deviceinfo = $("input[@name='deviceinfo']").val();
		var nameType = $.trim($("select[@name='nameType']").val());
		
		if(deviceinfo==''){
			alert('请输入设备信息');
			return;
		}
		var url = "<s:url value='/itms/resource/faultTreadtMent!queryDevice.action'/>";
		$.post(url,{
			deviceinfo:deviceinfo,
			nameType : nameType
		},function(ajax){
			deviceClear();
			$("div[@id='div_user']").show();
			$("div[@id='div_user']").html(ajax);
		});
	}
	//awifi开通
	function awifiOpen(){
		$("div[@id='wifi_open']").show();
		$("div[@id='wifi_close']").hide();
		$("div[@id='luyou_open']").hide();
		$("div[@id='luyou_close']").hide();
		$("div[@id='bd_content']").hide();
		$("div[@id='db_result']").hide();
	}
	//判断是否已经存在关闭无线业务,如果没有则执行
	function isHaveStrategy(){
		var device_id = $("input[@name='device_id']").val();
		 $("div[@id='db_result']").show();
		var isAwifiUrl = "<s:url value='/itms/resource/faultTreadtMent!isHaveStrategy.action'/>";
		$.post(isAwifiUrl, {
			device_id : device_id
	     },function(ajax){
	    	 if(ajax != ''){
	    		 $("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">'+ajax+'</span>');
	  			return;
	 		}else{
	 			doExecuteClose(0,1);
	 		}
	     });
	}
	//判断是否支持awifi开通
	function isAwifi(){
		var device_id = $("input[@name='device_id']").val();
		var isAwifiUrl = "<s:url value='/itms/resource/faultTreadtMent!isAwifi.action'/>";
		$.post(isAwifiUrl, {
			device_id : device_id
	     },function(ajax){
	    	 if(ajax == 0){
	    		 $("div[@id='db_result']").show();
	    		 $("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">终端版本不支持开通，开通失败</span>');
// 	  			tips = "终端版本不支持开通，开通失败";
	  			return;
	 		}else{
	 			doExecute(1,1);
	 		}
	     });
	}
	//awifi关闭
	function awifiClose(){
		$("div[@id='wifi_open']").hide();
		$("div[@id='wifi_close']").show();
		$("div[@id='luyou_open']").hide();
		$("div[@id='luyou_close']").hide();
		$("div[@id='bd_content']").hide();
		$("div[@id='db_result']").hide();
		
	}
	//开通aWifi
	function doExecute(flag,do_type){
		var device_id = $("input[@name='device_id']").val();
		var strategy_type  = $("select[@name='strategy_type']").val();
		var url = "<s:url value='/itms/resource/faultTreadtMent!doConfig.action'/>";
		$("div[@id='db_result']").show();
		$("div[@id='db_result']").html('正在执行开通aWIFI无线业务,请稍后...');
			$.post(url,{
				strategy_type : strategy_type, 
	            flag : flag,
	            ssid : 'aWiFi',
	            vlanIdMark : 32,
	            wireless_port : 4,
	            buss_level : 0,
	            device_id : device_id,
	            gw_type: 1,
				do_type: do_type,
				awifi_type :　1
	         },function(ajax){
					if("1"==ajax){
						$("div[@id='db_result']").html('<span class="icon_right"></span><span class="right">后台执行成功</span>');
					}else if ("-4"==ajax){
						$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">后台执行失败</span>');
					}else{
						$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">'+ajax+'</span>');
					} 
	          });
		
	}
	//关闭无线业务
	function doExecuteClose(flag,do_type){
		var device_id = $("input[@name='device_id']").val();
		var strategy_type  = $("select[@name='strategy_type']").val();
		var wireless_port  = 4;
		var vlanid;
		if(wireless_port == 3){
			vlanid = 33;
		}else if(wireless_port == 4){
			vlanid = 32;
		}
		$("div[@id='db_result']").show();
		$("div[@id='db_result']").html('正在执行关闭aWIFI无线业务,请稍后...');
		var url = "<s:url value='/itms/resource/faultTreadtMent!doConfig.action'/>";
			$.post(url,{
	            flag : flag,
	            vlanIdMark : vlanid,
	            wireless_port : wireless_port,
	            strategy_type : strategy_type,
	            device_id : device_id,
	            gw_type: 1,
	            do_type: do_type,
	            awifi_type :　1
	         },function(ajax){
					if("1"==ajax){
						$("div[@id='db_result']").html('<span class="icon_right"></span><span class="right">后台执行成功</span>');
					}else if ("-4"==ajax){
						$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">调用后台预读模块失败</span>');
					}else{
						$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">用户规格不支持无线开通</span>');
					} 
	          });
		
	}
	function luyouOpen(){
		$("div[@id='wifi_open']").hide();
		$("div[@id='wifi_close']").hide();
		$("div[@id='luyou_open']").show();
		$("div[@id='luyou_close']").hide();
		$("div[@id='db_result']").hide();
// 		var serUsername = $("input[@name='serUsername']").val();
// 		alert(serUsername);
	}
	function luyouClose(){
		$("div[@id='wifi_open']").hide();
		$("div[@id='wifi_close']").hide();
		$("div[@id='luyou_open']").hide();
		$("div[@id='luyou_close']").show();
		$("div[@id='db_result']").hide();
	}
	//根据宽带帐号查询是否需要手工改路由
	function doQueryBySer(flag){
		var username;
		if(flag == '1'){
			username = $("select[@name='netAcc']").find("option:selected").text();
			}else{
				username = $("select[@name='netAccclose']").find("option:selected").text();
				}
		
		if(username=="" || typeof(username)=='undefined' || username=="请选择账号"){
			alert("宽带帐号为空");
			return ;
		}
		
// 		alert();
		var netNum = '';
		$.trim($("#netNum").val());
		if(flag==1){
			netNum=$.trim($("#netNumOpen").val());
			if("-1" == netNum)
			{
				alert("请输入上网个数。");
				return;
			}
		}
		
		$("div[@id='db_result']").show();
		if(flag==1){
			$("div[@id='db_result']").html('正在执行开通路由业务,请稍后...');
		}else{
			$("div[@id='db_result']").html('正在执行关闭路由业务,请稍后...');
		}
		var url = "<s:url value='/itms/resource/faultTreadtMent!getServUserInfo.action'/>"; 
		$.post(url,
			   {serUsername : username,
			    gw_type : 1},
			   function(ajax){
				   	if('2'==ajax){
				   		$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">该业务用户对应多个用户！</span>');
				   	}
			   		else if('1'==ajax){
// 			   			$("#result").html("该业务用户已存在!");
						doIssued(flag)
			   		}else if('0'==ajax){
			   			$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">该用户不存在！</span>');
			   		}else if('-1'==ajax){
			   			$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">业务用户不存在,请先开通宽带业务！</span>');
			   		}
			   		else if('-2'==ajax){
			   			$("#result").html("请先绑定设备！");
			   			$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">请先绑定设备！</span>');
			   		}
			   });
	}
	/**
	*发送开通路由工单	
	**/
	function doIssued(flag){
		var username;
		if(flag == '1'){
			username = $("select[@name='netAcc']").find("option:selected").text();
			}else{
				username = $("select[@name='netAccclose']").find("option:selected").text();
				}
		
// 		$("#sendBtn").css("disabled",true);
// 		$("#closeBtn").css("disabled",true);
		var netNum = '';
		$.trim($("#netNum").val());
		if(flag==1){
			netNum=$.trim($("#netNumOpen").val());
		}else{
			netNum=$.trim($("#netNumClose").val());
		}
		if("-1" == netNum)
		{
			alert("请输入上网个数。");
			return;
		}
// 		var city_id = $("input[@name='city_id']").val();
		$("div[@id='db_result']").show();
		$("div[@id='db_result']").html('正在执行开通路由业务,请稍后...');
		var url = "<s:url value='/itms/resource/faultTreadtMent!doExecute.action'/>"; 
		$.post(url,
			   {serUsername : username,
				city_id : '00',
			    gw_type : 1,
			    flag : flag,
			    netNum : netNum
			    },
			   function(ajax){
			   		$("#sendBtn").css("disabled",false);
			   		$("#closeBtn").css("disabled",false);
			   		var array  = ajax.split("|||");
			   		if(array.length > 2){
			   			$("div[@id='db_result']").html('<span class="icon_error"></span><span class="error">返回结果："'+array[2]+'</span>');
			   		}else{
			   			$("div[@id='db_result']").html('<span class="icon_right"></span><span class="right">返回结果：工单发送成功</span>');
			   		}
			   });
	}
	function getAllInfo(){
		var device_id = '<s:property value="deviceMap.device_id" />';
		var city_id = '<s:property value="deviceMap.city_id" />';
		var messageCaiji = '<s:property value="deviceMap.device_id" />'+","+'<s:property value="deviceMap.city_id" />';
	
		if(messageCaiji!=""){
		var url = "faultCollectByTran.jsp?messageCaiji="+messageCaiji;
			//var url = "faultCollectByTran.jsp?device_id="+device_id+"&cityId="+city_id;
		/* window.open(url,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes").onbeforeload = function(){
			document.write("<font>正在加载,请稍后...</font>");
		}; */
		window.open(url,"","left=200,top=200,width=1000,height=400,resizable=yes,scrollbars=yes");
	}else{
		alert("未绑定设备");
		}
 		/* $.post(url,{
 			device_id:device_id,
 			user_id:user_id,
 			gw_type:1
 	    },function(mesg){
 			$("div[@id='divAllInfo']").html(mesg);
 			parent.unblock();
 			parent.dyniframesize();
 	    }); */
	}
	
	//隐藏配置信息
	function configInfoClose(){
		$("div[@id='pz_content']").hide();
	}
	
	$('#btn_msbd').click(function(){
		$('#bd_content').hide();
	})
	$('.sta_close').click(function(){
		$(this).parents('.stainfo').hide();
	})
	$('.give_list').hide();
	$('.give_btn').mouseover(function(){
			$('.give_list').show();
	})
	$('.it_table').mouseout(function(){
			$('.give_list').hide();
	})
	$(document).ready(function() { 
		var device_id_judge = '<s:property value="deviceMap.device_id" />';
		if(null != device_id_judge&&"" !=device_id_judge){
				var url = "<s:url value='/itms/resource/faultTreadtMent!queryConnMsgACT.action'/>"; 
	$.post(url, {
			device_id_judge : device_id_judge
		}, function(ajax) {
			if("1"==ajax){
				$("div[@id='connMsg']").html('<span class="icon_linked"></span><span class="right">正常交互</span>');
			}else if("-6"==ajax){
				$("div[@id='connMsg']").html('<span class="icon_nolink"></span><span class="error">设备正被操作，请稍后再试</span>');
			}else{
				$("div[@id='connMsg']").html('<span class="icon_nolink"></span><span class="error">不能交互</span>');
				}
		});
	}
	}); 

</script>
</head>

<body>
<input type="hidden" name="tempusername" value='' />
	<s:if test="msg=='false'">
		<div class="it_stips" id="nouser">系统没有此用户！</div>
	</s:if>
	<s:else>
		<div class="it_main">
<%-- 			<s:if test="deviceMap!=null"> --%>
				<div class="content">
					<div class="itms_tit">
						<h2>设备信息</h2>
						<div class="it_tip">
				<!--  			<s:if test='deviceMap.deviceConnStatus=="1"'>
								<div class="linked">
									<span class="icon_linked"></span><span class="right">正常交互</span>
								</div>
							</s:if>
							<s:else>
								<div class="nolink">
									<span class="icon_nolink"></span><span class="error">不能交互</span>
								</div>
							</s:else>
							
				-->
							<s:if test='deviceMap.deviceConnStatus=="0"'>
								<div class="nolink">
									<span class="icon_nolink"></span><span class="error">不能交互</span>
								</div>
							</s:if>
							<s:else>
								<div class="nolink" id="connMsg">
									<span class="icon_nolink"></span><span class="error">正在检测. . .</span>
								</div>
							</s:else>
							
							
							
							
							
							
						</div>
						<div class="it_deal">
							<a class="deal_flesh"
								href="javascript:DeviceRestart('<s:property value="deviceMap.device_id" />','1')">重启</a>
							<a class="deal_back"
								href="javascript:DeviceRestart('<s:property value="deviceMap.device_id" />','2')">恢复出厂设置</a>
							<a class="deal_level" href="javascript:softUpgrade('<s:property value="deviceMap.device_id" />','2')">升级</a>
							<input type="hidden" name="device_id" value='<s:property value="deviceMap.device_id" />' />
							<input type="hidden" name="deviceCityId" value='<s:property value="deviceMap.city_id" />' />
							<input type="hidden" name="device_oui" value='<s:property value="deviceMap.oui" />' />
							<input type="hidden" name="deviceSN" value='<s:property value="deviceMap.device_serialnumber" />' />
							<input type="hidden" name="deviceType" value='<s:property value="deviceMap.device_type" />' />
						</div>
					</div>
					<div>
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							class="it_table">
							<tr>
								<th>设备厂商</th>
								<th>设备型号</th>
								<th>软件版本</th>
								<th>属地</th>
								<th>设备类型</th>
								<th>设备序列号</th>
								<th>硬件版本</th>
							</tr>
							<tr>
								<td><s:property value="deviceMap.vendor_name" /><s:if test="deviceMap.oui!=null">(<s:property value="deviceMap.oui" />）</s:if></td>
								<td><s:property value="deviceMap.device_model" /></td>
								<td><s:property value="deviceMap.softwareversion" /></td>
								<td><s:property value="deviceMap.city_name" /></td>
								<td><s:property value="deviceMap.device_type" /></td>
								<td><s:property value="deviceMap.device_serialnumber" /></td>
									<td><s:property value="deviceMap.hardwareversion" /></td>
								<td></td>
							</tr>
							<tr>
								<th>设备规格</th>
								<th>绑定状态</th>
								<th>确认状态</th>
								<th>是否最新版本</th>
								<th>版本是否规范</th>
								<th>终端上报LOID</th>
								<th></th>
							</tr>
							<tr>
								<td><s:property value="deviceMap.spec_name" /></td>
								<td><s:property value="deviceMap.cpe_allocatedstatus" /></td>
								<td><s:property value="deviceMap.device_status" /></td>
								<td><s:property value="deviceMap.haveNew" /></td>
								<td><s:property value="deviceMap.is_normal" /></td>
								<td><s:property value="deviceMap.device_id_ex" /></td>
								<td>
								<s:if test='deviceMap==null'>
								<a href="javascript:alert('没有用户绑定此设备!');" class="itta_more"></a>
								</s:if>
								<s:else>
								<a href="javascript:DetailDevice('<s:property value="deviceMap.device_id" />');" class="itta_more">查看详情</a></s:else></td>
							</tr>
							
						</table>
					</div>
					<div id="error_tips" class="error_tips" style="display: none">
						<span class="icon_error"></span><span class="error">报错信息</span>
					</div>
					<div id="right_tips" class="right_tips" style="display: none">
						<span class="icon_right"></span><span class="right">调用后台恢复出厂设置成功，请稍后查看设备状态</span>
					</div>
				</div>
<%-- 			</s:if> --%>
<%-- 			<s:if test="userMap!=null"> --%>
				<div class="content">
					<div class="itms_tit">
						<h2>用户信息</h2>
					</div>
					<div>
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							class="it_table">
							<tr>
								<th>用户帐号</th>
								<th>用户规格</th>
								<th>BSS终端类型</th>
								<th>客户类型</th>
								<th>语音协议类型</th>
								<th>用户工单来源</th>
								<th></th>
							</tr>
							<tr>
								<td><s:property value="userMap.username" /></td>
								<td><s:property value="userMap.spec_name" /></td>
								<td><s:property value="userMap.type_name" /></td>
								<td><s:property value="userMap.cust_type_name" /></td>
								<td><s:property value="userMap.protocol" /></td>
								<td><s:property value="userMap.user_type_name" /></td>
								<td>
								<s:if test='userMap==null'>
								<a href="javascript:alert('此用户没有绑定设备!');" class="itta_more"></a>
								</s:if>
								<s:else>
								<a href="javascript:GoContent('<s:property value="userMap.user_id" />',1);" class="itta_more">查看详情</a></s:else></td>
							</tr>
						</table>
						<input type="hidden" name="username" value='<s:property value="userMap.username" />' />
						<input type="hidden" name="user_id" value='<s:property value="userMap.user_id" />' />
						<input type="hidden" name="city_id" value='<s:property value="userMap.city_id" />' />
						<input type="hidden" name="typename" value='<s:property value="userMap.type_name" />' />
					</div>
					<div class="error_tips" style="display: none">
						<span class="icon_error"></span><span class="error">报错信息</span>
					</div>
					<div class="right_tips" style="display: none">
						<span class="icon_right"></span><span class="right">调用后台恢复出厂设置成功，请稍后查看设备状态</span>
					</div>
				</div>
<%-- 			</s:if> --%>
			<s:if test="serlist!=null">
				<s:if test="serlist.size()>0">
					<div class="content">
						<div class="itms_tit">
							<h2>业务工单信息</h2>
							<div class="give_box">
								<a class="give_btn">业务下发</a>
								<ul class="give_list">
								<s:if test="serTypeList!=null">
								<s:if test="serTypeList.size()>0">
								
									<li><a href="javascript:doBusiness('0')">全业务</a></li>
									<s:iterator value="serTypeList">
									<li><a href='javascript:doBusiness("<s:property value="serv_type_id" />")'><s:property value="serv_type_name" /></a></li>
									</s:iterator>
									</s:if>
								</s:if>
								</ul>
							</div>
						</div>
						<div>
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="it_table">
								<tr>
									<th>业务类型</th>
									<th>业务帐号</th>
									<th>属地</th>
									<th>业务工单来源</th>
									<th>开通状态</th>
									<th>开通时间</th>
									<th width="300"></th>
								</tr>
								<s:iterator value="serlist">
									<s:if test='open_status_name!="成功"'>
										<tr class="error_tr">
									</s:if>
									<s:else>
										<tr>
									</s:else>
									<td><s:property value="serv_type_name" /><s:if test='serv_type_id=="10"'>(<s:property value="wan_type" />)<input type="hidden" name="serUsername" value='<s:property value="serUsername" />' /></s:if></td>
									<td>
									<s:property value="serusername" />
									</td>
									<td><s:property value="city_name" /></td>
									<td><s:property value="user_type_name" /></td>
									<td><s:if test='open_status_name=="成功"'>
											<span class="icon_right"/>
										</s:if>
										<s:else>
											<span class="icon_error"/>
										</s:else><span><s:property value="open_status_name" /></span></td>
									<td><s:property value="opendate" /></td>
									<td>
									<a class="td_btn" id="show_pz" href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serusername" />')">
									配置信息</a>
									<s:if test='serv_type_id!="18"'>
										<a class="td_btn" href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serusername" />')">BSS工单</a>
									</s:if> 
									<s:if test='serv_type_id=="10" || serv_type_id=="11" '>
								 <a class="td_btn" href="javascript:configDetail('<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="serusername" />')">配置模版</a>
									</s:if>
									<s:elseif test=' serv_type_id=="14"'>
									<a class="td_btn" href="javascript:configDetail('<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="username" />')">配置模版</a>
									</s:elseif>
									</td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<div class="error_tips" id="bssError" style="display: none">
						<span class="icon_error"></span><span class="error">报错信息</span>
					</div>
					<div class="right_tips" id="bssSuccess" style="display: none">
						<span class="icon_right"></span><span class="right">调用后台恢复出厂设置成功，请稍后查看设备状态</span>
					</div>
					</div>
				</s:if>
			</s:if>
			<div class="stainfo" id="pz_content" style="display: none;">
			</div>
			<div class="content">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="do_table">
					<tr>
					<s:if test="deviceMap!=null">
					<s:if test="userMap!=null">
						<td width="50%"><div class="itms_tit" style="text-align: center">
								<h2>业务开通工具</h2>
							</div></td>
					</s:if>
					</s:if>
						<td width="10"></td>
						<td><div class="itms_tit"  style="text-align: center">
								<h2>故障管理工具</h2>
							</div></td>
					</tr>
					<tr>
					<s:if test="deviceMap!=null">
					<s:if test="userMap!=null">
						<td class="btn_tdline"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">
								<tr>
									<td><a class="link_btn" href="javascript:awifiOpen();"><span>aWIFI 开通</span></a></td>
									<td><a class="link_btn" href="javascript:awifiClose();"><span>aWIFI 关闭</span></a></td>
									<td><a class="link_btn" href="javascript:luyouOpen();"><span>路由开通</span></a></td>
									<td><a class="link_btn" href="javascript:luyouClose();"><span>路由关闭</span></a></td>
								</tr>
							</table></td>
					</s:if>		
					</s:if>
						<td></td>
						<td class="btn_tdline"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">
								<tr>
									<td><a class="manage_btn" href="javascript:checkUser()" id="btn_bd"><span>绑定</span></a></td>
									<td><a class="manage_btn" onclick="release()"><span>解绑</span></a></td>
									<td><a class="manage_btn" href="javascript:getAllInfo()"><span>采集</span></a></td>
								</tr>
							</table></td>
					</tr>
				</table>
			</div>
			<div class="stainfo" id="bd_content" style="display: none">
				<a class="sta_close"></a>
				<div class="sta_tit">绑定操作</div>
				<div class="bd_search">
					选择条件：
					<s:if test='queryType == "devicesn"'>
						<select name="nameType" class="bd_select">
						<option value="1">LOID</option>
						<option value="2">上网宽带账号</option>
						<option value="3">IPTV宽带账号</option>
						<option value="4">VoIP认证号码</option>
						<option value="5">VoIP电话号码</option>
					
					
						</select> &nbsp;&nbsp;&nbsp;&nbsp;查询值：<input name="userName"
						type="text" class="bd_ipt" />&nbsp;&nbsp;<input type="button"
						value="查询" onclick="queryUser()" class="bd_btn" />
					</s:if>
					<s:else>
						<select name="nameType" class="bd_select">
						<option value="1">设备序列号</option>
						<option value="2">设备IP</option>
					
					
						</select> &nbsp;&nbsp;&nbsp;&nbsp;查询值：<input name="deviceinfo"
						type="text" class="bd_ipt" />&nbsp;&nbsp;<input type="button"
						value="查询" onclick="queryDevice()" class="bd_btn" />
					</s:else>
				</div>
				<div id="div_user" style="display: none"></div>
				
				
			</div>
			
			<div class="stainfo" id="wifi_open" style="display: none">
		    <a class="sta_close"></a>
		    <div class="sta_tit">awifi无线业务开通</div>
		    <div class="bd_search">策略方式：<select name="strategy_type" class="bd_select">
		    <option value="1">立即执行</option>
			<option value="2">终端上报执行</option>
			<option value="3">终端重启后执行</option></select>
		    </div>
		    <p class="sta_tip"></p>
		    <div class="bd_yes"><a id="btn_msbd" href="javascript:isAwifi();">开通业务</a></div>
		  </div>
		  <div class="stainfo" id="wifi_close" style="display: none">
		    <a class="sta_close" href="javascript:closeOther()"></a>
		    <div class="sta_tit">awifi无线业务关闭</div>
		    <div class="bd_search">策略方式：<select name="strategy_type" class="bd_select">
			<option value="1">立即执行</option>
			<option value="2">终端上报执行</option>
			<option value="3">终端重启后执行</option>
			</select>
		    </div>
		    <p class="sta_tip"><span class="icon_error"></span><span class="error">您确定要关闭业务？</span></p>
		    <div class="bd_yes"><a id="btn_msbd" href="javascript:isHaveStrategy();">确 定</a></div>
		  </div>
		  <div class="stainfo" id="luyou_open"  style="display: none">
		    <a class="sta_close"></a>
		    <div class="sta_tit">手工开通路由</div>
		    <span class="bd_search" float:left; >宽带账号：<s:select list="netAccList" name="netAcc"
									headerKey="-1" headerValue="请选择账号" listKey="netAcc_id"
									listValue="netAcc_name" value="netAcc_name" class="bd_select"></s:select>
		    </span>
		    <span class="bd_search">上网个数：<select name="" class="bd_select" id ="netNumOpen" ><option value="-1">==请选择==</option><option value="4">4</option><option value="8">8</option></select>
		    </span>
		    <p class="sta_tip"></p>
		    <div class="bd_yes"><a id="btn_msbd" href="javascript:doQueryBySer('1');">开通路由</a></div>
		  </div>
		  <div class="stainfo" id="luyou_close"  style="display: none">
		    <a class="sta_close"></a>
		    <div class="sta_tit">手工关闭路由</div>
<%-- 		    <div class="bd_search">上网个数：<select name="netNum" class="bd_select" id ="netNumClose" ><option value="-1">==请选择==</option><option value="4">4</option><option value="8">8</option></select> --%>
<!-- 		    </div> -->
							<span class="bd_search" float:left; >宽带账号：<s:select list="netAccList" name="netAccclose"
									headerKey="-1" headerValue="请选择账号" listKey="netAcc_id"
									listValue="netAcc_name" value="netAcc_name" class="bd_select"></s:select>
		    			</span>
		    <p class="sta_tip"><span class="icon_error"></span><span class="error">您确定要关闭路由？</span></p>
		    <div class="bd_yes"><a id="btn_msbd" href="javascript:doQueryBySer('0');">确 定</a></div>
		  </div>
			<div class="right_tips" id="db_result" style="display: none">
<%-- 				<span class="icon_right"></span><span class="right">用户帐号：27CA34EC464518C0和设备序列号：44300E0300520B4E5 --%>
<%-- 					绑定成功！ </span> --%>
			</div>
			
		</div>
	</s:else>
</body>
</html>
