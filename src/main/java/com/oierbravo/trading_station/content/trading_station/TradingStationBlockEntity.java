package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.content.trading_recipe.IHaveMachineId;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.infrastructure.config.MConfigs;
import com.oierbravo.trading_station.registrate.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TradingStationBlockEntity extends BlockEntity  implements MenuProvider, ITradingStationBlockEntity, IHaveMachineId {


    private CompoundTag updateTag;
    public final ItemStackHandler inputItems = createInputItemHandler();
    public final ItemStackHandler outputItems = createOutputItemHandler();

    public final ItemStackHandler targetItemHandler = createTargetItemHandler();
    private final Lazy<IItemHandler> inputItemHandler = Lazy.of(() -> inputItems);
    private final Lazy<IItemHandler> outputItemHandler = Lazy.of(() -> outputItems);

    public int progress = 0;
    public int maxProgress = 1;

    private boolean isWorking = false;

    protected final ContainerData containerData;

    byte currentRedstoneMode = 0;

    protected boolean inputLocked = false;

    public TradingStationBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        updateTag = getPersistentData();
        containerData = createContainerData();
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
            }
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return true;
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
            }
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if(!isLocked())
                    return true;
                Optional<RecipeHolder<?>> recipe = getRecipe();
                if(recipe.isEmpty())
                    return true;
                return ((TradingRecipe) recipe.get().value()).matchIngredient(slot, stack);
            }
        };
    }

    @NotNull
    @Nonnull
    private ItemStackHandler createOutputItemHandler() {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return canProcess(stack) && super.isItemValid(slot, stack);
            }
        };
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputItemHandler.invalidate();
        outputItemHandler.invalidate();
    }
    public ItemStackHandler  getInputItemHandler(){
        return inputItems;
    }
    public ItemStackHandler getOutputItemHandler(){
        return outputItems;
    }
    public ItemStackHandler getTargetItemHandler(){ return targetItemHandler;}

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.TRADING_STATION_BLOCK_ENTITY.get(),
                (be, context) -> {
                    Direction localDir = be.getBlockState().getValue(TradingStationBlock.HORIZONTAL_FACING);
                    //if(context != null && localDir == context)
                    if(context != null && localDir == context.getOpposite())
                        return be.getInputItemHandler();
                    if(context == Direction.DOWN)
                        return be.getOutputItemHandler();
                    if(context != null && localDir == context.getClockWise())
                        return be.getOutputItemHandler();
                    if(context != null && localDir == context.getCounterClockWise())
                        return be.getInputItemHandler();
                    if(context == null)
                        return null;
                    return null;
                }
        );
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("input", inputItems.serializeNBT(registries));
        tag.put("output", outputItems.serializeNBT(registries));
        tag.put("target", targetItemHandler.serializeNBT(registries));
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
        tag.putByte("redstoneMode", currentRedstoneMode);
        tag.putBoolean("isWorking", isWorking);
        tag.putBoolean("inputLocked",inputLocked);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag,registries);
        inputItems.deserializeNBT(registries,tag.getCompound("input"));
        outputItems.deserializeNBT(registries,tag.getCompound("output"));
        targetItemHandler.deserializeNBT(registries,tag.getCompound("target"));
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
        currentRedstoneMode = tag.getByte("redstoneMode");
        isWorking = tag.getBoolean("isWorking");
        inputLocked = tag.getBoolean("inputLocked");
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

        maxProgress = getProcessingTime();
        if (progress > maxProgress) {
            craftItem();
        }
        setWorking(true);
    }
    public void setWorking(boolean value){
            isWorking = value;
            BlockState pState = getBlockState().setValue(TradingStationBlock.LIT, isWorking());
            getLevel().setBlock(getBlockPos(), pState, 2);
            setChanged(getLevel(), getBlockPos(), pState);
    }

    public boolean isWorking() {
        return isWorking;
    }

    protected void updateProgress(){
        this.progress += MConfigs.server().tradingStation.progressPerTick.get();
        setChanged();
    }

    public boolean canProcess(ItemStack stack) {
        return getRecipe().isPresent();
    }

    @Override
    public int getProgress() {
        return this.progress;
    }

    @Override
    public int getMaxProgress() {
        return this.maxProgress;
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

    @Override
    public byte getRedstoneMode() {
        return currentRedstoneMode;
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("block.trading_station.trading_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return TradingStationMenu.create(pContainerId, pPlayerInventory, this, this.containerData);

    }
    public void sendToMenu(RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(getBlockPos());
        buffer.writeNbt(getUpdateTag(buffer.registryAccess()));
    }

    @Override
    public void setChangedInternal() {
        setChanged();
    }




    public final void readClient(CompoundTag tag,HolderLookup.Provider registries) {
        loadAdditional(tag,registries);
    }

    public CompoundTag writeClient(CompoundTag tag, HolderLookup.Provider registries) {
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public String getMachineId() {
        return "basic";
    }

    @Override
    public void setInputLock(boolean lock) {
        inputLocked = lock;
    }

    @Override
    public boolean isLocked() {
        return inputLocked;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return writeClient(new CompoundTag(), registries);
    }


    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        readClient(tag, registries);
    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        CompoundTag tag = pkt.getTag();
        readClient(tag == null ? new CompoundTag() : tag, registries);
    }

}
