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

package com.sinpo.xnfc.nfc.reader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
//import android.content.res.Resources;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
//import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
//import android.widget.Toast;
import android.os.Parcelable;
import android.widget.Toast;
//二代证读取库
import cn.com.keshengxuanyi.mobilereader.NFCReaderHelper;
import cn.com.keshengxuanyi.mobilereader.UserInfo;


//import java.io.Console;
import java.io.IOException;
import java.nio.charset.Charset;
//import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

import com.sinpo.xnfc.MainActivity;
import com.sinpo.xnfc.SPEC;
import com.sinpo.xnfc.nfc.Util;
import com.sinpo.xnfc.nfc.bean.Application;
import com.sinpo.xnfc.nfc.bean.Card;
import com.sinpo.xnfc.nfc.reader.pboc.StandardPboc;
import com.sinpo.xnfc.nfc.tech.Iso7816;
import com.sinpo.xnfc.nfc.tech.Iso7816.Response;

public final class ReaderManager extends AsyncTask<Tag, SPEC.EVENT, Card> {

	public static void readCard(Context ctx, Intent intent, Tag tag, ReaderListener listener) {
		new ReaderManager(ctx, intent, listener).execute(tag);
	}
	private Context m_ctx;
	private ReaderListener realListener;
	private Intent m_intent;
	private ReaderManager(Context ctx, Intent intent,ReaderListener listener) {
		m_intent = intent;
		m_ctx = ctx;
		realListener = listener;
	}

	@Override
	protected Card doInBackground(Tag... detectedTag) {
		return readCard(detectedTag[0]);
	}

	@Override
	protected void onProgressUpdate(SPEC.EVENT... events) {
		if (realListener != null)
			realListener.onReadEvent(events[0]);
	}

	@Override
	protected void onPostExecute(Card card) {
		if (realListener != null)
			realListener.onReadEvent(SPEC.EVENT.FINISHED, card);
	}

	private String ByteArrayToHexString(byte[] inarray) { // converts byte
		// arrays to string
		int i, j, in;
		String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		String out = "";

		for (j = 0; j < inarray.length; ++j) {
			in = inarray[j] & 0xff;
			i = (in >> 4) & 0x0f;
			out += hex[i];
			i = in & 0x0f;
			out += hex[i];
		}
		return out;
	}

