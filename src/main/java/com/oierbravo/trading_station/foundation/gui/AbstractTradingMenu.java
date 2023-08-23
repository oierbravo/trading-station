package com.oierbravo.trading_station.foundation.gui;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTradingMenu  extends AbstractContainerMenu {

    public final ITradingStationBlockEntity blockEntity;
    public final Inventory inventory;
    protected final Level level;
    private final ContainerData containerData;


    public AbstractTradingMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory pInv, BlockEntity pBlockEntity, ContainerData pData){
        super(pMenuType, pContainerId);
        checkContainerSize(pInv, 3);
        checkContainerDataCount(pData,3);
        blockPos = pBlockEntity.getBlockPos();
        blockEntity = (ITradingStationBlockEntity) pBlockEntity;
        level = pInv.player.getLevel();
        containerData = pData;
        inventory = pInv;

        addDataSlots(pData);
        addPlayerInventory(pInv);
        addPlayerHotbar(pInv);
        Coords2D co = getOutputSlotCoords();
        this.blockEntity.getInputItemHandler().ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler,0, getInputSlotCoords()[0].x,getInputSlotCoords()[0].y));
            this.addSlot(new SlotItemHandler(itemHandler,1, getInputSlotCoords()[1].x,getInputSlotCoords()[1].y));
        });
        this.blockEntity.getOutputItemHandler().ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler,0,getOutputSlotCoords().x,getOutputSlotCoords().y));
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.getTargetItemHandler(),0,getTargetSlotCoords().x,getTargetSlotCoords().y));



    }

    public AbstractTradingMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inv, FriendlyByteBuf extraData){
        this(pMenuType, pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }
    public abstract Coords2D[] getInputSlotCoords();

    public abstract Coords2D getOutputSlotCoords();
    public abstract  Coords2D getTargetSlotCoords();

    public abstract  Coords2D[] getInputRecipeCoords();
    @Override
    public abstract boolean stillValid(Player pPlayer);

    private BlockPos blockPos;
    public byte getCurrentRedstoneMode(){
        return (byte) this.containerData.get(2);
        //return 0;
    }
    public int getScaledProgress() {
        int progress = this.containerData.get(0);
        int maxProgress = this.containerData.get(1);  // Max Progress
        int progressArrowSize = 34; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    public BlockPos getBlockPos(){
        return blockPos;
    }
    public boolean isCrafting() {
        return containerData.get(0) > 0;
    }
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex == TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE Outpu slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }


    private static final int PLAYER_INVENTORY_Y = 74;

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, PLAYER_INVENTORY_Y + i * 18));
            }
        }
    }
    private static final int HOTBAR_Y = 132;

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, HOTBAR_Y));
        }
    }
    public Level getLevel(){
        return level;
    }
}
