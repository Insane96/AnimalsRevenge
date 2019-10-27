package insane.animalsrevenge.setup;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import insane.animalsrevenge.AnimalsRevenge;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = AnimalsRevenge.MOD_ID)
public class ModConfig {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec SPEC;

	public static void init(Path file) {
		final CommentedFileConfig configData = CommentedFileConfig.builder(file)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();

		configData.load();
		SPEC.setConfig(configData);
	}

	public static ForgeConfigSpec.ConfigValue<List<? extends String>> affectedMobs;

	public static void init(){
		affectedMobs = BUILDER
				.comment("A list of all the mobs that must become neutral")
				.defineList("affected_mobs", Arrays.asList("minecraft:cow", "minecraft:chicken", "minecraft:sheep", "minecraft:pig"), o -> o instanceof String);
	}

	@SubscribeEvent
	public static void eventConfigReload(final net.minecraftforge.fml.config.ModConfig.ConfigReloading event) {
		for (String mob : ModConfig.affectedMobs.get()) {
			if (!ForgeRegistries.ENTITIES.containsKey(new ResourceLocation(mob))){
				AnimalsRevenge.LOGGER.warn("Whitlisted mob " + mob + " doesn't exist");
			}
		}
	}

	static {
		init();

		SPEC = BUILDER.build();
	}
}
