package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ContainerSetSlotPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.CONTAINER_SET_SLOT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }


    public int windowid;
    public int slot;
    public int hotbarSlot;
    public Item item;
    public int selectedSlot;

    public ContainerSetSlotPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, InventorySlotPacket.class);

        InventorySlotPacket pk = (InventorySlotPacket) pkk;
        this.windowid = pk.inventoryId;
        this.slot = pk.slot;
        this.item = pk.item;
        return this;
    }

    public InventorySlotPacket toDefault() {
        InventorySlotPacket pk = new InventorySlotPacket();
        pk.inventoryId = windowid;
        pk.slot = slot;
        pk.item = item;
        return pk;
    }

    @Override
    public void decode() {
        this.windowid = this.getByte();
        this.slot = this.getVarInt();
        this.hotbarSlot = this.getVarInt();
        this.item = this.getSlot();
        this.selectedSlot = this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.windowid);
        this.putVarInt(this.slot);
        this.putVarInt(this.hotbarSlot);
        this.putSlot(this.item);
        this.putByte((byte) this.selectedSlot);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.InventorySlotPacket.class;
    }
}
