package cn.iszt.protocol.iface.model.pakg;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.IFaceMessage;
import java.util.Arrays;
//import org.apache.commons.lang3.ByteUtil;

public class Package7100
  extends IFaceMessage
{
  private static final long serialVersionUID = -8701113501179466434L;
  private byte[] appVersion;
  private byte[] balence;
  private byte[] businessType;
  private byte[] cardPhyType;
  private byte[] cardTransSeq;
  private byte[] centerSN;
  private byte[] chargemoney;
  private byte[] companyCode;
  private byte[] dateTime;
  private byte[] esam;
  private byte[] esamid;
  private byte[] historicalBytes;
  private byte[] historicalByteslen;
  private byte[] hold;
  private byte[] hold1;
  private byte[] iccid;
  private byte[] lastRecord;
  private String loginUserID;
  private byte[] mac;
  private byte[] messageType;
  private byte[] osVersion;
  private byte[] p7100Bytes;
  private byte[] packageLength7100;
  private byte[] paymentSN;
  private byte[] phone;
  private byte[] phoneType;
  private byte[] phyID;
  private byte[] phyIDLen;
  private byte[] posID;
  private byte[] retry;
  private String shopID;
  private byte[] step;
  private String sysInfo;
  private byte[] sztCardNo;
  private byte[] sztPosSN;
  
  public Package7100()
  {
    this.messageType = ByteUtil.codeBCD("7100");
  }
  
  public Package7100(byte[] paramArrayOfByte)
  {
    to7100Package(paramArrayOfByte);
  }
  
  private void to7100Package(byte[] paramArrayOfByte)
  {
    int j = 0 + 2;
    setPackageLength7100(ByteUtil.subarray(paramArrayOfByte, 0, j));
    int i = j + 5;
    setTpdu(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    setEncryption(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    setMessageType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    setRetry(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 3;
    setBusinessType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 2;
    setCompanyCode(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    setEsam(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    setEsamid(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 16;
    setPosID(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 11;
    setPhone(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 9;
    setHold(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 20;
    setIccid(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    setCardPhyType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    setPhyIDLen(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 8;
    setPhyID(ByteUtil.subarray(paramArrayOfByte, j, i));
    //上面90
    j = i + 1;
    setHistoricalByteslen(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 8;
    setHistoricalBytes(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    setChargemoney(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 64;
    setPaymentSN(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    setSztCardNo(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    setCardTransSeq(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    setBalence(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 23;
    setLastRecord(ByteUtil.subarray(paramArrayOfByte, j, i));
    //上面200
    j = i + 16;
    setPhoneType(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 8;
    setOsVersion(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 6;
    setAppVersion(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    setHold1(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 7;
    setDateTime(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    setStep(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 12;
    setCenterSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 5;
    setSztPosSN(ByteUtil.subarray(paramArrayOfByte, j, i));
    this.p7100Bytes = ByteUtil.merge(new byte[][] { this.messageType, this.retry, this.businessType, this.companyCode, this.esam, this.esamid, this.posID, this.phone, this.hold, this.iccid, this.cardPhyType, this.phyIDLen, this.phyID, this.historicalByteslen, this.historicalBytes, this.chargemoney, this.paymentSN, this.sztCardNo, this.cardTransSeq, this.balence, this.lastRecord, this.phoneType, this.osVersion, this.appVersion, this.hold1, this.dateTime, this.step, this.centerSN, this.sztPosSN });
    if ((Arrays.equals(getEncryption(), ByteUtil.codeBCD("01"))) || (Arrays.equals(getEncryption(), ByteUtil.codeBCD("03")))) {
      setMac(ByteUtil.subarray(paramArrayOfByte, i, i + 128));
    }
  }
  
  public byte[] getAppVersion()
  {
    return this.appVersion;
  }
  
  public byte[] getBalence()
  {
    return this.balence;
  }
  
  public byte[] getBusinessType()
  {
    return this.businessType;
  }
  
  public byte[] getCardPhyType()
  {
    return this.cardPhyType;
  }
  
  public byte[] getCardTransSeq()
  {
    return this.cardTransSeq;
  }
  
  public byte[] getCenterSN()
  {
    return this.centerSN;
  }
  
  public byte[] getChargemoney()
  {
    return this.chargemoney;
  }
  
  public byte[] getCompanyCode()
  {
    return this.companyCode;
  }
  
  public byte[] getDateTime()
  {
    return this.dateTime;
  }
  
  public byte[] getEsam()
  {
    return this.esam;
  }
  
  public byte[] getEsamid()
  {
    return this.esamid;
  }
  
  public byte[] getHistoricalBytes()
  {
    return this.historicalBytes;
  }
  
  public byte[] getHistoricalByteslen()
  {
    return this.historicalByteslen;
  }
  
  public byte[] getHold()
  {
    return this.hold;
  }
  
  public byte[] getHold1()
  {
    return this.hold1;
  }
  
  public byte[] getIccid()
  {
    return this.iccid;
  }
  
  public byte[] getLastRecord()
  {
    return this.lastRecord;
  }
  
  public String getLoginUserID()
  {
    return this.loginUserID;
  }
  
  public byte[] getMac()
  {
    return this.mac;
  }
  
  public byte[] getMessageType()
  {
    return this.messageType;
  }
  
  public byte[] getOsVersion()
  {
    return this.osVersion;
  }
  
  public byte[] getP7100Bytes()
  {
    return this.p7100Bytes;
  }
  
  public byte[] getPackage()
  {
    byte[] arrayOfByte2 = ByteUtil.merge(new byte[][] { this.messageType, this.retry, this.businessType, this.companyCode, this.esam, this.esamid, this.posID, this.phone, this.hold, this.iccid, this.cardPhyType, this.phyIDLen, this.phyID, this.historicalByteslen, this.historicalBytes, this.chargemoney, this.paymentSN, this.sztCardNo, this.cardTransSeq, this.balence, this.lastRecord, this.phoneType, this.osVersion, this.appVersion, this.hold1, this.dateTime, this.step, this.centerSN, this.sztPosSN });
    byte[] arrayOfByte1 = new byte[0];
    if (Arrays.equals(getEncryption(), ByteUtil.codeBCD("01"))) {
      arrayOfByte1 = ByteUtil.calculateMAC(arrayOfByte2, ByteUtil.codeBCD("49535A542E504F53"), ByteUtil.codeBCD("5445524D494E414C"));
    }
    return ByteUtil.merge(new byte[][] { ByteUtil.shortToBytes(getPackageLength(), false), super.getPackage(), arrayOfByte2, arrayOfByte1 });
  }
  
  public short getPackageLength()
  {
    short s2 = (short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)((short)(super.getPackageLength() + 0) + 2) + 1) + 3) + 2) + 64) + 1) + 1) + 8) + 1) + 8) + 4) + 64) + 64) + 7) + 1) + 12) + 5);
    short s1 = s2;
    if (Arrays.equals(getEncryption(), ByteUtil.codeBCD("01"))) {
      s1 = (short)(s2 + 4);
    }
    return s1;
  }
  
  public byte[] getPackageLength7100()
  {
    return this.packageLength7100;
  }
  
  public byte[] getPaymentSN()
  {
    return this.paymentSN;
  }
  
  public byte[] getPhone()
  {
    return this.phone;
  }
  
  public byte[] getPhoneType()
  {
    return this.phoneType;
  }
  
  public byte[] getPhyID()
  {
    return this.phyID;
  }
  
  public byte[] getPhyIDLen()
  {
    return this.phyIDLen;
  }
  
  public byte[] getPosID()
  {
    return this.posID;
  }
  
  public byte[] getRetry()
  {
    return this.retry;
  }
  
  public String getShopID()
  {
    return this.shopID;
  }
  
  public byte[] getStep()
  {
    return this.step;
  }
  
  public String getSysInfo()
  {
    return this.sysInfo;
  }
  
  public byte[] getSztCardNo()
  {
    return this.sztCardNo;
  }
  
  public byte[] getSztPosSN()
  {
    return this.sztPosSN;
  }
  
  public String log()
  {
    return null;
  }
  
  public void setAppVersion(byte[] paramArrayOfByte)
  {
    int i;
    if (paramArrayOfByte.length < 6)
    {
      int j = paramArrayOfByte.length;
      i = 0;
      while (i < 6 - j) {
          paramArrayOfByte = ByteUtil.merge(new byte[][] { " ".getBytes(), paramArrayOfByte });
          i += 1;
      }
    }
    else
    {
    	paramArrayOfByte = ByteUtil.subarray(paramArrayOfByte, 0, 6);
    }
    
    
    this.appVersion = paramArrayOfByte;
    return;
  }
  
  public void setBalence(byte[] paramArrayOfByte)
  {
    this.balence = paramArrayOfByte;
  }
  
  public void setBusinessType(byte[] paramArrayOfByte)
  {
    this.businessType = paramArrayOfByte;
  }
  
  public void setCardPhyType(byte[] paramArrayOfByte)
  {
    this.cardPhyType = paramArrayOfByte;
  }
  
  public void setCardTransSeq(byte[] paramArrayOfByte)
  {
    this.cardTransSeq = paramArrayOfByte;
  }
  
  public void setCenterSN(byte[] paramArrayOfByte)
  {
    this.centerSN = paramArrayOfByte;
  }
  
  public void setChargemoney(byte[] paramArrayOfByte)
  {
    this.chargemoney = paramArrayOfByte;
  }
  
  public void setCompanyCode(byte[] paramArrayOfByte)
  {
    this.companyCode = paramArrayOfByte;
  }
  
  public void setDateTime(byte[] paramArrayOfByte)
  {
    this.dateTime = paramArrayOfByte;
  }
  
  public void setEsam(byte[] paramArrayOfByte)
  {
    this.esam = paramArrayOfByte;
  }
  
  public void setEsamid(byte[] paramArrayOfByte)
  {
    this.esamid = paramArrayOfByte;
  }
  
  public void setHistoricalBytes(byte[] paramArrayOfByte)
  {
    this.historicalBytes = paramArrayOfByte;
  }
  
  public void setHistoricalByteslen(byte[] paramArrayOfByte)
  {
    this.historicalByteslen = paramArrayOfByte;
  }
  
  public void setHold(byte[] paramArrayOfByte)
  {
    this.hold = paramArrayOfByte;
  }
  
  public void setHold1(byte[] paramArrayOfByte)
  {
    this.hold1 = paramArrayOfByte;
  }
  
  public void setIccid(byte[] paramArrayOfByte)
  {
    this.iccid = paramArrayOfByte;
  }
  
  public void setLastRecord(byte[] paramArrayOfByte)
  {
    this.lastRecord = paramArrayOfByte;
  }
  
  public void setLoginUserID(String paramString)
  {
    this.loginUserID = paramString;
  }
  
  public void setMac(byte[] paramArrayOfByte)
  {
    this.mac = paramArrayOfByte;
  }
  
  public void setMessageType(byte[] paramArrayOfByte)
  {
    this.messageType = paramArrayOfByte;
  }
  
  public void setOsVersion(byte[] paramArrayOfByte)
  {
    int i;
    if (paramArrayOfByte.length < 8)
    {
      int j = paramArrayOfByte.length;
      i = 0;
      while (i < 8 - j) {
    	  
          paramArrayOfByte = ByteUtil.merge(new byte[][] { " ".getBytes(), paramArrayOfByte });
          i += 1;
      }
    }
    else
    {
        paramArrayOfByte = ByteUtil.subarray(paramArrayOfByte, 0, 8);
    }
    this.osVersion = paramArrayOfByte;
    return;
  }
  
  public void setP7100Bytes(byte[] paramArrayOfByte)
  {
    this.p7100Bytes = paramArrayOfByte;
  }
  
  public void setPackageLength7100(byte[] paramArrayOfByte)
  {
    this.packageLength7100 = paramArrayOfByte;
  }
  
  public void setPaymentSN(byte[] paramArrayOfByte)
  {
    this.paymentSN = paramArrayOfByte;
  }
  
  public void setPhone(byte[] paramArrayOfByte)
  {
    this.phone = paramArrayOfByte;
  }
  
  public void setPhoneType(byte[] paramArrayOfByte)
  {
    int i;
    if (paramArrayOfByte.length < 16)
    {
      int j = paramArrayOfByte.length;
      i = 0;
      while (i < 16 - j) {
          paramArrayOfByte = ByteUtil.merge(new byte[][] { " ".getBytes(), paramArrayOfByte });
          i += 1;
      }
    }
    else
    {
        paramArrayOfByte = ByteUtil.subarray(paramArrayOfByte, 0, 16);
    }
    this.phoneType = paramArrayOfByte;
    return;
  }
  
  public void setPhyID(byte[] paramArrayOfByte)
  {
    this.phyID = paramArrayOfByte;
  }
  
  public void setPhyIDLen(byte[] paramArrayOfByte)
  {
    this.phyIDLen = paramArrayOfByte;
  }
  
  public void setPosID(byte[] paramArrayOfByte)
  {
    this.posID = paramArrayOfByte;
  }
  
  public void setRetry(byte[] paramArrayOfByte)
  {
    this.retry = paramArrayOfByte;
  }
  
  public void setShopID(String paramString)
  {
    this.shopID = paramString;
  }
  
  public void setStep(byte[] paramArrayOfByte)
  {
    this.step = paramArrayOfByte;
  }
  
  public void setSysInfo(String paramString)
  {
    this.sysInfo = paramString;
  }
  
  public void setSztCardNo(byte[] paramArrayOfByte)
  {
    this.sztCardNo = paramArrayOfByte;
  }
  
  public void setSztPosSN(byte[] paramArrayOfByte)
  {
    this.sztPosSN = paramArrayOfByte;
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\iface\model\pakg\Package7100.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */