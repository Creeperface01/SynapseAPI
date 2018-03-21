package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SetPlayerGameTypePacket extends Packet11 {
    public final static byte NETWORK_ID = ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public SetPlayerGameTypePacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetPlayerGameTypePacket.class);

        cn.nukkit.network.protocol.SetPlayerGameTypePacket pk = (cn.nukkit.network.protocol.SetPlayerGameTypePacket) pkk;
        this.gamemode = pk.gamemode;
        return this;
    }

    public cn.nukkit.network.protocol.SetPlayerGameTypePacket toDefault() {
        cn.nukkit.network.protocol.SetPlayerGameTypePacket pk = new cn.nukkit.network.protocol.SetPlayerGameTypePacket();
        pk.gamemode = gamemode;
        return pk;
    }

    public int gamemode;

    @Override
    public void decode() {
        this.gamemode = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.gamemode);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetPlayerGameTypePacket.class;
    }
}
