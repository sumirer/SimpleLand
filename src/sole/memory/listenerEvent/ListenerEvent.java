package sole.memory.listenerEvent;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.utils.TextFormat;
import money.Money;
import sole.memory.SimpleLand;
import sole.memory.changeWorld.ChangeLand;


/**
 * Created by SoleMemory
 * on 2017/6/29.
 */
public class ListenerEvent extends SimpleLand implements Listener {

    private SimpleLand land;

    public ListenerEvent(SimpleLand land) {
        this.land = land;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (!land.isLandWord(event.getPlayer().getLevel().getFolderName())) {
            return;
        }
        if (land.isLandBlock(block, land.getWorldType(block))) {
            String id = (int) block.x + "-" + (int) block.z + "-" + event.getPlayer().getLevel().getFolderName();
            float prices = Float.valueOf(land.getConfig().getString("price"));
            if (land.isBuyLand(id)) {
                if (land.isAdmin(player.getName().toLowerCase())) {
                    ChangeLand s = new ChangeLand();
                    s.level = block.getLevel().getName();
                    s.x1 = block.x;
                    s.z1 = block.z;
                    s.x2 = s.x1 + 39;
                    s.z2 = s.z1 + 39;
                    if (land.getWorldType(block) == 2) {
                        s.x2 = s.x1 + 55;
                        s.z2 = s.z1 + 55;
                    }
                    s.setLandChunk();
                    land.removeLand(player, block);
                    player.sendMessage(TextFormat.AQUA + "[SimpleLand管理] 成功删除编号为" + id + "的地皮");
                    event.setCancelled();
                    return;
                }
                if (land.isOwner(player, id)) {
                    if (land.getLandCount(player) == 1) {
                        player.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功删除编号为" + id + "的地皮，此地皮为领取地皮，无法获得金币");
                    } else {
                        Money.getInstance().addMoney(player, prices / 2);
                        player.sendMessage(TextFormat.AQUA + "[SimpleLand] 成功删除编号为" + id + "的地皮，售出价格为" + prices / 2);
                    }
                    ChangeLand s = new ChangeLand();
                    s.level = block.getLevel().getName();
                    s.x1 = block.x;
                    s.z1 = block.z;
                    s.x2 = s.x1 + 39;
                    s.z2 = s.z1 + 39;
                    if (land.getWorldType(block) == 2) {
                        s.x2 = s.x1 + 55;
                        s.z2 = s.z1 + 55;
                    }
                    s.setLandChunk();
                    land.removeLand(player, block);
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
        if (!land.isAdmin(player.getName().toLowerCase())) {
            if (!land.isOwner(player, block)) {
                if (!land.isGuest(player, block)) {
                    player.sendTip(TextFormat.RED + "你没有破坏权限");
                    event.setCancelled();
                }

            }
        }
    }

    @EventHandler
    public void good(EntityExplosionPrimeEvent event) {
        if (land.isLandWord(event.getEntity().getLevel().getName())) {
            event.setBlockBreaking(false);
        }
    }

    @EventHandler
    public void EntityExplodeEvent(EntityExplodeEvent tnt) {
        if (land.isLandWord(tnt.getEntity().getLevel().getName())) {
            tnt.setCancelled();
        }
    }


    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        float price = Float.valueOf(land.getConfig().getString("price", "20000"));
        if (!land.isLandWord(event.getPlayer().getLevel().getFolderName())) {
            System.out.printf("1");
            return;
        }
        String id = (int) block.x + "-" + (int) block.z + "-" + block.getLevel().getFolderName();
        if (land.isLandBlock(block, land.getWorldType(block))) {
            System.out.printf("2");
            if (land.isBuyLand(id)) {
                event.setCancelled();
                player.sendMessage(TextFormat.BLUE + "[SimpleLand] 此领地已经有主人了！！");
                return;
            }
            if (land.getLandCount(player) == 0) {
                land.createNewLand(block, player, land.getWorldType(block));
                player.sendMessage(TextFormat.BLUE + "[SimpleLand] 成功领取第一块新手地皮，此地皮免费");
                event.setCancelled();
                return;
            } else {
                if (!land.isAdmin(player.getName().toLowerCase())) {
                    if (land.getLandCount(player) >= land.getMaxLandCount()) {
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
                    Money.getInstance().reduceMoney(player, price);
                    land.createNewLand(block, player, land.getWorldType(block));
                    player.sendMessage(TextFormat.BLUE + "[SimpleLand] 成功购买此地皮，花费" + price + "金币");
                }
                event.setCancelled();
                return;
            }
        }
        if (!land.isAdmin(player.getName().toLowerCase())) {
            if (!land.isOwner(player, block)) {
                if (!land.isGuest(player, block)) {
                    player.sendTip(TextFormat.RED + "你没有权限");
                    event.setCancelled();
                }
            }
        }
    }
}
