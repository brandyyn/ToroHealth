package net.torocraft.torohealthmod.server;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.torocraft.torohealthmod.ToroHealthMod;
import net.torocraft.torohealthmod.net.DamageMessage;

public class DamageSyncEventHandler {

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        final EntityLivingBase entity = event.entityLiving;
        if (entity == null || entity.worldObj == null || entity.worldObj.isRemote) {
            return;
        }

        final float amount = event.ammount; // 1.7.10 field name
        if (amount <= 0.0F) {
            return;
        }

        final int damage = MathHelper.ceiling_float_int(amount);
        ToroHealthMod.NETWORK.sendToAll(new DamageMessage(entity.getEntityId(), damage));
    }
}
