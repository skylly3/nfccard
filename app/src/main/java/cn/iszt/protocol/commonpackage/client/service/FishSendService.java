package cn.iszt.protocol.commonpackage.client.service;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.iface.model.pakg.Package7102;
import cn.iszt.protocol.iface.model.pakg.p7102.CheckCard7102;
import cn.iszt.protocol.iface.model.pakg.p7102.Command7102;
import cn.iszt.protocol.iface.model.pakg.p7102.E7102;
import cn.iszt.protocol.iface.model.pakg.p7102.RechargeConsume7102;

import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
//import java.util.ArrayList;
import java.util.Arrays;
//import java.util.List;
//import java.util.Properties;
//import org.apache.commons.lang3.ByteUtil;

import android.os.Environment;

public class FishSendService
{
  int client = 64536;
  
  static
  {
	  try 
	  {
	//	    System.loadLibrary("socket");
	  }
	  catch(Exception e)
	  {
		  System.out.println(e.toString());
		  
	  }

  }
  
  private CheckCard7102 bytesToCheckCard7102(byte[] paramArrayOfByte)
  {
    CheckCard7102 localCheckCard7102 = new CheckCard7102();
    int j = 0 + 2;
    int i = j + 5;
    localCheckCard7102.setTpdu(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localCheckCard7102.setEncryption(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localCheckCard7102.setMessageType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 16;
    localCheckCard7102.setLxrBusinessSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    localCheckCard7102.setRetry(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    localCheckCard7102.setBusinessType(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localCheckCard7102.setResult(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localCheckCard7102.setStep(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localCheckCard7102.setChargeMoney(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    localCheckCard7102.setTradeTime(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localCheckCard7102.setTradeDate(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 12;
    localCheckCard7102.setCenterSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 5;
    localCheckCard7102.setSztPosSN(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localCheckCard7102.setPhyIDLen(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 8;
    localCheckCard7102.setPhyID(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    localCheckCard7102.setSztResult(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localCheckCard7102.setSw(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 9;
    localCheckCard7102.setCardFaceNo(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    localCheckCard7102.setCardType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localCheckCard7102.setCardStatus(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localCheckCard7102.setWalletBalance(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    localCheckCard7102.setEffectiveDateStart(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localCheckCard7102.setEffectiveDatetEnd(ByteUtil.subarray(paramArrayOfByte, j, i));
    
    //这里有５个字节不知道干嘛用的: 00 95 00 00 02　先忽略掉
    i += 5;
    
    j = i + 1;
    localCheckCard7102.setTradeCount(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 23;
    localCheckCard7102.setTradeRecord1(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 23;
    localCheckCard7102.setTradeRecord2(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 23;
    localCheckCard7102.setTradeRecord3(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 23;
    localCheckCard7102.setTradeRecord4(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 23;
    localCheckCard7102.setTradeRecord5(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 23;
    localCheckCard7102.setTradeRecord6(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 23;
    localCheckCard7102.setTradeRecord7(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 23;
    localCheckCard7102.setTradeRecord8(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 23;
    localCheckCard7102.setTradeRecord9(ByteUtil.subarray(paramArrayOfByte, j, i));
    localCheckCard7102.setTradeRecord10(ByteUtil.subarray(paramArrayOfByte, i, i + 23));
    return localCheckCard7102;
  }
  
  private Command7102 bytesToCommand7102(byte[] paramArrayOfByte)
  {
    Command7102 localCommand7102 = new Command7102();
    int j = 0 + 2;
    int i = j + 5;
    localCommand7102.setTpdu(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localCommand7102.setEncryption(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localCommand7102.setMessageType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 16;
    localCommand7102.setLxrBusinessSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    localCommand7102.setRetry(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    localCommand7102.setBusinessType(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localCommand7102.setResult(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localCommand7102.setStep(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localCommand7102.setChargeMoney(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    localCommand7102.setTradeTime(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localCommand7102.setTradeDate(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 12;
    localCommand7102.setCenterSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 5;
    localCommand7102.setSztPosSN(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localCommand7102.setPhyIDLen(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 8;
    localCommand7102.setPhyID(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localCommand7102.setApduSum(ByteUtil.subarray(paramArrayOfByte, i, j));
    localCommand7102.setCapdu(ByteUtil.subarray(paramArrayOfByte, j, paramArrayOfByte.length));
    return localCommand7102;
  }
  
  private E7102 bytesToE7102(byte[] paramArrayOfByte)
  {
    E7102 localE7102 = new E7102();
    int j = 0 + 2;
    int i = j + 5;
    localE7102.setTpdu(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localE7102.setEncryption(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localE7102.setMessageType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 16;
    localE7102.setLxrBusinessSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    localE7102.setRetry(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    localE7102.setBusinessType(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localE7102.setResult(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localE7102.setStep(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localE7102.setChargeMoney(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    localE7102.setTradeTime(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localE7102.setTradeDate(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 12;
    localE7102.setCenterSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    localE7102.setSztPosSN(ByteUtil.subarray(paramArrayOfByte, j, j + 5));
    return localE7102;
  }
  
  public String getSDPath(){ 
      File sdDir = null; 
      boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
      if   (sdCardExist)   
      {                               
           sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
     }   
      return sdDir.toString(); 
      
  }
	 /** 
   * 以行为单位读取文件，常用于读面向行的格式化文件 
   */  
  public static boolean writeFile(String fileName, byte[] bb) {  
  //	List<String> list = new ArrayList<String>();
      File file = new File(fileName);  
      BufferedOutputStream writer = null;  
      try {  
    	  FileOutputStream fos = new FileOutputStream(file);  
          writer = new BufferedOutputStream(fos);  
        //  String ss =bb.toString();
          
          writer.write(bb);
         
          writer.close();  
          fos.close();
          writer = null;
      } catch (IOException e) {  
          e.printStackTrace();  
      } finally {  
          if (writer != null) {  
              try {  
            	  writer.close();  
              } catch (IOException e1) {  
              }  
          }  
      }  
       return true;
  }  
  
  private Package7102 bytesToPackage7102(byte[] paramArrayOfByte)
  {
	  if (paramArrayOfByte.length == 0)
	  {
		  E7102 localE7102 = new E7102();
		  return localE7102;
	  }
	  
	//  boolean bOk = writeFile(getSDPath() +File.separator + "dat.dat", paramArrayOfByte);
	  
    if (Arrays.equals(ByteUtil.subarray(paramArrayOfByte, 30, 34), ByteUtil.codeBCD("00000007"))) {
      return bytesToCommand7102(paramArrayOfByte);
    }
    if ((Arrays.equals(ByteUtil.subarray(paramArrayOfByte, 30, 34), ByteUtil.codeBCD("00000000"))) || (Arrays.equals(ByteUtil.subarray(paramArrayOfByte, 30, 34), ByteUtil.codeBCD("00000009"))) || (Arrays.equals(ByteUtil.subarray(paramArrayOfByte, 30, 34), ByteUtil.codeBCD("00000002"))) || (Arrays.equals(ByteUtil.subarray(paramArrayOfByte, 30, 34), ByteUtil.codeBCD("00000004"))))
    {
      if (Arrays.equals(ByteUtil.subarray(paramArrayOfByte, 27, 30), ByteUtil.codeBCD("800012"))) {
        return bytesToCheckCard7102(paramArrayOfByte);
      }
      return bytesToRechargeConsume7102(paramArrayOfByte);
    }
    return bytesToE7102(paramArrayOfByte);
  }
  
  private RechargeConsume7102 bytesToRechargeConsume7102(byte[] paramArrayOfByte)
  {
    RechargeConsume7102 localRechargeConsume7102 = new RechargeConsume7102();
    int j = 0 + 2;
    int i = j + 5;
    localRechargeConsume7102.setTpdu(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localRechargeConsume7102.setEncryption(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localRechargeConsume7102.setMessageType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 16;
    localRechargeConsume7102.setLxrBusinessSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    localRechargeConsume7102.setRetry(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    localRechargeConsume7102.setBusinessType(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localRechargeConsume7102.setResult(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localRechargeConsume7102.setStep(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localRechargeConsume7102.setChargeMoney(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 3;
    localRechargeConsume7102.setTradeTime(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localRechargeConsume7102.setTradeDate(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 12;
    localRechargeConsume7102.setCenterSN(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 5;
    localRechargeConsume7102.setSztPosSN(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 1;
    localRechargeConsume7102.setPhyIDLen(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 8;
    localRechargeConsume7102.setPhyID(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    localRechargeConsume7102.setSztResult(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 2;
    localRechargeConsume7102.setSw(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 12;
    localRechargeConsume7102.setBatchNo(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localRechargeConsume7102.setCenterTradeSN(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 9;
    localRechargeConsume7102.setSztCardNo(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 1;
    localRechargeConsume7102.setSztCardType(ByteUtil.subarray(paramArrayOfByte, j, i));
    j = i + 4;
    localRechargeConsume7102.setChargeMoney(ByteUtil.subarray(paramArrayOfByte, i, j));
    i = j + 4;
    localRechargeConsume7102.setBofMoney(ByteUtil.subarray(paramArrayOfByte, j, i));
    localRechargeConsume7102.setEofMoney(ByteUtil.subarray(paramArrayOfByte, i, i + 4));
    return localRechargeConsume7102;
  }
  
//  private String getValue(String paramString)
//  {
//    Properties localProperties = new Properties();
//    try
//    {
//      localProperties.load(getClass().getResourceAsStream("/assets/iszt.properties"));
//      return localProperties.getProperty(paramString);
//    }
//    catch (IOException localIOException)
//    {
//      for (;;)
//      {
//        localIOException.printStackTrace();
//      }
//    }
//  }
  
//  public native void clientclose(int paramInt);
  
 // public native int clientconnect(byte[] paramArrayOfByte, int paramInt);
  
//  public native byte[] clientsendrecv(int paramInt, byte[] paramArrayOfByte);
  
  public Package7102 send(byte[] paramArrayOfByte)
  {
//	  #test.iszt.cn 9000  debug
//	  #pt.iszt.cn 3001  release
//	  #192.168.1.231 9000 debug
//	  #219.135.155.107:9000  pre
	  // 创建Socket对象 & 指定服务端的IP及端口号 
	    String str1 = "pt.iszt.cn";  // getValue("ip");
	    String str2 = "3001";   // getValue("port");
	    Socket socket = null;
	    Package7102  paramArrayOfBytex = new E7102();
		try {
			socket = new Socket(str1 , Integer.valueOf(str2).intValue());

		    // 判断客户端和服务器是否连接成功  
		    if (socket.isConnected())
		    {
	            // 步骤1：从Socket 获得输出流对象OutputStream
	            // 该对象作用：发送数据
	            OutputStream oStream = socket.getOutputStream(); 

	            // 步骤2：写入需要发送的数据到输出流对象中
	            oStream.write(paramArrayOfByte);
	            // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

	            // 步骤3：发送数据到服务端 
	            oStream.flush();  
	          
	           // 步骤1：创建输入流对象InputStream
	            InputStream iStream = socket.getInputStream();
	            while (iStream.available() == 0)
	            {
	            	 Thread.sleep(100);
	            }
	            byte[] byt = new byte[iStream.available()];

	            iStream.read(byt);

	       
//v
//	            // 步骤2：创建输入流读取器对象 并传入输入流对象
//	            // 该对象作用：获取服务器返回的数据
//	            InputStreamReader isr = new InputStreamReader(iStream);
//	            BufferedReader br = new BufferedReader(isr);
//	            int iRead = br.read();
//	            byte [] buff = new byte[200];  //iRead+1
//	            int iReal =  br.read(buff);
//	            @SuppressWarnings("deprecation")
//				String str = new String(buff, iReal);
//	            byte[] bs = str.getBytes();
	            
	            iStream.close();
	            oStream.close();
	            socket.close();
	            
	            // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
	            //String ss = br.readLine();
	            paramArrayOfBytex = bytesToPackage7102(byt);
	    	 
			    if (!(paramArrayOfBytex instanceof Command7102)) 
			    {
			    //	System.out.println("clientsendrecv fail");
			   //   clientclose(this.client);
			    }
			
		    }
		    else
		    {
		        paramArrayOfBytex = new E7102();
		        paramArrayOfBytex.setResult(ByteUtil.codeBCD("00030002"));
		        socket.close();
		        return paramArrayOfBytex;
		    	
		    }
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  


		    return paramArrayOfBytex;
//
//    if (this.client < 0) {
//      this.client = clientconnect(str1.getBytes(), Integer.valueOf(str2).intValue());
//    }
//    if (this.client < 0)
//    {
//    	Package7102 paramArrayOfBytex = new E7102();
//        paramArrayOfBytex.setResult(ByteUtil.codeBCD("00030002"));
//        return paramArrayOfBytex;
//    }
  
  }
}


/* Location:              C:\dex2jar-2.0\classes-dex2jar.jar!\cn\iszt\protocol\commonpackage\client\service\FishSendService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */