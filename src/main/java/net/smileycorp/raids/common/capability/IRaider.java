package net.smileycorp.raids.common.capability;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.raids.common.RaidsContent;

public interface IRaider {
	
	public IRaid getRaid();
	
	public boolean hasRaid();
	
	public void setRaid(IRaid raid);
	
	public NBTTagCompound writeNBT(NBTTagCompound nbtTagCompound);

	public void readNBT(NBTTagCompound nbt);
	
	public boolean isRaidActive();
	
	public boolean isLeader();
	
	public void setLeader();
	
	public class Storage implements IStorage<IRaider> {

		@Override
		public NBTBase writeNBT(Capability<IRaider> capability, IRaider instance, EnumFacing side) {
			return instance.writeNBT(new NBTTagCompound());
		}

		@Override
		public void readNBT(Capability<IRaider> capability, IRaider instance, EnumFacing side, NBTBase nbt) {
			instance.readNBT((NBTTagCompound) nbt);
		}
		
	}
	
	public static class Implementation implements IRaider {
		
		protected final EntityLiving entity;
		protected IRaid raid = null;
		protected boolean raidLeader;
		
		public Implementation() {
			entity = null;
		}

		public Implementation(EntityLiving entity) {
			this.entity=entity;
		}

		@Override
		public IRaid getRaid() {
			return raid;
		}

		@Override
		public boolean hasRaid() {
			return raid != null;
		}

		@Override
		public void setRaid(IRaid raid) {
			this.raid=raid;
		}

		@Override
		public NBTTagCompound writeNBT(NBTTagCompound nbt) {
			nbt.setBoolean("raidLeader", raidLeader);
			BlockPos pos = raid.getVillage().getCenter();
			NBTTagCompound villagePos = new NBTTagCompound();
			villagePos.setInteger("x", pos.getX());
			villagePos.setInteger("y", pos.getY());
			villagePos.setInteger("z", pos.getZ());
			nbt.setTag("villagePos", villagePos);
			return nbt;
		}

		@Override
		public void readNBT(NBTTagCompound nbt) {
			if (nbt.hasKey("raidLeader"))raidLeader = nbt.getBoolean("raidLeader");
			if (nbt.hasKey("villagePos") && entity!=null) {
				NBTTagCompound villagePos = nbt.getCompoundTag("pos");
				Village village = entity.world.villageCollection.getNearestVillage(
						new BlockPos(villagePos.getInteger("x"), villagePos.getInteger("y"), villagePos.getInteger("z")), 0);
				if (village!=null) {
					if (village.hasCapability(RaidsContent.RAID_CAPABILITY, null)) raid = village.getCapability(RaidsContent.RAID_CAPABILITY, null);
				}
			}
		}

		@Override
		public boolean isRaidActive() {
			if (hasRaid() && entity != null) return raid.isActive(entity.world);
			return false;
		}

		@Override
		public boolean isLeader() {
			return raidLeader;
		}

		@Override
		public void setLeader() {
			if (entity!=null) {
				if (entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
					entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, RaidsContent.OMINOUS_BANNER);
				}
			}
			raidLeader = true;
		}
		
	}
	
	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
		
		protected IRaider impl;
		
		public Provider(EntityLiving entity) {
			impl = new Implementation(entity);
		}

		@Override
		public boolean hasCapability(Capability<?> cap, EnumFacing facing) {
			return cap == RaidsContent.RAIDER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> cap, EnumFacing facing) {
			return hasCapability(cap, facing) ? RaidsContent.RAIDER_CAPABILITY.cast(impl) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return impl.writeNBT(new NBTTagCompound());
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			impl.readNBT(nbt);
		}
		
	}
	
}
