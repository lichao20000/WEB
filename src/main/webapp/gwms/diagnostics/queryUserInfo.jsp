
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��Ϣ</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/echarts-all.js"/>"></script>
<%-- <script> 
function click(){
if(event.button==2){
alert( '����վ��ӭ�� !!'); 
} 
} 
document.onmousedown=click 
</script>  --%>
<script language="JavaScript">
$(document).ready(function(){ 
	 //��ʼ����
	    $("#userInfo").hide();
	    
});
//��ѯ��Ϣ
function searchByDevSn(){
	var device_serialnumber=$("input[@name='device_serialnumber']").val();
	 if(null==device_serialnumber||device_serialnumber.length<6){
		alert("������������λ�豸���кţ�");
		$("input[@name='device_serialnumber']").focus();
	}else{
		$("#snButton").attr({"disabled":"disabled"});
		 $("#result").html("���ڲ�ѯ......");
		 $("#userInfo").hide();
		var url= "<s:url value='/ids/queryUserInfo!getUserInfoBySn.action'/>";
		$.post(url,{
			device_serialnumber:device_serialnumber
		},function(ajax){
			if(ajax=='0'){
				  $("#result").html("��ѯ�����豸��");
			}else if(ajax=='2'){
				  $("#result").html("��ѯ������豸��");
			}else{
				initPage();
				dealAllData(ajax);
				$("#result").html("");
				$("#userInfo").show();
		   }
			$("#snButton").removeAttr("disabled");//����ť����
		}); 
	} 
}

//��ѯ��Ϣ
function searchByLoid(){
	var loid=$("input[@name='loid']").val();
	if(null==loid||""==loid){
		alert("�������������û��˺�");
		$("input[@name='loid']").focus();
	}
	else{
		$("#loidButton").attr({"disabled":"disabled"});
		$("#result").html("���ڲ�ѯ......");
		$("#userInfo").hide();
		var url= "<s:url value='/ids/queryUserInfo!getUserInfoByLoid.action'/>";
		$.post(url,{
			loid:loid
		},function(ajax){
			if(ajax=='0'){
				 $("#result").html("��ѯ�����û���");
			}else{
				initPage();
				dealAllData(ajax);
				$("#result").html("");
				$("#userInfo").show();
			}
			$("#loidButton").removeAttr("disabled");//����ť����
		}); 
	}
}
//�л���ѯ��ʽ
function deviceSelect_ShowDialog(param)
{
	
	//�����û�����ѯ
	if(param==1)
	{
		this.tr21.style.display="";
		this.tr31.style.display="none";
	}
	
	//�����豸���к�����ѯ
	else if(param==2)
	{
		this.tr21.style.display="none";
		this.tr31.style.display="";
	}
}

