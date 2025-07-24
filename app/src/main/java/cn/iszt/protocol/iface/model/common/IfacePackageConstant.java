package cn.iszt.protocol.iface.model.common;

public abstract interface IfacePackageConstant
{
  public static abstract interface COMPANY
  {
    public static final byte[] LS = { 76, 83 };
    public static final byte[] MERCHANT = {2, 0};
    public static final byte[] NXP = { 83, 66 };
    public static final byte[] QQ = { -55, -59 };
    public static final byte[] XIAOYU = new byte[2];
 
  }
  
  public static abstract interface MESSAGESTYPE
  {
    public static final byte[] MESSAGE_7100 = {112,0};
    public static final byte[] MESSAGE_7101 = { 113, 1 };
    public static final byte[] MESSAGE_7102 = { 113, 2 };
    
  
  }
  
  public static abstract interface MESSAGESlEN
  {
    public static final int MESSAGE_7100_LEN = 384;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\common\IfacePackageConstant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */