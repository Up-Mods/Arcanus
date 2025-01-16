package dev.cammiescorner.arcanus.fabric.common.blocks.entities;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusBlockEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Locale;
import java.util.UUID;

public class MagicDoorBlockEntity extends BlockEntity {
	private UUID ownerId = Util.NIL_UUID;
	private String password = "please";

	public MagicDoorBlockEntity(BlockPos pos, BlockState state) {
		super(ArcanusBlockEntities.MAGIC_DOOR.get(), pos, state);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);

		ownerId = nbt.getUUID("OwnerId");
		password = nbt.getString("Password");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);

		nbt.putUUID("OwnerId", ownerId);
		nbt.putString("Password", password);
	}

	public LivingEntity getOwner() {
		if(level instanceof ServerLevel server && server.getEntity(ownerId) instanceof LivingEntity livingEntity)
			return livingEntity;

		return null;
	}

	public void setOwner(LivingEntity owner) {
		ownerId = owner.getUUID();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password.toLowerCase(Locale.ROOT);
	}
}
