package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.registrate.ModBlocks;
import com.oierbravo.trading_station.registrate.ModMenus;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class  TradingStationMenu extends AbstractContainerMenu {
    public final TradingStationBlockEntity blockEntity;
    public final Inventory inventory;
    private final Level level;
    private final ContainerData containerData;
    public ItemStackHandler ghostInventory;


    public TradingStationMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData){
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public TradingStationMenu(int pContainerId, Inventory pInv, BlockEntity pBlockEntity, ContainerData pData){
        super(ModMenus.TRADING_STATION.get(), pContainerId);
        checkContainerSize(pInv, 3);
        blockEntity = (TradingStationBlockEntity) pBlockEntity;
        this.level = pInv.player.getLevel();
        this.containerData = pData;
        this.inventory = pInv;
        addPlayerHotbar(pInv);
        addPlayerInventory(pInv);
        this.addDataSlots(pData);
        //this.blockEntity.inputItems
        this.blockEntity.getInputItemHandler().ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler,0,TradingStationScreen.inputSlotX[0],TradingStationScreen.inputSlotY));
            this.addSlot(new SlotItemHandler(itemHandler,1,TradingStationScreen.inputSlotX[1],TradingStationScreen.inputSlotY));
        });
        this.blockEntity.getOutputItemHandler().ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler,0,TradingStationScreen.outputSlotX,TradingStationScreen.outputSlotY));
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.getTargetItemHandler(),0,87,28));


    }

    public TradingStationMenu(int pContainerId, Inventory pInventory, BlockPos sourcePos) {
        this(pContainerId, pInventory, Minecraft.getInstance().level.getBlockEntity(sourcePos), TradingStationBlockEntity.createContainerData((TradingStationBlockEntity) Minecraft.getInstance().level.getBlockEntity(sourcePos)));
    }

    public static TradingStationMenu factory(@Nullable MenuType<TradingStationMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        return new TradingStationMenu(pContainerId, inventory, buf);
    }
    public int getScaledProgress() {
        int progress = this.containerData.get(0);
        int maxProgress = this.containerData.get(1);  // Max Progress
        int progressArrowSize = 34; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
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

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.TRADING_STATION.get());
    }
    private static final int PLAYER_INVENTORY_Y = 64;

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, PLAYER_INVENTORY_Y + i * 18));
            }
        }
    }
    private static final int HOTBAR_Y = 122;

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, HOTBAR_Y));
        }
    }

  }
