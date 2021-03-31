
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>仿真测速账号添加</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript">
        var cityId;
        var netSpeed;
        var url;
        var type;
        function queryAccount(){
        $("#username0").val('');
    	$("#password0").val('');
        for(var i=1;i<=9;i++){
        	$("#username"+i).val('');
    		$("#password"+i).val('');
    		$("#tr"+i).css('display','none');
    	}
        	
        netSpeed=$("#netSpeed").val();
        cityId=$("#cityId").val();
        url = "<s:url value='/ids/addSimSpeedAcc!queryAcc.action'/>";
        if(-1!=netSpeed){
        $.post(url, {
        cityId : cityId,
        netRate : netSpeed
        }, function(ajax) {
        if(""!==(ajax)){
        var strs= new Array();
        strs=ajax.split("###");
        if(null!=strs && strs.length>1){
        	var usernameArr= new Array();
        	usernameArr = strs[0].split(",");
        	if(null!=usernameArr){
        		for(var i=0;i<usernameArr.length;i++){ 
        			$("#tr"+i).css('display','block');
        			$("#username"+i).val(usernameArr[i]);
        		}
        	}
        	
        	var passwordArr= new Array();
        	passwordArr = strs[1].split(",");
        	if(null!=passwordArr){
        		for(var i=0;i<usernameArr.length;i++){
        			$("#tr"+i).css('display','block');
        			$("#password"+i).val(passwordArr[i]);
        		}
        	}
        }
        type=0;
        }else{
        	$("#username0").val("");
            $("#password0").val("");
        	for(var i=1;i<=9;i++){
        		$("#username"+i).val("");
    			$("#password"+i).val("");
    			$("#tr"+i).css('display','none');
    		}
        type=1;
        }
        });
        }else{
        	$("#username0").val("");
            $("#password0").val("");
        	for(var i=1;i<=9;i++){
        		$("#username"+i).val("");
    			$("#password"+i).val("");
    			$("#tr"+i).css('display','none');
    		}
        }

        }
        function addAcc(){
        cityId=$("#cityId").val();
        netSpeed=$("#netSpeed").val();
        // var username=$.trim($("#username0").val());
        // var password=$.trim($("#password0").val());
        
        var username='';
        var password='';       
        for(var i=0;i<=9;i++){
        	if($.trim($("#username"+i).val())!='' && $("#tr"+i).css('display')=='block'){
        		username += ($.trim($("#username"+i).val())) + "##";
        	}
        	if($.trim($("#password"+i).val())!='' && $("#tr"+i).css('display')=='block'){
        		password += ($.trim($("#password"+i).val())) + "##";
        	}
    	}
        
        if(username!='' && $.trim(username).length>=2){
        	username = username.substring(0,$.trim(username).length-2);
        }
        
        if(password!='' && $.trim(password).length>=2){
        	password = password.substring(0,$.trim(password).length-2);
        }
        
        if(-1==netSpeed){
        alert("请选择带宽速率");
        return false;
        }
        
        for(var i=0;i<=9;i++){
        	var username_i=$.trim($("#username"+i).val());
        	for(var j=i+1;j<=9;j++){
        		if(username_i=='' && $.trim($("#username"+j).val())!=''){
        			alert('账号密码不可以有空行');
        			return false;
        		}
        	}
        	
        	var password_i=$.trim($("#password"+i).val());
        	for(var j=i+1;j<=9;j++){
        		if(password_i=='' && $.trim($("#password"+j).val())!=''){
        			alert('账号密码不可以有空行');
        			return false;
        		}
        	}
    	}
        
        for(var i=0;i<=9;i++){
        	var username_i=$.trim($("#username"+i).val());
        	var password_i=$.trim($("#password"+i).val());
        	if((username_i=='' && password_i!='') || (username_i!='' && password_i=='')){
    			alert('账号密码不可以有空行');
    			return false;
    		}
    	}
        
        for(var i=0;i<=9;i++){
        	var username_i=$.trim($("#username"+i).val());
        	for(var j=i+1; j<=9; j++){
            	var username_j=$.trim($("#username"+j).val());
            	if(username_i!='' && username_j!='' && username_i==username_j){
            		alert('账号不可以有重复');
        			return false;
            	}
        	}
    	}
        
        if(IsNull(username,"请输入用户名")&&IsNull(password,"请输入密码")){
        url = "<s:url value='/ids/addSimSpeedAcc!addAcc.action'/>";
        $.post(url, {
        cityId : cityId,
        netRate : netSpeed,
        account : username,
        password : password,
        type : type
        }, function(ajax) {
        if('1'==ajax){
        alert("更新成功");
        }else if('-1'==ajax){
        alert("更新失败");
        }
        });
        }
        }
        
        function addUser(){
        	for(var i=1;i<=9;i++){
        		if($("#tr"+i).css('display')=='none'){
        			$("#tr"+i).css('display','block');
        			break;
        		}
        	}
        }
        
        function delUser(){
        	for(var i=9;i>=1;i--){
        		if($("#tr"+i).css('display')=='block'){
        			$("#tr"+i).css('display','none');
        			$("#username"+i).val('');
        			$("#password"+i).val('');
        			break;
        		}
        	}
        }
        </script>
</head>

<body>
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							仿真测速账号添加</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12" /> 仿真测速账号添加</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<th colspan="4">仿真测速账号添加</th>
					</tr>
					<TR>
						<td class="column" width='15%' align="right">属地</td>
						<TD width="35%"><s:select list="cityList" id="cityId"
								listKey="city_id" listValue="city_name"
								onchange="queryAccount()" cssClass="bk"></s:select></TD>
						<td class="column" width='15%' align="right">带宽速率</td>
						<TD width="35%"><s:select list="netRateList" id="netSpeed"
								listKey="rate" listValue="rate" headerKey="-1" headerValue="请选择"
								cssClass="bk" onchange="queryAccount()"></s:select> (M)</TD>
					</tr>
					<TR id="tr0">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username0" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password0" type="input" value="" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button onclick="addUser()">添 加</button>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button onclick="delUser()">删 除</button>
						</td>
					</tr>
					<TR STYLE="display: none" id="tr1">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username1" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password1" type="input" value="" /></td>
					</tr>
					<TR STYLE="display: none" id="tr2">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username2" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password2" type="input" value="" /></td>
					</tr>
					<TR STYLE="display: none" id="tr3">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username3" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password3" type="input" value="" /></td>
					</tr>
					<TR STYLE="display: none" id="tr4">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username4" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password4" type="input" value="" /></td>
					</tr>
					<TR STYLE="display: none" id="tr5">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username5" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password5" type="input" value="" /></td>
					</tr>
					<TR STYLE="display: none" id="tr6">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username6" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password6" type="input" value="" /></td>
					</tr>
					<TR STYLE="display: none" id="tr7">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username7" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password7" type="input" value="" /></td>
					</tr>
					<TR STYLE="display: none" id="tr8">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username8" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password8" type="input" value="" /></td>
					</tr>
					<TR STYLE="display: none" id="tr9">
						<td class="column" width='15%' align="right">用户名</td>
						<td><input id="username9" value="" /></td>
						<td class="column" width='15%' align="right">密码</td>
						<td><input id="password9" type="input" value="" /></td>
					</tr>
					<TR>
						<td colspan="4" align="right" class=foot>
							<button onclick="addAcc()">&nbsp;提 交&nbsp;</button>
						</td>
					</TR>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
<%@ include file="../foot.jsp"%>