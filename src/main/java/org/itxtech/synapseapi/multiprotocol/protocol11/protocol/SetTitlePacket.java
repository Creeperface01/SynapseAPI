package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Tee7even
 */
public class SetTitlePacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.SET_TITLE_PACKET;

    public static final int TYPE_CLEAR = 0;
    public static final int TYPE_RESET = 1;
    public static final int TYPE_TITLE = 2;
    public static final int TYPE_SUBTITLE = 3;
    public static final int TYPE_ACTION_BAR = 4;
    public static final int TYPE_ANIMATION_TIMES = 5;

    public int type;
    public String text = "";
    public int fadeInTime = 0;
    public int stayTime = 0;
    public int fadeOutTime = 0;

    public SetTitlePacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetTitlePacket.class);

        cn.nukkit.network.protocol.SetTitlePacket pk = (cn.nukkit.network.protocol.SetTitlePacket) pkk;
        this.type = pk.type;
        this.text = pk.text;
        this.fadeInTime = pk.fadeInTime;
        this.stayTime = pk.stayTime;
        this.fadeOutTime = pk.fadeOutTime;
        return this;
    }

    public cn.nukkit.network.protocol.SetTitlePacket toDefault() {
        cn.nukkit.network.protocol.SetTitlePacket pk = new cn.nukkit.network.protocol.SetTitlePacket();
        pk.type = type;
        pk.text = text;
        pk.fadeInTime = fadeInTime;
        pk.stayTime = stayTime;
        pk.fadeOutTime = fadeOutTime;
        return pk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.type = this.getVarInt();
        this.text = this.getString();
        this.fadeInTime = this.getVarInt();
        this.stayTime = this.getVarInt();
        this.fadeOutTime = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(type);
        this.putString(text);
        this.putVarInt(fadeInTime);
        this.putVarInt(stayTime);
        this.putVarInt(fadeOutTime);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetTitlePacket.class;
    }
}
