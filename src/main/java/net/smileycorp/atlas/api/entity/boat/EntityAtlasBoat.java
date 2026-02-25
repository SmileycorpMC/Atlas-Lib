package net.smileycorp.atlas.api.entity.boat;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityAtlasBoat extends EntityBoat {

    public static final DataParameter<String> TYPE = EntityDataManager.createKey(EntityAtlasBoat.class, DataSerializers.STRING);

    public EntityAtlasBoat(World world) {
        super(world);
    }

    public EntityAtlasBoat(World world, BoatRegistry.Type type, double x, double y, double z, float yaw) {
        this(world);
        setPositionAndRotation(x, y, z, yaw, 0);
        dataManager.set(TYPE, type.getRegistryName().toString());
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(TYPE, "");
    }

    public BoatRegistry.Type getType() {
        return BoatRegistry.INSTANCE.get(new ResourceLocation(dataManager.get(TYPE)));
    }

    @Override
    public String getName() {
        if (hasCustomName()) getCustomNameTag();
        BoatRegistry.Type type = getType();
        return type == null ? "entity.atlaslib.Boat.name" : type.getEntityName();
    }

    //not actually needed because we overwrite everything that calls this
    @Override
    public Item getItemBoat() {
        BoatRegistry.Type type = getType();
        return type == null ? Items.BOAT : getType().getBoat().getItem();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        BoatRegistry.Type type = getType();
        return type == null ? ItemStack.EMPTY : getType().getBoat();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source) || world.isRemote || isDead) return false;
        if (source instanceof EntityDamageSourceIndirect && source.getTrueSource() != null && isPassenger(source.getTrueSource())) return false;
        setForwardDirection(-getForwardDirection());
        setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
        this.markVelocityChanged();
        boolean creative = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)source.getTrueSource()).capabilities.isCreativeMode;
        if (!creative && getDamageTaken() <= 40) return true;
        setDead();
        BoatRegistry.Type type = getType();
        if (!creative && type != null && world.getGameRules().getBoolean("doEntityDrops")) entityDropItem(type.getBoat(), 0);
        return true;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Type")) dataManager.set(TYPE, nbt.getString("Type"));
        nbt.removeTag("Type");
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setString("Type", dataManager.get(TYPE));
    }

    @Override
    public void setBoatType(EntityBoat.Type boatType) {}

    @Override
    public EntityBoat.Type getBoatType() {
        return Type.OAK;
    }

}
