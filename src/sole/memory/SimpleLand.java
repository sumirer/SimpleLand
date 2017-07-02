package sole.memory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import sole.memory.changeWorld.FoundLand;
import sole.memory.generateWorld.ExpandLand;
import sole.memory.generateWorld.Land;
import sole.memory.landInfo.GuestInfo;
import sole.memory.landInfo.LandInfo;
import sole.memory.listenerEvent.ListenerEvent;
import sole.memory.tasks.InfoTask;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SoleMemory
 * on 2017/3/30
 */
public class SimpleLand extends PluginBase implements Listener {


    private HashMap<String,HashMap<String,LandInfo>> owner_list = new HashMap<>();
    private HashMap<String,HashMap<String,GuestInfo>> guest_list = new HashMap<>();

    @Override
    public void onLoad() {
        initGenerateBlock();
        createWorld();
    }

    @Override
    public void onEnable() {

        if (getDataFolder().mkdir()){
            Server.getInstance().getLogger().info("Hello SimpleLand");
        }
        if (haveOldDate()){
            changeOldData();
            if (delOldData()){
                Server.getInstance().getLogger().info(TextFormat.AQUA+"旧数据清理成功");
            }
        }
        saveDefaultConfig();
        saveResource("worldblock.yml");
        this.getServer().getLogger().info("[SimpleLand] 地皮插件加载成功");
        initData();
        WorldCheck();
        Server.getInstance().getPluginManager().registerEvents(new ListenerEvent(this), this);
        Server.getInstance().getScheduler().scheduleRepeatingTask(this,new InfoTask(this),3);
        this.getServer().getLogger().info("[SimpleLand] 加载成功");
    }

    private boolean haveOldDate(){
        File file = new File(getDataFolder()+"/SimpleLand.yml");
        File file1 = new File(getDataFolder()+"/LandName.yml");
        File file2 = new File(getDataFolder()+"/Guest.yml");
        return file.exists()||file1.exists()||file2.exists();
    }
    private void changeOldData(){
        Map<String,Object> config = new Config(getDataFolder()+"/SimpleLand.yml",Config.YAML).getAll();
        for (Map.Entry info:config.entrySet()) {
            HashMap map = (HashMap) info.getValue();
            LandInfo mn = new LandInfo();
            mn.x1 = Integer.valueOf(map.get("x1").toString());
            mn.z1 = Integer.valueOf(map.get("z1").toString());
            mn.x2 = Integer.valueOf(map.get("x2").toString());
            mn.z2 = Integer.valueOf(map.get("z2").toString());
            mn.owner = map.get("ower").toString();
            mn.level = map.get("world").toString();
            mn.id = map.get("id").toString();
            mn.name = setDefaultName(mn.owner);
            HashMap<String, LandInfo> map1;
            if (owner_list.containsKey(mn.owner)) {
                 map1 = owner_list.get(mn.owner);
            }else {
                map1 = new HashMap<>();
            }
            map1.put(mn.id,mn);
            owner_list.put(mn.owner,map1);
        }
        initData();
        saveLandData();
        Server.getInstance().getLogger().info(TextFormat.AQUA+"数据转换成功");
    }
    private boolean delOldData() {
        File file = new File(getDataFolder() + "/SimpleLand.yml");
        if (file.exists()) {
            return file.delete();

        }
        File file1 = new File(getDataFolder() + "/LandName.yml");
        if (file1.exists()) {
            return file1.delete();
        }
        File file2 = new File(getDataFolder() + "/Guest.yml");
        return file2.exists() && file2.delete();
    }
    private void initData(){
        Map<String, Object> cfgg = new Config(getDataFolder() + "/SimpleGuest.yml", Config.YAML).getAll();
        cfgg.forEach((name,guest_info)->{
            @SuppressWarnings("unchecked")
            HashMap<String,HashMap<String,Object>> s = (HashMap) guest_info;
            HashMap<String,GuestInfo> ns = new HashMap<>();
            s.forEach((id,all_info)->{
                GuestInfo mn = new GuestInfo();
                mn.x1 = Integer.valueOf(all_info.get("x1").toString());
                mn.z1 = Integer.valueOf(all_info.get("z1").toString());
                mn.x2 = Integer.valueOf(all_info.get("x2").toString());
                mn.z2 = Integer.valueOf(all_info.get("z2").toString());
                mn.id = all_info.get("id").toString();
                mn.owner = all_info.get("owner").toString();
                mn.canbreak = (boolean)all_info.get("break");
                mn.canjoin = (boolean)all_info.get("join");
                mn.canplace = (boolean)all_info.get("place");
                ns.put(id,mn);
            });
            guest_list.put(name,ns);
        });
        Map<String, Object> cfg_land = new Config(getDataFolder() + "/Land.yml", Config.YAML).getAll();
        cfg_land.forEach((name,land_info)->{
            @SuppressWarnings("unchecked")
            HashMap<String,HashMap<String,Object>> s = (HashMap) land_info;
            HashMap<String,LandInfo> ns = new HashMap<>();
            s.forEach((id,all_info)->{
                LandInfo mn = new LandInfo();
                mn.x1 = Integer.valueOf(all_info.get("x1").toString());
                mn.z1 = Integer.valueOf(all_info.get("z1").toString());
                mn.x2 = Integer.valueOf(all_info.get("x2").toString());
                mn.z2 = Integer.valueOf(all_info.get("z2").toString());
                mn.owner = all_info.get("owner").toString();
                mn.level = all_info.get("level").toString();
                mn.id = all_info.get("id").toString();
                mn.name = all_info.get("name").toString();
                ns.put(id,mn);
            });
            owner_list.put(name,ns);
        });
    }

