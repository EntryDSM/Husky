package kr.hs.entrydsm.husky.global.util;

public class Validator {
    public static boolean isBlank(String target) {
        return target == null || target.isBlank();
    }

    public static boolean isExist(String target) {
        if(target == null) return false;
        return !target.isBlank();
    }
    
}
