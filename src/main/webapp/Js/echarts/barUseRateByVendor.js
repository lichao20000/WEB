$(document).ready(function(){ 
	var repair_vendor_id = $.trim($("select[@name='repair_vendor_id']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    var batchNum = $.trim($("select[@name='batchNum']").val());
    var searchType = $.trim($("input[@name='searchType']").val());
	 $.ajax({  
	        type:'POST',  
	        url:"itms/resource/devRepairTestInfo!getUseRateData.action",
	        data:{                    
	        	repair_vendor_id: repair_vendor_id, 
	        	cityId : cityId,
	        	starttime: starttime,  
	        	endtime : endtime,
	        	batchNum : batchNum,
	        	searchType : searchType
	        }, 
	        dataType:"json",
	       // contentType: "application/x-www-form-urlencoded; charset=GBK", 
	        success:function(data){ 
	        	var userRateL = 0;
	        	var useRateH = 0;
	        	if(data.cities.length > 0){
	        		var myChartsBar = echarts.init(document.getElementById('columechat'));
		        	var cities = data.cities;//[ '江苏省中心', '南京', '苏州', '无锡', '徐州', '常州', '扬州', '镇江', '泰州', '宿迁' ];
		        	var totals = data.total;
		        	var istests = data.istest;
		        	var binds = data.bind;
		        	var uses = data.use;
		        	var noUses = data.noUse;
		        	var rateHigh = [];
		        	var rateLow = [];
		        	var tmp = 0;
		        	for ( var i = 0; i < totals.length; i++) {
		        		tmp = Math.round((1 -  (parseInt(istests[i]) + parseInt(binds[i])) / parseInt(totals[i])) * 10000) /100;
		        		if(tmp < 0){
		        			tmp = 0;
		        		}
		        		if(totals[i] == 0){
	         				tmp = 0;
	         			}
		        		if(tmp >= 90){
			        		useRateH++;
			        		rateHigh.push(tmp);
			        		rateLow.push('-');
			        	}else{
			        		userRateL++;
			        		rateHigh.push('-');
			        		rateLow.push(tmp);
			        	}
		        		
		        	}
		        	
		        	var optionBar = {
		        		grid :{
		        			width:780
		        		},
	        			color: [ '#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed', 
	        			         '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0', 
	        			         '#1e90ff', '#ff6347', '#7b68ee', '#00fa9a', '#ffd700', 
	        			         '#6b8e23', '#ff00ff', '#3cb371', '#b8860b', '#30e0e0'],
		        		title : {
		        			text : '使用率',//'使用率',
		        			x : 'center',
		        			y : 'top'
		        		},
		        		tooltip : {
		        			trigger : 'item',
		        			formatter : function(params) {
		        				var index = -1;
		        				var total;
		        				for(var j = 0; j < cities.length; j++){
		        					if(params[1] == cities[j]){
		        						index = j;
		        					}
		        				}
		        				if (index > -1) {
		        					return "使用数: " + uses[index] + "<br>" + "未使用数: " + noUses[index] + "<br>" + "返修终端总数: " + totals[index];
		        				} else {
		        					return params[1] + ": " + params[2] + ": " + params[3] + " %";
		        				}

		        			}
		        		},
		        		legend : {
		        			x : 'center',
		        			y : 'bottom',
		        			data : [ '<90%', '>90%' ]
		        		},

		        		xAxis : [ {
		        			type : 'category',
		        			data : cities,
		        			margin:1
		        		//margin:1
		        		} ],
		        		yAxis : [ {
		        			type : 'value',
		        			axisLabel : {
		        				show : true,
		        				formatter : '{value} %'
		        			}
//		        			,
//		        			max : 100
		        		} ],
		        		series : [
			        		   {
			        			name : '>90%',
			        			type : 'bar',
			        			stack : '使用率',
			        			barWidth : 25,
			        			itemStyle : {
			        				normal : {
			        					label : {
			        						show : true,
			        						position : 'top',
			        						formatter : '{c} %'
			        					}
			        				}
			        			},
			        			data : rateHigh
			        			},
			        			{
				        			name : '<90%',
				        			type : 'bar',
				        			stack : '使用率',
				        			barWidth : 25,
				        			itemStyle : {
				        				normal : {
				        					label : {
				        						show : true,
				        						position : 'top',
				        						formatter : '{c} %'
				        					}
				        				}
				        			},
				        			data : rateLow
			        			}
			        			
			        		]
		        	};
		        	myChartsBar.setOption(optionBar);
	        	}
	        	$("#userRateLow").html(userRateL);
	        	$("#userRatHigh").html(useRateH);
	        },
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
//	           alert(textStatus); // 调用本次AJAX请求时传递的options参数
//	           alert(errorThrown);
	        }
	    });  
});