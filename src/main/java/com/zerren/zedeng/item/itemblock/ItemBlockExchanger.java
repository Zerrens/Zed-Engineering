package com.zerren.zedeng.item.itemblock;

import com.zerren.zedeng.api.block.ZedBlocks;
import com.zerren.zedeng.reference.MultiblockCost;
import com.zerren.zedeng.reference.Names;
import com.zerren.zedeng.utility.TooltipHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Zerren on 3/6/2015.
 */
public class ItemBlockExchanger extends ItemMultiTexture {
    public ItemBlockExchanger(Block block) {
        super(ZedBlocks.plumbing, ZedBlocks.plumbing, Names.Blocks.PLUMBING_SUBTYPES);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (stack.getItemDamage() == 0) {
            TooltipHelper.addSizeInfo(list, "5x1x1");
            TooltipHelper.addMaterialCostInfo(list, MultiblockCost.LIQUID_HEAT_EXCHANGER);
        }
    }
}