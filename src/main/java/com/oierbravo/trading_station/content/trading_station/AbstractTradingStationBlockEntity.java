package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.foundation.util.ModLang;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class AbstractTradingStationBlockEntity extends BlockEntity {
    private CompoundTag updateTag;
    public final ItemStackHandler inputItems = createInputItemHandler();
    public final ItemStackHandler outputItems = createOutputItemHandler();

    public final ItemStackHandler targetItemHandler = createTargetItemHandler();
    private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> inputItems);
    private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(() -> outputItems);

    public int progress = 0;
    public int maxProgress = 1;
    private BlockState lastBlockState;

    protected final ContainerData containerData;
    private Item preferedItem;

    public AbstractTradingStationBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        updateTag = getPersistentData();
        lastBlockState = this.getBlockState();
        preferedItem = ItemStack.EMPTY.getItem();
        containerData = AbstractTradingStationBlockEntity.createContainerData(this);
    }
    public static ContainerData createContainerData(AbstractTradingStationBlockEntity pBlockEntity){
        return new ContainerData(){
            @Override
            public int get(int pIndex){
                return switch (pIndex) {
                    case 0 -> pBlockEntity.progress;
                    case 1 -> pBlockEntity.maxProgress;
                    default -> 0;
                };
            }


            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> pBlockEntity.progress = pValue;
                    case 1 -> pBlockEntity.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
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
                if(!level.isClientSide()) {
                    ModMessages.sendToClients(new ItemStackSyncS2CPacket(slot,this.getStackInSlot(0), worldPosition, ItemStackSyncS2CPacket.SlotType.INPUT));
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
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
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
        String preferedItemString = ForgeRegistries.ITEMS.getKey(preferedItem).toString();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inputItems.deserializeNBT(tag.getCompound("input"));
        outputItems.deserializeNBT(tag.getCompound("output"));
        targetItemHandler.deserializeNBT(tag.getCompound("target"));
        progress = tag.getInt("trading_station.progress");
        maxProgress = tag.getInt("trading_station.maxProgress");
        String preferedItemString = tag.getString("trading_station.preferedItem");
        preferedItem = ForgeRegistries.ITEMS.getValue( ResourceLocation.tryParse(preferedItemString));
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

    protected static Optional<TradingRecipe> getRecipe(AbstractTradingStationBlockEntity pBlockEntity){
        Level level = pBlockEntity.getLevel();
        SimpleContainer inputInventory = getInputInventory(pBlockEntity);
        if(!pBlockEntity.targetItemHandler.getStackInSlot(0).isEmpty())
             return ModRecipes.findByOutput(level,pBlockEntity.targetItemHandler.getStackInSlot(0));
        return ModRecipes.find(inputInventory,level);
//        return level.getRecipeManager().getRecipeFor(TradingRecipe.Type.INSTANCE, inputInventory, level);
    }
    protected int getProcessingTime(AbstractTradingStationBlockEntity pBlockEntity) {
        return getRecipe(pBlockEntity).map(TradingRecipe::getProcessingTime).orElse(1);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, AbstractTradingStationBlockEntity pBlockEntity) {

        if(pLevel.isClientSide()) {
            return;
        }
        if(!isPowered(pBlockEntity))
            return;

        if (canCraftItem(pBlockEntity)) {
            pBlockEntity.progress += 1;
            BlockEntity.setChanged(pLevel, pPos, pState);
            pBlockEntity.maxProgress = pBlockEntity.getProcessingTime(pBlockEntity);
            if (pBlockEntity.progress > pBlockEntity.maxProgress) {
                AbstractTradingStationBlockEntity.craftItem(pBlockEntity);
            }
            BlockEntity.setChanged(pLevel, pPos, pState);

        } else {
            pBlockEntity.resetProgress();
            BlockEntity.setChanged(pLevel, pPos, pState);
        }


    }
    protected static void updateProgress(AbstractTradingStationBlockEntity pBlockEntity){
        pBlockEntity.progress += 1;

    }
    private static boolean isPowered(AbstractTradingStationBlockEntity pBlockEntity){
        return pBlockEntity.getLevel().getBlockState(pBlockEntity.getBlockPos())
                .getValue(BlockStateProperties.POWERED);
    }
    private static SimpleContainer getInputInventory(AbstractTradingStationBlockEntity pBlockEntity){
        int containerSize = 0;
        for(int index = 0; index < pBlockEntity.inputItems.getSlots(); index++) {
            if (!pBlockEntity.inputItems.getStackInSlot(index).isEmpty())
                containerSize++;
        }

        SimpleContainer inputInventory = new SimpleContainer(containerSize);
        pBlockEntity.inputItemHandler.ifPresent(iItemHandler -> {
            for(int slot = 0; slot < iItemHandler.getSlots(); slot++) {
                if(!iItemHandler.getStackInSlot(slot).isEmpty()){
                    inputInventory.addItem(iItemHandler.getStackInSlot(slot));
                }
            }
        });
        return inputInventory;
    }
    private static void craftItem(AbstractTradingStationBlockEntity pBlockEntity) {
        Level level = pBlockEntity.getLevel();
        SimpleContainer inputInventory = getInputInventory(pBlockEntity);

        Optional<TradingRecipe> recipe = getRecipe(pBlockEntity);

        if(recipe.isPresent()){
            for (int i = 0; i < recipe.get().getIngredients().size(); i++) {
                Ingredient ingredient = recipe.get().getIngredients().get(i);

                for (int slot = 0; slot < pBlockEntity.inputItems.getSlots(); slot++) {
                    ItemStack itemStack = pBlockEntity.inputItems.getStackInSlot(slot);
                    if(ingredient.test(itemStack)){
                        pBlockEntity.inputItems.extractItem(slot,ingredient.getItems()[0].getCount(),false);
                        inputInventory.setChanged();
                    }
                }
            }
            pBlockEntity.outputItems.insertItem(0, recipe.get().getResultItem(), false);
        }

        pBlockEntity.resetProgress();
        pBlockEntity.setChanged();
    }


    static boolean canCraftItem(AbstractTradingStationBlockEntity pBlockEntity) {
        Level level = pBlockEntity.getLevel();
        if(level == null)
            return false;

        SimpleContainer inputInventory = getInputInventory(pBlockEntity);

        Optional<TradingRecipe> match = getRecipe(pBlockEntity);

        if(match.isEmpty()) {
            return false;
        }
        //return false;
        return match.isPresent()
                && AbstractTradingStationBlockEntity.hasEnoughInputItems(inputInventory,match.get().getIngredients())
                && AbstractTradingStationBlockEntity.hasEnoughOutputSpace(pBlockEntity.outputItems,match.get().getResultItem());
    }

    private boolean canProcess(ItemStack stack) {

        return getRecipe(this).isPresent();
    }
    protected static boolean hasEnoughInputItems(SimpleContainer inventory, NonNullList<Ingredient> ingredients){
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

    protected static boolean hasEnoughOutputSpace(ItemStackHandler stackHandler,ItemStack resultItemStack){
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


    public void setItemStack(int slot, ItemStack itemStack,ItemStackSyncS2CPacket.SlotType slotType) {
        if(slotType == ItemStackSyncS2CPacket.SlotType.INPUT)
            inputItems.setStackInSlot(slot,itemStack);
        else if (slotType == ItemStackSyncS2CPacket.SlotType.OUTPUT)
            outputItems.setStackInSlot(slot,itemStack);
        else if (slotType == ItemStackSyncS2CPacket.SlotType.TARGET)
            targetItemHandler.setStackInSlot(slot,itemStack);

    }

    public void setPreferedItem(ItemStack itemStack) {
        this.targetItemHandler.setStackInSlot(0,itemStack);
    }


    public ItemStack getPreferedItemStack() {
        return targetItemHandler.getStackInSlot(0);
    }
    public ItemStackHandler getTargetItemHandler(){
        return targetItemHandler;

    }
}
