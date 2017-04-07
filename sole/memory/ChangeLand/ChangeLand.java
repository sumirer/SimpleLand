package sole.memory.ChangeWorld;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;

/**
 * Created by SoleMemory on 2017/4/6.
 */
public final  class ChangeLand{

    public String level;
    public Double z1;
    public Double x1;
    public Double x2;
    public Double z2;


/**
 *  
 if(x > dx1 && x < dx2 && z > dz1 && z < dz2){
 * */
   public void setLandChunk() {
       Level leve = Server.getInstance().getLevelByName(level);

       for (double xs=x1+1 ; xs<x2;xs++){
           for (double zx=z1+1;zx<z2;zx++ ){
               for (int ys=0; ys < 128;ys++){
                    if( ys > 0 && ys <10){
                        leve.setBlockIdAt((int)xs,ys,(int)zx, Block.GRASS);
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
