package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.network.packets.ItemStackSyncS2CPacket;
import com.oierbravo.trading_station.registrate.ModMessages;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TradingStationBlockEntity extends BlockEntity  implements MenuProvider, ITradingStationBlockEntity {


    private CompoundTag updateTag;
    public final ItemStackHandler inputItems = createInputItemHandler();
    public final ItemStackHandler outputItems = createOutputItemHandler();

    public final ItemStackHandler targetItemHandler = createTargetItemHandler();
    private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> inputItems);
    private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(() -> outputItems);

    public int progress = 0;
    public int maxProgress = 1;

    private int lastProgress = 0;
    private boolean isWorking = false;
    private BlockState lastBlockState;

    protected final ContainerData containerData;

    byte currentRedstoneMode = 0;

    Optional<TradingRecipe> targetedRecipe;

    public TradingStationBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        updateTag = getPersistentData();
        lastBlockState = this.getBlockState();
        containerData = createContainerData();
        targetedRecipe = Optional.empty();
    }
    public ContainerData createContainerData(){
        return new ContainerData(){
            @Override
            public int get(int pIndex){
                return switch (pIndex) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> (int) currentRedstoneMode;
                    default -> 0;
                };
            }


            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> progress = pValue;
                    case 1 -> maxProgress = pValue;
                    case 2 -> currentRedstoneMode = (byte) pValue;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }
    @NotNull
    @Nonnull
    private ItemStackHandler createTargetItemHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                resetProgress();
                if(!level.isClientSide()) {
                    ModMessages.sendToClients(new ItemStackSyncS2CPacket(slot,this.getStackInSlot(0), worldPosition, ItemStackSyncS2CPacket.SlotType.TARGET));
                }
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
               // clientSync();
            }
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return true;
                //return canProcess(stack) && super.isItemValid(slot, stack);
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return ItemStack.EMPTY;
            }

            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        };
    }

    private ItemStackHandler createInputItemHandler() {
        return new ItemStackHandler(2) {

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                resetProgress();
                if(!level.isClientSide()) {
                    ModMessages.sendToClients(new ItemStackSyncS2CPacket(slot,this.getStackInSlot(0), worldPosition, ItemStackSyncS2CPacket.SlotType.INPUT));
                }
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return true;
            }
        };
    }

    @NotNull
    @Nonnull
    private ItemStackHandler createOutputItemHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    ModMessages.sendToClients(new ItemStackSyncS2CPacket(slot, this.getStackInSlot(slot), worldPosition, ItemStackSyncS2CPacket.SlotType.OUTPUT));
                }
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
                // clientSync();
            }
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return canProcess(stack) && super.isItemValid(slot, stack);
                //return false;
            }
        };
    }
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(side == Direction.UP)
            return  super.getCapability(cap, side);
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            if(side == Direction.DOWN)
                return outputItemHandler.cast();

            Direction localDir = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            return inputItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputItemHandler.invalidate();
        outputItemHandler.invalidate();
    }
    public LazyOptional<IItemHandler>  getInputItemHandler(){
        return inputItemHandler;
    }
    public LazyOptional<IItemHandler>  getOutputItemHandler(){
        return outputItemHandler;
    }
    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputItemHandler.invalidate();
        outputItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("input", inputItems.serializeNBT());
        tag.put("output", outputItems.serializeNBT());
        tag.put("target", targetItemHandler.serializeNBT());
        tag.putInt("trading_station.progress", progress);
        tag.putInt("trading_station.maxProgress", maxProgress);
        tag.putByte("redstoneMode", currentRedstoneMode);
        String targetedRecipeId = "";
        if(targetedRecipe.isPresent()){
            targetedRecipeId = targetedRecipe.get().getId().toString();
        }
        tag.putString("targetedRecipeId", targetedRecipeId);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inputItems.deserializeNBT(tag.getCompound("input"));
        outputItems.deserializeNBT(tag.getCompound("output"));
        targetItemHandler.deserializeNBT(tag.getCompound("target"));
        progress = tag.getInt("trading_station.progress");
        maxProgress = tag.getInt("trading_station.maxProgress");
        currentRedstoneMode = tag.getByte("redstoneMode");
        setTargetedRecipeById(tag.getString("targetedRecipeId"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(inputItems.getSlots() + 1);
        for (int i = 0; i < inputItems.getSlots(); i++) {
            inventory.setItem(i, inputItems.getStackInSlot(i));
        }
        inventory.setItem(inputItems.getSlots(), inputItems.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public void resetProgress() {
        this.progress = 0;
        this.maxProgress = 1;
    }


    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        if(pLevel.isClientSide()) {
            return;
        }
        if(!isPowered()) {
            setWorking(false);
            return;
        }
        if(!canCraftItem()) {
            setWorking(false);
            return;
        }

        updateProgress();
        setWorking(true);

        maxProgress = getProcessingTime();
        if (progress > maxProgress) {
            craftItem();
        }

        setChanged(pLevel, pPos, pState);

    }
    private void setWorking(boolean value){
        if(isWorking != value){
            isWorking = value;
            BlockState pState = getBlockState().setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(isWorking()));

//            if(lastBlockState.getValue(BlockStateProperties.LIT) != pState.getValue(BlockStateProperties.LIT)){
 //               lastBlockState = pState;
                getLevel().setBlock(getBlockPos(), pState, 3);
            setChanged(getLevel(), getBlockPos(), pState);

            //}

    }


    }

    private boolean isWorking() {
        return isWorking;
    }

    protected void updateProgress(){
        this.progress += TradingStationConfig.PROGRESS_PER_TICK.get();

    }
    public boolean isPowered() {
        if(currentRedstoneMode == REDSTONE_MODES.IGNORE.ordinal())
            return true;
        if(currentRedstoneMode == REDSTONE_MODES.LOW.ordinal())
            return !this.getLevel().getBlockState(getBlockPos())
                    .getValue(BlockStateProperties.POWERED);

        return this.getLevel().getBlockState(getBlockPos())
                .getValue(BlockStateProperties.POWERED);
    }





    public boolean canCraftItem() {
        SimpleContainer inputInventory = getInputInventory();
        Optional<TradingRecipe> match = getRecipe();

        if(!match.isPresent()) {
            return false;
        }
        return hasEnoughInputItems(inputInventory, match.get().getIngredients())
                && hasEnoughOutputSpace(this.outputItems, match.get().getResultItem());
    }

    public boolean canProcess(ItemStack stack) {

        return getRecipe().isPresent();
    }
    protected boolean hasEnoughInputItems(SimpleContainer inventory, NonNullList<Ingredient> ingredients){
        int enough = 0;
        for(int ingredientIndex = 0; ingredientIndex < ingredients.size();ingredientIndex ++){
            Ingredient ingredient = ingredients.get(ingredientIndex);
            for(int slot = 0; slot < inventory.getContainerSize(); slot++){
                if(ingredient.test(inventory.getItem(slot))){
                    if(inventory.getItem(slot).getCount() >= ingredient.getItems()[0].getCount() )
                        enough++;
                }
            }
        }
        return ingredients.size() == enough;
    }

    protected boolean hasEnoughOutputSpace(ItemStackHandler stackHandler,ItemStack resultItemStack){
        return stackHandler.getStackInSlot(0).isEmpty() || stackHandler.getStackInSlot(0).is(resultItemStack.getItem()) &&  stackHandler.getStackInSlot(0).getMaxStackSize() - stackHandler.getStackInSlot(0).getCount()  >= resultItemStack.getCount() ;
    }

    @Override
    public CompoundTag getUpdateTag() {
        this.saveAdditional(updateTag);
        return updateTag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }



    public int getProgressPercent() {
        return this.progress * 100 / this.maxProgress;
    }

    @Override
    public ItemStackHandler getInputItems() {
        return inputItems;
    }

    @Override
    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    @Override
    public void setRedstoneMode(byte mode) {
        currentRedstoneMode = mode;
        setChanged();
    }

    @Override
    public byte getCurrentRedstoneMode() {
        return currentRedstoneMode;
    }


    public void setItemStack(int slot, ItemStack itemStack,ItemStackSyncS2CPacket.SlotType slotType) {
        if(slotType == ItemStackSyncS2CPacket.SlotType.INPUT)
            inputItems.setStackInSlot(slot,itemStack);
        else if (slotType == ItemStackSyncS2CPacket.SlotType.OUTPUT)
            outputItems.setStackInSlot(slot,itemStack);
        else if (slotType == ItemStackSyncS2CPacket.SlotType.TARGET)
            targetItemHandler.setStackInSlot(slot,itemStack);

    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return null;
    }

    @Override
    public String getTraderType() {
        return "basic";
    }



    @Override
    public Component getDisplayName() {
        return Component.translatable("block.trading_station.trading_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TradingStationMenu(pContainerId, pPlayerInventory, this, containerData);
    }

    public void setPreferedItem(ItemStack itemStack) {
        this.targetItemHandler.setStackInSlot(0,itemStack);
    }


    public ItemStack getTargetItemStack() {
        return targetItemHandler.getStackInSlot(0);
    }
    public ItemStackHandler getTargetItemHandler(){ return targetItemHandler;}

    @Override
    public void setTargetedRecipeById(ResourceLocation recipeId){
        Optional<TradingRecipe> recipe = ModRecipes.findById(this.getLevel(),recipeId);
        targetedRecipe = recipe;
        if(recipe.isPresent()) {
            targetItemHandler.setStackInSlot(0,recipe.get().getResultItem());
        }

    }
    public void setTargetedRecipeById(String recipeId){
        Optional<TradingRecipe> recipe = ModRecipes.findById(this.getLevel(), recipeId);
        targetedRecipe = recipe;
        if(recipe.isPresent()) {
            targetItemHandler.setStackInSlot(0,recipe.get().getResultItem());
        }
        setChanged();

    }
    @Nullable
    public Optional<TradingRecipe> getTargetedRecipe(){
        return targetedRecipe;
    }
    @Override
    public String getTargetedRecipeId() {
        if(!targetedRecipe.isPresent()){
            return "";
        }
        return targetedRecipe.get().getId().toString();
    }
}
