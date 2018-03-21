package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MobEquipmentPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.MOB_EQUIPMENT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public Item item;
    public int slot;
    public int selectedSlot;
    public int windowId;

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.MobEquipmentPacket.class);

        cn.nukkit.network.protocol.MobEquipmentPacket packet = (cn.nukkit.network.protocol.MobEquipmentPacket) pk;
        this.eid = packet.eid;
        this.item = packet.item;
        this.slot = packet.inventorySlot;
        this.selectedSlot = packet.hotbarSlot;
        this.windowId = packet.windowId;

        return this;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.MobEquipmentPacket pk = new cn.nukkit.network.protocol.MobEquipmentPacket();
        pk.eid = eid;
        pk.item = item;
        pk.hotbarSlot = selectedSlot;
        pk.inventorySlot = slot;
        pk.windowId = windowId;

        return pk;
    }

    @Override
    public void decode() {
        this.eid = this.getVarLong(); //EntityRuntimeID
        this.item = this.getSlot();
        this.slot = this.getByte();
        this.selectedSlot = this.getByte();
        this.windowId = this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.eid); //EntityRuntimeID
        this.putSlot(this.item);
        this.putByte((byte) this.slot);
        this.putByte((byte) this.selectedSlot);
        this.putByte((byte) this.windowId);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.MobEquipmentPacket.class;
    }
}
