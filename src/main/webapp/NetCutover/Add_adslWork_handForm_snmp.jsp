<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.DeviceAct" %>
<%@ include file="../head.jsp"%>

<%
	//属地信息
	
	DeviceAct deviceAct = new DeviceAct();
	String 	strCityList = deviceAct.getCityListSelf(true, "", "", request);

 %>
<script type="text/javascript" src="../Js/jquery.js" /></script>
<SCRIPT LANGUAGE="JavaScript">
<!--

function send(){
	
	var flag = false; 
		
	if($("select[@name=username]").val() == -1){
	    alert("请选择用户帐号！");
	    $("select[@name=username]").focus();
	    return false;
    }
    if(document.frm.service_type700.checked){
    
    	flag = true;
    	if(getRadioValue("operate_type700")== 50){
    	
    		if(Trim(document.frm.num700.value) == ""){
	    		alert("请输入激活数量！");
	    		document.frm.num700.focus();
	    		return false;
    		}
    		else if(!IsNumber(document.frm.num700.value,"激活数量")){
            	document.frm.num700.focus();
                return false;
			}    		
    	}
    	
    }
    if(document.frm.service_type701.checked){
    	
    	flag = true;
    	if(getRadioValue("operate_type701")== 50){
    	
    		if(Trim(document.frm.num701.value) == ""){
	    		alert("请输入激活数量！");
	    		document.frm.num701.focus();
	    		return false;
    		}
    		else if(!IsNumber(document.frm.num701.value,"激活数量")){
            	document.frm.num701.focus();
                return false;
			}    		
    	}
    	
    }
    if(document.frm.service_type702.checked){
    	flag = true;
    	if(getRadioValue("operate_type702")== 50){
    		if(Trim(document.frm.num702.value) == ""){
	    		alert("请输入激活数量！");
	    		document.frm.num702.focus();
	    		return false;
	    	} 
	    	else if(!IsNumber(document.frm.num702.value,"激活数量")){
            	document.frm.num702.focus();
                return false;
            }
    	}
    	
    }
    if(document.frm.service_type703.checked){
    
    	flag = true;
    	if(getRadioValue("operate_type703")== 50){
    		if(Trim(document.frm.num703.value) == ""){
	    		alert("请输入激活数量！");
	    		document.frm.num703.focus();
	    		return false;
    		}
    		else if(!IsNumber(document.frm.num703.value,"激活数量")){
            	document.frm.num703.focus();
                return false;
            }
    	}
	
    }   
    
    
    if(document.frm.service_type704.checked){
    
    	flag = true;
    	if(getRadioValue("operate_type704")== 50){
    	
    		if(Trim(document.frm.voip_user_num.value) == ""){
	    		alert("请输入语音信箱总用户数！");
	    		document.frm.voip_user_num.focus();
	    		return false;
    		}
    		
    		else if(!IsNumber(document.frm.voip_user_num.value,"语音信箱总用户数")){
            	document.frm.voip_user_num.focus();
                return false;
            }
            
     		if(Trim(document.frm.voip_time.value) == ""){
	    		alert("请输入每条语音最长时间！");
	    		document.frm.voip_time.focus();
	    		return false;
    		}
    		
    		else if(!IsNumber(document.frm.voip_time.value,"每条语音最长时间")){
            	document.frm.voip_time.focus();
                return false;
            }
                       
     		if(Trim(document.frm.voip_mail_num.value) == ""){
	    		alert("请输入单用户最多语音邮件数！");
	    		document.frm.voip_mail_num.focus();
	    		return false;
    		}
    		
    		else if(!IsNumber(document.frm.voip_mail_num.value,"单用户最多语音邮件数")){
            	document.frm.voip_mail_num.focus();
                return false;
            }              
    	}
	
    } 
     if(document.frm.service_type705.checked){
    
    	flag = true;
    	if(getRadioValue("operate_type705")== 50){
    	
    		if(Trim(document.frm.num705.value) == ""){
	    		alert("请输入激活数量！");
	    		document.frm.num705.focus();
	    		return false;
    		}
    		
    		else if(!IsNumber(document.frm.num705.value,"请输入激活数量！")){
            	document.frm.num705.focus();
                return false;
            }           
    	}
	
    }    
    if(document.frm.service_type706.checked){
    
    	flag = true;
    	if(getRadioValue("operate_type706")== 50){
    		if(Trim(document.frm.num706.value) == ""){
	    		alert("请输入激活数量！");
	    		document.frm.num706.focus();
	    		return false;
    		}
    		else if(!IsNumber(document.frm.num706.value,"激活数量")){
            	document.frm.num706.focus();
                return false;
            }
    	}
	
    }  
    if(document.frm.service_type707.checked){   
    	flag = true;
    }  

    if(document.frm.service_type708.checked){
    
    	flag = true;
    	if(getRadioValue("operate_type708")== 50){
    		if(Trim(document.frm.num708.value) == ""){
	    		alert("请输入激活数量！");
	    		document.frm.num708.focus();
	    		return false;
    		}
    		else if(!IsNumber(document.frm.num708.value,"激活数量")){
            	document.frm.num708.focus();
                return false;
            }
    	}
	
    }
    if(document.frm.service_type710.checked){
    
    	flag = true;
    	if(getRadioValue("operate_type710")== 50){
    		if(Trim(document.frm.num710.value) == ""){
	    		alert("请输入激活数量！");
	    		document.frm.num710.focus();
	    		return false;
    		}
    		else if(!IsNumber(document.frm.num710.value,"激活数量")){
            	document.frm.num710.focus();
                return false;
            }
    	}
	
    }
    
 	if(document.frm.service_type709.checked){   
    	flag = true;
    } 
    
    if(!flag){
    	alert("请选择一种业务！");
    	return false;
    }         
	document.frm.submit();
}

