package net.torocraft.torohealthmod.client.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.torocraft.torohealthmod.client.particle.DamageParticles;
import net.torocraft.torohealthmod.mixins.interfaces.EntityLivingBaseExt;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToroHealthEventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        final EntityLivingBase entity = event.entityLiving;
        if (!entity.worldObj.isRemote) {
            return;
        }

        final EntityLivingBaseExt ext = (EntityLivingBaseExt) entity;
        final int prevHp = MathHelper.floor_float(ext.getTorohealth$prevHealth());
        final int hp = MathHelper.floor_float(entity.getHealth());

        // Avoid treating initial spawn sync as a giant heal
        if (hp > prevHp + 2 && entity.ticksExisted < 5) {
            ext.setTorohealth$prevHealth(hp);
            return;
        }

        if (hp == prevHp) {
            return;
        }

        // Only show healing via client-side HP diff.
        // All damage popoffs come from the server-synced true damage.
        if (hp > prevHp) {
            DamageParticles.spawnDamageParticle(entity, prevHp - hp);
        }

        ext.setTorohealth$prevHealth(hp);
    }

}
