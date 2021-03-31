/***************************************************************************************
 *             				性能、流量批量配置JS方法
 ***************************************************************************************
 *@author:benyp(5260) E-mail:benyp@lianchuang.com
 *@since:2008-10-10
 *@version 1.0
 *@remark:主要为性能配置提供统一的JS方法
 ************************************修改记录*******************************************
 *  ID      时间     修改人        修改内容          备注
 *-------------------------------------------------------------------------------------
 *  1   2008-10-13  benyp      修改对float的判断  需要引入jQeuryCheckForm-linkage.js   
 *-------------------------------------------------------------------------------------
 *  2   2008-10-14  benyp      增加查询设备  
 *-------------------------------------------------------------------------------------
 *  3   2008-10-14  benyp      贵州要求增加修改采集时间间隔，同时在查询配置结果时将采集时间间隔显示
 *                             增加的方法：$.MO(),$.showT(),$.changeT()
 *-------------------------------------------------------------------------------------
 *  4   2008-10-30  benyp      增加流量的相关方法
 ***************************************************************************************
 */
jQuery.extend({
	//*******************************************流量*************************************
	checkFluxWarn:function(){
		var data="";
		//端口流入利用率阈值
		if($("input[@name='ifinoctetsbps_max']").attr("readonly") != true){
			data=$("input[@name='ifinoctetsbps_max']").val();
			if($.trim(data)==""){
				alert("端口流入利用率阈值不能为空！");
				$("input[@name='ifinoctetsbps_max']").focus();
				$("input[@name='ifinoctetsbps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("端口流入利用率阈值只能为数字格式!");
				$("input[@name='ifinoctetsbps_max']").focus();
				$("input[@name='ifinoctetsbps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("端口流入利用率阈值数据越界！");
				$("input[@name='ifinoctetsbps_max']").focus();
				$("input[@name='ifinoctetsbps_max']").select();
				return false;
			}	
		}else{
			$("input[@name='ifinoctetsbps_max']").val("-1");
		}
		//端口流出利用率阈值
		if($("input[@name='ifoutoctetsbps_max']").attr("readonly") != true){
			data=$("input[@name='ifoutoctetsbps_max']").val();
			if($.trim(data)==""){
				alert("端口流出利用率阈值不能为空！");
				$("input[@name='ifoutoctetsbps_max']").focus();
				$("input[@name='ifoutoctetsbps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("端口流出利用率阈值只能为数字格式!");
				$("input[@name='ifoutoctetsbps_max']").focus();
				$("input[@name='ifoutoctetsbps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("端口流出利用率阈值数据越界！");
				$("input[@name='ifoutoctetsbps_max']").focus();
				$("input[@name='ifoutoctetsbps_max']").select();
				return false;
			}		
		}else{
			$("input[@name='ifoutoctetsbps_max']").val("-1");
		}
		//端口流入丢包率阈值
		if($("input[@name='ifindiscardspps_max']").attr("readonly") != true){
			data=$("input[@name='ifindiscardspps_max']").val();
			if($.trim(data)==""){
				alert("端口流入丢包率阈值不能为空！");
				$("input[@name='ifindiscardspps_max']").focus();
				$("input[@name='ifindiscardspps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("端口流入丢包率阈值只能为数字格式!");
				$("input[@name='ifindiscardspps_max']").focus();
				$("input[@name='ifindiscardspps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("端口流入丢包率阈值数据越界！");
				$("input[@name='ifindiscardspps_max']").focus();
				$("input[@name='ifindiscardspps_max']").select();
				return false;
			}		
		}else{
			$("input[@name='ifindiscardspps_max']").val("-1");
		}
		//端口流出丢包率阈值
		if($("input[@name='ifoutdiscardspps_max']").attr("readonly") != true){
			data=$("input[@name='ifoutdiscardspps_max']").val();
			if($.trim(data)==""){
				alert("端口流出丢包率阈值不能为空！");
				$("input[@name='ifoutdiscardspps_max']").focus();
				$("input[@name='ifoutdiscardspps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("端口流出丢包率阈值只能为数字格式!");
				$("input[@name='ifoutdiscardspps_max']").focus();
				$("input[@name='ifoutdiscardspps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("端口流出丢包率阈值数据越界！");
				$("input[@name='ifoutdiscardspps_max']").focus();
				$("input[@name='ifoutdiscardspps_max']").select();
				return false;
			}		
		}else{
			$("input[@name='ifoutdiscardspps_max']").val("-1");
		}
		//端口流入错包率阈值
		if($("input[@name='ifinerrorspps_max']").attr("readonly") != true){
			data=$("input[@name='ifinerrorspps_max']").val();
			if($.trim(data)==""){
				alert("端口流入丢包率阈值不能为空！");
				$("input[@name='ifinerrorspps_max']").focus();
				$("input[@name='ifinerrorspps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("端口流入丢包率阈值只能为数字格式!");
				$("input[@name='ifinerrorspps_max']").focus();
				$("input[@name='ifinerrorspps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("端口流入丢包率阈值数据越界！");
				$("input[@name='ifinerrorspps_max']").focus();
				$("input[@name='ifinerrorspps_max']").select();
				return false;
			}	
		}else{
			$("input[@name='ifinerrorspps_max']").val("-1");
		}
		//端口流出错包率阈值
		if($("input[@name='ifouterrorspps_max']").attr("readonly") != true){
			data=$("input[@name='ifouterrorspps_max']").val();
			if($.trim(data)==""){
				alert("端口流出错包率阈值不能为空！");
				$("input[@name='ifouterrorspps_max']").focus();
				$("input[@name='ifouterrorspps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("端口流出错包率阈值只能为数字格式!");
				$("input[@name='ifouterrorspps_max']").focus();
				$("input[@name='ifouterrorspps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("端口流出错包率阈值数据越界！");
				$("input[@name='ifouterrorspps_max']").focus();
				$("input[@name='ifouterrorspps_max']").select();
				return false;
			}	
		}else{
			$("input[@name='ifouterrorspps_max']").val("-1");
		}
		//超出阈值的次数
		if($.trim($("input[@name='warningnum']").val())==""){
			alert("超出阈值的次数不能为空!");
			$("input[@name='warningnum']").focus();
			$("input[@name='warningnum']").select();
			return false;
		}else if(!$.checkNum($("input[@name='warningnum']").val(),'int')){
			alert("超出阈值的次数只能为数字格式！");
			$("input[@name='warningnum']").focus();
			$("input[@name='warningnum']").select();
			return false;
		}
		//江苏专用
		if($("input[@name='ifinoctetsbps_min']").length>0){
			//端口流入利用率阈值
			if($("input[@name='ifinoctetsbps_min']").attr("readonly")!=true){
				data=$("input[@name='ifinoctetsbps_min']").val();
				if($.trim(data)==""){
					alert("端口流入利用率阈值不能为空！");
					$("input[@name='ifinoctetsbps_min']").focus();
					$("input[@name='ifinoctetsbps_min']").select();
					return false;
				}else if(!$.checkNum(data,'float')){
					alert("端口流入利用率阈值只能为数字格式!");
					$("input[@name='ifinoctetsbps_min']").focus();
					$("input[@name='ifinoctetsbps_min']").select();
					return false;
				}else if(data<0 || data>100){
					alert("端口流入利用率阈值数据越界！");
					$("input[@name='ifinoctetsbps_min']").focus();
					$("input[@name='ifinoctetsbps_min']").select();
					return false;
				}	
			}else{
				$("input[@name='ifinoctetsbps_min']").val("-1");
			}
			//端口流出利用率阈值二(%)
			if($("input[@name='ifoutoctetsbps_min']").attr("readonly")!=true){
				data=$("input[@name='ifoutoctetsbps_min']").val();
				if($.trim(data)==""){
					alert("端口流出利用率阈值不能为空！");
					$("input[@name='ifoutoctetsbps_min']").focus();
					$("input[@name='ifoutoctetsbps_min']").select();
					return false;
				}else if(!$.checkNum(data,'float')){
					alert("端口流出利用率阈值只能为数字格式!");
					$("input[@name='ifoutoctetsbps_min']").focus();
					$("input[@name='ifoutoctetsbps_min']").select();
					return false;
				}else if(data<0 || data>100){
					alert("端口流出利用率阈值数据越界！");
					$("input[@name='ifoutoctetsbps_min']").focus();
					$("input[@name='ifoutoctetsbps_min']").select();
					return false;
				}	
			}else{
				$("input[@name='ifoutoctetsbps_min']").val("-1");
			}
			//超出阈值二的次数（发告警）
			if($.trim($("input[@name='warningnum_min']").val())==""){
				alert("超出阈值的次数不能为空!");
				$("input[@name='warningnum_min']").focus();
				$("input[@name='warningnum_min']").select();
				return false;
			}else if(!$.checkNum($("input[@name='warningnum_min']").val(),'int')){
				alert("超出阈值的次数只能为数字格式！");
				$("input[@name='warningnum_min']").focus();
				$("input[@name='warningnum_min']").select();
				return false;
			}
			//动态阈值二(%)
			if($("input[@name='overper_min']").attr("readonly")!=true){
				data=$("input[@name='overper_min']").val();
				if($.trim(data)==""){
					alert("动态阈值二不能为空！");
					$("input[@name='overper_min']").focus();
					$("input[@name='overper_min']").select();
					return false;
				}else if(!$.checkNum(data,'float')){
					alert("动态阈值二只能为数字格式!");
					$("input[@name='overper_min']").focus();
					$("input[@name='overper_min']").select();
					return false;
				}else if(data<0 || data>100){
					alert("动态阈值二数据越界！");
					$("input[@name='overper_min']").focus();
					$("input[@name='overper_min']").select();
					return false;
				}	
			}else{
				$("input[@name='overper_min']").val("-1");
			}
			//超出动态阈值二次数(发告警)
			if($.trim($("input[@name='overnum_min']").val())==""){
				alert("超出动态阈值二次数不能为空!");
				$("input[@name='overnum_min']").focus();
				$("input[@name='overnum_min']").select();
				return false;
			}else if(!$.checkNum($("input[@name='overnum_min']").val(),'int')){
				alert("超出动态阈值二次数只能为数字格式！");
				$("input[@name='overnum_min']").focus();
				$("input[@name='overnum_min']").select();
				return false;
			}
		}
		//超出动态阈值的百分比
		if($("input[@name='overper']").attr("readonly")!=true){
			data=$("input[@name='overper']").val();
			if($.trim(data)==""){
				alert("超出动态阈值的百分比不能为空！");
				$("input[@name='overper']").focus();
				$("input[@name='overper']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("超出动态阈值的百分比只能为数字格式!");
				$("input[@name='overper']").focus();
				$("input[@name='overper']").select();
				return false;
			}else if(data<0 || data>100){
					alert("超出动态阈值的百分比数据越界！");
					$("input[@name='overper']").focus();
					$("input[@name='overper']").select();
					return false;
			}	
		}else{
			$("input[@name='overper']").val("-1");
		}
		//超出动态阈值一的次数(发告警)
		if($("input[@name='overnum']").attr("readonly")!=true){
			if($.trim($("input[@name='overnum']").val())==""){
				alert("超出动态阈值一的次数不能为空！");
				$("input[@name='overnum']").focus();
				$("input[@name='overnum']").select();
				return false;
			}else if(!$.checkNum($("input[@name='overnum']").val(),'int')){
				alert("超出动态阈值一的次数只能为数字格式!");
				$("input[@name='overnum']").focus();
				$("input[@name='overnum']").select();
				return false;
			}	
		}else{
			$("input[@name='overnum']").val("-1");
		}
		//生成动态阈值一的天数(天)
		if($("input[@name='com_day']").attr("readonly")!=true){
			if($.trim($("input[@name='com_day']").val())==""){
				alert("生成动态阈值一的天数不能为空！");
				$("input[@name='com_day']").focus();
				$("input[@name='com_day']").select();
				return false;
			}else if(!$.checkNum($("input[@name='com_day']").val(),'int')){
				alert("生成动态阈值一的天数只能为数字格式!");
				$("input[@name='com_day']").focus();
				$("input[@name='com_day']").select();
				return false;
			}	
		}else{
			$("input[@name='com_day']").val("-1");
		}
		//流入速率变化率阈值
		if($("input[@name='ifinoctets']").attr("readonly")!=true){
			data=$("input[@name='ifinoctets']").val();
			if($.trim(data)==""){
				alert("流入速率变化率阈值不能为空！");
				$("input[@name='ifinoctets']").focus();
				$("input[@name='ifinoctets']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("流入速率变化率阈值只能为数字格式!");
				$("input[@name='ifinoctets']").focus();
				$("input[@name='ifinoctets']").select();
				return false;
			}else if(data<0 || data>100){
				alert("流入速率变化率阈值数据越界！");
				$("input[@name='ifinoctets']").focus();
				$("input[@name='ifinoctets']").select();
				return false;
			}	
		}else{
			$("input[@name='ifinoctets']").val("-1");
		}
		//流出速率变化率阈值
		if($("input[@name='ifoutoctets']").attr("readonly")!=true){
			data=$("input[@name='ifoutoctets']").val();
			if($.trim(data)==""){
				alert("流出速率变化率阈值不能为空！");
				$("input[@name='ifoutoctets']").focus();
				$("input[@name='ifoutoctets']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("流出速率变化率阈值只能为数字格式!");
				$("input[@name='ifoutoctets']").focus();
				$("input[@name='ifoutoctets']").select();
				return false;
			}else if(data<0 || data>100){
				alert("流出速率变化率阈值数据越界！");
				$("input[@name='ifoutoctets']").focus();
				$("input[@name='ifoutoctets']").select();
				return false;
			}		
		}else{
			$("input[@name='ifoutoctets']").val("-1");
		}
		return true;
	},
	//初始化告警页面
	initWarn:function(){
		//端口流入利用率阈值一比较操作符
		$("select[@name='ifinoct_maxtype']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='ifinoctetsbps_max']").attr("readonly",true);
				$("input[@name='ifinoctetsbps_max']").attr("class","onread");
			}else{
				$("input[@name='ifinoctetsbps_max']").attr("readonly",false);
				$("input[@name='ifinoctetsbps_max']").attr("class","");
				$("input[@name='ifinoctetsbps_max']").focus();
				$("input[@name='ifinoctetsbps_max']").select();
			}
		});
		//端口流出利用率阈值一比较操作符
		$("select[@name='ifoutoct_maxtype']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='ifoutoctetsbps_max']").attr("readonly",true);
				$("input[@name='ifoutoctetsbps_max']").attr("class","onread");
			}else{
				$("input[@name='ifoutoctetsbps_max']").attr("readonly",false);
				$("input[@name='ifoutoctetsbps_max']").attr("class","");
				$("input[@name='ifoutoctetsbps_max']").focus();
				$("input[@name='ifoutoctetsbps_max']").select();
			}
		});
		//端口流入利用率阈值二比较操作符
		$("select[@name='ifinoct_mintype']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='ifinoctetsbps_min']").attr("readonly",true);
				$("input[@name='ifinoctetsbps_min']").attr("class","onread");
			}else{
				$("input[@name='ifinoctetsbps_min']").attr("readonly",false);
				$("input[@name='ifinoctetsbps_min']").attr("class","");
				$("input[@name='ifinoctetsbps_min']").focus();
				$("input[@name='ifinoctetsbps_min']").select();
			}
		});
		//端口流出利用率阈值二比较操作符
		$("select[@name='ifoutoct_mintype']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='ifoutoctetsbps_min']").attr("readonly",true);
				$("input[@name='ifoutoctetsbps_min']").attr("class","onread");
			}else{
				$("input[@name='ifoutoctetsbps_min']").attr("readonly",false);
				$("input[@name='ifoutoctetsbps_min']").attr("class","");
				$("input[@name='ifoutoctetsbps_min']").focus();
				$("input[@name='ifoutoctetsbps_min']").select();
			}
		});
		//动态阈值一操作符
		$("select[@name='overmax']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='overper']").attr("readonly",true);
				$("input[@name='overper']").attr("class","onread");
				$("input[@name='overnum']").attr("readonly",true);
				$("input[@name='overnum']").attr("class","onread");
				$("select[@name='overlevel']").attr("disabled",true);				
				$("select[@name='reinoverlevel']").attr("disabled",true);
			}else{
				$("input[@name='overper']").attr("readonly",false);
				$("input[@name='overper']").attr("class","");
				$("input[@name='overper']").focus();
				$("input[@name='overper']").select();
				$("input[@name='overnum']").attr("readonly",false);
				$("input[@name='overnum']").attr("class","");
				$("select[@name='overlevel']").attr("disabled",false);				
				$("select[@name='reinoverlevel']").attr("disabled",false);
			}
		});
		//动态阈值二操作符
		$("select[@name='overmin']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='overper_min']").attr("readonly",true);
				$("input[@name='overper_min']").attr("class","onread");
				$("input[@name='overnum_min']").attr("readonly",true);
				$("input[@name='overnum_min']").attr("class","onread");
				$("select[@name='overlevel_min']").attr("disabled",true);				
				$("select[@name='reinoverlevel_min']").attr("disabled",true);
			}else{
				$("input[@name='overper_min']").attr("readonly",false);
				$("input[@name='overper_min']").attr("class","");
				$("input[@name='overper_min']").focus();
				$("input[@name='overper_min']").select();
				$("input[@name='overnum_min']").attr("readonly",false);
				$("input[@name='overnum_min']").attr("class","");
				$("select[@name='overlevel_min']").attr("disabled",false);				
				$("select[@name='reinoverlevel_min']").attr("disabled",false);
			}
		});
		//端口流入带宽利用率阈值
		$("#c_inmax").click(function(){
			$.targetClick($(this),"ifinoctetsbps_max");
		});
		//端口流出带宽利用率阈值
		$("#c_outmax").click(function(){
			$.targetClick($(this),"ifoutoctetsbps_max");
		});
		//端口流入丢包率阈值
		$("#c_disinmax").click(function(){
			$.targetClick($(this),"ifindiscardspps_max");
		});
		//端口流出丢包率阈值
		$("#c_disoutmax").click(function(){
			$.targetClick($(this),"ifoutdiscardspps_max");
		});
		//端口流入错包率阈值
		$("#c_errinmax").click(function(){
			$.targetClick($(this),"ifinerrorspps_max");
		});
		//端口流出错包率阈值
		$("#c_erroutmax").click(function(){
			$.targetClick($(this),"ifouterrorspps_max");
		});
		//启用动态阈值告警
		$("#c_usedynamic").click(function(){
			if($(this).attr("checked")==true){
				//超出动态阈值的百分比
				$("input[@name='overper']").attr("readonly",false);
				$("input[@name='overper']").attr("class","");
				$("input[@name='overper']").focus();
				$("input[@name='overper']").select();
				//超出百分比次数(发告警)
				$("input[@name='overnum']").attr("readonly",false);
				$("input[@name='overnum']").attr("class","");
				//发出动态阈值告警时的告警级别
				$("select[@name='overlevel']").attr("disabled",false);
				//动态阈值告警恢复级别
				$("select[@name='reinoverlevel']").attr("disabled",false);
				//生成动态阈值一的天数(天)
				$("input[@name='com_day']").attr("readonly",false);
				$("input[@name='com_day']").attr("class","");
			}else{
				//超出动态阈值的百分比
				$("input[@name='overper']").attr("readonly",true);
				$("input[@name='overper']").attr("class","onread");
				//超出百分比次数(发告警)
				$("input[@name='overnum']").attr("readonly",true);
				$("input[@name='overnum']").attr("class","onread");
				//发出动态阈值告警时的告警级别
				$("select[@name='overlevel']").attr("disabled",true);
				//动态阈值告警恢复级别
				$("select[@name='reinoverlevel']").attr("disabled",true);
				//生成动态阈值一的天数(天)
				$("input[@name='com_day']").attr("readonly",true);
				$("input[@name='com_day']").attr("class","onread");
			}
		});
		//启用流入流量突变告警配置
		$("#c_intbflag").click(function(){
			if($(this).attr("checked")==true){
				$("input[@name='intbflag']").val(1)
				//流入速率变化率阈值(%)
				$("input[@name='ifinoctets']").attr("readonly",false);
				$("input[@name='ifinoctets']").attr("class","");
				$("input[@name='ifinoctets']").select();
				$("input[@name='ifinoctets']").focus();
				//流入速率突变告警操作符
				$("select[@name='inoperation']").attr("disabled",false);
				//流入速率突变告警级别
				$("select[@name='inwarninglevel']").attr("disabled",false);
				//流入速率恢复突变告警级别
				$("select[@name='inreinstatelevel']").attr("disabled",false);
			}else{
				$("input[@name='intbflag']").val(0)
				//流入速率变化率阈值(%)
				$("input[@name='ifinoctets']").attr("readonly",true);
				$("input[@name='ifinoctets']").attr("class","onread");
				//流入速率突变告警操作符
				$("select[@name='inoperation']").attr("disabled",true);
				//流入速率突变告警级别
				$("select[@name='inwarninglevel']").attr("disabled",true);
				//流入速率恢复突变告警级别
				$("select[@name='inreinstatelevel']").attr("disabled",true);
			}
		});
		//启用流出流量突变告警配置
		$("#c_outtbflag").click(function(){
			if($(this).attr("checked")==true){
				$("input[@name='outtbflag']").val(1);
				//流出速率变化率阈值(%)
				$("input[@name='ifoutoctets']").attr("readonly",false);
				$("input[@name='ifoutoctets']").attr("class","");
				$("input[@name='ifoutoctets']").select();
				$("input[@name='ifoutoctets']").focus();
				//流出速率突变告警操作符
				$("select[@name='outoperation']").attr("disabled",false);
				//流出速率突变告警级别
				$("select[@name='outwarninglevel']").attr("disabled",false);
				//流出速率恢复突变告警级别
				$("select[@name='outreinstatelevel']").attr("disabled",false);
			}else{
				$("input[@name='outtbflag']").val(0);
				//流出速率变化率阈值(%)
				$("input[@name='ifoutoctets']").attr("readonly",true);
				$("input[@name='ifoutoctets']").attr("class","onread");
				//流出速率突变告警操作符
				$("select[@name='outoperation']").attr("disabled",true);
				//流出速率突变告警级别
				$("select[@name='outwarninglevel']").attr("disabled",true);
				//流出速率恢复突变告警级别
				$("select[@name='outreinstatelevel']").attr("disabled",true);
			}
		});
	},
	//input框点击事件
	targetClick:function(target,name){
		if(target.attr("checked")==true){
			$("input[@name='"+name+"']").attr("readonly",false);
			$("input[@name='"+name+"']").attr("class","");
			$("input[@name='"+name+"']").select();
			$("input[@name='"+name+"']").focus();
		}else{
			$("input[@name='"+name+"']").attr("readonly",true);
			$("input[@name='"+name+"']").attr("class","onread");
		}
	},
	//***********************************************性能**********************************
	//Input OnMouseOut事件
	MO:function(interval,target){
		if(interval==target.val()){
			target.toggle();
			target.parent().find("label").toggle();
		}
	},
	//切换Label和Input
	showT:function(target){
		target.toggle();
		target.parent().find("input").toggle();
	},
	//显示修改时间
	changeT:function(interval,expressionid,device_id,target){
		var lab=target.parent().find("label");
		if(window.confirm("确定要修改采集时间间隔？")){
			$.post(
				"./configPmee!ChangeInterval.action",
				{
					interval:target.val(),
					expressionid:expressionid,
					device_id:device_id
				},
				function(data){
					if(data=="true"){
						alert("修改间隔时间成功！");
						target.toggle();
						lab.html(target.val());
						lab.toggle();					
					}else{
						alert("修改间隔时间失败，请重试！");
					}
				}
			);
		}else{
			target.toggle();
			target.val(interval);
			lab.toggle();
		}
	},
	//根据设备名称、设备IP获取设备
	SelDevByName:function(pmeeflg, gw_type){
		if($.trim($("input[@name='device_name']").val())=="" && $.trim($("input[@name='loopback_ip']").val())==""){
			alert("请输入设备名称或设备IP！");
			$("input[@name='device_name']").focus();
			return false;
		}
		$("tbody[@name='tbody']").html("<tr><td colspan='6' class='even'>正在获取设备，请等待</td></tr>");
		$.post(
			"./configPmee!getDevByNameIP.action",
			{
				device_name:$.cc($("input[@name='device_name']")),
				loopback_ip:$("input[@name='loopback_ip']").val(),
				gw_type:gw_type,
				pmeeflg:pmeeflg
			},
			function(data){
				$("tbody[@name='tbody']").html(data);
				$("#chkall").click(function(){
					var chk=$(this).attr("checked");
					chk=typeof(chk)=="undefined"?false:chk;
					$("input[@name='chk']").attr("checked",chk);
				});
			}
		);
	},
	//根据设备厂商、设备型号查询设备
	SelDevByModel:function(pmeeflg, gw_type){
		if($("select[@name='device_model']").val()==""){
			alert("请选择厂商和型号!");
			$("select[@name='device_model']").focus();
			return false;
		}
		
		$("#chkall").attr("checked",false);
		$("tbody[@name='tbody']").html("<tr><td colspan='6' class='even'>正在获取设备，请等待</td></tr>");
		$.post(
			"./configPmee!getDevByModel.action",
			{
				city_id:$("select[@name='city_id']").val(),
				vendor_id:$("select[@name='vendor_id']").val(),
				device_model:$("select[@name='device_model']").val(),
				gw_type:gw_type,
				pmeeflg:pmeeflg
			},
			function(data){
				$("tbody[@name='tbody']").html(data);
				$("#chkall").click(function(){
					var chk=$(this).attr("checked");
					chk=typeof(chk)=="undefined"?false:chk;
					$("input[@name='chk']").attr("checked",chk);
				});
			}
		);
	},
	//初始化告警显示 type:show tr_fix显示 其他不显示
	init:function(type){
		if(type=="hide"){
			$("tr[@name='tr_fix']").hide();
			$("tr[@name='tr_show']").hide();
		}else{
			$("tr[@name='tr_fix']").show();
		}		
		$("tr[@name='tr_active']").hide();
		$("tr[@name='tr_sudden']").hide();
	},
	//显示、隐藏tab type:第几个tab
	showHide:function(type){
		//全部隐藏
		$("tr[@name='tr_fix']").hide();
		$("tr[@name='tr_active']").hide();
		$("tr[@name='tr_sudden']").hide();
		//class全为out
		$("td[@name='td_fix']").attr("class","mouseout");
		$("td[@name='td_active']").attr("class","mouseout");
		$("td[@name='td_sudden']").attr("class","mouseout");
		//mouseout全为out
		$("td[@name='td_fix']").mouseout(function(){
			$(this).attr("class","mouseout");
		});
		$("td[@name='td_active']").mouseout(function(){
			$(this).attr("class","mouseout");
		});
		$("td[@name='td_sudden']").mouseout(function(){
			$(this).attr("class","mouseout");
		});
		//区分tab
		switch(type){
			case 1:
				$("td[@name='td_fix']").attr("class","mouseon");
				$("td[@name='td_fix']").mouseout(function(){
					$(this).attr("class","mouseon");
				});
				$("tr[@name='tr_fix']").show();
				break;
			case 2:
				$("td[@name='td_active']").attr("class","mouseon");
				$("td[@name='td_active']").mouseout(function(){
					$(this).attr("class","mouseon");
				});
				$("tr[@name='tr_active']").show();
				break;
			case 3:
				$("td[@name='td_sudden']").attr("class","mouseon");
				$("td[@name='td_sudden']").mouseout(function(){
					$(this).attr("class","mouseon");
				});
				$("tr[@name='tr_sudden']").show();
				break;
		}
	},
	//根据Tab显示相应的告警配置项
	showChage:function(){
		if($("td[@name='td_fix']").attr("class")=="mouseon"){
			$("tr[@name='tr_fix']").show();
		}else if($("td[@name='td_active']").attr("class")=="mouseon"){
			$("tr[@name='tr_active']").show();
		}else{
			$("tr[@name='tr_sudden']").show();
		}
	},
	//检查告警配置
	CheckWarn:function(){
		if($("select[@name='mintype']").val()>0 && $("input[@name='minthres']").val()==""){
			alert("启用了固定阀值一，固定阀值一不能为空，请输入!");
			$("input[@name='minthres']").focus();
			return false;
		}else if($("input[@name='minthres']").val()!="" && !$.checkNum($("input[@name='minthres']").val(),'float')){
			alert("固定阀值只能为数字，请重新输入！");
			$("input[@name='minthres']").focus();
			$("input[@name='minthres']").select();
			return false;
		}else if($("input[@name='mincount']").val()==""){
			alert("连续超出阀值的次数不能为空，请重新输入！");
			$("input[@name='mincount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='mincount']").val(),'int')){
			alert("连续超出阀值的次数只能为数字格式，请重新输入！");
			$("input[@name='mincount']").focus();
			$("input[@name='mincount']").select();
			return false;
		}else if($("select[@name='maxtype']").val()>0 && $("input[@name='maxthres']").val()==""){
			alert("固定阀值不能为空，请输入！");
			$("input[@name='maxthres']").focus();
			return false;
		}else if($("input[@name='maxthres']").val()!="" && !$.checkNum($("input[@name='maxthres']").val(),'float')){
			alert("固定阀值只能为数字格式，请重新输入！");
			$("input[@name='maxthres']").focus();
			$("input[@name='maxthres']").select();
			return false;
		}else if($("input[@name='maxcount']").val()==""){
			alert("连续超出阀值的次数不能为空，请输入！");
			$("input[@name='maxcount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='maxcount']").val(),'int')){
			alert("连续超出阀值的次数只能为数字格式，请重新输入！");
			$("input[@name='maxcount']").focus();
			$("input[@name='maxcount']").select();
			return false;
		}else if($("input[@name='beforeday']").val()==""){
			alert("请输入数据基准值！");
			$("input[@name='beforeday']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='beforeday']").val(),'int')){
			alert("数据基准值只能为数字格式，请重新输入！");
			$("input[@name='beforeday']").focus();
			$("input[@name='beforeday']").select();
			return false;
		}else if($("select[@name='dynatype']").val()>0 && $("input[@name='dynathres']").val()==""){
			alert("阀值百分比不能为空，请输入！");
			$("input[@name='dynathres']").focus();
			return false;
		}else if($("input[@name='dynathres']").val()!="" && !$.checkNum($("input[@name='dynathres']").val(),'float')){
			alert("阈值百分比只能为数字格式，请重新输入");
			$("input[@name='dynathres']").focus();
			$("input[@name='dynathres']").select();
			return false;
		}else if($("select[@name='mutationtype']").val()>0 && $("input[@name='mutationthres']").val()==""){
			alert("突变阀值超出百分比不能为空，请输入!");
			$("input[@name='mutationthres']").focus();
			return false;
		}else if($("input[@name='mutationthres']").val()!="" && !$.checkNum($("input[@name='mutationthres']").val(),'float')){
			alert("百分比只能为数字格式，请重新输入!");
			$("input[@name='mutationthres']").focus();
			$("input[@name='mutationthres']").select();
			return false;
		}else if($("input[@name='mutationcount']").val()==""){
			alert("达到阀值百分比次数不能为空，请输入!");
			$("input[@name='mutationcount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='mutationcount']").val(),'int')){
			alert("次数只能为数字格式，请重新输入");
			$("input[@name='mutationcount']").focus();
			$("input[@name='mutationcount']").select();
			return false;
		}else{
			return true;
		}
	}
});