function getRadioValue(object){
	var o = document.all(object);
	for(var i=0;i<o.length;i++){
		if(o[i].checked){
			return o[i].value;
			break;
		}
	}
}

function doBack(){
	this.location.reload();
}

function showChild(param){

	if(param == "searchType"){
		if(document.frm.searchType.value == "0"){
			document.all("idsearch").innerHTML = "<input type='text' name='user_name' value=''  size='13' class='bk' ONKEYDOWN='doAction(this);'>";
		}
		else if(document.frm.searchType.value == "1"){
			document.all("idsearch").innerHTML = "<%=strCityList%>";
		}
		else if(document.frm.searchType.value == "2"){
			document.all("idsearch").innerHTML = "<input type='text' name='loopback_ip' value='' size='13' class='bk' ONKEYDOWN='doAction(this);'>";
		} else {
			document.all("idsearch").innerHTML = "";
		}
		
	}
	
	if(param == "city_id"){
		var page = "adsl_userComboBox.jsp?city_id=" + document.frm.city_id.value + "&searchType="+ document.frm.searchType.value;
		document.all("childFrm").src= page;
	}
	if(param == "customer_id")
		{
		
		page = "getusercount_from.jsp?city_id=" + document.frm.city_id.value + "&username1="+ encodeURIComponent(document.frm.customer_id.value)+ "&searchType="+ document.frm.searchType.value;
		document.all("childFrm").src = page;	
		}
	
}

function doAction(){
	var initcode = event.keyCode;
	if(initcode == 13){
		var param = "";
		window.event.returnValue = false;
		
		if(document.frm.searchType.value == "0"){
			if(Trim(document.frm.user_name.value) == ""){
				alert("请输入用户帐号！");
				document.frm.user_name.focus();
				return false;
			}
			param = "&user_name=" + document.frm.user_name.value;
		}
		else if(document.frm.searchType.value == "2"){
		
			if(Trim(document.frm.loopback_ip.value) == ""){
				alert("请输入设备域名！");
				document.frm.loopback_ip.focus();
				return false;
			}
			param = "&loopback_ip=" + document.frm.loopback_ip.value;		
		} 
		
		var page = "adsl_userComboBox.jsp?searchType=" + document.frm.searchType.value + param;
		document.all("childFrm").src = page;	
	}
}


function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}



function disPlay(object){
				
		
		var obj = $("input[@name=service_type" + object.value + "]");
		
		$(obj).each(function(i){	
			if(obj[i].checked){
				$("span[@id=radio" + object.value + "]").show();
				$("span[@id=text" + object.value + "]").show();
			} else {
				$("span[@id=radio" + object.value + "]").hide();
				$("span[@id=text" + object.value + "]").hide();
			}
		});
}


function radiocheck(object){

	if(object.value == 50){
		$("span[@id=text" + object.service + "]").show();
	} else {
		$("span[@id=text" + object.service + "]").hide();
	}
		
}

//-->
</SCRIPT>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<FORM name="frm" action="Add_adslWork_hand.jsp"  method="post">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT="20">
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											工单管理
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">										
										注：带有
										<font color=red>*</font>必填,按输入方式查询的时候，回车提交查询条件！
									</td>

								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >						
								<TR bgcolor="#ffffff">
									<TH nowrap colspan="4">
										SNMP手工工单
									</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="15%" nowarp>
										查询方式 ：
									</TD>
									<TD nowrap width="35%" nowarp>
										<select name="searchType" class="bk" onchange="showChild('searchType');">
											<option value="-1">==请选择==</option>
											
											<option value="1">--按属地过滤--</option>
											
										</select>
										<span id="idsearch">
										</span>
									</TD>
									<TD align="right" width="10%" nowrap>
										客户帐号 ：
									</TD>
									<TD nowrap width="40%">
										<span id="iduser">
											<select name="customer_id" class="bk">
												<option value="-1">==请按条件查询==</option>
											</select>
										</span>
										&nbsp;&nbsp;<font color=red>*</font>								
										用户帐号 ：									
										<span id="idusercount">
											<select name="user_id" class="bk">
												<option value="-1">==请选择客户帐号==</option>
											</select>
										</span>
										&nbsp;&nbsp;<font color=red>*</font>
									</TD>									

								</TR>	
								<TR bgcolor="#ffffff">
									<TD align="right" nowarp>
										网络传真 <input type="checkbox" name="service_type700" value="700" onclick="disPlay(this);">：
									</TD>
									<TD nowrap nowarp>
										<span id="radio700" style="display:">
											<input type="radio" name="operate_type700" value="50" service="700" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type700" value="51" service="700" onclick="radiocheck(this);">全部激活
											<input type="radio" name="operate_type700" value="52" service="700" onclick="radiocheck(this);">关闭
										</span>
									</TD>
									<TD align="right" nowrap>
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text700" style="display:">
											<input type="text" class="bk" name="num700"><font color=red>*</font>一个帐号多个用户使用			
										</span>							
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" nowrap>
										远程办公 <input type="checkbox" name="service_type701" value="701" onclick="disPlay(this);" >：
									</TD>
									<TD nowrap>
										<span id="radio701" style="display:">
											<input type="radio" name="operate_type701" value="50" service="701" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type701" value="51" service="701" onclick="radiocheck(this);" >全部激活
											<input type="radio" name="operate_type701" value="52" service="701" onclick="radiocheck(this);" >关闭
										</span>
									</TD>
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text701" style="display:">
											<input type="text" class="bk" name="num701">	
										</span>									
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right">
									  企业短信 <input type="checkbox" name="service_type702" value="702" onclick="disPlay(this);">：
									</TD>
									<TD nowrap>
										<span id="radio702" style="display:">
											<input type="radio" name="operate_type702" value="50" service="702" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type702" value="51" service="702" onclick="radiocheck(this);">全部激活
											<input type="radio" name="operate_type702" value="52" service="702" onclick="radiocheck(this);">关闭
										</span>
									</TD>
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text702" style="display:">
											<input type="text" class="bk" name="num702"><font color=red>*</font>一个帐号多个用户使用
										</span>										
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" nowarp>
										电话一号通 <input type="checkbox" name="service_type703" value="703" onclick="disPlay(this);">：
									</TD>
									<TD nowrap>
										<span id="radio703" style="display:">
											<input type="radio" name="operate_type703" value="50" service="703" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type703" value="51" service="703" onclick="radiocheck(this);" >全部激活
											<input type="radio" name="operate_type703" value="52" service="703" onclick="radiocheck(this);" >关闭
										</span>
									</TD>
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text703" style="display:">
											<input type="text" class="bk" name="num703">
										</span>										
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right">										
										语音留言 <input type="checkbox" name="service_type704" value="704" onclick="disPlay(this);">：
									</TD>
									<TD nowrap vlign="top">
										<span id="radio704" style="display:">
											<input type="radio" name="operate_type704" value="50" service="704" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type704" value="51" service="704" onclick="radiocheck(this);">全部激活
											<input type="radio" name="operate_type704" value="52" service="704" onclick="radiocheck(this);">关闭
										</span>
									</TD>
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text704" style="display:">
												<input type="text" class="bk" name="voip_user_num">&nbsp;(语音信箱总用户数)<br>
												<input type="text" class="bk" name="voip_time">&nbsp;(每条语音最长时间:m分钟)<br>
												<input type="text" class="bk" name="voip_mail_num">&nbsp;(单用户最多语音邮件数目)
										</span>										
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right">
										Sip分机 <input type="checkbox" name="service_type705" value="705" onclick="disPlay(this);">：
									</TD>
									<TD nowrap>
										<span id="radio705" style="display:">
											<input type="radio" name="operate_type705" value="50" service="705" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type705" value="51" service="705" onclick="radiocheck(this);">全部激活
											<input type="radio" name="operate_type705" value="52" service="705" onclick="radiocheck(this);">关闭
										</span>
									</TD>
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text705" style="display:">
												<input type="text" name="num705" class="bk" value="">
												<!-- <input type="text" name="phone_trans_num" value="" class="bk">&nbsp;(呼叫转移用户数)<br>	  -->																					
										</span>									
									</TD>
								</TR>																																
								<TR bgcolor="#ffffff">
									<TD align="right">
										电话会议 <input type="checkbox" name="service_type706" value="706" onclick="disPlay(this);">：
									</TD>
									<TD nowrap>
										<span id="radio706" style="display:">
											<input type="radio" name="operate_type706" value="50" service="706" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type706" value="51" service="706" onclick="radiocheck(this);">全部激活
											<input type="radio" name="operate_type706" value="52" service="706" onclick="radiocheck(this);">关闭
										</span>
									</TD>
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text706" style="display:">
											<input type="text" class="bk" name="num706">
										</span>										
									</TD>
								</TR>

								<TR bgcolor="#ffffff">
									<TD align="right">
										NGN <input type="checkbox" name="service_type708" value="708" onclick="disPlay(this);">：
									</TD>
									<TD nowrap>
										<span id="radio708" style="display:">
											<input type="radio" name="operate_type708" value="50" service="708" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type708" value="51" service="708" onclick="radiocheck(this);">全部激活
											<input type="radio" name="operate_type708" value="52" service="708" onclick="radiocheck(this);">关闭
										</span>
									</TD>
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text708" style="display:">
											<input type="text" class="bk" name="num708">
										</span>										
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right">
										移动分机 <input type="checkbox" name="service_type710" value="710" onclick="disPlay(this);">：
									</TD>
									<TD nowrap>
										<span id="radio710" style="display:">
											<input type="radio" name="operate_type710" value="50" service="710" onclick="radiocheck(this);" checked>帐号激活
											<input type="radio" name="operate_type710" value="51" service="710" onclick="radiocheck(this);">全部激活
											<input type="radio" name="operate_type710" value="52" service="710" onclick="radiocheck(this);">关闭
										</span>
									</TD>
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text710" style="display:">
												<input type="text" name="num710" class="bk" value="">
												<!-- <input type="text" name="phone_trans_num" value="" class="bk">&nbsp;(呼叫转移用户数)<br>	  -->																					
										</span>									
									</TD>
								</TR>																									
								<TR bgcolor="#ffffff">
									<TD align="right">
										语音导航 <input type="checkbox" name="service_type707" value="707" onclick="disPlay(this);">：
									</TD>
									<TD nowrap colspan="3">
										<span id="radio707" style="display:">
											<input type="radio" name="operate_type707" value="50" service="707" onclick="radiocheck(this);" checked>功能打开	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;										
											<input type="radio" name="operate_type707" value="52" service="707" onclick="radiocheck(this);" >功能关闭
										</span>
									</TD>
									<!-- 
									<TD align="right">
										激活数量 ：
									</TD>
									<TD nowrap>
										<span id="text707" style="display:">
											<input type="text" class="bk" name="num707">	
										</span>									
									</TD>
									 -->
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right">
										防火墙 <input type="checkbox" name="service_type709" value="709" onclick="disPlay(this);">：
									</TD>
									<TD nowrap colspan="3">
										<span id="radio709" style="display:">
											<input type="radio" name="operate_type709" value="50" service="709" onclick="radiocheck(this);" checked>通用型	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;										
											<input type="radio" name="operate_type709" value="51" service="709" onclick="radiocheck(this);" >加强型
										</span>
									</TD>
								</TR>
																							
								<TR bgcolor="#ffffff">
									<TD nowrap colspan="4" align="right" class="green_foot">
										<input type="button" name="button" value=" 发送工单 " onclick="send();">
									</TD>
								</TR>							
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>

		<TR>
			<TD HEIGHT=20>
				&nbsp;
				<IFRAME ID=childFrm name="childFrm" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
	</TABLE>
</form>
<%@ include file="../foot.jsp"%>
