package sole.memory.listenerEvent;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.utils.TextFormat;
import money.Money;
import sole.memory.SimpleLand;
import sole.memory.changeWorld.ChangeLand;
import sole.memory.landInfo.LandInfo;


/**
 * Created by SoleMemory
 * on 2017/6/29.
 */
public class ListenerEvent extends SimpleLand implements Listener {

    private SimpleLand land;
    public ListenerEvent(SimpleLand land){
        this.land = land;
    }


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (!isLandWord(event.getPlayer().getLevel().getName())) {
            return;
        }
        if (isLandBlock(block,getWorldType(block))) {
            String id = (int)block.x + "-" + (int)block.z + "-" + event.getPlayer().getLevel().getFolderName();
            float prices = Float.valueOf(getConfig().getString("price"));
            if (isBuyLand(id)) {
                if (isAdmin(player.getName().toLowerCase())){
                    ChangeLand s = new ChangeLand();
                    s.level=block.getLevel().getName();
                    s.x1 = block.x;
                    s.z1 = block.z;
                        s.x2 = s.x1 + 39;
                        s.z2 = s.z1 + 39;
                    if (getWorldType(block) ==2) {
                        s.x2 = s.x1 + 55;
                        s.z2 = s.z1 + 55;
                    }
                    s.setLandChunk();
                    removeLand(player,block);
                    player.sendMessage(TextFormat.AQUA + "[SimpleLand管理] 成功删除编号为" + id + "的地皮");
                    event.setCancelled();
                    return;
                }
                if (isOwner(player,id)) {
                    if(getLandCount(player) ==1){
                        player.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功删除编号为" + id + "的地皮，此地皮为领取地皮，无法获得金币");
                    }else {
                        Money.getInstance().addMoney(player, prices / 2);
                        player.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功删除编号为" + id + "的地皮，售出价格为" + prices / 2);
                    }
                    ChangeLand s = new ChangeLand();
                    s.level=block.getLevel().getName();
                    s.x1 = block.x;
                    s.z1 = block.z;
                        s.x2 = s.x1 + 39;
                        s.z2 = s.z1 + 39;
                    if (getWorldType(block) ==2) {
                        s.x2 = s.x1 + 55;
                        s.z2 = s.z1 + 55;
                    }
                    s.setLandChunk();
                    removeLand(player,block);
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
            if (!isOwner(player,block)) {
                if (!isGuest(player, block)) {
                    player.sendTip(TextFormat.RED + "你没有破坏权限");
                    event.setCancelled();
                }

            }
        }
    }

    @EventHandler
    public void good(EntityExplosionPrimeEvent event){
        if (isLandWord(event.getEntity().getLevel().getName())) {
            event.setBlockBreaking(false);
        }
    }

    @EventHandler
    public void EntityExplodeEvent(EntityExplodeEvent tnt) {
        if (isLandWord(tnt.getEntity().getLevel().getName())) {
            tnt.setCancelled();
        }
    }


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void PlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        float price = Float.valueOf(getConfig().getString("price"));
        if(!isLandWord(event.getPlayer().getLevel().getName())){
            return;
        }
        String id = (int)block.x + "-" + (int)block.z + "-" + block.getLevel().getFolderName();
        if(isLandBlock(block,getWorldType(block))) {
            if (isBuyLand(id)){
                event.setCancelled();
                player.sendMessage(TextFormat.BLUE + "[SimpleLand] 此领地已经有主人了！！");
                return;
            }
            if (getLandCount(player) == 0) {
               createNewLand(block,player,getWorldType(block));
                player.sendMessage(TextFormat.BLUE + "[SimpleLand] 成功领取第一块新手地皮，此地皮免费");
                event.setCancelled();
                return;
            } else {
                if(!isAdmin(player.getName().toLowerCase())) {
                    if (getLandCount(player) >= getMaxLandCount()) {
                        player.sendMessage(TextFormat.BLUE + "[SimpleLand] 你的地皮已经超过限制数量，无法购买");
                        event.setCancelled();
                        return;
                    }
                }
                float money = Money.getInstance().getMoney(player);
                if (money < price) {
                    player.sendMessage(TextFormat.BLUE + "[SimpleLand] 你的金币不足！！");
                } else {
                    /*
                     * 经济核心调用 @Him188 的经济核心
                     * */
                    Money.getInstance().reduceMoney(player,price);
                    createNewLand(block,player,getWorldType(block));
                    player.sendMessage(TextFormat.BLUE + "[SimpleLand] 成功购买此地皮，花费" + price + "金币");
                }
                event.setCancelled();
                return;
            }
        } if (!isAdmin(player.getName().toLowerCase())) {
            if (!isOwner(player,block)) {
                    if (!isGuest(player,block)) {
                        player.sendTip(TextFormat.RED + "你没有权限");
                        event.setCancelled();
                    }
            }

        }
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
                                        land.createWorld(args[1],Integer.parseInt(args[2]));
                                        land.addWorld(args[1],Integer.parseInt(args[2]));
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
                                    land.addAdmin(args[2]);
                                    sender.sendMessage(TextFormat.RED + "[SimpleLand] " + args[2] + "成功添加" + args[2] + "为管理员");
                                }
                                if (args[1].equals("del")) {
                                    if (isAdmin(args[2])) {
                                        land.delAdmin(args[2]);
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
                                if (land.isLandWord(player.getLevel().getName())) {
                                    if(land.isOwner(player)){
                                            if(land.isGuest(args[1],land.getLand(player).getId())){
                                                land.delGuest(player,args[1],land.getLand(player).getId());
                                                sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 成功删除访客"+args[1]);
                                            }else {
                                                land.addGuest(args[1],land.getLand(player));
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
                            land.showAllLandName(player);
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
                                            if(land.setLandOwnerChange(player,args[1],info)) {
                                                sender.sendMessage(TextFormat.AQUA + "[SimpleLand] 赠送地皮给玩家" + args[1]);
                                            }else {
                                                player.sendMessage(TextFormat.AQUA + "[SimpleLand] 无法赠送地皮给玩家，该玩家地皮已经上限");
                                            }
                                            sender.sendMessage(TextFormat.AQUA+"[SimpleLand] 强制赠送地皮给玩家"+args[1]);
                                            return true;
                                        }
                                        if(land.isOwner(player)){
                                            if(land.setLandOwnerChange(player,args[1],getLand(player))) {
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
                                            land.setLandName(player,land.getLand(player).getId(),args[1]);
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
                                if(land.isLandName(player,args[1])){
                                    LandInfo a =getLandByName(player,args[1]);
                                    ((Player)sender).setPosition(getServer().getLevelByName(a.getLevel()).getSafeSpawn());
                                    player.teleport(a.getPos());
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
