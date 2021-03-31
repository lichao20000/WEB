// JavaScript Document

$(document).ready(function($){	
	//交换场景第一部分选项卡切换
	$("#main1_tab").tabso({
		cntSelect:"#main1_content",
		tabEvent:"click",
		tabStyle:"normal"
	});	
	//第二部分选项卡切换
	$("#main2_tab").tabso({
		cntSelect:"#main2_content",
		tabEvent:"click",
		tabStyle:"normal"
	});	
	//交换场景第三部分选项卡切换
	$("#main3_tab").tabso({
		cntSelect:"#main3_content",
		tabEvent:"click",
		tabStyle:"normal"
	});		
	//交换场景第四部分选项卡切换
	$("#main4_tab").tabso({
		cntSelect:"#main4_content",
		tabEvent:"click",
		tabStyle:"normal"
	});	
	//交换场景弹出框内选项卡切换
	$("#dialog_tab").tabso({
		cntSelect:"#dialog_tabcontent",
		tabEvent:"click",
		tabStyle:"normal"
	});	
	//数据场景资源分析模块切换
	$("#main5_tab").tabso({
		cntSelect:"#main5_content",
		tabEvent:"click",
		tabStyle:"normal"
	});	
	//数据场景资源分析模块图表切换
	$("#chatTab").tabso({
		cntSelect:"#chatTab_content",
		tabEvent:"click",
		tabStyle:"normal"
	});
	//系统管理查询切换
	$("#searchTab").tabso({
		cntSelect:"#searchTab_content",
		tabEvent:"click",
		tabStyle:"normal"
	});	
	//告警模块切换
	$("#warnTab").tabso({
		cntSelect:"#warnTab_content",
		tabEvent:"click",
		tabStyle:"move-animate",
		direction : "left"
	});	
	//类型统计切换
	$("#warn1chatTab").tabso({
		cntSelect:"#warn1chatTab_content",
		tabEvent:"click",
		tabStyle:"normal"
	});	
	//告警切换切换
	$("#warn2chatTab").tabso({
		cntSelect:"#warn2chatTab_content",
		tabEvent:"click",
		tabStyle:"normal"
	});	
    //ITMS切换
	$("#itmsMain1tab").tabso({
		cntSelect:"#itmsMain1tab_content",
		tabEvent:"click",
		tabStyle:"normal"
	});						
});


//KPI指标图表左右切换
var Speed_1 = 10; //速度(毫秒)
var Space_1 = 20; //每次移动(px)
var PageWidth_1 = 163 * 5; //翻页宽度
var interval_1 = 5000; //翻页间隔时间
var fill_1 = 0; //整体移位
var MoveLock_1 = false;
var MoveTimeObj_1;
var MoveWay_1="right";
var Comp_1 = 0;
var AutoPlayObj_1=null;
function GetObj(objName){if(document.getElementById){return eval('document.getElementById("'+objName+'")')}else{return eval('document.all.'+objName)}}
function AutoPlay_1(){clearInterval(AutoPlayObj_1);AutoPlayObj_1=setInterval('ISL_GoDown_1();ISL_StopDown_1();',interval_1)}
function ISL_GoUp_1(){if(MoveLock_1)return;clearInterval(AutoPlayObj_1);MoveLock_1=true;MoveWay_1="left";MoveTimeObj_1=setInterval('ISL_ScrUp_1();',Speed_1);}
function ISL_StopUp_1(){if(MoveWay_1 == "right"){return};clearInterval(MoveTimeObj_1);if((GetObj('ISL_Cont_1').scrollLeft-fill_1)%PageWidth_1!=0){Comp_1=fill_1-(GetObj('ISL_Cont_1').scrollLeft%PageWidth_1);CompScr_1()}else{MoveLock_1=false}
AutoPlay_1()}
function ISL_ScrUp_1(){if(GetObj('ISL_Cont_1').scrollLeft<=0){GetObj('ISL_Cont_1').scrollLeft=GetObj('ISL_Cont_1').scrollLeft+GetObj('List1_1').offsetWidth}
GetObj('ISL_Cont_1').scrollLeft-=Space_1}
function ISL_GoDown_1(){clearInterval(MoveTimeObj_1);if(MoveLock_1)return;clearInterval(AutoPlayObj_1);MoveLock_1=true;MoveWay_1="right";ISL_ScrDown_1();MoveTimeObj_1=setInterval('ISL_ScrDown_1()',Speed_1)}
function ISL_StopDown_1(){if(MoveWay_1 == "left"){return};clearInterval(MoveTimeObj_1);if(GetObj('ISL_Cont_1').scrollLeft%PageWidth_1-(fill_1>=0?fill_1:fill_1+1)!=0){Comp_1=PageWidth_1-GetObj('ISL_Cont_1').scrollLeft%PageWidth_1+fill_1;CompScr_1()}else{MoveLock_1=false}
AutoPlay_1()}
function ISL_ScrDown_1(){if(GetObj('ISL_Cont_1').scrollLeft>=GetObj('List1_1').scrollWidth){GetObj('ISL_Cont_1').scrollLeft=GetObj('ISL_Cont_1').scrollLeft-GetObj('List1_1').scrollWidth}
GetObj('ISL_Cont_1').scrollLeft+=Space_1}
function CompScr_1(){if(Comp_1==0){MoveLock_1=false;return}
var num,TempSpeed=Speed_1,TempSpace=Space_1;if(Math.abs(Comp_1)<PageWidth_1/2){TempSpace=Math.round(Math.abs(Comp_1/Space_1));if(TempSpace<1){TempSpace=1}}
if(Comp_1<0){if(Comp_1<-TempSpace){Comp_1+=TempSpace;num=TempSpace}else{num=-Comp_1;Comp_1=0}
GetObj('ISL_Cont_1').scrollLeft-=num;setTimeout('CompScr_1()',TempSpeed)}else{if(Comp_1>TempSpace){Comp_1-=TempSpace;num=TempSpace}else{num=Comp_1;Comp_1=0}
GetObj('ISL_Cont_1').scrollLeft+=num;setTimeout('CompScr_1()',TempSpeed)}}
function picrun_ini(){
GetObj("List2_1").innerHTML=GetObj("List1_1").innerHTML;
GetObj('ISL_Cont_1').scrollLeft=fill_1>=0?fill_1:GetObj('List1_1').scrollWidth-Math.abs(fill_1);
GetObj("ISL_Cont_1").onmouseover=function(){clearInterval(AutoPlayObj_1)}
GetObj("ISL_Cont_1").onmouseout=function(){AutoPlay_1()}
AutoPlay_1();
}

//网元列表——更多选项显示与隐藏
$(function(){ 
	var product=$("#search_table");
	product.hide();
	$("#search_more").click(function(){
		if(product.is(":hidden")==true)
		{
			product.show();
			$(this).addClass("main4Search_more_down");
		}
		else
		{
			product.hide();
			$(this).removeClass("main4Search_more_down");
			$(this).addClass("main4Search_more");
		}		
	})
})

//弹出框
$(function(){ 
	var product=$("#ui_dialog");
	product.hide();
	$(".piechatBox").click(function(){$("#ui_dialog").fadeIn(400)})
	$('#dialog_close').click(function (event) { $("#ui_dialog").fadeOut(400) }); 
})