	public void readTagClassic(Tag tag, Card card) {
		boolean auth = false;
		MifareClassic mfc = MifareClassic.get(tag);
		final Application app;
		app = new Application();
		app.setProperty(SPEC.PROP.ID, "M1卡");
		app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
		String metaInfo = "";
		String paramInfo = "";
		String verInfo = "普通M1卡";
		Float fBalance = (float) 0.0;
		
		// 读取TAG
		try {

			int type = mfc.getType();// 获取TAG的类型
			int sectorCount = mfc.getSectorCount();// 获取TAG中包含的扇区数
			String typeS = "";
			switch (type) {
			case MifareClassic.TYPE_CLASSIC:
				typeS = "TYPE_CLASSIC";
				break;
			case MifareClassic.TYPE_PLUS:
				typeS = "TYPE_PLUS";
				break;
			case MifareClassic.TYPE_PRO:
				typeS = "TYPE_PRO";
				break;
			case MifareClassic.TYPE_UNKNOWN:
				typeS = "TYPE_UNKNOWN";
				break;
			}

			metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共" + mfc.getBlockCount() + "个块\n存储空间: "
					+ mfc.getSize() + "B\n";

			//株洲公交
        	byte[][] keyAZhuZhou ={ {(byte) 0xA0,(byte) 0xA1,(byte) 0xA2,(byte) 0xA3,(byte) 0xA4,(byte) 0xA5},
        			 {(byte) 0x74,(byte) 0x72,(byte) 0x63,(byte) 0xBC,(byte) 0x74,(byte) 0x72},
					 {(byte) 0xB6,(byte) 0x86,(byte) 0xB3,(byte) 0x1B,(byte) 0xF9,(byte) 0x88},
					 {(byte) 0x69,(byte) 0x32,(byte) 0x16,(byte) 0x51,(byte) 0xC4,(byte) 0x63},
					 {(byte) 0x69,(byte) 0x32,(byte) 0x16,(byte) 0x51,(byte) 0xC4,(byte) 0x63},
					 {(byte) 0x69,(byte) 0x32,(byte) 0x16,(byte) 0x51,(byte) 0xC4,(byte) 0x63},
					 {(byte) 0xB6,(byte) 0x86,(byte) 0xB3,(byte) 0x1B,(byte) 0xF9,(byte) 0x88},
					 {(byte) 0x60,(byte) 0xAA,(byte) 0x84,(byte) 0x8C,(byte) 0x0D,(byte) 0x3D},	 
					 {(byte) 0x60,(byte) 0xAA,(byte) 0x84,(byte) 0x8C,(byte) 0x0D,(byte) 0x3D}
			};
//        	byte[][] keyBZhuZhou ={ {(byte) 0xB0,(byte) 0xB1,(byte) 0xB2,(byte) 0xB3,(byte) 0xB4,(byte) 0xB5},
//					 {(byte) 0x10,(byte) 0xD0,(byte) 0x01,(byte) 0x1D,(byte) 0xF0,(byte) 0x87},
//					 {(byte) 0xA0,(byte) 0xC8,(byte) 0x89,(byte) 0xB8,(byte) 0x91,(byte) 0x19},
//					 {(byte) 0x69,(byte) 0x32,(byte) 0x16,(byte) 0x51,(byte) 0xC4,(byte) 0x63},
//					 {(byte) 0x69,(byte) 0x32,(byte) 0x16,(byte) 0x51,(byte) 0xC4,(byte) 0x63},
//					 {(byte) 0x69,(byte) 0x32,(byte) 0x16,(byte) 0x51,(byte) 0xC4,(byte) 0x63}, 
//					 {(byte) 0xA0,(byte) 0xC8,(byte) 0x89,(byte) 0xB8,(byte) 0x91,(byte) 0x19},
//					 {(byte) 0x4C,(byte) 0x28,(byte) 0x7C,(byte) 0x0E,(byte) 0xFD,(byte) 0x59},	 
//					 {(byte) 0x4C,(byte) 0x28,(byte) 0x7C,(byte) 0x0E,(byte) 0xFD,(byte) 0x59}
//			};
        	//珠海公交
        	byte[][] keyAZhuHai ={ {(byte) 0xA0,(byte) 0xA1,(byte) 0xA2,(byte) 0xA3,(byte) 0xA4,(byte) 0xA5},
       			 {(byte) 0x14,(byte) 0xC6,(byte) 0x0E,(byte) 0xD2,(byte) 0x14,(byte) 0xC6},
					 {(byte) 0xA4,(byte) 0xA9,(byte) 0x56,(byte) 0xB0,(byte) 0xCA,(byte) 0x95},
					 {(byte) 0x4A,(byte) 0x04,(byte) 0xB1,(byte) 0xAC,(byte) 0x54,(byte) 0x1B},
					 {(byte) 0x4A,(byte) 0x04,(byte) 0xB1,(byte) 0xAC,(byte) 0x54,(byte) 0x1B},
					 {(byte) 0x4A,(byte) 0x04,(byte) 0xB1,(byte) 0xAC,(byte) 0x54,(byte) 0x1B},
					 {(byte) 0xA4,(byte) 0xA9,(byte) 0x56,(byte) 0xB0,(byte) 0xCA,(byte) 0x95},
					 {(byte) 0x04,(byte) 0xE0,(byte) 0x72,(byte) 0x78,(byte) 0xB1,(byte) 0x2A},	 
					 {(byte) 0x04,(byte) 0xE0,(byte) 0x72,(byte) 0x78,(byte) 0xB1,(byte) 0x2A}
			};
			boolean bZhuZhou = false;
			boolean bZhuHai = false;		
			mfc.connect();
			for (int j = 0; j < sectorCount; j++) {
				// Authenticate a sector with key A.

				auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
				if (!auth && (j < 9))
				{//有密钥 株洲公交卡
					auth = mfc.authenticateSectorWithKeyA(j, keyAZhuZhou[j]);
					if (auth)
					{	 
						bZhuZhou = true;
						bZhuHai = false;
					}
				}
				if (!auth && (j < 9))
				{//有密钥 珠海公交卡
					auth = mfc.authenticateSectorWithKeyA(j, keyAZhuHai[j]);
					if (auth)
					{	 
						bZhuZhou = false;
						bZhuHai = true;
					}
				}
				
				int bCount;
				int bIndex;
				if (auth) {
					paramInfo += "\n扇区: " + j + "\n";
					// 读取扇区中的块
					bCount = mfc.getBlockCountInSector(j);
					bIndex = mfc.sectorToBlock(j);
					for (int i = 0; i < bCount; i++) 
					{
						byte[] data = mfc.readBlock(bIndex);
						paramInfo += ByteArrayToHexString(data);
						paramInfo += "\n";
						// += "Block " + bIndex + " : "

						if (bZhuZhou || bZhuHai)
						{//公交卡
							if ((j == 2) && (i==1))
							{//扇区2  块1是存储余额的
								fBalance = (float) ((data[1]*256+data[0])/100.0);
							}
						}
						bIndex++;
					}
				} else {
					paramInfo += "扇区 " + j + ":验证失败\n";

				}
			}
			if (bZhuZhou)
				verInfo = "株洲公交卡";
			else if (bZhuHai)
				verInfo = "珠海公交卡";
			
			mfc.close();
			app.setProperty(SPEC.PROP.PARAM, paramInfo);
			
			if (bZhuZhou || bZhuHai)
				app.setProperty(SPEC.PROP.BALANCE, fBalance);
			
			card.addApplication(app);

			return;
		} catch (Exception e) {
			// Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} finally {
			if (mfc != null) {
				try {
					mfc.close();
					app.setProperty(SPEC.PROP.ISSUE, verInfo);
					app.setProperty(SPEC.PROP.VERSION, metaInfo );
					card.addApplication(app);
				} catch (IOException e) {
					// Toast.makeText(this, e.getMessage(),
					// Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	public String readTagUltralight(Tag tag) {
		MifareUltralight mifare = MifareUltralight.get(tag);
		try {
			mifare.connect();
			int size = mifare.PAGE_SIZE;
			byte[] payload = mifare.readPages(0);
			String result = "总容量：" + String.valueOf(size) + "\n" + "page0：" + ByteArrayToHexString(payload) + "\n";

			// 这里只读取了其中几个page、
			byte[] payload1 = mifare.readPages(4);
			byte[] payload2 = mifare.readPages(8);
			byte[] payload3 = mifare.readPages(12);
			result += "page4:" + ByteArrayToHexString(payload1) + "\npage8:" + ByteArrayToHexString(payload2)
					+ "\npage12：" + ByteArrayToHexString(payload3) + "\n";

			// byte[] payload4 = mifare.readPages(16);
			// byte[] payload5 = mifare.readPages(20);
			return result;
			// + new String(payload4, Charset.forName("US-ASCII"));
			// + new String(payload5, Charset.forName("US-ASCII"));
		} catch (IOException e) {
			// Log.e(TAG, "IOException while writing MifareUltralight
			// message...", e);
			return "读取失败！";
		} catch (Exception ee) {
			// Log.e(TAG, "IOException while writing MifareUltralight
			// message...", ee);
			return "读取失败！";
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (IOException e) {
					// Log.e(TAG, "Error closing tag...", e);
				}
			}
		}
	}

	NdefMessage[] getNdefMessages(Intent intent) {// 读取nfc数据
		// Parse the intent
	//	intent.getSystemService();
	 
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		//if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
		{
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				msgs = new NdefMessage[] { msg };
			}
		} 
		//else 
		{
			// Log.d(TAG, "Unknown intent.");
			// finish();
		}
		return msgs;
	}
	class MyHandler extends Handler {
	//	private MainActivity activity;

		MyHandler() {
			//this.activity = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1000:

				String msgTemp = (String) msg.obj;
			//	readerstatText.setText(msgTemp);

				break;
			}
		}
	}

