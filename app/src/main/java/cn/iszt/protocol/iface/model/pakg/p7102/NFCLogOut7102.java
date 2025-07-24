package cn.iszt.protocol.iface.model.pakg.p7102;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.pakg.Package7102;

public class NFCLogOut7102
  extends Package7102
{
	private static final long serialVersionUID = 1L; //这个是缺省的
  private byte[] batchNo;
  private byte[] bofMoney;
  private byte[] centerTradeSN;
  private byte[] eofMoney;
  private byte[] phyID;
  private byte[] phyIDLen;
  private byte[] sw;
  private byte[] sztCardNo;
  private byte[] sztCardType;
  
  public byte[] getBatchNo()
  {
    return this.batchNo;
  }
  
  public byte[] getBofMoney()
  {
    return this.bofMoney;
  }
  
  public byte[] getCenterTradeSN()
  {
    return this.centerTradeSN;
  }
  
  public byte[] getChargeMoney()
  {
    return this.chargeMoney;
  }
  
  public byte[] getEofMoney()
  {
    return this.eofMoney;
  }
  
  public byte[] getPackage()
  {
    return ByteUtil.merge(new byte[][] { super.getPackage(), this.phyIDLen, this.phyID, getSztResult(), this.sw, this.batchNo, this.centerTradeSN, this.sztCardNo, this.sztCardType, this.chargeMoney, this.bofMoney, this.eofMoney });
  }
  
  public short getPackageLength()
  {
    return (short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)(super.getPackageLength() + 1) + 8) + 4) + 2) + 12) + 4) + 9) + 1) + 4) + 4) + 4);
  }
  
  public byte[] getPhyID()
  {
    return this.phyID;
  }
  
  public byte[] getPhyIDLen()
  {
    return this.phyIDLen;
  }
  
  public byte[] getSw()
  {
    return this.sw;
  }
  
  public byte[] getSztCardNo()
  {
    return this.sztCardNo;
  }
  
  public byte[] getSztCardType()
  {
    return this.sztCardType;
  }
  
  public String log()
  {
    return null;
  }
  
  public void setBatchNo(byte[] paramArrayOfByte)
  {
    this.batchNo = paramArrayOfByte;
  }
  
  public void setBofMoney(byte[] paramArrayOfByte)
  {
    this.bofMoney = paramArrayOfByte;
  }
  
  public void setCenterTradeSN(byte[] paramArrayOfByte)
  {
    this.centerTradeSN = paramArrayOfByte;
  }
  
  public void setChargeMoney(byte[] paramArrayOfByte)
  {
    this.chargeMoney = paramArrayOfByte;
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
  
  public void setSw(byte[] paramArrayOfByte)
  {
    this.sw = paramArrayOfByte;
  }
  
  public void setSztCardNo(byte[] paramArrayOfByte)
  {
    this.sztCardNo = paramArrayOfByte;
  }
  
  public void setSztCardType(byte[] paramArrayOfByte)
  {
    this.sztCardType = paramArrayOfByte;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\pakg\p7102\NFCLogOut7102.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */