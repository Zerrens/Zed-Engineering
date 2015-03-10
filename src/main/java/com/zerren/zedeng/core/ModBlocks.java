package com.zerren.zedeng.core;

import com.zerren.zedeng.ZederrianEngineering;
import com.zerren.zedeng.block.*;
import com.zerren.zedeng.block.fluid.BlockFluidCoolantCold;
import com.zerren.zedeng.block.fluid.BlockFluidCoolantHot;
import com.zerren.zedeng.block.fluid.BlockFluidSteam;
import com.zerren.zedeng.block.fluid.BlockFluidZE;
import com.zerren.zedeng.handler.ConfigHandler;
import com.zerren.zedeng.item.itemblock.*;
import com.zerren.zedeng.reference.Names;
import com.zerren.zedeng.reference.Textures;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;

/**
 * Created by Zerren on 2/19/2015.
 */
public class ModBlocks {

    public static BlockZE glass = new BlockGlass(Names.Blocks.GLASS, Names.Blocks.GLASS_SUBTYPES, Material.glass, 0.4F, 2F, Block.soundTypeGlass, Textures.folders.GLASS_FOLDER, ZederrianEngineering.cTabZE);
    public static BlockZE vault = new BlockVault(Names.Blocks.VAULT, Names.Blocks.VAULT_SUBTYPES, Material.rock, 3F, 15F, Block.soundTypeStone, Textures.folders.VAULT_FOLDER, ZederrianEngineering.cTabZE);
    public static BlockZE chest = new BlockZedChest(Names.Blocks.CHEST, Names.Blocks.CHEST_SUBTYPES, Material.rock, 3F, 15F, Block.soundTypeStone, Textures.folders.VAULT_FOLDER, ZederrianEngineering.cTabZE);
    public static BlockZE exchanger = new BlockExchanger(Names.Blocks.EXCHANGER, Names.Blocks.EXCHANGER_SUBTYPES, Material.iron, 3F, 10F, Block.soundTypeMetal, Textures.folders.REACTOR_FOLDER, ZederrianEngineering.cTabZE);

    public static BlockFluidZE coolantCold = new BlockFluidCoolantCold(ModFluids.coolantColdFluid, Material.water, Names.Fluids.COOLANT_COLD, 7, 5, 100F, 3);
    public static BlockFluidZE coolantHot = new BlockFluidCoolantHot(ModFluids.coolantHotFluid, Material.water, Names.Fluids.COOLANT_HOT, 8, 5, 100F, 3);
    public static BlockFluidZE steam = new BlockFluidSteam(ModFluids.steam, new MaterialLiquid(MapColor.silverColor), Names.Fluids.STEAM, 8, 2, 1F, 1);

    public static void init() {

        GameRegistry.registerBlock(glass, ItemBlockGlass.class, Names.Blocks.GLASS);
        GameRegistry.registerBlock(vault, ItemBlockVault.class, Names.Blocks.VAULT);
        GameRegistry.registerBlock(chest, ItemBlockChest.class, Names.Blocks.CHEST);
        GameRegistry.registerBlock(exchanger, ItemBlockExchanger.class, Names.Blocks.EXCHANGER);

        GameRegistry.registerBlock(coolantCold, Names.Fluids.COOLANT_COLD);
        GameRegistry.registerBlock(coolantHot, Names.Fluids.COOLANT_HOT);
        if (!ConfigHandler.steamName.equals("steam"))
            GameRegistry.registerBlock(steam, Names.Fluids.STEAM);
    }
}