package sole.memory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import sole.memory.generateWorld.ExpandLand;
import sole.memory.generateWorld.Land;
import sole.memory.landInfo.GuestInfo;
import sole.memory.landInfo.LandInfo;
import sole.memory.listenerEvent.ListenerEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SoleMemory
 * on 2017/3/30
 */
public class SimpleLand extends PluginBase {

   private HashMap<String, HashMap<String, Object>> landinfo = new HashMap<>();

   private HashMap<String, List<String>> guest = new HashMap<>();

    private HashMap<String,Object> landname = new HashMap<>();

    private HashMap<String,HashMap<String,LandInfo>> owner_list = new HashMap<>();
    private HashMap<String,HashMap<String,GuestInfo>> guest_list = new HashMap<>();

/*
* name:
*      id:
*         x1: (int)
*         z1: (int)
*         x2: (int)
*         z2: (int)
*         owner: (string)
*         name: (string)
*
*      id:
*         x1: (int)
*         z1: (int)
*         x2: (int)
*         z2: (int)
*         owner: (string)
*         name: (string)
* ==============================
* name:
*      id:
*         x1: (int)
*         z1: (int)
*         x2: (int)
*         z2: (int)
*         break: (boole)
*         place: (boole)
*         enter: (boole)
*         owner: (string)
* **/


    @Override
    public void onEnable() {

        getDataFolder().mkdir();
        saveDefaultConfig();
        saveResource("resources/World.yml",false);
        this.getServer().getLogger().info("[SimpleLand] 地皮插件加载成功");
        //Config cfg = new Config(getDataFolder() + "/config.yml", Config.YAML);
        initData();
        initGenerateBlock();
        Config c = new Config(getDataFolder() + "/resources/World.yml", Config.YAML);
        for (String landsds:c.getKeys()) {
            createWorld(landsds,Integer.parseInt(c.get(landsds).toString()));
        }
        Server.getInstance().getPluginManager().registerEvents(new ListenerEvent(this), this);
        this.getServer().getLogger().info("[SimpleLand] 加载成功");
    }

    private void initData(){
        Map<String, Object> cfgg = new Config(getDataFolder() + "/Guest.yml", Config.YAML).getAll();
        cfgg.entrySet().forEach((name)->{
            @SuppressWarnings("unchecked")
            HashMap<String,HashMap<String,Object>> s = (HashMap) name.getValue();
            HashMap<String,GuestInfo> ns = new HashMap<>();
            s.entrySet().forEach((id)->{
                GuestInfo mn = new GuestInfo();
                mn.x1 = Integer.valueOf((id.getValue()).get("x1").toString());
                mn.z1 = Integer.valueOf((id.getValue()).get("z1").toString());
                mn.x2 = Integer.valueOf((id.getValue()).get("x2").toString());
                mn.z2 = Integer.valueOf((id.getValue()).get("z2").toString());
                mn.id = id.getValue().get("id").toString();
                mn.owner = (id.getValue()).get("owner").toString();
                mn.canbreak = (boolean)id.getValue().get("break");
                mn.canjoin = (boolean)id.getValue().get("join");
                mn.canplace = (boolean)id.getValue().get("place");
                ns.put(id.getKey(),mn);
            });
            guest_list.put(name.getKey(),ns);
        });
        Map<String, Object> cfga = new Config(getDataFolder() + "/SimpleLand.yml", Config.YAML).getAll();
        cfga.entrySet().forEach((name)->{
            @SuppressWarnings("unchecked")
            HashMap<String,HashMap<String,Object>> s = (HashMap) name.getValue();
            HashMap<String,LandInfo> ns = new HashMap<>();
            s.entrySet().forEach((id)->{
                LandInfo mn = new LandInfo();
                mn.x1 = Integer.valueOf((id.getValue()).get("x1").toString());
                mn.z1 = Integer.valueOf((id.getValue()).get("z1").toString());
                mn.x2 = Integer.valueOf((id.getValue()).get("x2").toString());
                mn.z2 = Integer.valueOf((id.getValue()).get("z2").toString());
                mn.owner = (id.getValue()).get("owner").toString();
                mn.level = (id.getValue()).get("level").toString();
                mn.id = (id.getValue()).get("id").toString();
                mn.name = (id.getValue()).get("name").toString();
                ns.put(id.getKey(),mn);
            });
            owner_list.put(name.getKey(),ns);
        });
    }

