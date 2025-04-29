package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.mechanicals.compat.jade.IHavePercent;
import com.oierbravo.mechanicals.foundation.ingredient.CountableIngredient;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.component.TradingRecipeComponent;
import com.oierbravo.trading_station.foundation.util.TradingUtils;
import com.oierbravo.trading_station.network.packets.data.ItemSyncPayload;
import com.oierbravo.trading_station.registrate.ModItemComponents;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Optional;

public interface ITradingStationBlockEntity extends IHavePercent {
    int DEFAULT_PROCESSING_TIME = 500;

    ItemStackHandler getInputItemHandler();
    ItemStackHandler getOutputItemHandler();

    ItemStackHandler getTargetItemHandler();

    default void setItemStack(int slot, ItemStack itemStack, ItemSyncPayload.SlotType slotType) {
        if(slotType == ItemSyncPayload.SlotType.INPUT)
            getInputItemHandler().setStackInSlot(slot,itemStack);
        else if (slotType == ItemSyncPayload.SlotType.OUTPUT)
            getOutputItemHandler().setStackInSlot(slot,itemStack);
        else if (slotType == ItemSyncPayload.SlotType.TARGET)
            getTargetItemHandler().setStackInSlot(slot,itemStack);
    }

    default IEnergyStorage getEnergyStorageHandler(){
        return null;
    }

    default String getTargetedRecipeId(){
        Optional<RecipeHolder<?>> recipeHolder = TargetedRecipeUtils.getRecipeFromItemStack(getTargetItemHandler().getStackInSlot(0));
        return recipeHolder.map(holder -> holder.id().toString()).orElse("");
    };

    default void setTargetedRecipeById(ResourceLocation recipeId){
        Optional<RecipeHolder<?>> recipeHolder = ModRecipes.findById(this.getLevel(),recipeId);
        if(recipeHolder.isPresent()){
            TradingRecipe recipe = (TradingRecipe) recipeHolder.get().value();
            ItemStack targetedItemStack = recipe.getResult();
            targetedItemStack.set(ModItemComponents.TRADING_RECIPE_ID, new TradingRecipeComponent(recipeHolder.get().id()));
            getTargetItemHandler().setStackInSlot(0,targetedItemStack);
            setChangedInternal();
        }
    }
    default Optional<RecipeHolder<?>> getRecipe(){
        ItemStack targetedItemStack = getTargetItemHandler().getStackInSlot(0);
        if(targetedItemStack.isEmpty())
            return Optional.empty();
        return TargetedRecipeUtils.getRecipeFromItemStack(targetedItemStack);
    }

    default boolean canCraftItem() {
        IItemHandler inputInventory = getInputItemHandler();
        Optional<RecipeHolder<?>> match = getRecipe();

        if(match.isEmpty()) {
            this.resetProgress();
            return false;
        }

        TradingRecipe tradingRecipe = (TradingRecipe) match.get().value();
        ArrayList<ItemStack> currentStackList = new ArrayList<>();
        for(int i = 0; i< 2; i++){
            ItemStack currentStack = inputInventory.getStackInSlot(i);
            if(!currentStack.isEmpty())
                currentStackList.add(currentStack);
        }
        if(tradingRecipe.getIngredients().size() != currentStackList.size()){
            this.resetProgress();
            return false;
        }
        return hasEnoughInputItems(inputInventory, tradingRecipe.getCountableIngredients())
                && hasEnoughOutputSpace(this.getOutputItemHandler(), tradingRecipe.getResult());
    }


    byte getRedstoneMode();

    void setInputLock(boolean lock);
    boolean isLocked();

    void readClient(CompoundTag readNbt,  HolderLookup.Provider registries);

    BlockPos getBlockPos();
    Level getLevel();

    enum REDSTONE_MODES {
        IGNORE,
        LOW,
        HIGH
    }

    void setWorking(boolean value);
    boolean isWorking();

    boolean canProcess(ItemStack stack);

    void resetProgress();
    default int getProcessingTime(){
        if(getRecipe().isEmpty())
            return DEFAULT_PROCESSING_TIME;
        return ((TradingRecipe) getRecipe().get().value()).getProcessingTime();
    }
    default int getProgressPercent(){
        return getProgress() * 100 / getMaxProgress();
    };
    int getProgress();
    int getMaxProgress();

    default void craftItem() {
        Optional<RecipeHolder<?>> recipeHolder = getRecipe();

        ItemStack resultItemStack = ItemStack.EMPTY;

        if(recipeHolder.isPresent()){
            TradingRecipe recipe = (TradingRecipe) recipeHolder.get().value();
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                CountableIngredient ingredient = recipe.getCountableIngredients().get(i);

                for (int slot = 0; slot < getInputItemHandler().getSlots(); slot++) {
                    ItemStack itemStack = getInputItemHandler().getStackInSlot(slot);
                    if(ingredient.test(itemStack)){
                        getInputItemHandler().extractItem(slot,ingredient.count(),false);
                    }
                }
            }
            resultItemStack = TradingUtils.reEnchantItemStack(recipe.getResult(), getLevel());
            getOutputItemHandler().insertItem(0, resultItemStack, false);
        }

        this.resetProgress();
        craftCompleted(resultItemStack);
    }
    default void craftCompleted(ItemStack resulItemStack){

    }

    void setRedstoneMode(byte mode);
    byte getCurrentRedstoneMode();
    void sendToMenu(RegistryFriendlyByteBuf buffer);

    default void clearTargetedRecipe(){
        getTargetItemHandler().setStackInSlot(0, ItemStack.EMPTY);
        setChangedInternal();
    };
    void setChangedInternal();


    default boolean hasEnoughInputItems(IItemHandler inventory, NonNullList<CountableIngredient> ingredients){
        int enough = 0;
        for(int ingredientIndex = 0; ingredientIndex < ingredients.size();ingredientIndex ++){
            CountableIngredient ingredient = ingredients.get(ingredientIndex);
            for(int slot = 0; slot < inventory.getSlots(); slot++){
                if(ingredient.test(inventory.getStackInSlot(slot))){
                    if(inventory.getStackInSlot(slot).getCount() >= ingredient.count() )
                        enough++;
                }
            }
        }
        return ingredients.size() == enough;
    }

    default boolean isPowered() {
        if(getCurrentRedstoneMode() == REDSTONE_MODES.IGNORE.ordinal())
            return true;
        if(getCurrentRedstoneMode() == REDSTONE_MODES.LOW.ordinal())
            return !this.getLevel().getBlockState(getBlockPos())
                    .getValue(BlockStateProperties.POWERED);

        return this.getLevel().getBlockState(getBlockPos())
                .getValue(BlockStateProperties.POWERED);
    }
    default boolean hasEnoughOutputSpace(ItemStackHandler stackHandler,ItemStack resultItemStack){
        return stackHandler.getStackInSlot(0).isEmpty() || stackHandler.getStackInSlot(0).is(resultItemStack.getItem()) &&  stackHandler.getStackInSlot(0).getMaxStackSize() - stackHandler.getStackInSlot(0).getCount()  >= resultItemStack.getCount() ;
    }


    default void drops() {
        SimpleContainer inventory = new SimpleContainer(getInputItemHandler().getSlots() + 1);
        for (int i = 0; i < getInputItemHandler().getSlots(); i++) {
            inventory.setItem(i, getInputItemHandler().getStackInSlot(i));
        }
        inventory.setItem(getInputItemHandler().getSlots(), getInputItemHandler().getStackInSlot(0));

        Containers.dropContents(this.getLevel(), this.getBlockPos(), inventory);
    }
}
