package net.torocraft.torohealthmod.net;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.torocraft.torohealthmod.client.particle.DamageParticles;
import net.torocraft.torohealthmod.mixins.interfaces.EntityLivingBaseExt;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class DamageMessageHandler implements IMessageHandler<DamageMessage, IMessage> {

    @Override
    public IMessage onMessage(DamageMessage message, MessageContext ctx) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) {
            return null;
        }

        Entity entity = mc.theWorld.getEntityByID(message.entityId);
        if (!(entity instanceof EntityLivingBase)) {
            return null;
        }

        EntityLivingBase living = (EntityLivingBase) entity;
        DamageParticles.spawnDamageParticle(living, message.damage);

        if (living instanceof EntityLivingBaseExt) {
            ((EntityLivingBaseExt) living).setTorohealth$lastDamageParticleTick(living.ticksExisted);
        }

        return null;
    }
}
