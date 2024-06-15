package net.smileycorp.atlas.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.smileycorp.atlas.api.BoatRegistry;

public class AtlasBoat extends Boat {

	private static final EntityDataAccessor<String> DATA_ID_TYPE = SynchedEntityData.defineId(AtlasBoat.class, EntityDataSerializers.STRING);

	public AtlasBoat(EntityType<? extends AtlasBoat> type, Level level) {
		super(type, level);
	}

	public AtlasBoat(Level level, double x, double y, double z) {
		this(BoatRegistry.BOAT_ENTITY.get(), level);
		this.setPos(x, y, z);
		xo = x;
		yo = y;
		zo = z;
	}

	public void setType(BoatRegistry.Type type) {
		if (type == null) entityData.set(DATA_ID_TYPE, "none");
		else entityData.set(DATA_ID_TYPE, type.getRegistryName().toString());
	}

	public BoatRegistry.Type getAtlasType() {
		return BoatRegistry.INSTANCE.get(ResourceLocation.parse(entityData.get(DATA_ID_TYPE)));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		if (nbt.contains("Type", 8)) {
			setType(BoatRegistry.INSTANCE.get(ResourceLocation.parse(nbt.getString("Type"))));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		nbt.putString("Type", entityData.get(DATA_ID_TYPE));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_ID_TYPE, "none");
	}

	@Override
	protected void checkFallDamage(double p_38307_, boolean p_38308_, BlockState p_38309_, BlockPos p_38310_) {
		lastYd = this.getDeltaMovement().y;
		if (!this.isPassenger()) {
			if (p_38308_) {
				if (fallDistance > 3.0F) {
					if (status != Boat.Status.ON_LAND) {
						this.resetFallDistance();
						return;
					}

					this.causeFallDamage(fallDistance, 1.0F, this.damageSources().fall());
					if (!level().isClientSide && !this.isRemoved()) {
						this.kill();
						if (level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
							for(int i = 0; i < 3; ++i) {
								this.spawnAtLocation(getDropItem());
							}

							for(int j = 0; j < 2; ++j) {
								this.spawnAtLocation(Items.STICK);
							}
						}
					}
				}

				this.resetFallDistance();
			} else if (!level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && p_38307_ < 0.0D) {
				fallDistance -= (float)p_38307_;
			}

		}
	}

	@Override
	public Item getDropItem() {
		BoatRegistry.Type type = getAtlasType();
		return type == null ? Items.OAK_BOAT : BoatRegistry.INSTANCE.get(ResourceLocation.parse(entityData.get(DATA_ID_TYPE))).getBoat();
	}

	//override vanilla types
	@Override
	public void setVariant(Type type) {}

	@Override
	public Type getVariant() {
		return Type.OAK;
	}

}