	/**
	 * demo使用
	 */
	private static String appKey = "941c9b37d4dd4e569ff0320b21d9071c";
	private static String appSecret = "8eb5c020856040f7be7e52cff4ce3a77";
	
    //创建一个封装要写入的文本的NdefRecord对象
    public NdefRecord createTextRecord(String text) {
        //生成语言编码的字节数组，中文编码
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(
                Charset.forName("US-ASCII"));
        //将要写入的文本以UTF_8格式进行编码
        Charset utfEncoding = Charset.forName("UTF-8");
        //由于已经确定文本的格式编码为UTF_8，所以直接将payload的第1个字节的第7位设为0
        byte[] textBytes = text.getBytes(utfEncoding);
        int utfBit = 0;
        //定义和初始化状态字节
        char status = (char) (utfBit + langBytes.length);
        //创建存储payload的字节数组
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置状态字节
        data[0] = (byte) status;
        //设置语言编码
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置实际要写入的文本
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);
        //根据前面设置的payload创建NdefRecord对象
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }
    
	private Card readCard(Tag tag) {

		final Card card = new Card();

		try {

			publishProgress(SPEC.EVENT.READING);

			card.setProperty(SPEC.PROP.ID, Util.toHexString(tag.getId()));

			ArrayList<String> list = new ArrayList<String>();
			String types = "";
			for (String string : tag.getTechList()) {
				list.add(string);
				types += string.substring(string.lastIndexOf(".") + 1, string.length()) + ",";
			}
			types = types.substring(0, types.length() - 1);

			if (list.contains("android.nfc.tech.IsoDep")) {// cpu卡 中国人民银行的Pboc
				final IsoDep isodep = IsoDep.get(tag);
				if (isodep != null)
					StandardPboc.readCard(isodep, card);
			}
			else if (list.contains("android.nfc.tech.NfcF")) {// 深圳通和八达通// 索尼的Felica
			   FelicaReader.readCard(m_ctx, tag,   card);
			} else if (list.contains("android.nfc.tech.NfcV")) {// 深圳图书馆卡
				final NfcV nfcv = NfcV.get(tag);
				if (nfcv != null) 
					VicinityCard.readCard(nfcv, card);			// 德州仪器的VicinityCard卡
			} else if (list.contains("android.nfc.tech.NfcB")) {// 二代身份证
				final NfcB nfcb = NfcB.get(tag);
				try {
					nfcb.connect();
					if (nfcb.isConnected())
					{
						//System.out.println("已连接");
						//Toast.makeText(m_ctx, "身份证已连接", 1).show();
						CommandAsyncTask task = new CommandAsyncTask();
						task.SetNfcB(nfcb);
						String strRet = task.readUUid();
						nfcb.close();
						
						final Application app;
						app = new Application();
						app.setProperty(SPEC.PROP.ID, "二代身份证");
						app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
						app.setProperty(SPEC.PROP.UUID, strRet);
						
						//这个二代证读取库貌似用不了,不知道是不是appkey过期了,还是他们家接口不能用了,没仔细研究,暂时注释掉
						/*
						MyHandler uiHandler = new MyHandler();
						NFCReaderHelper mNFCReaderHelper = new NFCReaderHelper(m_ctx, uiHandler, appKey,	appSecret, true);
						
						String strCardInfo = mNFCReaderHelper.readCardWithIntent(m_intent);
						if ((null != strCardInfo) && (strCardInfo.length() > 1600))
						{
							UserInfo userInfo = mNFCReaderHelper.parsePersonInfoNew(strCardInfo);
							
							app.setProperty(SPEC.PROP.NAME, userInfo.name);
							app.setProperty(SPEC.PROP.IDNUM, userInfo.id);	
							
							app.setProperty(SPEC.PROP.SEX, userInfo.sex);	
							app.setProperty(SPEC.PROP.NATION, userInfo.nation);	
							app.setProperty(SPEC.PROP.BIRTHDAY, userInfo.brithday);	
							app.setProperty(SPEC.PROP.ADDR, userInfo.address);			
							app.setProperty(SPEC.PROP.ISSUE, userInfo.issue);	
							
							app.setProperty(SPEC.PROP.DATE, userInfo.exper + "-" + userInfo.exper2);			
							 try{
						            System.loadLibrary("DecodeWlt"); 
						        }catch(Throwable ex){
						            ex.printStackTrace();
						        }
							 
							
							//解析头像
						//	Bitmap bm = mNFCReaderHelper.decodeImagexxx(strCardInfo);
						//	app.setProperty(SPEC.PROP.HEAD, bm);	
						}
					    */
						card.addApplication(app);

						//nfcb.close();
					}
					// nfcbTag.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			if (list.contains("android.nfc.tech.MifareUltralight")) { //一般是地铁单程票使用
				final Application app;
				app = new Application();
				app.setProperty(SPEC.PROP.ID, "M1 Ultralight卡");
				app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
				String str = readTagUltralight(tag);
				app.setProperty(SPEC.PROP.SERIAL, str);
				card.addApplication(app);
			} else if (list.contains("android.nfc.tech.MifareClassic")) {
				// m1卡
				// 飞利浦的Mifare
			    readTagClassic(tag, card);
			}
			if (list.contains("android.nfc.tech.NfcA")) {// typeA卡
				final NfcA nfca = NfcA.get(tag);
				if (nfca != null) {
					final Application app;
					app = new Application();
					app.setProperty(SPEC.PROP.ID, "typeA卡");
					app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);

					card.addApplication(app);
					
//					try {
//						nfca.connect();
//						if (nfca.isConnected()) {// NTAG216的芯片
//							
//							
//							
//							byte[] SELECT = { (byte) 0x30, (byte) 5 & 0x0ff,// 0x05
//							};
//							byte[] response = nfca.transceive(SELECT);
//							nfca.close();
//
//							if (response != null) {
//								app.setProperty(SPEC.PROP.SERIAL, new String(response, Charset.forName("utf-8")));
//								card.addApplication(app);
//							}
//						}
//					} // try
//					catch (IOException e) {
//						System.out.println("IOException while writing MifareUltralight message..." + e.toString());
//						// return "读取失败！";
//					} catch (Exception ee) {
//						System.out.println("IOException while writing  MifareUltralight message..." + ee.toString());
//						// return "读取失败！";
//					} finally {
//						if (nfca.isConnected())
//							nfca.close();
//					}

				} // if (nfca != null)
			} // typeA
			if (list.contains("android.nfc.tech.Ndef")) {
				// ndef  NFC魔法贴功能, Ndef为读取数据接口 
				final Ndef ndef = Ndef.get(tag);
				if (ndef != null) {
					final Application app;
					app = new Application();
					app.setProperty(SPEC.PROP.ID, "Ndef卡");
					app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);

				//	NdefMessage[] messages = getNdefMessages(m_intent);
				//	byte[] payload = messages[0].getRecords()[0].getPayload();
				//	String str = Util.toHexString(payload);
				//	app.setProperty(SPEC.PROP.PARAM, str);
					card.addApplication(app);
				}
			} // ndef
			else if (list.contains("android.nfc.tech.NdefFormatable")) {
				// NdefFormatable NFC魔法贴功能, NdefFormatable为写入数据接口
				final NdefFormatable ndefformat = NdefFormatable.get(tag);
				if (ndefformat != null) {
					final Application app;
					app = new Application();
					app.setProperty(SPEC.PROP.ID, "NdefFormatable卡");
					app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
					  try {
	                        //允许对标签进行IO操作
						//    ndefformat.connect();
						//	if (ndefformat.isConnected())
							{
							//	NdefMessage message = new NdefMessage(new NdefRecord[] {createTextRecord("hello world")});
							//	ndefformat.format(message);
							//	ndefformat.close();
							//	app.setProperty(SPEC.PROP.PARAM, "已成功写入数据");
		                      //  Toast.makeText(m_ctx, "已成功写入数据！", Toast.LENGTH_LONG).show();
							}
						//	else
							{								
						//		app.setProperty(SPEC.PROP.PARAM, "NDEF格式 连接失败");
		                   //     Toast.makeText(m_ctx, "写入NDEF格式 连接失败", Toast.LENGTH_LONG).show();
							}
	 
	                    } catch (Exception e) {
	                    	app.setProperty(SPEC.PROP.PARAM, "NDEF格式 连接发生异常");
	                       // Toast.makeText(m_ctx, "写入NDEF格式数据失败！", Toast.LENGTH_LONG).show();
	                    }
                    /*
					NdefMessage[] messages = getNdefMessages(m_intent);
					byte[] payload = messages[0].getRecords()[0].getPayload();
					String str = Util.toHexString(payload);
					*/
					app.setProperty(SPEC.PROP.SERIAL, "");
					card.addApplication(app);
				}//	if (ndefformat != null) {
			} // ndefformat

			publishProgress(SPEC.EVENT.IDLE);

		} catch (Exception e) {
			card.setProperty(SPEC.PROP.EXCEPTION, e);
			publishProgress(SPEC.EVENT.ERROR);
		}

		return card;
	}
}


