package net.smileycorp.raids.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.raids.client.particle.ParticleRaidOmen;
import net.smileycorp.raids.client.particle.ParticleVibration;
import net.smileycorp.raids.common.Constants;
import net.smileycorp.raids.common.RaidsSoundEvents;
import net.smileycorp.raids.common.util.EnumRaidsParticle;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientHandler {
	
	public static final ResourceLocation STRIKETHROUGH_TEXTURE = Constants.loc("textures/gui/villager_strikethrough.png");
	
	public static void playRaidSound(BlockPos pos) {
		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.world;
		EntityPlayer player = mc.player;
		double dx = pos.getX() + 0.5f - player.posX;
		double dz = pos.getZ() + 0.5f  - player.posZ;
		double angle = Math.atan2(dz, dx);
		Vec3d dir =  new Vec3d(Math.cos(angle), 0, Math.sin(angle));
		BlockPos soundPos = new BlockPos(player.posX + (13 * dir.x), player.posY, player.posZ + (13 * dir.z));
		float pitch = 1 + ((world.rand.nextInt(6) - 3) / 10);
		world.playSound(player, soundPos, RaidsSoundEvents.RAID_HORN, SoundCategory.HOSTILE, 0.3f, pitch);
	}
	
	public static void spawnParticle(EnumRaidsParticle type, double x, double y, double z, Double... data) {
		Minecraft mc = Minecraft.getMinecraft();
		switch (type) {
			case RAID_OMEN:
				mc.effectRenderer.addEffect(new ParticleRaidOmen(mc.world, x, y, z, (int)(double)data[0]));
				break;
			case VIBRATION:
				mc.effectRenderer.addEffect(new ParticleVibration(mc.world, x, y, z, data[0], data[1], data[2]));
				break;
		}
	}
	
}
