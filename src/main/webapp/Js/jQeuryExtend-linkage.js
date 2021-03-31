//作者： 王志猛 5194 tel:13701409234 07-09-11
// 请使用ASCII编码
jQuery.extend({
/**
 * 日期转long的函数，对于我们web上的日期转换为long的方式。支持date为2007-07-07的形式获取为
 * @param date dom对象（日期 标准形式为2007-07-07）
 *  return 时间的毫秒数
 */
dateToLong:function(date){
	var all  = date.attr("value").split("-");
	var da = new Date(all[1]+"/"+all[2]+"/"+all[0]);
	return da.getTime();
},
/**
 *日期转换long型（yyyy-mm-dd hh:MM:ss）
 *param:time(String类型)
 */
dtls:function(time){
    time=time.replace("-","/");
    var t=new Date(time);
    return t.getTime()/1000;
},
/**
 *author:benyp(5260) 2007-12-20
 *取得当前时间(去0)
 *param:sep:分隔符(/或-):时分秒以：分割
 *param:flg:是否要时分秒
 *return time:当前时间
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
 *获取时间(主要用于去前一天、前一周、前一月、前一年的时间，也提供自定义往前推几天)
 *param：时间：time：如果为now即为当前时间，否则是自定义的时间
 *param:类型：day：前一天，week:前一周,month：前一月，year：前一年，def：自定义
 *param：sep：分隔符(/或-):时分秒以：分割
 *param：num：需要往前推的天数
 *param flg:是否需要时刻
 *return rt:需要的时间
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
  *往前推d天
  *sep:分隔符
  *flg:是否需要时刻
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
  *返回该月有多少天
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
  *判断是否为闰年
  *true是false不是
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
*转码(使用函数encodeURIComponent())
*param:name(需要转码的字段：如input[@name='id'] or #id)
*注：默认是取val()的值
*return:转码后的字段：等价于：encodeURIComponent($("input[@name='id']").val())
*用法：$.cc(input[@name='name']) or $.cc(#tab)
*/
cc:function(name){
	 	return encodeURIComponent($(name).val());
},
/**
*author:benyp(5260) 2007-12-11
*转码(使用函数encodeURIComponent())
*param:name(需要转码的字段需要写全参数：如$(input[@name='id']).val() or $('#id').val() or $("#sel").text())
*注：自定义取值
*return:转码后的字段：等价于：encodeURIComponent($("input[@name='id']").val())
*用法：$.co($(input[@name='name']).val()) or $.co($(#tab).text() 等)
*/
co:function(name){
		return encodeURIComponent(name);
},
/**
*下载文件的js方法，防止每个下载的页面都出现iframe这个无用的东西
*@param url 下载文档的路径，例如/vipms/download.action?XXXXX
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
*用法：$.autoMatch3("<s:url value='/liposs/workflow/wifi/searchSuggestAction!hotspotNameSuggest.action'/>",$("#hotspotNameId"),"#");
*     注意：在action中要定义两个变量：1.String searchTxt 2. String separator
*作者：shenwq
*在界面输入信息时自动匹配内容
*@param url  查询数据的action
*@param target  输入的input框
*@param separator  从后台查询到的相匹配的数据所用的分隔符如：'#'
*return
*/
autoMatch3:function(url,str,separator){

   var target = str[0];
   var parDiv = str[1];
   var gwShare_queryField = str[2];

   target.attr("autocomplete","off");

   //给input框绑定blur事件，让让外层的div消失
   target.bind("blur",function(){
      parDiv.hide();
   });
   //给input框绑定focus事件，让让外层的div显示
   target.bind("focus",function(){
       parDiv.show();
   });
	   
   //给input框绑定keyup事件，查询后台数据
   target.bind("propertychange",function(){
   
        //获取文本框输入的值
        var ch = target.val();
        var ch2 = gwShare_queryField.val();
        //若为空则返回
        if($.trim(ch)==''){
          return ;
        }
        //请求数据
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
   //防止如select的遮盖。
   parDiv.append("<iframe style='position:absolute;visibility:inherit;top:0px;left:0px;width:100%;height:100%;z-index:-1;filter:progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);'></iframe>");
   //先将之前div内的内容清空
   parDiv.empty();
   
   //构造一个列表
   var ul = $("<ul style='margin:0;padding:0;background-color:#FFFFFF;'></ul>");
   
   //组装数据，加事件
   var arr=data.split(separator);
   var len=arr.length;
   for(var i=0;i<len;i++){
        var value=arr[i];
        if (value== null||value==""){
        	continue;
        }
        
        var li=$("<li tt='" +value + "' style=\"padding:'2px 6px 2px 6px';cursor:'hand'\">" +value + "</li>");
         //绑定mouseover
        li.bind("mouseover",function(){
           $(this).css({"background-color":"#d0d7ec"});
        });
        //绑定mouseout
        li.bind("mouseout",function(){
           $(this).css({"background-color":"#FFFFFF"});
        });
        //绑定mousedown
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
        //防止如select的遮盖。
        li.append("<iframe style='position:absolute;visibility:inherit;top:0px;left:0px;width:100%;height:100%;z-index:-1;filter:progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);'></iframe>");
        ul.append(li);
        
   }
   
   ul.attr("size",len);
   parDiv.append(ul);   
},

/**
*作者：陈仲民
*在界面输入信息时自动匹配内容
*@param url  查询数据的action
*@param target  输入的input框
*@param showTarget  显示符合条件的可选数据
*return
*/
autoMatch2:function(url,target,param){

	target.attr("autocomplete","off");

	//取得显示选择框的位置
	var left = target.parent().attr("offsetLeft") + target.attr("offsetLeft") + 7;
	var top = target.parent().attr("offsetTop") + target.attr("offsetTop") 
			+ target.parent().attr("offsetHeight") + target.attr("offsetHeight") + 40;
	var width = target.attr("offsetWidth");
	

	//追加输入内容匹配的列表
	var list_id = target.attr("id") + "_matchList";
	target.after("<div id='" + list_id + "' style='position:absolute;width:" +width + "px;height:100px;z-index:1;border:1px solid #000000;display:none;background-color:#FFFFFF;left:" + left + "px;top:" + top + "px;'></div>");

	//获取对象
	var showTarget = $("#" + list_id);

	//定义按键事件
	target.bind("keyup",function(){
		//当前输入框的信息
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
*作者：陈仲民
*在界面输入信息时自动匹配内容
*@param url  查询数据的action
*@param target  输入的input框
*@param showTarget  显示符合条件的可选数据
*return
*/
autoMatch:function(url,target,param){

	target.attr("autocomplete","off");

	//取得显示选择框的位置
	var left = target.parent().attr("offsetLeft") + target.attr("offsetLeft");
	var top = target.parent().attr("offsetTop") + target.attr("offsetTop") + target.attr("offsetHeight");
	var width = target.attr("offsetWidth");

	//追加输入内容匹配的列表
	var list_id = target.attr("id") + "_matchList";
	target.after("<div id='" + list_id + "' style='position:absolute;width:" +width + "px;height:100px;z-index:1;border:1px solid #000000;display:none;background-color:#FFFFFF;left:" + left + "px;top:" + top + "px;'></div>");

	//获取对象
	var showTarget = $("#" + list_id);

	//定义按键事件
	target.bind("keyup",function(){
		//当前输入框的信息
		var ch = target.val();

		if (ch == ''){
			showTarget.html("");
			showTarget.hide();
		}
		else{
			//每输入两个字符进行一次匹配,查询出需要的数据
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
*作者：陈仲民
*在界面输入信息时自动匹配内容
*@param input  输入的内容
*@param data  根据输入框的内容查询出的符合条件的数据
*@param target  输入的input框
*@param showTarget  显示符合条件的可选数据
*return
*/
showMatchText:function(input,data,target,showTarget){

		//处理返回数据,若不为空则输出到页面,为空则关闭选择列表
		if (data != null && data != ''){
			var dataList = data.split("#");
			var len = dataList.length;
			var isyou = 0;

			var msg = "";

			//以列表形式显示的可选数据
			msg += "<ul size=10 style='margin:0;padding:0;width:100%;background-color:#FFFFFF;' >";
			for (var i=0;i<len;i++){

				if (dataList[i] != ''){

					msg += "<li >"+dataList[i]+"</li>";
					isyou = 1;
				}
			}
			msg += "</ul>";

			//判断是否存在可选信息,若没有则关闭选择列表
			if (isyou == 1){
				showTarget.html(msg);
				showTarget.show();

				//对列表定义单击事件
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
*作者：隋晓哲
*联动处理生成select的options选项,本方法只是对前台的动态替换/请求过程简化,并为设计后台的action逻辑.后台仍需要自行封装
*@param url  处理该请求产生option字符串的action
*@param params  传给action的参数
*@param target  需要动态替换的select的标识
*@param defaultVal 默认值：被联动菜单的值，如果不需要则传null
*e.g
*$("eg").change(function(){
*	$.createOpts("<s:url value="cops.action"/>","param1=eg[可以为json数组]","select[@name=XX] or XXId[目标的id]",null[需要默认值时可以是具体值]);
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
			alert("无法获取需要替换的select组件,请检查传入的id或者相关标识");
			return;
		}else
		{
			tgt.html("<option value='loading'>正在加载数据...</option>");
			$.ajax({
			type: "post",
			url: url,
			data: params,
			success: function(data){
				tgt.html(data);
				tgt.val(defaultVal);
			},
				error: function(xmlR,msg,other){
				tgt.html("<option value='error'>==请选择==</option>");}
			});
		}
	}
},
/**
*作者：王志猛
*联动处理生成select的options选项,本方法只是对前台的动态替换/请求过程简化,并为设计后台的action逻辑.后台仍需要自行封装
*@param url  处理该请求产生option字符串的action
*@param params  传给action的参数
*@param target  需要动态替换的select的标识
*e.g
*$("eg").change(function(){
*	$.createOpts("<s:url value="cops.action"/>","param1=eg[可以为json数组]","select[@name=XX] or XXId[目标的id]");
*})
*/
createOpts:function(url,params,target)
{
	var  tgt= $(target);
	if(tgt.size()==0)
	{
		alert("无法获取需要替换的select组件,请检查传入的id或者相关标识");
		return;
	}else
	{
			tgt.html("<option value='loading'>正在加载数据...</option>");
			$.ajax({
			type: "POST",
			url: url,
			data: params,
			success: function(data){
				tgt.html(data);
			},
				error: function(xmlR,msg,other){
				tgt.html("<option value='error'>==请选择==</option>");}
			});
		}
},
/**
*初始化Tab的方法:
$.iTab(); 或者$.iTab(0); 0 为选中第一个标签
*@param selectedIndex 不传入则默认为0 tab标签中选中的索引值（一般传入默认选中的值0,为第一个）
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
	//点击时间绑定
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
  //遍历初始化
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
*作者：王志猛
*负责统一弹出窗口，保持风格一致
*@param url  open窗口要打开的地址
*@param width 窗口代开的宽度，例如12px，请自行带上单位
*@param height 窗口的高度，例如12px,请自行带上单位
*@param top 窗口显示时，离浏览器上边的距离，例如100px，请自行带上单位
*@param left 窗口显示时，离浏览器左边的距离，例如200px，请自行带上单位
*@param resizable 是否可以调整窗口大小,boolean值，true or false
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