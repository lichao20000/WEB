//���ߣ� ��־�� 5194 tel:13701409234 07-09-11
// ��ʹ��ASCII����
jQuery.extend({
/**
 * ����תlong�ĺ�������������web�ϵ�����ת��Ϊlong�ķ�ʽ��֧��dateΪ2007-07-07����ʽ��ȡΪ
 * @param date dom�������� ��׼��ʽΪ2007-07-07��
 *  return ʱ��ĺ�����
 */
dateToLong:function(date){
	var all  = date.attr("value").split("-");
	var da = new Date(all[1]+"/"+all[2]+"/"+all[0]);
	return da.getTime();
},
/**
 *����ת��long�ͣ�yyyy-mm-dd hh:MM:ss��
 *param:time(String����)
 */
dtls:function(time){
    time=time.replace("-","/");
    var t=new Date(time);
    return t.getTime()/1000;
},
/**
 *author:benyp(5260) 2007-12-20
 *ȡ�õ�ǰʱ��(ȥ0)
 *param:sep:�ָ���(/��-):ʱ�����ԣ��ָ�
 *param:flg:�Ƿ�Ҫʱ����
 *return time:��ǰʱ��
 */
now:function(sep,flg){
    var t=new Date();
    var y=t.getYear();
    var m=t.getMonth()+1;
    var d=t.getDate();
    var h=t.getHours();
    var min=t.getMinutes();
    var s=t.getSeconds();
    var time="";
    if(flg){
        time=y+sep+m+sep+d+" "+h+":"+min+":"+s;
    }else{
        time=y+sep+m+sep+d;
    }
    return time;
},
/**
 *author:benyp(5260) 2007-12-20
 *��ȡʱ��(��Ҫ����ȥǰһ�졢ǰһ�ܡ�ǰһ�¡�ǰһ���ʱ�䣬Ҳ�ṩ�Զ�����ǰ�Ƽ���)
 *param��ʱ�䣺time�����Ϊnow��Ϊ��ǰʱ�䣬�������Զ����ʱ��
 *param:���ͣ�day��ǰһ�죬week:ǰһ��,month��ǰһ�£�year��ǰһ�꣬def���Զ���
 *param��sep���ָ���(/��-):ʱ�����ԣ��ָ�
 *param��num����Ҫ��ǰ�Ƶ�����
 *param flg:�Ƿ���Ҫʱ��
 *return rt:��Ҫ��ʱ��
 */
 getTime:function(time,type,sep,num,flg){
    var rt="";
    if(time=="now"){
        time=$.now(sep,flg);
    }
    if(type=="day"){
        rt=$.bd(time,1,sep,flg);
    }else if(type=="week"){
        rt=$.bd(time,7,sep,flg);
    }else if(type=="month"){
        var d=$.RD(time,sep);
        rt=$.bd(time,d,sep,flg);
    }else if(type=="year"){
        var tmp=time.split(sep);
        var y=tmp[0];
        rt=time.replace(y,y-1);
    }else{
        rt=$.bd(time,num,sep,flg);
    }
    return rt;
 },
 /**
  *��ǰ��d��
  *sep:�ָ���
  *flg:�Ƿ���Ҫʱ��
  */
 bd:function(time,day,sep,flg){
    var tt;
    if(sep=="-"){
        tt=new Date(time.replace("-","/"));
    }else{
        tt=new Date(time);
    }
	var t=new Date(tt.getTime()-24*60*60*1000*day);
	var y=t.getYear();
	var m=t.getMonth()+1;
	var d=t.getDate();
	var h=t.getHours();
	var M=t.getMinutes();
	var s=t.getSeconds();
	var rtime;
	if(flg){
	   rtime=y+sep+m+sep+d+" "+h+":"+M+":"+s;
	}else{
	   rtime=y+sep+m+sep+d;
	}
    return rtime;
 },
 /**
  *���ظ����ж�����
  */
RD:function(time,sep){
	var tmp=time.split(sep);
	var y=tmp[0];
	var m=tmp[1];
	m=m==1?12:m-1;
    var t=0;
    if(m==2){
        var flg=$.WFyear(y);
        t=flg==0?29:28;
      }else if(m in {1:1,3:3,5:5,7:7,8:8,10:10,12:12}){
        t=31;
      }else{
        t=30;
      }
      return t;
},
 /**
  *�ж��Ƿ�Ϊ����
  *true��false����
  */
WFYear:function(year){
   var flg=false;
   if(year % 100 == 0 && year % 4 == 0){
      flg=true;
   }else if(year % 4 == 0){
      flg=true;
   }
   return flg;
},
/**
*author:benyp(5260) 2007-12-11
*ת��(ʹ�ú���encodeURIComponent())
*param:name(��Ҫת����ֶΣ���input[@name='id'] or #id)
*ע��Ĭ����ȡval()��ֵ
*return:ת�����ֶΣ��ȼ��ڣ�encodeURIComponent($("input[@name='id']").val())
*�÷���$.cc(input[@name='name']) or $.cc(#tab)
*/
cc:function(name){
	 	return encodeURIComponent($(name).val());
},
/**
*author:benyp(5260) 2007-12-11
*ת��(ʹ�ú���encodeURIComponent())
*param:name(��Ҫת����ֶ���Ҫдȫ��������$(input[@name='id']).val() or $('#id').val() or $("#sel").text())
*ע���Զ���ȡֵ
*return:ת�����ֶΣ��ȼ��ڣ�encodeURIComponent($("input[@name='id']").val())
*�÷���$.co($(input[@name='name']).val()) or $.co($(#tab).text() ��)
*/
co:function(name){
		return encodeURIComponent(name);
},
/**
*�����ļ���js��������ֹÿ�����ص�ҳ�涼����iframe������õĶ���
*@param url �����ĵ���·��������/vipms/download.action?XXXXX
**/
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

/**
*
*�÷���$.autoMatch3("<s:url value='/liposs/workflow/wifi/searchSuggestAction!hotspotNameSuggest.action'/>",$("#hotspotNameId"),"#");
*     ע�⣺��action��Ҫ��������������1.String searchTxt 2. String separator
*���ߣ�shenwq
*�ڽ���������Ϣʱ�Զ�ƥ������
*@param url  ��ѯ���ݵ�action
*@param target  �����input��
*@param separator  �Ӻ�̨��ѯ������ƥ����������õķָ����磺'#'
*return
*/
autoMatch3:function(url,str,separator){

   var target = str[0];
   var parDiv = str[1];
   var gwShare_queryField = str[2];

   target.attr("autocomplete","off");

   //��input���blur�¼�����������div��ʧ
   target.bind("blur",function(){
      parDiv.hide();
   });
   //��input���focus�¼�����������div��ʾ
   target.bind("focus",function(){
       parDiv.show();
   });
	   
   //��input���keyup�¼�����ѯ��̨����
   target.bind("propertychange",function(){
   
        //��ȡ�ı��������ֵ
        var ch = target.val();
        var ch2 = gwShare_queryField.val();
        //��Ϊ���򷵻�
        if($.trim(ch)==''){
          return ;
        }
        //��������
        $.ajax({
				type: "post",
				url: url,
				data: {"searchTxt":$.co(ch),"separator":separator,"gwShare_queryField":ch2},
				success: function(data){
                     $.wrapSuggestData(data,target,parDiv,separator,ch2);
				},
				error: function(xmlR,msg,other){alert(msg);}
			});
   });
   
},

wrapSuggestData:function(data,target,parDiv,separator,queryField){
      
   var left = target.parent().attr("offsetLeft") + target.attr("offsetLeft") + 20;
   var width = target.attr("offsetWidth");
   parDiv.css({	position: "absolute","width":width+"px","left":left+"px","height":"96px","background-color": "#FFFFFF","text-align": "left", border: "1px solid #000000","z-index":"10"});
   //��ֹ��select���ڸǡ�
   parDiv.append("<iframe style='position:absolute;visibility:inherit;top:0px;left:0px;width:100%;height:100%;z-index:-1;filter:progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);'></iframe>");
   //�Ƚ�֮ǰdiv�ڵ��������
   parDiv.empty();
   
   //����һ���б�
   var ul = $("<ul style='margin:0;padding:0;background-color:#FFFFFF;'></ul>");
   
   //��װ���ݣ����¼�
   var arr=data.split(separator);
   var len=arr.length;
   for(var i=0;i<len;i++){
        var value=arr[i];
        if (value== null||value==""){
        	continue;
        }
        
        var li=$("<li tt='" +value + "' style=\"padding:'2px 6px 2px 6px';cursor:'hand'\">" +value + "</li>");
         //��mouseover
        li.bind("mouseover",function(){
           $(this).css({"background-color":"#d0d7ec"});
        });
        //��mouseout
        li.bind("mouseout",function(){
           $(this).css({"background-color":"#FFFFFF"});
        });
        //��mousedown
        li.bind("mousedown",function(){
			var ddvalue = this.tt.split("|");
			if("deviceSn"==queryField){
				 target.val(ddvalue[0]);
			}else{
			     target.val(ddvalue[1]);
			}
            parDiv.empty();
            parDiv.hide();
        });
        //��ֹ��select���ڸǡ�
        li.append("<iframe style='position:absolute;visibility:inherit;top:0px;left:0px;width:100%;height:100%;z-index:-1;filter:progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);'></iframe>");
        ul.append(li);
        
   }
   
   ul.attr("size",len);
   parDiv.append(ul);   
},

/**
*���ߣ�������
*�ڽ���������Ϣʱ�Զ�ƥ������
*@param url  ��ѯ���ݵ�action
*@param target  �����input��
*@param showTarget  ��ʾ���������Ŀ�ѡ����
*return
*/
autoMatch2:function(url,target,param){

	target.attr("autocomplete","off");

	//ȡ����ʾѡ����λ��
	var left = target.parent().attr("offsetLeft") + target.attr("offsetLeft") + 7;
	var top = target.parent().attr("offsetTop") + target.attr("offsetTop") 
			+ target.parent().attr("offsetHeight") + target.attr("offsetHeight") + 40;
	var width = target.attr("offsetWidth");
	

	//׷����������ƥ����б�
	var list_id = target.attr("id") + "_matchList";
	target.after("<div id='" + list_id + "' style='position:absolute;width:" +width + "px;height:100px;z-index:1;border:1px solid #000000;display:none;background-color:#FFFFFF;left:" + left + "px;top:" + top + "px;'></div>");

	//��ȡ����
	var showTarget = $("#" + list_id);

	//���尴���¼�
	target.bind("keyup",function(){
		//��ǰ��������Ϣ
		var ch = target.val();
		if (ch.length <= 4)
		{
			return;
		}

		if (ch == ''){
			showTarget.html("");
			showTarget.hide();
		}
		else{
				$.ajax({
					type: "POST",
					url: url,
					data: param+"="+ch,
					success: function(data){

						$.showMatchText(ch,data,target,showTarget);
					},
					error: function(xmlR,msg,other){alert(msg);}
				});
		}

	});
	
	//target.bind("blur",function(){
	//alert(window.event.srcElement.parentElement.name);
	//		showTarget.hide();
	//});
},
/**
*���ߣ�������
*�ڽ���������Ϣʱ�Զ�ƥ������
*@param url  ��ѯ���ݵ�action
*@param target  �����input��
*@param showTarget  ��ʾ���������Ŀ�ѡ����
*return
*/
autoMatch:function(url,target,param){

	target.attr("autocomplete","off");

	//ȡ����ʾѡ����λ��
	var left = target.parent().attr("offsetLeft") + target.attr("offsetLeft");
	var top = target.parent().attr("offsetTop") + target.attr("offsetTop") + target.attr("offsetHeight");
	var width = target.attr("offsetWidth");

	//׷����������ƥ����б�
	var list_id = target.attr("id") + "_matchList";
	target.after("<div id='" + list_id + "' style='position:absolute;width:" +width + "px;height:100px;z-index:1;border:1px solid #000000;display:none;background-color:#FFFFFF;left:" + left + "px;top:" + top + "px;'></div>");

	//��ȡ����
	var showTarget = $("#" + list_id);

	//���尴���¼�
	target.bind("keyup",function(){
		//��ǰ��������Ϣ
		var ch = target.val();

		if (ch == ''){
			showTarget.html("");
			showTarget.hide();
		}
		else{
			//ÿ���������ַ�����һ��ƥ��,��ѯ����Ҫ������
				$.ajax({
					type: "POST",
					url: url,
					data: param+"="+ch,
					success: function(data){

						$.showMatchText(ch,data,target,showTarget);
					},
					error: function(xmlR,msg,other){alert(msg);}
				});
		}

	});
},
/**
*���ߣ�������
*�ڽ���������Ϣʱ�Զ�ƥ������
*@param input  ���������
*@param data  �������������ݲ�ѯ���ķ�������������
*@param target  �����input��
*@param showTarget  ��ʾ���������Ŀ�ѡ����
*return
*/
showMatchText:function(input,data,target,showTarget){

		//����������,����Ϊ���������ҳ��,Ϊ����ر�ѡ���б�
		if (data != null && data != ''){
			var dataList = data.split("#");
			var len = dataList.length;
			var isyou = 0;

			var msg = "";

			//���б���ʽ��ʾ�Ŀ�ѡ����
			msg += "<ul size=10 style='margin:0;padding:0;width:100%;background-color:#FFFFFF;' >";
			for (var i=0;i<len;i++){

				if (dataList[i] != ''){

					msg += "<li >"+dataList[i]+"</li>";
					isyou = 1;
				}
			}
			msg += "</ul>";

			//�ж��Ƿ���ڿ�ѡ��Ϣ,��û����ر�ѡ���б�
			if (isyou == 1){
				showTarget.html(msg);
				showTarget.show();

				//���б��嵥���¼�
				$("li").each(function(){
					$(this).bind("click",function(){
						target.val(this.innerHTML);
						showTarget.hide();
					});

					$(this).bind("mouseover",function(){
						this.style.backgroundColor="#4169E1";
					});

					$(this).bind("mouseout",function(){
						this.style.backgroundColor="";
					});
					
				});
			}else{
				showTarget.html("");
				showTarget.hide();
			}
		}
		else{
			showTarget.html("");
			showTarget.hide();
		}
},
/**
*���ߣ�������
*������������select��optionsѡ��,������ֻ�Ƕ�ǰ̨�Ķ�̬�滻/������̼�,��Ϊ��ƺ�̨��action�߼�.��̨����Ҫ���з�װ
*@param url  ������������option�ַ�����action
*@param params  ����action�Ĳ���
*@param target  ��Ҫ��̬�滻��select�ı�ʶ
*@param defaultVal Ĭ��ֵ���������˵���ֵ���������Ҫ��null
*e.g
*$("eg").change(function(){
*	$.createOpts("<s:url value="cops.action"/>","param1=eg[����Ϊjson����]","select[@name=XX] or XXId[Ŀ���id]",null[��ҪĬ��ֵʱ�����Ǿ���ֵ]);
*})
*/
createOptsWithDefault:function(url,params,target,defaultVal)
{
	if(defaultVal==null)
	{
		$.createOpts(url,params,target);
	}
	else
	{
		var  tgt= $(target);
		if(tgt.size()==0)
		{
			alert("�޷���ȡ��Ҫ�滻��select���,���鴫���id������ر�ʶ");
			return;
		}else
		{
			tgt.html("<option value='loading'>���ڼ�������...</option>");
			$.ajax({
			type: "post",
			url: url,
			data: params,
			success: function(data){
				tgt.html(data);
				tgt.val(defaultVal);
			},
				error: function(xmlR,msg,other){
				tgt.html("<option value='error'>==��ѡ��==</option>");}
			});
		}
	}
},
/**
*���ߣ���־��
*������������select��optionsѡ��,������ֻ�Ƕ�ǰ̨�Ķ�̬�滻/������̼�,��Ϊ��ƺ�̨��action�߼�.��̨����Ҫ���з�װ
*@param url  ������������option�ַ�����action
*@param params  ����action�Ĳ���
*@param target  ��Ҫ��̬�滻��select�ı�ʶ
*e.g
*$("eg").change(function(){
*	$.createOpts("<s:url value="cops.action"/>","param1=eg[����Ϊjson����]","select[@name=XX] or XXId[Ŀ���id]");
*})
*/
createOpts:function(url,params,target)
{
	var  tgt= $(target);
	if(tgt.size()==0)
	{
		alert("�޷���ȡ��Ҫ�滻��select���,���鴫���id������ر�ʶ");
		return;
	}else
	{
			tgt.html("<option value='loading'>���ڼ�������...</option>");
			$.ajax({
			type: "POST",
			url: url,
			data: params,
			success: function(data){
				tgt.html(data);
			},
				error: function(xmlR,msg,other){
				tgt.html("<option value='error'>==��ѡ��==</option>");}
			});
		}
},
/**
*��ʼ��Tab�ķ���:
$.iTab(); ����$.iTab(0); 0 Ϊѡ�е�һ����ǩ
*@param selectedIndex ��������Ĭ��Ϊ0 tab��ǩ��ѡ�е�����ֵ��һ�㴫��Ĭ��ѡ�е�ֵ0,Ϊ��һ����
*
*
**/
iTab:function(selectedIndex)
{
	if(selectedIndex==null||selectedIndex=="undefined")
	{
		selectedIndex=0;
	}
	var lis= $(".tab_Menubox ul li");
	//���ʱ���
	lis.bind("click",function(){
	var tg = $(this).attr("target");
	var selected =$(".tab_Menubox ul li[@target="+tg+"]");
  	selected.addClass("hover");
  	$("#"+selected.attr("target")).show();
  	var unselected= $(".tab_Menubox ul li[@target!="+tg+"]");
  	unselected.removeClass("hover");
  	unselected.each(function(){
  		$("#"+$(this).attr("target")).hide();
  	});
  });
  //������ʼ��
  lis.each(function(i){
  var t = $(this);
  t.addClass("out");
  	if(i==selectedIndex)
  	{
  		t.addClass("hover");
  		$("#"+t.attr("target")).show();
  	}
  	else
  	{
  		t.removeClass("hover");
  		$("#"+t.attr("target")).hide();
  	}
  });
},
/**
*���ߣ���־��
*����ͳһ�������ڣ����ַ��һ��
*@param url  open����Ҫ�򿪵ĵ�ַ
*@param width ���ڴ����Ŀ�ȣ�����12px�������д��ϵ�λ
*@param height ���ڵĸ߶ȣ�����12px,�����д��ϵ�λ
*@param top ������ʾʱ����������ϱߵľ��룬����100px�������д��ϵ�λ
*@param left ������ʾʱ�����������ߵľ��룬����200px�������д��ϵ�λ
*@param resizable �Ƿ���Ե������ڴ�С,booleanֵ��true or false
**/
open:function(url,width,height,top,left,resizable)
{
	return window.open(url,"_blank","status=yes,toolbar=no,menubar=no,location=no,width="+width+",height="+height+",top="+top+",left="+left+",resizable="+(resizable=="true"?"yes":"no"));
},
/*
*Table scroll (1)
*/
fixerEvent:function(divid)
{
	$.fixerTable($("#"+divid));
	$("#"+divid).scroll(function(){
		$.fixerTable($(this));
	});
},
/*
*Table scroll (2)
*/
fixerTable:function(div)
{
	var divcon=$(div);
	var table=$("table",divcon);
	var top=divcon[0].scrollTop;
	$("thead>tr",table).css({"position":"relative","top":top});
	var bottom = divcon[0].scrollHeight > divcon[0].clientHeight ? divcon[0].scrollHeight - divcon[0].scrollTop - divcon[0].clientHeight :0;
	$("tfoot>tr",table).css({"position":"relative","bottom":bottom});
}
});