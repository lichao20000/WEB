/*
 **********************************************
 *
 *			BBMS　性能配置
 *
 * @author:benyp
 * @E-mail:benyp@lianchuang.com
 * @Version 1.0
 * @Since 2008.5.22
 * Copyright (c):联创科技 网管产品部
 *
 **********************************************
 */
jQuery.extend({
	//查询方式切换
	ToggleQuery:function(type){
		switch(type){
			case "0"://根据属地和型号
				$("#tr1").show();
				$("#tr2").show();
				$("#tr3").hide();
				$("#tr4").hide();
				break;
			case "1"://根据用户
				$("#tr1").hide();
				$("#tr2").hide();
				$("#tr3").show();
				$("#tr4").hide();
				break;
			case "2"://根据设备
				$("#tr1").hide();
				$("#tr2").hide();
				$("#tr3").hide();
				$("#tr4").show();
				break;
		}
	},
	//厂商change事件
	VendorChange:function(vendor_id){
		$.post(
			"DevPmeeConfig!getModel.action",
			{
				vendor_id:vendor_id
			},
			function(data){
				$("select[@name='device_model']").html(data);
			}
		);
	},
	//设备型号change事件
	ModelChange:function(vendor_id,model){
		$.post(
			"DevPmeeConfig!getVersion.action",
			{
				vendor_id:vendor_id,
				device_model:model
			},
			function(data){
				$("select[@name='version']").html(data);
			}
		);
	},
	//设备版本change事件
	VersionChange:function(city_id,vendor_id,version){
		$.post(
			"DevPmeeConfig!getDevByModel.action",
			{
				vendor_id:vendor_id,
				version:version,
				city_id:city_id
			},
			function(data){
				$("#div_device span").html(data);
			}
		);
	},
	//测试是否已经配置
	TestConfig:function(pm_name,expressionid,device_id){
		$.post(
			"DevPmeeConfig!IsConfig.action",
			{
				expressionid:expressionid,
				device_id:device_id
			},
			function(data){
				if(data==""){
					$.Config(device_id,expressionid,pm_name);
				}else{
					if(window.confirm(data+"\n是否继续配置?")){
						$.Config(device_id,expressionid,pm_name);
					}
				}
			}
		);
	},
	Config:function(device_id,expressionid,pm_name){
		$.post(
			"DevPmeeConfig!ConfigDev.action",
			{
				device_id:device_id,
				serial:getSerial(),
				expressionid:expressionid,
				interval:$("input[@name='samp_distance']").val(),
				pm_name:pm_name,
				ruku:$("select[@name='ruku']").val()
				
			},
			function(data){
				var tmp=data.split("-/-");
				alert(tmp[0]);
				$.post(
					"DevPmeeConfig!ConfigPmee.action",
					{
						device_id:device_id,
						serial:tmp[1],
						expressionid:expressionid,
						interval:$("input[@name='samp_distance']").val(),
						pm_name:pm_name,
						ruku:$("select[@name='ruku']").val()
					}
				);
			}
		);
	}
	
});