function initPage(){
	winNavigate(1);
    $("#lan3status").hide();
    $("#lan3byte").hide();
    $("#lan4status").hide();
    $("#lan4byte").hide();
    
    $("#lan1Status").html("");
	$("#lan1Receive").html("");
	$("#lan1Sent").html("");
	
	$("#lan2Status").html("");
	$("#lan2Receive").html("");
	$("#lan2Sent").html("");
	
	$("#lan3Sta").html("");
	$("#lan3Receive").html("");
	$("#lan3Sent").html("");
	
	$("#lan4Sta").html("");
	$("#lan4Receive").html("");
	$("#lan4Sent").html("");
	 
}
function dealAllData(data){
	var s=eval("("+data+")");
	
    $("#linkman").html(s.linkman);
	 $("#linkaddress").html(s.linkaddress);
	 $("#credno").html(s.credno);
	 $("#linkphone").html(s.linkphone);
	 $("#kdname").html(s.kdname);
	 $("#bandwidth").html(s.bandwidth);
	 $("#username").html(s.username);

	 if(s.username!=null){
	    $("#isGoverment").html('����');
	 }

	 $("#vendor_add").html(s.vendor_add);
	 $("#device_model").html(s.device_model);
	 $("#hardwareversion").html(s.hardwareversion);
	 $("#softwareversion").html(s.softwareversion);
	 $("#register").html(s.register);
	 $("#loopback_ip").html(s.loopback_ip);
	 $("#device_serialnumber").html(s.device_serialnumber);
	 
    if(s.access_type=='1'){
	    $("#access_type").html("DSL");
	 }else if(s.access_type=='2'){
	    $("#access_type").html("Ethernet");
	 }else if(s.access_type=='3'){
	    $("#access_type").html("PON");
	 } 
    
	 if(s.device_type=='1'){
	    $("#device_type").html("��׼��e8-B����");
	 }else if(s.device_type=='2'){
	    $("#device_type").html("�ռ���e8-B����");
	 }else if(s.device_type=='3'){
	    $("#device_type").html("��׼��e8-C����");
	 }else if(s.device_type=='4'){
	    $("#device_type").html("AP������e8-C����");
	 }else if(s.device_type=='5'){
	    $("#device_type").html("����ITMS������ն�����");
	 } 

	 if(s.is_card_apart=='1'){
	    $("#is_card_apart").html("��");
	 }else if(s.is_card_apart=='0'){
	    $("#is_card_apart").html("��");
	 }

	 if(s.ip_model_type=='0'){
	    $("#ip_model_type").html("IPV4");
	 }else if(s.ip_model_type=='1'){
	    $("#ip_model_type").html("PV4+IPV6");
	 }else if(s.ip_model_type=='2'){
	    $("#ip_model_type").html("DS-Lite");
	 }else if(s.ip_model_type=='3'){
	    $("#ip_model_type").html("LAFT6");
	 }else if(s.ip_model_type=='4'){
	    $("#ip_model_type").html("��IPV6");
	 }
	 
	 $("#status").html(s.status);
	 $("#add_time").html(s.add_time);
	 $("#tx_power").html(s.tx_power);
	 $("#rx_power").html(s.rx_power);
	 $("#temperature").html(s.temperature);
	 $("#vottage").html(s.vottage);
	 $("#bais_current").html(s.bais_current);
	 $("#netStatus").html(s.netStatus);
	 $("#reason").html(s.reason);
	 $("#enabledLine1").html(s.enabledLine1);
	 $("#reasonLine1").html(s.reasonLine1);
	 $("#enabledLine2").html(s.enabledLine2);
	 $("#reasonLine2").html(s.reasonLine2);
	 $("#downPert").html(s.downPert);
	 $("#test_time").html(s.test_time); 

var myChart1 = echarts.init(document.getElementById('charts1')); 
var myChart2 = echarts.init(document.getElementById('charts2')); 
var myChart3 = echarts.init(document.getElementById('charts3')); 
var myChart4 = echarts.init(document.getElementById('charts4')); 
var myChart5 = echarts.init(document.getElementById('charts5')); 
var myChart6 = echarts.init(document.getElementById('charts6'));
var myChart7 = echarts.init(document.getElementById('charts7')); 
var myChart8 = echarts.init(document.getElementById('charts8')); 				 
	//lan1��
	 var json2=s.lan1Data;
	 var option7;
	 if(json2.length>0){
		 $("#lan1Status").html(json2[0].status);
		 $("#lan1Receive").html(json2[json2.length-1].bytes_received);
		 $("#lan1Sent").html(json2[json2.length-1].bytes_sent);
		  option7= {
		  		    title : {
		  		        text : 'ʱ����������ͼ'
		  		    },
		  		    tooltip : {
		  		        trigger: 'item',
		  		        formatter : function (params) {
		  		            var date = new Date(params.value[0]);
		  		            data = date.getFullYear() + '-'
		  		                   + (date.getMonth() + 1) + '-'
		  		                   + date.getDate() + ' '
		  		                   + date.getHours() + ':'
		  		                   + date.getMinutes();
		  		            return data + '<br/>'
		  		                   + params.value[1] + ', ' 
		  		                   + params.value[2];
		  		        }
		  		    },
		  		    legend : {
		  		        data : ['lan1�ڽ���(M)','lan1�ڷ���(M)']
		  		    },
		  		    grid: {
		  		        y2: 80
		  		    },
		  		    xAxis : [
		  		        {
		  		            type : 'time',
		  		            splitNumber:15
		  		        }
		  		    ],
		  		    yAxis : [
		  		        {
		  		            type : 'value'
		  		        }
		  		    ],
		  		    series : [
		  		         {
		  		            name: 'lan1�ڽ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                var len = 0;
		  		                while (len < json2.length) {
		  		                    d.push([json2[len].upload_time-0,json2[len].bytes_received-0]);
		  		                       len++;
		  		                }
		  		                return d;
		  		            })()
		  		        },
		  		       {
		  		            name: 'lan1�ڷ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                var len = 0;
		  		                while (len < json2.length) {
		  		                    d.push([json2[len].upload_time-0,json2[len].bytes_sent-0]);
		  		                       len++;
		  		                }
		  		                return d;
		  		            })()
		  		        }
		  		    ]
		  		};
		
	 }else{
		 
		 var start=new Date();
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);
		 option7= {
		  		    title : {
		  		        text : 'ʱ����������ͼ'
		  		    },
		  		    tooltip : {
		  		        trigger: 'item',
		  		        formatter : function (params) {
		  		            var date = new Date(params.value[0]);
		  		            data = date.getFullYear() + '-'
		  		                   + (date.getMonth() + 1) + '-'
		  		                   + date.getDate() + ' '
		  		                   + date.getHours() + ':'
		  		                   + date.getMinutes();
		  		            return data + '<br/>'
		  		                   + params.value[1] + ', ' 
		  		                   + params.value[2];
		  		        }
		  		    },
		  		    legend : {
		  		        data : ['lan1�ڽ���(M)','lan1�ڷ���(M)']
		  		    },
		  		    grid: {
		  		        y2: 80
		  		    },
		  		    xAxis : [
		  		        {
		  		            type : 'time',
		  		            splitNumber:15,
		  		            min:start
		  		        }
		  		    ],
		  		    yAxis : [
		  		        {
		  		            type : 'value'
		  		        }
		  		    ],
		  		    series : [
		  		         {
		  		            name: 'lan1�ڽ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                d.push([new Date(),2500,'-']);
		  		                return d;
		  		            })()
		  		        },
		  		       {
		  		            name: 'lan1�ڷ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                d.push([new Date(),2500,'-']);
		  		                return d;
		  		            })()
		  		        }
		  		    ]
		  		};
	 }
	 myChart7.setOption(option7);
	 //lan2��
	 var json3=s.lan2Data;
	 var option8;
	 if(json3.length>0){
		 $("#lan2Status").html(json3[0].status);
		 $("#lan2Receive").html(json3[json3.length-1].bytes_received);
		 $("#lan2Sent").html(json3[json3.length-1].bytes_sent);
		 option8= {
		  		    title : {
		  		        text : 'ʱ����������ͼ'
		  		    },
		  		    tooltip : {
		  		        trigger: 'item',
		  		        formatter : function (params) {
		  		            var date = new Date(params.value[0]);
		  		            data = date.getFullYear() + '-'
		  		                   + (date.getMonth() + 1) + '-'
		  		                   + date.getDate() + ' '
		  		                   + date.getHours() + ':'
		  		                   + date.getMinutes();
		  		            return data + '<br/>'
		  		                   + params.value[1] + ', ' 
		  		                   + params.value[2];
		  		        }
		  		    },
		  		    legend : {
		  		        data : ['lan2�ڽ���(M)','lan2�ڷ���(M)']
		  		    },
		  		    grid: {
		  		        y2: 80
		  		    },
		  		    xAxis : [
		  		        {
		  		            type : 'time',
		  		            splitNumber:15
		  		        }
		  		    ],
		  		    yAxis : [
		  		        {
		  		            type : 'value'
		  		        }
		  		    ],
		  		    series : [
		  		         {
		  		            name: 'lan2�ڽ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                var len = 0;
		  		                while (len < json3.length) {
		  		                    d.push([json3[len].upload_time-0,json3[len].bytes_received-0]);
		  		                       len++;
		  		                }
		  		                return d;
		  		            })()
		  		        },
		  		       {
		  		            name: 'lan2�ڷ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                var len = 0;
		  		                while (len < json3.length) {
		  		                    d.push([json3[len].upload_time-0,json3[len].bytes_sent-0]);
		  		                       len++;
		  		                }
		  		                return d;
		  		            })()
		  		        }
		  		    ]
		  		    
		  		};
	 }else{
		 var start=new Date();
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);
		 option8= {
		  		    title : {
		  		        text : 'ʱ����������ͼ'
		  		    },
		  		    tooltip : {
		  		        trigger: 'item',
		  		        formatter : function (params) {
		  		            var date = new Date(params.value[0]);
		  		            data = date.getFullYear() + '-'
		  		                   + (date.getMonth() + 1) + '-'
		  		                   + date.getDate() + ' '
		  		                   + date.getHours() + ':'
		  		                   + date.getMinutes();
		  		            return data + '<br/>'
		  		                   + params.value[1] + ', ' 
		  		                   + params.value[2];
		  		        }
		  		    },
		  		    legend : {
		  		        data : ['lan2�ڽ���(M)','lan2�ڷ���(M)']
		  		    },
		  		    grid: {
		  		        y2: 80
		  		    },
		  		    xAxis : [
		  		        {
		  		            type : 'time',
		  		            splitNumber:15,
		  		            min:start
		  		        }
		  		    ],
		  		    yAxis : [
		  		        {
		  		            type : 'value'
		  		        }
		  		    ],
		  		    series : [
		  		         {
		  		            name: 'lan2�ڽ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                d.push([new Date(),2500,'-']);
		  		                return d;
		  		            })()
		  		        },
		  		       {
		  		            name: 'lan2�ڷ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                d.push([new Date(),2500,'-']);
		  		                return d;
		  		            })()
		  		        }
		  		    ]
		  		    
		  		};
	 }
	 myChart8.setOption(option8);

	 //lan3��
	 var json4=s.lan3Data;
	 if(!!json4){  
	 if(json4.length>0){
		 $("#lan3status").show();
		 $("#lan3byte").show();
		 $("#lan3Sta").html(json4[0].status);
		 $("#lan3Receive").html(json4[json4.length-1].bytes_received);
		 $("#lan3Sent").html(json4[json4.length-1].bytes_sent);
		 $("#charts").append('<div id="charts9" style="height:400px"></div>');
		 var myChart9 = echarts.init(document.getElementById('charts9')); 
		 var option9= {
		  		    title : {
		  		        text : 'ʱ����������ͼ'
		  		    },
		  		    tooltip : {
		  		        trigger: 'item',
		  		        formatter : function (params) {
		  		            var date = new Date(params.value[0]);
		  		            data = date.getFullYear() + '-'
		  		                   + (date.getMonth() + 1) + '-'
		  		                   + date.getDate() + ' '
		  		                   + date.getHours() + ':'
		  		                   + date.getMinutes();
		  		            return data + '<br/>'
		  		                   + params.value[1] + ', ' 
		  		                   + params.value[2];
		  		        }
		  		    },
		  		    legend : {
		  		        data : ['lan3�ڽ���(M)','lan3�ڷ���(M)']
		  		    },
		  		    grid: {
		  		        y2: 80
		  		    },
		  		    xAxis : [
		  		        {
		  		            type : 'time',
		  		            splitNumber:15
		  		        }
		  		    ],
		  		    yAxis : [
		  		        {
		  		            type : 'value'
		  		        }
		  		    ],
		  		    series : [
		  		         {
		  		            name: 'lan3�ڽ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                var len = 0;
		  		                while (len < json4.length) {
		  		                    d.push([json4[len].upload_time-0,json4[len].bytes_received-0]);
		  		                       len++;
		  		                }
		  		                return d;
		  		            })()
		  		        },
		  		       {
		  		            name: 'lan3�ڷ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                var len = 0;
		  		                while (len < json4.length) {
		  		                    d.push([json4[len].upload_time-0,json4[len].bytes_sent-0]);
		  		                       len++;
		  		                }
		  		                return d;
		  		            })()
		  		        }
		  		    ]
		  		};
		 $("#charts9").hide();
		 myChart9.setOption(option9);
	 }
  }
	 //lan4��
	 var json5=s.lan4Data;
	 if(!!json5){
	 if(json5.length>0){
		 $("#lan4status").show();
		 $("#lan4byte").show();
		 $("#lan4Sta").html(json5[0].status);
		 $("#lan4Receive").html(json5[json5.length-1].bytes_received);
		 $("#lan4Sent").html(json5[json5.length-1].bytes_sent);
		 $("#charts").append('<div id="charts10" style="height:400px"></div>');
		 var myChart10 = echarts.init(document.getElementById('charts10')); 
		 var option10= {
		  		    title : {
		  		        text : 'ʱ����������ͼ'
		  		    },
		  		    tooltip : {
		  		        trigger: 'item',
		  		        formatter : function (params) {
		  		            var date = new Date(params.value[0]);
		  		            data = date.getFullYear() + '-'
		  		                   + (date.getMonth() + 1) + '-'
		  		                   + date.getDate() + ' '
		  		                   + date.getHours() + ':'
		  		                   + date.getMinutes();
		  		            return data + '<br/>'
		  		                   + params.value[1] + ', ' 
		  		                   + params.value[2];
		  		        }
		  		    },
		  		    legend : {
		  		        data : ['lan4�ڽ���(M)','lan4�ڷ���(M)']
		  		    },
		  		    grid: {
		  		        y2: 80
		  		    },
		  		    xAxis : [
		  		        {
		  		            type : 'time',
		  		            splitNumber:15
		  		        }
		  		    ],
		  		    yAxis : [
		  		        {
		  		            type : 'value'
		  		        }
		  		    ],
		  		    series : [
		  		         {
		  		            name: 'lan4�ڽ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                var len = 0;
		  		                while (len < json5.length) {
		  		                    d.push([json5[len].upload_time-0,json5[len].bytes_received-0]);
		  		                       len++;
		  		                }
		  		                return d;
		  		            })()
		  		        },
		  		       {
		  		            name: 'lan4�ڷ���(M)',
		  		            type: 'line',
		  		            showAllSymbol: false,
		  		            data: (function () {
		  		                var d = [];
		  		                var len = 0;
		  		                var now = new Date();
		  		                var value;
		  		                while (len < json5.length) {
		  		                    d.push([json5[len].upload_time-0,json5[len].bytes_sent-0]);
		  		                       len++;
		  		                }
		  		                return d;
		  		            })()
		  		        }
		  		    ]
		  		};
		 $("#charts10").hide();
		 myChart10.setOption(option10);
	 }
 }  
	 var json=s.chartData;
    var option1,option2,option3,option4,option5;
    if(json.length==0){
   	 var start=new Date();
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);
        

        option1 = {
      		    title : {
      		        text : 'ʱ����������ͼ'
      		    },
      		    tooltip : {
      		        trigger: 'item',
      		        formatter : function (params) {
      		            var date = new Date(params.value[0]);
      		            data = date.getFullYear() + '-'
      		                   + (date.getMonth() + 1) + '-'
      		                   + date.getDate() + ' '
      		                   + date.getHours() + ':'
      		                   + date.getMinutes();
      		            return data + '<br/>'
      		                   + params.value[1] + ', ' 
      		                   + params.value[2];
      		        }
      		    },
      		    legend : {
      		        data : ['PON�ڷ���⹦��(dbm)']
      		    },
      		    grid: {
      		        y2: 80
      		    },
      		    xAxis : [
      		        {
      		            type : 'time',
      		            splitNumber:15,
      		             min:start
      		        }
      		    ],
      		    yAxis : [
      		        {
      		            type : 'value'
      		        }
      		    ],
      		    series : [
      		        {
      		            name: 'PON�ڷ���⹦��(dbm)',
      		            type: 'line',
      		            showAllSymbol: false,
      		            data: (function () {
  	 		                var d = [];
  	 		                d.push([new Date(),4,'-']);
  	 		                return d;
  	 		            })()
      		        }
      		    ]
      		};
        option2 = {
   		    title : {
   		        text : 'ʱ����������ͼ'
   		    },
   		    tooltip : {
   		        trigger: 'item',
   		        formatter : function (params) {
   		            var date = new Date(params.value[0]);
   		            data = date.getFullYear() + '-'
   		                   + (date.getMonth() + 1) + '-'
   		                   + date.getDate() + ' '
   		                   + date.getHours() + ':'
   		                   + date.getMinutes();
   		            return data + '<br/>'
   		                   + params.value[1] + ', ' 
   		                   + params.value[2];
   		        }
   		    },
   		    legend : {
   		        data : ['PON�ڽ��չ⹦��(dbm)']
   		    },
   		    grid: {
   		        y2: 80
   		    },
   		    xAxis : [
   		        {
   		            type : 'time',
   		            splitNumber:15,
   		            min:start
   		        }
   		    ],
   		    yAxis : [
   		        {
   		            type : 'value'
   		        }
   		    ],
   		    series : [
   		        {
   		            name: 'PON�ڽ��չ⹦��(dbm)',
   		            type: 'line',
   		            showAllSymbol: false,
   		            data: (function () {
  	 		                var d = [];
  	 		                d.push([new Date(),-30,'-']);
  	 		                return d;
  	 		            })()
   		        }
   		    ]
   		};
        option3 = {
   		    title : {
   		        text : 'ʱ����������ͼ'
   		    },
   		    tooltip : {
   		        trigger: 'item',
   		        formatter : function (params) {
   		            var date = new Date(params.value[0]);
   		            data = date.getFullYear() + '-'
   		                   + (date.getMonth() + 1) + '-'
   		                   + date.getDate() + ' '
   		                   + date.getHours() + ':'
   		                   + date.getMinutes();
   		            return data + '<br/>'
   		                   + params.value[1] + ', ' 
   		                   + params.value[2];
   		        }
   		    },
   		    legend : {
   		        data : ['��ģ���¶ȡ�']
   		    },
   		    grid: {
   		        y2: 80
   		    },
   		    xAxis : [
   		        {
   		            type : 'time',
   		            splitNumber:15,
   		            min:start
   		        }
   		    ],
   		    yAxis : [
   		        {
   		            type : 'value'
   		        }
   		    ],
   		    series : [
   		        {
   		            name: '��ģ���¶ȡ�',
   		            type: 'line',
   		            showAllSymbol: false,
   		            data: (function () {
  	 		                var d = [];
  	 		                d.push([new Date(),60,'-']);
  	 		                return d;
  	 		            })()
   		        }
   		    ]
   		};
        option4 = {
   		    title : {
   		        text : 'ʱ����������ͼ'
   		    },
   		    tooltip : {
   		        trigger: 'item',
   		        formatter : function (params) {
   		            var date = new Date(params.value[0]);
   		            data = date.getFullYear() + '-'
   		                   + (date.getMonth() + 1) + '-'
   		                   + date.getDate() + ' '
   		                   + date.getHours() + ':'
   		                   + date.getMinutes();
   		            return data + '<br/>'
   		                   + params.value[1] + ', ' 
   		                   + params.value[2];
   		        }
   		    },
   		    legend : {
   		        data : ['��ģ�鹩���ѹ(��)']
   		    },
   		    grid: {
   		        y2: 80
   		    },
   		    xAxis : [
   		        {
   		            type : 'time',
   		            splitNumber:15,
   		            min:start
   		        }
   		    ],
   		    yAxis : [
   		        {
   		            type : 'value'
   		        }
   		    ],
   		    series : [
   		        {
   		            name: '��ģ�鹩���ѹ(��)',
   		            type: 'line',
   		            showAllSymbol: false,
   		            data: (function () {
  	 		                var d = [];
  	 		                d.push([new Date(),5,'-']);
  	 		                return d;
  	 		            })()
   		        }
   		    ]
   		};
        option5 = {
   		    title : {
   		        text : 'ʱ����������ͼ'
   		    },
   		    tooltip : {
   		        trigger: 'item',
   		        formatter : function (params) {
   		            var date = new Date(params.value[0]);
   		            data = date.getFullYear() + '-'
   		                   + (date.getMonth() + 1) + '-'
   		                   + date.getDate() + ' '
   		                   + date.getHours() + ':'
   		                   + date.getMinutes();
   		            return data + '<br/>'
   		                   + params.value[1] + ', ' 
   		                   + params.value[2];
   		        }
   		    },
   		    legend : {
   		        data : ['��ģ��ƫ�õ���(����)']
   		    },
   		    grid: {
   		        y2: 80
   		    },
   		    xAxis : [
   		        {
   		            type : 'time',
   		            splitNumber:15,
   		            min:start
   		        }
   		    ],
   		    yAxis : [
   		        {
   		            type : 'value'
   		        }
   		    ],
   		    series : [
   		        {
   		            name: '��ģ��ƫ�õ���(����)',
   		            type: 'line',
   		            showAllSymbol: false,
   		            data: (function () {
  	 		                var d = [];
  	 		                d.push([new Date(),20,'-']);
  	 		                return d;
  	 		            })()
   		        }
   		    ]
   		};
        
    }else{
         option1 = {
       		    title : {
       		        text : 'ʱ����������ͼ'
       		    },
       		    tooltip : {
       		        trigger: 'item',
       		        formatter : function (params) {
       		            var date = new Date(params.value[0]);
       		            data = date.getFullYear() + '-'
       		                   + (date.getMonth() + 1) + '-'
       		                   + date.getDate() + ' '
       		                   + date.getHours() + ':'
       		                   + date.getMinutes();
       		            return data + '<br/>'
       		                   + params.value[1] + ', ' 
       		                   + params.value[2];
       		        }
       		    },
       		    legend : {
       		        data : ['PON�ڷ���⹦��(dbm)']
       		    },
       		    grid: {
       		        y2: 80
       		    },
       		    xAxis : [
       		        {
       		            type : 'time',
       		            splitNumber:15
       		        }
       		    ],
       		    yAxis : [
       		        {
       		            type : 'value'
       		        }
       		    ],
       		    series : [
       		        {
       		            name: 'PON�ڷ���⹦��(dbm)',
       		            type: 'line',
       		            showAllSymbol: false,
       		            data: (function () {
       		                var d = [];
       		                var len = 0;
       		                while (len < json.length) {
       		                    d.push([json[len].upload_time-0,json[len].tx_power-0]);
       		                       len++;
       		                }
       		                return d;
       		            })()
       		        }
       		    ]
       		};
         option2 = {
    		    title : {
    		        text : 'ʱ����������ͼ'
    		    },
    		    tooltip : {
    		        trigger: 'item',
    		        formatter : function (params) {
    		            var date = new Date(params.value[0]);
    		            data = date.getFullYear() + '-'
    		                   + (date.getMonth() + 1) + '-'
    		                   + date.getDate() + ' '
    		                   + date.getHours() + ':'
    		                   + date.getMinutes();
    		            return data + '<br/>'
    		                   + params.value[1] + ', ' 
    		                   + params.value[2];
    		        }
    		    },
    		    legend : {
    		        data : ['PON�ڽ��չ⹦��(dbm)']
    		    },
    		    grid: {
    		        y2: 80
    		    },
    		    xAxis : [
    		        {
    		            type : 'time',
    		            splitNumber:15
    		        }
    		    ],
    		    yAxis : [
    		        {
    		            type : 'value'
    		        }
    		    ],
    		    series : [
    		        {
    		            name: 'PON�ڽ��չ⹦��(dbm)',
    		            type: 'line',
    		            showAllSymbol: false,
    		            data: (function () {
    		                var d = [];
    		                var len = 0;
    		                while (len < json.length) {
   		                    d.push([json[len].upload_time-0,json[len].rx_power-0]);
   		                       len++;
   		                }
    		                return d;
    		            })()
    		        }
    		    ]
    		};
         option3 = {
    		    title : {
    		        text : 'ʱ����������ͼ'
    		    },
    		    tooltip : {
    		        trigger: 'item',
    		        formatter : function (params) {
    		            var date = new Date(params.value[0]);
    		            data = date.getFullYear() + '-'
    		                   + (date.getMonth() + 1) + '-'
    		                   + date.getDate() + ' '
    		                   + date.getHours() + ':'
    		                   + date.getMinutes();
    		            return data + '<br/>'
    		                   + params.value[1] + ', ' 
    		                   + params.value[2];
    		        }
    		    },
    		    legend : {
    		        data : ['��ģ���¶ȡ�']
    		    },
    		    grid: {
    		        y2: 80
    		    },
    		    xAxis : [
    		        {
    		            type : 'time',
    		            splitNumber:15
    		        }
    		    ],
    		    yAxis : [
    		        {
    		            type : 'value'
    		        }
    		    ],
    		    series : [
    		        {
    		            name: '��ģ���¶ȡ�',
    		            type: 'line',
    		            showAllSymbol: false,
    		            data: (function () {
    		                var d = [];
    		                var len = 0;
    		                while (len < json.length) {
    		                    d.push([json[len].upload_time-0,json[len].temperature-0]);
    		                       len++;
    		                }
    		                return d;
    		            })()
    		        }
    		    ]
    		};
         option4 = {
    		    title : {
    		        text : 'ʱ����������ͼ'
    		    },
    		    tooltip : {
    		        trigger: 'item',
    		        formatter : function (params) {
    		            var date = new Date(params.value[0]);
    		            data = date.getFullYear() + '-'
    		                   + (date.getMonth() + 1) + '-'
    		                   + date.getDate() + ' '
    		                   + date.getHours() + ':'
    		                   + date.getMinutes();
    		            return data + '<br/>'
    		                   + params.value[1] + ', ' 
    		                   + params.value[2];
    		        }
    		    },
    		    legend : {
    		        data : ['��ģ�鹩���ѹ(��)']
    		    },
    		    grid: {
    		        y2: 80
    		    },
    		    xAxis : [
    		        {
    		            type : 'time',
    		            splitNumber:15
    		        }
    		    ],
    		    yAxis : [
    		        {
    		            type : 'value'
    		        }
    		    ],
    		    series : [
    		        {
    		            name: '��ģ�鹩���ѹ(��)',
    		            type: 'line',
    		            showAllSymbol: false,
    		            data: (function () {
    		                var d = [];
    		                var len = 0;
    		                while (len < json.length) {
    		                    d.push([json[len].upload_time-0,json[len].vottage-0]);
    		                       len++;
    		                }
    		                return d;
    		            })()
    		        }
    		    ]
    		};
         option5 = {
    		    title : {
    		        text : 'ʱ����������ͼ'
    		    },
    		    tooltip : {
    		        trigger: 'item',
    		        formatter : function (params) {
    		            var date = new Date(params.value[0]);
    		            data = date.getFullYear() + '-'
    		                   + (date.getMonth() + 1) + '-'
    		                   + date.getDate() + ' '
    		                   + date.getHours() + ':'
    		                   + date.getMinutes();
    		            return data + '<br/>'
    		                   + params.value[1] + ', ' 
    		                   + params.value[2];
    		        }
    		    },
    		    legend : {
    		        data : ['��ģ��ƫ�õ���(����)']
    		    },
    		    grid: {
    		        y2: 80
    		    },
    		    xAxis : [
    		        {
    		            type : 'time',
    		            splitNumber:15
    		        }
    		    ],
    		    yAxis : [
    		        {
    		            type : 'value'
    		        }
    		    ],
    		    series : [
    		        {
    		            name: '��ģ��ƫ�õ���(����)',
    		            type: 'line',
    		            showAllSymbol: false,
    		            data: (function () {
    		                var d = [];
    		                var len = 0;
    		                while (len < json.length) {
    		                    d.push([json[len].upload_time-0,json[len].bais_current-0]);
    		                       len++;
    		                }
    		                return d;
    		            })()
    		        }
    		    ]
    		};
    }
    myChart1.setOption(option1); 
    myChart2.setOption(option2); 
    myChart3.setOption(option3); 
    myChart4.setOption(option4); 
    myChart5.setOption(option5); 
    //pon��
    var option6;
    var json1=s.ponData;
    if(json1.length==0){
        var start=new Date();
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);
  	     option6 = {
  	 		    title : {
  	 		        text : 'ʱ����������ͼ'
  	 		    },
  	 		    tooltip : {
  	 		        trigger: 'item',
  	 		        formatter : function (params) {
  	 		            var date = new Date(params.value[0]);
  	 		            data = date.getFullYear() + '-'
  	 		                   + (date.getMonth() + 1) + '-'
  	 		                   + date.getDate() + ' '
  	 		                   + date.getHours() + ':'
  	 		                   + date.getMinutes();
  	 		            return data + '<br/>'
  	 		                   + params.value[1] + ', ' 
  	 		                   + params.value[2];
  	 		        }
  	 		    },
  	 		    legend : {
  	 		        data : ['pon�ڽ���(M)','pon�ڷ���(M)']
  	 		    },
  	 		    grid: {
  	 		        y2: 80
  	 		    },
  	 		    xAxis : [
  	 		        {
  	 		            type : 'time',
  	 		            splitNumber:15,
  	 		            min:start
  	 		        }
  	 		    ],
  	 		    yAxis : [
  	 		        {
  	 		            type : 'value'
  	 		        }
  	 		    ],
  	 		    series : [
  	 		        {
  	 		            name: 'pon�ڽ���(M)',
  	 		            type: 'line',
  	 		            showAllSymbol: false,
  	 		            data: (function () {
  	 		                var d = [];
  	 		                d.push([new Date(),2500,'-']);
  	 		                return d;
  	 		            })()
  	 		        },
  	 		       {
  	 		            name: 'pon�ڷ���(M)',
  	 		            type: 'line',
  	 		            showAllSymbol: false,
  	 		            data: (function () {
  	 		                var d = [];
  	 		                d.push([new Date(),2500,'-']);
  	 		                return d;
  	 		            })()
  	 		        }
  	 		    ]
  	 		};
    }else{
   	 option6 = {
   	 		    title : {
   	 		        text : 'ʱ����������ͼ'
   	 		    },
   	 		    tooltip : {
   	 		        trigger: 'item',
   	 		        formatter : function (params) {
   	 		            var date = new Date(params.value[0]);
   	 		            data = date.getFullYear() + '-'
   	 		                   + (date.getMonth() + 1) + '-'
   	 		                   + date.getDate() + ' '
   	 		                   + date.getHours() + ':'
   	 		                   + date.getMinutes();
   	 		            return data + '<br/>'
   	 		                   + params.value[1] + ', ' 
   	 		                   + params.value[2];
   	 		        }
   	 		    },
   	 		    legend : {
   	 		        data : ['pon�ڽ���(M)','pon�ڷ���(M)']
   	 		    },
   	 		    grid: {
   	 		        y2: 80
   	 		    },
   	 		    xAxis : [
   	 		        {
   	 		            type : 'time',
   	 		            splitNumber:15
   	 		        }
   	 		    ],
   	 		    yAxis : [
   	 		        {
   	 		            type : 'value'
   	 		        }
   	 		    ],
   	 		    series : [
   	 		        {
   	 		            name: 'pon�ڽ���(M)',
   	 		            type: 'line',
   	 		            showAllSymbol: false,
   	 		            data: (function () {
   	 		                var d = [];
   	 		                var len = 0;
  	 		                	while (len < json1.length) {
  	 	 		                    d.push([json1[len].upload_time-0,json1[len].bytes_received-0]);
  	 	 		                       len++;
  	 	 		                }
   	 		                return d;
   	 		            })()
   	 		        },
   	 		       {
   	 		            name: 'pon�ڷ���(M)',
   	 		            type: 'line',
   	 		            showAllSymbol: false,
   	 		            data: (function () {
   	 		                var d = [];
   	 		                var len = 0;
   	 		                while (len < json1.length) {
   	 		                    d.push([json1[len].upload_time-0,json1[len].bytes_sent-0]);
   	 		                       len++;
   	 		                }
   	 		                return d;
   	 		            })()
   	 		        }
   	 		    ]
   	 		};
    }
    myChart6.setOption(option6); 
}
//��ǩҳ����л�
function winNavigate(uniformid)
{
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="curendtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="endtab_bbms";
			document.all("td5").className="endtab_bbms";
			document.all("td6").className="endtab_bbms";
			$("#charts1").show();
			$("#charts2").hide();
			$("#charts3").hide();
			$("#charts4").hide();
			$("#charts5").hide();
			$("#charts6").hide();
			$("#charts7").hide();
			$("#charts8").hide();
			$("#charts9").hide();
			$("#charts10").hide();
			break;
		}
		case 2:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="curendtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="endtab_bbms";
			document.all("td5").className="endtab_bbms";
			document.all("td6").className="endtab_bbms";
			$("#charts1").hide();
			$("#charts2").show();
			$("#charts3").hide();
			$("#charts4").hide();
			$("#charts5").hide();
			$("#charts6").hide();
			$("#charts7").hide();
			$("#charts8").hide();
			$("#charts9").hide();
			$("#charts10").hide();
			break;
		}
		
		case 3:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="curendtab_bbms";
			document.all("td4").className="endtab_bbms";
			document.all("td5").className="endtab_bbms";
			document.all("td6").className="endtab_bbms";
			$("#charts1").hide();
			$("#charts2").hide();
			$("#charts3").show();
			$("#charts4").hide();
			$("#charts5").hide();
			$("#charts6").hide();
			$("#charts7").hide();
			$("#charts8").hide();
			$("#charts9").hide();
			$("#charts10").hide();
			break;
		}
		case 4:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="curendtab_bbms";
			document.all("td5").className="endtab_bbms";
			document.all("td6").className="endtab_bbms";
			$("#charts1").hide();
			$("#charts2").hide();
			$("#charts3").hide();
			$("#charts4").show();
			$("#charts5").hide();
			$("#charts6").hide();
			$("#charts7").hide();
			$("#charts8").hide();
			$("#charts9").hide();
			$("#charts10").hide();
			break;
		}
		case 5:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="endtab_bbms";
			document.all("td5").className="curendtab_bbms";
			document.all("td6").className="endtab_bbms";
			$("#charts1").hide();
			$("#charts2").hide();
			$("#charts3").hide();
			$("#charts4").hide();
			$("#charts5").show();
			$("#charts6").hide();
			$("#charts7").hide();
			$("#charts8").hide();
			$("#charts9").hide();
			$("#charts10").hide();
			break;
		}
		case 6:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="endtab_bbms";
			document.all("td5").className="endtab_bbms";
			document.all("td6").className="curendtab_bbms";
			$("#charts1").hide();
			$("#charts2").hide();
			$("#charts3").hide();
			$("#charts4").hide();
			$("#charts5").hide();
			$("#charts6").show();
			$("#charts7").show();
			$("#charts8").show();
			$("#charts9").show();
			$("#charts10").show();
			break;
		}
	}
}
</script>
</head>

