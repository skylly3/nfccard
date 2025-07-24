package cn.iszt.protocol.iface.model;

import java.io.Serializable;

public abstract interface IFacePackage
  extends Serializable
{
  public abstract byte[] getPackage();
  
  public abstract short getPackageLength();
  
  public abstract String log();
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\IFacePackage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */