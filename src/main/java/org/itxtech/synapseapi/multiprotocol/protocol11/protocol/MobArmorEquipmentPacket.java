package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MobArmorEquipmentPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public Item[] slots = new Item[4];

    public MobArmorEquipmentPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.MobArmorEquipmentPacket.class);

        cn.nukkit.network.protocol.MobArmorEquipmentPacket pk = (cn.nukkit.network.protocol.MobArmorEquipmentPacket) pkk;
        this.eid = pk.eid;
        this.slots = pk.slots;
        return this;
    }

    public cn.nukkit.network.protocol.MobArmorEquipmentPacket toDefault() {
        cn.nukkit.network.protocol.MobArmorEquipmentPacket pk = new cn.nukkit.network.protocol.MobArmorEquipmentPacket();
        pk.eid = eid;
        pk.slots = slots;
        return pk;
    }

    @Override
    public void decode() {
        this.eid = this.getVarLong();
        this.slots = new Item[4];
        this.slots[0] = this.getSlot();
        this.slots[1] = this.getSlot();
        this.slots[2] = this.getSlot();
        this.slots[3] = this.getSlot();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.eid);
        this.putSlot(this.slots[0]);
        this.putSlot(this.slots[1]);
        this.putSlot(this.slots[2]);
        this.putSlot(this.slots[3]);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.MobArmorEquipmentPacket.class;
    }
}
