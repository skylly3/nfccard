package com.sinpo.xnfc.nfc.reader;

public class SztApdu
{
  public static byte[] SZT_CreditCAPP;
  static byte[] SZT_Deduct;
  public static byte[] SZT_DeductCAPP;
  static byte[] SZT_GETEF0011;
  public static byte[] SZT_GETEF0015;
  static byte[] SZT_GETEF0016;
  public static byte[] SZT_GETEF0018;
  public static byte[] SZT_GETEF0019;
  public static byte[] SZT_GETEF001A;
  static byte[] SZT_GETEF001B;
  public static byte[] SZT_GETOVERMONEY;
  static byte[] SZT_Get08File;
  static byte[] SZT_GetSWPUID;
  static byte[] SZT_Get_APPLET_VER;
  public static byte[] SZT_SELECTFILE_1001;
  public static byte[] SZT_SWPCreditCAPP;
  static byte[] SZT_UpdateEF0019;
  
  static
  {
    byte[] arrayOfByte = new byte[7];
    arrayOfByte[1] = -92;
    arrayOfByte[4] = 2;
    arrayOfByte[5] = 16;
    arrayOfByte[6] = 1;
    SZT_SELECTFILE_1001 = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[1] = -80;
    arrayOfByte[2] = -111;
    arrayOfByte[4] = 40;
    SZT_GETEF0011 = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[0] = Byte.MIN_VALUE;
    arrayOfByte[1] = 92;
    arrayOfByte[3] = 2;
    arrayOfByte[4] = 4;
    SZT_GETOVERMONEY = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[1] = -80;
    arrayOfByte[2] = -107;
    arrayOfByte[4] = 32;
    SZT_GETEF0015 = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[1] = -80;
    arrayOfByte[2] = -106;
    arrayOfByte[4] = 80;
    SZT_GETEF0016 = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[1] = -78;
    arrayOfByte[3] = -60;
    arrayOfByte[4] = 23;
    SZT_GETEF0018 = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[1] = -78;
    arrayOfByte[2] = 1;
    arrayOfByte[3] = -52;
    arrayOfByte[4] = 32;
    SZT_GETEF0019 = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[1] = -80;
    arrayOfByte[2] = -102;
    arrayOfByte[4] = 28;
    SZT_GETEF001A = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[1] = -80;
    arrayOfByte[2] = -101;
    arrayOfByte[4] = 28;
    SZT_GETEF001B = arrayOfByte;
    arrayOfByte = new byte[6];
    arrayOfByte[0] = Byte.MIN_VALUE;
    arrayOfByte[1] = 80;
    arrayOfByte[3] = 2;
    arrayOfByte[4] = 11;
    arrayOfByte[5] = 1;
    SZT_CreditCAPP = arrayOfByte;
    SZT_SWPCreditCAPP = new byte[] { -128, 80, 48, 2, 11, 1 };
    SZT_DeductCAPP = new byte[] { -128, 80, 3, 2, 11, 1 };
    arrayOfByte = new byte[5];
    arrayOfByte[0] = Byte.MIN_VALUE;
    arrayOfByte[1] = 84;
    arrayOfByte[2] = 1;
    arrayOfByte[4] = 15;
    SZT_Deduct = arrayOfByte;
    SZT_UpdateEF0019 = new byte[] { -128, -36, 1, -56, 32, 1, 30 };
    arrayOfByte = new byte[5];
    arrayOfByte[0] = Byte.MIN_VALUE;
    arrayOfByte[1] = 12;
    arrayOfByte[4] = 8;
    SZT_GetSWPUID = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[1] = -78;
    arrayOfByte[2] = 1;
    arrayOfByte[3] = 68;
    SZT_Get08File = arrayOfByte;
    arrayOfByte = new byte[5];
    arrayOfByte[0] = -96;
    arrayOfByte[1] = 66;
    SZT_Get_APPLET_VER = arrayOfByte;
  }
}

 