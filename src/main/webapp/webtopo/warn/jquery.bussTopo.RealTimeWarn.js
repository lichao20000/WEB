    /**
     **专为实时告警牌使用
     */
jQuery.extend({
    //手动刷新和自动刷新切换
    showRef:function(v){
        if(v=="0"){ //自动
          $("#autoref").show();
          $("#handref").hide();
       }else{
          $("#autoref").hide();
          $("#handref").show();
       }
    },
	//告警数量统计
	StatWarnNum:function(){
		//$.SynOperator(function(){
		var lev0 = $("tr[@lev='0']").length;
		var lev1 = $("tr[@lev='1']").length;
		var lev2 = $("tr[@lev='2']").length;
		var lev3 = $("tr[@lev='3']").length;
		var lev4 = $("tr[@lev='4']").length;
		var lev5 = $("tr[@lev='5']").length;

		$("label[@name='lev0_num']").html(lev0 == 0 ? "0":lev0);
		$("label[@name='lev1_num']").html(lev1 == 0 ? "0":lev1);
		$("label[@name='lev2_num']").html(lev2 == 0 ? "0":lev2);
		$("label[@name='lev3_num']").html(lev3 == 0 ? "0":lev3);
		$("label[@name='lev4_num']").html(lev4 == 0 ? "0":lev4);
		$("label[@name='lev5_num']").html(lev5 == 0 ? "0":lev5);
		//用lev0记录告警总数
		lev0 = (lev0 + lev1 + lev2 + lev3 + lev4 + lev5);
		$("label[@name='total']").html(lev0 == 0 ? "0":lev0);

		lev0 = null;lev1 = null;lev2 = null;
		lev3 = null;lev4 = null;lev5 = null;
		//});
	},
    //显示时间
    showTime:function(){
        $("#last_reftime").html($.getTime());
    },
	//告警声音转向URL:把王之猛的方法copy过来的
	download:function(url,param)
	{
		if(typeof param=='string')
		{
			url = url+"?"+param;
		}
		else if(typeof param=='object')
		{
			url =url+"?";
			for(var tmp in param)
			{
				url+=tmp+"="+param[tmp]+"&";
			}
			url=url.substr(0,url.length-1);
		}
		var df = $("#lk_download_file").size();
		if(df==0)
		{
			$("body").append("<iframe id = 'lk_download_file' style='display:none;'></iframe>");
			$("#lk_download_file").attr("src",url);
		}
		else
		{
			$("#lk_download_file").attr("src",url);
		}
	},
	//获取当前时间
	getTime:function(){
		var date=new Date();
        var y=date.getYear();
        var m=date.getMonth()+1;
        var d=date.getDate();
        var h=date.getHours();
        var M=date.getMinutes();
        var s=date.getSeconds();
		time=y+"-"+m+"-"+d+" "+h+":"+M+":"+s;
		//释放变量
		date=null;
		y=null;m=null;d=null;h=null;M=null;s=null;
		return time;
	},
	//根据状态提示
	loading:function(type){
		if(type==0)
		{
			$("#msg").html("<img src='../images/loading.gif'/>正在载入数据，请等待......");
		}else if(type==1){
			$("#msg").html("<img src='../images/loading.gif'/>正在更新数据，请等待......");
		}else if(type==2){
			$("#msg").html("<img src='../images/loading.gif'/>数据获取完毕，正在更新页面，请等待......");
		}else if(type==3){
			$("#msg").html("");
		}
	},
	//获取恢复告警
	getRecWarn:function(){
		$.ajaxTimeout(10000);
		$.ajax({
			type:"post",
			url:"RealTimeWarn!getClearWarnData.action",
			data:"gather_val="+$("input[@name='gather']").val()+"&ruleid="+$("input[@name='rule']").val(),
			success:function(data){
				if(data!=""){
					var f=false;
					data=eval("("+data+")");
					tmp_id=data.id.split("-/-");
					n=tmp_id.length;
					var target;
					for(var i=0;i<n;i++){
						target=$("#"+tmp_id[i]);
						if($(target).length>0){
							f=true;
							target.children("[@name='atime']").html(data[id].t);
							target.children("[@name='lev']").html("已清除");
							target.children("[@name='astu']").html("已确认");
							target.attr("class","level_0");
							target.attr("lev","0");
							target.find("div").html(data[id].s);
						}
						
					}
					if(f) {
						$.StatWarnNum();//统计告警
						//$("#data").trigger("update");
						update_flg=false;
						if(debug) $("#test ul").prepend("<li><<<<<<<<<<更新恢复告警>>>>>>>>>>>></li>");
					}
					//释放变量
					data=null;
					tmp_id=null;
					n=null;id=null;
				}
			},
			error:function(e){

			}
		});
	},
	//刷新数据
	RefData:function(){
		var t=new Date().getTime();
		if(debug) $("#test ul").prepend("<li>//////////////////////////</li>");
		if(debug) $("#test ul").prepend("<li>开始刷新数据************"+(new Date().getTime()-t)+"</li>");
		t= new Date().getTime();
		$.RightMenuHide();
		$.RecorverCss();
		$.RemoveCSS();
		$.loading(1);
		ClearWarnInterval();
		var n0=eval($("label[@name='lev0_num']").html());//自动清除
		var n1=eval($("label[@name='lev1_num']").html());//正常日志
		var n2=eval($("label[@name='lev2_num']").html());//提示告警
		var n3=eval($("label[@name='lev3_num']").html());//一般告警
		var n4=eval($("label[@name='lev4_num']").html());//严重告警
		var n5=eval($("label[@name='lev5_num']").html());//紧急告警
		var _total = n0 + n1 + n2 + n3 + n4 + n5;

		if(debug) 	$("#test ul").prepend("<li>开始接受数据************"+(new Date().getTime()-t)+"</li>");
		t= new Date().getTime();
		//$.ajaxTimeout(20000);
		$.ajax({
			type:"post",
			url:"RealTimeWarn!getupdateData.action",
			timeout:20000,
			data:"ruleid="+$("input[@name='rule']").val()+"&fetchCount="+$("input[@name='max']").val()+"&columnID="+$("input[@name='columnID']").val()+"&gather_val="+$("input[@name='gather']").val(),
			success:function(s){
				if(debug)  $("#test ul").prepend("<li>取得数据结束************"+(new Date().getTime()-t)+"</li>");
				t= new Date().getTime();
			    $.loading(2);
				var tmp = eval("("+s+")");
				var data=$(tmp.data);
				var lev_0=eval(tmp.lev_0);
				var lev_1=eval(tmp.lev_1);
				var lev_2=eval(tmp.lev_2);
				var lev_3=eval(tmp.lev_3);
				var lev_4=eval(tmp.lev_4);
				var lev_5=eval(tmp.lev_5);
				var get_num=lev_0+lev_1+lev_2+lev_3+lev_4+lev_5;//当前取得告警数据总数
				var need_num = $("input[@name='max']").val()- _total;//规则配置每页显示最大条数-当前页面总共数据总数
				var num = get_num - need_num;//超出部分
				//alert("取得数据总数"+num+" 差额:"+need_num);
				$("input[@name='gather']").val(tmp.gather);
				if(debug) $("#test ul").prepend("<li>开始计算************"+(new Date().getTime()-t)+"</li>");
				t = new Date().getTime();
				//申明临时数组，用于存放tr对象
				var array = null;				
				if(get_num > 0){//有数据的情况下
					if(debug) $("#test ul").prepend("<li>取得数据总数************"+get_num+"</li>");
					if(get_num <= need_num){//得到的数据小于差额数据:全部添加
					}else if(num<n0){//超出部分数据小于自动清除的数目
						array=$.getArray("0");
						$.RemoveData(array,num,"0");
					}else if(num<n1){//超出部分数据小于正常日志和提示告警的数目
						array=$.getArray("1");
						$("tr[@lev='0']").remove();
						$.RemoveData(array,(num-n0),"1");
					}else if((num-n0-n1)<n2){//超出部分数据小于一般告警
						array=$.getArray("2");
						$("tr[@lev='0']").remove();
						$("tr[@lev='1']").remove();
						$.RemoveData(array,(num-n0-n1),"2");
					}else if((num-n0-n1-n2)<n3){//超出部分数据小于严重告警
						array=$.getArray("3");
						$("tr[@lev='0']").remove();
						$("tr[@lev='1']").remove();
						$("tr[@lev='2']").remove();
						$.RemoveData(array,(num-n0-n1-n2),"3");
					}else if((num-n0-n1-n2-n3)<n4){//超出部分数据小于紧急告警
						array=$.getArray("4");
						$("tr[@lev='0']").remove();
						$("tr[@lev='1']").remove();
						$("tr[@lev='2']").remove();
						$("tr[@lev='3']").remove();
						$.RemoveData(array,(num-n0-n1-n2-n3),"4");
					}else if((num-n0-n1-n2-n3-n4)<n5){
						array=$.getArray("5");
						$("tr[@lev='0']").remove();
						$("tr[@lev='1']").remove();
						$("tr[@lev='2']").remove();
						$("tr[@lev='3']").remove();
						$("tr[@lev='4']").remove();
						$.RemoveData(array,(num-n0-n1-n2-n3-n4),"5");
					}else{
						$("tr[@lev='0']").remove();
						$("tr[@lev='1']").remove();
						$("tr[@lev='2']").remove();
						$("tr[@lev='3']").remove();
						$("tr[@lev='4']").remove();
						$("tr[@lev='5']").remove();
					}

					if(debug) $("#test ul").prepend("<li>数据处理结束************"+(new Date().getTime()-t)+"</li>");
					t= new Date().getTime();
					$("#tbody").prepend(data);
					$.ChangeStyle(get_num);
					$.MouseEvent($(data));
					//$("#data").trigger("update");
					update_flg=false;
					$.StatWarnNum();//统计告警条数
					if(debug) 	$("#test ul").prepend("<li>数据加载结束************"+(new Date().getTime()-t)+"</li>");
					t= new Date().getTime();
					alarmlevel=lev_5>0?5:lev_4>0?4:lev_3>0?3:lev_2>0?2:lev_1>0?1:0;
					play();
					
					//释放变量
					tmp=null;
					delete tmp;
					data=null;
					delete data;
					lev_0=null;lev_1=null;lev_2=null;lev_3=null;lev_4=null;lev_5=null;

					//销毁array数组
					if(array != null){array.length = 0;delete array;array = null;}
				}
				
				$.showTime();
				if($("input[@name='ref'][@checked]").val()==0){
					ReSetRefTime();
					ShowTimeRef();
				}
				getRefWarn();
				$.RecoverMEM();
				$.loading(3);
				if(timeout>0){
					$("#timeout").html("");
					timeout--;
				} 
				if(debug) 	$("#test ul").prepend("<li>数据处理完成************"+(new Date().getTime()-t)+"</li>");
			},
			error:function(xmlR,msg,other){
				if(debug) 	$("#test ul").prepend("<li>//\\\\\\\超时\\\\\\\\//"+(new Date().getTime()-t)+" "+timeout+"</li>");
				timeout++;
				$("#timeout").html("发生超时"+timeout+"次");
				if(timeout>=7 & timeout<=9){
					OutTimeVoice();
				}
				else if(timeout>=10){
					ReloadWEB();//退出
				}
				$.showTime();
				if($("input[@name='ref'][@checked]").val()==0){
					ReSetRefTime();
					ShowTimeRef();
				}
				getRefWarn();
				$.RecoverMEM();
				$.loading(3);
			}
		});

	},
	//鼠标动作
	MouseEvent:function(target){
		$.SynOperator(function(){
			//鼠标单击
			target.click(function(){
				if(event.ctrlKey){
					startindex=$(this).attr("rowIndex");
					$.AddJsonVal($(this).attr("id"),$(this).attr("class"));
					$(this).attr("class","click");
				}else if(event.shiftKey){
					endindex=$(this).attr("rowIndex");
					startindex=startindex==""?endindex:startindex;
					$.SelByShift(Number(startindex),Number(endindex),$(this).attr("class"));
					startindex=endindex;
				}else{
					startindex=$(this).attr("rowIndex");
					$.RecorverCss();
					$.AddJsonVal($(this).attr("id"),$(this).attr("class"));
					$(this).attr("class","click");
				}
				$.RightMenuHide();
			});
			//鼠标悬停
			target.bind("mouseover",function(){
				$(this).removeClass("tr_b");
			});
			//鼠标右键
			target.bind("contextmenu",function(){
				if(event.ctrlKey){
					startindex=$(this).attr("rowIndex");
					$.AddJsonVal($(this).attr("id"),$(this).attr("class"));
					$(this).attr("class","click");
				}else if(event.shiftKey){
					endindex=$(this).attr("rowIndex");
					startindex=startindex==""?endindex:startindex;
					$.SelByShift(Number(startindex),Number(endindex),$(this).attr("class"));
					startindex=endindex;
				}else{
					startindex=$(this).attr("rowIndex");
					$.RecorverCss();
					$.AddJsonVal($(this).attr("id"),$(this).attr("class"));
					$(this).attr("class","click");
				}
				//增加右键菜单
				$.ShowMenu();
				return false;
			});
			//双击事件
			target.dblclick(function(){
				var tmp=$(this).attr("id").split("-");
				var url="../webtopo/warnDetailInfo.action?serialNo="+tmp[0]+"&gatherId="+tmp[1]+"&createTime="+$(this).attr("time");
				
				var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=no,status=0,toolbar=0";
				var l		= (document.body.clientWidth-750)/2;
				var t		= (document.body.clientHeight-600)/2;

				window.open(url,"","left=" + l + ",top=" + t + ",width=700,height=550," + otherpra);
				otherpra = null;
				delete otherpra;
			});
			$.RecoverMEM();
		});
	},
	//获取数据后改变样式
	ChangeStyle:function(num){
		
		/*
		var divobj=null;
		var obj=$("#tbody");
		if(debug) 	$("#test ul").prepend("<li>开始改变样式</li>");
		for(var i=0;i<num;i++){
			divobj=$(obj.rows[i]).find("div[@class='content']");
			divobj.click(function(){
				$(this).toggleClass('content')
			});
		}
		*/
		if(debug) 	$("#test ul").prepend("<li>结束样式</li>");
	},
	//删除样式
	RemoveCSS:function(){
		$("#tbody tr[@class contains tr_b]").removeClass("tr_b");
	},
	//删除数据
	RemoveData:function(array,num,css){
		var sortobj=null;
		for(var i=0;i<num;i++){
			sortobj=array[i];
			//$("#"+sortobj.id).remove();
			sortobj.remove();
		}
	},
	//得到数组
	getArray:function(level){
		var array=[];
		$("tr[@lev='"+level+"']").each(function(i){
			//array[i]=new sortobj($(this).attr("time"),$(this).attr("id"));
			array[i]=$(this);
		});
		array.sort(function(a,b){return eval(a.attr("time"))-eval(b.attr("time"));});
		return array;
	},
	//判断告警声音
	getWarnLevel:function(){
		if(eval($("label[@name='lev5_num']").html())>0){
			return 5;
		}else if(eval($("label[@name='lev4_num']").html())>0){
			return 4;
		}else if(eval($("label[@name='lev3_num']").html())>0){
			return 3;
		}else if(eval($("label[@name='lev2_num']").html())>0){
			return 2;
		}else if(eval($("input[@name='lev1_num']").html())>0){
			return 1;
		}else if(eval($("input[@name='lev0_num']").html())>0){
			return 0;
		}else{
			return -1;
		}
	},
	//显示右键菜单
	ShowMenu:function(){
		createRMenu("-3",-1);
		showRightMenu();
	},
	//定位设备
	findDevObj:function(){
		var open = null;
		try{
			open = ((window.opener == null) ? null : window.opener);
		}catch(e){
			alert("WebTopo窗口已被关闭，无法定位到设备！(" + e.description + ")");
			return false;
		}
		$.SynOperator(function(){
			var tmp=sel_id.split("-/-");
			var dev=$("#"+tmp[tmp.length-2]).attr("devid");
			if(typeof(dev)=="undefined"){
				alert("该设备没有设备ID，无法定位!");
				return;
			}
			if(open != null && typeof(open.findDevObjLocation) == "object"){
				//20091014 何茂才修改 解决实时告警牌上无法定位设备的bug （MC那边编码都是1/gw/device_id）
				open.findDevObjLocation("1/gw/"+dev);
				open.focus();
			}
			else alert("WebTopo窗口已被关闭，无法定位到设备！");
			return;
		});
	},
	//异步方法
	SynOperator:function(fun){
		setTimeout(fun,1);
	},
	//导出EXCEL
	Export:function(){
		$.RemoveCSS();
		$.RecorverCss();
		//initialize(data,0,0);
		$("#rptdataExcel").val($("#table").html());
		$("form").submit();
		$.RightMenuHide();
		$.RecoverMEM();
	},
	//IE内存释放
	RecoverMEM:function(){
		//垃圾回收,只针对IE
		if(typeof(CollectGarbage)=="object"){
			CollectGarbage();
		}
	},
	//确认告警
	ConfigAlarm:function(){
		if(window.confirm("是否要确认告警?")){			
			ClearWarnInterval();
			$.ajax({
				type:"post",
				url:"./RealTimeWarn!ConfigWarn.action",
				data:"alarmid="+sel_devid,
				success:function(data){
					if(data==1){
						var tmp=sel_id.split("-/-");
						var n=tmp.length;
						for(var i=0;i<n-1;i++){
							$("#"+tmp[i]).attr("class","level_0");
							$("#"+tmp[i]).attr("lev","0");
							$("#"+tmp[i]+" td[@name=lev]").html("已经处理");
							$("#"+tmp[i]+" td[@name=astu]").html("手工确认");
							$("#"+tmp[i]+" td[@name=atime]").html($.getTime());
						}
						$.StatWarnNum();//统计告警条数
						sel_id="";
						sel_devid="";
						//$("#data").trigger("update");
						update_flg=false;
						if($("input[@name='ref'][@checked]").val()==0){
							ReSetRefTime();
							ShowTimeRef();
						}
						$.RecoverMEM();
						
					}else{
						alert("确认未成功，请重试");
					}
					getRefWarn();
				},
				error:function(e){
					alert("session超时,请重新登陆!");
					getRefWarn();
				}
			});
		}
		$.RightMenuHide();
	},
	//删除单个告警
	clearAlarm:function(id){
		if(window.confirm("是否要删除告警?")){		
			ClearWarnInterval();
			$.ajax({
				type:"post",
				url:"./RealTimeWarn!DelWarn.action",
				data:"alarmid="+id,
				success:function(data){
					if(data==1){
						$("#"+id).remove();
						update_flg=false;
						if($("input[@name='ref'][@checked]").val()==0){
							ReSetRefTime();
							ShowTimeRef();
						}
						$.StatWarnNum();//统计告警条数
						$.RecoverMEM();
					}else{
						alert("删除未成功，请重试");
					}
					getRefWarn();
				},
				error:function(msg){
					getRefWarn();
					alert(msg);
				}
			});
		}
	},
	//删除告警
	DelAlarm:function(){
		if(window.confirm("是否要清除告警?")){		
			ClearWarnInterval();
			$.ajax({
				type:"post",
				url:"./RealTimeWarn!DelWarn.action",
				data:"alarmid="+sel_devid,
				success:function(data){
					if(data==1){
						var tmp=sel_id.split("-/-");
						var n=tmp.length;
						for(var i=0;i<n-1;i++){
							$("#"+tmp[i]).remove();
						}
						sel_id="";
						sel_devid="";
						//$("#data").trigger("update");
						update_flg=false;
						if($("input[@name='ref'][@checked]").val()==0){
							ReSetRefTime();
							ShowTimeRef();
						}
						$.StatWarnNum();//统计告警条数
						$.RecoverMEM();
						
					}else{
						alert("清除未成功，请重试");
					}
					getRefWarn();
				},
				error:function(msg){
					getRefWarn();
					alert("session超时,请重新登陆!");
				}
			});
			
		}
		$.RightMenuHide();	
	},
	//隐藏右健菜单
	RightMenuHide:function(){
		document_click();
	},
	//按住Shift键操作选中的列
	SelByShift:function(startIndex,endIndex){
		var tmp=startIndex<endIndex?startIndex:endIndex;
		endIndex=startIndex>endIndex?startIndex:endIndex;
		startIndex=tmp;
		var id;
		var dataObj = document.getElementById("data");
		for(var t=startIndex;t<=endIndex;t++){
			id=dataObj.rows[t].id;
			$.AddJsonVal(id,$("#"+id).attr("class"));
			$("#"+id).attr("class","click");
		}
	},
	//恢复table原来的样式
	RecorverCss:function(){
		if(!sel_id==""){
			sel_id=sel_id.substring(0,sel_id.length-3);
			var tmp=sel_id.split("-/-");
			var n=tmp.length;
			for(var i=0;i<n;i++){
				$("#"+sel_json[tmp[i]].id).attr("class",sel_json[tmp[i]].css);
			}
		}
		//清空变量
		endindex="";
		sel_id="";
		sel_devid="";
		sel_json={};
	},
	//添加Json值 暂时使用1/dev如果是业务平台就用6/dev
	AddJsonVal:function(id,cls){
		if(typeof(sel_json[id])=="undefined"){
			sel_json[id]={id:id,css:cls};
			sel_id+=id+"-/-";
			var tmp=$("#"+id).attr("devid");
			tmp=(typeof(tmp)=="undefined"?"":tmp);
			sel_devid+=id+"-1/hgw/"+tmp+"-"+$("#"+id).attr("time")+"-/-";
		}
	},
	//显示隐藏数据
	DisplayStr:function(){
		$("#tbody td").addClass("td_r");
		$("div[@class='content']").bind("click",function(){
			$(this).removeClass("td_r");
			$(this).toggleClass('content')
		});
	},
	//初始化模版选择框
	InitModule:function(){
		var rid=$("input[@name='rule']").val()+"-/-"+$("input[@name='max']").val();
		//没有rule_id
		if(rid=="-/-"){
			$("select[@name='ruleid'] option").each(function(){
				if($(this).attr("sel")==1){
					$(this).attr("selected",true);
				}
			});
			var tmp=($("select[@name='ruleid']").val()).split("-/-");
			$("input[@name='rule']").val(tmp[0]);//需要显示的告警规则ID
			$("input[@name='max']").val(tmp[1]);//每页显示的最大条数
		}else{
			$("select[@name='ruleid'] option").each(function(){
				if($(this).val()==rid){
					$(this).attr("selected",true);
				}
			});
		}
	},
	//DIV显示和隐藏
	divshow:function(target){
		target.toggleClass('content');
	},
	//移除告警 flg:-2:右键选中的告警,-1:所有告警,0:清除告警,1:正常日志,2:提示告警,3:一般告警,4:严重告警,5:紧急告警
	RemoveWarn:function(flg){
		switch(flg){
			case -2://右键选中
				if(window.confirm("是否确定要移除选中的告警?")){
					sel_id=sel_id.substring(0,sel_id.length-3);
					var tmp=sel_id.split("-/-");
					var n=tmp.length;
					for(var i=0;i<n;i++){
						$("#"+sel_json[tmp[i]].id).remove();
					}
					$.StatWarnNum();
					//清空变量
					endindex="";
					sel_id="";
					sel_devid="";
					sel_json={};
					update_flg=false;
				}
				break;
			case -1://所有告警
				if(window.confirm("是否确定要移除选中的告警?")){
					$("#tbody").html("");
					$("label[@name='total']").html("0");
					$("label[@name='lev5_num']").html("0");
					$("label[@name='lev4_num']").html("0");
					$("label[@name='lev3_num']").html("0");
					$("label[@name='lev2_num']").html("0");
					$("label[@name='lev1_num']").html("0");
					$("label[@name='lev0_num']").html("0");
					update_flg=false;
				}
				break;
			case 0://清除告警
				if(window.confirm("是否确定要移除所有清除告警?")){
					$("tr[@lev='0']").remove();
					$("label[@name='lev0_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 1://正常日志
				if(window.confirm("是否确定要移除所有事件告警?")){
					$("tr[@lev='1']").remove();
					$("label[@name='lev1_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 2://提示告警
				if(window.confirm("是否确定要移除所有警告告警?")){
					$("tr[@lev='2']").remove();
					$("label[@name='lev2_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 3://一般告警
				if(window.confirm("是否确定要移除所有次要告警?")){
					$("tr[@lev='3']").remove();
					$("label[@name='lev3_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 4://严重告警
				if(window.confirm("是否确定要移除所有主要告警?")){
					$("tr[@lev='4']").remove();
					$("label[@name='lev4_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 5://紧急告警
				if(window.confirm("是否确定要移除所有严重告警?")){
					$("tr[@lev='5']").remove();
					$("label[@name='lev5_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
		}
	},
	//计算告警总数
	sumtotalnum:function(){
		var total=0;
		total=eval($("label[@name='lev0_num']").html())
			 +eval($("label[@name='lev1_num']").html())
			 +eval($("label[@name='lev2_num']").html())
			 +eval($("label[@name='lev3_num']").html())
			 +eval($("label[@name='lev4_num']").html())
			 +eval($("label[@name='lev5_num']").html());
		$("label[@name='total']").html(total);
	}
});