package com.example.demo.util;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class TimeUtils {
	
	public static Map<String, String> splitTime(Time time) {
        Map<String, String> timeParts = new HashMap<>();
        if (time != null) {
            String timeStr = time.toString(); // "hh:mm:ss" 形式
            String[] parts = timeStr.split(":");
            timeParts.put("hour", parts[0]); // 時
            timeParts.put("minute", parts[1]); // 分
        } else {
            timeParts.put("hour", "");
            timeParts.put("minute", "");
        }
        return timeParts;
    }

}
