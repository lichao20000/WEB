package com.linkage.litms.webtopo.warn;

public class WarnResult {
  public WarnResult() {
  }
  private String device_id;
    private String device_name;
    private int w0 = 0;
    private int w1 = 0;
    private int w2 = 0;
    private int w3 = 0;
    private int w4 = 0;
    private int w5 = 0;
    public String getDevice_id() {
      return device_id;
    }

    public void setDevice_id(String device_id) {
      this.device_id = device_id;
    }



    public String getDevice_name() {
      return device_name;
    }

    public void setDevice_name(String device_name) {
      this.device_name = device_name;
    }

    public int getW0() {
     return w0;
   }

   public void setW0(int w0) {
     this.w1 += w0;
   }


    public int getW1() {
      return w1;
    }

    public void setW1(int w1) {
      this.w1 += w1;
    }

    public int getW2() {
      return w2;
    }

    public void setW2(int w2) {
      this.w2 += w2;
    }

    public int getW3() {
      return w3;
    }

    public void setW3(int w3) {
      this.w3 += w3;
    }

    public int getW4() {
      return w4;
    }

    public void setW4(int w4) {
      this.w4 += w4;
    }

    public int getW5() {
      return w5;
    }

    public void setW5(int w5) {
      this.w5 += w5;
    }

}