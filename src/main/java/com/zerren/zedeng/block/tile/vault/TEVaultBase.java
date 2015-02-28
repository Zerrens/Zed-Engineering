package com.zerren.zedeng.block.tile.vault;

import com.zerren.zedeng.block.tile.TileEntityZE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Zerren on 2/22/2015.
 */
public class TEVaultBase extends TileEntityZE {

    private boolean breakable = true;
    private String masterID = "";
    private int masterX, masterY, masterZ;

    public TEVaultBase() {
        super();
    }

    public boolean isBreakable() {
        if (!hasMaster()) return true;
        return breakable;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    public void setBreakable(boolean b) {
        breakable = b;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        this.markDirty();
    }

    public TEVaultController getCommandingController() {
        if (hasMaster() && getControllerID().length() > 0) {
            return (TEVaultController)worldObj.getTileEntity(masterX, masterY, masterZ);
        }
        return null;
    }

    public void setController(String id, int x, int y, int z) {
        masterID = id;
        masterX = x;
        masterY = y;
        masterZ = z;

        this.markDirty();
    }

    public int[] getMasterPos() {
        int[] pos = {masterX, masterY, masterZ};
        return pos;
    }

    public void removeController() {
        masterID = "";
        masterX = 0;
        masterY = 0;
        masterZ = 0;
    }

    public String getControllerID() {
        if (masterID.length() > 0) {
            return masterID;
        }
        return null;
    }

    public boolean hasMaster() {
        return worldObj.getTileEntity(masterX, masterY, masterZ) != null;
    }

    public void unlockAdjacent(int x, int y, int z, EntityPlayer player) {
        toggleBlock(x - 1, y, z, player);
        toggleBlock(x + 1, y, z, player);

        toggleBlock(x, y - 1, z, player);
        toggleBlock(x, y + 1, z, player);

        toggleBlock(x, y, z - 1, player);
        toggleBlock(x, y, z + 1, player);
    }

    private void toggleBlock(int x, int y, int z, EntityPlayer player) {
        //3 is closed, 4 is open
        int meta = worldObj.getBlockMetadata(x, y, z);

        switch (meta) {
            case 1:
                TEVaultBase controller = (TEVaultBase) worldObj.getTileEntity(x, y, z);
                if (controller != null && controller instanceof TEVaultController) {
                    ((TEVaultController) controller).checkMultiblock(getOwner(), player);
                }
                break;
            case 3:
                worldObj.setBlockMetadataWithNotify(x, y, z, 4, 2);
                worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 0.5F, worldObj.rand.nextFloat() * 0.25F + 0.5F);
                break;
            case 4:
                worldObj.setBlockMetadataWithNotify(x, y, z, 3, 2);
                worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 0.5F, worldObj.rand.nextFloat() * 0.25F + 0.5F);
                break;
        }

        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        breakable = tag.getBoolean("breakable");
        masterID = tag.getString("masterID");

        masterX = tag.getInteger("masterX");
        masterY = tag.getInteger("masterY");
        masterZ = tag.getInteger("masterZ");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("breakable", breakable);
        tag.setString("masterID", masterID);

        tag.setInteger("masterX", masterX);
        tag.setInteger("masterY", masterY);
        tag.setInteger("masterZ", masterZ);
    }
}