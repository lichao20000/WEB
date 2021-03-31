$(document)
		.ready(
				function() {
					var cityId = $.trim($("select[@name='cityId']").val());
					var starttime = $.trim($("input[@name='starttime']").val());
					var endtime = $.trim($("input[@name='endtime']").val());

					$
							.ajax({
								type : 'POST',
								url : "itms/resource/devWriteOffRate!devChange.action",
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
										var normal = data.normal;
										var nonormal = data.nonormal;

										// >90%
										var rateHigh = [];
										// <90%
										var rateLow = [];

										var normalmp = 0;

										var normalRate = 0;
										var nonormalRate = 0;
										for (var i = 0; i < cities.length; i++) {
											if (i == 0) {
												if (parseInt(total[0]) == 0) {
													normalRate = 0;
													nonormalRate = 100;
												} else {
													normalRate = Math
															.round(parseInt(normal[i])
																	/ parseInt(total[i])
																	* 10000) / 100;
													nonormalRate = 100 - normalRate;
												}
											}
											if (parseInt(total[i]) == 0) {
												normalmp = 0;
											} else {
												normalmp = Math
														.round(parseInt(normal[i])
																/ parseInt(total[i])
																* 10000) / 100;

											}

											if (normalmp >= 90) {
												rateHigh.push(normalmp);
												rateLow.push('-');
											} else {
												rateHigh.push('-');
												rateLow.push(normalmp);
											}
										}

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

										var optionBar = {
											color : [ '#FF7F50', '#87CEFA' ],
											title : {
												text : "终端更换规范率统计",
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
														return "更换规范数: "
																+ normal[index]
																+ "<br>"
																+ "更换不规范数: "
																+ nonormal[index]
																+ "<br>"
																+ "更换总数: "
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

											// series : [ {
											// name : "终端更换规范率",
											// type : 'bar',
											// barWidth : 30,
											// itemStyle : {
											// normal : {
											// label : {
											// show : true,
											// formatter : '{c} %',
											// textStyle : {
											// color : '#333333'
											// }
											// }
											// }
											// },
											// data : normalList
											// }
											//
											// ]
											// };

											series : [ {
												name : '>90%',
												type : 'bar',
												barWidth : 30,
												stack : '规范率',
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
												stack : '规范率',
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
													name : '更换规范率',
													value : normalRate,
													itemStyle : labelTop
												}, {
													name : '更换不规范率',
													value : nonormalRate,
													itemStyle : labelBottom
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