<body oncontextmenu=self.event.returnValue=false>
<form name="form1"> 
   <table cellspacing="0" cellpadding="0" width="98%" border="0"> 
    <tbody>
    <TR>
		<TD HEIGHT=18>&nbsp;</TD>
	</TR>
     
     <tr> 
      <td> 
       <table class="text" cellspacing="0" cellpadding="0" width="100%" align="center" border="0"> 
        <tbody> 
         <tr> 
          <td> 
           <table class="green_gargtd" height="30" cellspacing="0" cellpadding="0" width="100%" border="0"> 
            <tbody> 
              <tr> 
                <td class="title_bigwhite" width="162" nowrap="" align="center">�û�״̬��Ϣ��ѯ </td> 
                <td nowrap=""><img src="../../images/attention_2.gif" width="15" height="12" /> </td>
              </tr>
            </tbody>
           </table>
          </td>
         </tr>
        </tbody>
       </table>
      </td>
     </tr> 
     
     
     <tr> 
      <td> 
       <table id="deviceResult" cellspacing="0" cellpadding="0" width="100%" align="center" border="0" valign="middle"> 
        <tbody> 
         <tr> 
          <th colspan="4" align="center">�û���ѯ </th>
         </tr> 
         <tr bgcolor="#ffffff"> 
          <td colspan="4"> 
           <div id="selectDevice"> 
            <link rel="stylesheet" type="text/css" href="../css/css_green.css" />
            <table cellspacing="0" cellpadding="0" width="100%" align="center" border="0"> 
             <tbody> 
              <tr> 
               <td bgcolor="#999999"> 
                <table cellspacing="1" cellpadding="2" width="100%" border="0"> 
                 <tbody> 
                  <tr bgcolor="#ffffff"> 
                   <td width="15%" align="right">��ѯ��ʽ</td> 
                   <td width="85%" colspan="3" align="left">
                   <label><input onclick="deviceSelect_ShowDialog(this.value)" checked="" type="radio" value="2" name="checkType" />���豸&nbsp;</label> 
                   <label><input onclick="deviceSelect_ShowDialog(this.value)" type="radio" value="1" name="checkType" />���û�</label>
                   </td>
                  </tr> 
                  <tr id="tr21" style="DISPLAY: none" bgcolor="#ffffff"> 
                   <td width="15%" align="right">LOID(������ʺ�)</td> 
                   <td width="35%" align="left"><input class="bk" name="loid" /><font color="red">&nbsp;*</font></td> 
                   <td width="15%" align="right"></td> 
                   <td width="35%"><input id="loidButton" onclick="searchByLoid()" class="jianbian" type="button" value=" ��  ѯ " /></td>
                  </tr> 
                  <tr id="tr31" bgcolor="#ffffff"> 
                   <td width="15%" align="right">�� �� SN</td> 
                   <td width="35%" align="left"><input class="bk" value="" name="device_serialnumber" /> <font color="red">*�����������λ</font> </td> 
                   <td width="15%" align="right"></td> 
                   <td width="35%"> <input id="snButton" onclick="searchByDevSn()" class="jianbian" type="button" value=" ��  ѯ " /></td>
                  </tr>
                 </tbody>
                </table>
               </td>
              </tr>
             </tbody>
            </table>
           </div>
           
