package org.itxtech.synapseapi.multiprotocol.protocol11.transaction;

import cn.nukkit.inventory.Inventory;

import java.util.Set;

/**
 * @author CreeperFace
 */
public interface TransactionGroup {

    long getCreationTime();

    Set<Transaction> getTransactions();

    Set<Inventory> getInventories();

    void addTransaction(Transaction transaction);

    boolean canExecute();

    boolean execute();

    boolean execute(boolean force);

    boolean hasExecuted();
}
