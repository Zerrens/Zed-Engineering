package com.zerren.chainreaction.block.fluid;

import com.zerren.chainreaction.ChainReaction;
import com.zerren.chainreaction.core.DamageSourceCR;
import com.zerren.chainreaction.core.ModPotions;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

/**
 * Created by Zerren on 3/7/2015.
 */
public class BlockFluidCryogenic extends BlockFluidCR {
    private final int potionLevel;
    private final float damageOnCollide;

    public BlockFluidCryogenic(Fluid fluid, Material material, String name, int quanta, int tickr, float hardness, int lightOpacity, int potionLevel, float damageOnCollide) {
        super(fluid, material, name, quanta, tickr, hardness, lightOpacity);
        this.potionLevel = potionLevel;
        this.damageOnCollide = damageOnCollide;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            PotionEffect effect = new PotionEffect(ModPotions.getEffectID("hypothermia"), 600, potionLevel);
            effect.getCurativeItems().clear();

            ((EntityLivingBase) entity).addPotionEffect(effect);
            entity.attackEntityFrom(DamageSourceCR.CRYOGENIC, damageOnCollide);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {

        /*super.randomDisplayTick(world, x, y, z, rand);

        double px = x + rand.nextFloat();
        double py = y + rand.nextFloat();
        double pz = z + rand.nextFloat();

        double vx = (rand.nextFloat() - 0.5) * 3;
        double vy = (rand.nextFloat() - 0.5) * 3;
        double vz = (rand.nextFloat() - 0.5) * 3;

        ChainReaction.proxy.radiationFX(world, px, py, pz, vx, vy, vz, 5);*/
    }
}
