package org.itxtech.synapseapi.multiprotocol.protocol11.transaction;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;

/**
 * @author CreeperFace
 */
public interface Transaction {

    Inventory getInventory();

    int getSlot();

    Item getSourceItem();

    Item getTargetItem();

    long getCreationTime();
}
