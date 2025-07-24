package cn.iszt.protocol.iface.model.pakg;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.IFaceMessage;
//import org.apache.commons.lang3.ByteUtil;

public class Package7101
  extends IFaceMessage
{
  private static final long serialVersionUID = -8988491600259799438L;
  protected byte[] apduStatue;
  protected byte[] apduSum;
  protected byte[] businessType;
  protected byte[] centerSN;
  protected byte[] chargeMoney;
  protected byte[] lxrBusinessSN;
  protected byte[] messageType;
  private byte[] packageLength7101;
  protected byte[] phyID;
  protected byte[] phyIDLen;
  protected byte[] rapdu;
  protected byte[] retry;
  protected byte[] step;
  protected byte[] sztPosSN;
  protected byte[] tradeDate;
  protected byte[] tradeTime;
  
  public Package7101()
  {
    this.messageType = ByteUtil.codeBCD("7101");
  }
  
  public Package7101(byte[] paramArrayOfByte)
  {
    toPackage7101(paramArrayOfByte);
  }
  
  private void toPackage7101(byte[] paramArrayOfByte)
  {
    int j = 0 + 2;
    setPackageLength7101(ByteUtil.subarray(paramArrayOfByte, 0, j));
    int i = j + 5;
    setTpdu(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    setEncryption(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    setMessageType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 16;
    setLxrBusinessSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    setRetry(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    setBusinessType(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    setStep(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    setChargeMoney(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 3;
    setTradeTime(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 2;
    setTradeDate(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 12;
    setCenterSN(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 5;
    setSztPosSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    setPhyIDLen(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 8;
    setPhyID(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    setApduStatue(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    setApduSum(ByteUtil.subarray(paramArrayOfByte, i, j));
    paramArrayOfByte = ByteUtil.subarray(paramArrayOfByte, j, paramArrayOfByte.length);
    j = getApduSum()[0];
    byte[] arrayOfByte = new byte[0];
    i = 0;
    for (;;)
    {
      if (i >= j)
      {
        setRapdu(arrayOfByte);
        return;
      }
      int k = ByteUtil.bytesToShort(ByteUtil.subarray(paramArrayOfByte, 0, 2), false);
      arrayOfByte = ByteUtil.merge(new byte[][] { arrayOfByte, ByteUtil.subarray(paramArrayOfByte, 0, k + 2) });
      paramArrayOfByte = ByteUtil.subarray(paramArrayOfByte, k + 2, paramArrayOfByte.length);
      i += 1;
    }
  }
  
  public byte[] getApduStatue()
  {
    return this.apduStatue;
  }
  
  public byte[] getApduSum()
  {
    return this.apduSum;
  }
  
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
    return ByteUtil.merge(new byte[][] { ByteUtil.shortToBytes(getPackageLength(), false), super.getPackage(), this.messageType, this.lxrBusinessSN, this.retry, this.businessType, this.step, this.chargeMoney, this.tradeTime, this.tradeDate, this.centerSN, this.sztPosSN, this.phyIDLen, this.phyID, this.apduStatue, this.apduSum, this.rapdu });
  }
  
  public short getPackageLength()
  {
    int i = (short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)(super.getPackageLength() + 0) + 2) + 16) + 1) + 3) + 1) + 4) + 3) + 2) + 12) + 5) + 1) + 8) + 2) + 1);
    return (short)(getRapdu().length + i);
  }
  
  public byte[] getPackageLength7101()
  {
    return this.packageLength7101;
  }
  
  public byte[] getPhyID()
  {
    return this.phyID;
  }
  
  public byte[] getPhyIDLen()
  {
    return this.phyIDLen;
  }
  
  public byte[] getRapdu()
  {
    return this.rapdu;
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
  
  public byte[] getTradeDate()
  {
    return this.tradeDate;
  }
  
  public byte[] getTradeTime()
  {
    return this.tradeTime;
  }
  
  public String log()
  {
    return null;
  }
  
  public void setApduStatue(byte[] paramArrayOfByte)
  {
    this.apduStatue = paramArrayOfByte;
  }
  
  public void setApduSum(byte[] paramArrayOfByte)
  {
    this.apduSum = paramArrayOfByte;
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
  
  public void setPackageLength7101(byte[] paramArrayOfByte)
  {
    this.packageLength7101 = paramArrayOfByte;
  }
  
  public void setPhyID(byte[] paramArrayOfByte)
  {
    this.phyID = paramArrayOfByte;
  }
  
  public void setPhyIDLen(byte[] paramArrayOfByte)
  {
    this.phyIDLen = paramArrayOfByte;
  }
  
  public void setRapdu(byte[] paramArrayOfByte)
  {
    this.rapdu = paramArrayOfByte;
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
  
  public void setTradeDate(byte[] paramArrayOfByte)
  {
    this.tradeDate = paramArrayOfByte;
  }
  
  public void setTradeTime(byte[] paramArrayOfByte)
  {
    this.tradeTime = paramArrayOfByte;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\pakg\Package7101.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */