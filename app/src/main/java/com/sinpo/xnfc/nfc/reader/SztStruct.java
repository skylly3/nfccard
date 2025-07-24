package com.sinpo.xnfc.nfc.reader;

import java.lang.reflect.Array;

public class SztStruct
{
  public static byte[] EF0015Data = new byte[128];
  public static byte[] EF0016Data = new byte[128];
  public static byte[] EF0018Data = new byte[128];
  public static byte[] EF0019Data = new byte[36];
  public static byte[] EF001AData = new byte[128];
  public static byte[] EF001BData = new byte[128];
  public static byte[] OverMoneyData = new byte[64];
  public static int result;
  
  public static class CreditCAPP
  {
    public static byte CalculateType;
    public static byte KeyType;
    public static byte[] MAC1 = new byte[4];
    public static byte[] OverMoney = new byte[4];
    public static byte[] Random;
    public static byte[] TransSN = new byte[2];
    
    static
    {
      Random = new byte[4];
    }
  }
  
  public static class DeductCAPP
  {
    public static byte CalculateType;
    public static byte KeyType;
    public static byte[] OverDraft = new byte[3];
    public static byte[] OverMoney = new byte[4];
    public static byte[] Random = new byte[4];
    public static byte[] TransSN = new byte[2];
  }
  
  public static class StructEF0015
  {
    public static byte[] AppSequence;
    public static byte AppType;
    public static byte AppVersion;
    public static byte CardType;
    public static byte CardVersion;
    public static byte[] IssueCardFile = new byte[2];
    public static byte[] IssueDate;
    public static byte[] PublishCardSign = new byte[8];
    public static byte[] ValidDate;
    
    static
    {
      AppSequence = new byte[10];
      IssueDate = new byte[4];
      ValidDate = new byte[4];
    }
  }
  
  public static class StructEF0016
  {
    public static byte[] CardHolderID = new byte[32];
    public static byte[] CardHolderName = new byte[20];
    public static byte CardHolderSex;
    public static byte CardHolderType;
    public static byte[] Hold1 = new byte[24];
    public static byte PIAppVersion;
    public static byte UnitEmployeeSign;
  }
  
  public static class StructEF0018
  {
    public static byte[][] Overdraft;
    public static byte[][] TerminalID = (byte[][])Array.newInstance(Byte.TYPE, new int[] { 10, 6 });
    public static byte[][] TradeDate = (byte[][])Array.newInstance(Byte.TYPE, new int[] { 10, 4 });
    public static byte[][] TradeMoney;
    public static byte[][] TradeSN = (byte[][])Array.newInstance(Byte.TYPE, new int[] { 10, 2 });
    public static byte[][] TradeTime = (byte[][])Array.newInstance(Byte.TYPE, new int[] { 10, 3 });
    public static byte[] TradeType;
    
    static
    {
      Overdraft = (byte[][])Array.newInstance(Byte.TYPE, new int[] { 10, 3 });
      TradeMoney = (byte[][])Array.newInstance(Byte.TYPE, new int[] { 10, 4 });
      TradeType = new byte[10];
    }
  }
  
  public static class StructEF0019_1
  {
    public static byte CardStaus;
    public static byte[] Holder1 = new byte[2];
    public static byte Holder2;
    public static byte[] Holder3 = new byte[4];
    public static byte[] Holder4 = new byte[4];
    public static byte[] Holder5 = new byte[7];
    public static byte[] Holder6 = new byte[5];
    public static byte[] RecentUseDate = new byte[4];
    public static byte TradeType;
    public static byte[] TransferInfo = new byte[3];
  }
  
  public static class StructEF001A
  {
    public static byte[] EndDate;
    public static byte[] Forgift;
    public static byte[] SellDate = new byte[4];
    public static byte[] SellOpetatorID = new byte[8];
    public static byte[] SellTerminalID;
    public static byte[] UserDefine1 = new byte[2];
    
    static
    {
      EndDate = new byte[4];
      Forgift = new byte[4];
      SellTerminalID = new byte[6];
    }
  }
  
  public static class StructEF001B
  {
    public static byte[] AddValueSum = new byte[4];
    public static byte[] AddValueTradeSN = new byte[2];
    public static byte[] AddedValueBalance = new byte[4];
    public static byte[] AddvalueTradeMoney = new byte[4];
    public static byte[] Hold5 = new byte[20];
    public static byte[] TerminalID = new byte[6];
    public static byte[] TradeDateTime = new byte[7];
    public static byte TradeTpye;
  }
}

 