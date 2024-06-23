package marshie.straya.entities.ai;

import marshie.straya.entities.KangarooEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import java.util.EnumSet;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class EscapeGoal extends Goal {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final KangarooEntity kangaroo;
    private final Level world;
    private final double jumpHeight = 0.8;
    
    public EscapeGoal(KangarooEntity kangaroo) {
        this.kangaroo = kangaroo;
        this.world = kangaroo.level;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        // Check if the kangaroo is in front of a 2-block high wall
        BlockPos pos = kangaroo.blockPosition();
        boolean isTrapped = isInFrontOfWall(pos);
        
        // Start using this goal if the kangaroo is in front of a wall
        return isTrapped;
    }

    @Override
    public void start() {
        // Make the kangaroo jump higher to try to escape
        kangaroo.setJumping(true);
        LOGGER.info("Kangaroo is attempting to escape!");
        kangaroo.setDeltaMovement(kangaroo.getDeltaMovement().add(0.0, jumpHeight, 0.0));
        kangaroo.setHighJump(true);
    }

    @Override
    public void stop() {
        // Reset the jump height when stopping the goal
        kangaroo.setJumping(false);
        kangaroo.setHighJump(false);
    }

    private boolean isInFrontOfWall(BlockPos pos) {
        BlockPos forwardPos = pos.relative(kangaroo.getDirection());
        BlockPos forwardPosAbove = forwardPos.above();
        
        BlockState blockStateForward = world.getBlockState(forwardPos);
        BlockState blockStateForwardAbove = world.getBlockState(forwardPosAbove);
        
        // Check if there are blocks directly in front and one block above
        return !blockStateForward.isAir() && !blockStateForwardAbove.isAir();
    }
}
