/**
 * @(#)FluxUnit.java 2006-2-24
 *
 * Copyright 2005 联创科技.版权所有
 */

package com.linkage.litms.flux;

import javax.servlet.http.HttpSession;

import com.linkage.litms.LipossGlobals;

/**
 * <p>Title: 报表系统.链路组报表.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Linkage Corporation.</p>
 * @author Yanhj, Network Management Product Department, ID Card No.5126
 * @version 2.0
 */

public class FluxUnit {

  private String FluxUnit;
  private String UnitName;
  private double confirm;
  private double FluxBase;
  private double Unit = 1000;

  public FluxUnit(String FluxUnit, String UnitName, double confirm,
                  double FluxBase) {
    this.FluxUnit = FluxUnit;
    this.UnitName = UnitName;
    this.confirm = confirm;
    this.FluxBase = FluxBase;
    // TODO 自动生成构造函数存根
  }

//	public static void main(String[] args) {
//	}

  /**
   * @return 返回 confirm。
   */
  public double getConfirm() {
    return confirm;
  }

  /**
   * @param confirm 要设置的 confirm。
   */
  public void setConfirm(double confirm) {
    this.confirm = confirm;
  }

  /**
   * @return 返回 fluxBase。
   */
  public double getFluxBase() {
    return FluxBase;
  }

  /**
   * @param fluxBase 要设置的 fluxBase。
   */
  public void setFluxBase(double fluxBase) {
    FluxBase = fluxBase;
  }

  /**
   * @return 返回 fluxUnit。
   */
  public String getFluxUnit() {
    return FluxUnit;
  }

  /**
   * @param fluxUnit 要设置的 fluxUnit。
   */
  public void setFluxUnit(String fluxUnit) {
    FluxUnit = fluxUnit;
  }

  /**
   * @return 返回 unitName。
   */
  public String getUnitName() {
    return UnitName;
  }

  /**
   * @param unitName 要设置的 unitName。
   */
  public void setUnitName(String unitName) {
    UnitName = unitName;
  }

  public static FluxUnit getFluxUnit(HttpSession session) {
    FluxUnit unit = null;
    HttpSession Session = session;
    if (Session.getAttribute("FluxUnitSession") == null) {
      String FluxUnit = LipossGlobals.getLipossProperty("report.FluxUnit");
      String FluxBase = LipossGlobals.getLipossProperty("report.FluxBase");
      String UnitName = LipossGlobals.getLipossProperty("report.UnitName");
      String confirm = LipossGlobals.getLipossProperty("report.confirm");
      //只要判断有一个为Null,则判断为M的单位
      if (FluxUnit == null || FluxBase == null || UnitName == null || confirm == null) {
        unit = new FluxUnit("(1000*1000*1)", "M", 1, 1024);
        
      }
      else {
        unit = new FluxUnit(FluxUnit, UnitName, Double.parseDouble(confirm),Double.parseDouble(FluxBase));

      }
       Session.setAttribute("FluxUnitSession", unit);

    }
    else {
      unit = (FluxUnit) Session.getAttribute("FluxUnitSession");

    }

    return unit;

  }

  /**
   * @return 返回 unit。
   */
  public double getUnit() {
    return Unit;
  }

  /**
   * @param unit 要设置的 unit。
   */
  public void setUnit(double unit) {
    Unit = unit;
  }
}