package tamaized.aov.common.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamaized.aov.common.capabilities.CapabilityList;
import tamaized.aov.common.capabilities.aov.IAoVCapability;
import tamaized.aov.common.core.abilities.Abilities;
import tamaized.aov.common.helper.ParticleHelper;
import tamaized.aov.common.helper.ParticleHelper.MeshType;
import tamaized.aov.proxy.CommonProxy;
import tamaized.aov.registry.AoVDamageSource;

import javax.annotation.Nonnull;

public class ProjectileFlameStrike extends Entity implements IProjectile, IEntityAdditionalSpawnData {

	private Entity attacker;
	private float damage = 2;

	public ProjectileFlameStrike(World worldIn) {
		super(worldIn);
		setFire(100);
		motionX = 0;
		motionY = -0.8;
		motionZ = 0;
		setRotation(0, 90);
	}

	public ProjectileFlameStrike(World world, Entity attacker) {
		this(world);
		this.attacker = attacker;
	}

	public ProjectileFlameStrike(World world, Entity attacker, float dmg) {
		this(world, attacker);
		damage = dmg;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		return 0xF000F0;
	}

	public void setDamage(float d) {
		damage = d;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeDouble(posX);
		buffer.writeDouble(posY);
		buffer.writeDouble(posZ);
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		setPosition(data.readDouble(), data.readDouble(), data.readDouble());

	}

	@Override
	public void onUpdate() {
		onEntityUpdate();
		Vec3d vec3d1 = new Vec3d(posX, posY, posZ);
		Vec3d vec3d = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
		RayTraceResult ray = world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
		if (ray != null && ray.typeOfHit != null) {
			switch (ray.typeOfHit) {
				case BLOCK:
				case ENTITY:
					explode();
					return;
				case MISS:
				default:
					break;
			}
		}
		posY += motionY;
	}

	private void explode() {
		setDead();
		for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPosition().add(-10, -1, -10), getPosition().add(10, 5, 10)))) {
			if (attacker != null) {
				IAoVCapability cap = attacker.hasCapability(CapabilityList.AOV, null) ? attacker.getCapability(CapabilityList.AOV, null) : null;
				if (entity == attacker || (cap != null && !IAoVCapability.selectiveTarget(cap, entity)))
					continue;
				if (cap != null)
					cap.addExp(attacker, 20, Abilities.flameStrike);
			}
			entity.setFire(15);
			entity.attackEntityFrom(AoVDamageSource.createEntityDamageSource(DamageSource.IN_FIRE, attacker), damage);
		}
		for (int i = 0; i < 2; i++)
			ParticleHelper.spawnParticleMesh(MeshType.BURST, CommonProxy.ParticleType.Fluff, world, new Vec3d(posX, posY, posZ), 10, 0xFF4801FF);
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

	}

}
