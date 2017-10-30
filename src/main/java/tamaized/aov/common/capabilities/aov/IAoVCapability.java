package tamaized.aov.common.capabilities.aov;

import tamaized.aov.AoV;
import tamaized.aov.common.capabilities.aov.AoVCapabilityHandler.DecayWrapper;
import tamaized.aov.common.core.abilities.Ability;
import tamaized.aov.common.core.abilities.AbilityBase;
import tamaized.aov.common.core.abilities.Aura;
import tamaized.aov.common.core.skills.AoVSkill;
import io.netty.buffer.ByteBufInputStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IAoVCapability {

	ResourceLocation ID = new ResourceLocation(AoV.modid, "AoVCapabilityHandler");

	void markDirty();

	void reset(boolean b);

	void update(EntityPlayer player);

	void resetCharges();

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

	void setSlot(Ability ability, int slot);

	Ability getSlot(int slot);

	/**
	 * Returns -1 if slots dont contain ability
	 */
	int getSlotFromAbility(Ability ability);

	default Ability getAbilityFromSlots(Ability ability) {
		return getSlot(getSlotFromAbility(ability));
	}

	int getCurrentSlot();

	void setCurrentSlot(int index);

	boolean slotsContain(Ability ability);

	void addToNearestSlot(Ability ability);

	void removeSlot(int slot);

	void clearAllSlots();

	void setDecayMap(Map<AbilityBase, DecayWrapper> map);

	Map<AbilityBase, DecayWrapper> getDecayMap();

	void copyFrom(IAoVCapability cap);

	Ability[] getSlots();
}
