package com.sinpo.xnfc.nfc.reader;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.sinpo.xnfc.nfc.tech.FeliCa;

public class CardOperatorUtils
{
  private static CardOperatorUtils Instance = null;
  TimerTask heartBeatTask;
  Timer heartBeatTimer;
  
  public static CardOperatorUtils getInstance()
  {
    if (Instance == null) {
      Instance = new CardOperatorUtils();
    }
    return Instance;
  }
  
  public void startHeartBeatThread(final FeliCa.Tag paramTag) throws IOException
  {
    this.heartBeatTimer = new Timer();
    this.heartBeatTask = new TimerTask()
    {
      public void run()
      {
        try {
			paramTag.reauest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//stopHeartBeatThread();
		}
      }
    };
    this.heartBeatTimer.schedule(this.heartBeatTask, 0L, 100L);
  }
  
  public void stopHeartBeatThread()
  {
    if (this.heartBeatTimer != null)
    {
      this.heartBeatTimer.cancel();
      this.heartBeatTimer = null;
    }
    if (this.heartBeatTask != null)
    {
      this.heartBeatTask.cancel();
      this.heartBeatTask = null;
    }
  }
}

 