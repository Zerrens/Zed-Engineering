package com.zerren.zedeng.block.tile.plumbing;

import com.zerren.zedeng.block.tile.TileEntityZE;
import com.zerren.zedeng.handler.ConfigHandler;
import com.zerren.zedeng.handler.PacketHandler;
import com.zerren.zedeng.handler.network.client.tile.MessageTileGasTank;
import com.zerren.zedeng.handler.network.client.tile.MessageTileZE;
import com.zerren.zedeng.reference.Names;
import com.zerren.zedeng.utility.NetworkUtility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by Zerren on 4/13/2015.
 */
public class TEGasTank extends TileEntityZE implements IFluidHandler {
    public final FluidTank tank;
    private boolean tankDirty;
    private int updateCounter;
    public int fluidAmount;

    public TEGasTank() {
        super();
        fluidAmount = 0;
        updateCounter = 0;
        tankDirty = false;
        tank =  new FluidTank(ConfigHandler.gasTankVolume * 1000);
    }

    @Override
    public void updateEntity() {
        updateCounter++;

        if (updateCounter >= 60) {
            if (tankDirty && tank.getFluid() != null && tank.getFluidAmount() > 0) {
                fluidAmount = tank.getFluidAmount();
                PacketHandler.netHandler.sendToAllAround(new MessageTileGasTank(this, fluidAmount), NetworkUtility.makeTargetPoint(this));
                tankDirty = false;
                this.markDirty();
            }

            updateCounter = 0;
        }
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int used = 0;
        FluidStack filling = resource.copy();

        if (canFill(from, resource.getFluid())) {
            used += tank.fill(filling, doFill);
            filling.amount -= used;
        }
        tankDirty = true;
        return used;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        //nothing to drain
        if (resource == null) return null;
        //tank doesn't contain what the drainer wants
        if (!resource.isFluidEqual(tank.getFluid())) return null;

        if (canDrain(from, resource.getFluid())) {
            tankDirty = true;
            return drain(from, resource.amount, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (from == ForgeDirection.UP || from == ForgeDirection.DOWN) {
            tankDirty = true;
            return tank.drain(maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return (from == ForgeDirection.DOWN || from == ForgeDirection.UP) && (tank.getFluid() == null || tank.getFluid().getFluid() == fluid) && fluid.isGaseous();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return (from == ForgeDirection.DOWN || from == ForgeDirection.UP) && tank.getFluid() != null && tank.getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{this.tank.getInfo()};
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.tank.readFromNBT(tag.getCompoundTag(Names.NBT.TANK));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag(Names.NBT.TANK, tank.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.netHandler.getPacketFrom(new MessageTileGasTank(this, tank.getFluidAmount()));
    }
}
