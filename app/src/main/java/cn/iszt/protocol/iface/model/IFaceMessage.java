package cn.iszt.protocol.iface.model;

import cn.iszt.protocol.common.util.ByteUtil;

public abstract class IFaceMessage
  implements IFacePackage
{
  private static final long serialVersionUID = -7815196988410561728L;
  protected byte[] encryption = new byte[1];
  private byte[] tpdu = ByteUtil.codeBCD("6000000000");
  
  public byte[] getEncryption()
  {
    return this.encryption;
  }
  
  public byte[] getPackage()
  {
    return ByteUtil.merge(new byte[][] { this.tpdu, this.encryption });
  }
  
  public short getPackageLength()
  {
    return (short)((short)5 + 1);
  }
  
  public byte[] getTpdu()
  {
    return this.tpdu;
  }
  
  public void setEncryption(byte[] paramArrayOfByte)
  {
    this.encryption = paramArrayOfByte;
  }
  
  public void setTpdu(byte[] paramArrayOfByte)
  {
    this.tpdu = paramArrayOfByte;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\IFaceMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */