package com.sinpo.xnfc.nfc.reader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;
//import android.os.Handler;
import android.os.Message;
//import android.telephony.TelephonyManager;

import java.io.IOException;
//import java.io.PrintStream;
//import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
import java.util.List;

import com.sinpo.xnfc.SPEC;
import com.sinpo.xnfc.nfc.Util;
import com.sinpo.xnfc.nfc.bean.Application;
import com.sinpo.xnfc.nfc.tech.FeliCa;

import cn.iszt.protocol.common.util.ByteUtil;
import cn.iszt.protocol.commonpackage.client.service.FishSendService;
import cn.iszt.protocol.iface.model.pakg.Package7100;
import cn.iszt.protocol.iface.model.pakg.Package7101;
import cn.iszt.protocol.iface.model.pakg.Package7102;
import cn.iszt.protocol.iface.model.pakg.p7102.CheckCard7102;
import cn.iszt.protocol.iface.model.pakg.p7102.Command7102;
import cn.iszt.protocol.iface.model.pakg.p7102.CardTransactionRecord;

public class PukaCardOperator {
	private Package7100 package7100;
	private Package7101 package7101;
	private Package7102 package7102;
	
	protected final static byte TRANS_CSU_IN = 2;         //  充值,未验证
	protected final static byte TRANS_CSU = 6;            //  公交,未验证
	protected final static byte TRANS_CSU_CPX = 9;        //  地铁,未验证
	protected final static byte TRANS_CSU_IN2 = 0x0b;     //  充值,已验证
	protected final static byte TRANS_CSU_CPX2 = 0x16;    //  地铁,已验证
	protected final static byte TRANS_CSU_CPX3 = 0x1f;    //  公交,已验证
	protected final static byte TRANS_CSU_CPX4 = 0x25;    //  圈提,已验证
	
	private String CardCode;
	private String CardNo;
	private int CardStatus;
	private int CardType;
	private String File08;
	private String OverMoney;
	private String iccid;
	private byte[] lastRecord;
	private String orderNo;

	private FishSendService fishSendService;
	// private static byte[] centerSN;
	private static byte[] posSN;
	// private static byte[] retryTime;
	public static boolean stopSearch = false;
	// private byte[] SYS_SZT = { -128, 5 };
	private int Sum_18;
	// private BaseApplication app;
	int clientSuccess = 0;
	private IsoDep dep;
	private FeliCa.Tag felica;

	// private Handler handler;
	private byte[] historyBtyes = new byte[8];
	private NfcF nfcF;

	private byte[] physicId;
	private byte[] rApdu;
	private Tag tag;
 	private String[] tradeRecord;
 //	private ArrayList<String> tradeRecord2;
	private List<CardTransactionRecord> tradeRecordList;

	public void setCardId(String paramString) {
		this.CardCode = paramString;
	}

	public void setCardNo(String paramString) {
		this.CardNo = paramString;
	}

	public void setCardStatus(int paramInt) {
		this.CardStatus = paramInt;
	}

	public void setCardType(int paramInt) {
		this.CardType = paramInt;
	}

	public void setFile08(String paramString) {
		this.File08 = paramString;
	}

	public void setIccid(String paramString) {
		this.iccid = paramString;
	}

	public void setLastRecord(byte[] paramArrayOfByte) {
		this.lastRecord = paramArrayOfByte;
	}

	public void setOrderNo(String paramString) {
		this.orderNo = paramString;
	}

	public void setOverMoney(String paramString) {
		this.OverMoney = paramString;
	}

	public PukaCardOperator(Tag tagx, FeliCa.Tag tag) {
		// this.app = paramBaseApplication;
		// this.handler = paramHandler;
		this.tag = tagx;
		this.felica = tag;
		stopSearch = false;
		if (this.fishSendService == null) {
			this.fishSendService = new FishSendService();
		}
	}

	private boolean CreditCAPP(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
		byte[] arrayOfByte = new byte[16];
		System.arraycopy(SztApdu.SZT_CreditCAPP, 0, arrayOfByte, 0, 6);
		System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 6, 4);
		System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 10, 6);
		System.out.println("CreditCAPP==============>");
		return sendAndCheckAPDU(arrayOfByte);
	}

	private boolean DeductCAPP(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
		byte[] arrayOfByte = new byte[16];
		System.arraycopy(SztApdu.SZT_DeductCAPP, 0, arrayOfByte, 0, 6);
		System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 6, 4);
		System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 10, 6);
		System.out.println("DeductCAPP==============>");
		return sendAndCheckAPDU(arrayOfByte);
	}

	private byte[] GetSztEF0018() {
		byte[] arrayOfByte1 = new byte[128];
		byte[] arrayOfByte2 = new byte[5];
		System.arraycopy(SztApdu.SZT_GETEF0018, 0, arrayOfByte2, 0, 5);
		int i = 0;
		for (;;) {
			if (i >= 10) {
				return arrayOfByte1;
			}
			arrayOfByte2[2] = ((byte) (arrayOfByte2[2] + 1));
			System.out.println("GetSFI0018==============>");
			if (!sendAndCheckAPDU(arrayOfByte2)) {
				break;
			}
			System.arraycopy(this.rApdu, 0, arrayOfByte1, i * 23, 23);
			this.Sum_18 = (i + 1);
			i += 1;
		}
		this.Sum_18 = i;
		return arrayOfByte1;
	}

	public boolean checkNfcFCardResult(final Application app, Context ctx) {
		this.package7100 = new Package7100();
		this.package7100.setRetry(ByteUtil.codeBCD("00"));
		// 新版发的800012, 旧版代码是800010
		this.package7100.setBusinessType(ByteUtil.codeBCD("800012"));
		this.package7100.setCompanyCode(ByteUtil.codeBCD("0000"));
		this.package7100.setEsam(new byte[4]);
		this.package7100.setEsamid(new byte[4]);
		byte[] arrayOfByte;
		// TelephonyManager tm = (TelephonyManager)
		// ctx.getSystemService(Context.TELEPHONY_SERVICE);
		String strIMei = AppPhoneMgr.getInstance().getPhoneImei(ctx);
		if (strIMei.length() == 15) {
			this.package7100.setPosID(ByteUtil.getBytes("0" + strIMei));

			String strPhone = AppPhoneMgr.getInstance().getPhoneNum(ctx);

			this.package7100.setPhone(ByteUtil.getBytes(strPhone));

			String strIccid = AppPhoneMgr.getInstance().getIccid(ctx);
			// 不发送真实iccid
			strIccid = "";

			if (strIccid.length() > 20) {
				strIccid = strIccid.substring(0, 20);
			}
			byte[] iccid = ByteUtil.getBytes(strIccid);
			if (iccid.length < 20) {
				iccid = ByteUtil.merge(new byte[][] { new byte[20 - iccid.length], iccid });
			}
			this.package7100.setIccid(iccid);

			this.package7100.setHold(new byte[9]);

			// 新版发的01,旧版00
			this.package7100.setHold1(ByteUtil.codeBCD("01"));
			this.package7100.setCardPhyType(new byte[1]);
			this.package7100.setPhyIDLen(ByteUtil.codeBCD("08"));
			Package7100 localPackage7100 = this.package7100;
			if (this.physicId == null) {
				arrayOfByte = ByteUtil.codeBCD("0000000000000000");
			} else
				arrayOfByte = this.physicId;

			localPackage7100.setPhyID(arrayOfByte);
			// 新版发的08, 旧版代码04
			this.package7100.setHistoricalByteslen(ByteUtil.codeBCD("08"));
			localPackage7100 = this.package7100;
			if (this.historyBtyes == null) {
				arrayOfByte = ByteUtil.codeBCD("0000000000000000");
			} else
				arrayOfByte = this.historyBtyes;

			localPackage7100.setHistoricalBytes(arrayOfByte);
			this.package7100.setChargemoney(new byte[4]);
			// 新版长一倍
			this.package7100.setPaymentSN(new byte[64]);
			// 旧版不对
			// this.package7100.setPaymentSN(ByteUtil.codeBCD("0000000000000000000000000000000000000000000000000000000000000000"));
			this.package7100.setSztCardNo(new byte[4]);
			// sn+1?
			this.package7100.setCardTransSeq(ByteUtil.codeBCD("0001"));
			this.package7100.setBalence(new byte[4]);
			this.package7100.setLastRecord(new byte[23]);

			String strPhoneModule = AppPhoneMgr.getInstance().getPhoneModel();
			if (strPhoneModule != "") {
				// label498:
				this.package7100.setPhoneType(ByteUtil.getBytes(strPhoneModule));
			} else
				this.package7100.setPhoneType(ByteUtil.getBytes(""));
		}
		// for (;;)
		// {
		this.package7100.setOsVersion(ByteUtil.getBytes(AppPhoneMgr.getInstance().getOsVersion()));
		this.package7100.setAppVersion(ByteUtil.getBytes(AppPhoneMgr.getInstance().getVersionCode(ctx)));
		this.package7100.setDateTime(ByteUtil.codeBCD("00000000000000"));
		this.package7100.setCenterSN(ByteUtil.getBytes("000000000000"));
		this.package7100.setSztPosSN(ByteUtil.codeBCD("0000000000"));
		this.package7100.setStep(ByteUtil.codeBCD("00"));
		// System.out.println("package7100::::[" +
		// ByteUtil.bytesToHexString(this.package7100.getPackage()) + "]");

		PukaCardOperator.this.fishSendService = new FishSendService();
		PukaCardOperator.this.package7102 = PukaCardOperator.this.fishSendService
				.send(PukaCardOperator.this.package7100.getPackage());
		// LogUtil.printLog("package7102",
		// ByteUtil.bytesToHexString(PukaCardOperator.this.package7102.getPackage()));
		boolean bRet = PukaCardOperator.this.get7102CheckCard(app);
		return bRet;
		// PukaCardOperator.this.closeChanel();
		// new Thread(new Runnable()
		// {
		// public void run()
		// {
		// try
		// {
		// PukaCardOperator.this.fishSendService = new FishSendService();
		// PukaCardOperator.this.package7102 =
		// PukaCardOperator.this.fishSendService.send(PukaCardOperator.this.package7100.getPackage());
		// // LogUtil.printLog("package7102",
		// ByteUtil.bytesToHexString(PukaCardOperator.this.package7102.getPackage()));
		// PukaCardOperator.this.get7102CheckCard( app, paramBoolean);
		//
		// Message localMessage = Message.obtain();
		// if (paramBoolean) {}
		// // for (localMessage.what = 4;; localMessage.what = 1)
		// {
		// // PukaCardOperator.this.handler.sendMessage(localMessage);
		// // return;
		// }
		//
		// return;
		// }
		// catch (Exception localException)
		// {
		// localException.printStackTrace();
		//
		// }
		// finally
		// {
		// if (!paramBoolean) {
		// PukaCardOperator.this.closeChanel();
		// }
		// }
		// }
		// }).start();
		// return;

		// }
	}

	private boolean get7102CheckCard(Application app) {
		// int i = 0;
		if (!(this.package7102 instanceof Command7102)) {// 查卡返回结果
			CheckCard7102 localObject1;
			localObject1 = (CheckCard7102) this.package7102;
			if ((int) ByteUtil.bytesToInt( localObject1.getSztResult(), false) != 0) {
				boolean bCheckOk = true;
			}
			String strCardNo = ByteUtil.getString(localObject1.getCardFaceNo());
			app.setProperty(SPEC.PROP.SERIAL, strCardNo);
			System.out.println("cardno:" + strCardNo);

			if (strCardNo == null || strCardNo.trim().length() == 0) {// 读卡失败
				return false;
			}
			this.setCardNo(strCardNo);
			long lBalance = ByteUtil.bytesToInt(localObject1.getWalletBalance(), false);
			System.out.println("cardBalance:" + lBalance);
			float fAmount = (float) (lBalance / 100.0);
			app.setProperty(SPEC.PROP.BALANCE, fAmount);

			//
			this.Sum_18 = localObject1.getTradeCount()[0];
			app.setProperty(SPEC.PROP.COUNT, String.valueOf(this.Sum_18));
			//
			this.setCardId("");
			this.setOverMoney(String.valueOf(lBalance));

			this.setCardType(localObject1.getCardType()[0]);
			this.setCardStatus(localObject1.getCardStatus()[0]);
			String strStatus = "已入闸";
			if (localObject1.getCardStatus()[0] == 3) {
				strStatus = "已出闸";
			}
			app.setProperty(SPEC.PROP.PARAM, strStatus);
			this.tradeRecordList = getTradeRecords(ByteUtil.merge(new byte[][] {
				    localObject1.getTradeRecord1(), localObject1.getTradeRecord2(),
					localObject1.getTradeRecord3(), localObject1.getTradeRecord4(),
					localObject1.getTradeRecord5(), localObject1.getTradeRecord6(),
				    localObject1.getTradeRecord7(), localObject1.getTradeRecord8(),
					localObject1.getTradeRecord9(),
					localObject1.getTradeRecord10() }));
			
			if (this.tradeRecord.length != 0)
				app.setProperty(SPEC.PROP.TRANSLOG, this.tradeRecord);
			// this.setTradeRecord(this.tradeRecordList);
			
			byte bStart[] = localObject1.getEffectiveDateStart();
			byte bEnd[] = localObject1.getEffectiveDatetEnd();
			app.setProperty(SPEC.PROP.DATE, String.format("%02X%02X.%02X.%02X - %02X%02X.%02X.%02X",
					bStart[0], bStart[1], bStart[2], bStart[3],
					bEnd[0], bEnd[1], bEnd[2], bEnd[3]));
			return true;
		} else {// 是个中间命令
				// Message localObject1m = Message.obtain();
				// localObject1m.what = i;

			Command7102 localObject1x = (Command7102) this.package7102;
			// Object localObject2 = Message.obtain();
			// ((Message)localObject2).what = 11;
			// ((Message)localObject2).obj =
			// Integer.valueOf(localObject1x.getStep()[0]);
			// this.handler.sendMessage((Message)localObject2);
			// LogUtil.printLog("Capdu--------------------",
			// ByteUtil.bytesToHexString(((Command7102)localObject1).getCapdu()));
			byte[] localObject2bb = sendApdu(((Command7102) localObject1x).getCapdu(), 0,
					localObject1x.getApduSum()[0]);
			// LogUtil.printLog("������7101����", "===>" +
			// ByteUtil.bytesToHexString((byte[])localObject2));
			this.package7101 = new Package7101();
			this.package7101.setApduSum(((Command7102) localObject1x).getApduSum());
			this.package7101.setRapdu((byte[]) localObject2bb);
			this.package7101.setApduStatue(ByteUtil.codeBCD("0000"));
			this.package7101.setBusinessType(((Command7102) localObject1x).getBusinessType());
			this.package7101.setCenterSN(((Command7102) localObject1x).getCenterSN());
			this.package7101.setChargeMoney(((Command7102) localObject1x).getChargeMoney());
			this.package7101.setEncryption(((Command7102) localObject1x).getEncryption());
			this.setOrderNo(ByteUtil.getString(((Command7102) localObject1x).getLxrBusinessSN()));
			this.package7101.setLxrBusinessSN(((Command7102) localObject1x).getLxrBusinessSN());
			this.package7101.setPhyID(((Command7102) localObject1x).getPhyID());
			this.package7101.setPhyIDLen(((Command7102) localObject1x).getPhyIDLen());
			this.package7101.setRetry(((Command7102) localObject1x).getRetry());
			this.package7101.setStep(new byte[] { (byte) (localObject1x.getStep()[0] + 1) });
			this.package7101.setSztPosSN(((Command7102) localObject1x).getSztPosSN());
			this.package7101.setTpdu(((Command7102) localObject1x).getTpdu());
			this.package7101.setTradeDate(((Command7102) localObject1x).getTradeDate());
			this.package7101.setTradeTime(((Command7102) localObject1x).getTradeTime());
			this.package7102 = this.fishSendService.send(this.package7101.getPackage());
			// break;

			boolean bRet = PukaCardOperator.this.get7102CheckCard(app);
			return bRet;
			// this.handler.sendMessage((Message)localObject1m);
		}
		// return false;
	}

	// isodep的检查卡函数
	public boolean checkCardResult(boolean paramBoolean) {
		Object localObject;
		byte[] localObject2 = new byte[6];
		if (sendAndCheckAPDU(SztApdu.SZT_SELECTFILE_1001)) {
			if (sendAndCheckAPDU(SztApdu.SZT_Get08File)) {
				if (this.rApdu[3] != 0) {
					// break label75;
				}
				localObject = "已出闸";
				this.setFile08((String) localObject);
			}
			if (sendAndCheckAPDU(SztApdu.SZT_GETEF0015)) {
				getCardInfo(this.rApdu);
				this.rApdu = GetSztEF0018();
				if (this.rApdu != null) {
					// break label97;
				}
			}
		}
		// label75:
		// label97:
		label247:

		do {
			do {
				do {
					do {
						do {
							// return false;
							if (this.rApdu[3] == 1) {
								localObject = "已进闸";
								break;
							}
							localObject = "未知错误";
							break;
							// System.arraycopy(this.rApdu, 0,
							// SztStruct.EF0018Data, 0, this.rApdu.length);
							// getRecentTrade(this.rApdu);
							// this.tradeRecordList =
							// getTradeRecords(this.rApdu);
						} while (!sendAndCheckAPDU(SztApdu.SZT_GETEF0019));
						getTCardInfo();
					} while (!sendAndCheckAPDU(SztApdu.SZT_GETEF001A));
					getEACardInfo();
				} while (!sendAndCheckAPDU(SztApdu.SZT_GETOVERMONEY));
				System.arraycopy(this.rApdu, 0, SztStruct.OverMoneyData, 0, this.rApdu.length - 2);

				localObject2[2] = -103;
				localObject2[4] = 97;
				localObject2[5] = 67;
				if (!paramBoolean) {
					break label247;
				}
			} while (!CreditCAPP(IntToByte(0, 4, false), (byte[]) localObject2));
			System.arraycopy(this.rApdu, 4, SztStruct.CreditCAPP.TransSN, 0, SztStruct.CreditCAPP.TransSN.length);
			return true;
		} while (!DeductCAPP(IntToByte(0, 4, false), (byte[]) localObject2));
		System.arraycopy(this.rApdu, 4, SztStruct.DeductCAPP.TransSN, 0, SztStruct.DeductCAPP.TransSN.length);
		return true;
	}

	public static byte[] IntToByte(int paramInt1, int paramInt2, boolean paramBoolean) {
		byte[] arrayOfByte = new byte[paramInt2];
		int i = 0;
		if (i >= paramInt2) {
			return arrayOfByte;
		}
		if (paramBoolean) {
			arrayOfByte[i] = ((byte) (paramInt1 >>> i * 8 & 0xFF));
		}
		for (;;) {
			i += 1;
			break;

		}
		arrayOfByte[(paramInt2 - i - 1)] = ((byte) (paramInt1 >>> i * 8 & 0xFF));
		return arrayOfByte;
	}

	public static byte[] intToBytes(int paramInt, boolean paramBoolean) {
		ByteBuffer localByteBuffer = ByteBuffer.allocate(4);
		if (paramBoolean) {
			localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		} else {

			localByteBuffer.order(ByteOrder.BIG_ENDIAN);
		}
		// for (;;)
		{
			localByteBuffer.putInt(paramInt);

		}
		return localByteBuffer.array();
	}

