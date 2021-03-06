package tamaized.aov.common.capabilities.aov;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import tamaized.aov.AoV;
import tamaized.aov.common.capabilities.aov.AoVCapabilityHandler.DecayWrapper;
import tamaized.aov.common.core.abilities.Ability;
import tamaized.aov.common.core.abilities.AbilityBase;
import tamaized.aov.common.core.abilities.Aura;
import tamaized.aov.common.core.skills.AoVSkill;

import java.util.List;
import java.util.Map;

public interface IAoVCapability {

	ResourceLocation ID = new ResourceLocation(AoV.modid, "AoVCapabilityHandler");

	/**
	 * @return true if friendly
	 */
	static boolean selectiveTarget(IAoVCapability cap, EntityLivingBase entity) {
		return (!cap.hasSelectiveFocus() || (entity.getTeam() == null && (entity instanceof EntityPlayer || (entity instanceof IEntityOwnable && ((IEntityOwnable) entity).getOwner() == entity))) || (entity.isOnSameTeam(entity) || (entity instanceof IEntityOwnable && ((IEntityOwnable) entity).getOwner() == entity)));
	}

	void markDirty();

	void setLoaded();

	void reset(boolean b);

	void update(EntityPlayer player);

	void setCooldown(AbilityBase ability, int cd);

	int getCooldown(AbilityBase ability);

	void resetCharges(EntityPlayer player);

	void restoreCharges(int amount);

	List<Ability> getAbilities();

	boolean canUseAbility(Ability ability);

	void addAbility(Ability ability);

	void removeAbility(Ability ability);

	void addAura(Aura aura);

	void addExp(Entity player, int amount, AbilityBase spell);

	void addObtainedSkill(AoVSkill skill);

	boolean hasSkill(AoVSkill skill);

	boolean hasCoreSkill();

	AoVSkill getCoreSkill();

	List<AoVSkill> getObtainedSkills();

	void removeSkill(AoVSkill skill);

	int getLevel();

	int getMaxLevel();

	void setMaxLevel(int amount);

	int getExp();

	void setExp(int amount);

	int getExpNeededToLevel();

	int getSkillPoints();

	void setSkillPoints(int amount);

	int getSpentSkillPoints();

	float getSpellPower();

	int getExtraCharges();

	int getDodge();

	default int getDodgeForRand() {
		return Math.round(100F / ((float) getDodge()));
	}

	int getDoubleStrike();

	default int getDoubleStrikeForRand() {
		return Math.round(100F / ((float) getDoubleStrike()));
	}

	boolean hasSelectiveFocus();

	void toggleInvokeMass(boolean b);

	void toggleInvokeMass();

	boolean getInvokeMass();

	boolean hasInvokeMass();

	void cast(int slotLoc);

	void setSlot(Ability ability, int slot, boolean force);

	Ability getSlot(int slot);

	/**
	 * Returns -1 if slots dont contain ability
	 */
	int getSlotFromAbility(AbilityBase ability);

	default Ability getAbilityFromSlots(AbilityBase ability) {
		return getSlot(getSlotFromAbility(ability));
	}

	int getCurrentSlot();

	void setCurrentSlot(int index);

	boolean slotsContain(AbilityBase ability);

	void addToNearestSlot(AbilityBase ability);

	void removeSlot(int slot);

	void clearAllSlots();

	Map<AbilityBase, DecayWrapper> getDecayMap();

	void setDecayMap(Map<AbilityBase, DecayWrapper> map);

	void copyFrom(IAoVCapability cap);

	Ability[] getSlots();
}
