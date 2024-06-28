package plum.straya.entity;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import plum.straya.init.StrayaBlocks;
import plum.straya.init.StrayaEntityTypes;
import plum.straya.init.StrayaItems;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

@SuppressWarnings("removal")
public class KangarooEntity extends TamableAnimal implements IAnimatable {
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(KangarooEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_TAMED = SynchedEntityData.defineId(KangarooEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData.defineId(KangarooEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> DATA_SITTING = SynchedEntityData.defineId(KangarooEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("idle", true);
    protected static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("walk", true);
    protected static final AnimationBuilder HOP_ANIM = new AnimationBuilder().addAnimation("hop", true);
    protected static final AnimationBuilder KICK_ANIM = new AnimationBuilder().addAnimation("kick", false);
    protected static final AnimationBuilder JUMP_ANIM = new AnimationBuilder().addAnimation("jump", true);
    protected static final AnimationBuilder SIT_ANIM = new AnimationBuilder().addAnimation("sit", true);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean highJump = false;

    public KangarooEntity(EntityType<? extends KangarooEntity> type, Level world) {
        super(type, world);
        this.setTame(false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 3.5D, true));
        this.targetSelector.addGoal(3, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(6, new PanicGoal(this, 3.0D));
        this.goalSelector.addGoal(7, new BreedGoal(this, 2.0D));
        this.goalSelector.addGoal(8, new TemptGoal(this, 2.5D, Ingredient.of(Items.DEAD_BUSH), false));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 2D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(12, new FollowOwnerGoal(this, 3.0D, 3.0F, 2.0F, false));
    }

    @Override
    public void setJumping(boolean jumping) {
        super.setJumping(jumping);
        if (jumping) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, highJump ? 0.5 : 0.2, 0.0));
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isOrderedToSit()) {
            this.getNavigation().stop();
            return;
        }
    }

    public void setHighJump(boolean highJump) {
        this.highJump = highJump;
    }

    @Override
    public void registerControllers(final AnimationData data) {
        data.addAnimationController(new AnimationController<KangarooEntity>(this, "controller", 3, this::animationPredicate));
        data.addAnimationController(new AnimationController<KangarooEntity>(this, "attackController", 0, this::attackPredicate));
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (!this.isOnGround()) {
            event.getController().setAnimationSpeed(1);
            event.getController().setAnimation(JUMP_ANIM);
            return PlayState.CONTINUE;
        } else if (event.isMoving()) {
            event.getController().setAnimationSpeed(2);
            event.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        } else if (this.isSitting()) {
            event.getController().setAnimationSpeed(1);
            event.getController().setAnimation(SIT_ANIM);
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimationSpeed(1);
            event.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }
    }
    
    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
    	if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
    		event.getController().markNeedsReload();
            event.getController().setAnimationSpeed(1.5);
    		event.getController().setAnimation(KICK_ANIM);
    		this.swinging = false;
    	}
        	return PlayState.CONTINUE;
    }
    
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob mate) {
        KangarooEntity baby = new KangarooEntity(StrayaEntityTypes.KANGAROO.get(), world);
        if (mate instanceof KangarooEntity) {
            KangarooEntity parent = (KangarooEntity) mate;
            if (this.random.nextBoolean()) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(parent.getVariant());
            }
        }
        return baby;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        this.setVariant(accessor.getRandom().nextInt(5));
        if (data == null) {
            data = new AgeableMob.AgeableMobGroupData(false);
        }

        return super.finalizeSpawn(accessor, difficulty, type, data, tag);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        if (distance <= 3.5F) {
            return false;
        }
        return super.causeFallDamage(distance - 3.5F, damageMultiplier, source);
    }

    public int getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    public void setVariant(int variant) {
        this.entityData.set(DATA_VARIANT_ID, variant);
    }

    public boolean isTamed() {
        return this.entityData.get(DATA_TAMED);
    }

    public void setTamed(boolean tamed) {
        this.entityData.set(DATA_TAMED, tamed);
    }

    public boolean isSitting() {
        return this.entityData.get(DATA_SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(DATA_SITTING, sitting);
    }

    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNERUUID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.entityData.set(DATA_OWNERUUID, Optional.ofNullable(ownerUUID));
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // Prevent using Kangaroo Paw items on tamed kangaroos
        if (this.isTamed() && itemstack.is(StrayaBlocks.KANGAROO_PAW_ITEM.get())) {
            if (this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.heal(6.0F); // Heal the kangaroo by 6 health points
                this.level.broadcastEntityEvent(this, (byte) 7); // Play happy particles
                return InteractionResult.SUCCESS;
            } else if (this.getHealth() == this.getMaxHealth()) {
                // Toggle sit/stand state for tamed kangaroo only if at max health
                if (!this.level.isClientSide) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.navigation.stop();
                    this.setTarget(null);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        
     // Toggle sit/stand state for tamed kangaroo
        if (this.isTamed() && !itemstack.is(Items.DEAD_BUSH) && !itemstack.is(StrayaItems.HIDE_POUCH.get())) {
            if (!this.level.isClientSide) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.navigation.stop();
                this.setTarget(null);
            }
            return InteractionResult.SUCCESS;
        }

        // Tame baby kangaroo with Kangaroo Paw item
        if (itemstack.is(StrayaBlocks.KANGAROO_PAW_ITEM.get()) && !this.isTame() && this.isBaby()) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            if (!this.level.isClientSide) {
                if (this.random.nextInt(3) == 0) {  // 33% chance to tame the kangaroo
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);  // Taming failed
                }
            }

            return InteractionResult.SUCCESS;
        }

        // Handle Hide Pouch interaction
        if (itemstack.is(StrayaItems.HIDE_POUCH.get()) && this.isBaby()) {
            if (!this.level.isClientSide) {
                ItemStack fullHidePouch = new ItemStack(StrayaItems.FULL_HIDE_POUCH.get());
                CompoundTag tag = new CompoundTag();
                this.addAdditionalSaveData(tag);
                fullHidePouch.getOrCreateTag().put("KangarooData", tag);
                fullHidePouch.setHoverName(this.getCustomName());
                player.setItemInHand(hand, fullHidePouch); // Replace the item in hand with the full hide pouch
                this.remove(RemovalReason.KILLED);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    public void tame(Player player) {
        this.setTamed(true);
        this.setOwnerUUID(player.getUUID());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, 0);
        this.entityData.define(DATA_TAMED, false);
        this.entityData.define(DATA_OWNERUUID, Optional.empty());
        this.entityData.define(DATA_SITTING, false);
    }

    public void addAdditionalSaveData(CompoundTag add) {
        super.addAdditionalSaveData(add);
        add.putInt("Variant", this.getVariant());
        add.putBoolean("Tamed", this.isTamed());
        add.putBoolean("Sitting", this.isSitting());
        if (this.getOwnerUUID() != null) {
            add.putUUID("OwnerUUID", this.getOwnerUUID());
        }
    }

    public void readAdditionalSaveData(CompoundTag read) {
        super.readAdditionalSaveData(read);
        this.setVariant(read.getInt("Variant"));
        this.setTamed(read.getBoolean("Tamed"));
        this.setSitting(read.getBoolean("Sitting"));
        if (read.hasUUID("OwnerUUID")) {
            this.setOwnerUUID(read.getUUID("OwnerUUID"));
        }
    }

    public static boolean checkKangarooSpawnRules(EntityType<KangarooEntity> entityType, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState blockstate = level.getBlockState(pos.below());
        return blockstate.is(BlockTags.BASE_STONE_OVERWORLD) && level.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.DEAD_BUSH);
    }

    public void setTame(boolean isTame) {
        super.setTame(isTame);
        if (isTame) {
           this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(25.0D);
           this.setHealth(25.0F);
        } else {
           this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(15.0D);
        }

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.5D);
     }
    
    public boolean isOrderedToSit() {
        return this.entityData.get(DATA_SITTING);
    }

    public void setOrderedToSit(boolean sitting) {
        this.entityData.set(DATA_SITTING, sitting);
        if (sitting) {
            this.getNavigation().stop();
        }
    }
    
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);

        if (recentlyHit) {
            int count = this.random.nextInt(2) + 0; //Drops 0, 1 or 2 hide
            this.spawnAtLocation(new ItemStack(StrayaItems.KANGAROO_HIDE.get(), count));
        }
    }
}