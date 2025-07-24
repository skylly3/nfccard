package cn.iszt.protocol.iface.model.pakg;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.IFaceMessage;

public abstract class Package7102
  extends IFaceMessage
{
  private static final long serialVersionUID = 960903667197154898L;
  protected byte[] businessType;
  protected byte[] centerSN;
  protected byte[] chargeMoney;
  protected byte[] lxrBusinessSN;
  protected byte[] messageType = ByteUtil.codeBCD("7102");
  protected byte[] result;
  protected byte[] retry;
  protected byte[] step;
  protected byte[] sztPosSN;
  private byte[] sztResult;
  protected byte[] tradeDate;
  protected byte[] tradeTime;
  
  public byte[] getBusinessType()
  {
    return this.businessType;
  }
  
  public byte[] getCenterSN()
  {
    return this.centerSN;
  }
  
  public byte[] getChargeMoney()
  {
    return this.chargeMoney;
  }
  
  public byte[] getLxrBusinessSN()
  {
    return this.lxrBusinessSN;
  }
  
  public byte[] getMessageType()
  {
    return this.messageType;
  }
  
  public byte[] getPackage()
  {
    return ByteUtil.merge(new byte[][] { ByteUtil.shortToBytes(getPackageLength(), false), super.getPackage(), this.messageType, this.lxrBusinessSN, this.retry, this.businessType, this.result, this.step, this.chargeMoney, this.tradeTime, this.tradeDate, this.centerSN, this.sztPosSN });
  }
  
  public short getPackageLength()
  {
    return (short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)(super.getPackageLength() + 2) + 16) + 1) + 3) + 4) + 1) + 4) + 3) + 2) + 12) + 5);
  }
  
  public byte[] getResult()
  {
    return this.result;
  }
  
  public byte[] getRetry()
  {
    return this.retry;
  }
  
  public byte[] getStep()
  {
    return this.step;
  }
  
  public byte[] getSztPosSN()
  {
    return this.sztPosSN;
  }
  
  public byte[] getSztResult()
  {
    return this.sztResult;
  }
  
  public byte[] getTradeDate()
  {
    return this.tradeDate;
  }
  
  public byte[] getTradeTime()
  {
    return this.tradeTime;
  }
  
  public void setBusinessType(byte[] paramArrayOfByte)
  {
    this.businessType = paramArrayOfByte;
  }
  
  public void setCenterSN(byte[] paramArrayOfByte)
  {
    this.centerSN = paramArrayOfByte;
  }
  
  public void setChargeMoney(byte[] paramArrayOfByte)
  {
    this.chargeMoney = paramArrayOfByte;
  }
  
  public void setLxrBusinessSN(byte[] paramArrayOfByte)
  {
    this.lxrBusinessSN = paramArrayOfByte;
  }
  
  public void setMessageType(byte[] paramArrayOfByte)
  {
    this.messageType = paramArrayOfByte;
  }
  
  public void setResult(byte[] paramArrayOfByte)
  {
    this.result = paramArrayOfByte;
  }
  
  public void setRetry(byte[] paramArrayOfByte)
  {
    this.retry = paramArrayOfByte;
  }
  
  public void setStep(byte[] paramArrayOfByte)
  {
    this.step = paramArrayOfByte;
  }
  
  public void setSztPosSN(byte[] paramArrayOfByte)
  {
    this.sztPosSN = paramArrayOfByte;
  }
  
  public void setSztResult(byte[] paramArrayOfByte)
  {
    this.sztResult = paramArrayOfByte;
  }
  
  public void setTradeDate(byte[] paramArrayOfByte)
  {
    this.tradeDate = paramArrayOfByte;
  }
  
  public void setTradeTime(byte[] paramArrayOfByte)
  {
    this.tradeTime = paramArrayOfByte;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\pakg\Package7102.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */