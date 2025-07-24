package cn.iszt.protocol.common.util;

import java.io.IOException;
import java.io.InputStream;
//import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;

//import com.sinpo.xnfc.nfc.Util;

public class ByteUtil
{
  public static final byte[] EMPTY = new byte[0];
  
  public static String bytesToHexString(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null)
    {
      StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length);
      int j = paramArrayOfByte.length;
      int i = 0;
      for (;;)
      {
        if (i >= j) {
          return localStringBuilder.toString();
        }
        String str = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
        if (str.length() < 2) {
          localStringBuilder.append(0);
        }
        localStringBuilder.append(str.toUpperCase());
        i += 1;
      }
    }
    return "";
  }
  
  public static long bytesToInt(byte[] paramArrayOfBytex, boolean paramBoolean)
  {
	  ByteBuffer paramArrayOfByte = ByteBuffer.wrap(paramArrayOfBytex);
    if (paramBoolean) {
      paramArrayOfByte.order(ByteOrder.LITTLE_ENDIAN);
    }
    else
    {

      paramArrayOfByte.order(ByteOrder.BIG_ENDIAN);
    }
    return paramArrayOfByte.getInt() & 0xFFFFFFFF;
  }
  
  public static int bytesToShort(byte[] paramArrayOfBytex, boolean paramBoolean)
  {
	  ByteBuffer paramArrayOfByte = ByteBuffer.wrap(paramArrayOfBytex);
    if (paramBoolean) {
      paramArrayOfByte.order(ByteOrder.LITTLE_ENDIAN);
    }
    else
    {

      paramArrayOfByte.order(ByteOrder.BIG_ENDIAN);
    }
    return paramArrayOfByte.getShort() & 0xFFFF;
  }
  
  public static byte[] calculateMAC(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    int i = paramArrayOfByte1.length;
    byte[] arrayOfByte1 = new byte[4];
    byte[] arrayOfByte2 = new byte[8];
    byte[] arrayOfByte3 = new byte[8];
    int k = i / 8 + 1;
    if (k * 8 + 2 > 1024) {
      return arrayOfByte1;
    }
    byte[] arrayOfByte4 = new byte[k * 8 + 2];
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte4, 0, i);
    paramArrayOfByte1 = new byte[8];
    paramArrayOfByte1[0] = Byte.MIN_VALUE;
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte4, i, 8 - i % 8);
    i = 0;
    if (i >= k)
    {
      System.arraycopy(des(arrayOfByte2, paramArrayOfByte3, 0), 0, arrayOfByte2, 0, 8);
      System.arraycopy(des(arrayOfByte2, paramArrayOfByte2, 1), 0, arrayOfByte2, 0, 8);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 4);
      return arrayOfByte1;
    }
    int j = 0;
    for (;;)
    {
      if (j >= 8)
      {
        System.arraycopy(des(arrayOfByte3, paramArrayOfByte2, 1), 0, arrayOfByte2, 0, 8);
        i += 1;
        break;
      }
      arrayOfByte3[j] = ((byte)(arrayOfByte2[j] ^ arrayOfByte4[(i * 8 + j)]));
      j += 1;
    }
    return arrayOfByte2;
  }
  
  private static Object des(byte[] arrayOfByte2, byte[] paramArrayOfByte3, int i) {
	// TODO Auto-generated method stub
	return null;
}

public static byte[] codeBCD(String paramStringx)
  {
    String str = paramStringx;
    if (paramStringx.length() == 1) {
      str = "0" + paramStringx;
    }
    byte[] arrayOfByte2 = new byte[str.length() / 2];
    byte[] paramString = new byte[0];
    try
    {
      byte[] arrayOfByte1 = str.getBytes("utf-8");
      paramString = arrayOfByte1;
      
      for (int i = 0;;i += 1)
      {
        if (i >= str.length() / 2) {
          return arrayOfByte2;
        }

      //  continue;
        arrayOfByte2[i] = uniteBytes(paramString[(i * 2)], paramString[(i * 2 + 1)]);
        
      }

    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
        localUnsupportedEncodingException.printStackTrace();
    }
    return arrayOfByte2;
   
  }
  
  public static byte[] codeBCD(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
    int i = 0;
    for (;;)
    {
      if (i >= paramArrayOfByte.length / 2) {
        return arrayOfByte;
      }
      arrayOfByte[i] = uniteBytes(paramArrayOfByte[(i * 2)], paramArrayOfByte[(i * 2 + 1)]);
      i += 1;
    }
  }
  
  public static byte convertInt2Byte(int paramInt)
  {
    return (byte)(paramInt & 0xFF);
  }
