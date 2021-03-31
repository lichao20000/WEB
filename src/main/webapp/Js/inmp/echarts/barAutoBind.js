function barAutoBind(year1,month1,flag){ 
	 $.ajax({  
	        type:"POST",  
	        url:"../itms/inmp/homePageData!getAutoBindBarData.action" + "?year=" + year1 + "&month=" + month1 + "&sort=" + flag,  
	        dataType:"json",
	        success:function(data){  
	        	var bindL = 0;
	        	var bindH = 0;
	        	
	        	if(data.cities.length > 0){
	        		var myChartsBar1 = echarts.init(document.getElementById('columechat1'));
		        	var cities = data.cities;//[ '江苏省中心', '南京', '苏州', '无锡', '徐州', '常州', '扬州', '镇江', '泰州', '宿迁' ];
		        	var totals = data.total;//[ 1000, 900, 850, 800, 750, 700, 650, 600, 550, 500 ];
		        	var binds = data.bind;//[ 925, 557, 410, 765, 721, 685, 521, 375, 540, 475 ];
		        	var rateHigh = [];
		        	var rateLow = [];
		        	for ( var i = 0; i < totals.length; i++) {
		        		var tmp = Math.round(binds[i] / totals[i] * 10000)/100;
		        		if(totals[i] == 0){
	         				tmp = 0;
	         			}
		        		if(tmp >= 90){
			        		bindH++;
			        		rateHigh.push(tmp);
			        		rateLow.push('-');
			        	}else{
			        		bindL++;
			        		rateHigh.push('-');
			        		rateLow.push(tmp);
			        	}
		        		
		        	}
		        	
		        	var optionBar1 = {
		        		grid :{
		        			width:900
		        		},
	        			color: ['#70c5f1','#0095e7','#ffac2c','#a8c6f0','#ff7f50','#60d22e','#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
        			            '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
        			            '#1e90ff','#ff6347','#7b68ee','#00fa9a','#ffd700',
        			            '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'],
		        		title : {
		        			text : '',//'自动绑定率',
		        			x : 'center',
		        			y : 'top'
		        		},
		        		tooltip : {
		        			trigger : 'item',
		        			formatter : function(params) {
		        				var index = -1;
		        				var total, bind;
		        				for(var j = 0; j < cities.length; j++){
		        					if(params[1] == cities[j]){
		        						index = j;
		        					}
		        				}
		        				
		        				if (index > -1) {

		        					return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总开户数: " + totals[index] + "<br>" + "已绑定户数: " + binds[index];
		        				} else {
		        					return params[1] + ": " + params[2] + " %";
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
		        			margin:10
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
			        			stack : '自动绑定率',
			        			barWidth : 30,
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
				        			stack : '自动绑定率',
				        			barWidth : 30,
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
		        	myChartsBar1.setOption(optionBar1);
	        	}
	        	$("#bindLow").html(bindL);
	        	$("#bindHigh").html(bindH);
	        },
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
//	           alert(textStatus); // 调用本次AJAX请求时传递的options参数
//	           alert(errorThrown);
	        }
	    });  
}