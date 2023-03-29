package com.eerussianguy.beneath.common.entities.prey;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.schedule.Activity;

import net.dries007.tfc.common.entities.ai.prey.PreyAi;
import net.dries007.tfc.common.entities.prey.Prey;

public class NetherPreyAi
{
    public static Brain<?> makeBrain(Brain<? extends Prey> brain)
    {
        initCoreActivity(brain);
        PreyAi.initIdleActivity(brain);
        PreyAi.initRetreatActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    public static void initCoreActivity(Brain<? extends Prey> brain)
    {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink()));
    }

}
