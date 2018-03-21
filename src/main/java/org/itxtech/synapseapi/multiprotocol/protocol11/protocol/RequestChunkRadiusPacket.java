package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class RequestChunkRadiusPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET;

    public int radius;

    public RequestChunkRadiusPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.RequestChunkRadiusPacket.class);

        cn.nukkit.network.protocol.RequestChunkRadiusPacket pk = (cn.nukkit.network.protocol.RequestChunkRadiusPacket) pkk;
        this.radius = pk.radius;
        return this;
    }

    public cn.nukkit.network.protocol.RequestChunkRadiusPacket toDefault() {
        cn.nukkit.network.protocol.RequestChunkRadiusPacket pk = new cn.nukkit.network.protocol.RequestChunkRadiusPacket();
        pk.radius = radius;
        return pk;
    }

    @Override
    public void decode() {
        this.radius = this.getVarInt();
    }

    @Override
    public void encode() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.RequestChunkRadiusPacket.class;
    }
}
