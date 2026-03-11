package uk.co.dotcode.asb.neoforge;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import uk.co.dotcode.asb.ASB;
import uk.co.dotcode.asb.ModUtils;
import uk.co.dotcode.asb.event.TooltipEvent;
import uk.co.dotcode.asb.event.WorldJoinEvent;

public class EventHandler {



    @SubscribeEvent
    public void playerInteract(EntityInteract event) {
        if (event.getSide() == LogicalSide.SERVER && event.getTarget() instanceof LivingEntity) {
            ModUtils.checkAndApplyBonusesTo(event.getEntity(), (LivingEntity)event.getTarget(), "interact");
        }

    }

    @SubscribeEvent
    public void playerAttack(AttackEntityEvent event) {
        if (event.getTarget() instanceof LivingEntity) {
            ModUtils.checkAndApplyBonusesTo(event.getEntity(), (LivingEntity)event.getTarget(), "attack");
        }

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void itemToolTip(ItemTooltipEvent event) {
        TooltipEvent.modifyTooltip(event.getEntity(), event.getItemStack().getItem(), event.getToolTip());
    }

    @SubscribeEvent
    public void onWorldLogin(PlayerLoggedInEvent event) {
        WorldJoinEvent.onWorldLogin(event.getEntity());
        ASB.sendConfigIssues(event.getEntity());
    }
}