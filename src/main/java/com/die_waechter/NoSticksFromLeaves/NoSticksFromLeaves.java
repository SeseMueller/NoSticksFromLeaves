package com.die_waechter.NoSticksFromLeaves;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NoSticksFromLeaves.MODID)
public class NoSticksFromLeaves
{

    public static final String MODID = "no_sticks_from_leaves";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MODID);

    private static final RegistryObject<LootTableModifier.Serializer> LOOT_TABLE_MODIFIER = GLM.register("loot_table_modifier", LootTableModifier.Serializer::new);

    public NoSticksFromLeaves() {
        
        LOGGER.info("Hello from NoSticksFromLeaves!");

        GLM.register(FMLJavaModLoadingContext.get().getModEventBus());

        LOGGER.info("Registered loot modifier");
    }


    //As in the exaple from the forge documentation.
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class EventHandlers {
        @SubscribeEvent
        public static void runData(GatherDataEvent event) {
            LOGGER.info("GatherDataEvent");
            event.getGenerator().addProvider(new DataProvider(event.getGenerator(), MODID));
        }


        @SubscribeEvent
        public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
            LOGGER.info("Registering loot modifier");
            // event.getRegistry().register(LOOT_TABLE_MODIFIER.get());
            event.getRegistry().register(new LootTableModifier.Serializer().setRegistryName(new ResourceLocation(MODID, "leaves_decay")));
            
        }
    }

    private static class DataProvider extends GlobalLootModifierProvider {
            
        public DataProvider(DataGenerator gen, String modid) {
            super(gen, modid);
        }

        @Override
        protected void start() {

            String[] leaves = new String[] {
                "blocks/oak_leaves",
                "blocks/spruce_leaves",
                "blocks/birch_leaves",
                "blocks/jungle_leaves",
                "blocks/acacia_leaves",
                "blocks/dark_oak_leaves"
            };

            //Adds the leave decay loottable modifier to all leaves.
            for (String leaf : leaves) {

                add("leave_decay", LOOT_TABLE_MODIFIER.get(), new LootTableModifier(
                    new ILootCondition[] { 
                        LootTableIdCondition.builder(new ResourceLocation(leaf)).build()
                    },
                    Items.STICK
                    )
                );
            }

        }
    }

    
}
