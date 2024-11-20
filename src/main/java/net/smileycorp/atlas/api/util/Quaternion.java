package net.smileycorp.atlas.api.util;

import net.minecraft.util.math.Vec3d;

public class Quaternion {
    
    private float x, y, z, w;
    
    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Quaternion(Vec3d vec, float angle, boolean degrees) {
        if (degrees) angle *= Math.PI / 180f;
        float scale = (float) Math.sin(angle / 2f);
        x = (float) (vec.x * scale);
        y = (float) (vec.y * scale);
        z = (float) (vec.z * scale);
        w = (float) Math.cos(angle / 2f);
    }
    
    public Quaternion(Quaternion quart) {
        x = quart.z;
        y = quart.y;
        z = quart.z;
        w = quart.w;
    }
    
    public void multiply(Quaternion quaternion) {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        float w = this.w;
        this.x = w * quaternion.x + x * quaternion.w + y * quaternion.z - z * quaternion.y;
        this.y = w * quaternion.y- x * quaternion.z + y * quaternion.w + z * quaternion.x;
        this.z = w * quaternion.z + x * quaternion.y - y * quaternion.x + z * quaternion.w;
        this.w = w * quaternion.w - x * quaternion.x - y * quaternion.y - z * quaternion.z;
    }
    
    public Quaternion conjugate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }
    
    public Vec3d transformVector(Vec3d vec3d) {
        Quaternion quart = new Quaternion(this);
        quart.multiply(new Quaternion((float) vec3d.x, (float) vec3d.y, (float) vec3d.z, 0f));
        Quaternion quart1 = new Quaternion(this).conjugate();
        quart.multiply(quart1);
        return new Vec3d(quart.x, quart.y, quart.z);
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getZ() {
        return z;
    }
    
    public float getW() {
        return w;
    }
    
}
