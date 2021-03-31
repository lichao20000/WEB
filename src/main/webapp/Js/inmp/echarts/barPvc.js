function barPvc(year3,month3,flag){
	 $.ajax({  
         type:"POST",  
         url:"../itms/inmp/homePageData!getPvcBarData.action" + "?year=" + year3 + "&month=" + month3 + "&sort=" + flag,  
         dataType:"json",
         success:function(data){
        	var pvcLow = 0;
        	var pvcHigh = 0;
         	if(data.cities.length > 0){
         		var myChartsBar3 = echarts.init(document.getElementById('columechat3'));
         		var cities = data.cities;//[ '江苏省中心', '南京', '苏州', '无锡', '徐州', '常州', '扬州', '镇江', '泰州', '宿迁' ];
         		var deploy = data.deploy;//[ 9810, 9632, 9254, 9789, 9788, 9655, 9612, 7925, 9762, 9989 ];
         		var nodeploy = data.nodeploy;//[243,231,567,145,321,426,725,213,243,433];
         		var rateHigh = [];
         		var rateLow = [];
         		for ( var i = 0; i < deploy.length; i++) {
         			var tmp = Math.round((deploy[i] / (deploy[i] + nodeploy[i])) * 10000)/100;
         			if((deploy[i] + nodeploy[i]) == 0){
         				tmp = 0;
         			}
         			if(tmp >= 90){
         				pvcHigh++;
         				rateHigh.push(tmp);
         				rateLow.push('-');
         			}else{
         				pvcLow++;
         				rateHigh.push('-');
         				rateLow.push(tmp);
         			}
         		}
         		var optionBar3 = {
         				grid :{
		        			width:900
		        		},
         				color: ['#70c5f1','#0095e7','#ff7f50','#60d22e','#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
        			            '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
        			            '#1e90ff','#ff6347','#7b68ee','#00fa9a','#ffd700',
        			            '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'],
         				title : {
         					text : '',//'部署率',
         					x : 'center',
         					y : 'top'
         				},
         				tooltip : {
         					trigger : 'item',
         					formatter : function(params) {
         						var index;
         						for(var j = 0; j < cities.length; j++){
		        					if(params[1] == cities[j]){
		        						index = j;
		        					}
		        				}
		        				if (index > -1) {
         							return "已部署数: " + deploy[index] + " <br>" + "未部署数: " + nodeploy[index];
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
	         				data : cities
	         			} ],
	         			yAxis : [ {
	         				type : 'value',
	         				axisLabel : {
	         					show : true,
	         					formatter : '{value} %'
	         				}
	//         				,
	//         				max : 100
	         			} ],
	         			series : [
	      	         			{
	      	         				name : '>90%',
	      	         				type : 'bar',
	      	         				stack : '部署率',
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
	      	         			},{
	      	         				name : '<90%',
	      	         				type : 'bar',
	      	         				stack : '部署率',
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
		         		myChartsBar3.setOption(optionBar3);
         	}
         	$("#pvcLow").html(pvcLow);
        	$("#pvcHigh").html(pvcHigh);
             
         },
         error:function (XMLHttpRequest, textStatus, errorThrown) {
             // 通常 textStatus 和 errorThrown 之中
             // 只有一个会包含信息
//            alert(textStatus); // 调用本次AJAX请求时传递的options参数
//            alert(errorThrown);
         }
     });
}