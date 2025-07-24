package cn.iszt.protocol.iface.model.pakg.p7102;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.pakg.Package7102;

public class SWPOpen7102
  extends Package7102
{
	private static final long serialVersionUID = 1L; //这个是缺省的
	
  protected byte[] alDate;
  protected byte[] batchNo;
  protected byte[] bofMoney;
  protected byte[] cardSN;
  protected byte[] centerTradeSN;
  protected byte[] dateTime;
  protected byte[] eofMoney;
  protected byte[] phyID;
  protected byte[] phyIDLen;
  protected byte[] sW;
  protected byte[] sztCardNo;
  protected byte[] sztCardType;
  protected byte[] sztResult;
  protected byte[] tAC;
  protected byte[] tradeType;
  
  public byte[] getAlDate()
  {
    return this.alDate;
  }
  
  public byte[] getBatchNo()
  {
    return this.batchNo;
  }
  
  public byte[] getBofMoney()
  {
    return this.bofMoney;
  }
  
  public byte[] getCardSN()
  {
    return this.cardSN;
  }
  
  public byte[] getCenterTradeSN()
  {
    return this.centerTradeSN;
  }
  
  public byte[] getChargeMoney()
  {
    return this.chargeMoney;
  }
  
  public byte[] getDateTime()
  {
    return this.dateTime;
  }
  
  public byte[] getEofMoney()
  {
    return this.eofMoney;
  }
  
  public byte[] getPackage()
  {
    return ByteUtil.merge(new byte[][] { super.getPackage(), this.phyIDLen, this.phyID, this.sztResult, this.sW, this.batchNo, this.centerTradeSN, this.sztCardNo, this.sztPosSN, this.chargeMoney, this.bofMoney, this.eofMoney, this.tradeType, this.cardSN, this.dateTime, this.tAC, this.alDate });
  }
  
  public short getPackageLength()
  {
    int i = super.getPackageLength();
    i = (short)(this.phyIDLen.length + i);
    i = (short)(this.phyID.length + i);
    i = (short)(this.sztResult.length + i);
    i = (short)(this.sW.length + i);
    i = (short)(this.batchNo.length + i);
    i = (short)(this.centerTradeSN.length + i);
    i = (short)(this.sztCardNo.length + i);
    i = (short)(this.sztPosSN.length + i);
    i = (short)(this.chargeMoney.length + i);
    i = (short)(this.bofMoney.length + i);
    i = (short)(this.eofMoney.length + i);
    i = (short)(this.tradeType.length + i);
    i = (short)(this.cardSN.length + i);
    i = (short)(this.dateTime.length + i);
    i = (short)(this.tAC.length + i);
    return (short)(this.alDate.length + i);
  }
  
  public byte[] getPhyID()
  {
    return this.phyID;
  }
  
  public byte[] getPhyIDLen()
  {
    return this.phyIDLen;
  }
  
  public byte[] getSztCardNo()
  {
    return this.sztCardNo;
  }
  
  public byte[] getSztCardType()
  {
    return this.sztCardType;
  }
  
  public byte[] getSztPosSN()
  {
    return this.sztPosSN;
  }
  
  public byte[] getSztResult()
  {
    return this.sztResult;
  }
  
  public byte[] getTradeType()
  {
    return this.tradeType;
  }
  
  public byte[] getsW()
  {
    return this.sW;
  }
  
  public byte[] gettAC()
  {
    return this.tAC;
  }
  
  public String log()
  {
    return null;
  }
  
  public void setAlDate(byte[] paramArrayOfByte)
  {
    this.alDate = paramArrayOfByte;
  }
  
  public void setBatchNo(byte[] paramArrayOfByte)
  {
    this.batchNo = paramArrayOfByte;
  }
  
  public void setBofMoney(byte[] paramArrayOfByte)
  {
    this.bofMoney = paramArrayOfByte;
  }
  
  public void setCardSN(byte[] paramArrayOfByte)
  {
    this.cardSN = paramArrayOfByte;
  }
  
  public void setCenterTradeSN(byte[] paramArrayOfByte)
  {
    this.centerTradeSN = paramArrayOfByte;
  }
  
  public void setChargeMoney(byte[] paramArrayOfByte)
  {
    this.chargeMoney = paramArrayOfByte;
  }
  
  public void setDateTime(byte[] paramArrayOfByte)
  {
    this.dateTime = paramArrayOfByte;
  }
  
  public void setEofMoney(byte[] paramArrayOfByte)
  {
    this.eofMoney = paramArrayOfByte;
  }
  
  public void setPhyID(byte[] paramArrayOfByte)
  {
    this.phyID = paramArrayOfByte;
  }
  
  public void setPhyIDLen(byte[] paramArrayOfByte)
  {
    this.phyIDLen = paramArrayOfByte;
  }
  
  public void setSztCardNo(byte[] paramArrayOfByte)
  {
    this.sztCardNo = paramArrayOfByte;
  }
  
  public void setSztCardType(byte[] paramArrayOfByte)
  {
    this.sztCardType = paramArrayOfByte;
  }
  
  public void setSztPosSN(byte[] paramArrayOfByte)
  {
    this.sztPosSN = paramArrayOfByte;
  }
  
  public void setSztResult(byte[] paramArrayOfByte)
  {
    this.sztResult = paramArrayOfByte;
  }
  
  public void setTradeType(byte[] paramArrayOfByte)
  {
    this.tradeType = paramArrayOfByte;
  }
  
  public void setsW(byte[] paramArrayOfByte)
  {
    this.sW = paramArrayOfByte;
  }
  
  public void settAC(byte[] paramArrayOfByte)
  {
    this.tAC = paramArrayOfByte;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\pakg\p7102\SWPOpen7102.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */