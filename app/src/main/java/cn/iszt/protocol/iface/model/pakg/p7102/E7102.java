package cn.iszt.protocol.iface.model.pakg.p7102;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.pakg.Package7102;

public class E7102
  extends Package7102
{
  private static final long serialVersionUID = -572271298191639402L;
  
  public E7102()
  {
    this.lxrBusinessSN = ByteUtil.codeBCD("00000000000000000000000000000000");
    this.retry = ByteUtil.codeBCD("00");
    this.businessType = ByteUtil.codeBCD("000000");
    this.step = ByteUtil.codeBCD("99");
    this.chargeMoney = ByteUtil.codeBCD("00000000");
    this.tradeTime = ByteUtil.codeBCD("000000");
    this.tradeDate = ByteUtil.codeBCD("0000");
    this.centerSN = ByteUtil.getBytes("000000000000");
    this.sztPosSN = ByteUtil.codeBCD("0000000000");
  }
  
  public short getPackageLength()
  {
    return super.getPackageLength();
  }
  
  public String log()
  {
    return null;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\pakg\p7102\E7102.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */