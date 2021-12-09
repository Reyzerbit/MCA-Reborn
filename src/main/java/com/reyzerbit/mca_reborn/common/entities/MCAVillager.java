package com.reyzerbit.mca_reborn.common.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.reyzerbit.mca_reborn.api.API;
import com.reyzerbit.mca_reborn.api.types.APIButton;
import com.reyzerbit.mca_reborn.common.Constants;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.data.MCAInventory;
import com.reyzerbit.mca_reborn.common.data.ParentData;
import com.reyzerbit.mca_reborn.common.data.PlayerHistory;
import com.reyzerbit.mca_reborn.common.enums.EnumAgeState;
import com.reyzerbit.mca_reborn.common.enums.EnumChore;
import com.reyzerbit.mca_reborn.common.enums.EnumConstraint;
import com.reyzerbit.mca_reborn.common.enums.EnumGender;
import com.reyzerbit.mca_reborn.common.enums.EnumMarriageState;
import com.reyzerbit.mca_reborn.common.enums.EnumMoveState;
import com.reyzerbit.mca_reborn.common.init.EntityInit;
import com.reyzerbit.mca_reborn.common.init.VillagerInit;
import com.reyzerbit.mca_reborn.common.util.MCAConfig;
import com.reyzerbit.mca_reborn.common.util.ResourceLocationCache;
import com.reyzerbit.mca_reborn.network.MCAMessages;
import com.reyzerbit.mca_reborn.network.Network;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;

public class MCAVillager extends VillagerEntity {
	
	public static final String VANILLA_CAREER_ID_FIELD_INDEX = "13";
    
    public static final DataParameter<String> VILLAGER_NAME = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.STRING);
    public static final DataParameter<String> TEXTURE = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.STRING);
    public static final DataParameter<Integer> GENDER = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.INT);
    public static final DataParameter<Float> GIRTH = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> TALLNESS = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.FLOAT);
    public static final DataParameter<CompoundNBT> PLAYER_HISTORY_MAP = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.COMPOUND_TAG);
    public static final DataParameter<Integer> MOVE_STATE = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.INT);
    public static final DataParameter<String> SPOUSE_NAME = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.STRING);
    public static final DataParameter<Optional<UUID>> SPOUSE_UUID = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.OPTIONAL_UUID);
    public static final DataParameter<Integer> MARRIAGE_STATE = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.INT);
    public static final DataParameter<Boolean> IS_PROCREATING = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<CompoundNBT> PARENTS = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.COMPOUND_TAG);
    public static final DataParameter<Boolean> IS_INFECTED = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> AGE_STATE = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> ACTIVE_CHORE = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.INT);
    public static final DataParameter<Boolean> IS_SWINGING = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> HAS_BABY = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> BABY_IS_MALE = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> BABY_AGE = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.INT);
    public static final DataParameter<Optional<UUID>> CHORE_ASSIGNING_PLAYER = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.OPTIONAL_UUID);
    public static final DataParameter<BlockPos> BED_POS = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<BlockPos> WORKPLACE_POS = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<BlockPos> HANGOUT_POS = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<Boolean> SLEEPING = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.BOOLEAN);

    public int babyAge = 0;
    public UUID playerToFollowUUID = Constants.ZERO_UUID;
    private BlockPos home = BlockPos.ZERO;
    public final MCAInventory inventory;
    private int startingAge = 0;

    private static final Predicate<LivingEntity> BANDIT_TARGET_SELECTOR = (v) -> ((VillagerEntity)v).getVillagerData().getProfession() != VillagerInit.BANDIT && ((VillagerEntity)v).getVillagerData().getProfession() != VillagerInit.CHILD;
    private static final Predicate<LivingEntity> GUARD_TARGET_SELECTOR = (v) -> ((VillagerEntity)v).getVillagerData().getProfession() == VillagerInit.BANDIT;
    
    public MCAVillager() {
    	
        this(EntityInit.MCA_VILLAGER.get(), null);
        
    }
    
    // This is used for a default constructor, as the EntityInit class needs one, but cannot differentiate between many constructors.
    public static MCAVillager create(EntityType<? extends VillagerEntity> type, World world) {
    	
    	return new MCAVillager(type, world);
    	
    }
    
	public MCAVillager(EntityType<? extends VillagerEntity> entityType, World world) {
		
		this(entityType, world, Optional.empty());
		
	}

	public MCAVillager(EntityType<? extends VillagerEntity> entityType, World world, Optional<EnumGender> gender) {
    	
        this(entityType, world, Optional.empty(), gender);
        
    }

	public MCAVillager(EntityType<? extends VillagerEntity> entityType, World world, Optional<VillagerProfession> profession, Optional<EnumGender> gender) {
		
		super(entityType, world);
		
        inventory = new MCAInventory(this);
        
        CompoundNBT parents = new CompoundNBT();
        parents.putUUID("parent1UUID", Constants.ZERO_UUID);
        parents.putUUID("parent2UUID", Constants.ZERO_UUID);
        parents.putString("parent1Name", "");
        parents.putString("parent2Name", "");
		
        entityData.define(VILLAGER_NAME, "");
        entityData.define(TEXTURE, "");
        entityData.define(GENDER, EnumGender.MALE.getId());
        entityData.define(GIRTH, 0.0F);
        entityData.define(TALLNESS, 0.0F);
        entityData.define(PLAYER_HISTORY_MAP, new CompoundNBT());
        entityData.define(MOVE_STATE, EnumMoveState.MOVE.getId());
        entityData.define(SPOUSE_NAME, "");
        entityData.define(SPOUSE_UUID, Optional.of(Constants.ZERO_UUID));
        entityData.define(MARRIAGE_STATE, EnumMarriageState.NOT_MARRIED.getId());
        entityData.define(IS_PROCREATING, false);
        entityData.define(PARENTS, parents);
        entityData.define(IS_INFECTED, false);
        entityData.define(AGE_STATE, EnumAgeState.ADULT.getId());
        entityData.define(ACTIVE_CHORE, EnumChore.NONE.getId());
        entityData.define(IS_SWINGING, false);
        entityData.define(HAS_BABY, false);
        entityData.define(BABY_IS_MALE, false);
        entityData.define(BABY_AGE, 0);
        entityData.define(CHORE_ASSIGNING_PLAYER, Optional.of(Constants.ZERO_UUID));
        entityData.define(BED_POS, BlockPos.ZERO);
        entityData.define(WORKPLACE_POS, BlockPos.ZERO);
        entityData.define(HANGOUT_POS, BlockPos.ZERO);
        entityData.define(SLEEPING, false);
        
        setSilent(false);
    	
        set(GENDER, gender.isPresent() ? gender.get().getId() : EnumGender.getRandom().getId());
        set(VILLAGER_NAME, API.getRandomName(EnumGender.byId(get(GENDER))));
        set(TEXTURE, API.getRandomSkin(this));
        
        getVillagerData().setProfession(profession.isPresent() ? profession.get() : VillagerInit.randomProfession());

        // TODO See if this is really needed...
        //setVanillaCareer(getVillagerData().getProfession().getRandomCareer(world.str));

        applySpecialAI();
        
    }

    @Override
    protected SoundEvent getAmbientSound() {
    	
        return null;
        
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
    	
        return SoundEvents.GENERIC_HURT;
        
    }

    @Override
    protected SoundEvent getDeathSound() {
    	
        return get(IS_INFECTED) ? SoundEvents.ZOMBIE_DEATH : null;
        
    }

    public void setStartingAge(int value) {
    	
        this.startingAge = value;
        setAge(value);
        
    }
	
	public static AttributeModifierMap.MutableAttribute createVillagerAttributes() {

		return MobEntity.createMobAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.8D)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.MAX_HEALTH, MCAConfig.villagerMaxHealth.get());

	}
	
	public boolean isMarried() {
		
		return !get(SPOUSE_UUID).orElse(Constants.ZERO_UUID).equals(Constants.ZERO_UUID);
		
	}
	
    public void forcePositionAsHome() {
    	
        this.home = this.blockPosition();
        
    }

	public boolean isMarriedTo(UUID uuid) {
		
		return get(SPOUSE_UUID).orElse(Constants.ZERO_UUID).equals(uuid);
		
	}
	
	public <T> T get(DataParameter<T> key) {
    	
        return entityData.get(key);
        
    }

    public <T> void set(DataParameter<T> key, T value) {
    	
        entityData.set(key, value);
        
    }

    public void setVanillaCareer(String careerId) {
    	
        ObfuscationReflectionHelper.setPrivateValue(VillagerEntity.class, this, careerId, VANILLA_CAREER_ID_FIELD_INDEX);
        
    }

    private void handleInteraction(ServerPlayerEntity player, PlayerHistory history, APIButton button) {
    	
        float successChance = 0.85F;
        int heartsBoost = button.getConstraints().contains(EnumConstraint.ADULTS) ? 15 : 5;

        String interactionName = button.getIdentifier().replace("gui.button.", "");

        successChance -= button.getConstraints().contains(EnumConstraint.ADULTS) ? 0.25F : 0.0F;
        successChance += (history.getHearts() / 10.0D) * 0.025F;

        if (MCAConfig.enableDiminishingReturns.get()) successChance -= history.getInteractionFatigue() * 0.05F;

        boolean succeeded = random.nextFloat() < successChance;
        if (MCAConfig.enableDiminishingReturns.get() && succeeded)
            heartsBoost -= history.getInteractionFatigue() * 0.05F;

        history.changeInteractionFatigue(1);
        history.changeHearts(succeeded ? heartsBoost : (heartsBoost * -1));
        String responseId = String.format("%s.%s", interactionName, succeeded ? "success" : "fail");
        say(Optional.of(player), responseId);
        
    }

    public void reset() {
    	
        set(PLAYER_HISTORY_MAP, new CompoundNBT());

        setHealth(20.0F);

        set(SPOUSE_NAME, "");
        set(SPOUSE_UUID, Optional.of(Constants.ZERO_UUID));
        set(MARRIAGE_STATE, EnumMarriageState.NOT_MARRIED.getId());
        set(HAS_BABY, false);
        
    }
    
    public void say(Optional<PlayerEntity> player, String phraseId, @Nullable String... params) {
    	
        ArrayList<String> paramsList = new ArrayList<>();
        if (params != null) Collections.addAll(paramsList, params);

        if (player.isPresent()) {
        	
            PlayerEntity thePlayer = player.get();

            // Provide player as the first param, always
            paramsList.add(0, thePlayer.getName().getString());

            // Infected villagers do not speak.
            if (get(IS_INFECTED)) {
            	
                thePlayer.sendMessage(new StringTextComponent(getDisplayName().getString() + ": " + "???"), thePlayer.getUUID());
                this.playSound(SoundEvents.ZOMBIE_AMBIENT, 0.5F, random.nextFloat() + 0.5F);
                
            } else {
            	
                String dialogueType = getPlayerHistoryFor(player.get().getUUID()).getDialogueType().getId();
                String phrase = MCA.getLocalizer().localize(dialogueType + "." + phraseId, paramsList);
                thePlayer.sendMessage(new StringTextComponent(String.format("%1$s: %2$s", getDisplayName().getString(), phrase)), thePlayer.getUUID());
                
            }
            
        } else {
        	
            MCA.getLog().warn(new Throwable("Say called on player that is not present!"));
            
        }
        
    }

    public boolean playerIsParent(PlayerEntity player) {
    	
        ParentData data = ParentData.fromNBT(get(PARENTS));
        return data.getParent1UUID().equals(player.getUUID()) || data.getParent2UUID().equals(player.getUUID());
        
    }

    public void handleButtonClick(ServerPlayerEntity player, String guiKey, String buttonId) {
    	
        PlayerHistory history = getPlayerHistoryFor(player.getUUID());
        Optional<APIButton> button = API.getButtonById(guiKey, buttonId);
        
        if (!button.isPresent()) {
        	
            MCA.getLog().warn("Button not found for key and ID: " + guiKey + ", " + buttonId);
            
        } else if (button.get().isInteraction()) handleInteraction(player, history, button.get());

        //  TODO All of this...
        /*
        switch (buttonId) {
        
            case "gui.button.move":
                set(MOVE_STATE, EnumMoveState.MOVE.getId());
                this.playerToFollowUUID = Constants.ZERO_UUID;
                break;
                
            case "gui.button.stay":
                set(MOVE_STATE, EnumMoveState.STAY.getId());
                break;
                
            case "gui.button.follow":
                set(MOVE_STATE, EnumMoveState.FOLLOW.getId());
                this.playerToFollowUUID = player.getUUID();
                stopChore();
                break;
                
            case "gui.button.ridehorse":
                toggleMount(player);
                break;
                
            case "gui.button.sethome":
                setHome(player);
                break;
                
            case "gui.button.gohome":
                goHome(player);
                break;
                
            case "gui.button.setworkplace":
                setWorkplace(player);
                break;
                
            case "gui.button.sethangout":
                setHangout(player);
                break;
                
            case "gui.button.trade":
                if (MCA.getConfig().allowTrading) {
                	
                    setCustomer(player);
                    player.displayVillagerTradeGui(this);
                    
                } else {
                	
                    player.sendMessage(new TextComponentString(MCA.getLocalizer().localize("info.trading.disabled")));
                    
                }
                break;
                
            case "gui.button.inventory":
                player.openGui(MCA.getInstance(), Constants.GUI_ID_INVENTORY, player.world, this.getEntityId(), 0, 0);
                break;
                
            case "gui.button.gift":
                ItemStack stack = player.inventory.getStackInSlot(player.inventory.currentItem);
                int giftValue = API.getGiftValueFromStack(stack);
                if (!handleSpecialCaseGift(player, stack)) {
                	
                    if (stack.getItem() == Items.GOLDEN_APPLE) set(IS_INFECTED, false);
                    else {
                    	
                        history.changeHearts(giftValue);
                        say(Optional.of(player), API.getResponseForGift(stack));
                        
                    }
                    
                }
                
                if (giftValue > 0) {
                	
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    
                }
                break;
                
            case "gui.button.procreate":
                if (PlayerSaveData.get(player).isBabyPresent())
                    say(Optional.of(player), "interaction.procreate.fail.hasbaby");
                else if (history.getHearts() < 100) say(Optional.of(player), "interaction.procreate.fail.lowhearts");
                else {
                	
                    EntityAITasks.EntityAITaskEntry task = tasks.taskEntries.stream().filter((ai) -> ai.action instanceof EntityAIProcreate).findFirst().orElse(null);
                    
                    if (task != null) {
                    	
                        ((EntityAIProcreate) task.action).procreateTimer = 20 * 3; // 3 seconds
                        set(IS_PROCREATING, true);
                        
                    }
                    
                }
                break;
                
            case "gui.button.infected":
                set(IS_INFECTED, !get(IS_INFECTED));
                break;
                
            case "gui.button.texture.randomize":
                set(TEXTURE, API.getRandomSkin(this));
                break;
                
            case "gui.button.profession.randomize":
                setProfession(VillagerInit.randomProfession());
                setVanillaCareer(getVillagerData().getProfession().getRandomCareer(world.rand));
                break;
                
            case "gui.button.gender":
                EnumGender gender = EnumGender.byId(get(GENDER));
                if (gender == EnumGender.MALE) {
                    set(GENDER, EnumGender.FEMALE.getId());
                } else {
                    set(GENDER, EnumGender.MALE.getId());
                }
                // intentional fall-through here
                
            case "gui.button.texture":
                set(TEXTURE, API.getRandomSkin(this));
                break;
                
            case "gui.button.random":
                set(VILLAGER_NAME, API.getRandomName(EnumGender.byId(get(GENDER))));
                break;
                
            case "gui.button.profession":
                RegistryNamespaced<ResourceLocation, VillagerRegistry.VillagerProfession> registry = ObfuscationReflectionHelper.getPrivateValue(VillagerRegistry.class, VillagerRegistry.instance(), "REGISTRY");
                setProfession(VillagerInit.randomProfession());
                setVanillaCareer(getVillagerData().getProfession().getRandomCareer(world.rand));
                applySpecialAI();
                break;
                
            case "gui.button.prospecting":
                startChore(EnumChore.PROSPECT, player);
                break;
                
            case "gui.button.hunting":
                startChore(EnumChore.HUNT, player);
                break;
                
            case "gui.button.fishing":
                startChore(EnumChore.FISH, player);
                break;
                
            case "gui.button.chopping":
                startChore(EnumChore.CHOP, player);
                break;
                
            case "gui.button.harvesting":
                startChore(EnumChore.HARVEST, player);
                break;
                
            case "gui.button.stopworking":
                stopChore();
                break;
                
        }
        
        */
        
    }

    private void applySpecialAI() {
    	
        if (getVillagerData().getProfession() == VillagerInit.BANDIT) {
        	
        	this.goalSelector.getRunningGoals().forEach((g) -> this.goalSelector.removeGoal(g));
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8D, false));
            this.goalSelector.addGoal(2, new MoveThroughVillageGoal(this, 0.6D, false, 0, null));

            this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, MCAVillager.class, 100, false, false, BANDIT_TARGET_SELECTOR));
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
            
        } else if (getVillagerData().getProfession() == VillagerInit.GUARD) {
        	
        	this.goalSelector.getRunningGoals().filter((g) -> g.getClass().equals(AvoidEntityGoal.class)).forEach((g) -> this.goalSelector.removeGoal(g));

            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8D, false));
            this.goalSelector.addGoal(2, new MoveThroughVillageGoal(this, 0.6D, false, 0, null));

            this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, MCAVillager.class, 100, false, false, GUARD_TARGET_SELECTOR));
            this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 100, false, false, null));
            this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, VexEntity.class, 100, false, false, null));
            this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, VindicatorEntity.class, 100, false, false, null));
            
        } else {
        	
            //every other villager is allowed to defend itself from zombies while fleeing
        	// TODO Fix this cuz its broken
            //this.goalSelector.addGoal(0, new DefendVillageTargetGoal(null));
            
            this.goalSelector.getRunningGoals().forEach((g) -> this.goalSelector.removeGoal(g));
            this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 100, false, false, null));
            
        }
        
    }

    public void spawnParticles(BasicParticleType particleType) {
    	
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT) {
        	
            for (int i = 0; i < 5; ++i) {
            	
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                
                this.getLevel().addParticle(particleType, getX() + (double) (this.random.nextFloat() * getBbWidth() * 2.0F) - (double) getBbWidth(), getY() + 1.0D +
                		(double) (random.nextFloat() * getBbHeight()), getZ() + (double) (this.random.nextFloat() * getBbWidth() * 2.0F) - (double) getBbWidth(), d0, d1, d2);
            
            }
            
        } else {
        	
        	Network.INSTANCE.sendToServer(new MCAMessages.SpawnParticles(this.getUUID(), particleType));
            
        }
        
    }

    public void marry(PlayerEntity player) {
    	
        set(SPOUSE_UUID, Optional.of(player.getUUID()));
        set(SPOUSE_NAME, player.getName().getString());
        set(MARRIAGE_STATE, EnumMarriageState.MARRIED.getId());
        
    }

    public PlayerHistory getPlayerHistoryFor(UUID uuid) {
    	
        if (!get(PLAYER_HISTORY_MAP).contains(uuid.toString())) {
        	
            updatePlayerHistoryMap(PlayerHistory.getNew(this, uuid));
            
        }
        
        return PlayerHistory.fromNBT(this, uuid, get(PLAYER_HISTORY_MAP).getCompound(uuid.toString()));
        
    }

    public void updatePlayerHistoryMap(PlayerHistory history) {
    	
        CompoundNBT nbt = get(PLAYER_HISTORY_MAP);
        nbt.put(history.getPlayerUUID().toString(), history.toNBT());
        set(PLAYER_HISTORY_MAP, nbt);
        
    }

    public ResourceLocation getTextureResourceLocation() {
    	
        if (get(IS_INFECTED)) {
        	
            return ResourceLocationCache.getResourceLocationFor(String.format("mca_reborn:skins/%s/zombievillager.png", get(GENDER) == EnumGender.MALE.getId() ? "male" : "female"));
            
        } else {
        	
            return ResourceLocationCache.getResourceLocationFor(get(TEXTURE));
            
        }
        
    }

    public String getCurrentActivity() {
    	
        EnumMoveState moveState = EnumMoveState.byId(get(MOVE_STATE));
        
        if (moveState != EnumMoveState.MOVE) {
        	
            return moveState.getFriendlyName();
            
        }

        EnumChore chore = EnumChore.byId(get(ACTIVE_CHORE));
        
        if (chore != EnumChore.NONE) {
        	
            return chore.getFriendlyName();
            
        }

        return null;
        
    }
	
	@Override
	public void addAdditionalSaveData(CompoundNBT nbt) {

        nbt.putUUID("uuid", this.getUUID()); // for SavedVillagers
        nbt.putString("name", get(VILLAGER_NAME));
        nbt.putString("texture", get(TEXTURE));
        nbt.putInt("gender", get(GENDER));
        nbt.putFloat("girth", get(GIRTH));
        nbt.putFloat("tallness", get(TALLNESS));
        nbt.put("playerHistoryMap", get(PLAYER_HISTORY_MAP));
        nbt.putInt("moveState", get(MOVE_STATE));
        nbt.putInt("marriageState", get(MARRIAGE_STATE));
        nbt.putDouble("homePositionX", home.getX());
        nbt.putDouble("homePositionY", home.getY());
        nbt.putDouble("homePositionZ", home.getZ());
        nbt.putUUID("playerToFollowUUID", playerToFollowUUID);
        nbt.putUUID("spouseUUID", get(SPOUSE_UUID).orElse(Constants.ZERO_UUID));
        nbt.putString("spouseName", get(SPOUSE_NAME));
        nbt.putBoolean("isProcreating", get(IS_PROCREATING));
        nbt.putBoolean("infected", get(IS_INFECTED));
        nbt.putInt("ageState", get(AGE_STATE));
        nbt.putInt("startingAge", startingAge);
        nbt.putInt("activeChore", get(ACTIVE_CHORE));
        nbt.putUUID("choreAssigningPlayer", get(CHORE_ASSIGNING_PLAYER).orElse(Constants.ZERO_UUID));
        nbt.put("inventory", inventory.writeInventoryToNBT());
        nbt.putInt("babyAge", babyAge);
        nbt.put("parents", get(PARENTS));
        nbt.putInt("bedX", get(BED_POS).getX());
        nbt.putInt("bedY", get(BED_POS).getY());
        nbt.putInt("bedZ", get(BED_POS).getZ());
        nbt.putInt("workplaceX", get(WORKPLACE_POS).getX());
        nbt.putInt("workplaceY", get(WORKPLACE_POS).getY());
        nbt.putInt("workplaceZ", get(WORKPLACE_POS).getZ());
        nbt.putInt("hangoutX", get(HANGOUT_POS).getX());
        nbt.putInt("hangoutY", get(HANGOUT_POS).getY());
        nbt.putInt("hangoutZ", get(HANGOUT_POS).getZ());
        nbt.putBoolean("sleeping", get(SLEEPING));
        
		super.addAdditionalSaveData(nbt);
		
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {

        set(VILLAGER_NAME, nbt.getString("name"));
        set(GENDER, nbt.getInt("gender"));
        set(TEXTURE, nbt.getString("texture"));
        set(GIRTH, nbt.getFloat("girth"));
        set(TALLNESS, nbt.getFloat("tallness"));
        set(PLAYER_HISTORY_MAP, nbt.getCompound("playerHistoryMap"));
        set(MOVE_STATE, nbt.getInt("moveState"));
        set(MARRIAGE_STATE, nbt.getInt("marriageState"));
        set(SPOUSE_UUID, Optional.of(nbt.getUUID("spouseUUID")));
        set(SPOUSE_NAME, nbt.getString("spouseName"));
        set(IS_PROCREATING, nbt.getBoolean("isProcreating"));
        set(IS_INFECTED, nbt.getBoolean("infected"));
        set(AGE_STATE, nbt.getInt("ageState"));
        set(ACTIVE_CHORE, nbt.getInt("activeChore"));
        set(CHORE_ASSIGNING_PLAYER, Optional.of(nbt.getUUID("choreAssigningPlayer")));
        set(HAS_BABY, nbt.getBoolean("hasBaby"));
        set(BABY_IS_MALE, nbt.getBoolean("babyIsMale"));
        set(PARENTS, nbt.getCompound("parents"));
        set(BED_POS, new BlockPos(nbt.getInt("bedX"), nbt.getInt("bedY"), nbt.getInt("bedZ")));
        set(HANGOUT_POS, new BlockPos(nbt.getInt("hangoutX"), nbt.getInt("hangoutY"), nbt.getInt("hangoutZ")));
        set(WORKPLACE_POS, new BlockPos(nbt.getInt("workplaceX"), nbt.getInt("workplaceY"), nbt.getInt("workplaceZ")));
        set(SLEEPING, nbt.getBoolean("sleeping"));
        inventory.readInventoryFromNBT(nbt.getCompound("inventory"));

        // Vanilla Age doesn't apply from the superclass call. Causes children to revert to the starting age on world reload.
        this.startingAge = nbt.getInt("startingAge");
        setAge(nbt.getInt("Age"));

        this.home = new BlockPos(nbt.getDouble("homePositionX"), nbt.getDouble("homePositionY"), nbt.getDouble("homePositionZ"));
        this.playerToFollowUUID = nbt.getUUID("playerToFollowUUID");
        this.babyAge = nbt.getInt("babyAge");

        applySpecialAI();
        
		super.readAdditionalSaveData(nbt);
		
	}

}
