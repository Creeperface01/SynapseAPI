package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class PlayerInputPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_INPUT_PACKET;

    public float motionX;
    public float motionY;

    public boolean unknownBool1;
    public boolean unknownBool2;

    public PlayerInputPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.PlayerInputPacket.class);

        cn.nukkit.network.protocol.PlayerInputPacket pk = (cn.nukkit.network.protocol.PlayerInputPacket) pkk;
        this.motionX = pk.motionX;
        this.motionY = pk.motionY;
        this.unknownBool1 = pk.unknownBool1;
        this.unknownBool2 = pk.unknownBool2;
        return this;
    }

    public cn.nukkit.network.protocol.PlayerInputPacket toDefault() {
        cn.nukkit.network.protocol.PlayerInputPacket pk = new cn.nukkit.network.protocol.PlayerInputPacket();
        pk.motionX = motionX;
        pk.motionY = motionY;
        pk.unknownBool1 = unknownBool1;
        pk.unknownBool2 = unknownBool2;
        return pk;
    }

    @Override
    public void decode() {
        this.motionX = this.getLFloat();
        this.motionY = this.getLFloat();
        this.unknownBool1 = this.getBoolean();
        this.unknownBool2 = this.getBoolean();
    }

    @Override
    public void encode() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.PlayerInputPacket.class;
    }
}
