package sole.memory.changeWorld;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import sole.memory.generateWorld.ExpandLand;

import java.util.HashMap;

/**
 * Created by SoleMemory
 * on 2017/4/6.
 */
public final  class ChangeLand{

    public String level;
    public Double z1;
    public Double x1;
    public Double x2;
    public Double z2;


    private HashMap<String,String> BLOCK = ExpandLand.BLOCK;

 private String LAND_BLOCK = BLOCK.containsKey("地皮方块")&&BLOCK.get("地皮方块")!=null?BLOCK.get("地皮方块"):Block.GRASS+"";

    private int getBlockInfo(String block,boolean type){
        String[] d= block.split(":");
        if (type){
            return Integer.valueOf(d[0]);
        }
        if (d.length>1)return Integer.valueOf(d[1]);
        return 0;
    }
   public void setLandChunk() {
       Level levels = Server.getInstance().getLevelByName(level);

       for (double xs=x1+1 ; xs<x2;xs++){
           for (double zx=z1+1;zx<z2;zx++ ){
               for (int ys=0; ys < 100;ys++){
                    if( ys > 1 && ys <10){
                        levels.setBlock(new Vector3((int)xs,ys,(int)zx), Block.get(getBlockInfo(LAND_BLOCK,true),getBlockInfo(LAND_BLOCK,false)));
                   }else{
                        levels.setBlockIdAt((int)xs,ys,(int)zx, Block.AIR);
                    }
               }
           }
       }
       levels.saveChunks();
       levels.save();
   }
}
