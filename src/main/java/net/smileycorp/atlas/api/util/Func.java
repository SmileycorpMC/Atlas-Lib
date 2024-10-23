package net.smileycorp.atlas.api.util;

public class Func {
    
    public static boolean True(Object... o) {
        return true;
    }
    
    public static boolean False(Object... o) {
        return false;
    }
    
    public static void Void(Object... o) {}
    
    public static int One(Object... o) {
        return 1;
    }
    
    public static String OneStr(Object... o) {
        return "1";
    }
    
    public static String Str(Object... o) {
        return "";
    }
    
    public static <T> T Null(Object... o) {
        return null;
    }
    
}