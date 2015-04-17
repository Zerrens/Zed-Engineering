package com.zerren.zedeng.block.tile;

import com.zerren.zedeng.handler.PacketHandler;
import com.zerren.zedeng.handler.network.client.tile.MessageTileMultiblock;
import com.zerren.zedeng.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;

import java.util.UUID;

/**
 * Created by Zerren on 3/11/2015.
 */
public class TEMultiBlockBase extends TileEntityZE {
    /**
     * The UUID of this tile entitie's master (if it has one)
     */
    protected UUID masterID;
    protected int masterX, masterY, masterZ;
    protected boolean isMaster;
    public boolean hasMaster;
    /**
     * The UUID of this tile entity that it gives to its slaves
     */
    protected UUID controllerID;

    public TEMultiBlockBase() {
        super();
        masterID = null;
        isMaster = false;
        controllerID = null;
        hasMaster = false;
    }

    /**
     * If this tile entity has slave tile entities
     * @return
     */
    public boolean isMaster() {
        return isMaster;
    }

    /**
     * Set this tile entity as a maser that has slave tile entities?
     * @param b isMaster
     */
    public void setAsMaster(boolean b) {
        isMaster = b;
    }

    /**
     * Returns the TEMultiBlockBase that controls this tile entity or null if there is no master
     * @return
     */
    public TEMultiBlockBase getCommandingController() {
        if (hasValidMaster() && getMasterUUID() != null) {
            return (TEMultiBlockBase)worldObj.getTileEntity(masterX, masterY, masterZ);
        }
        return null;
    }

    /**
     * Break the multiblock that this tile entity is a part of (gets the commander and sets its master status as false)
     */
    public void invalidateMultiblock() { }

    /**
     * Returns the UUID of this tile entities' master
     * @return UUID
     */
    public UUID getMasterUUID() {
        if (masterID != null) {
            return masterID;
        }
        return null;
    }

    public void setControllerUUID(UUID id) {
        controllerID = id;
    }

    /**
     * If this tile entity is a part of a valid multiblock (master's UUID isn't null, master tile isn't null, and the master tile's UUID equals this tile's UUID)
     * @return
     */
    public boolean hasValidMaster() {
        TEMultiBlockBase masterTile = (TEMultiBlockBase)worldObj.getTileEntity(masterX, masterY, masterZ);

        //has a commanding UUID, the commander isn't null, and the commander's UUID matches this tile's UUID
        return getMasterUUID() != null && masterTile != null && masterTile.getControllerUUID().compareTo(getMasterUUID()) == 0;
    }

    /**
     * Sets this tile entity to belong to the master tile
     * @param id UUID of the master
     * @param x
     * @param y
     * @param z
     */
    public void setController(UUID id, int x, int y, int z) {
        masterID = id;
        masterX = x;
        masterY = y;
        masterZ = z;

        this.markDirty();
    }

    /**
     * Frees this tile entity from a master's control, removing it from the multiblock
     */
    public void removeController() {
        masterID = null;
        masterX = 0;
        masterY = 0;
        masterZ = 0;
        hasMaster = false;

        this.markDirty();
    }

    /**
     * Returns the array of the x, y, and z of the master's location
     * @return
     */
    public int[] getMasterPos() {
        int[] pos = {masterX, masterY, masterZ};
        return pos;
    }

    /**
     * If this tile entity has a master UUID: if true, this block is a master
     * @return
     */
    public boolean hasControllerID() {
        return getControllerUUID() != null;
    }

    /**
     * Returns the master UUID
     * @return UUID of master
     */
    public UUID getControllerUUID() {
        return this.controllerID;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        masterX = tag.getInteger("masterX");
        masterY = tag.getInteger("masterY");
        masterZ = tag.getInteger("masterZ");
        isMaster = tag.getBoolean("isMaster");

        if (tag.hasKey(Names.NBT.MASTER_UUID_MOST_SIG) && tag.hasKey(Names.NBT.MASTER_UUID_LEAST_SIG)) {
            this.masterID = new UUID(tag.getLong(Names.NBT.MASTER_UUID_MOST_SIG), tag.getLong(Names.NBT.MASTER_UUID_LEAST_SIG));
        }

        if (tag.hasKey(Names.NBT.CONTROLLER_UUID_MOST_SIG) && tag.hasKey(Names.NBT.CONTROLLER_UUID_LEAST_SIG)) {
            this.controllerID = new UUID(tag.getLong(Names.NBT.CONTROLLER_UUID_MOST_SIG), tag.getLong(Names.NBT.CONTROLLER_UUID_LEAST_SIG));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("masterX", masterX);
        tag.setInteger("masterY", masterY);
        tag.setInteger("masterZ", masterZ);
        tag.setBoolean("isMaster", isMaster);

        if (this.hasValidMaster()) {
            tag.setLong(Names.NBT.MASTER_UUID_MOST_SIG, masterID.getMostSignificantBits());
            tag.setLong(Names.NBT.MASTER_UUID_LEAST_SIG, masterID.getLeastSignificantBits());
        }

        if (this.hasControllerID()) {
            tag.setLong(Names.NBT.CONTROLLER_UUID_MOST_SIG, controllerID.getMostSignificantBits());
            tag.setLong(Names.NBT.CONTROLLER_UUID_LEAST_SIG, controllerID.getLeastSignificantBits());
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.netHandler.getPacketFrom(new MessageTileMultiblock(this, isMaster, hasValidMaster()));
    }
}
