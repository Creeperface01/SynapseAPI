package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;


import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

import java.util.UUID;

/**
 * @author Nukkit Project Team
 */
public class CraftingEventPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.CRAFTING_EVENT_PACKET;

    public int windowId;
    public int type;
    public UUID id;

    public Item[] input;
    public Item[] output;

    public CraftingEventPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.CraftingEventPacket.class);

        cn.nukkit.network.protocol.CraftingEventPacket pk = (cn.nukkit.network.protocol.CraftingEventPacket) pkk;
        this.windowId = pk.windowId;
        this.type = pk.type;
        this.id = pk.id;
        this.input = pk.input;
        this.output = pk.output;
        return this;
    }

    public cn.nukkit.network.protocol.CraftingEventPacket toDefault() {
        cn.nukkit.network.protocol.CraftingEventPacket pk = new cn.nukkit.network.protocol.CraftingEventPacket();
        pk.windowId = windowId;
        pk.type = type;
        pk.id = id;
        pk.input = input;
        pk.output = output;
        return pk;
    }

    @Override
    public void decode() {
        this.windowId = this.getByte();
        this.type = (int) this.getUnsignedVarInt();
        this.id = this.getUUID();

        int inputSize = (int) this.getUnsignedVarInt();
        this.input = new Item[inputSize];
        for (int i = 0; i < inputSize && i < 128; ++i) {
            this.input[i] = this.getSlot();
        }

        int outputSize = (int) this.getUnsignedVarInt();
        this.output = new Item[outputSize];
        for (int i = 0; i < outputSize && i < 128; ++i) {
            this.output[i] = getSlot();
        }
    }

    @Override
    public void encode() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.CraftingEventPacket.class;
    }
}
