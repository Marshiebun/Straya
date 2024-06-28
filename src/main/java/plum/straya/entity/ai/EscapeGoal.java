package plum.straya.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import plum.straya.entity.KangarooEntity;

import java.util.EnumSet;

public class EscapeGoal extends Goal {
    private final KangarooEntity kangaroo;
    private final Level world;
    private final double jumpHeight = 0.8;
    private final double forwardSpeed = 1.5;

    public EscapeGoal(KangarooEntity kangaroo) {
        this.kangaroo = kangaroo;
        this.world = kangaroo.level;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        BlockPos pos = kangaroo.blockPosition();
        boolean isTrapped = isInFrontOfWall(pos) || isInFrontOfFence(pos);

        return isTrapped;
    }

    @Override
    public void start() {
        kangaroo.setJumping(true);
        kangaroo.setDeltaMovement(kangaroo.getDeltaMovement().add(
            kangaroo.getLookAngle().x * forwardSpeed,
            jumpHeight,
            kangaroo.getLookAngle().z * forwardSpeed
        ));
        kangaroo.setHighJump(true);
    }

    @Override
    public void stop() {
        kangaroo.setJumping(false);
        kangaroo.setHighJump(false);
    }

    private boolean isInFrontOfWall(BlockPos pos) {
        BlockPos forwardPos = pos.relative(kangaroo.getDirection());
        BlockPos forwardPosAbove = forwardPos.above();

        BlockState blockStateForward = world.getBlockState(forwardPos);
        BlockState blockStateForwardAbove = world.getBlockState(forwardPosAbove);

        return !blockStateForward.isAir() && !blockStateForwardAbove.isAir();
    }

    private boolean isInFrontOfFence(BlockPos pos) {
        BlockPos forwardPos = pos.relative(kangaroo.getDirection());
        BlockState blockStateForward = world.getBlockState(forwardPos);

        return blockStateForward.is(BlockTags.FENCES) || blockStateForward.is(BlockTags.WALLS);
    }
}
