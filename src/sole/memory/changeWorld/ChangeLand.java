package sole.memory.changeWorld;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import sole.memory.generateWorld.ExpandLand;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/6.
 */
public final  class ChangeLand{

    public String level;
    public Double z1;
    public Double x1;
    public Double x2;
    public Double z2;


    private HashMap<String,String> BLOCK = ExpandLand.BLOCK;
/**
 *   Double dx1 = Double.parseDouble(landinfo.get(key).get("x1").toString());
 Double dz1 = Double.parseDouble(landinfo.get(key).get("z1").toString());
 Double dx2 = Double.parseDouble(landinfo.get(key).get("x2").toString());
 Double dz2 = Double.parseDouble(landinfo.get(key).get("z2").toString());
 if(x > dx1 && x < dx2 && z > dz1 && z < dz2){
 * */
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
       Level leve = Server.getInstance().getLevelByName(level);

       for (double xs=x1+1 ; xs<x2;xs++){
           for (double zx=z1+1;zx<z2;zx++ ){
               for (int ys=0; ys < 100;ys++){
                    if( ys > 1 && ys <10){
                        leve.setBlock(new Vector3((int)xs,ys,(int)zx), Block.get(getBlockInfo(LAND_BLOCK,true),getBlockInfo(LAND_BLOCK,false)));
                   }else{
                        leve.setBlockIdAt((int)xs,ys,(int)zx, Block.AIR);
                    }
               }
           }
       }
       leve.saveChunks();
       leve.save();
   }
}
