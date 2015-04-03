package com.zerren.zedeng.proxy;

import com.zerren.zedeng.block.tile.chest.TEChest;
import com.zerren.zedeng.block.tile.reactor.TEHeatExchanger;
import com.zerren.zedeng.client.fx.EntityRadiationFX;
import com.zerren.zedeng.client.fx.EntitySteamFX;
import com.zerren.zedeng.client.render.item.ItemRendererTubes;
import com.zerren.zedeng.client.render.item.ItemRendererVaultChest;
import com.zerren.zedeng.client.render.tileentity.TESRChest;
import com.zerren.zedeng.client.render.tileentity.TESRHeatExchanger;
import com.zerren.zedeng.core.ModBlocks;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.Random;

/**
 * Created by Zerren on 2/19/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static Random rand = new Random();

    public ClientProxy getClientProxy() {
        return this;
    }

    @Override
    public void registerEventHandlers() {

    }

    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public boolean isTheClientPlayer(EntityLivingBase entity) {
        return entity == Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void initRenderingAndTextures() {
        //Vault Chest
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.chest), new ItemRendererVaultChest());
        ClientRegistry.bindTileEntitySpecialRenderer(TEChest.class, new TESRChest());
        //Heat Exchanger
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.exchanger), new ItemRendererTubes());
        ClientRegistry.bindTileEntitySpecialRenderer(TEHeatExchanger.class, new TESRHeatExchanger());
    }

    @Override
    public void playSound(World world, float x, float y, float z, String soundName, float volume, float pitch) {
        world.playSoundEffect(Math.floor(x) + 0.5, Math.floor(y) + 0.5, Math.floor(z) + 0.5, soundName, volume, pitch);
    }

    @Override
    public void setShader(String shader) {
        while(!FMLClientHandler.instance().getClient().entityRenderer.isShaderActive() || !(FMLClientHandler.instance().getClient().entityRenderer.getShaderGroup().getShaderGroupName().equals("minecraft:shaders/post/" + shader + ".json")))
            FMLClientHandler.instance().getClient().entityRenderer.activateNextShader();
    }

    @Override
    public void removeShader() {
        FMLClientHandler.instance().getClient().entityRenderer.deactivateShader();
    }

    @Override
    public boolean isShaderActive() {
        return FMLClientHandler.instance().getClient().entityRenderer.isShaderActive();
    }

    @Override
    public void radiationFX(World world, double x, double y, double z, double velX, double velY, double velZ, int power) {

        if (shouldSpawnParticle()) {
            EntityRadiationFX fx = new EntityRadiationFX(world, x, y, z, velX, velY, velZ, power);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }

    @Override
    public void steamFX(World world, double x, double y, double z, double velX, double velY, double velZ, float scale) {

        if (shouldSpawnParticle()) {
            EntityFX fx = new EntitySteamFX(world, x, y, z, velX, velY, velZ, scale);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }

    private boolean shouldSpawnParticle() {
        Minecraft mc = FMLClientHandler.instance().getClient();

        int particleSetting = mc.gameSettings.particleSetting;

        if (particleSetting == 1 && rand.nextInt(3) == 0) {
            particleSetting = 2;
        }

        return particleSetting <= 1;
    }
}
