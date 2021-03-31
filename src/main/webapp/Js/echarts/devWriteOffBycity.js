$(document)
		.ready(
				function() {
					var writeOffType = $.trim($("select[@name='writeOffType']")
							.val());
					var cityId = $.trim($("select[@name='cityId']").val());
					var starttime = $.trim($("input[@name='starttime']").val());
					var endtime = $.trim($("input[@name='endtime']").val());

					$
							.ajax({
								type : 'POST',
								url : "itms/resource/devWriteOffRate!writeOffRate.action",
								data : {
									writeOffType : writeOffType,
									cityId : cityId,
									starttime : starttime,
									endtime : endtime
								},
								dataType : "json",
								success : function(data) {
									if (data.cities.length > 0) {
										var myChartsBar = echarts.init(document
												.getElementById('bar'));
										var myChartsPie = echarts.init(document
												.getElementById('pie'));
										var cities = data.cities;// [
										var total = data.total;
										var auto = data.auto;
										var man = data.man;

										var autoList = [];
										var manList = [];
										var automp = 0;
										var manmp = 0;
										var autoRate = 0;
										var manRate = 0;
										for (var i = 0; i < cities.length; i++) {
											if (i == 0) {
												if (parseInt(total[0]) == 0) {
													if (writeOffType == 1) {
														autoRate = 0;
														manRate = 100;
													} else {
														autoRate = 100;
														manRate = 0;
													}

												} else {
													autoRate = Math
															.round(parseInt(auto[i])
																	/ parseInt(total[i])
																	* 10000) / 100;
													manRate = (100 - autoRate).toFixed(2);
												}
											}
											if (parseInt(total[i]) == 0) {
												automp = 0;
												manmp = 0;
												autoList.push(automp);
												manList.push(manmp);
											} else {
												automp = Math
														.round(parseInt(auto[i])
																/ parseInt(total[i])
																* 10000) / 100;
												manmp = (100 - automp).toFixed(2);
												autoList.push(automp);
												manList.push(manmp);
											}

										}

										var title = "";
										var showList = [];
										var autoshow = null;
										var manshow = null;
										var labelTop = {
											normal : {
												label : {
													show : true,
													position : 'center',
													formatter : '{b}',

													textStyle : {
														baseline : 'bottom',
														color : '#4985c9',
														fontSize : 12
													}
												},
												labelLine : {
													show : false
												}
											}
										};
										var labelFromatter = {
											normal : {
												label : {
													formatter : function(params) {
														return (100
																- params.value).toFixed(2)
																+ '%';
													},
													textStyle : {
														baseline : 'top',
														color : '#4985c9',
														fontSize : 24
													}
												}
											}
										};
										var labelBottom = {
											normal : {
												color : '#ccc',
												label : {
													show : true,
													position : 'center'
												},
												labelLine : {
													show : false
												}
											},
											emphasis : {
												color : 'rgba(0,0,0,0)'
											}
										};
										if (writeOffType == 1) {
											if (cityId == "00") {
												title = "全省各地市自动核销率";
											} else {
												title = "本市各地区自动核销率";
											}

											showList = autoList;
											// 饼图 中间显示哪个
											autoshow = labelTop;
											manshow = labelBottom;
										} else {
											if (cityId == "00") {
												title = "全省各地市人工核销率";
											} else {
												title = "本市各地区人工核销率";
											}
											showList = manList;
											autoshow = labelBottom;
											manshow = labelTop;
										}
										// >90%
										var rateHigh = [];
										// <90%
										var rateLow = [];
										for(var i = 0; i < showList.length; i++){
											if (showList[i] >= 90) {
												rateHigh.push(showList[i]);
												rateLow.push('-');
											} else {
												rateHigh.push('-');
												rateLow.push(showList[i]);
											}
										}
										var optionBar = {
											color : [ '#FF7F50', '#87CEFA' ],
											title : {
												text : title,
												x : 'center',
												textStyle : {
													fontSize : 18,
													fontWeight : 'normal',
													color : '#4985c9'
												}
											},
											tooltip : {
												trigger : 'item',
												formatter : function(params) {
													var index = -1;
													for (var j = 0; j < cities.length; j++) {
														if (params[1] == cities[j]) {
															index = j;
														}
													}
													if (index > -1) {
														return "自动核销数: "
																+ auto[index]
																+ "<br>"
																+ "人工核销数: "
																+ man[index]
																+ "<br>"
																+ "核销总数: "
																+ total[index];
													} else {
														return params[1] + ": "
																+ params[2]
																+ ": "
																+ params[3]
																+ ": "
																+ params[4]
																+ ": "
																+ params[5]
																+ " %";
													}

												}
											},
											legend : {
												x : 'center',
												y : 'bottom',
												data : [ '<90%', '>90%' ]
											},
											grid : {
												x : '60',
												y : '60',
												x2 : '150',
												y2 : '70',
												borderColor : '#EFEFEF'
											},
											xAxis : [ {
												type : 'category',
												data : cities,
												axisLabel : {
													interval : 0
												}
											} ],
											yAxis : [ {
												type : 'value',
												splitLine : {
													lineStyle : {
														color : '#EFEFEF'
													}
												},
												axisLabel : {
													show : true,
													formatter : '{value} %'
												},
												splitArea : {
													show : true,
													areaStyle : {
														color : [ '#F2f2f2',
																'#FFFFFF'

														]
													}
												}

											// ,
											// max : 100
											} ],

											series : [ {
												name : '>90%',
												type : 'bar',
												barWidth : 30,
												stack : '核销率',
												itemStyle : {
													normal : {
														label : {
															show : true,

															formatter : '{c} %'
														}
													}
												},
												data : rateHigh
											}, {
												name : '<90%',
												type : 'bar',
												barWidth : 30,
												stack : '核销率',
												itemStyle : {
													normal : {
														label : {
															show : true,
															formatter : '{c} %'
														}
													}
												},
												data : rateLow
											}

											]
										};

										var optionPie = {
											color : [ '#5AB1EF' ],
											tooltip : {
												formatter : function(params) {
													return params.name + ' '
															+ params.value
															+ '%';
												}
											},
											series : [ {
												type : 'pie',
												center : [ '30%', '58%' ],
												radius : [ '40%', '55%' ],
												x : '0%',
												itemStyle : labelFromatter,
												data : [ {
													name : '自动核销率',
													value : autoRate,
													itemStyle : autoshow
												}, {
													name : '人工核销率',
													value : manRate,
													itemStyle : manshow
												} ]
											} ]
										};
										myChartsPie.setOption(optionPie);
										myChartsBar.setOption(optionBar);

									}
								},
								error : function(XMLHttpRequest, textStatus,
										errorThrown) {
									// alert(textStatus); // 调用本次AJAX请求时传递的options参数
									// alert(errorThrown);
								}
							});
				});