    private void initGenerateBlock(){
        saveResource("resources/worldblock.yml");
        Map<String,Object> c = new Config(getDataFolder()+ "/resources/worldblock.yml",Config.YAML).getAll();
        for (Map.Entry k:c.entrySet()) {
            ExpandLand.BLOCK.put(k.getKey().toString(),k.getValue().toString());
        }

    }
    @Override
    public void onDisable() {
        Config c = new Config(getDataFolder() + "/resources/World.yml", Config.YAML);
        for (String landsds:c.getKeys()) {
            getServer().getLevelByName(landsds).unload();
            getServer().getLogger().info("[SimpleLand] 地皮"+landsds+" 保护卸载成功");
        }
    }

    /**
     *创建地图
     **/
    public void createWorld(String world, int type){
        if(type == 1) {
            Generator.addGenerator(Land.class, world, 4);
            getServer().generateLevel(world, 2333, Land.class);
        }
        if(type == 2) {
            Generator.addGenerator(ExpandLand.class, world, 4);
            getServer().generateLevel(world, 2333, ExpandLand.class);
        }
        getServer().loadLevel(world);
        this.getServer().getLogger().info(TextFormat.AQUA+"[SimpleLand] 地皮世界"+world+"加载成功");
        getServer().getLevelByName(world).setSpawnLocation(new Vector3(0.0D, 10.0D, 0.0D));
    }


    @Override
    public void onLoad() {
        Config c = new Config(getDataFolder() + "/resources/World.yml", Config.YAML);
        for (String landsds:c.getKeys()) {
            createWorld(landsds,c.getInt(landsds));
            this.getServer().getLogger().info(TextFormat.AQUA+"[SimpleLand] 地皮世界"+landsds+"加载成功");
        }
    }


  /**
   *  saveData
   * */
    public void saveData(){
       LinkedHashMap<String, Object> con = new LinkedHashMap<>();
       con.putAll(landinfo);
       Config cfg = new Config(getDataFolder() + "/SimpleLand.yml", Config.YAML);
       cfg.setAll(con);
       cfg.save();
     }
    public void saveGuest(){
        LinkedHashMap<String, Object> con = new LinkedHashMap<>();
        con.putAll(guest);
        Config cfg = new Config(getDataFolder() + "/Guest.yml", Config.YAML);
        cfg.setAll(con);
        cfg.save();
    }
    public void saveName() {
        LinkedHashMap<String, Object> con = new LinkedHashMap<>();
        con.putAll(landname);
        Config cfg = new Config(getDataFolder() + "/LandName.yml", Config.YAML);
        cfg.setAll(con);
        cfg.save();
    }


     protected int getWorldType(Block block){
         Config c = new Config(getDataFolder() + "/resources/World.yml", Config.YAML);
        String id = block.getLevel().getFolderName();
        return Integer.parseInt(c.get(id).toString());
     }

     protected int getMaxLandCount(){
         return getConfig().getInt("limit",4);
     }


     protected void removeLand(Player player, Block block){
         String id = (int)block.x + "-" + (int)block.z + "-" + block.getLevel().getFolderName();
         if (owner_list.get(player.getName().toLowerCase()).size()==1){
             owner_list.remove(player.getName().toLowerCase());
             //TODO:sava
             return;
         }
         owner_list.get(player.getName().toLowerCase()).remove(id);

         //TODO:sava
     }

     protected boolean isLandBlock(Block block, int type) {
         if (type == 1) {
             int x = (int) block.getX();
             int z = (int) block.getZ();

             double f = ((double) z + 44) / 48;
             double n = ((double) x + 44) / 48;
             int q = (int) n;
             int w = (int) f;
             if (n == (double) q && (double) w == f) {
                 return true;
             }
         }
         if (type == 2) {
             int x = (int) block.getX();
             int z = (int) block.getZ();

             double f = ((double) z + 60) / 64;
             double n = ((double) x + 60) / 64;
             int q = (int) n;
             int w = (int) f;
             if (n == (double) q && (double) w == f) {
                 return true;
             }
         }
         return false;
     }