    private void initGenerateBlock(){
        saveResource("worldblock.yml");
        Map<String,Object> c = new Config(getDataFolder()+ "/worldblock.yml",Config.YAML).getAll();
        for (Map.Entry k:c.entrySet()) {
            ExpandLand.BLOCK.put(k.getKey().toString(),k.getValue().toString());
        }

    }
    @Override
    public void onDisable() {
        Config c = new Config(getDataFolder() + "/worldconfig.yml", Config.YAML);
        for (String landsds:c.getKeys()) {
            getServer().getLevelByName(landsds).unload();
            getServer().getLogger().info("[SimpleLand] 地皮"+landsds+" 保护卸载成功");
        }
    }


    private void createWorld(){
        saveResource("worldconfig.yml");
        Config c = new Config(getDataFolder()+ "/worldconfig.yml",Config.YAML);
        for (String landsds:c.getKeys()) {
            if (c.getInt(landsds)==1) {
                Generator.addGenerator(Land.class, landsds, 4);
            }else {
                Generator.addGenerator(ExpandLand.class,landsds,4);
            }
        }
    }

    private void WorldCheck(){
        Config c = new Config(getDataFolder()+ "/worldconfig.yml",Config.YAML);
        for (String landsds:c.getKeys()) {
            if (c.getInt(landsds)==1) {

                if (getServer().getLevelByName(landsds) == null){
                    getServer().generateLevel(landsds,2333,Land.class);
                    getServer().loadLevel(landsds);
                }
            }else {
                if (getServer().getLevelByName(landsds) == null){
                    getServer().generateLevel(landsds,2333,ExpandLand.class);
                    getServer().loadLevel(landsds);
                }
            }

        }
    }