<div id="userInfo">
    <TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
		<tr id=basicInfo>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=1 width="100%" >
					<tr align="left">
						<td colspan="4" class="green_title_left" style="text-align: center;">�û�������Ϣ</td>
					</tr>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">�û�����:</TD>
							
								<TD id="linkman" width="35%"></TD>
							<TD class=column align="right" width="15%">�û�סַ:</TD>
							
								<TD id="linkaddress" width="35%"></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">���֤����:</TD>
							
								<TD id="credno" width="35%"></TD>
							<TD class=column align="right" width="15%">��ϵ�绰:</TD>
							
								<TD id="linkphone" width="35%"></TD>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����˺�:</TD>
							<TD id="kdname" width="35%"></TD>
							<TD class=column align="right" width="15%">ǩԼ����:</TD>
							<TD id="bandwidth" width="35%"></TD>
						</TR>
						<TR bgcolor="#FFFFFF"><!-- �����߼�ID������/�̿� -->
							<TD class=column align="right" nowrap width="15%">�߼�ID:</TD>
							<TD id="username" width="35%"></TD>
							<TD class=column align="right" width="15%">����/�̿�:</TD>
							<TD id="isGoverment" width="35%">
							</TD>
						</TR>
					<tr align="left">
						<td colspan="4" class="green_title_left" style="text-align: center;">�ն˻�����Ϣ</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�ն˳���:</TD>
						<TD id="vendor_add" width="35%"></TD>
						<TD class=column align="right" width="15%">�ն��ͺ�:</TD>
						<TD id="device_model" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�ն�Ӳ���汾:</TD>
						<TD id="hardwareversion" width="35%"></TD>
						<TD class=column align="right" width="15%">����汾:</TD>
						<TD id="softwareversion" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�ն�ע��״̬:</TD>
						<TD id="register" width="35%"></TD>
						<TD class=column align="right" width="15%">IP��ַ:</TD>
						<TD id="loopback_ip" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�ն����к�:</TD>
						<TD id="device_serialnumber" width="35%"></TD>
						<TD class=column align="right" width="15%">�豸�����ӿ�:</TD>
						<TD id="access_type" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�豸����:</TD>
						<TD id="device_type" width="35%"></TD>
						<TD class=column align="right" width="15%">�Ƿ����������:</TD>
						<TD id="is_card_apart" width="35%"></TD>
					</TR><TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�û�IP��ַ����:</TD>
						<TD id="ip_model_type" width="35%" colspan="3"></TD>
					</TR>
					<tr align="left">
						<td colspan="4" class="green_title_left" style="text-align: center;">�豸״̬��Ϣ</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">PON״̬:</TD>
						<TD id="status" width="35%"></TD>
						<TD class=column align="right" width="15%">������ʱ��:</TD>
						<TD id="add_time" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">PON�ڷ���⹦��(dbm):</TD>
						<TD id="tx_power" width="35%"></TD>
						<TD class=column align="right" width="15%">PON�ڽ��չ⹦��(dbm):</TD>
						<TD id="rx_power" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�¶�(��):</TD>
						<TD id="temperature" width="35%"></TD>
						<TD class=column align="right" width="15%">��ѹ(��):</TD>
						<TD id="vottage" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">����(����):</TD>
						<TD id="bais_current" width="35%"></TD>
						<TD class=column align="right" width="15%"></TD>
						<TD width="35%">
						</TD>
					</TR>
					
					<tr align="left">
						<td colspan="4" class="green_title_left" style="text-align: center;">���ҵ��״̬��Ϣ</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">����״̬:</TD>
						<TD id="netStatus" width="35%"></TD>
						<TD class=column align="right" width="15%">������Ϣ:</TD>
						<TD id="reason" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%"></TD>
						<TD width="35%" style="color: red">PPPOE����</TD>
						<TD class=column align="right" width="15%"></TD>
						<TD width="35%"></TD>
					</TR>
					
					<tr align="center">
						<td colspan="4" class="green_title_left" style="text-align: center;">����ҵ��״̬��Ϣ</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�����˿�1״̬</TD>
						<TD id="enabledLine1" width="35%"></TD>
						<TD class=column align="right" width="15%">ע��״̬</TD>
						<TD id="reasonLine1" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">�����˿�2״̬</TD>
						<TD id="enabledLine2" width="35%"></TD>
						<TD class=column align="right" width="15%">ע��״̬</TD>
						<TD id="reasonLine2" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%"></TD>
						<TD width="35%" style="color: red">����ע�Ღ��</TD>
						<TD class=column align="right" width="15%"></TD>
						<TD width="35%"></TD>
					</TR>
					
					<tr align="left">
						<td colspan="4" class="green_title_left" style="text-align: center;">HTTP����������Ϣ</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">���һ�β�������(kb/s):</TD>
						<TD id="downPert" width="35%"></TD>
						<TD class=column align="right" width="15%">����ʱ��:</TD>
						<TD id="test_time" width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%"></TD>
						<TD width="35%" style="color: red">HTTP���ز���</TD>
						<TD class=column align="right" width="15%"></TD>
						<TD width="35%"></TD>
					</TR>
					
					<tr align="center">
						<td colspan="4" class="green_title_left" style="text-align: center;">lan��״̬��Ϣ</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%"  style="color: red;">lan1��״̬</TD>
						<TD width="35%" id="lan1Status" style="color: red;"></TD>
						<TD class=column align="right" width="15%"></TD>
						<TD width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">lan1�ڷ����ֽ���(M)</TD>
						<TD width="35%" id="lan1Sent"></TD>
						<TD class=column align="right" width="15%">lan1�ڽ����ֽ���(M)</TD>
						<TD width="35%" id="lan1Receive"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%"  style="color: red;">lan2��״̬</TD>
						<TD width="35%" style="color: red;" id="lan2Status"></TD>
						<TD class=column align="right" width="15%"></TD>
						<TD width="35%"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">lan2�ڷ����ֽ���(M)</TD>
						<TD width="35%" id="lan2Sent" ></TD>
						<TD class=column align="right" width="15%">lan2�ڽ����ֽ���(M)</TD>
						<TD width="35%" id="lan2Receive" ></TD>
					</TR>
					<TR id="lan3status" bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%"  style="color: red;">lan3��״̬</TD>
						<TD width="35%" style="color: red;" id="lan3Sta" ></TD>
						<TD class=column align="right" width="15%"></TD>
						<TD width="35%"></TD>
					</TR>
					<TR id="lan3byte" bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">lan3�ڷ����ֽ���(M)</TD>
						<TD width="35%" id="lan3Sent" ></TD>
						<TD class=column align="right" width="15%">lan3�ڽ����ֽ���(M)</TD>
						<TD width="35%" id="lan3Receive" ></TD>
					</TR>
					<TR id="lan4status" bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%"  style="color: red;">lan4��״̬</TD>
						<TD width="35%" style="color: red;" id="lan4Sta" ></TD>
						<TD class=column align="right" width="15%"></TD>
						<TD width="35%"></TD>
					</TR>
					<TR id="lan4byte" bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">lan4�ڷ����ֽ���(M)</TD>
						<TD width="35%" id="lan4Sent"></TD>
						<TD class=column align="right" width="15%">lan4�ڽ����ֽ���(M)</TD>
						<TD width="35%" id="lan4Receive" ></TD>
					</TR>
				</TABLE>
				
			</td>
		</tr>
    </TABLE>

		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
			<TR id="tr2">
		  		<TD>
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle">
						<TR>
							<TD>
								<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
									<TR width="100%" height=30>
										<TH class="curendtab_bbms" id="td1" width="17%"><a class="tab_A" href="javascript:winNavigate(1);">PON�ڷ���⹦��</a></TH>
										<TH class="endtab_bbms" id="td2" width="17%"><a class="tab_A" href="javascript:winNavigate(2);">PON�ڽ��չ⹦��</a></TH>
										<TH class="endtab_bbms" id="td3" width="17%"><a class="tab_A" href="javascript:winNavigate(3);">��ģ���¶�</a></TH>
										<TH class="endtab_bbms" id="td4" width="17%"><a class="tab_A" href="javascript:winNavigate(4);">��ģ�鹩���ѹ</a></TH>
										<TH class="endtab_bbms" id="td5" width="17%"><a class="tab_A" href="javascript:winNavigate(5);">��ģ��ƫ�õ���</a></TH>
										<TH class="endtab_bbms" id="td6" width="15%"><a class="tab_A" href="javascript:winNavigate(6);">�˿�����</a></TH> 
									</TR>
								</TABLE>
		 					</TD>
						</TR>
						
					</TABLE>
				</TD>
			</TR>
		 </TABLE> 
		<div id="charts">
			<div id="charts1" style="height:400px"></div>
			<div id="charts2" style="height:400px"></div>
			<div id="charts3" style="height:400px"></div>
			<div id="charts4" style="height:400px"></div>
			<div id="charts5" style="height:400px"></div>
			<div id="charts6" style="height:400px"></div>
		    <div id="portChart">
			   <div id="charts7" style="height:400px"></div>
			   <div id="charts8" style="height:400px"></div>
		    </div>
		</div>
</div>  
<div id="result"></div>      
           </td>
         </tr>
        </tbody>
       </table>
       </td>
     </tr>
     
     
    </tbody>
   </table>
  </form>
</body>
</html>
