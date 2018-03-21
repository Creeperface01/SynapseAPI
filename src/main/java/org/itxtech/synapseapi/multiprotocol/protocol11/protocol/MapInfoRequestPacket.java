package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * Created by CreeperFace on 5.3.2017.
 */
public class MapInfoRequestPacket extends Packet11 {
    public long mapId;

    public MapInfoRequestPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, MapInfoRequestPacket.class);

        cn.nukkit.network.protocol.MapInfoRequestPacket pk = (cn.nukkit.network.protocol.MapInfoRequestPacket) pkk;
        this.mapId = pk.mapId;
        return this;
    }

    public cn.nukkit.network.protocol.MapInfoRequestPacket toDefault() {
        cn.nukkit.network.protocol.MapInfoRequestPacket pk = new cn.nukkit.network.protocol.MapInfoRequestPacket();
        pk.mapId = mapId;
        return pk;
    }

    @Override
    public byte pid() {
        return ProtocolInfo.MAP_INFO_REQUEST_PACKET;
    }

    @Override
    public void decode() {
        mapId = this.getVarLong();
    }

    @Override
    public void encode() {

    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.MapInfoRequestPacket.class;
    }
}
