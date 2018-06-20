package sole.memory.generateWorld;


import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SoleMemory
 * on 2017/3/30.
 */
public class ExpandLand extends Generator {


    @Override
    public int getId() {
        return 5;
    }

    private ChunkManager level;

    private BaseFullChunk chunk;

    private BaseFullChunk chunk1,chunk2,chunk3,chunk4,chunk5,chunk6,chunk7,chunk8,chunk9,chunk10,chunk11,chunk12,chunk13;

    private NukkitRandom random;

    private List<Populator> populators = new ArrayList<>();

    private Map<String, Object> options;

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public Map<String, Object> getSettings() {
        return this.options;
    }

    @Override
    public String getName() {
        return "land";
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0, 10, 0);
    }

    public ExpandLand() {
        this(new HashMap<>());
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.random = random;
    }


    public static HashMap<String,String> BLOCK = new HashMap<>();

    @Override
    public void populateChunk(int chunkX, int chunkZ) {

    }
    private String AISLE_BLOCK = BLOCK.containsKey("过道方块")&&BLOCK.get("过道方块")!=null?BLOCK.get("过道方块"):Block.WOOD+"";
    private String FRAME_TOP_BLOCK = BLOCK.containsKey("边框顶层方块")&&BLOCK.get("边框顶层方块")!=null?BLOCK.get("边框顶层方块"):Block.SLAB+"";
    private String FRAME_BLOCK = BLOCK.containsKey("边框方块")&&BLOCK.get("边框方块")!=null?BLOCK.get("边框方块"):Block.DOUBLE_SLAB+"";
    private String LAND_SHOP_BLOCK = BLOCK.containsKey("领地石方块")&&BLOCK.get("领地石方块")!=null?BLOCK.get("领地石方块"):Block.NETHER_REACTOR+"";
    private String LAND_BLOCK = BLOCK.containsKey("地皮方块")&&BLOCK.get("地皮方块")!=null?BLOCK.get("地皮方块"):Block.GRASS+"";

    private int getBlockInfo(String block,boolean type){
        String[] d= block.split(":");
        if (type){
            return Integer.valueOf(d[0]);
        }
        if (d.length>1)return Integer.valueOf(d[1]);
        return 0;
    }
    public ExpandLand(Map<String, Object> options) {
        this.options = options;
        this.chunk = null;

        if (this.options.containsKey("decoration")) {
            PopulatorOre ores = new PopulatorOre();
            ores.setOreTypes(new OreType[]{});
            this.populators.add(ores);
        }
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        int CX = (chunkX % 4) < 0 ? ((chunkX % 4) + 4) : (chunkX % 4);
        int CZ = (chunkZ % 4) < 0 ? ((chunkZ % 4) + 4) : (chunkZ % 4);
        String asd = CX+":"+CZ;
        switch (asd){
            case "0:0":
                /**
                 * 16 # # # # @ & & & & & & & & & & &
                 * 15 # # # # @ & & & & & & & & & & &
                 * 14 # # # # @ & & & & & & & & & & &
                 * 13 # # # # @ & & & & & & & & & & &
                 * 12 # # # # @ & & & & & & & & & & &
                 * 11 # # # # @ & & & & & & & & & & &
                 * 10 # # # # @ & & & & & & & & & & &
                 *  9 # # # # @ & & & & & & & & & & &
                 *  8 # # # # @ & & & & & & & & & & &
                 *  7 # # # # @ & & & & & & & & & & &
                 *  6 # # # # @ S & & & & & & & & & &
                 *  5 # # # # @ @ @ @ @ @ @ @ @ @ @ @
                 *  4 # # # # # # # # # # # # # # # #
                 *  3 # # # # # # # # # # # # # # # #
                 *  2 # # # # # # # # # # # # # # # #
                 *  1 # # # # # # # # # # # # # # # #
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6*/
                if(this.chunk1 == null){
                    this.chunk1 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (x < 4 || z < 4) {
                                    this.chunk1.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));//羊毛
                                    this.chunk1.setBlockData(x,y,x,getBlockInfo(AISLE_BLOCK,false));
                                }else if(x > 4 && z > 4){
                                    this.chunk1.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk1.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                    this.chunk1.setBlockId(4, 11, 4, getBlockInfo(LAND_SHOP_BLOCK,true));
                                    this.chunk1.setBlockData(4, 11, 4, getBlockInfo(LAND_SHOP_BLOCK,false));
                                }else{
                                    this.chunk1.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk1.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk1.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk1.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk1.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            case "0:1":
                /**            +
                 * 16 # # # # @ & & & & & & & & & & &
                 * 15 # # # # @ & & & & & & & & & & &
                 * 14 # # # # @ & & & & & & & & & & &
                 * 13 # # # # @ & & & & & & & & & & &
                 * 12 # # # # @ & & & & & & & & & & &
                 * 11 # # # # @ & & & & & & & & & & &
                 * 10 # # # # @ & & & & & & & & & & &
                 *  9 # # # # @ & & & & & & & & & & &
                 *  8 # # # # @ & & & & & & & & & & &
                 *  7 # # # # @ & & & & & & & & & & &
                 *  6 # # # # @ & & & & & & & & & & &
                 *  5 # # # # @ & & & & & & & & & & &
                 *  4 # # # # @ & & & & & & & & & & &
                 *  3 # # # # @ & & & & & & & & & & &
                 *  2 # # # # @ & & & & & & & & & & &
                 *  1 # # # # @ & & & & & & & & & & &
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk2 == null){
                    this.chunk2 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (x < 4 ) {
                                    this.chunk2.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk2.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(x > 4){
                                    this.chunk2.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk2.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk2.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk2.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk2.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk2.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk2.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            case "0:2":
                /**            +
                 * 16 # # # # @ & & & & & & & & & & &
                 * 15 # # # # @ & & & & & & & & & & &
                 * 14 # # # # @ & & & & & & & & & & &
                 * 13 # # # # @ & & & & & & & & & & &
                 * 12 # # # # @ & & & & & & & & & & &
                 * 11 # # # # @ & & & & & & & & & & &
                 * 10 # # # # @ & & & & & & & & & & &
                 *  9 # # # # @ & & & & & & & & & & &
                 *  8 # # # # @ & & & & & & & & & & &
                 *  7 # # # # @ & & & & & & & & & & &
                 *  6 # # # # @ & & & & & & & & & & &
                 *  5 # # # # @ & & & & & & & & & & &
                 *  4 # # # # @ & & & & & & & & & & &
                 *  3 # # # # @ & & & & & & & & & & &
                 *  2 # # # # @ & & & & & & & & & & &
                 *  1 # # # # @ & & & & & & & & & & &
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk13 == null){
                    this.chunk13 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (x < 4 ) {
                                    this.chunk13.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk13.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(x > 4){
                                    this.chunk13.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk13.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk13.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk13.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk13.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk13.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk13.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            case "0:3":
                /**
                 * 16 # # # # # # # # # # # # # # # #
                 * 15 # # # # # # # # # # # # # # # #
                 * 14 # # # # # # # # # # # # # # # #
                 * 13 # # # # # # # # # # # # # # # #
                 * 12 # # # # @ @ @ @ @ @ @ @ @ @ @ @
                 * 11 # # # # @ # # # # # # # # # # #
                 * 10 # # # # @ # # # # # # # # # # #
                 *  9 # # # # @ # # # # # # # # # # #
                 *  8 # # # # @ # # # # # # # # # # #
                 *  7 # # # # @ # # # # # # # # # # #
                 *  6 # # # # @ # # # # # # # # # # #
                 *  5 # # # # @ # # # # # # # # # # #
                 *  4 # # # # @ # # # # # # # # # # #
                 *  3 # # # # @ # # # # # # # # # # #
                 *  2 # # # # @ # # # # # # # # # # #
                 *  1 # # # # @ # # # # # # # # # # #
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk3 == null){
                    this.chunk3 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (x < 4 || z > 11) {
                                    this.chunk3.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk3.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(x > 4 && z < 11){
                                    this.chunk3.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk3.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk3.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk3.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk3.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk3.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk3.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            case "1:0":
                /**
                 * 16 & & & & & & & & & & & & & & & &
                 * 15 & & & & & & & & & & & & & & & &
                 * 14 & & & & & & & & & & & & & & & &
                 * 13 & & & & & & & & & & & & & & & &
                 * 12 & & & & & & & & & & & & & & & &
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 @ @ @ @ @ @ @ @ @ @ @ @ @ @ @ @
                 *  4 # # # # # # # # # # # # # # # #
                 *  3 # # # # # # # # # # # # # # # #
                 *  2 # # # # # # # # # # # # # # # #
                 *  1 # # # # # # # # # # # # # # # #
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk4 == null){
                    this.chunk4 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (z < 4) {
                                    this.chunk4.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk4.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(z > 4 ){
                                    this.chunk4.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk4.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk4.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk4.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk4.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk4.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk4.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;
            case "2:0":
                /**
                 * 16 & & & & & & & & & & & & & & & &
                 * 15 & & & & & & & & & & & & & & & &
                 * 14 & & & & & & & & & & & & & & & &
                 * 13 & & & & & & & & & & & & & & & &
                 * 12 & & & & & & & & & & & & & & & &
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 @ @ @ @ @ @ @ @ @ @ @ @ @ @ @ @
                 *  4 # # # # # # # # # # # # # # # #
                 *  3 # # # # # # # # # # # # # # # #
                 *  2 # # # # # # # # # # # # # # # #
                 *  1 # # # # # # # # # # # # # # # #
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk12 == null){
                    this.chunk12 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (z < 4) {
                                    this.chunk12.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk12.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(z > 4 ){
                                    this.chunk12.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk12.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk12.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk12.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk12.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk12.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk12.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;
            case "3:0":
                /**
                 * 16 & & & & & & & & & & & @ # # # #
                 * 15 & & & & & & & & & & & @ # # # #
                 * 14 & & & & & & & & & & & @ # # # #
                 * 13 & & & & & & & & & & & @ # # # #
                 * 12 & & & & & & & & & & & @ # # # #
                 * 11 & & & & & & & & & & & @ # # # #
                 * 10 & & & & & & & & & & & @ # # # #
                 *  9 & & & & & & & & & & & @ # # # #
                 *  8 & & & & & & & & & & & @ # # # #
                 *  7 & & & & & & & & & & & @ # # # #
                 *  6 & & & & & & & & & & & @ # # # #
                 *  5 @ @ @ @ @ @ @ @ @ @ @ @ # # # #
                 *  4 # # # # # # # # # # # # # # # #
                 *  3 # # # # # # # # # # # # # # # #
                 *  2 # # # # # # # # # # # # # # # #
                 *  1 # # # # # # # # # # # # # # # #
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk5 == null){
                    this.chunk5 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (x > 11 || z<4) {
                                    this.chunk5.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk5.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(x < 11 && z > 4){
                                    this.chunk5.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk5.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk5.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk5.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk5.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk5.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk5.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            case "3:1":
                /**
                 * 16 & & & & & & & & & & & @ # # # #
                 * 15 & & & & & & & & & & & @ # # # #
                 * 14 & & & & & & & & & & & @ # # # #
                 * 13 & & & & & & & & & & & @ # # # #
                 * 12 & & & & & & & & & & & @ # # # #
                 * 11 & & & & & & & & & & & @ # # # #
                 * 10 & & & & & & & & & & & @ # # # #
                 *  9 & & & & & & & & & & & @ # # # #
                 *  8 & & & & & & & & & & & @ # # # #
                 *  7 & & & & & & & & & & & @ # # # #
                 *  6 & & & & & & & & & & & @ # # # #
                 *  5 & & & & & & & & & & & @ # # # #
                 *  4 & & & & & & & & & & & @ # # # #
                 *  3 & & & & & & & & & & & @ # # # #
                 *  2 & & & & & & & & & & & @ # # # #
                 *  1 & & & & & & & & & & & @ # # # #
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk6 == null){
                    this.chunk6 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (x > 11 ) {
                                    this.chunk6.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk6.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(x < 11 ){
                                    this.chunk6.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk6.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk6.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk6.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk6.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk6.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk6.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;
            case "3:2":
                /**
                 * 16 & & & & & & & & & & & @ # # # #
                 * 15 & & & & & & & & & & & @ # # # #
                 * 14 & & & & & & & & & & & @ # # # #
                 * 13 & & & & & & & & & & & @ # # # #
                 * 12 & & & & & & & & & & & @ # # # #
                 * 11 & & & & & & & & & & & @ # # # #
                 * 10 & & & & & & & & & & & @ # # # #
                 *  9 & & & & & & & & & & & @ # # # #
                 *  8 & & & & & & & & & & & @ # # # #
                 *  7 & & & & & & & & & & & @ # # # #
                 *  6 & & & & & & & & & & & @ # # # #
                 *  5 & & & & & & & & & & & @ # # # #
                 *  4 & & & & & & & & & & & @ # # # #
                 *  3 & & & & & & & & & & & @ # # # #
                 *  2 & & & & & & & & & & & @ # # # #
                 *  1 & & & & & & & & & & & @ # # # #
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk11 == null){
                    this.chunk11 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (x > 11 ) {
                                    this.chunk11.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk11.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(x < 11 ){
                                    this.chunk11.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk11.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk11.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk11.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk11.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk11.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk11.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            case "3:3":
                /**
                 * 16 # # # # # # # # # # # # # # # #
                 * 15 # # # # # # # # # # # # # # # #
                 * 14 # # # # # # # # # # # # # # # #
                 * 13 # # # # # # # # # # # # # # # #
                 * 12 @ @ @ @ @ @ @ @ @ @ @ @ # # # #
                 * 11 & & & & & & & & & & & @ # # # #
                 * 10 & & & & & & & & & & & @ # # # #
                 *  9 & & & & & & & & & & & @ # # # #
                 *  8 & & & & & & & & & & & @ # # # #
                 *  7 & & & & & & & & & & & @ # # # #
                 *  6 & & & & & & & & & & & @ # # # #
                 *  5 & & & & & & & & & & & @ # # # #
                 *  4 & & & & & & & & & & & @ # # # #
                 *  3 & & & & & & & & & & & @ # # # #
                 *  2 & & & & & & & & & & & @ # # # #
                 *  1 & & & & & & & & & & & @ # # # #
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk7 == null){
                    this.chunk7 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if (x > 11 || z > 11) {
                                    this.chunk7.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk7.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if(x < 11 && z < 11){
                                    this.chunk7.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk7.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk7.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk7.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk7.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk7.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk7.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            case "1:3":
                /**
                 * 16 # # # # # # # # # # # # # # # #
                 * 15 # # # # # # # # # # # # # # # #
                 * 14 # # # # # # # # # # # # # # # #
                 * 13 # # # # # # # # # # # # # # # #
                 * 12 @ @ @ @ @ @ @ @ @ @ @ @ @ @ @ @
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 & & & & & & & & & & & & & & & &
                 *  4 & & & & & & & & & & & & & & & &
                 *  3 & & & & & & & & & & & & & & & &
                 *  2 & & & & & & & & & & & & & & & &
                 *  1 & & & & & & & & & & & & & & & &
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk8 == null){
                    this.chunk8 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if ( z > 11) {
                                    this.chunk8.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk8.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if( z < 11){
                                    this.chunk8.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk8.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk8.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk8.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk8.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk8.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk8.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            case "2:3":
                /**
                 * 16 # # # # # # # # # # # # # # # #
                 * 15 # # # # # # # # # # # # # # # #
                 * 14 # # # # # # # # # # # # # # # #
                 * 13 # # # # # # # # # # # # # # # #
                 * 12 @ @ @ @ @ @ @ @ @ @ @ @ @ @ @ @
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 & & & & & & & & & & & & & & & &
                 *  4 & & & & & & & & & & & & & & & &
                 *  3 & & & & & & & & & & & & & & & &
                 *  2 & & & & & & & & & & & & & & & &
                 *  1 & & & & & & & & & & & & & & & &
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if(this.chunk10 == null){
                    this.chunk10 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int z;
                    int y;
                    for (x = 0; x < 16; x++) {
                        for (z = 0; z < 16; z++) {
                            for (y = 0; y < 10; y++) {
                                if ( z > 11) {
                                    this.chunk10.setBlockId(x, y, z,getBlockInfo(AISLE_BLOCK,true));
                                    this.chunk10.setBlockData(x, y, z,getBlockInfo(AISLE_BLOCK,false));
                                }else if( z < 11){
                                    this.chunk10.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                    this.chunk10.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                                }else{
                                    this.chunk10.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK,true));
                                    this.chunk10.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK,false));
                                    if (y == 9) {
                                        this.chunk10.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,true));
                                        this.chunk10.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK,false));
                                    }
                                }
                            }
                        }
                    }
                }
                chunk = this.chunk10.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;

            default:
                /**
                 * 16 & & & & & & & & & & & & & & & &
                 * 15 & & & & & & & & & & & & & & & &
                 * 14 & & & & & & & & & & & & & & & &
                 * 13 & & & & & & & & & & & & & & & &
                 * 12 & & & & & & & & & & & & & & & &
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 & & & & & & & & & & & & & & & &
                 *  4 & & & & & & & & & & & & & & & &
                 *  3 & & & & & & & & & & & & & & & &
                 *  2 & & & & & & & & & & & & & & & &
                 *  1 & & & & & & & & & & & & & & & &
                 *	 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 **/
                if (this.chunk9 == null) {
                    this.chunk9 = this.level.getChunk(chunkX, chunkZ).clone();
                    int x;
                    int y;
                    int z;
                    for (x=0; x < 16; x++) {
                        for (z=0; z < 16; z++) {
                            for (y=0; y < 10; y++) {
                                this.chunk9.setBlockId(x, y, z,getBlockInfo(LAND_BLOCK,true));
                                this.chunk9.setBlockData(x,y,x,getBlockInfo(LAND_BLOCK,false));
                            }
                        }
                    }
                }
                chunk = this.chunk9.clone();
                chunk.setX(chunkX);
                chunk.setZ(chunkZ);
                this.level.setChunk(chunkX, chunkZ, chunk);
                break;
        }
    }
}