    private void saveLandData(){
       // HashMap<String,HashMap<String,HashMap<String,Object>>> maps = new HashMap<>();
        LinkedHashMap<String, Object> con = new LinkedHashMap<>();
       owner_list.forEach((name,hash)->{
           HashMap<String,HashMap<String,Object>> map = new HashMap<>();
           hash.forEach((id,info)->{
               HashMap<String,Object> is = new HashMap<>();
               is.put("id",info.getId());
               is.put("x1",info.getX1());
               is.put("x2",info.getX2());
               is.put("z1",info.getZ1());
               is.put("z2",info.getZ2());
               is.put("name",info.getName());
               is.put("level",info.getLevel());
               is.put("owner",info.getOwner());
               map.put(id,is);
           });
           con.put(name,map);
       });
        Config cfg = new Config(getDataFolder() + "/Land.yml", Config.YAML);
        cfg.setAll(con);
        cfg.save();
     }
    private void saveGuestData(){
        LinkedHashMap<String, Object> con = new LinkedHashMap<>();
        guest_list.forEach((name,hash)->{
            HashMap<String,HashMap<String,Object>> map = new HashMap<>();
            hash.forEach((id,info)->{
                HashMap<String,Object> is = new HashMap<>();
                is.put("id",info.getId());
                is.put("x1",info.getX1());
                is.put("x2",info.getX2());
                is.put("z1",info.getZ1());
                is.put("z2",info.getZ2());
                is.put("owner",info.getOwer());
                is.put("break",info.canbreak);
                is.put("join",info.canjoin);
                is.put("place",info.canplace);
                map.put(id,is);
            });
            con.put(name,map);
        });
        Config cfg = new Config(getDataFolder() + "/SimpleGuest.yml", Config.YAML);
        cfg.setAll(con);
        cfg.save();
    }


     public int getWorldType(Block block){
         Config c = new Config(getDataFolder() + "/worldconfig.yml", Config.YAML);
        String id = block.getLevel().getFolderName();
        return Integer.parseInt(c.get(id).toString());
     }

    private int getWorldType(Player player){
        Config c = new Config(getDataFolder() + "/worldconfig.yml", Config.YAML);
        String id = player.getLevel().getFolderName();
        return Integer.parseInt(c.get(id).toString());
    }
     public int getMaxLandCount(){
         return getConfig().getInt("limit",4);
     }


     public void removeLand(Player player, Block block) {
         String id = (int) block.x + "-" + (int) block.z + "-" + block.getLevel().getFolderName();
         if (owner_list.get(player.getName().toLowerCase()).size() == 1) {
             owner_list.remove(player.getName().toLowerCase());
             saveLandData();
             return;
         }
         owner_list.get(player.getName().toLowerCase()).remove(id);
         saveLandData();
     }

