    /**
     **רΪʵʱ�澯��ʹ��
     */
jQuery.extend({
    //�ֶ�ˢ�º��Զ�ˢ���л�
    showRef:function(v){
        if(v=="0"){ //�Զ�
          $("#autoref").show();
          $("#handref").hide();
       }else{
          $("#autoref").hide();
          $("#handref").show();
       }
    },
	//�澯����ͳ��
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
		//��lev0��¼�澯����
		lev0 = (lev0 + lev1 + lev2 + lev3 + lev4 + lev5);
		$("label[@name='total']").html(lev0 == 0 ? "0":lev0);

		lev0 = null;lev1 = null;lev2 = null;
		lev3 = null;lev4 = null;lev5 = null;
		//});
	},
    //��ʾʱ��
    showTime:function(){
        $("#last_reftime").html($.getTime());
    },
	//�澯����ת��URL:����֮�͵ķ���copy������
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
	//��ȡ��ǰʱ��
	getTime:function(){
		var date=new Date();
        var y=date.getYear();
        var m=date.getMonth()+1;
        var d=date.getDate();
        var h=date.getHours();
        var M=date.getMinutes();
        var s=date.getSeconds();
		time=y+"-"+m+"-"+d+" "+h+":"+M+":"+s;
		//�ͷű���
		date=null;
		y=null;m=null;d=null;h=null;M=null;s=null;
		return time;
	},
	//����״̬��ʾ
	loading:function(type){
		if(type==0)
		{
			$("#msg").html("<img src='../images/loading.gif'/>�����������ݣ���ȴ�......");
		}else if(type==1){
			$("#msg").html("<img src='../images/loading.gif'/>���ڸ������ݣ���ȴ�......");
		}else if(type==2){
			$("#msg").html("<img src='../images/loading.gif'/>���ݻ�ȡ��ϣ����ڸ���ҳ�棬��ȴ�......");
		}else if(type==3){
			$("#msg").html("");
		}
	},
	//��ȡ�ָ��澯
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
							target.children("[@name='lev']").html("�����");
							target.children("[@name='astu']").html("��ȷ��");
							target.attr("class","level_0");
							target.attr("lev","0");
							target.find("div").html(data[id].s);
						}
						
					}
					if(f) {
						$.StatWarnNum();//ͳ�Ƹ澯
						//$("#data").trigger("update");
						update_flg=false;
						if(debug) $("#test ul").prepend("<li><<<<<<<<<<���»ָ��澯>>>>>>>>>>>></li>");
					}
					//�ͷű���
					data=null;
					tmp_id=null;
					n=null;id=null;
				}
			},
			error:function(e){

			}
		});
	},
	//ˢ������
	RefData:function(){
		var t=new Date().getTime();
		if(debug) $("#test ul").prepend("<li>//////////////////////////</li>");
		if(debug) $("#test ul").prepend("<li>��ʼˢ������************"+(new Date().getTime()-t)+"</li>");
		t= new Date().getTime();
		$.RightMenuHide();
		$.RecorverCss();
		$.RemoveCSS();
		$.loading(1);
		ClearWarnInterval();
		var n0=eval($("label[@name='lev0_num']").html());//�Զ����
		var n1=eval($("label[@name='lev1_num']").html());//������־
		var n2=eval($("label[@name='lev2_num']").html());//��ʾ�澯
		var n3=eval($("label[@name='lev3_num']").html());//һ��澯
		var n4=eval($("label[@name='lev4_num']").html());//���ظ澯
		var n5=eval($("label[@name='lev5_num']").html());//�����澯
		var _total = n0 + n1 + n2 + n3 + n4 + n5;

		if(debug) 	$("#test ul").prepend("<li>��ʼ��������************"+(new Date().getTime()-t)+"</li>");
		t= new Date().getTime();
		//$.ajaxTimeout(20000);
		$.ajax({
			type:"post",
			url:"RealTimeWarn!getupdateData.action",
			timeout:20000,
			data:"ruleid="+$("input[@name='rule']").val()+"&fetchCount="+$("input[@name='max']").val()+"&columnID="+$("input[@name='columnID']").val()+"&gather_val="+$("input[@name='gather']").val(),
			success:function(s){
				if(debug)  $("#test ul").prepend("<li>ȡ�����ݽ���************"+(new Date().getTime()-t)+"</li>");
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
				var get_num=lev_0+lev_1+lev_2+lev_3+lev_4+lev_5;//��ǰȡ�ø澯��������
				var need_num = $("input[@name='max']").val()- _total;//��������ÿҳ��ʾ�������-��ǰҳ���ܹ���������
				var num = get_num - need_num;//��������
				//alert("ȡ����������"+num+" ���:"+need_num);
				$("input[@name='gather']").val(tmp.gather);
				if(debug) $("#test ul").prepend("<li>��ʼ����************"+(new Date().getTime()-t)+"</li>");
				t = new Date().getTime();
				//������ʱ���飬���ڴ��tr����
				var array = null;				
				if(get_num > 0){//�����ݵ������
					if(debug) $("#test ul").prepend("<li>ȡ����������************"+get_num+"</li>");
					if(get_num <= need_num){//�õ�������С�ڲ������:ȫ�����
					}else if(num<n0){//������������С���Զ��������Ŀ
						array=$.getArray("0");
						$.RemoveData(array,num,"0");
					}else if(num<n1){//������������С��������־����ʾ�澯����Ŀ
						array=$.getArray("1");
						$("tr[@lev='0']").remove();
						$.RemoveData(array,(num-n0),"1");
					}else if((num-n0-n1)<n2){//������������С��һ��澯
						array=$.getArray("2");
						$("tr[@lev='0']").remove();
						$("tr[@lev='1']").remove();
						$.RemoveData(array,(num-n0-n1),"2");
					}else if((num-n0-n1-n2)<n3){//������������С�����ظ澯
						array=$.getArray("3");
						$("tr[@lev='0']").remove();
						$("tr[@lev='1']").remove();
						$("tr[@lev='2']").remove();
						$.RemoveData(array,(num-n0-n1-n2),"3");
					}else if((num-n0-n1-n2-n3)<n4){//������������С�ڽ����澯
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

					if(debug) $("#test ul").prepend("<li>���ݴ������************"+(new Date().getTime()-t)+"</li>");
					t= new Date().getTime();
					$("#tbody").prepend(data);
					$.ChangeStyle(get_num);
					$.MouseEvent($(data));
					//$("#data").trigger("update");
					update_flg=false;
					$.StatWarnNum();//ͳ�Ƹ澯����
					if(debug) 	$("#test ul").prepend("<li>���ݼ��ؽ���************"+(new Date().getTime()-t)+"</li>");
					t= new Date().getTime();
					alarmlevel=lev_5>0?5:lev_4>0?4:lev_3>0?3:lev_2>0?2:lev_1>0?1:0;
					play();
					
					//�ͷű���
					tmp=null;
					delete tmp;
					data=null;
					delete data;
					lev_0=null;lev_1=null;lev_2=null;lev_3=null;lev_4=null;lev_5=null;

					//����array����
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
				if(debug) 	$("#test ul").prepend("<li>���ݴ������************"+(new Date().getTime()-t)+"</li>");
			},
			error:function(xmlR,msg,other){
				if(debug) 	$("#test ul").prepend("<li>//\\\\\\\��ʱ\\\\\\\\//"+(new Date().getTime()-t)+" "+timeout+"</li>");
				timeout++;
				$("#timeout").html("������ʱ"+timeout+"��");
				if(timeout>=7 & timeout<=9){
					OutTimeVoice();
				}
				else if(timeout>=10){
					ReloadWEB();//�˳�
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
	//��궯��
	MouseEvent:function(target){
		$.SynOperator(function(){
			//��굥��
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
			//�����ͣ
			target.bind("mouseover",function(){
				$(this).removeClass("tr_b");
			});
			//����Ҽ�
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
				//�����Ҽ��˵�
				$.ShowMenu();
				return false;
			});
			//˫���¼�
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
	//��ȡ���ݺ�ı���ʽ
	ChangeStyle:function(num){
		
		/*
		var divobj=null;
		var obj=$("#tbody");
		if(debug) 	$("#test ul").prepend("<li>��ʼ�ı���ʽ</li>");
		for(var i=0;i<num;i++){
			divobj=$(obj.rows[i]).find("div[@class='content']");
			divobj.click(function(){
				$(this).toggleClass('content')
			});
		}
		*/
		if(debug) 	$("#test ul").prepend("<li>������ʽ</li>");
	},
	//ɾ����ʽ
	RemoveCSS:function(){
		$("#tbody tr[@class contains tr_b]").removeClass("tr_b");
	},
	//ɾ������
	RemoveData:function(array,num,css){
		var sortobj=null;
		for(var i=0;i<num;i++){
			sortobj=array[i];
			//$("#"+sortobj.id).remove();
			sortobj.remove();
		}
	},
	//�õ�����
	getArray:function(level){
		var array=[];
		$("tr[@lev='"+level+"']").each(function(i){
			//array[i]=new sortobj($(this).attr("time"),$(this).attr("id"));
			array[i]=$(this);
		});
		array.sort(function(a,b){return eval(a.attr("time"))-eval(b.attr("time"));});
		return array;
	},
	//�жϸ澯����
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
	//��ʾ�Ҽ��˵�
	ShowMenu:function(){
		createRMenu("-3",-1);
		showRightMenu();
	},
	//��λ�豸
	findDevObj:function(){
		var open = null;
		try{
			open = ((window.opener == null) ? null : window.opener);
		}catch(e){
			alert("WebTopo�����ѱ��رգ��޷���λ���豸��(" + e.description + ")");
			return false;
		}
		$.SynOperator(function(){
			var tmp=sel_id.split("-/-");
			var dev=$("#"+tmp[tmp.length-2]).attr("devid");
			if(typeof(dev)=="undefined"){
				alert("���豸û���豸ID���޷���λ!");
				return;
			}
			if(open != null && typeof(open.findDevObjLocation) == "object"){
				//20091014 ��ï���޸� ���ʵʱ�澯�����޷���λ�豸��bug ��MC�Ǳ߱��붼��1/gw/device_id��
				open.findDevObjLocation("1/gw/"+dev);
				open.focus();
			}
			else alert("WebTopo�����ѱ��رգ��޷���λ���豸��");
			return;
		});
	},
	//�첽����
	SynOperator:function(fun){
		setTimeout(fun,1);
	},
	//����EXCEL
	Export:function(){
		$.RemoveCSS();
		$.RecorverCss();
		//initialize(data,0,0);
		$("#rptdataExcel").val($("#table").html());
		$("form").submit();
		$.RightMenuHide();
		$.RecoverMEM();
	},
	//IE�ڴ��ͷ�
	RecoverMEM:function(){
		//��������,ֻ���IE
		if(typeof(CollectGarbage)=="object"){
			CollectGarbage();
		}
	},
	//ȷ�ϸ澯
	ConfigAlarm:function(){
		if(window.confirm("�Ƿ�Ҫȷ�ϸ澯?")){			
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
							$("#"+tmp[i]+" td[@name=lev]").html("�Ѿ�����");
							$("#"+tmp[i]+" td[@name=astu]").html("�ֹ�ȷ��");
							$("#"+tmp[i]+" td[@name=atime]").html($.getTime());
						}
						$.StatWarnNum();//ͳ�Ƹ澯����
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
						alert("ȷ��δ�ɹ���������");
					}
					getRefWarn();
				},
				error:function(e){
					alert("session��ʱ,�����µ�½!");
					getRefWarn();
				}
			});
		}
		$.RightMenuHide();
	},
	//ɾ�������澯
	clearAlarm:function(id){
		if(window.confirm("�Ƿ�Ҫɾ���澯?")){		
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
						$.StatWarnNum();//ͳ�Ƹ澯����
						$.RecoverMEM();
					}else{
						alert("ɾ��δ�ɹ���������");
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
	//ɾ���澯
	DelAlarm:function(){
		if(window.confirm("�Ƿ�Ҫ����澯?")){		
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
						$.StatWarnNum();//ͳ�Ƹ澯����
						$.RecoverMEM();
						
					}else{
						alert("���δ�ɹ���������");
					}
					getRefWarn();
				},
				error:function(msg){
					getRefWarn();
					alert("session��ʱ,�����µ�½!");
				}
			});
			
		}
		$.RightMenuHide();	
	},
	//�����ҽ��˵�
	RightMenuHide:function(){
		document_click();
	},
	//��סShift������ѡ�е���
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
	//�ָ�tableԭ������ʽ
	RecorverCss:function(){
		if(!sel_id==""){
			sel_id=sel_id.substring(0,sel_id.length-3);
			var tmp=sel_id.split("-/-");
			var n=tmp.length;
			for(var i=0;i<n;i++){
				$("#"+sel_json[tmp[i]].id).attr("class",sel_json[tmp[i]].css);
			}
		}
		//��ձ���
		endindex="";
		sel_id="";
		sel_devid="";
		sel_json={};
	},
	//���Jsonֵ ��ʱʹ��1/dev�����ҵ��ƽ̨����6/dev
	AddJsonVal:function(id,cls){
		if(typeof(sel_json[id])=="undefined"){
			sel_json[id]={id:id,css:cls};
			sel_id+=id+"-/-";
			var tmp=$("#"+id).attr("devid");
			tmp=(typeof(tmp)=="undefined"?"":tmp);
			sel_devid+=id+"-1/hgw/"+tmp+"-"+$("#"+id).attr("time")+"-/-";
		}
	},
	//��ʾ��������
	DisplayStr:function(){
		$("#tbody td").addClass("td_r");
		$("div[@class='content']").bind("click",function(){
			$(this).removeClass("td_r");
			$(this).toggleClass('content')
		});
	},
	//��ʼ��ģ��ѡ���
	InitModule:function(){
		var rid=$("input[@name='rule']").val()+"-/-"+$("input[@name='max']").val();
		//û��rule_id
		if(rid=="-/-"){
			$("select[@name='ruleid'] option").each(function(){
				if($(this).attr("sel")==1){
					$(this).attr("selected",true);
				}
			});
			var tmp=($("select[@name='ruleid']").val()).split("-/-");
			$("input[@name='rule']").val(tmp[0]);//��Ҫ��ʾ�ĸ澯����ID
			$("input[@name='max']").val(tmp[1]);//ÿҳ��ʾ���������
		}else{
			$("select[@name='ruleid'] option").each(function(){
				if($(this).val()==rid){
					$(this).attr("selected",true);
				}
			});
		}
	},
	//DIV��ʾ������
	divshow:function(target){
		target.toggleClass('content');
	},
	//�Ƴ��澯 flg:-2:�Ҽ�ѡ�еĸ澯,-1:���и澯,0:����澯,1:������־,2:��ʾ�澯,3:һ��澯,4:���ظ澯,5:�����澯
	RemoveWarn:function(flg){
		switch(flg){
			case -2://�Ҽ�ѡ��
				if(window.confirm("�Ƿ�ȷ��Ҫ�Ƴ�ѡ�еĸ澯?")){
					sel_id=sel_id.substring(0,sel_id.length-3);
					var tmp=sel_id.split("-/-");
					var n=tmp.length;
					for(var i=0;i<n;i++){
						$("#"+sel_json[tmp[i]].id).remove();
					}
					$.StatWarnNum();
					//��ձ���
					endindex="";
					sel_id="";
					sel_devid="";
					sel_json={};
					update_flg=false;
				}
				break;
			case -1://���и澯
				if(window.confirm("�Ƿ�ȷ��Ҫ�Ƴ�ѡ�еĸ澯?")){
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
			case 0://����澯
				if(window.confirm("�Ƿ�ȷ��Ҫ�Ƴ���������澯?")){
					$("tr[@lev='0']").remove();
					$("label[@name='lev0_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 1://������־
				if(window.confirm("�Ƿ�ȷ��Ҫ�Ƴ������¼��澯?")){
					$("tr[@lev='1']").remove();
					$("label[@name='lev1_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 2://��ʾ�澯
				if(window.confirm("�Ƿ�ȷ��Ҫ�Ƴ����о���澯?")){
					$("tr[@lev='2']").remove();
					$("label[@name='lev2_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 3://һ��澯
				if(window.confirm("�Ƿ�ȷ��Ҫ�Ƴ����д�Ҫ�澯?")){
					$("tr[@lev='3']").remove();
					$("label[@name='lev3_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 4://���ظ澯
				if(window.confirm("�Ƿ�ȷ��Ҫ�Ƴ�������Ҫ�澯?")){
					$("tr[@lev='4']").remove();
					$("label[@name='lev4_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
			case 5://�����澯
				if(window.confirm("�Ƿ�ȷ��Ҫ�Ƴ��������ظ澯?")){
					$("tr[@lev='5']").remove();
					$("label[@name='lev5_num']").html("0");
					$.sumtotalnum();
					update_flg=false;
				}
				break;
		}
	},
	//����澯����
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