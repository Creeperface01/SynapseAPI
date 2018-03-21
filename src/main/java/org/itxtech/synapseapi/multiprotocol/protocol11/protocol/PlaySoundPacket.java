package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class PlaySoundPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.PLAY_SOUND_PACKET;

    public String name;
    public int x;
    public int y;
    public int z;
    public float volume;
    public float pitch;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putString(this.name);
        this.putBlockVector3(this.x * 8, this.y * 8, this.z * 8);
        this.putLFloat(this.volume);
        this.putLFloat(this.pitch);
    }

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.PlaySoundPacket.class);

        cn.nukkit.network.protocol.PlaySoundPacket packet = (cn.nukkit.network.protocol.PlaySoundPacket) pk;
        this.name = packet.name;
        this.volume = packet.volume;
        this.pitch = packet.pitch;
        this.x = packet.x;
        this.y = packet.y;
        this.z = packet.z;

        return this;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.PlaySoundPacket pk = new cn.nukkit.network.protocol.PlaySoundPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.name = name;
        pk.volume = volume;
        pk.pitch = pitch;

        return pk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.PlaySoundPacket.class;
    }
}
