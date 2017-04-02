package sole.memory.generateWorld;


import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.Populator;
import cn.nukkit.level.generator.populator.PopulatorOre;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/30.
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


    @Override
    public void populateChunk(int chunkX, int chunkZ) {

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
                                    this.chunk1.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(x > 4 && z > 4){
                                    this.chunk1.setBlockId(x, y, z, Block.GRASS);//草方块
                                    this.chunk1.setBlockId(4, 11, 4, Block.NETHER_REACTOR);
                                }else{
                                    this.chunk1.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk1.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk2.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(x > 4){
                                    this.chunk2.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk2.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk2.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk13.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(x > 4){
                                    this.chunk13.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk13.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk13.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk3.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(x > 4 && z < 11){
                                    this.chunk3.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk3.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk3.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk4.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(z > 4 ){
                                    this.chunk4.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk4.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk4.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk12.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(z > 4 ){
                                    this.chunk12.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk12.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk12.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk5.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(x < 11 && z > 4){
                                    this.chunk5.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk5.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk5.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk6.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(x < 11 ){
                                    this.chunk6.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk6.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk6.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk11.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(x < 11 ){
                                    this.chunk11.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk11.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk11.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk7.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if(x < 11 && z < 11){
                                    this.chunk7.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk7.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk7.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk8.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if( z < 11){
                                    this.chunk8.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk8.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk8.setBlockId(x, y + 1, z, Block.SLAB);
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
                                    this.chunk10.setBlockId(x, y, z, Block.WOOL);//羊毛
                                }else if( z < 11){
                                    this.chunk10.setBlockId(x, y, z, Block.GRASS);//草方块
                                }else{
                                    this.chunk10.setBlockId(x, y, z, Block.DOUBLE_SLAB);
                                    if (y == 9) {
                                        this.chunk10.setBlockId(x, y + 1, z, Block.SLAB);
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
                                this.chunk9.setBlockId(x, y, z, Block.GRASS);//草方块
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