//  
//  private static byte[] des(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
//  {
//    byte[] arrayOfByte1 = new byte[16];
//    byte[] arrayOfByte2 = new byte[64];
//    byte[] arrayOfByte3 = new byte[56];
//    byte[] arrayOfByte4 = new byte[48];
//    byte[] arrayOfByte5 = new byte[48];
//    byte[] arrayOfByte6 = new byte[64];
//    byte[] arrayOfByte7 = new byte[64];
//    byte[] arrayOfByte8 = new byte[16];
//    arrayOfByte8[0] = 14;
//    arrayOfByte8[1] = 4;
//    arrayOfByte8[2] = 13;
//    arrayOfByte8[3] = 1;
//    arrayOfByte8[4] = 2;
//    arrayOfByte8[5] = 15;
//    arrayOfByte8[6] = 11;
//    arrayOfByte8[7] = 8;
//    arrayOfByte8[8] = 3;
//    arrayOfByte8[9] = 10;
//    arrayOfByte8[10] = 6;
//    arrayOfByte8[11] = 12;
//    arrayOfByte8[12] = 5;
//    arrayOfByte8[13] = 9;
//    arrayOfByte8[15] = 7;
//    byte[] arrayOfByte9 = new byte[16];
//    arrayOfByte9[1] = 15;
//    arrayOfByte9[2] = 7;
//    arrayOfByte9[3] = 4;
//    arrayOfByte9[4] = 14;
//    arrayOfByte9[5] = 2;
//    arrayOfByte9[6] = 13;
//    arrayOfByte9[7] = 1;
//    arrayOfByte9[8] = 10;
//    arrayOfByte9[9] = 6;
//    arrayOfByte9[10] = 12;
//    arrayOfByte9[11] = 11;
//    arrayOfByte9[12] = 9;
//    arrayOfByte9[13] = 5;
//    arrayOfByte9[14] = 3;
//    arrayOfByte9[15] = 8;
//    byte[] arrayOfByte10 = new byte[16];
//    arrayOfByte10[0] = 4;
//    arrayOfByte10[1] = 1;
//    arrayOfByte10[2] = 14;
//    arrayOfByte10[3] = 8;
//    arrayOfByte10[4] = 13;
//    arrayOfByte10[5] = 6;
//    arrayOfByte10[6] = 2;
//    arrayOfByte10[7] = 11;
//    arrayOfByte10[8] = 15;
//    arrayOfByte10[9] = 12;
//    arrayOfByte10[10] = 9;
//    arrayOfByte10[11] = 7;
//    arrayOfByte10[12] = 3;
//    arrayOfByte10[13] = 10;
//    arrayOfByte10[14] = 5;
//    byte[] arrayOfByte11 = new byte[16];
//    arrayOfByte11[0] = 15;
//    arrayOfByte11[1] = 12;
//    arrayOfByte11[2] = 8;
//    arrayOfByte11[3] = 2;
//    arrayOfByte11[4] = 4;
//    arrayOfByte11[5] = 9;
//    arrayOfByte11[6] = 1;
//    arrayOfByte11[7] = 7;
//    arrayOfByte11[8] = 5;
//    arrayOfByte11[9] = 11;
//    arrayOfByte11[10] = 3;
//    arrayOfByte11[11] = 14;
//    arrayOfByte11[12] = 10;
//    arrayOfByte11[14] = 6;
//    arrayOfByte11[15] = 13;
//    byte[] arrayOfByte12 = new byte[16];
//    arrayOfByte12[0] = 15;
//    arrayOfByte12[1] = 1;
//    arrayOfByte12[2] = 8;
//    arrayOfByte12[3] = 14;
//    arrayOfByte12[4] = 6;
//    arrayOfByte12[5] = 11;
//    arrayOfByte12[6] = 3;
//    arrayOfByte12[7] = 4;
//    arrayOfByte12[8] = 9;
//    arrayOfByte12[9] = 7;
//    arrayOfByte12[10] = 2;
//    arrayOfByte12[11] = 13;
//    arrayOfByte12[12] = 12;
//    arrayOfByte12[14] = 5;
//    arrayOfByte12[15] = 10;
//    byte[] arrayOfByte13 = new byte[16];
//    arrayOfByte13[0] = 3;
//    arrayOfByte13[1] = 13;
//    arrayOfByte13[2] = 4;
//    arrayOfByte13[3] = 7;
//    arrayOfByte13[4] = 15;
//    arrayOfByte13[5] = 2;
//    arrayOfByte13[6] = 8;
//    arrayOfByte13[7] = 14;
//    arrayOfByte13[8] = 12;
//    arrayOfByte13[10] = 1;
//    arrayOfByte13[11] = 10;
//    arrayOfByte13[12] = 6;
//    arrayOfByte13[13] = 9;
//    arrayOfByte13[14] = 11;
//    arrayOfByte13[15] = 5;
//    byte[] arrayOfByte14 = new byte[16];
//    arrayOfByte14[1] = 14;
//    arrayOfByte14[2] = 7;
//    arrayOfByte14[3] = 11;
//    arrayOfByte14[4] = 10;
//    arrayOfByte14[5] = 4;
//    arrayOfByte14[6] = 13;
//    arrayOfByte14[7] = 1;
//    arrayOfByte14[8] = 5;
//    arrayOfByte14[9] = 8;
//    arrayOfByte14[10] = 12;
//    arrayOfByte14[11] = 6;
//    arrayOfByte14[12] = 9;
//    arrayOfByte14[13] = 3;
//    arrayOfByte14[14] = 2;
//    arrayOfByte14[15] = 15;
//    byte[] arrayOfByte15 = new byte[16];
//    arrayOfByte15[0] = 13;
//    arrayOfByte15[1] = 8;
//    arrayOfByte15[2] = 10;
//    arrayOfByte15[3] = 1;
//    arrayOfByte15[4] = 3;
//    arrayOfByte15[5] = 15;
//    arrayOfByte15[6] = 4;
//    arrayOfByte15[7] = 2;
//    arrayOfByte15[8] = 11;
//    arrayOfByte15[9] = 6;
//    arrayOfByte15[10] = 7;
//    arrayOfByte15[11] = 12;
//    arrayOfByte15[13] = 5;
//    arrayOfByte15[14] = 14;
//    arrayOfByte15[15] = 9;
//    byte[] arrayOfByte16 = new byte[16];
//    arrayOfByte16[0] = 10;
//    arrayOfByte16[2] = 9;
//    arrayOfByte16[3] = 14;
//    arrayOfByte16[4] = 6;
//    arrayOfByte16[5] = 3;
//    arrayOfByte16[6] = 15;
//    arrayOfByte16[7] = 5;
//    arrayOfByte16[8] = 1;
//    arrayOfByte16[9] = 13;
//    arrayOfByte16[10] = 12;
//    arrayOfByte16[11] = 7;
//    arrayOfByte16[12] = 11;
//    arrayOfByte16[13] = 4;
//    arrayOfByte16[14] = 2;
//    arrayOfByte16[15] = 8;
//    byte[] arrayOfByte17 = new byte[16];
//    arrayOfByte17[0] = 13;
//    arrayOfByte17[1] = 7;
//    arrayOfByte17[3] = 9;
//    arrayOfByte17[4] = 3;
//    arrayOfByte17[5] = 4;
//    arrayOfByte17[6] = 6;
//    arrayOfByte17[7] = 10;
//    arrayOfByte17[8] = 2;
//    arrayOfByte17[9] = 8;
//    arrayOfByte17[10] = 5;
//    arrayOfByte17[11] = 14;
//    arrayOfByte17[12] = 12;
//    arrayOfByte17[13] = 11;
//    arrayOfByte17[14] = 15;
//    arrayOfByte17[15] = 1;
//    byte[] arrayOfByte18 = new byte[16];
//    arrayOfByte18[0] = 13;
//    arrayOfByte18[1] = 6;
//    arrayOfByte18[2] = 4;
//    arrayOfByte18[3] = 9;
//    arrayOfByte18[4] = 8;
//    arrayOfByte18[5] = 15;
//    arrayOfByte18[6] = 3;
//    arrayOfByte18[8] = 11;
//    arrayOfByte18[9] = 1;
//    arrayOfByte18[10] = 2;
//    arrayOfByte18[11] = 12;
//    arrayOfByte18[12] = 5;
//    arrayOfByte18[13] = 10;
//    arrayOfByte18[14] = 14;
//    arrayOfByte18[15] = 7;
//    byte[] arrayOfByte19 = new byte[16];
//    arrayOfByte19[0] = 1;
//    arrayOfByte19[1] = 10;
//    arrayOfByte19[2] = 13;
//    arrayOfByte19[4] = 6;
//    arrayOfByte19[5] = 9;
//    arrayOfByte19[6] = 8;
//    arrayOfByte19[7] = 7;
//    arrayOfByte19[8] = 4;
//    arrayOfByte19[9] = 15;
//    arrayOfByte19[10] = 14;
//    arrayOfByte19[11] = 3;
//    arrayOfByte19[12] = 11;
//    arrayOfByte19[13] = 5;
//    arrayOfByte19[14] = 2;
//    arrayOfByte19[15] = 12;
//    byte[] arrayOfByte20 = new byte[16];
//    arrayOfByte20[0] = 7;
//    arrayOfByte20[1] = 13;
//    arrayOfByte20[2] = 14;
//    arrayOfByte20[3] = 3;
//    arrayOfByte20[5] = 6;
//    arrayOfByte20[6] = 9;
//    arrayOfByte20[7] = 10;
//    arrayOfByte20[8] = 1;
//    arrayOfByte20[9] = 2;
//    arrayOfByte20[10] = 8;
//    arrayOfByte20[11] = 5;
//    arrayOfByte20[12] = 11;
//    arrayOfByte20[13] = 12;
//    arrayOfByte20[14] = 4;
//    arrayOfByte20[15] = 15;
//    byte[] arrayOfByte21 = new byte[16];
//    arrayOfByte21[0] = 13;
//    arrayOfByte21[1] = 8;
//    arrayOfByte21[2] = 11;
//    arrayOfByte21[3] = 5;
//    arrayOfByte21[4] = 6;
//    arrayOfByte21[5] = 15;
//    arrayOfByte21[7] = 3;
//    arrayOfByte21[8] = 4;
//    arrayOfByte21[9] = 7;
//    arrayOfByte21[10] = 2;
//    arrayOfByte21[11] = 12;
//    arrayOfByte21[12] = 1;
//    arrayOfByte21[13] = 10;
//    arrayOfByte21[14] = 14;
//    arrayOfByte21[15] = 9;
//    byte[] arrayOfByte22 = new byte[16];
//    arrayOfByte22[0] = 10;
//    arrayOfByte22[1] = 6;
//    arrayOfByte22[2] = 9;
//    arrayOfByte22[4] = 12;
//    arrayOfByte22[5] = 11;
//    arrayOfByte22[6] = 7;
//    arrayOfByte22[7] = 13;
//    arrayOfByte22[8] = 15;
//    arrayOfByte22[9] = 1;
//    arrayOfByte22[10] = 3;
//    arrayOfByte22[11] = 14;
//    arrayOfByte22[12] = 5;
//    arrayOfByte22[13] = 2;
//    arrayOfByte22[14] = 8;
//    arrayOfByte22[15] = 4;
//    byte[] arrayOfByte23 = new byte[16];
//    arrayOfByte23[0] = 3;
//    arrayOfByte23[1] = 15;
//    arrayOfByte23[3] = 6;
//    arrayOfByte23[4] = 10;
//    arrayOfByte23[5] = 1;
//    arrayOfByte23[6] = 13;
//    arrayOfByte23[7] = 8;
//    arrayOfByte23[8] = 9;
//    arrayOfByte23[9] = 4;
//    arrayOfByte23[10] = 5;
//    arrayOfByte23[11] = 11;
//    arrayOfByte23[12] = 12;
//    arrayOfByte23[13] = 7;
//    arrayOfByte23[14] = 2;
//    arrayOfByte23[15] = 14;
//    byte[] arrayOfByte24 = new byte[16];
//    arrayOfByte24[0] = 2;
//    arrayOfByte24[1] = 12;
//    arrayOfByte24[2] = 4;
//    arrayOfByte24[3] = 1;
//    arrayOfByte24[4] = 7;
//    arrayOfByte24[5] = 10;
//    arrayOfByte24[6] = 11;
//    arrayOfByte24[7] = 6;
//    arrayOfByte24[8] = 8;
//    arrayOfByte24[9] = 5;
//    arrayOfByte24[10] = 3;
//    arrayOfByte24[11] = 15;
//    arrayOfByte24[12] = 13;
//    arrayOfByte24[14] = 14;
//    arrayOfByte24[15] = 9;
//    byte[] arrayOfByte25 = new byte[16];
//    arrayOfByte25[0] = 14;
//    arrayOfByte25[1] = 11;
//    arrayOfByte25[2] = 2;
//    arrayOfByte25[3] = 12;
//    arrayOfByte25[4] = 4;
//    arrayOfByte25[5] = 7;
//    arrayOfByte25[6] = 13;
//    arrayOfByte25[7] = 1;
//    arrayOfByte25[8] = 5;
//    arrayOfByte25[10] = 15;
//    arrayOfByte25[11] = 10;
//    arrayOfByte25[12] = 3;
//    arrayOfByte25[13] = 9;
//    arrayOfByte25[14] = 8;
//    arrayOfByte25[15] = 6;
//    byte[] arrayOfByte26 = new byte[16];
//    arrayOfByte26[0] = 4;
//    arrayOfByte26[1] = 2;
//    arrayOfByte26[2] = 1;
//    arrayOfByte26[3] = 11;
//    arrayOfByte26[4] = 10;
//    arrayOfByte26[5] = 13;
//    arrayOfByte26[6] = 7;
//    arrayOfByte26[7] = 8;
//    arrayOfByte26[8] = 15;
//    arrayOfByte26[9] = 9;
//    arrayOfByte26[10] = 12;
//    arrayOfByte26[11] = 5;
//    arrayOfByte26[12] = 6;
//    arrayOfByte26[13] = 3;
//    arrayOfByte26[15] = 14;
//    byte[] arrayOfByte27 = new byte[16];
//    arrayOfByte27[0] = 11;
//    arrayOfByte27[1] = 8;
//    arrayOfByte27[2] = 12;
//    arrayOfByte27[3] = 7;
//    arrayOfByte27[4] = 1;
//    arrayOfByte27[5] = 14;
//    arrayOfByte27[6] = 2;
//    arrayOfByte27[7] = 13;
//    arrayOfByte27[8] = 6;
//    arrayOfByte27[9] = 15;
//    arrayOfByte27[11] = 9;
//    arrayOfByte27[12] = 10;
//    arrayOfByte27[13] = 4;
//    arrayOfByte27[14] = 5;
//    arrayOfByte27[15] = 3;
//    byte[] arrayOfByte28 = new byte[16];
//    arrayOfByte28[0] = 12;
//    arrayOfByte28[1] = 1;
//    arrayOfByte28[2] = 10;
//    arrayOfByte28[3] = 15;
//    arrayOfByte28[4] = 9;
//    arrayOfByte28[5] = 2;
//    arrayOfByte28[6] = 6;
//    arrayOfByte28[7] = 8;
//    arrayOfByte28[9] = 13;
//    arrayOfByte28[10] = 3;
//    arrayOfByte28[11] = 4;
//    arrayOfByte28[12] = 14;
//    arrayOfByte28[13] = 7;
//    arrayOfByte28[14] = 5;
//    arrayOfByte28[15] = 11;
//    byte[] arrayOfByte29 = new byte[16];
//    arrayOfByte29[0] = 10;
//    arrayOfByte29[1] = 15;
//    arrayOfByte29[2] = 4;
//    arrayOfByte29[3] = 2;
//    arrayOfByte29[4] = 7;
//    arrayOfByte29[5] = 12;
//    arrayOfByte29[6] = 9;
//    arrayOfByte29[7] = 5;
//    arrayOfByte29[8] = 6;
//    arrayOfByte29[9] = 1;
//    arrayOfByte29[10] = 13;
//    arrayOfByte29[11] = 14;
//    arrayOfByte29[13] = 11;
//    arrayOfByte29[14] = 3;
//    arrayOfByte29[15] = 8;
//    byte[] arrayOfByte30 = new byte[16];
//    arrayOfByte30[0] = 9;
//    arrayOfByte30[1] = 14;
//    arrayOfByte30[2] = 15;
//    arrayOfByte30[3] = 5;
//    arrayOfByte30[4] = 2;
//    arrayOfByte30[5] = 8;
//    arrayOfByte30[6] = 12;
//    arrayOfByte30[7] = 3;
//    arrayOfByte30[8] = 7;
//    arrayOfByte30[10] = 4;
//    arrayOfByte30[11] = 10;
//    arrayOfByte30[12] = 1;
//    arrayOfByte30[13] = 13;
//    arrayOfByte30[14] = 11;
//    arrayOfByte30[15] = 6;
//    byte[] arrayOfByte31 = new byte[16];
//    arrayOfByte31[0] = 4;
//    arrayOfByte31[1] = 3;
//    arrayOfByte31[2] = 2;
//    arrayOfByte31[3] = 12;
//    arrayOfByte31[4] = 9;
//    arrayOfByte31[5] = 5;
//    arrayOfByte31[6] = 15;
//    arrayOfByte31[7] = 10;
//    arrayOfByte31[8] = 11;
//    arrayOfByte31[9] = 14;
//    arrayOfByte31[10] = 1;
//    arrayOfByte31[11] = 7;
//    arrayOfByte31[12] = 6;
//    arrayOfByte31[14] = 8;
//    arrayOfByte31[15] = 13;
//    byte[] arrayOfByte32 = new byte[16];
//    arrayOfByte32[0] = 4;
//    arrayOfByte32[1] = 11;
//    arrayOfByte32[2] = 2;
//    arrayOfByte32[3] = 14;
//    arrayOfByte32[4] = 15;
//    arrayOfByte32[6] = 8;
//    arrayOfByte32[7] = 13;
//    arrayOfByte32[8] = 3;
//    arrayOfByte32[9] = 12;
//    arrayOfByte32[10] = 9;
//    arrayOfByte32[11] = 7;
//    arrayOfByte32[12] = 5;
//    arrayOfByte32[13] = 10;
//    arrayOfByte32[14] = 6;
//    arrayOfByte32[15] = 1;
//    byte[] arrayOfByte33 = new byte[16];
//    arrayOfByte33[0] = 13;
//    arrayOfByte33[2] = 11;
//    arrayOfByte33[3] = 7;
//    arrayOfByte33[4] = 4;
//    arrayOfByte33[5] = 9;
//    arrayOfByte33[6] = 1;
//    arrayOfByte33[7] = 10;
//    arrayOfByte33[8] = 14;
//    arrayOfByte33[9] = 3;
//    arrayOfByte33[10] = 5;
//    arrayOfByte33[11] = 12;
//    arrayOfByte33[12] = 2;
//    arrayOfByte33[13] = 15;
//    arrayOfByte33[14] = 8;
//    arrayOfByte33[15] = 6;
//    byte[] arrayOfByte34 = new byte[16];
//    arrayOfByte34[0] = 1;
//    arrayOfByte34[1] = 4;
//    arrayOfByte34[2] = 11;
//    arrayOfByte34[3] = 13;
//    arrayOfByte34[4] = 12;
//    arrayOfByte34[5] = 3;
//    arrayOfByte34[6] = 7;
//    arrayOfByte34[7] = 14;
//    arrayOfByte34[8] = 10;
//    arrayOfByte34[9] = 15;
//    arrayOfByte34[10] = 6;
//    arrayOfByte34[11] = 8;
//    arrayOfByte34[13] = 5;
//    arrayOfByte34[14] = 9;
//    arrayOfByte34[15] = 2;
//    byte[] arrayOfByte35 = new byte[16];
//    arrayOfByte35[0] = 6;
//    arrayOfByte35[1] = 11;
//    arrayOfByte35[2] = 13;
//    arrayOfByte35[3] = 8;
//    arrayOfByte35[4] = 1;
//    arrayOfByte35[5] = 4;
//    arrayOfByte35[6] = 10;
//    arrayOfByte35[7] = 7;
//    arrayOfByte35[8] = 9;
//    arrayOfByte35[9] = 5;
//    arrayOfByte35[11] = 15;
//    arrayOfByte35[12] = 14;
//    arrayOfByte35[13] = 2;
//    arrayOfByte35[14] = 3;
//    arrayOfByte35[15] = 12;
//    byte[] arrayOfByte36 = new byte[16];
//    arrayOfByte36[0] = 13;
//    arrayOfByte36[1] = 2;
//    arrayOfByte36[2] = 8;
//    arrayOfByte36[3] = 4;
//    arrayOfByte36[4] = 6;
//    arrayOfByte36[5] = 15;
//    arrayOfByte36[6] = 11;
//    arrayOfByte36[7] = 1;
//    arrayOfByte36[8] = 10;
//    arrayOfByte36[9] = 9;
//    arrayOfByte36[10] = 3;
//    arrayOfByte36[11] = 14;
//    arrayOfByte36[12] = 5;
//    arrayOfByte36[14] = 12;
//    arrayOfByte36[15] = 7;
//    byte[] arrayOfByte37 = new byte[16];
//    arrayOfByte37[0] = 1;
//    arrayOfByte37[1] = 15;
//    arrayOfByte37[2] = 13;
//    arrayOfByte37[3] = 8;
//    arrayOfByte37[4] = 10;
//    arrayOfByte37[5] = 3;
//    arrayOfByte37[6] = 7;
//    arrayOfByte37[7] = 4;
//    arrayOfByte37[8] = 12;
//    arrayOfByte37[9] = 5;
//    arrayOfByte37[10] = 6;
//    arrayOfByte37[11] = 11;
//    arrayOfByte37[13] = 14;
//    arrayOfByte37[14] = 9;
//    arrayOfByte37[15] = 2;
//    byte[] arrayOfByte38 = new byte[16];
//    arrayOfByte38[0] = 7;
//    arrayOfByte38[1] = 11;
//    arrayOfByte38[2] = 4;
//    arrayOfByte38[3] = 1;
//    arrayOfByte38[4] = 9;
//    arrayOfByte38[5] = 12;
//    arrayOfByte38[6] = 14;
//    arrayOfByte38[7] = 2;
//    arrayOfByte38[9] = 6;
//    arrayOfByte38[10] = 10;
//    arrayOfByte38[11] = 13;
//    arrayOfByte38[12] = 15;
//    arrayOfByte38[13] = 3;
//    arrayOfByte38[14] = 5;
//    arrayOfByte38[15] = 8;
//    byte[] arrayOfByte39 = new byte[16];
//    arrayOfByte39[0] = 2;
//    arrayOfByte39[1] = 1;
//    arrayOfByte39[2] = 14;
//    arrayOfByte39[3] = 7;
//    arrayOfByte39[4] = 4;
//    arrayOfByte39[5] = 10;
//    arrayOfByte39[6] = 8;
//    arrayOfByte39[7] = 13;
//    arrayOfByte39[8] = 15;
//    arrayOfByte39[9] = 12;
//    arrayOfByte39[10] = 9;
//    arrayOfByte39[12] = 3;
//    arrayOfByte39[13] = 5;
//    arrayOfByte39[14] = 6;
//    arrayOfByte39[15] = 11;
//    byte[] arrayOfByte40 = new byte[16];
//    byte[] tmp3260_3258 = arrayOfByte40;
//    tmp3260_3258[0] = 1;
//    byte[] tmp3265_3260 = tmp3260_3258;
//    tmp3265_3260[1] = 1;
//    byte[] tmp3270_3265 = tmp3265_3260;
//    tmp3270_3265[2] = 2;
//    byte[] tmp3275_3270 = tmp3270_3265;
//    tmp3275_3270[3] = 2;
//    byte[] tmp3280_3275 = tmp3275_3270;
//    tmp3280_3275[4] = 2;
//    byte[] tmp3285_3280 = tmp3280_3275;
//    tmp3285_3280[5] = 2;
//    byte[] tmp3290_3285 = tmp3285_3280;
//    tmp3290_3285[6] = 2;
//    byte[] tmp3296_3290 = tmp3290_3285;
//    tmp3296_3290[7] = 2;
//    byte[] tmp3302_3296 = tmp3296_3290;
//    tmp3302_3296[8] = 1;
//    byte[] tmp3308_3302 = tmp3302_3296;
//    tmp3308_3302[9] = 2;
//    byte[] tmp3314_3308 = tmp3308_3302;
//    tmp3314_3308[10] = 2;
//    byte[] tmp3320_3314 = tmp3314_3308;
//    tmp3320_3314[11] = 2;
//    byte[] tmp3326_3320 = tmp3320_3314;
//    tmp3326_3320[12] = 2;
//    byte[] tmp3332_3326 = tmp3326_3320;
//    tmp3332_3326[13] = 2;
//    byte[] tmp3338_3332 = tmp3332_3326;
//    tmp3338_3332[14] = 2;
//    byte[] tmp3344_3338 = tmp3338_3332;
//    tmp3344_3338[15] = 1;
//   // tmp3344_3338;
//    byte[] arrayOfByte41 = new byte[64];
//    arrayOfByte41[7] = 1;
//    arrayOfByte41[10] = 1;
//    arrayOfByte41[14] = 1;
//    arrayOfByte41[15] = 1;
//    arrayOfByte41[17] = 1;
//    arrayOfByte41[21] = 1;
//    arrayOfByte41[23] = 1;
//    arrayOfByte41[25] = 1;
//    arrayOfByte41[26] = 1;
//    arrayOfByte41[29] = 1;
//    arrayOfByte41[30] = 1;
//    arrayOfByte41[31] = 1;
//    arrayOfByte41[32] = 1;
//    arrayOfByte41[36] = 1;
//    arrayOfByte41[39] = 1;
//    arrayOfByte41[40] = 1;
//    arrayOfByte41[42] = 1;
//    arrayOfByte41[44] = 1;
//    arrayOfByte41[46] = 1;
//    arrayOfByte41[47] = 1;
//    arrayOfByte41[48] = 1;
//    arrayOfByte41[49] = 1;
//    arrayOfByte41[52] = 1;
//    arrayOfByte41[53] = 1;
//    arrayOfByte41[55] = 1;
//    arrayOfByte41[56] = 1;
//    arrayOfByte41[57] = 1;
//    arrayOfByte41[58] = 1;
//    arrayOfByte41[60] = 1;
//    arrayOfByte41[61] = 1;
//    arrayOfByte41[62] = 1;
//    arrayOfByte41[63] = 1;
//    int k = 0;
//    if (k >= 8)
//    {
//      k = 0;
//      label3562:
//      if (k < 8) {
//        break label5564;
//      }
//      arrayOfByte2[0] = arrayOfByte6[57];
//      arrayOfByte2[1] = arrayOfByte6[49];
//      arrayOfByte2[2] = arrayOfByte6[41];
//      arrayOfByte2[3] = arrayOfByte6[33];
//      arrayOfByte2[4] = arrayOfByte6[25];
//      arrayOfByte2[5] = arrayOfByte6[17];
//      arrayOfByte2[6] = arrayOfByte6[9];
//      arrayOfByte2[7] = arrayOfByte6[1];
//      arrayOfByte2[8] = arrayOfByte6[59];
//      arrayOfByte2[9] = arrayOfByte6[51];
//      arrayOfByte2[10] = arrayOfByte6[43];
//      arrayOfByte2[11] = arrayOfByte6[35];
//      arrayOfByte2[12] = arrayOfByte6[27];
//      arrayOfByte2[13] = arrayOfByte6[19];
//      arrayOfByte2[14] = arrayOfByte6[11];
//      arrayOfByte2[15] = arrayOfByte6[3];
//      arrayOfByte2[16] = arrayOfByte6[61];
//      arrayOfByte2[17] = arrayOfByte6[53];
//      arrayOfByte2[18] = arrayOfByte6[45];
//      arrayOfByte2[19] = arrayOfByte6[37];
//      arrayOfByte2[20] = arrayOfByte6[29];
//      arrayOfByte2[21] = arrayOfByte6[21];
//      arrayOfByte2[22] = arrayOfByte6[13];
//      arrayOfByte2[23] = arrayOfByte6[5];
//      arrayOfByte2[24] = arrayOfByte6[63];
//      arrayOfByte2[25] = arrayOfByte6[55];
//      arrayOfByte2[26] = arrayOfByte6[47];
//      arrayOfByte2[27] = arrayOfByte6[39];
//      arrayOfByte2[28] = arrayOfByte6[31];
//      arrayOfByte2[29] = arrayOfByte6[23];
//      arrayOfByte2[30] = arrayOfByte6[15];
//      arrayOfByte2[31] = arrayOfByte6[7];
//      arrayOfByte2[32] = arrayOfByte6[56];
//      arrayOfByte2[33] = arrayOfByte6[48];
//      arrayOfByte2[34] = arrayOfByte6[40];
//      arrayOfByte2[35] = arrayOfByte6[32];
//      arrayOfByte2[36] = arrayOfByte6[24];
//      arrayOfByte2[37] = arrayOfByte6[16];
//      arrayOfByte2[38] = arrayOfByte6[8];
//      arrayOfByte2[39] = arrayOfByte6[0];
//      arrayOfByte2[40] = arrayOfByte6[58];
//      arrayOfByte2[41] = arrayOfByte6[50];
//      arrayOfByte2[42] = arrayOfByte6[42];
//      arrayOfByte2[43] = arrayOfByte6[34];
//      arrayOfByte2[44] = arrayOfByte6[26];
//      arrayOfByte2[45] = arrayOfByte6[18];
//      arrayOfByte2[46] = arrayOfByte6[10];
//      arrayOfByte2[47] = arrayOfByte6[2];
//      arrayOfByte2[48] = arrayOfByte6[60];
//      arrayOfByte2[49] = arrayOfByte6[52];
//      arrayOfByte2[50] = arrayOfByte6[44];
//      arrayOfByte2[51] = arrayOfByte6[36];
//      arrayOfByte2[52] = arrayOfByte6[28];
//      arrayOfByte2[53] = arrayOfByte6[20];
//      arrayOfByte2[54] = arrayOfByte6[12];
//      arrayOfByte2[55] = arrayOfByte6[4];
//      arrayOfByte2[56] = arrayOfByte6[62];
//      arrayOfByte2[57] = arrayOfByte6[54];
//      arrayOfByte2[58] = arrayOfByte6[46];
//      arrayOfByte2[59] = arrayOfByte6[38];
//      arrayOfByte2[60] = arrayOfByte6[30];
//      arrayOfByte2[61] = arrayOfByte6[22];
//      arrayOfByte2[62] = arrayOfByte6[14];
//      arrayOfByte2[63] = arrayOfByte6[6];
//      arrayOfByte3[0] = arrayOfByte7[56];
//      arrayOfByte3[1] = arrayOfByte7[48];
//      arrayOfByte3[2] = arrayOfByte7[40];
//      arrayOfByte3[3] = arrayOfByte7[32];
//      arrayOfByte3[4] = arrayOfByte7[24];
//      arrayOfByte3[5] = arrayOfByte7[16];
//      arrayOfByte3[6] = arrayOfByte7[8];
//      arrayOfByte3[7] = arrayOfByte7[0];
//      arrayOfByte3[8] = arrayOfByte7[57];
//      arrayOfByte3[9] = arrayOfByte7[49];
//      arrayOfByte3[10] = arrayOfByte7[41];
//      arrayOfByte3[11] = arrayOfByte7[33];
//      arrayOfByte3[12] = arrayOfByte7[25];
//      arrayOfByte3[13] = arrayOfByte7[17];
//      arrayOfByte3[14] = arrayOfByte7[9];
//      arrayOfByte3[15] = arrayOfByte7[1];
//      arrayOfByte3[16] = arrayOfByte7[58];
//      arrayOfByte3[17] = arrayOfByte7[50];
//      arrayOfByte3[18] = arrayOfByte7[42];
//      arrayOfByte3[19] = arrayOfByte7[34];
//      arrayOfByte3[20] = arrayOfByte7[26];
//      arrayOfByte3[21] = arrayOfByte7[18];
//      arrayOfByte3[22] = arrayOfByte7[10];
//      arrayOfByte3[23] = arrayOfByte7[2];
//      arrayOfByte3[24] = arrayOfByte7[59];
//      arrayOfByte3[25] = arrayOfByte7[51];
//      arrayOfByte3[26] = arrayOfByte7[43];
//      arrayOfByte3[27] = arrayOfByte7[35];
//      arrayOfByte3[28] = arrayOfByte7[62];
//      arrayOfByte3[29] = arrayOfByte7[54];
//      arrayOfByte3[30] = arrayOfByte7[46];
//      arrayOfByte3[31] = arrayOfByte7[38];
//      arrayOfByte3[32] = arrayOfByte7[30];
//      arrayOfByte3[33] = arrayOfByte7[22];
//      arrayOfByte3[34] = arrayOfByte7[14];
//      arrayOfByte3[35] = arrayOfByte7[6];
//      arrayOfByte3[36] = arrayOfByte7[61];
//      arrayOfByte3[37] = arrayOfByte7[53];
//      arrayOfByte3[38] = arrayOfByte7[45];
//      arrayOfByte3[39] = arrayOfByte7[37];
//      arrayOfByte3[40] = arrayOfByte7[29];
//      arrayOfByte3[41] = arrayOfByte7[21];
//      arrayOfByte3[42] = arrayOfByte7[13];
//      arrayOfByte3[43] = arrayOfByte7[5];
//      arrayOfByte3[44] = arrayOfByte7[60];
//      arrayOfByte3[45] = arrayOfByte7[52];
//      arrayOfByte3[46] = arrayOfByte7[44];
//      arrayOfByte3[47] = arrayOfByte7[36];
//      arrayOfByte3[48] = arrayOfByte7[28];
//      arrayOfByte3[49] = arrayOfByte7[20];
//      arrayOfByte3[50] = arrayOfByte7[12];
//      arrayOfByte3[51] = arrayOfByte7[4];
//      arrayOfByte3[52] = arrayOfByte7[27];
//      arrayOfByte3[53] = arrayOfByte7[19];
//      arrayOfByte3[54] = arrayOfByte7[11];
//      arrayOfByte3[55] = arrayOfByte7[3];
//      k = 1;
//      if (k < 17) {
//        break label5723;
//      }
//      paramInt = 0;
//    }
//    for (;;)
//    {
//      if (paramInt >= 32)
//      {
//        arrayOfByte6[0] = arrayOfByte2[39];
//        arrayOfByte6[1] = arrayOfByte2[7];
//        arrayOfByte6[2] = arrayOfByte2[47];
//        arrayOfByte6[3] = arrayOfByte2[15];
//        arrayOfByte6[4] = arrayOfByte2[55];
//        arrayOfByte6[5] = arrayOfByte2[23];
//        arrayOfByte6[6] = arrayOfByte2[63];
//        arrayOfByte6[7] = arrayOfByte2[31];
//        arrayOfByte6[8] = arrayOfByte2[38];
//        arrayOfByte6[9] = arrayOfByte2[6];
//        arrayOfByte6[10] = arrayOfByte2[46];
//        arrayOfByte6[11] = arrayOfByte2[14];
//        arrayOfByte6[12] = arrayOfByte2[54];
//        arrayOfByte6[13] = arrayOfByte2[22];
//        arrayOfByte6[14] = arrayOfByte2[62];
//        arrayOfByte6[15] = arrayOfByte2[30];
//        arrayOfByte6[16] = arrayOfByte2[37];
//        arrayOfByte6[17] = arrayOfByte2[5];
//        arrayOfByte6[18] = arrayOfByte2[45];
//        arrayOfByte6[19] = arrayOfByte2[13];
//        arrayOfByte6[20] = arrayOfByte2[53];
//        arrayOfByte6[21] = arrayOfByte2[21];
//        arrayOfByte6[22] = arrayOfByte2[61];
//        arrayOfByte6[23] = arrayOfByte2[29];
//        arrayOfByte6[24] = arrayOfByte2[36];
//        arrayOfByte6[25] = arrayOfByte2[4];
//        arrayOfByte6[26] = arrayOfByte2[44];
//        arrayOfByte6[27] = arrayOfByte2[12];
//        arrayOfByte6[28] = arrayOfByte2[52];
//        arrayOfByte6[29] = arrayOfByte2[20];
//        arrayOfByte6[30] = arrayOfByte2[60];
//        arrayOfByte6[31] = arrayOfByte2[28];
//        arrayOfByte6[32] = arrayOfByte2[35];
//        arrayOfByte6[33] = arrayOfByte2[3];
//        arrayOfByte6[34] = arrayOfByte2[43];
//        arrayOfByte6[35] = arrayOfByte2[11];
//        arrayOfByte6[36] = arrayOfByte2[51];
//        arrayOfByte6[37] = arrayOfByte2[19];
//        arrayOfByte6[38] = arrayOfByte2[59];
//        arrayOfByte6[39] = arrayOfByte2[27];
//        arrayOfByte6[40] = arrayOfByte2[34];
//        arrayOfByte6[41] = arrayOfByte2[2];
//        arrayOfByte6[42] = arrayOfByte2[42];
//        arrayOfByte6[43] = arrayOfByte2[10];
//        arrayOfByte6[44] = arrayOfByte2[50];
//        arrayOfByte6[45] = arrayOfByte2[18];
//        arrayOfByte6[46] = arrayOfByte2[58];
//        arrayOfByte6[47] = arrayOfByte2[26];
//        arrayOfByte6[48] = arrayOfByte2[33];
//        arrayOfByte6[49] = arrayOfByte2[1];
//        arrayOfByte6[50] = arrayOfByte2[41];
//        arrayOfByte6[51] = arrayOfByte2[9];
//        arrayOfByte6[52] = arrayOfByte2[49];
//        arrayOfByte6[53] = arrayOfByte2[17];
//        arrayOfByte6[54] = arrayOfByte2[57];
//        arrayOfByte6[55] = arrayOfByte2[25];
//        arrayOfByte6[56] = arrayOfByte2[32];
//        arrayOfByte6[57] = arrayOfByte2[0];
//        arrayOfByte6[58] = arrayOfByte2[40];
//        arrayOfByte6[59] = arrayOfByte2[8];
//        arrayOfByte6[60] = arrayOfByte2[48];
//        arrayOfByte6[61] = arrayOfByte2[16];
//        arrayOfByte6[62] = arrayOfByte2[56];
//        arrayOfByte6[63] = arrayOfByte2[24];
//        k = 0;
//        paramInt = 0;
//        if (paramInt < 8) {
//          break label8393;
//        }
//        return arrayOfByte1;
//        byte m = (byte)(paramArrayOfByte2[k]) & 0xFF;
//        arrayOfByte7[(k * 8)] = ((byte)(m / 128 % 2));
//        arrayOfByte7[(k * 8 + 1)] = ((byte)(m / 64 % 2));
//        arrayOfByte7[(k * 8 + 2)] = ((byte)(m / 32 % 2));
//        arrayOfByte7[(k * 8 + 3)] = ((byte)(m / 16 % 2));
//        arrayOfByte7[(k * 8 + 4)] = ((byte)(m / 8 % 2));
//        arrayOfByte7[(k * 8 + 5)] = ((byte)(m / 4 % 2));
//        arrayOfByte7[(k * 8 + 6)] = ((byte)(m / 2 % 2));
//        arrayOfByte7[(k * 8 + 7)] = ((byte)(m % 2));
//        k += 1;
//        break;
//        label5564:
//         m = paramArrayOfByte1[k] & 0xFF;
//        arrayOfByte6[(k * 8)] = ((byte)(m / 128 % 2));
//        arrayOfByte6[(k * 8 + 1)] = ((byte)(m / 64 % 2));
//        arrayOfByte6[(k * 8 + 2)] = ((byte)(m / 32 % 2));
//        arrayOfByte6[(k * 8 + 3)] = ((byte)(m / 16 % 2));
//        arrayOfByte6[(k * 8 + 4)] = ((byte)(m / 8 % 2));
//        arrayOfByte6[(k * 8 + 5)] = ((byte)(m / 4 % 2));
//        arrayOfByte6[(k * 8 + 6)] = ((byte)(m / 2 % 2));
//        arrayOfByte6[(k * 8 + 7)] = ((byte)(m % 2));
//        k += 1;
//        break label3562;
//        label5723:
//        m = 0;
//        //break label5726:
//        int i1;
//        //break label6226:
//       // break label6697:
//        int n;
//        if (m >= 32)
//        {
//          arrayOfByte4[0] = arrayOfByte6[31];
//          arrayOfByte4[1] = arrayOfByte6[0];
//          arrayOfByte4[2] = arrayOfByte6[1];
//          arrayOfByte4[3] = arrayOfByte6[2];
//          arrayOfByte4[4] = arrayOfByte6[3];
//          arrayOfByte4[5] = arrayOfByte6[4];
//          arrayOfByte4[6] = arrayOfByte6[3];
//          arrayOfByte4[7] = arrayOfByte6[4];
//          arrayOfByte4[8] = arrayOfByte6[5];
//          arrayOfByte4[9] = arrayOfByte6[6];
//          arrayOfByte4[10] = arrayOfByte6[7];
//          arrayOfByte4[11] = arrayOfByte6[8];
//          arrayOfByte4[12] = arrayOfByte6[7];
//          arrayOfByte4[13] = arrayOfByte6[8];
//          arrayOfByte4[14] = arrayOfByte6[9];
//          arrayOfByte4[15] = arrayOfByte6[10];
//          arrayOfByte4[16] = arrayOfByte6[11];
//          arrayOfByte4[17] = arrayOfByte6[12];
//          arrayOfByte4[18] = arrayOfByte6[11];
//          arrayOfByte4[19] = arrayOfByte6[12];
//          arrayOfByte4[20] = arrayOfByte6[13];
//          arrayOfByte4[21] = arrayOfByte6[14];
//          arrayOfByte4[22] = arrayOfByte6[15];
//          arrayOfByte4[23] = arrayOfByte6[16];
//          arrayOfByte4[24] = arrayOfByte6[15];
//          arrayOfByte4[25] = arrayOfByte6[16];
//          arrayOfByte4[26] = arrayOfByte6[17];
//          arrayOfByte4[27] = arrayOfByte6[18];
//          arrayOfByte4[28] = arrayOfByte6[19];
//          arrayOfByte4[29] = arrayOfByte6[20];
//          arrayOfByte4[30] = arrayOfByte6[19];
//          arrayOfByte4[31] = arrayOfByte6[20];
//          arrayOfByte4[32] = arrayOfByte6[21];
//          arrayOfByte4[33] = arrayOfByte6[22];
//          arrayOfByte4[34] = arrayOfByte6[23];
//          arrayOfByte4[35] = arrayOfByte6[24];
//          arrayOfByte4[36] = arrayOfByte6[23];
//          arrayOfByte4[37] = arrayOfByte6[24];
//          arrayOfByte4[38] = arrayOfByte6[25];
//          arrayOfByte4[39] = arrayOfByte6[26];
//          arrayOfByte4[40] = arrayOfByte6[27];
//          arrayOfByte4[41] = arrayOfByte6[28];
//          arrayOfByte4[42] = arrayOfByte6[27];
//          arrayOfByte4[43] = arrayOfByte6[28];
//          arrayOfByte4[44] = arrayOfByte6[29];
//          arrayOfByte4[45] = arrayOfByte6[30];
//          arrayOfByte4[46] = arrayOfByte6[31];
//          arrayOfByte4[47] = arrayOfByte6[0];
//          if (paramInt == 0) {
//            break label8178;
//          }
//          i1 = (byte)(arrayOfByte40[(k - 1)] & 0xFF);
//          m = 0;
//          if (m < i1) {
//            break label8097;
//          }
//          arrayOfByte5[0] = arrayOfByte3[13];
//          arrayOfByte5[1] = arrayOfByte3[16];
//          arrayOfByte5[2] = arrayOfByte3[10];
//          arrayOfByte5[3] = arrayOfByte3[23];
//          arrayOfByte5[4] = arrayOfByte3[0];
//          arrayOfByte5[5] = arrayOfByte3[4];
//          arrayOfByte5[6] = arrayOfByte3[2];
//          arrayOfByte5[7] = arrayOfByte3[27];
//          arrayOfByte5[8] = arrayOfByte3[14];
//          arrayOfByte5[9] = arrayOfByte3[5];
//          arrayOfByte5[10] = arrayOfByte3[20];
//          arrayOfByte5[11] = arrayOfByte3[9];
//          arrayOfByte5[12] = arrayOfByte3[22];
//          arrayOfByte5[13] = arrayOfByte3[18];
//          arrayOfByte5[14] = arrayOfByte3[11];
//          arrayOfByte5[15] = arrayOfByte3[3];
//          arrayOfByte5[16] = arrayOfByte3[25];
//          arrayOfByte5[17] = arrayOfByte3[7];
//          arrayOfByte5[18] = arrayOfByte3[15];
//          arrayOfByte5[19] = arrayOfByte3[6];
//          arrayOfByte5[20] = arrayOfByte3[26];
//          arrayOfByte5[21] = arrayOfByte3[19];
//          arrayOfByte5[22] = arrayOfByte3[12];
//          arrayOfByte5[23] = arrayOfByte3[1];
//          arrayOfByte5[24] = arrayOfByte3[40];
//          arrayOfByte5[25] = arrayOfByte3[51];
//          arrayOfByte5[26] = arrayOfByte3[30];
//          arrayOfByte5[27] = arrayOfByte3[36];
//          arrayOfByte5[28] = arrayOfByte3[46];
//          arrayOfByte5[29] = arrayOfByte3[54];
//          arrayOfByte5[30] = arrayOfByte3[29];
//          arrayOfByte5[31] = arrayOfByte3[39];
//          arrayOfByte5[32] = arrayOfByte3[50];
//          arrayOfByte5[33] = arrayOfByte3[44];
//          arrayOfByte5[34] = arrayOfByte3[32];
//          arrayOfByte5[35] = arrayOfByte3[47];
//          arrayOfByte5[36] = arrayOfByte3[43];
//          arrayOfByte5[37] = arrayOfByte3[48];
//          arrayOfByte5[38] = arrayOfByte3[38];
//          arrayOfByte5[39] = arrayOfByte3[55];
//          arrayOfByte5[40] = arrayOfByte3[33];
//          arrayOfByte5[41] = arrayOfByte3[52];
//          arrayOfByte5[42] = arrayOfByte3[45];
//          arrayOfByte5[43] = arrayOfByte3[41];
//          arrayOfByte5[44] = arrayOfByte3[49];
//          arrayOfByte5[45] = arrayOfByte3[35];
//          arrayOfByte5[46] = arrayOfByte3[28];
//          arrayOfByte5[47] = arrayOfByte3[31];
//          m = 0;
//          if (m < 48) {
//            break label8294;
//          }
//          m = arrayOfByte4[0];
//          n = arrayOfByte4[5];
//          m = (new byte[][] { arrayOfByte8, arrayOfByte9, arrayOfByte10, arrayOfByte11 }[(m * 2 + n)][(((arrayOfByte4[1] * 2 + arrayOfByte4[2]) * 2 + arrayOfByte4[3]) * 2 + arrayOfByte4[4])] & 0xFF) * 4;
//          arrayOfByte5[0] = arrayOfByte41[(m + 0)];
//          arrayOfByte5[1] = arrayOfByte41[(m + 1)];
//          arrayOfByte5[2] = arrayOfByte41[(m + 2)];
//          arrayOfByte5[3] = arrayOfByte41[(m + 3)];
//          m = arrayOfByte4[6];
//          n = arrayOfByte4[11];
//          m = (new byte[][] { arrayOfByte12, arrayOfByte13, arrayOfByte14, arrayOfByte15 }[(m * 2 + n)][(((arrayOfByte4[7] * 2 + arrayOfByte4[8]) * 2 + arrayOfByte4[9]) * 2 + arrayOfByte4[10])] & 0xFF) * 4;
//          arrayOfByte5[4] = arrayOfByte41[(m + 0)];
//          arrayOfByte5[5] = arrayOfByte41[(m + 1)];
//          arrayOfByte5[6] = arrayOfByte41[(m + 2)];
//          arrayOfByte5[7] = arrayOfByte41[(m + 3)];
//          m = arrayOfByte4[12];
//          n = arrayOfByte4[17];
//          m = (new byte[][] { arrayOfByte16, arrayOfByte17, arrayOfByte18, arrayOfByte19 }[(m * 2 + n)][(((arrayOfByte4[13] * 2 + arrayOfByte4[14]) * 2 + arrayOfByte4[15]) * 2 + arrayOfByte4[16])] & 0xFF) * 4;
//          arrayOfByte5[8] = arrayOfByte41[(m + 0)];
//          arrayOfByte5[9] = arrayOfByte41[(m + 1)];
//          arrayOfByte5[10] = arrayOfByte41[(m + 2)];
//          arrayOfByte5[11] = arrayOfByte41[(m + 3)];
//          m = arrayOfByte4[18];
//          n = arrayOfByte4[23];
//          m = (new byte[][] { arrayOfByte20, arrayOfByte21, arrayOfByte22, arrayOfByte23 }[(m * 2 + n)][(((arrayOfByte4[19] * 2 + arrayOfByte4[20]) * 2 + arrayOfByte4[21]) * 2 + arrayOfByte4[22])] & 0xFF) * 4;
//          arrayOfByte5[12] = arrayOfByte41[(m + 0)];
//          arrayOfByte5[13] = arrayOfByte41[(m + 1)];
//          arrayOfByte5[14] = arrayOfByte41[(m + 2)];
//          arrayOfByte5[15] = arrayOfByte41[(m + 3)];
//          m = arrayOfByte4[24];
//          n = arrayOfByte4[29];
//          m = (new byte[][] { arrayOfByte24, arrayOfByte25, arrayOfByte26, arrayOfByte27 }[(m * 2 + n)][(((arrayOfByte4[25] * 2 + arrayOfByte4[26]) * 2 + arrayOfByte4[27]) * 2 + arrayOfByte4[28])] & 0xFF) * 4;
//          arrayOfByte5[16] = arrayOfByte41[(m + 0)];
//          arrayOfByte5[17] = arrayOfByte41[(m + 1)];
//          arrayOfByte5[18] = arrayOfByte41[(m + 2)];
//          arrayOfByte5[19] = arrayOfByte41[(m + 3)];
//          m = arrayOfByte4[30];
//          n = arrayOfByte4[35];
//          m = (new byte[][] { arrayOfByte28, arrayOfByte29, arrayOfByte30, arrayOfByte31 }[(m * 2 + n)][(((arrayOfByte4[31] * 2 + arrayOfByte4[32]) * 2 + arrayOfByte4[33]) * 2 + arrayOfByte4[34])] & 0xFF) * 4;
//          arrayOfByte5[20] = arrayOfByte41[(m + 0)];
//          arrayOfByte5[21] = arrayOfByte41[(m + 1)];
//          arrayOfByte5[22] = arrayOfByte41[(m + 2)];
//          arrayOfByte5[23] = arrayOfByte41[(m + 3)];
//          m = arrayOfByte4[36];
//          n = arrayOfByte4[41];
//          m = (new byte[][] { arrayOfByte32, arrayOfByte33, arrayOfByte34, arrayOfByte35 }[(m * 2 + n)][(((arrayOfByte4[37] * 2 + arrayOfByte4[38]) * 2 + arrayOfByte4[39]) * 2 + arrayOfByte4[40])] & 0xFF) * 4;
//          arrayOfByte5[24] = arrayOfByte41[(m + 0)];
//          arrayOfByte5[25] = arrayOfByte41[(m + 1)];
//          arrayOfByte5[26] = arrayOfByte41[(m + 2)];
//          arrayOfByte5[27] = arrayOfByte41[(m + 3)];
//          m = arrayOfByte4[42];
//          n = arrayOfByte4[47];
//          m = (new byte[][] { arrayOfByte36, arrayOfByte37, arrayOfByte38, arrayOfByte39 }[(m * 2 + n)][(((arrayOfByte4[43] * 2 + arrayOfByte4[44]) * 2 + arrayOfByte4[45]) * 2 + arrayOfByte4[46])] & 0xFF) * 4;
//          arrayOfByte5[28] = arrayOfByte41[(m + 0)];
//          arrayOfByte5[29] = arrayOfByte41[(m + 1)];
//          arrayOfByte5[30] = arrayOfByte41[(m + 2)];
//          arrayOfByte5[31] = arrayOfByte41[(m + 3)];
//          arrayOfByte4[0] = arrayOfByte5[15];
//          arrayOfByte4[1] = arrayOfByte5[6];
//          arrayOfByte4[2] = arrayOfByte5[19];
//          arrayOfByte4[3] = arrayOfByte5[20];
//          arrayOfByte4[4] = arrayOfByte5[28];
//          arrayOfByte4[5] = arrayOfByte5[11];
//          arrayOfByte4[6] = arrayOfByte5[27];
//          arrayOfByte4[7] = arrayOfByte5[16];
//          arrayOfByte4[8] = arrayOfByte5[0];
//          arrayOfByte4[9] = arrayOfByte5[14];
//          arrayOfByte4[10] = arrayOfByte5[22];
//          arrayOfByte4[11] = arrayOfByte5[25];
//          arrayOfByte4[12] = arrayOfByte5[4];
//          arrayOfByte4[13] = arrayOfByte5[17];
//          arrayOfByte4[14] = arrayOfByte5[30];
//          arrayOfByte4[15] = arrayOfByte5[9];
//          arrayOfByte4[16] = arrayOfByte5[1];
//          arrayOfByte4[17] = arrayOfByte5[7];
//          arrayOfByte4[18] = arrayOfByte5[23];
//          arrayOfByte4[19] = arrayOfByte5[13];
//          arrayOfByte4[20] = arrayOfByte5[31];
//          arrayOfByte4[21] = arrayOfByte5[26];
//          arrayOfByte4[22] = arrayOfByte5[2];
//          arrayOfByte4[23] = arrayOfByte5[8];
//          arrayOfByte4[24] = arrayOfByte5[18];
//          arrayOfByte4[25] = arrayOfByte5[12];
//          arrayOfByte4[26] = arrayOfByte5[29];
//          arrayOfByte4[27] = arrayOfByte5[5];
//          arrayOfByte4[28] = arrayOfByte5[21];
//          arrayOfByte4[29] = arrayOfByte5[10];
//          arrayOfByte4[30] = arrayOfByte5[3];
//          arrayOfByte4[31] = arrayOfByte5[24];
//          m = 0;
//        }
//        for (;;)
//        {
//          if (m >= 32)
//          {
//            k += 1;
//            break;
//            arrayOfByte6[m] = arrayOfByte2[(m + 32)];
//            m += 1;
//            break label5726;
//            label8097:
//            int i = arrayOfByte3[0];
//            int j = arrayOfByte3[28];
//            n = 0;
//            for (;;)
//            {
//              if (n >= 27)
//              {
//                arrayOfByte3[27] = i;
//                arrayOfByte3[55] = j;
//                m += 1;
//                break;
//              }
//              arrayOfByte3[n] = arrayOfByte3[(n + 1)];
//              arrayOfByte3[(n + 28)] = arrayOfByte3[(n + 29)];
//              n += 1;
//            }
//            label8178:
//            if (k <= 1) {
//              break label6226;
//            }
//            i1 = (byte)(arrayOfByte40[(17 - k)] & 0xFF);
//            m = 0;
//            label8202:
//            if (m < (byte)(i1 & 0xFF))
//            {
//              i = arrayOfByte3[27];
//              j = arrayOfByte3[55];
//              n = 27;
//            }
//            for (;;)
//            {
//              if (n <= 0)
//              {
//                arrayOfByte3[0] = i;
//                arrayOfByte3[28] = j;
//                m += 1;
//                break label8202;
//                break;
//              }
//              arrayOfByte3[n] = arrayOfByte3[(n - 1)];
//              arrayOfByte3[(n + 28)] = arrayOfByte3[(n + 27)];
//              n -= 1;
//            }
//            label8294:
//            arrayOfByte4[m] = ((byte)(arrayOfByte4[m] ^ arrayOfByte5[m]));
//            m += 1;
//            break label6697;
//          }
//          arrayOfByte2[(m + 32)] = ((byte)(arrayOfByte2[m] ^ arrayOfByte4[m]));
//          arrayOfByte2[m] = arrayOfByte6[m];
//          m += 1;
//        }
//      }
//      k = arrayOfByte2[paramInt];
//      arrayOfByte2[paramInt] = arrayOfByte2[(paramInt + 32)];
//      arrayOfByte2[(paramInt + 32)] = ((byte)k);
//      paramInt += 1;
//    }
//    label8393:
//    arrayOfByte1[paramInt] = 0;
//    int m = 0;
//    for (;;)
//    {
//      if (m >= 7)
//      {
//        arrayOfByte1[paramInt] = ((byte)(arrayOfByte1[paramInt] + arrayOfByte6[(k + 7)]));
//        k += 8;
//        paramInt += 1;
//        break;
//      }
//      arrayOfByte1[paramInt] = ((byte)((arrayOfByte1[paramInt] + arrayOfByte6[(k + m)]) * 2));
//      m += 1;
//    }
//  }
  public static byte[] getBytes(int paramStringy)
  {
	  String ss =    String.format("%d", paramStringy);
  
    return getBytes(ss);
  }
  public static byte[] getBytes(String paramStringy)
  {
    if (paramStringy == null) {
      return EMPTY;
    }
    try
    {
      byte[] paramStringx = paramStringy.getBytes("utf-8");
      return paramStringx;
    }
    catch (UnsupportedEncodingException paramString)
    {
      paramString.printStackTrace();
    }
    return EMPTY;
  }
  
  public static byte[] subarray(byte[] src, int begin, int end)
  {
	  int count = end - begin;
	   byte[] bs = new byte[count];
       System.arraycopy(src, begin, bs, 0, count);
       return bs;

  }
  public static byte[] subarray2(byte[] src, int begin, int count)
  {
	   byte[] bs = new byte[count];
       System.arraycopy(src, begin, bs, 0, count);
       return bs;

  }
  public static byte[] getBytes(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return EMPTY;
    }
    try
    {
    	byte[] paramString3 = paramString1.getBytes(paramString2);
      return paramString3;
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return EMPTY;
  }
  
  private static byte[] getBytes(char[] paramArrayOfChar)
  {
    Charset localCharset = Charset.forName("UTF-8");
    CharBuffer localCharBuffer = CharBuffer.allocate(paramArrayOfChar.length);
    localCharBuffer.put(paramArrayOfChar);
    localCharBuffer.flip();
    return localCharset.encode(localCharBuffer).array();
  }
  
  private static char[] getChars(byte[] paramArrayOfByte)
  {
    Charset localCharset = Charset.forName("UTF-8");
    ByteBuffer localByteBuffer = ByteBuffer.allocate(paramArrayOfByte.length);
    localByteBuffer.put(paramArrayOfByte);
    localByteBuffer.flip();
    return localCharset.decode(localByteBuffer).array();
  }
  
  public static byte[] getPackageFromSocket(InputStream paramInputStream, boolean paramBoolean)
    throws IOException
  {
    byte[] arrayOfByte1 = new byte[0];
    byte[] arrayOfByte3 = new byte[2];
    long l1 = System.currentTimeMillis();
    int i = paramInputStream.read(arrayOfByte3);
    long l2 = System.currentTimeMillis();
    System.err.println("获取长度:::::::::::::" + (l2 - l1));
    byte[] arrayOfByte2 = arrayOfByte1;
    int j = 0;
    if (i == 2)
    {
      j = bytesToShort(arrayOfByte3, paramBoolean);
      arrayOfByte2 = new byte['Ѐ'];
      i = 0;
    }
    else
    {
      if (i >= j)
      {
        arrayOfByte2 = arrayOfByte1;
        return merge(new byte[][] { arrayOfByte3, arrayOfByte2 });
      }
      l1 = System.currentTimeMillis();
      int k = paramInputStream.read(arrayOfByte2);
      l2 = System.currentTimeMillis();
      System.err.println("获取数据:::::::::::::" + (l2 - l1));
      i += k;
      arrayOfByte1 = merge(new byte[][] { arrayOfByte1, subarray(arrayOfByte2, 0, k) });
    }
    return arrayOfByte1;
  }
  
  public static String getString(byte[] paramArrayOfByte)
  {
    return getString(paramArrayOfByte, "utf-8");
  }
  
  public static String getString(byte[] paramArrayOfByte, String paramString)
  {
    return new String(paramArrayOfByte, Charset.forName(paramString));
  }
  
 /* public static String hexToBinaryString(String paramString)
  {
    return StringUtils.leftPad(Long.toBinaryString(Long.parseLong(paramString, 16)), paramString.length() * 4, '0');
  }
  */
  
  public static byte[] intBigToSmall(byte[] paramArrayOfByte)
  {
    return intToBytes((int)bytesToInt(paramArrayOfByte, false), true);
  }
  
  public static byte[] intSmallToBig(byte[] paramArrayOfByte)
  {
    return intToBytes((int)bytesToInt(paramArrayOfByte, false), true);
  }
  
  public static byte[] intToBytes(int paramInt, boolean paramBoolean)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(4);
    if (paramBoolean) {
      localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }
    else
    {
        localByteBuffer.order(ByteOrder.BIG_ENDIAN);
    }
  //  for (;;)
    {
      localByteBuffer.putInt(paramInt);
      return localByteBuffer.array();
  
    }
  }
  
  public static String kMKXorDecrypt(String paramString1, String paramString2)
    throws NoSuchAlgorithmException
  {
   String paramString1x = md5(paramString1).toLowerCase();
    byte[] paramString2x = getBytes(paramString2.toCharArray());
    int k = paramString2x.length;
    int m = paramString1x.length();
    int i = 0;
    int j = 0;
    for (;;)
    {
      if (i >= k) {
        return String.valueOf(getChars(paramString2x)).trim();
      }
      int n = paramString2x[i];
      int i1 = (byte)paramString1x.charAt(j);
      if ((n >= 48) && (n <= 57)) {
        paramString2x[i] = ((byte)((n - 48 + (10 - i1 % 10)) % 10 + 48));
      }
      j = (j + 1) % m;
      i += 1;
    }
  }
  
  public static String kMKXorEncrypt(String paramString1x, String paramString2)
  {
	  String paramString2x = md5(paramString2);
    int k = paramString1x.length();
    char[] paramString1 = paramString1x.toCharArray();
    char[] arrayOfChar = paramString2x.toCharArray();
    int i = 0;
    int j = 0;
    StringBuilder paramString2y = new StringBuilder();
    if (i >= k)
    {
    	
      j = paramString1.length;
      i = 0;
    }
    for (;;)
    {
      if (i >= j)
      {
    
        int m = paramString1[i];
        int n = arrayOfChar[j];
        if ((m >= 48) && (m <= 57)) {
          paramString1[i] = ((char)((m - 48 + n % 10) % 10 + 48));
        }
        j = (j + 1) % paramString2.length();
        i += 1;
        break;
      }
      paramString2y.append(paramString1[i]);
      i += 1;
    }
    return paramString2y.toString();
  }
  
  public static String md5(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    byte[] paramStringx = null;
    int i = 0;
    for (;;)
    {
      try
      {
    	  MessageDigest localObject = MessageDigest.getInstance("MD5");
        ((MessageDigest)localObject).reset();
        ((MessageDigest)localObject).update(paramString.getBytes());
        paramStringx = ((MessageDigest)localObject).digest();
        int j = paramStringx.length;
       // int i = 0;
        if (i < j) {
          continue;
        }
        break;
      }
      catch (NoSuchAlgorithmException e)
      {
      //  Object localObject;
     
        e.printStackTrace();
      //  continue;
      }
  
      String localObject = Integer.toHexString(paramStringx[i] & 0xFF);
      if (((String)localObject).length() == 1) {
        localStringBuilder.append("0");
      }
      localStringBuilder.append(((String)localObject).toUpperCase());
      i += 1;
    }
    return localStringBuilder.toString();
  }
  
  public static byte[] merge(byte[]... paramVarArgs)
  {
	    int length_byte = 0;
        for (int i = 0; i < paramVarArgs.length; i++) {
        	if (paramVarArgs[i] != null)
        		length_byte += paramVarArgs[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < paramVarArgs.length; i++) {
            byte[] b = paramVarArgs[i];
            if (b != null)
            {
            	System.arraycopy(b, 0, all_byte, countLength, b.length);
            	countLength += b.length;
            }
        }
        return all_byte;
  }
  
  public static byte[] reverse(byte[] paramArrayOfByte)
  {
    int k = paramArrayOfByte.length;
    byte[] arrayOfByte = new byte[k];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, k);
    int j = 0;
    for (;;)
    {
      if (j >= k / 2) {
        return arrayOfByte;
      }
      byte i = arrayOfByte[j];
      arrayOfByte[j] = arrayOfByte[(k - j - 1)];
      arrayOfByte[(k - j - 1)] = i;
      j += 1;
    }
  }
  
  public static byte[] shortToBytes(short paramShort, boolean paramBoolean)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(2);
    if (paramBoolean) {
      localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }
    else
    {
    
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
    }
    localByteBuffer.putShort(paramShort);
    return localByteBuffer.array();
  }
  
  private static byte uniteBytes(byte paramByte1, byte paramByte2)
  {
    int i = 0;
    try
    {
    	byte j = Byte.decode("0x" + new String(new byte[] { paramByte1 }, "utf-8")).byteValue();
      i = j;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException1)
    {
     // for (;;)
      {
       // int j;
        //int k;
        localUnsupportedEncodingException1.printStackTrace();
      }
    }
    byte k = (byte)(i << 4);
    i = 0;
    try
    {
      byte j = Byte.decode("0x" + new String(new byte[] { paramByte2 }, "utf-8")).byteValue();
      i = j;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException2)
    {
     // for (;;)
      {
        localUnsupportedEncodingException2.printStackTrace();
      }
    }
    return (byte)(k | i);
  }
}


 