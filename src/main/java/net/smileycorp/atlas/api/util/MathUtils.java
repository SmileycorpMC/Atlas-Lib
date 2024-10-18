package net.smileycorp.atlas.api.util;

public class MathUtils {
    
    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }
    
    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }
    
    public static float wrap(float val, float deviation) {
        return (Math.abs(val % deviation - deviation * 0.5F) - deviation * 0.25F) / (deviation * 0.25F);
    }
    
}
