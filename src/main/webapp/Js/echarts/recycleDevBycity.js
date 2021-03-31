$(document)
		.ready(
				function() {
					var cityId = $.trim($("select[@name='cityId']").val());
					var starttime = $.trim($("input[@name='starttime']").val());
					var endtime = $.trim($("input[@name='endtime']").val());

					$
							.ajax({
								type : 'POST',
								url : "itms/resource/recycleDevRate!recycleDevRate.action",
								data : {
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
										var use = data.use;
										var unUse = data.unUse;

										var useList = [];
										var unUseList = [];
										var usemp = 0;
										var unUsemp = 0;
										var useRate = 0;
										var unUseRate = 0;
										for ( var i = 0; i < cities.length; i++) {
											if (i == 0) {
												if (parseInt(total[0]) == 0) {
													useRate = 100;
													unUseRate = 0;

												} else {
													useRate = Math
															.round(parseInt(use[i])
																	/ parseInt(total[i])
																	* 10000) / 100;
													unUseRate = 100 - useRate;
												}
											}
											if (parseInt(total[i]) == 0) {
												usemp = 0;
												unUsemp = 0;
												useList.push(usemp);
												unUseList.push(unUsemp);
											} else {
												usemp = Math
														.round(parseInt(use[i])
																/ parseInt(total[i])
																* 10000) / 100;
												unUsemp = 100 - usemp;
												useList.push(usemp);
												unUseList.push(unUsemp);
											}

										}

										var title = "";
										var showList = [];
										var useshow = null;
										var unUseshow = null;
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
														return (100 - params.value)
																.toFixed(2)
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
										if (cityId == "00") {
											title = "全省各地市回收终端使用率";
										} else {
											title = "本市各地区回收终端使用率";
										}

										showList = useList;
										// 饼图 中间显示哪个
										useshow = labelTop;
										unUseshow = labelBottom;
										// >90%
										var rateHigh = [];
										// <90%
										var rateLow = [];
										for ( var i = 0; i < showList.length; i++) {
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
													for ( var j = 0; j < cities.length; j++) {
														if (params[1] == cities[j]) {
															index = j;
														}
													}
													if (index > -1) {
														return "回收终端使用数: "
																+ use[index]
																+ "<br>"
																+ "回收终端未使用数: "
																+ unUse[index]
																+ "<br>"
																+ "回收终端总数: "
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
												stack : '回收终端使用率',
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
												stack : '回收终端使用率',
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
													name : '回收终端使用率',
													value : useRate,
													itemStyle : useshow
												}, {
													name : '回收终端未使用率',
													value : unUseRate,
													itemStyle : unUseshow
												} ]
											} ]
										};
										myChartsPie.setOption(optionPie);
										myChartsBar.setOption(optionBar);

									}
								},
								error : function(XMLHttpRequest, textStatus,
										errorThrown) {
									// alert(textStatus); //
									// 调用本次AJAX请求时传递的options参数
									// alert(errorThrown);
								}
							});
				});