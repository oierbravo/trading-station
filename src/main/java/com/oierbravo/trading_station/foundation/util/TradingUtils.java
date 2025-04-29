package com.oierbravo.trading_station.foundation.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import static com.oierbravo.trading_station.infrastructure.data.TradingRecipeGen.toHolder;

public class TradingUtils {
    public static ItemStack reEnchantItemStack(ItemStack itemStack, Level level){
        // We need to reenchant the item... it throws an error if we use directly from the json.
        if(!itemStack.isEnchanted() && !itemStack.is(Items.ENCHANTED_BOOK) ){
            return itemStack;
        }
        ItemEnchantments enchantmentComponent = itemStack.remove(EnchantmentHelper.getComponentType(itemStack));
        itemStack.set(EnchantmentHelper.getComponentType(itemStack), ItemEnchantments.EMPTY);
        if(enchantmentComponent != null){
            for(Object2IntMap.Entry<Holder<Enchantment>> entry : enchantmentComponent.entrySet()){
                itemStack.enchant(toHolder(entry.getKey().getKey(),level), entry.getIntValue());
            }
        }
        return itemStack;
    }
}
