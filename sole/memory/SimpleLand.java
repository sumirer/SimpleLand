package sole.memory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import money.Money;
import sole.memory.ChangeWorld.ChangeLand;
import sole.memory.generateWorld.ExpandLand;
import sole.memory.generateWorld.Land;


import java.util.*;

/**
 * Created by SoleMemory on 2017/3/30.
 */
public class SimpleLand extends PluginBase implements Listener {

   private HashMap<String, HashMap<String, Object>> landinfo = new HashMap<>();

   private HashMap<String, List<String>> guest = new HashMap<>();

    private HashMap<String,Object> landname = new HashMap<>();



    @Override
    public void onEnable() {

        getDataFolder().mkdir();
        saveDefaultConfig();
        saveResource("World.yml",false);
        this.getServer().getLogger().info("[SimpleLand] 地皮插件加载成功");
        Config cfg = new Config(getDataFolder() + "/config.yml", Config.YAML);
        Map<String, Object> cfgg = new Config(getDataFolder() + "/Guest.yml", Config.YAML).getAll();
        for (Map.Entry<String, Object> entry : cfgg.entrySet()) {
            guest.put(entry.getKey(),(List) entry.getValue());
        }
        Config c = new Config(getDataFolder() + "/World.yml", Config.YAML);
        //  landinfo = new HashMap<>();
        Map<String, Object> con = new Config(getDataFolder() + "/SimpleLand.yml", Config.YAML).getAll();
        con.entrySet().stream().filter(entry -> entry.getValue() instanceof Map).forEach(entry -> {
            landinfo.put(entry.getKey(), (HashMap) entry.getValue());
        });
        Map<String, Object> cs = new Config(getDataFolder() + "/LandName.yml", Config.YAML).getAll();
        cs.entrySet().stream().filter(entry -> entry.getValue() instanceof Objects).forEach(entry -> {
            landname.put(entry.getKey(),entry.getValue());
        });
        for (String landsds:c.getKeys()) {
            createWorld(landsds.toString(),Integer.parseInt(c.get(landsds).toString()));
        }
        Server.getInstance().getPluginManager().registerEvents(this, this);
        this.getServer().getLogger().info("[SimpleLand] 加载成功");
    }
    /**
     *创建地图
     **/
    public void createWorld(String world ,int type){
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
    //TODO:2017.04.01  领地判断和购买算法完成
    //TODO:2017.04.01  领地判断是否有主人，经济核心默认语言
    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
           int x = (int) block.getX();
           int z = (int) block.getZ();
        getDataFolder().mkdir();
        Config cfg = new Config(getDataFolder() + "/config.yml", Config.YAML);
        String prices =  cfg.get("price").toString();
        double price = Double.parseDouble(prices);
        /**
         * 世界判断
         * */
       if(!isLandWord(event.getPlayer().getLevel().getName())){
           return;
       }
        /**
         * 领地石判断
         * */

        if(isLandBlock(block,getWorldType(block))) {
            String landid = x + "-" + z;
            if (isBuyLand(landid + "-" + block.getLevel().getName())) {
                event.setCancelled();
                player.sendMessage(TextFormat.BLUE + "[SimpleLand] 此领地已经有主人了！！");
                return;
            }
            if (getLandCount(player) == 0) {
                HashMap<String, Object> sd = new HashMap<>();
                sd.put("ower", player.getName().toLowerCase());
                sd.put("x1", (int)block.x);
                sd.put("z1", (int)block.z);
                if (getWorldType(block)==1) {
                    sd.put("x2", (int) block.x + 39);
                    sd.put("z2", (int) block.z + 39);
                }
                if (getWorldType(block)==1) {
                    sd.put("x2", (int) block.x + 55);
                    sd.put("z2", (int) block.z + 55);
                }
                sd.put("id", landid + "-" + block.getLevel().getName());
                sd.put("world", block.getLevel().getName());
                landinfo.put(landid + "-" + block.getLevel().getName(), sd);
                saveData();
                setDefaultName(landid + "-" + block.getLevel().getName());
                player.sendMessage(TextFormat.BLUE + "[SimpleLand] 成功领取第一块新手地皮，此地皮免费");
                event.setCancelled();
                return;
            } else {
                if(!isAdmin(player.getName().toLowerCase())) {
                    if (getLandCount(player) >= Integer.parseInt(cfg.get("limit").toString())) {
                        player.sendMessage(TextFormat.BLUE + "[SimpleLand] 你的地皮已经超过限制数量，无法购买");
                        event.setCancelled();
                        return;
                    }
                }
                Double money = Money.getInstance().getMoney(player);
                if (money < price) {
                    player.sendMessage(TextFormat.BLUE + "[SimpleLand] 你的金币不足！！");
                    event.setCancelled();
                } else {
                    Double moneys = Money.getInstance().getMoney(player);
                    /**
                     * 经济核心调用 @Him188 的经济核心
                     * 默认使用第一种货币 @Money1
                     * */
                    Money.getInstance().setMoney(player, moneys - price, false);
                    /**
                     *  多地皮世界支持
                     * */
                    HashMap<String, Object> sd = new HashMap<>();
                    sd.put("ower", player.getName().toLowerCase());
                    sd.put("x1",(int)block.x);
                    sd.put("z1",(int) block.z);
                    if (getWorldType(block)==1) {
                        sd.put("x2", (int) block.x + 39);
                        sd.put("z2", (int) block.z + 39);
                    }
                    if (getWorldType(block)==1) {
                        sd.put("x2", (int) block.x + 55);
                        sd.put("z2", (int) block.z + 55);
                    }
                    sd.put("id", landid + "-" + block.getLevel().getName());
                    sd.put("world", block.getLevel().getName());
                    landinfo.put(landid + "-" + block.getLevel().getName(), sd);
                    saveData();
                    setDefaultName(landid + "-" + block.getLevel().getName());
                    player.sendMessage(TextFormat.BLUE + "[SimpleLand] 成功购买此地皮，花费" + price + "金币");
                    event.setCancelled();
                }
                event.setCancelled();
                return;
            }
        } if (!isAdmin(player.getName().toLowerCase())) {
            String ids = getID(block);
            if (ids == null) {
                event.setCancelled();
                return;
            }
            if (!isOwer(player, ids))
                if (!isGuest(player.getName().toLowerCase(), ids)) {
                    player.sendTip(TextFormat.RED + "你没有权限");
                    event.setCancelled();
                    return;
                }

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

     @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
         Block block = event.getBlock();
         Player player = event.getPlayer();
         getDataFolder().mkdir();
         Config cfg = new Config(getDataFolder() + "/config.yml", Config.YAML);
         double prices = Double.parseDouble(cfg.get("price").toString());
         /**
          * 世界判断
          * */
         if (!isLandWord(event.getPlayer().getLevel().getName())) {
             return;
         }
         if (isLandBlock(block,getWorldType(block))) {
             String id = (int)block.x + "-" + (int)block.z + "-" + event.getPlayer().getLevel().getName().toString();
             /**
              * 权限判断
              * */
             if (isBuyLand(id)) {
                 if (isOwer(player, id)) {
                     if(getLandCount(player) ==1){
                         player.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功删除编号为" + id + "的地皮，此地皮为领取地皮，无法获得金币");
                     }else {
                         Money.getInstance().addMoney(player, prices / 2, false);
                         player.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功删除编号为" + id + "的地皮，售出价格为" + prices / 2);
                     }
                     ChangeLand s = new ChangeLand();
                     s.level=block.getLevel().getName();
                     s.x1 = block.x;
                     s.z1 = block.z;
                     if (getWorldType(block) ==1) {
                         s.x2 = s.x1 + 39;
                         s.z2 = s.z1 + 39;
                     }
                     if (getWorldType(block) ==2) {
                         s.x2 = s.x1 + 55;
                         s.z2 = s.z1 + 55;
                     }
                     s.setLandChunk();
                     landinfo.remove(id);
                     saveData();
                     event.setCancelled();
                     return;
                 } else {
                     player.sendMessage(TextFormat.RED + "[SimpleLand] 你不是领地主人，无法删除领地");
                     event.setCancelled();
                     return;
                 }
             } else {
                 player.sendMessage(TextFormat.AQUA + "[SimpleLand] 这不是一个领地");
                 event.setCancelled();
                 return;
             }
         }
         if (!isAdmin(player.getName().toLowerCase())) {
             String ids = getID(block);
             if (ids == null) {
                 event.setCancelled();
                 return;
             }
             if (!isOwer(player, ids))
                if (!isGuest(player.getName().toLowerCase(), ids)) {
                  player.sendTip(TextFormat.RED + "你没有破坏权限");
                  event.setCancelled();
                  return;
         }

         }
     }
     /**
      * 地皮生成器类型
      * return 1 | 2
      * */

     public int getWorldType(Block block){
         Config c = new Config(getDataFolder() + "/World.yml", Config.YAML);
        String id = block.getLevel().getName();
        return Integer.parseInt(c.get(id).toString());
     }

     /**
      * 领地石判断
      * */
     public boolean isLandBlock(Block block ,int type) {
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

     /**
      * 获取玩家地皮数量
      * */
     public int getLandCount(Player player){
         int i = 0;
         for (HashMap map:landinfo.values()) {
             if(map.get("ower").equals(player.getName().toLowerCase())){
                 i++;
             }
         }
         return i;
     }


      /**
       * 地皮世界判断
       * */
      public boolean isLandWord(String land){
          Config c = new Config(getDataFolder() + "/World.yml", Config.YAML);
          for (String ids:c.getKeys()){
              if(ids.equals(land)) {
                  return true;
              }
          }
          return false;
      }
     /**
      * 获取方块位置是否有地皮，并返回地皮 ID
      * 不然返回 null
      * */
     //TODO: 2017.03.30 遍历方法更新 2333
     public  String getID(Block block) {
         double x =  block.x;
         double z =  block.z;
         for (String key:landinfo.keySet()) {
             Double dx1 = Double.parseDouble(landinfo.get(key).get("x1").toString());
             Double dz1 = Double.parseDouble(landinfo.get(key).get("z1").toString());
             Double dx2 = Double.parseDouble(landinfo.get(key).get("x2").toString());
             Double dz2 = Double.parseDouble(landinfo.get(key).get("z2").toString());
             if(x > dx1 && x < dx2 && z > dz1 && z < dz2){
                 String d = landinfo.get(key).get("id").toString();
                 return d;
             }
         }
         return  null;
     }
    /**
     * 获取玩家位置是否有地皮，并返回地皮 ID
     * 不然返回 null
     * */
    //TODO: 2017.03.30 遍历方法更新 2333
    public  String getID(Player player) {
        double x =  player.x;
        double z =  player.z;
        for (String key:landinfo.keySet()) {
            Double dx1 = Double.parseDouble(landinfo.get(key).get("x1").toString());
            Double dz1 = Double.parseDouble(landinfo.get(key).get("z1").toString());
            Double dx2 = Double.parseDouble(landinfo.get(key).get("x2").toString());
            Double dz2 = Double.parseDouble(landinfo.get(key).get("z2").toString());
            if(x > dx1 && x < dx2 && z > dz1 && z < dz2){
                String d = landinfo.get(key).get("id").toString();
                return  d;
            }
        }
        return null;
    }
/**
 * 设置玩家地皮名字
 * 设置默认名字
 * */
    public void setLandName(String id, String name){
            landname.put(id,name);
            saveName();
    }
   public void addWorld(String worldname,int type){
       Config c = new Config(getDataFolder() + "/World.yml", Config.YAML);
       c.set(worldname,type);
       c.save();
   }

    public Vector3 getLandPoint(String id){
       Double x =  Double.parseDouble(landinfo.get(id).get("x1").toString());
       Double z =  Double.parseDouble(landinfo.get(id).get("z1").toString());
       Double y = (double) 9;
       return new Vector3(x+2,y+1,z+2);
    }

    public String getLandIDbyName(String name){
        for (Map.Entry key:landname.entrySet()) {
            if(key.getValue().equals(name)){
                return key.getKey().toString();
            }
        }
        return null;
    }
    /**
     * 判断是否为访客
     * */
    public boolean isGuest(String player,String id){
      String name = player.toLowerCase();
      if(guest.containsKey(id)) {
          if (guest.get(id).contains(name)) {
              return true;
          }
      }
        return false;
    }

    public void addGuest(String player,String id){
        String name = player.toLowerCase();
        if(guest.containsKey(id)){
            List map = guest.get(id);
            map.add(name);
            map.toArray();
            guest.put(id,map);
       }else{
            List<String> map = new ArrayList<>();
               map.add(player);
               map.toArray();
              guest.put(id,map);
        }
       saveGuest();
    }

    public void showAllLandName(Player player){
        player.sendMessage(TextFormat.AQUA + "====地皮列表===");
        for (Map.Entry key:landname.entrySet()) {
            if(isOwer(player,key.getKey().toString())) {
                String name = key.getValue().toString();
                player.sendMessage(TextFormat.GOLD + name);
            }
        }

    }

    public void setDefaultName(String id){

              landname.put(id,id);
              saveName();
    }

    public void delGuest(String player,String id){
        String name = player.toLowerCase();
        List map = guest.get(id);
        map.remove(name);
        map.toArray();
        guest.put(id,map);
        saveGuest();
    }
    /**
     * 添加 admin 身份
     * */
    public void addAdmin(String player){
        Config cfg = new Config(getDataFolder() + "/config.yml", Config.YAML);
        List map = cfg.getList("admin");
        map.add(player.toLowerCase());
        map.toArray();
        cfg.set("admin",map);
        cfg.save();
    }
    /**
     * 删除 admin 身份
     * */
    public void delAdmin(String player){
        Config cfg = new Config(getDataFolder() + "/config.yml", Config.YAML);
        List map = cfg.getList("admin");
        map.remove(player.toLowerCase());
        map.toArray();
        cfg.set("admin",map);
        cfg.save();
    }
    /**
     * admin身份判断
     * */

    public boolean isAdmin(String player){
        Config cfg = new Config(getDataFolder() + "/config.yml", Config.YAML);
        List map = cfg.getList("admin");
        if(map.contains(player.toLowerCase())){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断是否为主人
     * */
    public boolean isOwer(Player player,String id){
       String ower =  landinfo.get(id).get("ower").toString();
       if(player.getName().toLowerCase().equals(ower)){
           return true;
       }else {
           return false;
       }
    }

    /**
     *购买时，判断是否为购买过的领地
     * */
    public boolean isBuyLand(String id) {
        Iterator keys = landinfo.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (id.equals(key)) {
                 return true;
            }
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args){

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
                                sender.sendMessage(TextFormat.YELLOW + " /land givland <玩家> " + TextFormat.AQUA + "把地皮给一个玩家，管理员无视权限");
                                sender.sendMessage(TextFormat.YELLOW + " /land setname <名字> " + TextFormat.AQUA + "更改地皮名字");
                                if(sender.isPlayer() && sender.isOp()) {
                                    sender.sendMessage(TextFormat.YELLOW + " /land add <名字> <1|2>" + TextFormat.AQUA + "创建一个新的地皮世界");
                                }
                                if(!sender.isPlayer()){
                                    sender.sendMessage(TextFormat.YELLOW + " /land add <名字> <1|2>" + TextFormat.AQUA + "创建一个新的地皮世界");
                                }
                                break;
                            case "add":
                                if(sender.isPlayer()){
                                    if(!isAdmin(player.getName().toLowerCase())){
                                        sender.sendMessage(TextFormat.RED+"WARNING 你不是管理员，请勿执行此命令");
                                        return true;
                                    }else {
                                        if (args.length > 2) {
                                            createWorld(args[1],Integer.parseInt(args[2]));
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
                            case  "guset":
                                if (!sender.isPlayer()) {
                                    sender.sendMessage(TextFormat.RED + "[SimpleLand] 请在游戏中运行此命令");
                                    return true;
                                }
                                if(args.length > 1){
                                    if (isLandWord(player.getLevel().getName())) {
                                        if(getID(player) != null){
                                            if(isOwer(player,getID(player))){
                                                if(isGuest(args[1],getID(player))){
                                                    delGuest(args[1],getID(player));
                                                    sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 成功删除访客"+args[1]);
                                                }else {
                                                    addGuest(args[1],getID(player));
                                                    sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 成功添加访客"+args[1]);
                                                }
                                            }else{
                                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 你不是地皮主人");
                                            }
                                        }else {
                                            sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 这不是一个领地");
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
                                        if(getID(player) != null){
                                            if(isAdmin(player.getName().toLowerCase())){
                                                landinfo.get(getID(player)).put("ower",args[1].toLowerCase());
                                                saveGuest();
                                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 强制赠送地皮给玩家"+args[1]);
                                                return true;
                                            }
                                            if(isOwer(player,getID(player))){
                                                landinfo.get(getID(player)).put("ower",args[1].toLowerCase());
                                                saveGuest();
                                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 赠送地皮给玩家"+args[1]);
                                            }else{
                                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 你不是地皮主人");
                                            }
                                        }else {
                                            sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 这不是一个领地");
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
                                        if (getID(player) != null) {
                                            if(isOwer(player,getID(player))){
                                                setLandName(getID(player),args[1]);
                                                sender.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功设置此地皮的名字为 "+args[1]);
                                            }else{
                                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 你不是地皮主人");
                                            }
                                        }else {
                                            sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 这不是一个领地");
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
                                   if(getLandIDbyName(args[1]) !=null){
                                       String id = getLandIDbyName(args[1]);
                                       ((Player)sender).setPosition(getServer().getLevelByName(landinfo.get(id).get("world").toString()).getSafeSpawn());
                                       player.teleport(getLandPoint(id));
                                       sender.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功传送到地皮 "+args[1]);
                                   }else{
                                       sender.sendMessage(TextFormat.RED + "[SimpleLand] 不存在名字为 "+args[1]+"的地皮");
                                   }
                                }
                                break;

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
