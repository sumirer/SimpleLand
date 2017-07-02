package sole.memory.changeWorld;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import sole.memory.SimpleLand;

/**
 * Created by SoleMemory
 * on 2017/7/1.
 */
public class FoundLand {
    public SimpleLand plugin;
    public Player player;
    public int type;

    public Vector3 getPos(){
        int z = (int)player.z;
        int x = (int)player.x;
        for (int o = 5; true; o+=5){
            for (int _x = x-o;_x<=x+o;_x+=1){
                for (int _z = z-o;_z<=z+o;_z+=1){
                    if (plugin.isLandBlock(new Vector3(_x,10,_z),type)){
                        return new Vector3(_x+2,10,_z+2);
                    }
                }
            }
        }
    }
}
