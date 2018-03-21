package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * Created on 15-10-13.
 */
public class TextPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.TEXT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static final byte TYPE_RAW = 0;
    public static final byte TYPE_CHAT = 1;
    public static final byte TYPE_TRANSLATION = 2;
    public static final byte TYPE_POPUP = 3;
    public static final byte TYPE_TIP = 4;
    public static final byte TYPE_SYSTEM = 5;
    public static final byte TYPE_WHISPER = 6;
    public static final byte TYPE_ANNOUNCEMENT = 7;

    public byte type;
    public String source = "";
    public String message = "";
    public String[] parameters = new String[0];

    @Override
    public void decode() {
        this.type = (byte) getByte();
        switch (type) {
            case TYPE_POPUP:
            case TYPE_CHAT:
            case TYPE_WHISPER:
            case TYPE_ANNOUNCEMENT:
                this.source = this.getString();
            case TYPE_RAW:
            case TYPE_TIP:
            case TYPE_SYSTEM:
                this.message = this.getString();
                break;

            case TYPE_TRANSLATION:
                this.message = this.getString();
                int count = (int) this.getUnsignedVarInt();
                this.parameters = new String[count];
                for (int i = 0; i < count; i++) {
                    this.parameters[i] = this.getString();
                }
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.type);
        switch (this.type) {
            case TYPE_POPUP:
            case TYPE_CHAT:
            case TYPE_WHISPER:
            case TYPE_ANNOUNCEMENT:
                this.putString(this.source);
            case TYPE_RAW:
            case TYPE_TIP:
            case TYPE_SYSTEM:
                this.putString(this.message);
                break;
            case TYPE_TRANSLATION:
                this.putString(this.message);
                this.putUnsignedVarInt(this.parameters.length);
                for (String parameter : this.parameters) {
                    this.putString(parameter);
                }
        }
    }

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.TextPacket.class);

        cn.nukkit.network.protocol.TextPacket packet = (cn.nukkit.network.protocol.TextPacket) pk;
        this.type = packet.type;
        this.source = packet.source;
        this.message = packet.message;
        this.parameters = packet.parameters;
        return this;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.TextPacket pk = new cn.nukkit.network.protocol.TextPacket();
        pk.type = this.type;
        pk.source = this.source;
        pk.message = this.message;
        pk.parameters = this.parameters;
        return pk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.TextPacket.class;
    }
}
