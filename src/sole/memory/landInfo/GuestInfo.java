package sole.memory.landInfo;

/**
 * Created by SoleMemory
 * on 2017/6/27.
 */
public class GuestInfo {
    public String owner = null;
    public int x1 = 0;
    public int z1 = 0;
    public int x2 = 0;
    public int z2 = 0;
    public boolean canbreak;
    public boolean canplace;
    public boolean canjoin;
    public String id = null;


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

    public String getOwer() {
        return owner;
    }

    public void setCanbreak(boolean canbreak) {
        this.canbreak = canbreak;
    }

    public boolean isCanbreak() {
        return canbreak;
    }

    public boolean isCanjoin() {
        return canjoin;
    }

    public boolean isCanplace() {
        return canplace;
    }

    public void setCanjoin(boolean canjoin) {
        this.canjoin = canjoin;
    }

    public void setCanplace(boolean canplace) {
        this.canplace = canplace;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }
}
