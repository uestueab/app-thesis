package com.test.viewpagerfun.toolbox;

import android.annotation.SuppressLint;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeProvider {

    //return unix timestamp
    public static long now(){
        return System.currentTimeMillis();
    }

    @SuppressLint("SimpleDateFormat")
    public static String toHumanReadableDate(long epoch){
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new java.util.Date(epoch));
    }

    public static LocalDateTime toLocalDateTime(long timestamp){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault());
    }

    public static long toUnixTimestamp(LocalDateTime time){
        ZoneId zoneId = ZoneId.systemDefault();
        return time.atZone(zoneId).toEpochSecond();
    }

}
