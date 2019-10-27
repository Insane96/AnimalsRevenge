package insane.animalsrevenge.event;

import insane.animalsrevenge.AnimalsRevenge;
import insane.animalsrevenge.setup.ModConfig;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = AnimalsRevenge.MOD_ID)
public class EntityJoinWorld {

	@SubscribeEvent
	public static void eventEntityJoinWorld(EntityJoinWorldEvent event){
		if (event.getWorld().isRemote)
			return;

		for (String mob : ModConfig.affectedMobs.get()) {
			if (!ForgeRegistries.ENTITIES.containsKey(new ResourceLocation(mob))){
				continue;
			}

			if (!(event.getEntity() instanceof CreatureEntity))
				continue;

			if (!ForgeRegistries.ENTITIES.getKey(event.getEntity().getType()).toString().equals(mob))
				continue;

			CreatureEntity entity = (CreatureEntity) event.getEntity();

			ArrayList<Goal> toRemove = new ArrayList<>();

			entity.goalSelector.goals.forEach(goal -> {
				if (goal.getGoal() instanceof PanicGoal) {
					toRemove.add(goal.getGoal());
				}
			});

			toRemove.forEach(goal -> {
				entity.goalSelector.removeGoal(goal);
			});

			entity.goalSelector.addGoal(1, new MeleeAttackGoal(entity, 1.1D, true));
			entity.targetSelector.addGoal(1, (new HurtByTargetGoal(entity)).setCallsForHelp());

		}
	}
}