     public boolean isLandBlock(Block block, int type) {
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

    public boolean isLandBlock(Vector3 vector3, int type) {
        if (type == 1) {
            int x = vector3.getFloorX();
            int z = vector3.getFloorZ();

            double f = ((double) z + 44) / 48;
            double n = ((double) x + 44) / 48;
            int q = (int) n;
            int w = (int) f;
            if (n == (double) q && (double) w == f) {
                return true;
            }
        }
        if (type == 2) {
            int x = vector3.getFloorX();
            int z = vector3.getFloorZ();

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

    private Vector3 getVacantLand(Player player){
        FoundLand land = new FoundLand();
        land.plugin = this;
        land.player = player;
        land.type = getWorldType(player);
        return land.getPos();
    }
     public int getLandCount(Player player){
         if (!owner_list.containsKey(player.getName().toLowerCase())) return 0;
         return owner_list.get(player.getName().toLowerCase()).size();
     }

    private int getLandCount(String player){
        if (!owner_list.containsKey(player.toLowerCase())) return 0;
        return owner_list.get(player.toLowerCase()).size();
    }


      public boolean isLandWord(String land){
          Map<String,Object> c = new Config(getDataFolder() + "/worldconfig.yml", Config.YAML).getAll();
          for (String lands:c.keySet()) {
              if (lands.equals(land)){
                  return true;
              }
          }
          return false;
      }


   private void setLandName(Player player, String id, String name){
            LandInfo map = owner_list.get(player.getName().toLowerCase()).get(id);
            map.setName(name);
    }
   private void addWorld(String worldname, int type){
       Config c = new Config(getDataFolder() + "/worldconfig.yml", Config.YAML);
       c.set(worldname,type);
       c.save();
   }



    public boolean isGuest(Player player, Block block){
        if (!guest_list.containsKey(player.getName().toLowerCase())) return  false;
        for (GuestInfo info:guest_list.get(player.getName().toLowerCase()).values()) {
            if (block.x>info.getX1()&&block.x<info.getX2()&&block.z>info.getZ1()&&block.z<info.getZ2()){
                return true;
            }
        }
        return false;
    }
    private boolean isLandName(Player player, String name) {
        if (!owner_list.containsKey(player.getName().toLowerCase())) return false;
        for (LandInfo info : owner_list.get(player.getName().toLowerCase()).values()) {
            if (info.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    private LandInfo getLandByName(Player player, String name){
        for (LandInfo info:owner_list.get(player.getName().toLowerCase()).values()) {
            if (info.getName().equals(name)){
                return info;
            }
        }
        return new LandInfo();
    }

    public boolean isGuest(Player player, String id) {
        return guest_list.containsKey(player.getName().toLowerCase()) && guest_list.get(player.getName().toLowerCase()).containsKey(id);
    }
    private boolean isGuest(String player, String id) {
        return guest_list.containsKey(player.toLowerCase()) && guest_list.get(player.toLowerCase()).containsKey(id);
    }

    private void addGuest(String player, LandInfo landInfo){
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
        saveGuestData();
    }

    private boolean setLandOwnerChange(Player owner, String player, LandInfo landInfo){
        if (getLandCount(player)>=getMaxLandCount()) return false;
        landInfo.setOwner(player.toLowerCase());
        if (!owner_list.containsKey(player.toLowerCase())){
            HashMap<String,LandInfo> map = new HashMap<>();
            map.put(landInfo.getId(),landInfo);
           owner_list.put(player.toLowerCase(),map);
        }else {
            HashMap<String,LandInfo> map = owner_list.get(player.toLowerCase());
            map.put(landInfo.getId(),landInfo);
            owner_list.put(player.toLowerCase(),map);
        }
        owner_list.get(owner.getName().toLowerCase()).remove(landInfo.getId());
        saveLandData();
        return true;
    }
    private void showAllLandName(Player player){
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


    public void createNewLand(Block block, Player player, int type){
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
        saveLandData();
    }
    private String setDefaultName(Player player) {
        if (!owner_list.containsKey(player.getName().toLowerCase())) return "地皮0";
        return "地皮"+owner_list.get(player.getName().toLowerCase()).size();
    }
    private String setDefaultName(String player) {
        if (!owner_list.containsKey(player.toLowerCase())) return "地皮0";
        return "地皮"+owner_list.get(player.toLowerCase()).size();
    }


    private void delGuest(Player owner, String player, String id){
        if (!guest_list.containsKey(player.toLowerCase())) return;
        for (Map.Entry m:guest_list.get(player.toLowerCase()).entrySet()) {
            if (((GuestInfo)m.getValue()).getOwer().equals(owner.getName().toLowerCase())&&((GuestInfo)m.getValue()).getId().equals(id)){
                guest_list.get(player.toLowerCase()).remove(id);
                return;
            }
        }
    }


    private void addAdmin(String player){
        List<String> map = getConfig().getStringList("admin");
        map.add(player.toLowerCase());
        map.toArray();
        getConfig().set("admin",map);
        getConfig().save();
    }



    private void delAdmin(String player){
        List map = getConfig().getList("admin");
        map.remove(player.toLowerCase());
        map.toArray();
        getConfig().set("admin",map);
        getConfig().save();
    }

    public boolean isAdmin(String player) {
        List map = getConfig().getStringList("admin");
        return map.toArray().length > 0 && map.contains(player.toLowerCase());
    }

    public boolean isOwner(Player player, Block block){
        if (!owner_list.containsKey(player.getName().toLowerCase())) return false;
        for (LandInfo info:owner_list.get(player.getName().toLowerCase()).values()) {
            if (block.x>info.getX1()&&block.x<info.getX2()&&block.z>info.getZ1()&&block.z<info.getZ2()){
                return true;
            }
        }
        return false;
    }
    private boolean isOwner(Player player){
        if (!owner_list.containsKey(player.getName().toLowerCase())) return false;
        for (LandInfo info:owner_list.get(player.getName().toLowerCase()).values()) {
            if (player.x>info.getX1()&&player.x<info.getX2()&&player.z>info.getZ1()&&player.z<info.getZ2()){
                return true;
            }
        }
        return false;
    }
    public boolean isOwner(Player player, String id) {
        return owner_list.containsKey(player.getName().toLowerCase()) && owner_list.get(player.getName().toLowerCase()).containsKey(id);
    }
    private LandInfo getLand(Player player){
        for (LandInfo info:owner_list.get(player.getName().toLowerCase()).values()) {
            if (player.x>info.getX1()&&player.x<info.getX2()&&player.z>info.getZ1()&&player.z<info.getZ2()){
                return info;
            }
        }
        return new LandInfo();
    }

    public LandInfo getStepLand(Player player){
        if (owner_list.isEmpty()) return new LandInfo();
        for (HashMap<String,LandInfo> map: owner_list.values()) {
            for (LandInfo info:map.values()) {
                if (player.x>info.getX1()&&player.x<info.getX2()&&player.z>info.getZ1()&&player.z<info.getZ2()){
                    return info;
                }
            }
        }
        return new LandInfo();
    }

    private HashMap<String,LandInfo> getPlayerAllLand(String player){
        if (!owner_list.containsKey(player.toLowerCase())) return new HashMap<>();
        HashMap<String,LandInfo> map = new HashMap<>();
        int i=1;
        for (LandInfo info:owner_list.get(player.toLowerCase()).values()) {
            map.put(i+"",info);
            i++;
        }
        return map;
    }
    public boolean isBuyLand(String id) {
        for (HashMap<String,LandInfo> map:owner_list.values()) {
            if (map.containsKey(id)){
                return true;
            }
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        Player player = this.getServer().getPlayer(sender.getName());
        switch (command.getName()){
            case "land":
                if(args.length > 0){
                    switch (args[0]) {
                        case "help":
                            sender.sendMessage(TextFormat.AQUA + ">==========[ SimpleLand ]==========<");
                            sender.sendMessage(TextFormat.YELLOW + " /land admin <add|del> " + TextFormat.AQUA + "后台添加地皮的管理员，无视权限");
                            sender.sendMessage(TextFormat.YELLOW + " /land guest <玩家> " + TextFormat.AQUA + "添加访客，给玩家破坏你的地皮授权");
                            sender.sendMessage(TextFormat.YELLOW + " /land myland " + TextFormat.AQUA + "我的地皮列表");
                            sender.sendMessage(TextFormat.YELLOW + " /land tp <地皮名字> " + TextFormat.AQUA + "tp到地皮名字为<地皮名字>的地皮");
                            sender.sendMessage(TextFormat.YELLOW + " /land tp <玩家> <地皮名字> " + TextFormat.AQUA + "管理员tp到<玩家>地皮名字为<地皮名字>的地皮");
                            sender.sendMessage(TextFormat.YELLOW + " /land giveland <玩家> " + TextFormat.AQUA + "把地皮给一个玩家，管理员无视权限");
                            sender.sendMessage(TextFormat.YELLOW + " /land setname <名字> " + TextFormat.AQUA + "更改地皮名字");
                            sender.sendMessage(TextFormat.YELLOW + " /land land <玩家> " + TextFormat.AQUA + "查询此玩家的地皮");
                            sender.sendMessage(TextFormat.YELLOW + " /land found " + TextFormat.AQUA + "自动寻找附近的空地皮，然后传送");
                            if(sender.isPlayer() && sender.isOp()) {
                                sender.sendMessage(TextFormat.YELLOW + " /land add <名字> <1|2>" + TextFormat.AQUA + "创建一个新的地皮世界");
                            }
                            if(!sender.isPlayer()){
                                sender.sendMessage(TextFormat.YELLOW + " /land add <名字> <1|2>" + TextFormat.AQUA + "创建一个新的地皮世界");
                            }
                            break;
                        case "found":
                            if (!sender.isPlayer()){
                                player.sendMessage(TextFormat.RED+"[SimpleLand] 请在游戏内执行此命令");
                                return true;
                            }
                            if (!isLandWord(player.getLevel().getFolderName())){
                                player.sendMessage(TextFormat.RED+"[SimpleLand] 这不是地皮世界");
                                return true;
                            }
                            player.teleport(getVacantLand(player));
                            player.sendMessage(TextFormat.AQUA+"[SimpleLand] 已经传送到空地皮");
                            break;
                        case "land":
                            if (!player.isOp()){
                                player.sendMessage(TextFormat.RED+"[SimpleLand] 你不是管理员");
                                return true;
                            }
                            if (args.length>1){
                                if (getPlayerAllLand(args[1]).isEmpty()){
                                    player.sendMessage(TextFormat.AQUA+"此玩家没有地皮");
                                    return true;
                                }
                                player.sendMessage(TextFormat.AQUA+">>>===== 玩家:"+args[1]+" ====<<<");
                                for (Map.Entry map:getPlayerAllLand(args[1]).entrySet()) {
                                    player.sendMessage(TextFormat.GOLD+"-> "+map.getKey().toString()+"   "+((LandInfo)map.getValue()).getName());
                                }
                                player.sendMessage(TextFormat.AQUA+">>>==========================<<<");
                            }
                        case "add":
                            if(sender.isPlayer()){
                                if(!isAdmin(player.getName().toLowerCase())){
                                    sender.sendMessage(TextFormat.RED+"WARNING 你不是管理员，请勿执行此命令");
                                    return true;
                                }else {
                                    if (args.length > 2) {
                                        addWorld(args[1],Integer.parseInt(args[2]));
                                        sender.sendMessage(TextFormat.RED + "WARNING 成功创建地皮世界 " + args[1]);
                                    }else{
                                        sender.sendMessage(TextFormat.RED + "WARNING 请输入世界名字和世界类型 <1|2>");
                                    }
                                }
                            }
                            break;
                        case "admin":
                            if (sender.isPlayer()) {
                                sender.sendMessage(TextFormat.RED + "[SimpleLand] 请在后台运行此命令");
                                return true;
                            }
                            if (args.length > 2) {
                                if (args[1].equals("add")) {
                                    if (isAdmin(args[2])) {
                                        sender.sendMessage(TextFormat.RED + "[SimpleLand] " + args[2] + "已经是管理员了，无法再次添加");
                                        return true;
                                    }
                                    addAdmin(args[2]);
                                    sender.sendMessage(TextFormat.RED + "[SimpleLand] " + args[2] + "成功添加" + args[2] + "为管理员");
                                }
                                if (args[1].equals("del")) {
                                    if (isAdmin(args[2])) {
                                        delAdmin(args[2]);
                                        sender.sendMessage(TextFormat.RED + "[SimpleLand] " + args[2] + "已经删除" + args[2] + "的管理员身份");
                                        return true;
                                    } else {
                                        sender.sendMessage(TextFormat.RED + "[SimpleLand] " + args[2] + "不是管理员，无法删除");
                                        return true;
                                    }
                                }
                            } else {
                                sender.sendMessage(TextFormat.RED + "[SimpleLand] 请输入/land help 查看更多帮助");
                            }
                            break;
                        case  "guest":
                            if (!sender.isPlayer()) {
                                sender.sendMessage(TextFormat.RED + "[SimpleLand] 请在游戏中运行此命令");
                                return true;
                            }
                            if(args.length > 1){
                                if (isLandWord(player.getLevel().getName())) {
                                    if(isOwner(player)){
                                        if(isGuest(args[1],getLand(player).getId())){
                                            delGuest(player,args[1],getLand(player).getId());
                                            sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 成功删除访客"+args[1]);
                                        }else {
                                            addGuest(args[1],getLand(player));
                                            sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 成功添加访客"+args[1]);
                                        }
                                    }else {
                                        sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 这不是你的领地");
                                    }
                                }else {
                                    sender.sendMessage(TextFormat.RED+"[SimpleLand] 这不是领地地图");
                                }
                            }else {
                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 请输入/land help 查看更多帮助");
                            }
                            break;
                        case "myland":
                            if (!sender.isPlayer()) {
                                sender.sendMessage(TextFormat.RED + "[SimpleLand] 请在游戏中运行此命令");
                                return true;
                            }
                            showAllLandName(player);
                            break;
                        case "giveland":
                            if (!sender.isPlayer()) {
                                sender.sendMessage(TextFormat.RED + "[SimpleLand] 请在游戏中运行此命令");
                                return true;
                            }
                            if(args.length > 1){
                                if (isLandWord(player.getLevel().getName())) {

                                    if(isAdmin(player.getName().toLowerCase())){
                                        LandInfo info = getLand(player);
                                        if (info==null){
                                            player.sendMessage(TextFormat.AQUA+"[SimpleLand] 这里没有领地");
                                        }
                                        if(setLandOwnerChange(player,args[1],info)) {
                                            sender.sendMessage(TextFormat.AQUA + "[SimpleLand] 赠送地皮给玩家" + args[1]);
                                        }else {
                                            player.sendMessage(TextFormat.AQUA + "[SimpleLand] 无法赠送地皮给玩家，该玩家地皮已经上限");
                                        }
                                        sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 强制赠送地皮给玩家"+args[1]);
                                        return true;
                                    }
                                    if(isOwner(player)){
                                        if(setLandOwnerChange(player,args[1],getLand(player))) {
                                            sender.sendMessage(TextFormat.AQUA + "[SimpleLand] 赠送地皮给玩家" + args[1]);
                                        }else {
                                            player.sendMessage(TextFormat.AQUA + "[SimpleLand] 无法赠送地皮给玩家，该玩家地皮已经上限");
                                        }
                                    }else {
                                        sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 这不是你的领地");
                                    }
                                }else {
                                    sender.sendMessage(TextFormat.RED+"[SimpleLand] 这不是领地地图");
                                }
                            }else {
                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 请输入/land help 查看更多帮助");
                            }
                            break;
                        case  "setname":
                            if (!sender.isPlayer()) {
                                sender.sendMessage(TextFormat.RED + "[SimpleLand] 请在游戏中运行此命令");
                                return true;
                            }
                            if(args.length > 1){
                                if (isLandWord(player.getLevel().getName())) {
                                    if (isOwner(player)) {
                                        setLandName(player,getLand(player).getId(),args[1]);
                                        sender.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功设置此地皮的名字为 "+args[1]);
                                    }else {
                                        sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 这不是你的领地");
                                    }
                                }else {
                                    sender.sendMessage(TextFormat.RED+"[SimpleLand] 这不是领地地图");
                                }
                            }else {
                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 请输入/land help 查看更多帮助");
                            }
                            break;
                        case "tp":
                            if (!sender.isPlayer()) {
                                sender.sendMessage(TextFormat.RED + "[SimpleLand] 请在游戏中运行此命令");
                                return true;
                            }
                            if(args.length > 1){
                                if(isLandName(player,args[1])){
                                    LandInfo a =getLandByName(player,args[1]);
                                    ((Player)sender).setPosition(getServer().getLevelByName(a.getLevel()).getSafeSpawn());
                                    player.teleport(a.getPos());
                                    sender.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功传送到地皮 "+args[1]);
                                }else{
                                    sender.sendMessage(TextFormat.RED + "[SimpleLand] 不存在名字为 "+args[1]+"的地皮");
                                }
                            }else if (args.length>2){
                                if (!player.isOp()){
                                    player.sendMessage(TextFormat.RED+"你不是管理员");
                                    return true;
                                }
                                if (getPlayerAllLand(args[1])==null){
                                    player.sendMessage(TextFormat.GOLD+"此玩家没有领地");
                                    return true;
                                }
                                for (LandInfo info:getPlayerAllLand(args[1]).values()) {
                                    if (info.getName().equals(args[2])){
                                        ((Player)sender).setPosition(getServer().getLevelByName(info.getLevel()).getSafeSpawn());
                                        player.teleport(info.getPos());
                                    }
                                }
                            }else {
                                player.sendMessage(TextFormat.GOLD+"请输入/land help 查看更多帮助");
                            }
                            break;

                       default:
                           sender.sendMessage(TextFormat.AQUA+"请输入/land help 查看帮助");
                    }
                    return true;
                }else{
                    sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 请输入/land help 查看更多帮助！！");
                }
                break;
        }
        return true;
    }
}