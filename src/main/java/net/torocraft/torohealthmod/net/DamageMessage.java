package net.torocraft.torohealthmod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.torocraft.torohealthmod.client.particle.DamageParticles;
import net.torocraft.torohealthmod.mixins.interfaces.EntityLivingBaseExt;

public class DamageMessage implements IMessage {

    public int entityId;
    public int damage;

    public DamageMessage() {
    }

    public DamageMessage(int entityId, int damage) {
        this.entityId = entityId;
        this.damage = damage;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.damage = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.damage);
    }

    public static class Handler implements IMessageHandler<DamageMessage, IMessage> {

        @Override
        public IMessage onMessage(DamageMessage message, MessageContext ctx) {
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc.theWorld == null) {
                return null;
            }
            final Entity e = mc.theWorld.getEntityByID(message.entityId);
            if (!(e instanceof EntityLivingBase)) {
                return null;
            }

            final EntityLivingBase living = (EntityLivingBase) e;
            if (message.damage <= 0) {
                return null;
            }

            // Full (overkill) damage popup
            DamageParticles.spawnDamageParticle(living, message.damage);

            // Mark last particle tick (currently unused by handler, but kept for future safety)
            ((EntityLivingBaseExt) living).setTorohealth$lastDamageParticleTick(living.ticksExisted);
            return null;
        }
    }
}
