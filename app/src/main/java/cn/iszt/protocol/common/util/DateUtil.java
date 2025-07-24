package cn.iszt.protocol.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
  public static Date getDateAfterYears(int paramInt)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(1, localCalendar.get(1) + paramInt);
    return localCalendar.getTime();
  }
}


 