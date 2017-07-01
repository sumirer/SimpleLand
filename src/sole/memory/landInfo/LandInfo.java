package sole.memory.landInfo;

import cn.nukkit.math.Vector3;

/**
 * Created by SoleMemory
 * on 2017/6/27.
 */
public class LandInfo {
    public String owner = null;
    public int x1 = 0;
    public int z1 = 0;
    public int x2 = 0;
    public int z2 = 0;
    public String name = null;
    public String id = null;
    public String level = null;

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getZ1() {
        return z1;
    }

    public int getZ2() {
        return z2;
    }

    public String getOwner() {
        return owner;
    }
    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Vector3 getPos(){
        return new Vector3(x1+2,10,z1+2);
    }

    public String getId() {
        return id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLevel() {
        return level;
    }
}
