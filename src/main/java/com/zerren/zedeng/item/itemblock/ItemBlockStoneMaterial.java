package com.zerren.zedeng.item.itemblock;

import zedeng.api.block.ZedBlocks;
import com.zerren.zedeng.reference.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

/**
 * Created by Zerren on 2/19/2015.
 */
public class ItemBlockStoneMaterial extends ItemMultiTexture {
    public ItemBlockStoneMaterial(Block block) {
        super(ZedBlocks.ores, ZedBlocks.ores, Names.Blocks.ORE_SUBTYPES);
    }
}