package com.die_waechter.NoSticksFromLeaves;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;

public class LootTableModifier extends LootModifier{

    private final Item deletion;

    protected LootTableModifier(ILootCondition[] conditionsIn, Item deletion) {
        super(conditionsIn);
        NoSticksFromLeaves.LOGGER.info("LootTableModifier constructor");
        this.deletion = deletion;
    }

    @Nullable
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        
        // NoSticksFromLeaves.LOGGER.debug(context.getQueriedLootTableId());
        
        generatedLoot.removeIf(itemStack -> itemStack.getItem() == Items.STICK);

        return generatedLoot;
    }


    protected static class Serializer extends GlobalLootModifierSerializer<LootTableModifier> {

        @Override
        public LootTableModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditionsIn) {
            Item deletion = ForgeRegistries.ITEMS.getValue(new ResourceLocation(object.get("deletion").getAsString()));

            return new LootTableModifier(conditionsIn, deletion);
        }

        @Override
        public JsonObject write(LootTableModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("deletion", ForgeRegistries.ITEMS.getKey(instance.deletion).toString());
            // json.addProperty("deletion", instance.deletion.getRegistryName().toString());
            return makeConditions(instance.conditions);
        }
    }
}
