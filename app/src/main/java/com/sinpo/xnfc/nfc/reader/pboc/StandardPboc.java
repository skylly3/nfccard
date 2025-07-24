/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */
//读公交卡的类
package com.sinpo.xnfc.nfc.reader.pboc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.sinpo.xnfc.SPEC;
import com.sinpo.xnfc.nfc.Util;
import com.sinpo.xnfc.nfc.Des;
import com.sinpo.xnfc.nfc.ByteUtil;
import com.sinpo.xnfc.nfc.bean.Application;
import com.sinpo.xnfc.nfc.bean.Card;
import com.sinpo.xnfc.nfc.tech.Iso7816;

import android.nfc.tech.IsoDep;
import android.os.Environment;

@SuppressWarnings("unchecked")
public abstract class StandardPboc {
	private static Class<?>[][] readers = {
			{ CityUnion.class, ShenzhenTong.class, YangchengTong.class, BeijingMunicipal.class, WuhanTong.class, TUnion.class, HardReader.class },
			{ StandardECash.class, } };

	public static void readCard(IsoDep tech, Card card)
			throws InstantiationException, IllegalAccessException, IOException {

		final Iso7816.StdTag tag = new Iso7816.StdTag(tech);

		tag.connect();

		try
		{
			for (final Class<?> g[] : readers) {
				HINT hint = HINT.RESETANDGONEXT;

				for (final Class<?> r : g) {

					final StandardPboc reader = (StandardPboc) r.newInstance();

					switch (hint) {

					case RESETANDGONEXT:
						if (!reader.resetTag(tag))
							continue;

					case GONEXT:
						hint = reader.readCard(tag, card);
						break;

					default:
						break;
					}

					if (hint == HINT.STOP)
						break;
				}
			}
		}
		catch(Exception e)
		{
			final Application app = new Application();
			app.setProperty(SPEC.PROP.ID, "ISO Dep卡");
			app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
			app.setProperty(SPEC.PROP.PARAM, "读卡出错");	
			
			card.addApplication(app);
		}
	

		tag.close();
		
		
		if (card.getApplications().size() == 0)
		{//所有的阅读器都没能读出这个卡,添加默认信息
			final Application app = new Application();
			app.setProperty(SPEC.PROP.ID, "ISO Dep卡");
			app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
			card.addApplication(app);
		}
	}

	protected boolean resetTag(Iso7816.StdTag tag) throws IOException {
		return tag.selectByID(DFI_MF).isOkey() || tag.selectByName(DFN_PSE).isOkey();
	}

	protected enum HINT {
		STOP, GONEXT, RESETANDGONEXT,
	}

	protected final static byte[] DFI_MF = { (byte) 0x3F, (byte) 0x00 };
	protected final static byte[] DFI_EP = { (byte) 0x10, (byte) 0x01 };
	// add by jl
	protected final static byte[] DFI_UNKNOW = { (byte) 0xDF, (byte) 0x03 };

