package cn.iszt.protocol.iface.model.pakg.p7102;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.pakg.Package7102;

public class Command7102
  extends Package7102
{
  private static final long serialVersionUID = 1L; //这个是缺省的
  protected byte[] apduSum;
  protected byte[] capdu;
  protected byte[] phyID;
  protected byte[] phyIDLen;
  
  public byte[] getApduSum()
  {
    return this.apduSum;
  }
  
  public byte[] getCapdu()
  {
    return this.capdu;
  }
  
  public byte[] getPackage()
  {
    return ByteUtil.merge(new byte[][] { super.getPackage(), this.phyIDLen, this.phyID, this.apduSum, this.capdu });
  }
  
  public short getPackageLength()
  {
    int i = (short)((short)((short)(super.getPackageLength() + 1) + 8) + 1);
    return (short)(this.capdu.length + i);
  }
  
  public byte[] getPhyID()
  {
    return this.phyID;
  }
  
  public byte[] getPhyIDLen()
  {
    return this.phyIDLen;
  }
  
  public String log()
  {
    return null;
  }
  
  public void setApduSum(byte[] paramArrayOfByte)
  {
    this.apduSum = paramArrayOfByte;
  }
  
  public void setCapdu(byte[] paramArrayOfByte)
  {
    this.capdu = paramArrayOfByte;
  }
  
  public void setPhyID(byte[] paramArrayOfByte)
  {
    this.phyID = paramArrayOfByte;
  }
  
  public void setPhyIDLen(byte[] paramArrayOfByte)
  {
    this.phyIDLen = paramArrayOfByte;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\pakg\p7102\Command7102.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */