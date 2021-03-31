package com.linkage.litms.webtopo.common;

import java.util.ArrayList;

public class PM_instance {
  String name, expressionid;
  String mindesc="", maxdesc="", dynadesc="", mutationdesc="";
  int interval, intodb;
  int mintype, mincount, minwarninglevel, minreinstatelevel;
  int maxtype, maxcount, maxwarninglevel, maxreinstatelevel;
  int dynatype, dynacount, beforeday, dynawarninglevel, dynareinstatelevel;
  int mutationtype, mutationcount, mutationwarninglevel, mutationreinstatelevel;
  float maxthres, minthres, dynathres, mutationthres;
  ArrayList devList;
  public PM_instance()
  {

  }

 public ArrayList getDevList()
 {
   return this.devList;
  }

  public int getInterval() {
    return this.interval;
  }

  public int getIntodb() {
    return this.intodb;
  }

  public int getMintype() {
    return this.mintype;
  }

  public int getMincount() {
    return this.mincount;
  }

  public int getMinwarninglevel() {
    return this.minwarninglevel;
  }

  public int getMinreinstatelevel() {
    return this.minreinstatelevel;
  }

  public int getMaxtype() {
    return this.maxtype;
  }

  public int getMaxcount() {
    return this.maxcount;
  }

  public int getMaxwarninglevel() {
    return this.maxwarninglevel;
  }

  public int getMaxreinstatelevel() {
    return this.maxreinstatelevel;
  }

  public int getDynatype() {
    return this.dynatype;
  }

  public int getDynacount() {
    return this.dynacount;
  }

  public int getBeforeday() {
    return this.beforeday;
  }

  public int getDynawarninglevel() {
    return this.dynawarninglevel;
  }

  public int getDynareinstatelevel() {
    return this.dynareinstatelevel;
  }

  public int getMutationtype() {
    return this.mutationtype;
  }

  public int getMutationcount() {
    return this.mutationcount;
  }

  public int getMutationwarninglevel() {
    return this.mutationwarninglevel;
  }

  public int getMutationreinstatelevel() {
    return this.mutationreinstatelevel;
  }

  public float getMaxthres(){
    return maxthres;
  }

  public float getMinthres(){
   return minthres;
 }

 public float getDynathres(){
    return dynathres;
  }

  public float getMutationthres(){
     return mutationthres;
   }


  public String getName() {
    return this.name;
  }

  public String getExpressionid() {
    return this.expressionid;
  }

  public String getMindesc() {
    return this.mindesc;
  }

  public String getMaxdesc() {
    return this.maxdesc;
  }

  public String getDynadesc() {
    return this.dynadesc;
  }

  public String getMutationdesc() {
    return this.mutationdesc;
  }


  public void setName(String name, String expressionid) {
    this.name = name;
    this.expressionid = expressionid;
  }

  public void setDesc(String mindesc, String maxdesc, String dynadesc,
                      String mutationdesc)
  {
    this.mindesc = mindesc;
    this.maxdesc = maxdesc;
    this.dynadesc = dynadesc;
    this.mutationdesc = mutationdesc;
  }

  public void setPerf(int interval, int intodb) {
    this.interval = interval;
    this.intodb = intodb;
  }

  public void setMin(int mintype, int mincount, int minwarninglevel,
                     int minreinstatelevel) {
    this.mintype = mintype;
    this.mincount = mincount;
    this.minwarninglevel = minwarninglevel;
    this.minreinstatelevel = minreinstatelevel;
  }

  public void setMax(int maxtype, int maxcount, int maxwarninglevel,
                     int maxreinstatelevel) {
    this.maxtype = maxtype;
    this.maxcount = maxcount;
    this.maxwarninglevel = maxwarninglevel;
    this.maxreinstatelevel = maxreinstatelevel;
  }


  public void setDyna(int dynatype, int dynacount, int beforeday,
                      int dynawarninglevel, int dynareinstatelevel) {
    this.dynatype = dynatype;
    this.dynacount = dynacount;
    this.beforeday = beforeday;
    this.dynawarninglevel = dynawarninglevel;
    this.dynareinstatelevel = dynareinstatelevel;
  }

  public void setMutation(int mutationtype, int mutationcount,
                          int mutationwarninglevel, int mutationreinstatelevel) {
    this.mutationtype = mutationtype;
    this.mutationcount = mutationcount;
    this.mutationwarninglevel = mutationwarninglevel;
    this.mutationreinstatelevel = mutationreinstatelevel;
  }

  public void setThres(float maxthres, float minthres, float dynathres,
                       float mutationthres) {
    this.maxthres = maxthres;
    this.minthres = minthres;
    this.dynathres = dynathres;
    this.mutationthres = mutationthres;
  }

 public void setDevList(ArrayList list) {
  this.devList = list;
 }




}
