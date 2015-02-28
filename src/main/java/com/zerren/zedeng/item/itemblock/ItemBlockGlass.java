package com.zerren.zedeng.item.itemblock;

import com.zerren.zedeng.core.ModBlocks;
import com.zerren.zedeng.reference.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

/**
 * Created by Zerren on 2/19/2015.
 */
public class ItemBlockGlass extends ItemMultiTexture {
    public ItemBlockGlass(Block block) {
        super(ModBlocks.glass, ModBlocks.glass, Names.Blocks.GLASS_SUBTYPES);
    }
}