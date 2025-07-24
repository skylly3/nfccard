package cn.iszt.protocol.iface.model.pakg.p7102;

import java.io.Serializable;

public class CardTransactionRecord
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String date;
  private String money;
  private String tpye;
  
  public String getDate()
  {
    return this.date;
  }
  
  public String getMoney()
  {
    return this.money;
  }
  
  public String getTpye()
  {
    return this.tpye;
  }
  
  public void setDate(String paramString)
  {
    this.date = paramString;
  }
  
  public void setMoney(String paramString)
  {
    this.money = paramString;
  }
  
  public void setTpye(String paramString)
  {
    this.tpye = paramString;
  }
}


 