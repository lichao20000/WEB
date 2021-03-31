package com.linkage.litms.webtopo.warn;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class WarnRuleBean {
  public WarnRuleBean() {
  }
  private int priority;
  private String name;
  private String desc;
  private String content;
  private String result;

  public void setPriority(int priority)
  {
    this.priority=priority;
  }

  public void setName(String name)
  {
    this.name=name;
  }

  public void setDesc(String desc)
  {
    this.desc=desc;
  }

  public void setContent(String content)
  {
    this.content=content;
  }

  public void setResult(String result)
  {
    this.result=result;
  }

  public int getPriority()
  {
    return priority;
  }

  public String getName()
  {
    return name;
  }

  public String getDesc()
  {
    return desc;
  }

  public String getContent()
  {
    return content;
  }


  public String getResult()
  {
    return result;
  }
}