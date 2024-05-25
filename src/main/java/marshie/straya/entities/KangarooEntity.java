package marshie.straya.entities;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
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
    private int textureIndex;
    protected static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("idle", true);
    protected static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("walk", true);
    protected static final AnimationBuilder HOP_ANIM = new AnimationBuilder().addAnimation("hop", true);
    protected static final AnimationBuilder KICK_ANIM = new AnimationBuilder().addAnimation("kick", true);
    protected static final AnimationBuilder JUMP_ANIM = new AnimationBuilder().addAnimation("jump", true);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public KangarooEntity(EntityType<? extends Animal> type, Level world) {
        super(type, world);
        this.textureIndex = new Random().nextInt(7); // Randomly selects a texture index between 0 and 6
    }

    public int getTextureIndex() {
        return this.textureIndex;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, Ingredient.of(Items.GRASS), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
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
        // Register animation controllers
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
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        // TODO Auto-generated method stub
        return null;
    }

    public static boolean checkKangarooSpawnRules(EntityType<KangarooEntity> entityType, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState blockstate = level.getBlockState(pos.below());
        return blockstate.is(BlockTags.BASE_STONE_OVERWORLD) && level.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("TextureIndex", this.textureIndex);
        System.out.println("Saving textureIndex: " + this.textureIndex); // Debugging line
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("TextureIndex")) {
            this.textureIndex = compound.getInt("TextureIndex");
            System.out.println("Loading textureIndex: " + this.textureIndex); // Debugging line
        } else {
            this.textureIndex = new Random().nextInt(7); // Default to a new random texture if none is found
            System.out.println("No textureIndex found, assigning new random index: " + this.textureIndex); // Debugging line
        }
    }
}
