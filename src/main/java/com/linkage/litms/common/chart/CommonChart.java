/*
 * 创建日期 2004-10-25
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.linkage.litms.common.chart;

import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import com.linkage.litms.common.database.Cursor;


/**
 * @author dolphin
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public abstract class CommonChart {
	public String title;			//标题
	public String xAxisLabel;		//X轴标签
	public String yAxisLabel;		//Y轴标签
	public String xField = "time"; //默认值为TIME
	public String yField = "value"; //默认值为VALUE
	public int valueType;
	public Cursor[] cursors;		//Chart数据集
	public String[] rowKeys;		//数据集：行标签
	public String[] columnKeys;	//数据集：列标签
	
	//构造函数
	public CommonChart(){
		
	}
	
	/**
	 * @param title
	 * @param xAxisLabel
	 * @param yAxisLabel
	 */
	//设置Chart静态数据
	public void setChartBaseinfo(String title,String xAxisLabel,String yAxisLabel,String xField,String yField,int valueType){
		this.title 		= title;
		this.xAxisLabel 	= xAxisLabel;
		this.yAxisLabel 	= yAxisLabel;
		this.xField 		= xField;
		this.yField 		= yField;
		this.valueType 	= valueType;
	}
	
	public String getXField() {
        return xField;
    }

    public void setXField(String field) {
        xField = field;
    }

    public String getYField() {
        return yField;
    }

    public void setYField(String field) {
        yField = field;
    }

    /**
	 * @param cursors
	 * @param rowKeys
	 * @param columnKeys
	 */
	//设置Chart动态数据
	public void setChartDataset(Cursor[] cursors,String[] rowKeys,String[] columnKeys){
		this.cursors 		= cursors;
		this.rowKeys 		= rowKeys;
		this.columnKeys 	= columnKeys;
	}
	
	/**
	 * @return Dataset
	 */
	//加载数据
	protected abstract void createDataset();
	
	/**
	 * @param dataset
	 * @return String
	 */
	//组建Chart
	public abstract String createChart(HttpSession session,PrintWriter pw);	
}
