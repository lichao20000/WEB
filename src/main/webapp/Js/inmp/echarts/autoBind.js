function autoBind(year,month){
	var monthRate;
	var yearRate;
	$.ajax({  
		type:"POST",  
		url:"../itms/inmp/homePageData!getAutoBindGaugeData.action" + "?year=" + year + "&month=" + month,  
		dataType:"json",
		success:function(data){  
			
			if(data.year.length > 0){
				var myChartsBar1 = echarts.init(document.getElementById('itmscolumechat1'));
				yearRate = data.year;//[ 1000, 900, 850, 800, 750, 700, 650, 600, 550, 500 ];
				monthRate = data.month;//[ 925, 557, 410, 765, 721, 685, 521, 375, 540, 475 ];
				/*var sum = 0;
				var bindsSum = 0;
				for ( var i = 0; i < totals.length; i++) {
					sum += totals[i];
					bindsSum += binds[i];
					
				}
				var tmp;
				if(sum != 0){
					tmp = Math.round(bindsSum / sum * 10000)/100;
				}else{
					tmp = 0;
				}*/
				
				var option1 = {
						tooltip : {
							formatter: "{a} <br/>{b} : {c}%"
						},
						series : [
						          {
						        	  name:'业务指标',
						        	  type:'gauge',
						        	  splitNumber: 10,       // 分割段数，默认为5
						        	  axisLine: {            // 坐标轴线
						        		  lineStyle: {       // 属性lineStyle控制线条样式
						        			  color: [[0.2, '#228b22'],[0.8, '#48b'],[1, '#ff4500']], 
						        			  width: 10
						        		  }
						        	  },
						        	  axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
						        		  textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
						        			  color: 'auto',
						        			  fontSize : 8
						        		  }
						        	  },
						        	  splitLine: {           // 分隔线
						        		  show: true,        // 默认显示，属性show控制显示与否
						        		  length :10,         // 属性length控制线长
						        		  lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
						        			  color: 'auto'
						        		  }
						        	  },
						        	  pointer : {
						        		  width : 5
						        	  },
						        	  title : {
						        		  show : true,
						        		  offsetCenter: [0, '-20%'],       // x, y，单位px
						        		  textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
						        			  fontWeight: 'bolder'
						        		  }
						        	  },
						        	  detail : {
						        		  formatter:'{value}%',
						        		  textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
						        			  color: 'auto',
						        			  fontWeight: 'bolder',
						        			  offsetCenter: [0, '40%'],
						        			  fontSize : 15
						        		  }
						        	  },
						        	  data:[{value: yearRate, name: year}]
						          }
						          ]
				};
				myChartsBar1.setOption(option1);
				$("#userNum").text(monthRate);
			}
			
			
		},
		error:function (XMLHttpRequest, textStatus, errorThrown) {
		}
	});  
}	