     protected int getLandCount(Player player){
         if (!owner_list.containsKey(player.getName().toLowerCase())) return 0;
         return owner_list.get(player.getName().toLowerCase()).size();
     }

    private int getLandCount(String player){
        if (!owner_list.containsKey(player.toLowerCase())) return 0;
        return owner_list.get(player.toLowerCase()).size();
    }


      public boolean isLandWord(String land){
          Config c = new Config(getDataFolder() + "/resources/World.yml", Config.YAML);
          return c.exists(land);
      }


   public void setLandName(Player player, String id,String name){
            LandInfo map = owner_list.get(player.getName().toLowerCase()).get(id);
            map.setName(name);
    }
   public void addWorld(String worldname, int type){
       Config c = new Config(getDataFolder() + "/resources/World.yml", Config.YAML);
       c.set(worldname,type);
       c.save();
   }



    protected boolean isGuest(Player player, Block block){
        if (!guest_list.containsKey(player.getName().toLowerCase())) return  false;
        for (GuestInfo info:guest_list.get(player.getName().toLowerCase()).values()) {
            if (block.x>info.getX1()&&block.x<info.getX2()&&block.z>info.getZ1()&&block.z<info.getZ2()){
                return true;
            }
        }
        return false;
    }
    public boolean isLandName(Player player,String name) {
        if (!owner_list.containsKey(player.getName().toLowerCase())) return false;
        for (LandInfo info : owner_list.get(player.getName().toLowerCase()).values()) {
            if (info.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    protected LandInfo getLandByName(Player player, String name){
        for (LandInfo info:owner_list.get(player.getName().toLowerCase()).values()) {
            if (info.getName().equals(name)){
                return info;
            }
        }
        return null;
    }
    public boolean isGuest(Player player){
        if (!guest_list.containsKey(player.getName().toLowerCase())) return  false;
        for (GuestInfo  info:guest_list.get(player.getName().toLowerCase()).values()) {
            if (player.x>info.getX1()&&player.x<info.getX2()&&player.z>info.getZ1()&&player.z<info.getZ2()){
                return true;
            }
        }
        return false;
    }
    public boolean isGuest(String player,String id) {
        return guest_list.containsKey(player.toLowerCase()) && guest_list.get(player.toLowerCase()).containsKey(id);
    }

    public void addGuest(String player, LandInfo landInfo){
        GuestInfo info = new GuestInfo();
        info.id = landInfo.getId();
        info.x1 = landInfo.getX1();
        info.x2 = landInfo.getX2();
        info.z1 = landInfo.getZ1();
        info.z2 = landInfo.getZ2();
        info.owner = landInfo.getOwner();
        info.canplace = true;
        info.canjoin = true;
        info.canbreak = true;
        if (guest_list.containsKey(player.toLowerCase())){
            HashMap<String,GuestInfo> j = guest_list.get(player.toLowerCase());
            j.put(landInfo.getId(),info);
            guest_list.put(player.toLowerCase(),j);
            return;
        }
        HashMap<String,GuestInfo> n = new HashMap<>();
        n.put(landInfo.getId(),info);
        guest_list.put(landInfo.getId(),n);
        //TODO:要添加保存
    }

    public boolean setLandOwnerChange(Player owner,String player,LandInfo landInfo){
        if (getLandCount(player)>=getMaxLandCount()) return false;
        landInfo.setOwner(player.toLowerCase());
        if (!owner_list.containsKey(player.toLowerCase())){
            HashMap<String,LandInfo> map = new HashMap<>();
            map.put(landInfo.getId(),landInfo);
           owner_list.put(player.toLowerCase(),map);
           //TODO:savaData
        }else {
            HashMap<String,LandInfo> map = owner_list.get(player.toLowerCase());
            map.put(landInfo.getId(),landInfo);
            owner_list.put(player.toLowerCase(),map);
            //TODO:savaData
        }
        owner_list.get(owner.getName().toLowerCase()).remove(landInfo.getId());
        return true;
    }
    public void showAllLandName(Player player){
        player.sendMessage(TextFormat.AQUA + "====地皮列表===");
        if (!owner_list.containsKey(player.getName().toLowerCase())){
            player.sendMessage(TextFormat.RED + "无地皮");
            return;
        }
        int i=1;
        for (Map.Entry key:owner_list.get(player.getName().toLowerCase()).entrySet()) {
            player.sendMessage(TextFormat.GOLD+"-"+i+":  "+((LandInfo)key.getValue()).getName());
            i+=1;
        }

    }


    protected void createNewLand(Block block, Player player, int type){
        LandInfo info = new LandInfo();
        info.x1 = (int)block.x;
        info.z1 = (int)block.z;
        info.z2 = (int)block.z +39;
        info.x2 = (int)block.x +39;
        if (type==2){
            info.z2 = (int)block.z +55;
            info.x2 = (int)block.x +55;
        }
        info.name = setDefaultName(player);
        info.id = (int)block.x + "-" + (int)block.z + "-" + block.getLevel().getFolderName();
        info.owner = player.getName().toLowerCase();
        info.level = block.getLevel().getFolderName();
        if (owner_list.containsKey(player.getName().toLowerCase())){
            HashMap<String,LandInfo> j = owner_list.get(player.getName().toLowerCase());
            j.put(info.getId(),info);
            owner_list.put(player.getName().toLowerCase(),j);
        }else {
            HashMap<String, LandInfo> n = new HashMap<>();
            n.put(info.getId(), info);
            owner_list.put(player.getName().toLowerCase(), n);
        }
        //TODO:SAVA
    }
    private String setDefaultName(Player player) {
        if (!owner_list.containsKey(player.getName().toLowerCase())) return "地皮0";
        return "地皮"+owner_list.get(player.getName().toLowerCase()).size();
    }


    public void delGuest(Player owner, String player, String id){
        if (!guest_list.containsKey(player.toLowerCase())) return;
        for (Map.Entry m:guest_list.get(player.toLowerCase()).entrySet()) {
            if (((GuestInfo)m.getValue()).getOwer().equals(owner.getName().toLowerCase())&&((GuestInfo)m.getValue()).getId().equals(id)){
                guest_list.get(player.toLowerCase()).remove(id);
                return;
            }
        }
    }


    public void addAdmin(String player){
        Config cfg = new Config(getDataFolder() + "/resources/config.yml", Config.YAML);
        List<String> map = cfg.getStringList("admin");
        map.add(player.toLowerCase());
        map.toArray();
        cfg.set("admin",map);
        cfg.save();
    }



    public void delAdmin(String player){
        Config cfg = new Config(getDataFolder() + "/resources/config.yml", Config.YAML);
        List map = cfg.getList("admin");
        map.remove(player.toLowerCase());
        map.toArray();
        cfg.set("admin",map);
        cfg.save();
    }

    protected boolean isAdmin(String player){
        Config cfg = new Config(getDataFolder() + "/resources/config.yml", Config.YAML);
        List map = cfg.getList("admin");
        return map.contains(player.toLowerCase());
    }

    protected boolean isOwner(Player player, Block block){
        if (owner_list.containsKey(player.getName().toLowerCase())) return false;
        for (LandInfo info:owner_list.get(player.getName().toLowerCase()).values()) {
            if (block.x>info.getX1()&&block.x<info.getX2()&&block.z>info.getZ1()&&block.z<info.getZ2()){
                return true;
            }
        }
        return false;
    }
    public boolean isOwner(Player player){
        if (owner_list.containsKey(player.getName().toLowerCase())) return false;
        for (LandInfo info:owner_list.get(player.getName().toLowerCase()).values()) {
            if (player.x>info.getX1()&&player.x<info.getX2()&&player.z>info.getZ1()&&player.z<info.getZ2()){
                return true;
            }
        }
        return false;
    }
    protected boolean isOwner(Player player, String id) {
        return !owner_list.containsKey(player.getName().toLowerCase()) && owner_list.get(player.getName().toLowerCase()).containsKey(id);
    }
    public LandInfo getLand(Player player){
        for (LandInfo info:owner_list.get(player.getName().toLowerCase()).values()) {
            if (player.x>info.getX1()&&player.x<info.getX2()&&player.z>info.getZ1()&&player.z<info.getZ2()){
                return info;
            }
        }
        return null;
    }

    protected boolean isBuyLand(String id) {
        for (HashMap<String,LandInfo> map:owner_list.values()) {
            if (map.containsKey(id)){
                return true;
            }
        }
        return false;
    }
}