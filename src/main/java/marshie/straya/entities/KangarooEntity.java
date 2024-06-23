package marshie.straya.entities;

import javax.annotation.Nullable;

import marshie.straya.entities.ai.EscapeGoal;
import marshie.straya.init.StrayaEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

@SuppressWarnings("removal")
public class KangarooEntity extends Animal implements IAnimatable {
	private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(KangarooEntity.class, EntityDataSerializers.INT);
    protected static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("idle", true);
    protected static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("walk", true);
    protected static final AnimationBuilder HOP_ANIM = new AnimationBuilder().addAnimation("hop", true);
    protected static final AnimationBuilder KICK_ANIM = new AnimationBuilder().addAnimation("kick", true);
    protected static final AnimationBuilder JUMP_ANIM = new AnimationBuilder().addAnimation("jump", true);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean highJump = false;

    public KangarooEntity(EntityType<? extends Animal> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EscapeGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25D, Ingredient.of(Items.GRASS), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public void setJumping(boolean jumping) {
        super.setJumping(jumping);
        if (jumping) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, highJump ? 0.0 : 0.0, 0.0));
        }
    }

    public void setHighJump(boolean highJump) {
        this.highJump = highJump;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(ForgeMod.SWIM_SPEED.get(), 0.3);
    }

    @Override
    public void registerControllers(final AnimationData data) {
        data.addAnimationController(new AnimationController<KangarooEntity>(this, "controller", 5, this::animationPredicate));
    }

    protected <E extends IAnimatable> PlayState animationPredicate(final AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob mate) {
        KangarooEntity baby = new KangarooEntity(StrayaEntityTypes.KANGAROO.get(), world);
        return baby;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
       this.setVariant(accessor.getRandom().nextInt(5));
       if (data == null) {
    	   data = new AgeableMob.AgeableMobGroupData(false);
       }

       return super.finalizeSpawn(accessor, difficulty, type, data, tag);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        if (distance <= 2.0F) {
            return false;
        }
        return super.causeFallDamage(distance - 2.0F, damageMultiplier, source);
    }

    public int getVariant() {
       return Mth.clamp(this.entityData.get(DATA_VARIANT_ID), 0, 4);
    }

    public void setVariant(int variant) {
       this.entityData.set(DATA_VARIANT_ID, variant);
    }

    protected void defineSynchedData() {
       super.defineSynchedData();
       this.entityData.define(DATA_VARIANT_ID, 0);
    }

    public void addAdditionalSaveData(CompoundTag add) {
       super.addAdditionalSaveData(add);
       add.putInt("Variant", this.getVariant());
    }

    public void readAdditionalSaveData(CompoundTag read) {
       super.readAdditionalSaveData(read);
       this.setVariant(read.getInt("Variant"));
    }
    
    public static boolean checkKangarooSpawnRules(EntityType<KangarooEntity> entityType, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState blockstate = level.getBlockState(pos.below());
        return blockstate.is(BlockTags.BASE_STONE_OVERWORLD) && level.getRawBrightness(pos, 0) > 8;
    }
}
