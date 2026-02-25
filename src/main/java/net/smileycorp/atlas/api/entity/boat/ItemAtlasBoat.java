package net.smileycorp.atlas.api.entity.boat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.wood.WoodEnum;
import net.smileycorp.atlas.api.item.IMetaItem;

import java.util.Collection;
import java.util.List;

public class ItemAtlasBoat<T extends Enum<T> & WoodEnum> extends Item implements IMetaItem {

    protected final String modid;
    protected final List<T> types;

    public ItemAtlasBoat(String modid, String name, CreativeTabs tab, List<T> types) {
        super();
        this.modid = modid;
        this.types = types;
        setMaxStackSize(1);
        setCreativeTab(tab);
        hasSubtypes = types.size() > 1;
        setRegistryName(new ResourceLocation(modid, name));
        setUnlocalizedName(modid + "." + name);
        for (int i = 0; i < types.size(); i++) BoatRegistry.INSTANCE.register(new ResourceLocation(modid, types.get(i).getName()),
                new ItemStack(this, 1, i));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, this::dispense);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Vec3d pos = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d look = player.getLookVec().scale(5);
        RayTraceResult result = world.rayTraceBlocks(pos, pos.addVector(look.x, look.y, look.z));
        if (result == null) return new ActionResult<>(EnumActionResult.PASS, stack);
        if (result.typeOfHit != RayTraceResult.Type.BLOCK) return new ActionResult<>(EnumActionResult.PASS, stack);
        Vec3d hit = result.hitVec;
        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(look.x, look.y, look.z).grow(1))) {
            if (!entity.canBeCollidedWith()) continue;
            if (entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize()).contains(hit)) return new ActionResult<>(EnumActionResult.PASS, stack);
        }
        IBlockState state = world.getBlockState(result.getBlockPos());
        if (!spawnBoat(stack, world, hit.x, hit.y + 1 - state.getBoundingBox(world, result.getBlockPos()).maxY, hit.z, player.rotationYaw))
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        if (!player.isCreative()) stack.shrink(1);
        player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public ItemStack dispense(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        EnumFacing facing = world.getBlockState(source.getBlockPos()).getValue(BlockDispenser.FACING);
        BlockPos pos = source.getBlockPos().offset(facing);
        Material material = world.getBlockState(pos).getMaterial();
        if (material != Material.WATER &!(material == Material.AIR &&
                world.getBlockState(pos.down()).getMaterial() == Material.WATER)) IBehaviorDispenseItem.DEFAULT_BEHAVIOR.dispense(source, stack);
        //width of a boat is 1.375 so 0.6875 is half of that
        double x = source.getX() + facing.getFrontOffsetX() * 0.6875;
        double y = source.getY() + facing.getFrontOffsetY() * 1.125;
        double z = source.getZ() + facing.getFrontOffsetZ() * 0.6875;
        if (material == Material.WATER) y++;
        if (spawnBoat(stack, world, x, y, z, facing.getHorizontalAngle())) stack.shrink(1);
        return stack;
    }

    public boolean spawnBoat(ItemStack stack, World world, double x, double y, double z, float yaw) {
        BoatRegistry.Type type = BoatRegistry.INSTANCE.get(new ResourceLocation(modid, types.get(stack.getMetadata() % types.size()).getName()));
        if (type == null) return false;
        EntityAtlasBoat boat = new EntityAtlasBoat(world, type, x, y, z, yaw);
        if (!world.getCollisionBoxes(boat, boat.getEntityBoundingBox().grow(-0.1D)).isEmpty()) return false;
        return world.isRemote || world.spawnEntity(boat);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < types.size(); i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + modid + "." + byMeta(stack.getMetadata());
    }

    @Override
    public int getMaxMeta() {
        return types.size();
    }

    @Override
    public String byMeta(int meta) {
        return types.get(meta % types.size()).getName() + "_boat";
    }

}
