package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.mechanical_lemon_ui.foundation.gui.menu.MenuBase;
import com.oierbravo.trading_station.foundation.gui.TradingRecipeSlot;
import com.oierbravo.trading_station.registrate.ModMenus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class TradingStationMenu extends MenuBase<ITradingStationBlockEntity> {
    public final ContainerData containerData;


    public TradingStationMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        this(type, id, inv, extraData, new SimpleContainerData(3));
    }

    public TradingStationMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData, ContainerData pData) {
        super(type, id, inv, extraData);
        this.containerData = pData;
        checkContainerSize(inv, 3);
        checkContainerDataCount(pData,3);
        addDataSlots(this.containerData);

    }

    protected TradingStationMenu(MenuType<?> type, int id, Inventory inv, ITradingStationBlockEntity contentHolder) {
        this(type, id, inv, contentHolder,new SimpleContainerData(3));
    }

    protected TradingStationMenu(MenuType<?> type, int id, Inventory inv, ITradingStationBlockEntity contentHolder, ContainerData pData) {
        super(type, id, inv, contentHolder);
        this.containerData = pData;
        checkContainerSize(inv, 3);
        checkContainerDataCount(pData,3);
        addDataSlots(this.containerData);

    }

    @Override
    protected ITradingStationBlockEntity createOnClient(FriendlyByteBuf extraData) {
        BlockPos readBlockPos = extraData.readBlockPos();
        CompoundTag readNbt = extraData.readNbt();

        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(readBlockPos);
        if (blockEntity instanceof ITradingStationBlockEntity tradingStationBlockEntity) {
            tradingStationBlockEntity.readClient(readNbt);
            return tradingStationBlockEntity;
        }

        return null;
    }

    @Override
    protected void initAndReadInventory(ITradingStationBlockEntity iTradingStationBlockEntity) {

    }


    @Override
    protected void addSlots() {

        addPlayerSlots(8, 86);

        addSlot(new SlotItemHandler(contentHolder.getInputItems(), 0, 20, 38));
        addSlot(new SlotItemHandler(contentHolder.getInputItems(), 1, 43, 38));

        contentHolder.getOutputItemHandler().ifPresent((iItemHandler -> {
            addSlot(new SlotItemHandler(iItemHandler,0,132,38));
        }));
        addSlot(new TradingRecipeSlot( this.contentHolder.getTargetItemHandler(),0,85,28));
    }

    @Override
    protected void saveData(ITradingStationBlockEntity iTradingStationBlockEntity) {

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
    private static final int TE_INVENTORY_SLOT_COUNT = 2;
    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
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
        sourceSlot.onTake(pPlayer, sourceStack);
        return copyOfSourceStack;
    }

    public static TradingStationMenu create(int id, Inventory inv, ITradingStationBlockEntity be, ContainerData containerData) {
        return new TradingStationMenu(ModMenus.TRADING_STATION.get(), id, inv, be, containerData);
    }

}
