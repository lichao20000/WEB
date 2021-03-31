$(document).ready(function(){ 
	var repair_vendor_id = $.trim($("select[@name='repair_vendor_id']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    var batchNum = $.trim($("select[@name='batchNum']").val());
    var searchType = $.trim($("input[@name='searchType']").val());
	 $.ajax({  
	        type:'POST',  
	        url:"itms/resource/devRepairTestInfo!getQualifiedRate.action",
	        data:{                    
	        	repair_vendor_id: repair_vendor_id, 
	        	cityId:cityId,
	        	starttime: starttime,  
	        	endtime : endtime,
	        	batchNum : batchNum,
	        	searchType : searchType
	        }, 
	        dataType:"json",
	       // contentType: "application/x-www-form-urlencoded; charset=GBK", 
	        success:function(data){ 
	        	var qualifedL = 0;
	        	var qualifedH = 0;
	        	if(data.vendors.length > 0){
	        		var myChartsBar = echarts.init(document.getElementById('columechat'));
		        	var vendors = data.vendors;
		        	var qualifyCounts = data.qualifyCount;
		        	var nobindCounts = data.nobindCount;
		        	var bindCounts = data.bindCount;
		        	//var addBussCounts = data.addBussCount;
		        	var istestCounts = data.istestCount;
		        	var totalCounts = data.totalCount;
		        	var qualifiedCounts = data.qualifiedCount;
		        	var noQualifiedCounts = data.noQualifiedCount;
		        	var rateHigh = [];
		        	var rateLow = [];
		        	var tmp = 0;
		        	for ( var i = 0; i < vendors.length; i++) {
		        		var div = parseInt(nobindCounts[i]) + parseInt(bindCounts[i]) - parseInt(istestCounts[i]);
		        		tmp = Math.round(parseInt(qualifyCounts[i]) / div * 10000) / 100;
		        		if(div == 0){
		        			tmp = 0;
		        		}
		        		if(tmp >= 90){
			        		qualifedH++;
			        		rateHigh.push(tmp);
			        		rateLow.push('-');
			        	}else{
			        		qualifedL++;
			        		rateHigh.push('-');
			        		rateLow.push(tmp);
			        	}
		        		
		        	}
		        	
		        	var optionBar = {
		        		grid :{
		        			width:780
		        		},
	        			color: ['#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed', 
	        			         '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0', 
	        			         '#1e90ff', '#ff6347', '#7b68ee', '#00fa9a', '#ffd700', 
	        			         '#6b8e23', '#ff00ff', '#3cb371', '#b8860b', '#30e0e0'],
		        		title : {
		        			text : '合格率',
		        			x : 'center',
		        			y : 'top'
		        		},
		        		tooltip : {
		        			trigger : 'item',
		        			formatter : function(params) {
		        				var index = -1;
		        				var total;
		        				for(var j = 0; j < vendors.length; j++){
		        					if(params[1] == vendors[j]){
		        						index = j;
		        					}
		        				}
		        				if (index > -1) {
		        					return "合格数: " + qualifiedCounts[index] + "<br>" + 
		        					"未合格数: " + noQualifiedCounts[index] + "<br>" + "使用终端总数: " + totalCounts[index];
		        				} else {
		        					return params[1] + ": " + params[2] + ": " + params[3] + ": " + params[4] + ": " + params[5] + " %";
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
		        			data : vendors,
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
			        			stack : '合格率',
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
				        			stack : '合格率',
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
	        	$("#userRateLow").html(qualifedL);
	        	$("#userRatHigh").html(qualifedH);
	        },
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
//	           alert(textStatus); // 调用本次AJAX请求时传递的options参数
//	           alert(errorThrown);
	        }
	    });  
});