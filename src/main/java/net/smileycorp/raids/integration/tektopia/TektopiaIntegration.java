package net.smileycorp.raids.integration.tektopia;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.VillageManager;
import net.tangotek.tektopia.entities.EntityVillagerTek;

public class TektopiaIntegration {
    
    public static boolean isVillage(World world, BlockPos center) {
        return VillageManager.get(world).getVillageAt(center) != null;
    }
    
    public static BlockPos getNewCenter(WorldServer world, BlockPos center) {
        Village village = VillageManager.get(world).getVillageAt(center);
        return  village == null ? null : village.getCenter();
    }
    
    public static void addTargetTask(EntityCreature entity) {
        entity.tasks.addTask(3, new EntityAINearestAttackableTarget(entity, EntityVillagerTek.class, true));
    }
    
}
