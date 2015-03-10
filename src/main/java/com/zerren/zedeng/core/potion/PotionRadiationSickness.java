package com.zerren.zedeng.core.potion;

import com.zerren.zedeng.ZederrianEngineering;
import com.zerren.zedeng.handler.ConfigHandler;
import com.zerren.zedeng.handler.PacketHandler;
import com.zerren.zedeng.handler.network.player.MessageShader;
import com.zerren.zedeng.proxy.ClientProxy;
import com.zerren.zedeng.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;

/**
 * Created by Zerren on 3/8/2015.
 */
public class PotionRadiationSickness extends PotionZE {

    public PotionRadiationSickness(int id) {
        super(id, "sickness", true, 0x000000, 4);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {

        EntityLivingBase e = event.entityLiving;

        if (e != null) {
            if (e instanceof EntityPlayerMP) {
                PacketHandler.netHandler.sendTo(new MessageShader((byte)0), (EntityPlayerMP)e);
            }
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase e = event.entityLiving;

        //if (e.worldObj.isRemote) {
        if (e instanceof EntityPlayerMP) {
            //has radiation sickness level 3+
            if (hasEffect(e) && getEffectLevel(e) >= 2) {
                //duration is longer than 2 seconds remaining
                if (getEffectTicks(e) > 40) {
                    //if no shader is active
                    if (e.getHealth() > 0)
                        PacketHandler.netHandler.sendTo(new MessageShader((byte)1), (EntityPlayerMP)e);
                }
                //duration is shorter than 2 seconds remaining
                else {
                    //if a shader is active
                    PacketHandler.netHandler.sendTo(new MessageShader((byte)0), (EntityPlayerMP)e);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {

        if (hasEffect(event.entityLiving)) {
            float amount = event.amount;
            int level = getEffectLevel(event.entityLiving);

            //50% heal effectiveness
            if (level == 0) {
                amount *= 0.5;
            }
            //50% heal effectiveness, no natural regeneration (regen potion OK)
            else if (level == 1) {
                if (amount >= 2 || hasEffect(event.entityLiving, Potion.regeneration))
                    amount *= 0.5;
                else {
                    amount = 0;
                    event.setCanceled(true);
                }
            }
            //33% heal effectiveness & no heals under 2 hearts (no natural regen/regen potions)
            else if (level == 2) {
                if (amount >= 4)
                    amount *= 0.33;
                else {
                    amount = 0;
                    event.setCanceled(true);
                }
            }
            //10% heal effectiveness
            else {
                if (amount >= 4)
                    amount *= 0.1;
                else {
                    amount = 0;
                    event.setCanceled(true);
                }
            }
            event.amount = amount;
        }
    }
}
