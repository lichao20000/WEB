package com.linkage.litms.vipms.flux;

public class PortJudgeAttr {
  private String name = "";
  private String value = "";
  private String desc = "";
  private String order = "0";
  private String model="0";
  public PortJudgeAttr() {

  }
  
  public String getModel()
  {
	  return model;
  }
  
  public void setModel(String _model)
  {
	  this.model=_model;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}