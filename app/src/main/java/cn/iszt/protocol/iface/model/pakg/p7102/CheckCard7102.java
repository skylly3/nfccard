package cn.iszt.protocol.iface.model.pakg.p7102;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.pakg.Package7102;

public class CheckCard7102
  extends Package7102
{
  private static final long serialVersionUID = 1L; //这个是缺省的
  private byte[] cardFaceNo;
  private byte[] cardStatus;
  private byte[] cardType;
  private byte[] effectiveDateStart;
  private byte[] effectiveDatetEnd;
  private byte[] phyID;
  private byte[] phyIDLen;
  private byte[] sw;
  private byte[] tradeCount;
  private byte[] tradeRecord1;
  private byte[] tradeRecord10;
  private byte[] tradeRecord2;
  private byte[] tradeRecord3;
  private byte[] tradeRecord4;
  private byte[] tradeRecord5;
  private byte[] tradeRecord6;
  private byte[] tradeRecord7;
  private byte[] tradeRecord8;
  private byte[] tradeRecord9;
  private byte[] walletBalance;
  
  public byte[] getCardFaceNo()
  {
    return this.cardFaceNo;
  }
  
  public byte[] getCardStatus()
  {
    return this.cardStatus;
  }
  
  public byte[] getCardType()
  {
    return this.cardType;
  }
  
  public byte[] getEffectiveDateStart()
  {
    return this.effectiveDateStart;
  }
  
  public byte[] getEffectiveDatetEnd()
  {
    return this.effectiveDatetEnd;
  }
  
  public byte[] getPackage()
  {
    return ByteUtil.merge(new byte[][] { super.getPackage(), this.phyIDLen, this.phyID, getSztResult(), this.sw, this.cardFaceNo, this.cardType, this.cardStatus, this.walletBalance, this.effectiveDateStart, this.effectiveDatetEnd, this.tradeCount, this.tradeRecord1, this.tradeRecord2, this.tradeRecord3, this.tradeRecord4, this.tradeRecord5, this.tradeRecord6, this.tradeRecord7, this.tradeRecord8, this.tradeRecord9, this.tradeRecord10 });
  }
  
  public short getPackageLength()
  {
    return (short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)(super.getPackageLength() + 4) + 1) + 8) + 2) + 9) + 1) + 1) + 4) + 4) + 4) + 1) + 23) + 23) + 23) + 23) + 23) + 23) + 23) + 23) + 23) + 23);
  }
  
  public byte[] getTradeCount()
  {
    return this.tradeCount;
  }
  
  public byte[] getTradeRecord1()
  {
    return this.tradeRecord1;
  }
  
  public byte[] getTradeRecord10()
  {
    return this.tradeRecord10;
  }
  
  public byte[] getTradeRecord2()
  {
    return this.tradeRecord2;
  }
  
  public byte[] getTradeRecord3()
  {
    return this.tradeRecord3;
  }
  
  public byte[] getTradeRecord4()
  {
    return this.tradeRecord4;
  }
  
  public byte[] getTradeRecord5()
  {
    return this.tradeRecord5;
  }
  
  public byte[] getTradeRecord6()
  {
    return this.tradeRecord6;
  }
  
  public byte[] getTradeRecord7()
  {
    return this.tradeRecord7;
  }
  
  public byte[] getTradeRecord8()
  {
    return this.tradeRecord8;
  }
  
  public byte[] getTradeRecord9()
  {
    return this.tradeRecord9;
  }
  
  public byte[] getWalletBalance()
  {
    return this.walletBalance;
  }
  
  public String log()
  {
    return null;
  }
  
  public void setCardFaceNo(byte[] paramArrayOfByte)
  {
    this.cardFaceNo = paramArrayOfByte;
  }
  
  public void setCardStatus(byte[] paramArrayOfByte)
  {
    this.cardStatus = paramArrayOfByte;
  }
  
  public void setCardType(byte[] paramArrayOfByte)
  {
    this.cardType = paramArrayOfByte;
  }
  
  public void setEffectiveDateStart(byte[] paramArrayOfByte)
  {
    this.effectiveDateStart = paramArrayOfByte;
  }
  
  public void setEffectiveDatetEnd(byte[] paramArrayOfByte)
  {
    this.effectiveDatetEnd = paramArrayOfByte;
  }
  
  public void setPhyID(byte[] paramArrayOfByte)
  {
    this.phyID = paramArrayOfByte;
  }
  
  public void setPhyIDLen(byte[] paramArrayOfByte)
  {
    this.phyIDLen = paramArrayOfByte;
  }
  
  public void setSw(byte[] paramArrayOfByte)
  {
    this.sw = paramArrayOfByte;
  }
  
  public void setTradeCount(byte[] paramArrayOfByte)
  {
    this.tradeCount = paramArrayOfByte;
  }
  
  public void setTradeRecord1(byte[] paramArrayOfByte)
  {
    this.tradeRecord1 = paramArrayOfByte;
  }
  
  public void setTradeRecord10(byte[] paramArrayOfByte)
  {
    this.tradeRecord10 = paramArrayOfByte;
  }
  
  public void setTradeRecord2(byte[] paramArrayOfByte)
  {
    this.tradeRecord2 = paramArrayOfByte;
  }
  
  public void setTradeRecord3(byte[] paramArrayOfByte)
  {
    this.tradeRecord3 = paramArrayOfByte;
  }
  
  public void setTradeRecord4(byte[] paramArrayOfByte)
  {
    this.tradeRecord4 = paramArrayOfByte;
  }
  
  public void setTradeRecord5(byte[] paramArrayOfByte)
  {
    this.tradeRecord5 = paramArrayOfByte;
  }
  
  public void setTradeRecord6(byte[] paramArrayOfByte)
  {
    this.tradeRecord6 = paramArrayOfByte;
  }
  
  public void setTradeRecord7(byte[] paramArrayOfByte)
  {
    this.tradeRecord7 = paramArrayOfByte;
  }
  
  public void setTradeRecord8(byte[] paramArrayOfByte)
  {
    this.tradeRecord8 = paramArrayOfByte;
  }
  
  public void setTradeRecord9(byte[] paramArrayOfByte)
  {
    this.tradeRecord9 = paramArrayOfByte;
  }
  
  public void setWalletBalance(byte[] paramArrayOfByte)
  {
    this.walletBalance = paramArrayOfByte;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\pakg\p7102\CheckCard7102.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */