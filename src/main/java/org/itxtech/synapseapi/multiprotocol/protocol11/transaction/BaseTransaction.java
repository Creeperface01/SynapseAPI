package org.itxtech.synapseapi.multiprotocol.protocol11.transaction;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;

/**
 * @author CreeperFace
 */
public class BaseTransaction implements Transaction {

    protected final Inventory inventory;

    protected final int slot;

    protected final Item sourceItem;

    protected final Item targetItem;

    protected final long creationTime;

    public BaseTransaction(Inventory inventory, int slot, Item sourceItem, Item targetItem) {
        this.inventory = inventory;
        this.slot = slot;
        this.sourceItem = sourceItem.clone();
        this.targetItem = targetItem.clone();
        this.creationTime = System.currentTimeMillis();
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public Item getSourceItem() {
        return sourceItem.clone();
    }

    @Override
    public Item getTargetItem() {
        return targetItem.clone();
    }
}
