package org.cloudanarchy.queueplugin.util;

import org.cloudanarchy.queueplugin.QueuePlugin;

public class TabUtil {
    private static String format(double tps) {
        return (tps > 18.0D ? "§a" : (tps > 10.0D ? "§e" : "§c")).toString() + (tps > 20.0D ? "" : "") + String.format("%.2f", Math.min((double)Math.round(tps * 100.0D) / 100.0D, 20.0D));
    }

    public static String getTps() {
        return format(QueuePlugin.getTPS());
    }


    public static String GetFormattedInterval(long ms) {
        String seconds = ms / 1000L % 60L + "";
        String minutes = ms / 60000L % 60L + "";
        String hours = ms / 3600000L % 24L + "";
        String days = ms / 86400000L + "";
       if(days.equals("0")){
           days = "";
       }else {
           days = days + "d ";
       }
       if(hours.equals("0")){
           hours = "";
       }else{
           hours = hours + "h ";
       }
       if(minutes.equals("0")){
           minutes = "";
       }else{
           minutes = minutes + "m ";
       }
       seconds = seconds + "s";

       return days + hours + minutes + seconds;
    }
}