//读二代身份证信息
class CommandAsyncTask extends AsyncTask<Integer, Integer, String> {

	@Override
	protected String doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		byte[] search = new byte[] { 0x05, 0x00, 0x00 };
		search = new byte[] { 0x00, (byte) 0xA4, 0x00, 0x00, 0x02, 0x60,
				0x02 };
		search = new byte[] { 0x1D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
				0x01, 0x08 };
		byte[] result = new byte[] {};
		StringBuffer sb = new StringBuffer();
		try {
			byte[] cmd = new byte[] { 0x05, 0x00, 0x00 };
			;
			result = nfcbTag.transceive(cmd);
			sb.append("寻卡指令:" + Util.toHexString(cmd) + "\n");
			sb.append("收:" + Util.toHexString(result) + "\n");
			cmd = new byte[] { 0x1D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
					0x01, 0x08 };
			result = nfcbTag.transceive(cmd);
			sb.append("选卡指令:" + Util.toHexString(cmd) + "\n");
			sb.append("收:" + Util.toHexString(result) + "\n");
			sb.append("读固定信息指令\n");

			cmd = new byte[] { 0x00, (byte) 0xA4, 0x00, 0x00, 0x02, 0x60,
					0x02 };
			result = nfcbTag.transceive(cmd);
			sb.append("发:" + Util.toHexString(cmd) + "\n");
			sb.append("收:" + Util.toHexString(result) + "\n");
			cmd = new byte[] { (byte) 0x80, (byte) 0xB0, 0x00, 0x00, 0x20 };
			result = nfcbTag.transceive(cmd);
			sb.append("发:" + Util.toHexString(cmd) + "\n");
			sb.append("收:" + Util.toHexString(result) + "\n");
			cmd = new byte[] { 0x00, (byte) 0x88, 0x00, 0x52, 0x0A,
					(byte) 0xF0, 0x00, 0x0E, 0x0C, (byte) 0x89, 0x53,
					(byte) 0xC3, 0x09, (byte) 0xD7, 0x3D };
			result = nfcbTag.transceive(cmd);
			sb.append("发:" + Util.toHexString(cmd) + "\n");
			sb.append("收:" + Util.toHexString(result) + "\n");
			cmd = new byte[] { 0x00, (byte) 0x88, 0x00, 0x52, 0x0A,
					(byte) 0xF0, 0x00, };
			result = nfcbTag.transceive(cmd);
			sb.append("发:" + Util.toHexString(cmd) + "\n");
			sb.append("收:" + Util.toHexString(result) + "\n");
			cmd = new byte[] { 0x00, (byte) 0x84, 0x00, 0x00, 0x08 };
			result = nfcbTag.transceive(cmd);
			sb.append("发:" + Util.toHexString(cmd) + "\n");
			sb.append("收:" + Util.toHexString(result) + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	protected String readUUid() {
		byte[] result = new byte[] {};
		String strRet = "";
		try {
			//GUID命令
			byte[] cmd = new byte[] { 0x00, 0x36, 0x00, 0x00, 0x08 };
			
			result = nfcbTag.transceive(cmd);
			if (result.length >= 10)
			{
				byte[] b1 = new byte[10];
				System.arraycopy(result, 0, b1, 0, 10);
				Iso7816.Response res =  new Response(b1);
				if (res.isOkey())
				{
					strRet = res.toString().substring(0, 16);
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strRet;
	}
	
	void SetNfcB(NfcB newnfcbTag)
	{
		nfcbTag = newnfcbTag;
	}
	 private NfcB nfcbTag;
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	//	setNoteBody(result);
		try {
			nfcbTag.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
