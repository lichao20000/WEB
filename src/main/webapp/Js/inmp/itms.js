// JavaScript Document

$(document).ready(function($){
	//ITMS菜单
	$("#Inav li:has('ul')").mouseover(function(){
		$(this).children("ul").show();
	});
	$("#Inav li:has('ul')").mouseout(function(){
		$(this).children("ul").hide();
	});	
	//ITMS切换1
	$("#itmsMain1tab").tabso({
		cntSelect:"#itmsMain1tab_content",
		tabEvent:"click",
		tabStyle:"normal"
	});
	
	//获取当前时间
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth(); 
	autoBind(year,month);
	buss(year,month);
	pvc(year,month);
	versionDev(year,month);
	$("#year1").text(year + "年");
	$("#year2").text(year + "年");
	$("#year3").text(year + "年");
	$("#year4").text(year + "年");
	//系统月份默认显示
	var monthobj1 = document.getElementById("month1"); 
	monthSel(monthobj1);
	var monthobj2 = document.getElementById("month2"); 
	monthSel(monthobj2);
	var monthobj3 = document.getElementById("month3"); 
	monthSel(monthobj3);
	var monthobj4 = document.getElementById("month4"); 
	monthSel(monthobj4);
	
	function monthSel(obj){
		for(var i = 0;i < obj.options.length;i++){ 
			var monthVal = obj.options[i].value;
			if(monthVal == month){
				obj.options[i].selected = true;
			}
		}
	}
	//年份触发事件
	$('#preyear1').click(function(){
		$("#year1").text(parseInt($("#year1").text().substr(0,4)) - 1 + "年");
		var year1 = $("#year1").text().substr(0,4);
		var month1 = $("#month1 option:selected").val();
		autoBind(year1,month1);
	});
	$('#preyear2').click(function(){
		$("#year2").text(parseInt($("#year2").text().substr(0,4)) - 1 + "年");
		var year2 = $("#year2").text().substr(0,4);
		var month2 = $("#month2 option:selected").val();
		buss(year2,month2);
	});
	$('#preyear3').click(function(){
		$("#year3").text(parseInt($("#year3").text().substr(0,4)) - 1 + "年");
		var year3 = $("#year3").text().substr(0,4);
		var month3 = $("#month3 option:selected").val();
		pvc(year3,month3);
	});
	$('#preyear4').click(function(){
		$("#year4").text(parseInt($("#year4").text().substr(0,4)) - 1 + "年");
		var year4 = $("#year4").text().substr(0,4);
		var month4 = $("#month4 option:selected").val();
		versionDev(year4,month4);
	});
	function checkTime(curYear,curMonth){
		if(curYear > year || curMonth > month){
			alert("不好意思，您所点击的是未来时间，请重新选择");
			return;
		}
	}
	$('#subyear1').click(function(){
		$("#year1").text(parseInt($("#year1").text().substr(0,4)) + 1 + "年");
		var year1 = $("#year1").text().substr(0,4);
		var month1 = $("#month1 option:selected").val();
		autoBind(year1,month1);
	});
	$('#subyear2').click(function(){
		$("#year2").text(parseInt($("#year2").text().substr(0,4)) + 1 + "年");
		var year2 = $("#year2").text().substr(0,4);
		var month2 = $("#month2 option:selected").val();
		buss(year2,month2);
	});
	$('#subyear3').click(function(){
		$("#year3").text(parseInt($("#year3").text().substr(0,4)) + 1 + "年");
		var year3 = $("#year3").text().substr(0,4);
		var month3 = $("#month3 option:selected").val();
		pvc(year3,month3);
	});
	$('#subyear4').click(function(){
		$("#year4").text(parseInt($("#year4").text().substr(0,4)) + 1 + "年");
		var year4 = $("#year4").text().substr(0,4);
		var month4 = $("#month4 option:selected").val();
		versionDev(year4,month4);
	});
	
	$("#month1").bind('change',function(){
		var monthSelVal1 = $('#month1 option:selected').val();
		var year1 = $("#year1").text().substr(0,4);
		autoBind(year1,monthSelVal1);
	});
	$("#month2").bind('change',function(){
		var monthSelVal2 = $('#month2 option:selected').val();
		var year2 = $("#year2").text().substr(0,4);
		buss(year2,monthSelVal2);
	});
	$("#month3").bind('change',function(){
		var monthSelVal3 = $('#month3 option:selected').val();
		var year3 = $("#year3").text().substr(0,4);
		pvc(year3,monthSelVal3);
	});
	$("#month4").bind('change',function(){
		var monthSelVal4 = $('#month4 option:selected').val();
		var year4 = $("#year4").text().substr(0,4);
		versionDev(year4,monthSelVal4);
	});
	
	
	//年份钻取弹出框
	//年份默认显示
	var yearVal;
	var yearSelObj1 = document.getElementById("yearSel1"); 
	yearSel(yearSelObj1,yearVal);
	var yearVal = $("#year2").text().substr(0,4);
	var yearSelObj2 = document.getElementById("yearSel2"); 
	yearSel(yearSelObj2);
	var yearVal = $("#year3").text().substr(0,4);
	var yearSelObj3 = document.getElementById("yearSel3"); 
	yearSel(yearSelObj3);
	var yearVal = $("#year4").text().substr(0,4);
	var yearSelObj4 = document.getElementById("yearSel4"); 
	yearSel(yearSelObj4);
	function yearSel(obj,yearVal){
		for(var i = 0;i < obj.options.length;i++){ 
			var yearObjVal = obj.options[i].value;
			if(yearObjVal == yearVal){
				obj.options[i].selected = true;
			}
		}
	}
	//用户自动绑定
	$('.close').click(function(){
		$('#autoBind,#autoBindWall').hide();	
	});
	$('#zq_btn1').click(function(){
		$('#autoBind,#autoBindWall,#monthSel1').hide();
		$('#yearSel1').show();
		$("#dateType").attr("value",'1');
		$("#cbx1").attr("checked",false);
		var year = $("#year1").text().substr(0,4);
		var yearSelObj = document.getElementById("yearSel1");
		for(var i = 0;i < yearSelObj.options.length;i++){ 
			var yearObjVal = yearSelObj.options[i].value;
			if(yearObjVal == year){
				yearSelObj1.options[i].selected = true;
			}
		}
		var yearSel1 = $("#year1").text().substr(0,4);
		barAutoBind(yearSel1,"","");
		$('#autoBind,#autoBindWall').show();
	});
	
	$("#yearSel1").bind('change',function(){
		var yearSel1 = $("#yearSel1 option:selected").text().substr(0,4);
		$("#cbx1").attr("checked",false);
		barAutoBind(yearSel1,"","");
	});
	
	//业务一次下发成功率
	$('.close').click(function(){
		$('#buss,#bussWall').hide();	
	});
	$('#zq_btn2').click(function(){
		$('#buss,#bussWall,#monthSel2').hide();
		$('#yearSel2').show();
		$("#dateType").attr("value",'1');
		$("#cbx2").attr("checked",false);
		var year = $("#year2").text().substr(0,4);
		var yearSelObj = document.getElementById("yearSel2");
		for(var i = 0;i < yearSelObj.options.length;i++){ 
			var yearObjVal = yearSelObj.options[i].value;
			if(yearObjVal == year){
				yearSelObj.options[i].selected = true;
			}
		}
		var yearSel2 = $("#year2").text().substr(0,4);
		barBuss(yearSel2,"","");
		$('#buss,#bussWall').show();	
	});
	$("#yearSel2").bind('change',function(){
		var yearSel2 = $("#yearSel2 option:selected").text().substr(0,4);
		$("#cbx2").attr("checked",false);
		barBuss(yearSel2,"","");
	});
	//新用户多PVC部署
	$('.close').click(function(){
		$('#pvc,#pvcWall').hide();	
	});
	$('#zq_btn3').click(function(){
		$('#pvc,#pvcWall,#monthSel3').hide();
		$('#yearSel3').show();
		$("#dateType").attr("value",'1');
		$("#cbx3").attr("checked",false);
		var year = $("#year3").text().substr(0,4);
		var yearSelObj = document.getElementById("yearSel3");
		for(var i = 0;i < yearSelObj.options.length;i++){ 
			var yearObjVal = yearSelObj.options[i].value;
			if(yearObjVal == year){
				yearSelObj.options[i].selected = true;
			}
		}
		var yearSel3 = $("#year3").text().substr(0,4);
		barPvc(yearSel3,"","");
		$('#pvc,#pvcWall').show();
	});
	$("#yearSel3").bind('change',function(){
		var yearSel3 = $("#yearSel3 option:selected").text().substr(0,4);
		$("#cbx3").attr("checked",false);
		barPvc(yearSel3,"","");
	});
	//终端版本规范率
	$('.close').click(function(){
		$('#versionDev,#versionDevWall').hide();	
	});
	$('#zq_btn4').click(function(){
		$('#versionDev,#versionDevWall,#monthSel4').hide();
		$('#yearSel4').show();
		$("#dateType").attr("value",'1');
		$("#cbx4").attr("checked",false);
		var year = $("#year4").text().substr(0,4);
		var yearSelObj = document.getElementById("yearSel4");
		for(var i = 0;i < yearSelObj.options.length;i++){ 
			var yearObjVal = yearSelObj.options[i].value;
			if(yearObjVal == year){
				yearSelObj.options[i].selected = true;
			}
		}
		var yearSel4 = $("#year4").text().substr(0,4);
		barVersionDev(yearSel4,"","");
		$('#versionDev,#versionDevWall').show();	
	});
	$("#yearSel4").bind('change',function(){
		var yearSel4 = $("#yearSel4 option:selected").text().substr(0,4);
		$("#cbx4").attr("checked",false);
		barVersionDev(yearSel4,"","");
	});
	//月份钻取弹出框
	//用户自动绑定
	$('.close').click(function(){
		$('#autoBind,#autoBindWall').hide();	
	});
	$('#zq_month1').click(function(){
		$('#autoBind,#autoBindWall,#yearSel1').hide();
		$('#monthSel1').show();
		$("#dateType").attr("value",'2');
		$("#cbx1").attr("checked",false);
		var year = $("#year1").text().substr(0,4);
		var monthSelObj = document.getElementById("monthSel1");
		for(var i = 0;i < monthSelObj.options.length;i++){ 
			var monthObjVal = monthSelObj.options[i].value;
			if(monthObjVal == month){
				monthSelObj.options[i].selected = true;
			}
		}
		//monthSel(monthobj1);
		var monthSel1 = $("#monthSel1 option:selected").val();
		barAutoBind(year,monthSel1,"");
		$('#autoBind,#autoBindWall').show();
	});
	
	$("#monthSel1").bind('change',function(){
		var yearSel1 = $("#year1").text().substr(0,4);
		var monthSel1 = $('#monthSel1 option:selected').val();
		$("#cbx1").attr("checked",false);
		barAutoBind(yearSel1,monthSel1,"");
	});
	
	//业务一次下发成功率
	$('.close').click(function(){
		$('#buss,#bussWall').hide();	
	});
	$('#zq_month2').click(function(){
		$('#buss,#bussWall,#yearSel2').hide();
		$('#monthSel2').show();
		$("#dateType").attr("value",'2');
		$("#cbx2").attr("checked",false);
		var year = $("#year2").text().substr(0,4);
		var monthSelObj = document.getElementById("monthSel2");
		for(var i = 0;i < monthSelObj.options.length;i++){ 
			var monthObjVal = monthSelObj.options[i].value;
			if(monthObjVal == month){
				monthSelObj.options[i].selected = true;
			}
		}
		//monthSel(monthobj2);
		var monthSel2 = $("#monthSel2 option:selected").val();
		barBuss(year,monthSel2,"");
		$('#buss,#bussWall').show();	
	});
	$("#monthSel2").bind('change',function(){
		var yearSel2 = $("#year2").text().substr(0,4);
		var monthSel2 = $("#monthSel2 option:selected").val();
		$("#cbx2").attr("checked",false);
		barBuss(yearSel2,monthSel2,"");
	});
	//新用户多PVC部署
	$('.close').click(function(){
		$('#pvc,#pvcWall').hide();	
	});
	$('#zq_month3').click(function(){
		$('#pvc,#pvcWall,#yearSel3').hide();
		$('#monthSel3').show();
		$("#dateType").attr("value",'2');
		$("#cbx3").attr("checked",false);
		var year = $("#year3").text().substr(0,4);
		var monthSelObj = document.getElementById("monthSel3");
		for(var i = 0;i < monthSelObj.options.length;i++){ 
			var monthObjVal = monthSelObj.options[i].value;
			if(monthObjVal == month){
				monthSelObj.options[i].selected = true;
			}
		}
		//monthSel(monthobj3);
		var monthSel3 = $("#monthSel3 option:selected").val();
		barPvc(year,monthSel3,"");
		$('#pvc,#pvcWall').show();
	});
	$("#monthSel3").bind('change',function(){
		var yearSel3 = $("#year3").text().substr(0,4);
		var monthSel3 = $("#monthSel3 option:selected").val();
		$("#cbx3").attr("checked",false);
		barPvc(yearSel3,monthSel3,"");
	});
	//终端版本规范率
	$('.close').click(function(){
		$('#versionDev,#versionDevWall').hide();	
	});
	$('#zq_month4').click(function(){
		$('#versionDev,#versionDevWall,#yearSel4').hide();
		$('#monthSel4').show();
		$("#dateType").attr("value",'2');
		$("#cbx4").attr("checked",false);
		var year = $("#year4").text().substr(0,4);
		var monthSelObj = document.getElementById("monthSel4");
		for(var i = 0;i < monthSelObj.options.length;i++){ 
			var monthObjVal = monthSelObj.options[i].value;
			if(monthObjVal == month){
				monthSelObj.options[i].selected = true;
			}
		}
		//monthSel(monthobj4);
		var monthSel4 = $("#monthSel4 option:selected").val();
		barVersionDev(year,monthSel4,"");
		$('#versionDev,#versionDevWall').show();	
	});
	$("#monthSel4").bind('change',function(){
		var yearSel4 = $("#year4").text().substr(0,4);
		var monthSel4 = $("#monthSel4 option:selected").val();
		$("#cbx4").attr("checked",false);
		barVersionDev(yearSel4,monthSel4,"");
	});
	
	//排名触发函数
	//排名构默认不选中
	$("#cbx1").attr("checked",false);
    $("#cbx1").change(function() {
    	var dateType = $("#dateType").val();
    	if(dateType == 1){
    		var yearSel1 = $("#yearSel1 option:selected").text().substr(0,4);
    		var monthSel1 = "";
    	}else{
    		var yearSel1 = year;
    		var monthSel1 = $("#monthSel1 option:selected").val();
    	}
	    if (!$("#cbx1").attr("checked")) {
			var flag = $("#flag").val();
			barAutoBind(yearSel1,monthSel1,"");
			$('#autoBind,#autoBindWall').show();
	    }else if($("#cbx1").attr("checked")){
			var flag = $("#flag").val();
			barAutoBind(yearSel1,monthSel1,flag);
			$('#autoBind,#autoBindWall').show();
	    }
    });

	$("#cbx2").attr("checked",false);
    $("#cbx2").change(function() {
    	var dateType = $("#dateType").val();
    	if(dateType == 1){
    		var yearSel2 = $("#yearSel2 option:selected").text().substr(0,4);
    		var monthSel2 = "";
    	}else{
    		var yearSel2 = year;
    		var monthSel2 = $("#monthSel2 option:selected").val();
    	}
	    if (!$("#cbx2").attr("checked")) {
			var flag = $("#flag").val();
			barBuss(yearSel2,monthSel2,"");
			$('#buss,#bussWall').show();
	    }else if($("#cbx2").attr("checked")){
			var flag = $("#flag").val();
			barBuss(yearSel2,monthSel2,flag);
			$('#buss,#bussWall').show();
	    }
    });

	$("#cbx3").attr("checked",false);
    $("#cbx3").change(function() {
    	var dateType = $("#dateType").val();
    	if(dateType == 1){
    		var yearSel3 = $("#yearSel3 option:selected").text().substr(0,4);
    		var monthSel3 = "";
    	}else{
    		var yearSel3 = year;
    		var monthSel3 = $("#monthSel3 option:selected").val();
    	}
	    if (!$("#cbx3").attr("checked")) {
			var flag = $("#flag").val();
			barPvc(yearSel3,monthSel3,"");
			$('#pvc,#pvcWall').show();
	    }else if($("#cbx3").attr("checked")){
			var flag = $("#flag").val();
			barPvc(yearSel3,monthSel3,flag);
			$('#pvc,#pvcWall').show();
	    }
    });

	 $("#cbx4").attr("checked",false);
	    $("#cbx4").change(function() {
	    	var dateType = $("#dateType").val();
	    	if(dateType == 1){
	    		var yearSel4 = $("#yearSel4 option:selected").text().substr(0,4);
	    		var monthSel4 = "";
	    	}else{
	    		var yearSel4 = year;
	    		var monthSel4 = $("#monthSel4 option:selected").val();
	    	}
		    if (!$("#cbx4").attr("checked")) {
				var flag = $("#flag").val();
				barVersionDev(yearSel4,monthSel4,"");
				$('#versionDev,#versionDevWall').show();
		    }else if($("#cbx4").attr("checked")){
				var flag = $("#flag").val();
				barVersionDev(yearSel4,monthSel4,flag);
				$('#versionDev,#versionDevWall').show();
		    }
	});
});


/*function loadBarAutoBind(){   
	var script = document.createElement("script");   
	script.type = "text/javascript";   
	script.src = "../Js/inmp/echarts/barAutoBind.js";   
	document.body.appendChild(script); 
}*/ 