	// 应用名称 1PAY.SYS.DDF01 对应的二进制
	protected final static byte[] DFN_PSE = { (byte) '1', (byte) 'P', (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S',
			(byte) 'Y', (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F', (byte) '0', (byte) '1', };

	protected final static byte[] DFN_PXX = { (byte) 'P' };

	protected final static int SFI_EXTRA_LOG = 4; // read card info file, binary
													// (4)
	protected final static int SFI_EXTRA_CNT = 5; // read card operation file,
													// binary (5)
	protected final static int SFI_EXTRA = 21;

	protected static int MAX_LOG = 10;
	protected static int SFI_LOG = 24;
	
	protected final static byte TRANS_CSU_IN = 2;    //  充值　
	protected final static byte TRANS_CSU = 6;      //  刷卡消费　
	protected final static byte TRANS_CSU_CPX = 9; //刷卡消费　
	protected final static byte TRANS_CSU_IN2 = 11;    //  充值　
	
	protected abstract Object getApplicationId();

	protected byte[] getMainApplicationId() {
		return DFI_EP;
	}

	protected SPEC.CUR getCurrency() {
		return SPEC.CUR.CNY;
	}

	protected boolean selectMainApplication(Iso7816.StdTag tag) throws IOException {
		final byte[] aid = getMainApplicationId();
		return ((aid.length == 2) ? tag.selectByID(aid) : tag.selectByName(aid)).isOkey();
	}
	
	 /** 
     * 以行为单位读取文件，常用于读面向行的格式化文件 
     */  
    public static List<String>  readFileByLines(String fileName) {  
    	List<String> list = new ArrayList<String>();
        File file = new File(fileName);  
        BufferedReader reader = null;  
        try {  
            System.out.println("以行为单位读取文件内容，一次读一整行：");  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
           // int line = 1;  
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                // 显示行号  
            	list.add(tempString) ;
               // System.out.println("line " + line + ": " + tempString);  
              //  line++;  
            }  
            reader.close();  
            reader = null;
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
        return list;
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

	protected void writeCard(Application app, Iso7816.StdTag tag, Card card) throws IOException
	{
		
		// 金额,单位(分)
		int iAmount = 2000;
		   // 交易金额
        String tradeAmount = String.format("%08d", iAmount);      
        System.out.println("交易金额:" + tradeAmount);

		
		 // 圈存的key 仅供测试
        // String loadKey = "3F013F013F013F013F013F013F013F01";
//        String[] loadKeyAry = {
//        "0102030405060708090A0B0C0D0E0F10", 
//        "3F023F023F023F023F023F023F023F02",
//        "3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F", 
//        "3D3D3D3D3D3D3D3D3D3D3D3D3D3D3D3D", 
//        "3D013D013D013D013D013D013D013D01", 
//        };
		List<String> loadKeyAry = readFileByLines(getSDPath() +File.separator + "keyfile.txt");
        if (loadKeyAry.size() == 0)
        {
        	app.setProperty(SPEC.PROP.PARAM, "keyfile不存在,忽略写卡操作");
        	return;
        }
		
		
		//长沙城市一卡通的pin值
		byte[] pin = { 0x59, 0x68, 0x72 };
		Iso7816.Response res = tag.verifypin(pin);
		if (!res.isOkey()) {
			app.setProperty(SPEC.PROP.PARAM, "verifypin:" + res.getSw12String());
			return;
		}
		byte[] arrayOfByte = new byte[4];

		
		arrayOfByte[0] = ((byte) (iAmount >> 24 & 0xFF));
		arrayOfByte[1] = ((byte) (iAmount >> 16 & 0xFF));
		arrayOfByte[2] = ((byte) (iAmount >> 8 & 0xFF));
		arrayOfByte[3] = ((byte) (iAmount & 0xFF));
		//终端机编号
		byte[] byteposid = { 3, 15, -1, -1, -1, -1 };
		String posid = Util.toHexString(byteposid);
		Iso7816.Response INFO = tag.precharge(arrayOfByte, byteposid);
		if (!INFO.isOkey()) {
			app.setProperty(SPEC.PROP.PARAM, "precharge:" + INFO.getSw12String());
			return;
		}
		final byte[] ret = INFO.getBytes();
	    // 卡余额
		String oldbalance  = Util.toHexString(ret, 0, 4);
	    int bal = ByteUtil.hexToInt(ret, 0, 4);
	    System.out.println("卡余额:" + bal);
	    
		String cardCnt = Util.toHexString(ret, 4, 2);
		System.out.println("联机计数器:" + cardCnt);
		
		String keyVersion  = Util.toHexString(ret, 6, 1);
		System.out.println("密钥版本:" + keyVersion );
		 
		String alglndMark = Util.toHexString(ret, 7, 1);
	    System.out.println("算法标识:" + alglndMark);
		 
		String random = Util.toHexString(ret, 8, 4);
		 System.out.println("随机数:" + random);
		
		String mac1 = Util.toHexString(ret, 12, 4);
		 System.out.println("mac1:" + mac1);

        // 验证tac的key　仅供测试
        String tacKey = "34343434343434343434343434343434";
        System.out.println("验证tac的key:" + tacKey);
		
        System.out.println("");
        System.out.println("开始验证mac1");
        // 验证mac1的正确性
        // 输入的数据为：随机数+联机计数器+“8000”
        String inputData = random + cardCnt + "8000";
        System.out.println("计算过程密钥数据:" + inputData);
        
        // 交易类型
        String tradeType = "02";
        System.out.println("交易类型:" + tradeType);
        

        
        boolean bMacOk = false;
        String sessionKey = "";
        for(int i=0;i<loadKeyAry.size();i++)
        {
        	String loadKey = loadKeyAry.get(i);
        	
            System.out.println("圈存的key:" + loadKey);
            
            // 计算过程密钥
            sessionKey = Des.getHintKey(inputData, loadKey);
            System.out.println("过程密钥:" + sessionKey);

            // 计算mac1需要输入的数据
            // 输入的数据为：余额+交易金额+交易类型+终端编号
            String inputData2 = oldbalance + tradeAmount + tradeType + posid;
            String legitMac1 = Des.PBOC_DES_MAC(sessionKey, "0000000000000000", inputData2, 0).substring(0, 8);
            System.out.println("标准的mac1数据:" + legitMac1);

            if (mac1.toUpperCase(Locale.getDefault()).equals(legitMac1))
            {
            	bMacOk = true;
                System.out.println("mac1校验成功！");
                break;
            }
       }
         if (!bMacOk)
         {
         	app.setProperty(SPEC.PROP.PARAM, "mac1校验失败");
 
        	 return;
         }
      	app.setProperty(SPEC.PROP.PARAM, "mac1校验成功");
 
        // 开始计算Mac2用于做充值确认操作
        System.out.println("");
        System.out.println("开始计算mac2");
		
		SimpleDateFormat localObject1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat localObject2 = new SimpleDateFormat("HHmmss");
        Date localObject3 = Calendar.getInstance().getTime();
        // 交易日期
        String tradeDate = ((SimpleDateFormat)localObject1).format(localObject3);
        // 交易时间
        String tradeTime = ((SimpleDateFormat)localObject2).format(localObject3);
       
        // 计算mac2的输入数据
        // 输入数据为：交易金额+交易类型+终端编号+交易日期+交易时间
        inputData = tradeAmount + tradeType + posid + tradeDate + tradeTime;
        String mac2 = Des.PBOC_DES_MAC(sessionKey, "0000000000000000", inputData, 0).substring(0, 8);
        // 得到mac2
        System.out.println("计算后的mac2:" + mac2);

        //圈存用mac2,空圈认证应答的返回值
        //String mac2 = "12345678";
		INFO = tag.charge((tradeDate+tradeTime).getBytes(), mac2.getBytes());
		app.setProperty(SPEC.PROP.PARAM, "charge:" + INFO.getSw12String());
		if (!INFO.isOkey()) {
		 
			return;
		}
		String tac = Util.toHexString(INFO.getBytes());
		System.out.println("开始验证tac");
        System.out.println("tac验证密钥:" + tacKey);
        // 对tac验证密钥左边8个字节和右边8个字节做异或处理得到tac过程密钥
        String tacTessionKey = Des.xOr(tacKey.substring(0, 16), tacKey.substring(16, 32));
        System.out.println("tac过程密钥:" + tacTessionKey);
        // 充值成功之后新的金额为00000043+00000001=00000044
        String newBalance = ByteUtil.hexToStr(ByteUtil.intToHex(bal + iAmount, 4));
        // 计算标准的tac的输入数据
        // 输入的数据：新的余额+旧的联机计数器+交易金额+交易类型+终端编号+交易日期+交易时间
        inputData = newBalance + cardCnt + tradeAmount + tradeType + posid + tradeDate + tradeTime;
        String legitTac = Des.PBOC_DES_MAC(tacTessionKey, "0000000000000000", inputData, 0).substring(0, 8);
        System.out.println("标准的tac数据:" + legitTac);

        if (!tac.toUpperCase(Locale.getDefault()).equals(legitTac))
        {
            System.out.println("tac校验错误！");
            return;
        }
        System.out.println("tac校验成功！");
		Iso7816.Response BALANCE = tag.getBalance(0, true);
		
	 
		
		return;
	}
	
	  public static byte[] toBytes(String str) {
	        if(str == null || str.trim().equals("")) {
	            return new byte[0];
	        }

	        byte[] bytes = new byte[str.length() / 2];
	        for(int i = 0; i < str.length() / 2; i++) {
	            String subStr = str.substring(i * 2, i * 2 + 2);
	            bytes[i] = (byte) Integer.parseInt(subStr, 16);
	        }

	        return bytes;
	    }

	protected HINT readCard(Iso7816.StdTag tag, Card card) throws IOException {

		Iso7816.Response INFO, BALANCE;


		/*--------------------------------------------------------------*/
		// read card info file, binary (05) add by jl,2018.9.12
		// 如果是城市一卡通,这里可以读到卡大类和小类
		/*--------------------------------------------------------------*/
		// INFO = tag.readBinary(SFI_EXTRA_CNT);
		// String main_card_type = Util.toHexString((byte[])INFO.getBytes(), 16, 1);
		// String sub_card_type = Util.toHexString((byte[])INFO.getBytes(), 17, 1);
//	        paramMap.put("main_card_type", localObject1);
//	        paramMap.put("sub_card_type", localObject2);
//
//	    paramVarArgs = (String)paramb.get("main_card_type");
//	    paramb = (String)paramb.get("sub_card_type");
//	    if (("80".equals(paramVarArgs)) && ("A4".equals(paramb)))  
		/*--------------------------------------------------------------*/
		// select Main Application select ep
		/*--------------------------------------------------------------*/
		if (!selectMainApplication(tag))
		{
			/* 下面是应用被锁时解锁应用代码，不知道应用维护密码，所以没有调通。。。
			//取随机数
			Iso7816.Response res2 = tag.getrand(4);
			
			byte[] rand = res2.getBytes();
			byte[] rand2 = res2.getBytes();
			System.arraycopy(rand, 0, rand2, 0, rand.length);
			
			//Iso7816.Response res3 = tag.calcmac(rand2);
		 	byte[] mac = { 0x00, 0x00, 0x00, 0x00 };
		 	
		//	System.arraycopy(res3.getBytes(), 0, mac, 0, 4);
			
		 	Iso7816.Response res = tag.unblockapp(mac);
		 	if (!res.isOkey()) 
			{	final Application app = createApplication();
			 	app.setProperty(SPEC.PROP.PARAM, "unblockapp:" + res.getSw12String());
			 	card.addApplication(app);
			 	return HINT.STOP;
			}
			*/
			
			
			// 下面是擦除卡片操作代码。因为不知道外部认证密钥，所以没有调通
			/* //获取８字节随机数
			INFO = tag.getrand(8);
			if (!INFO.isOkey())
			{
				return HINT.GONEXT;
			}
			String strIn = Util.toHexString(INFO.getBytes()) ;
			//外部认证密钥
			String strKey = "FFFFFFFFFFFFFFFF";
			String strOut = Des.DES_1(strIn, strKey, 0);
			// 外部认证
			INFO = tag.verifyout(toBytes(strOut));
			if (!INFO.isOkey())
			{
				return HINT.GONEXT;
			}
			INFO = tag.earsecard();
			if (!INFO.isOkey())
			{
				return HINT.GONEXT;
			}
			*/
		 

			return HINT.GONEXT;
		}
	
		/*--------------------------------------------------------------*/
		// read card info file, binary (21)
		/*--------------------------------------------------------------*/
		INFO = tag.readBinary(SFI_EXTRA);

		/*--------------------------------------------------------------*/
		// read balance 读取余额
		/*--------------------------------------------------------------*/
		BALANCE = tag.getBalance(0, true);

		/*--------------------------------------------------------------*/
		// read log file, record (24) 读交易明细 （0x18）
		/*--------------------------------------------------------------*/
		ArrayList<byte[]> LOG = readLog24(tag, SFI_LOG);

		/*--------------------------------------------------------------*/
		// build result
		/*--------------------------------------------------------------*/
		final Application app = createApplication();

		parseBalance(app, BALANCE);

		parseInfo21(app, INFO, 4, true);       //读短文件0x15 序列号和版本

		parseLog24(app, LOG);                  //交易日志

		configApplication(app);    

		card.addApplication(app);

		

		// add by jl,准备写入城市一卡通
	  //  boolean bb = tag.selectByID(DFI_UNKNOW).isOkey();
		String strName =  "城市一卡通（长沙）";
		if (strName.equals(getApplicationId()))
		{
	 	 	//writeCard(app, tag, card);
		}
		return HINT.STOP;
	}

	protected float parseBalance(Iso7816.Response data) {
		float ret = 0f;
		if (data.isOkey() && data.size() >= 4) {
			int n = Util.toInt(data.getBytes(), 0, 4);
			if (n > 1000000 || n < -1000000)
				n -= 0x80000000;

			ret = n / 100.0f;
		}
		return ret;
	}

	protected void parseBalance(Application app, Iso7816.Response... data) {

		float amount = 0f;
		for (Iso7816.Response rsp : data)
			amount += parseBalance(rsp);

		app.setProperty(SPEC.PROP.BALANCE, amount);
	}

	protected void parseInfo21(Application app, Iso7816.Response data, int dec, boolean bigEndian) {
		if (!data.isOkey() || data.size() < 30) {
			return;
		}

		final byte[] d = data.getBytes();
		if (dec < 1 || dec > 10) {
			app.setProperty(SPEC.PROP.SERIAL, Util.toHexString(d, 10, 10));
		} else {
			final int sn = bigEndian ? Util.toIntR(d, 19, dec) : Util.toInt(d, 20 - dec, dec);

			app.setProperty(SPEC.PROP.SERIAL, String.format("%d", 0xFFFFFFFFL & sn));
		}

		if (d[9] != 0){
			//app.setProperty(SPEC.PROP.VERSION, String.valueOf(d[9])); //这里如果是PBOC的卡的话，版本应该是0x32和0x48  赵洋 2015.8.27
			//这里应该先判断一下是AID，然后根据卡组织的定义来处理
			if(0 ==  0){
				if(d[9] == 0x32){
					app.setProperty(SPEC.PROP.VERSION, "PBOC2.0");
				}
				else if (d[9] == 0x48){
					app.setProperty(SPEC.PROP.VERSION, "PBOC3.0");
				}
				else {
					app.setProperty(SPEC.PROP.VERSION, String.format("%x", d[9])); //不识别的版本
				}
			}
				
		}

		app.setProperty(SPEC.PROP.DATE, String.format("%02X%02X.%02X.%02X - %02X%02X.%02X.%02X", d[20], d[21], d[22],
				d[23], d[24], d[25], d[26], d[27]));
	}

	protected boolean addLog24(final Iso7816.Response r, ArrayList<byte[]> l) {
		if (!r.isOkey())
			return false;

		final byte[] raw = r.getBytes();
		final int N = raw.length - 23;
		if (N < 0)
			return false;

		for (int s = 0, e = 0; s <= N; s = e) {
			l.add(Arrays.copyOfRange(raw, s, (e = s + 23)));
		}

		return true;
	}

	protected ArrayList<byte[]> readLog24(Iso7816.StdTag tag, int sfi) throws IOException {
		final ArrayList<byte[]> ret = new ArrayList<byte[]>(MAX_LOG);
		final Iso7816.Response rsp = tag.readRecord(sfi);
		if (rsp.isOkey()) {
			addLog24(rsp, ret);
		} else {
			for (int i = 1; i <= MAX_LOG; ++i) {
				if (!addLog24(tag.readRecord(sfi, i), ret))
					break;
			}
		}

		return ret;
	}

	protected void parseLog24(Application app, ArrayList<byte[]>... logs) {
		final ArrayList<String> ret = new ArrayList<String>(MAX_LOG);

		for (final ArrayList<byte[]> log : logs) {
			if (log == null)
				continue;

			for (final byte[] v : log) {
				final int money = Util.toInt(v, 5, 4);
				if (money > 0) {
				  String strType = "未知";
				  char s = '+';
				  if (v[9] == TRANS_CSU_CPX)   //地铁
				  {
					  strType = "地铁";
					  s = '-';
				  }
				  else if (v[9] == TRANS_CSU)   //公交车
				  {				
					  strType = "公交";
					  s = '-';
				  }
				  else if (v[9] == TRANS_CSU_IN)  //充值
				  {				
					  strType = "充值";
					  s = '+';
				  }
				  else if (v[9] == TRANS_CSU_IN2) //未验证
				  {				
					  strType = "充值二";
					  s = '+';
				  }
				  else  //未知
				  {
					  strType = String.format("未知:%d", v[9]);
					  s = '-';
				  }
					final int over = Util.toInt(v, 2, 3);
					final String slog;
					if (over > 0) {
						slog = String.format("%02X%02X.%02X.%02X %02X:%02X:%02X %s %c%.2f [o:%.2f] [%02X%02X%02X%02X%02X%02X]",
								v[16], v[17], v[18], v[19], v[20], v[21],v[22], strType, s, (money / 100.0f), (over / 100.0f), v[10],
								v[11], v[12], v[13], v[14], v[15]);
					} else {
						slog = String.format("%02X%02X.%02X.%02X %02X:%02X:%02X %s %C%.2f [%02X%02X%02X%02X%02X%02X]", 
								v[16], v[17], v[18], v[19], v[20], v[21],v[22], strType, s, (money / 100.0f), v[10], v[11], v[12], v[13],
								v[14], v[15]);

					}

					ret.add(slog);
				}
			}
		}

		if (!ret.isEmpty())
			app.setProperty(SPEC.PROP.TRANSLOG, ret.toArray(new String[ret.size()]));
	}

	protected Application createApplication() {
		return new Application();
	}

	protected void configApplication(Application app) {
		app.setProperty(SPEC.PROP.ID, getApplicationId());
		app.setProperty(SPEC.PROP.CURRENCY, getCurrency());
	}
}
