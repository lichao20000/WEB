<%@ page contentType="text/html;charset=GBK"%>
<title>设备性能配置</title>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun"></SCRIPT>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script src="../Js/CheckForm.js"></script>
<script src="../Js/jquery.js"></script>
<SCRIPT LANGUAGE="JavaScript">
function showChild(param)
{	
	var page ="";
	if(param == "city_id"){
		document.frm.vendor_id.value = "-1";
	}
	if(param == "devicetype_id"){
			page = "getdevice_version.jsp?vendor_id="+document.frm.vendor_id.value + "&devicetype_id="+ encodeURIComponent(document.frm.devicetype_id.value);
			document.all("childFrm2").src = page;
	}
	if(param == "softwareversion")
	{	
		page = "getdevice_model.jsp?city_id="+document.frm.city_id.value
								  +"&vendor_id="+document.frm.vendor_id.value
								  +"&devicetype_id="+document.frm.devicetype_id.value
								  + "&softwareversion=" + document.frm.softwareversion.value 
								  + "&flag=paramInstanceadd_Config&refresh="+Math.random()+"&version=snmpv3";	
		document.all("childFrm3").src = page;
		
	}
	if(param == "vendor_id")
	{
	    //alert("vendor_id1");	
		page = "getdevice_model_from.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;	
		//alert("vendor_id2");		
		page="Pm_map_temp.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm3").src = page;		
	}
}

function relateDevice()
{
   var hguser=document.all("hguser").value;
   var telephone= document.all("telephone").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(1==checkType&&""==hguser&&""==telephone)
   {
      alert("请填写用户名或电话号码！");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="getdevice_model.jsp?hguser="+hguser+"&telephone="+telephone+"&flag=paramInstanceadd_Config&refresh="+Math.random();
      document.all("childFrm1").src = page;

	 
   }
}

function relateDeviceBySerialno()
{
   var serialnumber=document.all("serialnumber").value;
   var loopback_ip=document.all("loopback_ip").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(checkType==2 && serialnumber=="" && loopback_ip == "")
   {
	      alert("请输入设备序列号或者设备域名！");
	      document.all("serialnumber").focus();
	      return false;
   }
   else if(checkType==2)
   {
	      var page="";
	      page="getdevice_model.jsp?serialnumber="+serialnumber+"&loopback_ip=" + loopback_ip + "&flag=paramInstanceadd_Config&refresh="+Math.random()+"&version=snmpv3";
	      document.all("childFrm1").src = page;
   }
   
}
function Del(device_id,expressionid,target){
		if(window.confirm("确认删除?")){
			$.post(
				"pm_config_del.jsp",
				{
					device_id:device_id,
					expressionid:expressionid
				},
				function(data){
					if(eval(data)==true){
						alert("删除成功");
						target.parent().parent().remove();
					}else{
						alert("删除失败");
					}
				}
			);
		}
}
function getInstance(){
	$("input[@name='device_id']").click(function(){
		page="Pm_map_temp.jsp?vendor_id="+$(this).attr("oui");
		document.all("childFrm3").src = page;	
		$("#dev_config").html("<img src='../images/loading.gif'>正在载入数据，请等待......");
		$.post(
			"pm_config_list.jsp",
			{
				device_id:$(this).val()
			},
			function(data){
				$("#dev_config").html(data);
			}
		);
	});
}
function ShowDialog(param)
{
  //根据用户来查询
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
  }
  
  //根据设备版本来查询
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
  }
  
  //根据设备序列号来查询
  if(param==2)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="none";
     tr4.style.display="";
  }
}


function Pm_Name() {
	var name = frm.exp_name.options[frm.exp_name.selectedIndex].text;
	frm.expression_Name.value = name;
}

</SCRIPT>
<form name="frm" method="post" target="childFrm">
  <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td>	
		<table width="100%"  border="0" cellspacing="0" cellpadding="2">
		  <tr>
			<td class=column1> <%@ include file="./SelectDevice_gw.jsp"%> 
			</td>
		  </tr>
		 
		  <tr>
			<td class=column1></td>
		  </tr>
		  <tr>
			<td class=column1><%@ include file="./Pm_table_gw_v3.jsp"%></td>
		  </tr>
		   <tr>
			<td class=column1>
				<div id="dev_config"></div>
			</td>
		  </tr>
		  <tr>
			<td class=column1><div id="childDiv"></div></td>
		  </tr>
		  <tr>
			<td height="40"></td>
		  </tr>
		</table>	  
	   </td>
      </tr>
      <tr>
	 <td><IFRAME ID=childFrm name="childFrm" STYLE="display:none"></IFRAME></td>
    </tr>
    
  </table>
</form>

<%@ include file="../foot.jsp"%>