//	//Util.toIntR 或　Util.toInt可以替代
//	public static int ByteToInt(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean) {
//		int i = 0;
//		int j = 1;
//		if (j > paramInt2) {
//			return i;
//		}
//		for (; j <= paramInt2;) {
//			if (paramBoolean) {
//				i = i << 8 | paramArrayOfByte[(paramInt1 + j - 1)] & 0xFF;
//			} else {
//				i = i << 8 | paramArrayOfByte[(paramInt1 + paramInt2 - j)] & 0xFF;
//			}
//			j += 1;
//			break;
//		}
//		return i;
//	}

	public static int bytesToShort(byte[] paramArrayOfBytex, boolean paramBoolean) {
		ByteBuffer paramArrayOfByte = ByteBuffer.wrap(paramArrayOfBytex);
		if (paramBoolean) {
			paramArrayOfByte.order(ByteOrder.LITTLE_ENDIAN);
		} else

		// for (;;)
		{
			paramArrayOfByte.order(ByteOrder.BIG_ENDIAN);

		}
		return paramArrayOfByte.getShort() & 0xFFFF;
	}

	/*
	 * private void get7102(String paramString1, int paramInt, String
	 * paramString2) { if (!(this.package7102 instanceof Command7102)) {
	 * paramString1 = Message.obtain(); if (this.clientSuccess == 1) {
	 * paramString1.what = 0; paramString1.obj = "交易成功";
	 * this.handler.sendMessage(paramString1); } } else { paramString2 =
	 * (Command7102)this.package7102; Object localObject = Message.obtain();
	 * ((Message)localObject).what = 8; ((Message)localObject).obj =
	 * Integer.valueOf(paramString2.getStep()[0]);
	 * this.handler.sendMessage((Message)localObject);
	 * LogUtil.printLog("Capdu--------------------",
	 * ByteUtil.bytesToHexString(paramString2.getCapdu())); localObject =
	 * sendApdu(paramString2.getCapdu(), 0, paramString2.getApduSum()[0]);
	 * LogUtil.printLog("rapdu--------------------",
	 * ByteUtil.bytesToHexString((byte[])localObject)); if
	 * ((paramString1.equals("801110")) && (this.nfcF == null)) { paramInt =
	 * ByteUtil.bytesToShort(ArrayUtils.subarray((byte[])localObject, 0, 2),
	 * false); if (Arrays.equals(paramString2.getStep(),
	 * ByteUtil.codeBCD("03"))) { if
	 * (!Arrays.equals(ArrayUtils.subarray((byte[])localObject, paramInt,
	 * paramInt + 2), ByteUtil.codeBCD("9000"))) { break label497; }
	 * this.clientSuccess = 1; } } for (;;) { //
	 * LogUtil.printLog("���ص�7101�ֽ�", "===>" +
	 * ByteUtil.bytesToHexString((byte[])localObject)); this.package7101 = new
	 * Package7101(); this.package7101.setApduSum(paramString2.getApduSum());
	 * this.package7101.setRapdu((byte[])localObject);
	 * this.package7101.setApduStatue(ByteUtil.codeBCD("0000"));
	 * this.package7101.setBusinessType(paramString2.getBusinessType());
	 * this.package7101.setCenterSN(paramString2.getCenterSN());
	 * this.package7101.setChargeMoney(paramString2.getChargeMoney());
	 * this.package7101.setEncryption(paramString2.getEncryption());
	 * this.app.setOrderNo(ByteUtil.getString(paramString2.getLxrBusinessSN()));
	 * this.package7101.setLxrBusinessSN(paramString2.getLxrBusinessSN());
	 * this.package7101.setPhyID(paramString2.getPhyID());
	 * this.package7101.setPhyIDLen(paramString2.getPhyIDLen());
	 * this.package7101.setRetry(paramString2.getRetry());
	 * this.package7101.setStep(new byte[] { (byte)(paramString2.getStep()[0] +
	 * 1) }); this.package7101.setSztPosSN(paramString2.getSztPosSN());
	 * this.package7101.setTpdu(paramString2.getTpdu());
	 * this.package7101.setTradeDate(paramString2.getTradeDate());
	 * this.package7101.setTradeTime(paramString2.getTradeTime());
	 * this.package7102 =
	 * this.fishSendService.send(this.package7101.getPackage());
	 * System.out.println("package7102::::[" +
	 * ByteUtil.bytesToHexString(this.package7102.getPackage()) + "]"); break;
	 * label497: if (Arrays.equals(ArrayUtils.subarray((byte[])localObject,
	 * paramInt, paramInt + 2), ByteUtil.codeBCD("6A6A"))) { this.clientSuccess
	 * = 2; } else { this.clientSuccess = 0; continue; if
	 * ((paramString1.equals("803710")) &&
	 * (Arrays.equals(paramString2.getStep(), ByteUtil.codeBCD("03")))) { if
	 * (Arrays.equals(ArrayUtils.subarray((byte[])localObject,
	 * localObject.length - 2, localObject.length), ByteUtil.codeBCD("9000"))) {
	 * this.clientSuccess = 1; } else if
	 * (Arrays.equals(ArrayUtils.subarray((byte[])localObject,
	 * localObject.length - 2, localObject.length), ByteUtil.codeBCD("6A6A"))) {
	 * this.clientSuccess = 2; } else { this.clientSuccess = 0; } } } } } if
	 * (this.clientSuccess == 2) { paramString1.what = 9; paramString1.obj =
	 * "����δ֪"; this.handler.sendMessage(paramString1); return; } if (this.nfcF
	 * != null) { if (ByteUtil.bytesToInt(this.package7102.getResult(), false)
	 * == 0L) { paramString1.what = 0; paramString1.obj = "���׳ɹ�";
	 * this.handler.sendMessage(paramString1); return; } if
	 * (this.package7102.getStep()[0] <= 7) { paramString1.what = 2;
	 * paramString1.obj = ("������:" +
	 * ByteUtil.bytesToHexString(this.package7102.getResult()));
	 * this.handler.sendMessage(paramString1); return; } paramString1.what = 9;
	 * paramString1.obj = "����δ֪"; this.handler.sendMessage(paramString1);
	 * return; } if (ByteUtil.bytesToInt(this.package7102.getResult(), false) ==
	 * 9L) { centerSN = this.package7102.getCenterSN(); posSN =
	 * this.package7102.getSztPosSN(); retryTime = ByteUtil.merge(new byte[][] {
	 * ByteUtil.codeBCD(new SimpleDateFormat("yyyy").format(new Date())),
	 * this.package7102.getTradeDate(), this.package7102.getTradeTime() });
	 * paramString1.what = 1; paramString1.obj = "������";
	 * this.handler.sendMessage(paramString1); return; } paramString1.what = 2;
	 * if (this.package7102 != null) {} for (paramString1.obj = ("������:" +
	 * ByteUtil.bytesToHexString(this.package7102.getResult()));;
	 * paramString1.obj = "�������쳣") { this.handler.sendMessage(paramString1);
	 * return; } }
	 * 
	 * 
	 * private void get7102Online(String paramString1, int paramInt, String
	 * paramString2) { for (;;) { if (!(this.package7102 instanceof
	 * Command7102)) { paramString1 = Message.obtain();
	 * LogUtil.printLog("Online���׽����",
	 * ByteUtil.bytesToInt(this.package7102.getResult(), false)); if
	 * (ByteUtil.bytesToInt(this.package7102.getResult(), false) != 0L) { break;
	 * } paramString1.what = 0; paramString1.obj = " ";
	 * this.handler.sendMessage(paramString1); return; } paramString1 =
	 * (Command7102)this.package7102; paramString2 = Message.obtain();
	 * paramString2.what = 8; paramString2.obj =
	 * Integer.valueOf(paramString1.getStep()[0]);
	 * this.handler.sendMessage(paramString2);
	 * LogUtil.printLog("Capdu--------------------",
	 * ByteUtil.bytesToHexString(paramString1.getCapdu())); paramString2 =
	 * sendApdu(paramString1.getCapdu(), 0, paramString1.getApduSum()[0]);
	 * LogUtil.printLog("rapdu--------------------",
	 * ByteUtil.bytesToHexString(paramString2));
	 * LogUtil.printLog("���ص�7101�ֽ�", "===>" +
	 * ByteUtil.bytesToHexString(paramString2)); this.package7101 = new
	 * Package7101(); this.package7101.setApduSum(paramString1.getApduSum());
	 * this.package7101.setRapdu(paramString2);
	 * this.package7101.setApduStatue(ByteUtil.codeBCD("0000"));
	 * this.package7101.setBusinessType(paramString1.getBusinessType());
	 * this.package7101.setCenterSN(paramString1.getCenterSN());
	 * this.package7101.setChargeMoney(paramString1.getChargeMoney());
	 * this.package7101.setEncryption(paramString1.getEncryption());
	 * this.app.setOrderNo(ByteUtil.getString(paramString1.getLxrBusinessSN()));
	 * this.package7101.setLxrBusinessSN(paramString1.getLxrBusinessSN());
	 * this.package7101.setPhyID(paramString1.getPhyID());
	 * this.package7101.setPhyIDLen(paramString1.getPhyIDLen());
	 * this.package7101.setRetry(paramString1.getRetry());
	 * this.package7101.setStep(new byte[] { (byte)(paramString1.getStep()[0] +
	 * 1) }); this.package7101.setSztPosSN(paramString1.getSztPosSN());
	 * this.package7101.setTpdu(paramString1.getTpdu());
	 * this.package7101.setTradeDate(paramString1.getTradeDate());
	 * this.package7101.setTradeTime(paramString1.getTradeTime());
	 * this.package7102 =
	 * this.fishSendService.send(this.package7101.getPackage()); } if
	 * (ByteUtil.bytesToInt(this.package7102.getResult(), false) == 2L) {
	 * paramString1.what = 2; paramString1.obj = ("������:" +
	 * ByteUtil.bytesToHexString(this.package7102.getResult()));
	 * this.handler.sendMessage(paramString1); return; } centerSN =
	 * this.package7102.getCenterSN(); posSN = this.package7102.getSztPosSN();
	 * retryTime = ByteUtil.merge(new byte[][] { ByteUtil.codeBCD(new
	 * SimpleDateFormat("yyyy").format(new Date())),
	 * this.package7102.getTradeDate(), this.package7102.getTradeTime() });
	 * paramString1.what = 9; paramString1.obj = "δ֪״̬";
	 * this.handler.sendMessage(paramString1); }
	 */
	private void getCardInfo(byte[] paramArrayOfByte) {
		System.arraycopy(paramArrayOfByte, 0, SztStruct.StructEF0015.PublishCardSign, 0,
				SztStruct.StructEF0015.PublishCardSign.length);
		int i = 0 + SztStruct.StructEF0015.PublishCardSign.length;
		SztStruct.StructEF0015.AppType = paramArrayOfByte[i];
		i += 1;
		SztStruct.StructEF0015.CardType = paramArrayOfByte[i];
		i += 1;
		System.arraycopy(paramArrayOfByte, i, SztStruct.StructEF0015.AppSequence, 0,
				SztStruct.StructEF0015.AppSequence.length);
		i += SztStruct.StructEF0015.AppSequence.length;
		System.arraycopy(paramArrayOfByte, i, SztStruct.StructEF0015.IssueDate, 0,
				SztStruct.StructEF0015.IssueDate.length);
		i += SztStruct.StructEF0015.IssueDate.length;
		System.arraycopy(paramArrayOfByte, i, SztStruct.StructEF0015.ValidDate, 0,
				SztStruct.StructEF0015.ValidDate.length);
		i += SztStruct.StructEF0015.ValidDate.length;
		SztStruct.StructEF0015.CardVersion = paramArrayOfByte[i];
		i += 1;
		SztStruct.StructEF0015.AppVersion = paramArrayOfByte[i];
		System.arraycopy(paramArrayOfByte, i + 1, SztStruct.StructEF0015.IssueCardFile, 0,
				SztStruct.StructEF0015.IssueCardFile.length);
		System.out.println("card info finish");
	}

	private void getEACardInfo() {
		System.arraycopy(this.rApdu, 0, SztStruct.StructEF001A.SellDate, 0, SztStruct.StructEF001A.SellDate.length);
		int i = 0 + SztStruct.StructEF001A.SellDate.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF001A.EndDate, 0, SztStruct.StructEF001A.EndDate.length);
		i += SztStruct.StructEF001A.EndDate.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF001A.Forgift, 0, SztStruct.StructEF001A.Forgift.length);
		i += SztStruct.StructEF001A.Forgift.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF001A.SellTerminalID, 0,
				SztStruct.StructEF001A.SellTerminalID.length);
		i += SztStruct.StructEF001A.SellTerminalID.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF001A.SellOpetatorID, 0,
				SztStruct.StructEF001A.SellOpetatorID.length);
		i += SztStruct.StructEF001A.SellOpetatorID.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF001A.UserDefine1, 0,
				SztStruct.StructEF001A.UserDefine1.length);
		i = SztStruct.StructEF001A.UserDefine1.length;
	}

	private int getPackagLen(byte[] paramArrayOfByte) {
		if (paramArrayOfByte.length < 2) {
			return 0;
		}
		return bytesToShort(subarray(paramArrayOfByte, 0, 2), false);
	}

	public static byte[] subarray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
		byte[] EMPTY_BYTE_ARRAY = new byte[0];
		if (paramArrayOfByte == null) {
			return null;
		}
		int i = paramInt1;
		if (paramInt1 < 0) {
			i = 0;
		}
		paramInt1 = paramInt2;
		if (paramInt2 > paramArrayOfByte.length) {
			paramInt1 = paramArrayOfByte.length;
		}
		paramInt1 -= i;
		if (paramInt1 <= 0) {
			return EMPTY_BYTE_ARRAY;
		}
		byte[] arrayOfByte = new byte[paramInt1];
		System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, paramInt1);
		return arrayOfByte;
	}

	private void getRecentTrade(byte[] paramArrayOfByte) {
		int i = 0;
		for (;;) {
			if (i >= this.Sum_18) {
			}
			int j;
			int k;
			int m;
			int n;
			do {
				this.Sum_18 = i;
				// return;
				j = paramArrayOfByte[(i * 23)];
				k = paramArrayOfByte[(i * 23 + 1)];
				m = paramArrayOfByte[(i * 23 + 9)];
				n = Util.toIntR(paramArrayOfByte, i * 23 + 5, 4);
			} while ((j * 256 + k == 0) || ((m == 2) && ((n == 0) || (Integer.MIN_VALUE + n == 0))));
			i += 1;
		}
	}

	private void getTCardInfo() {
		System.arraycopy(this.rApdu, 0, SztStruct.EF0019Data, 0, this.rApdu.length);
		System.arraycopy(this.rApdu, 0, SztStruct.StructEF0019_1.Holder1, 0, SztStruct.StructEF0019_1.Holder1.length);
		int i = SztStruct.StructEF0019_1.Holder1.length;
		SztStruct.StructEF0019_1.Holder2 = 0;
		i = 0 + i + 1;
		SztStruct.StructEF0019_1.CardStaus = this.rApdu[i];
		i += 1;
		SztStruct.StructEF0019_1.TradeType = this.rApdu[i];
		i += 1;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF0019_1.RecentUseDate, 0,
				SztStruct.StructEF0019_1.RecentUseDate.length);
		i += SztStruct.StructEF0019_1.RecentUseDate.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF0019_1.TransferInfo, 0,
				SztStruct.StructEF0019_1.TransferInfo.length);
		i += SztStruct.StructEF0019_1.TransferInfo.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF0019_1.Holder3, 0, SztStruct.StructEF0019_1.Holder3.length);
		i += SztStruct.StructEF0019_1.Holder3.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF0019_1.Holder4, 0, SztStruct.StructEF0019_1.Holder4.length);
		i += SztStruct.StructEF0019_1.Holder4.length;
		System.arraycopy(this.rApdu, i, SztStruct.StructEF0019_1.Holder5, 0, SztStruct.StructEF0019_1.Holder5.length);
		int j = SztStruct.StructEF0019_1.Holder5.length;
		System.arraycopy(this.rApdu, i + j, SztStruct.StructEF0019_1.Holder6, 0,
				SztStruct.StructEF0019_1.Holder6.length);
	}

	//其实paramArrayOfByte的结构和standardpboc的交易日志结构一致,可参考parseLog24函数
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<CardTransactionRecord> getTradeRecords(byte[] paramArrayOfByte) {
		this.tradeRecord = new String[this.Sum_18];
		this.setLastRecord(ByteUtil.subarray(paramArrayOfByte, 0, 23));

		ArrayList localArrayList= new ArrayList();
		if (this.Sum_18 == 0) {
			return localArrayList;
		}
		String str1 = Util.toHexString(paramArrayOfByte, 0, this.Sum_18 * 23);
		int i = 0;
		do {
			int j = i * 46;
			// 交易金额
			double d = Util.toInt(paramArrayOfByte, j / 2 + 5, 4) / 100.0D;
			//交易类型　02,0B充值
			//String strType = str1.substring(j + 18, j + 20);
			//金额正负
			String strType = "";
			String sign = "";
			byte bType = paramArrayOfByte[j/2+9];
			 if (bType == TRANS_CSU_CPX)   //地铁,未验证
			  {
				  strType = "地铁";
				  sign = "-";
			  }
			  else if (bType == TRANS_CSU)   //公交车,未验证
			  {				
				  strType = "公交";
				  sign = "-";
			  }
			  else if (bType == TRANS_CSU_IN)  //充值,未验证
			  {				
				  strType = "充值";
				  sign = "+";
			  }
			  else if (bType == TRANS_CSU_IN2) //充值,已验证
			  {				
				  strType = "充值";
				  sign = "+";
			  }
			  else if (bType == TRANS_CSU_CPX2)  //地铁,已验证
			  {
				  strType = "地铁";
				  sign = "-";
			  }
			  else if (bType == TRANS_CSU_CPX3)  //公交,已验证
			  {
				  strType = "公交";
				  sign = "-";
			  }
			  else if (bType == TRANS_CSU_CPX4)  //圈提,已验证
			  {
				  strType = "圈提";
				  sign = "-";
			  }	 
			  else  //未知
			  {
				  strType = "未知";
				  sign = "-";
				  
			  }
			String str6 = str1.substring(j + 32, j + 46);
			//str1.substring(j + 20, j + 32);

			// 年
			String strYear = str6.substring(0, 4);
			// 月
			String strMonth = str6.substring(4, 6);
			// 日
			String strDay = str6.substring(6, 8);
			// 时
			String strHour = str6.substring(8, 10);
			// 分
			String strMinute = str6.substring(10, 12);
			// 秒
			String strSecond = str6.substring(12, 14);
			String strTime = strYear + "-" + strMonth + "-" + strDay + " " + strHour + ":" + strMinute + ":" + strSecond;
			this.tradeRecord[i] = (strTime +  " " + strType + " "+ sign + d + "元");
			CardTransactionRecord recCard = new CardTransactionRecord();
			recCard.setDate(strTime);
			recCard.setMoney(String.valueOf(d));
			recCard.setTpye(strType);
			localArrayList.add(recCard);
			i += 1;
		} while (i < this.Sum_18);

		return (List<CardTransactionRecord>) localArrayList;
	}

	private boolean sendAndCheckAPDU(byte[] paramArrayOfByte) {
		boolean bool2 = false;
		try {
			this.rApdu = sendRecvApdu(paramArrayOfByte);
			System.out.println("RApdu:-----" + Util.toHexString(this.rApdu));
			this.rApdu = RApduCheck(this.rApdu);
			boolean bool1 = bool2;
			if (this.rApdu != null) {
				paramArrayOfByte = subarray(this.rApdu, this.rApdu.length - 2, this.rApdu.length);
				byte[] arrayOfByte = new byte[2];
				arrayOfByte[0] = -112;
				bool1 = bool2;
				if (Arrays.equals(paramArrayOfByte, arrayOfByte)) {
					bool1 = true;
				}
			}
			return bool1;
		} catch (Exception paramArrayOfByte2) {
			paramArrayOfByte2.printStackTrace();
		}
		return false;
	}

	private byte[] sendApduToDevice(byte[] paramArrayOfByte) {
		try {
			byte[] arrayOfByte2 = sendRecvApdu(paramArrayOfByte);
			int i = arrayOfByte2.length;
			byte[] arrayOfByte1 = arrayOfByte2;
			if (this.nfcF == null) {
				arrayOfByte1 = arrayOfByte2;
				if (arrayOfByte2[(i - 2)] == 97) {
					arrayOfByte1 = new byte[5];
					arrayOfByte1[1] = -64;
					arrayOfByte1[4] = paramArrayOfByte[(i - 1)];
					arrayOfByte1 = sendRecvApdu(arrayOfByte1);
				}
			}
			return arrayOfByte1;
		} catch (Exception paramArrayOfByte2) {
			paramArrayOfByte2.printStackTrace();
		}
		return ByteUtil.codeBCD("6A6A");
	}

	// private static byte uniteBytes(byte paramByte1, byte paramByte2)
	// {
	// return (byte)((byte)(Byte.decode("0x" + new String(new byte[] {
	// paramByte1 })).byteValue() << 4) | Byte.decode("0x" + new String(new
	// byte[] { paramByte2 })).byteValue());
	// }

	// public static byte[] codeBCD(String paramStringx)
	// {
	// String str = paramStringx;
	// if (paramStringx.length() == 1) {
	// str = "0" + paramStringx;
	// }
	// if (i >= str.length() / 2) {
	// return arrayOfByte2;
	// }
	// byte[] arrayOfByte2 = new byte[str.length() / 2];
	// byte[] paramString = new byte[0];
	// try
	// {
	// byte[] arrayOfByte1 = str.getBytes("utf-8");
	// paramString = arrayOfByte1;
	// }
	// catch (UnsupportedEncodingException localUnsupportedEncodingException)
	// {
	//
	// localUnsupportedEncodingException.printStackTrace();
	//
	// }
	// int i = 0;
	//
	// return arrayOfByte2;
	// }

	//
	// public static byte[] CodeBCD(String paramStringx, int paramInt)
	// {
	// byte[] arrayOfByte = new byte[paramInt / 2];
	// byte[] paramString = paramStringx.getBytes();
	// int i = 0;
	// for (;;)
	// {
	// if (i >= paramInt / 2) {
	// return arrayOfByte;
	// }
	// arrayOfByte[i] = uniteBytes(paramString[(i * 2)], paramString[(i * 2 +
	// 1)]);
	// i += 1;
	// }
	// }
	//

	public byte[] GetRespon(byte paramByte) throws Exception {
		byte[] arrayOfByte = new byte[5];
		arrayOfByte[1] = -64;
		arrayOfByte[4] = paramByte;
		System.out.println("CApdu:" + Util.toHexString(arrayOfByte));
		return sendRecvApdu(arrayOfByte);
	}

	// isodep的卡用这个函数检查
	public boolean PukaRechargeCheck(boolean paramBoolean) {
		try {
			if (!checkCardResult(paramBoolean)) {
				return false;
			}
			int iCardNo = Util.toIntR(SztStruct.StructEF0015.AppSequence, 6, 4);
			this.setCardNo(String.valueOf(iCardNo));
			int i = Util.toInt(SztStruct.OverMoneyData, 0, 4);
			this.setCardId(Util.toHexString(this.historyBtyes));
			this.setOverMoney(String.valueOf(i - Integer.MIN_VALUE));
			// this.setTradeRecord(this.tradeRecordList);
			this.setCardType(SztStruct.StructEF0015.CardType);
			this.setCardStatus(SztStruct.StructEF0019_1.CardStaus);
			// LogUtil.printLog("卡片信息", "卡片类型:" +
			// SztStruct.StructEF0015.CardType + "卡片状态̬:" +
			// SztStruct.StructEF0019_1.CardStaus + "卡号:" + this.app.getCardNo()
			// + "余额:" + this.app.getOverMoney() + "出售日期:" +
			// BaseFunction.bytesToHexString(SztStruct.StructEF001A.SellDate) +
			// "有效日期:" +
			// BaseFunction.bytesToHexString(SztStruct.StructEF001A.EndDate));
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public byte[] RApduCheck(byte[] paramArrayOfByte) throws Exception {
		int i = paramArrayOfByte.length;
		byte[] arrayOfByte = paramArrayOfByte;
		if (paramArrayOfByte[(i - 2)] == 97) {
			arrayOfByte = GetRespon(paramArrayOfByte[(i - 1)]);
		}
		return arrayOfByte;
	}

	@SuppressLint({ "NewApi" })
	public void closeChanel() {
		try {
			// if (this.tag != null)
			{
				if (this.dep != null) {
					this.dep.close();
					this.dep = null;
					System.out.println("close A card Chanel===========>");
					return;
				}
				if (this.nfcF != null) {
					this.nfcF = null;
					CardOperatorUtils.getInstance().stopHeartBeatThread();

					// 不在这里关闭
					// this.felica.close();
					return;
				}
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public int getSum_18() {
		return this.Sum_18;
	}

	public String[] getTradeRecord() {
		return this.tradeRecord;
	}

	/* Error */
	@SuppressLint({ "NewApi" })
	public boolean openChanel(NfcF nfcF) {
		this.nfcF = nfcF;
		if (this.nfcF != null) {
			// 下面的代码从鹏淘中抄袭过来
			try {

				CardOperatorUtils.getInstance().startHeartBeatThread(felica);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.physicId = tag.getId(); // felica.getIDm().getBytes();
		return true;
	}

	public byte[] sendApdu(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
		byte[] arrayOfByte2 = null;
		byte[] arrayOfByte1 = null;
		if (paramInt1 == 0) {
			paramInt1 = 0;
			for (;;) {
				if (paramInt1 >= paramInt2) {
					return arrayOfByte1;
				}
				int i = getPackagLen(paramArrayOfByte);
				arrayOfByte2 = sendApduToDevice(subarray(paramArrayOfByte, 2, i + 2));
				arrayOfByte1 = ByteUtil.merge(new byte[][] { arrayOfByte1, ByteUtil
						.merge(new byte[][] { shortToBytes((short) arrayOfByte2.length, false), arrayOfByte2 }) });
				// System.out.println("XXXX::::::::" +
				// Util.toHexString(arrayOfByte1));
				paramArrayOfByte = subarray(paramArrayOfByte, i + 2, paramArrayOfByte.length);
				paramInt1 += 1;
			}
		}
		arrayOfByte1 = arrayOfByte2;
		if (paramInt1 == 1) {
			arrayOfByte1 = sendApduToDevice(paramArrayOfByte);
		}
		return arrayOfByte1;
	}

	public static byte[] shortToBytes(short paramShort, boolean paramBoolean) {
		ByteBuffer localByteBuffer = ByteBuffer.allocate(2);
		if (paramBoolean) {
			localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		}
		// for (;;)
		{
			localByteBuffer.putShort(paramShort);

			localByteBuffer.order(ByteOrder.BIG_ENDIAN);
		}
		return localByteBuffer.array();
	}
	/*
	 * public static byte[] merge(byte[]... paramVarArgs) { int j = 0; int m =
	 * paramVarArgs.length; int i = 0; byte[] arrayOfByte1; if (i >= m) {
	 * arrayOfByte1 = new byte[j]; j = 0; m = paramVarArgs.length; i = 0; if (i
	 * >= m) { return arrayOfByte1; } } else { arrayOfByte1 = paramVarArgs[i];
	 * int k = j; if (arrayOfByte1 != null) { if (arrayOfByte1.length != 0) {
	 * return arrayOfByte1; } } // label65: // for (k = j;; k = j +
	 * arrayOfByte1.length) // { // i += 1; // j = k; // break; // } } byte[]
	 * arrayOfByte2 = paramVarArgs[i]; int k = j; if (arrayOfByte2 != null) { if
	 * (arrayOfByte2.length != 0) { return arrayOfByte2; } } for (k = j;; k = j
	 * + arrayOfByte2.length) { i += 1; j = k; // break;
	 * 
	 * }
	 * 
	 * // label103: // System.arraycopy(arrayOfByte2, 0, arrayOfByte1, j,
	 * arrayOfByte2.length); // return arrayOfByte2; }
	 */

	@SuppressLint({ "NewApi" })
	public byte[] sendRecvApdu(byte[] paramArrayOfByte) throws Exception {
		byte[] arrayOfByte = new byte[2];
		byte[] tmp5_4 = arrayOfByte;
		tmp5_4[0] = 106;
		byte[] tmp11_5 = tmp5_4;
		tmp11_5[1] = 106;
		// tmp11_5;
		if (this.tag != null) {
			if (this.dep != null) {
				// LogUtil.printLog("CAPDU A", "LL==" +
				// ByteUtil.bytesToHexString(paramArrayOfByte));
				arrayOfByte = this.dep.transceive(paramArrayOfByte);
				// LogUtil.printLog("RAPDU A", "LL==" +
				// ByteUtil.bytesToHexString(arrayOfByte));
			}
		} else {
			return arrayOfByte;
		}
		// LogUtil.printLog("CAPDU C", "LL==" +
		// ByteUtil.bytesToHexString(paramArrayOfByte));
		paramArrayOfByte = this.felica.transceive(paramArrayOfByte);
		// LogUtil.printLog("RAPDU C", "LL==" +
		// ByteUtil.bytesToHexString(paramArrayOfByte));
		return paramArrayOfByte;
	}

	public void setSum_18(int paramInt) {
		this.Sum_18 = paramInt;
	}

	public void setTradeRecord(List<CardTransactionRecord> mylist) {
	//	this.tradeRecord = paramArrayOfString;
	}
}
