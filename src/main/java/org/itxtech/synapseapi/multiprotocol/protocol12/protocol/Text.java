package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.network.protocol.TextPacket;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class Text extends PacketDecoder<TextPacket> {

    @Override
    public byte pid() {
        return 0x09;
    }

    @Override
    public byte[] encode(ProtocolGroup group, TextPacket pk) {
        this.reset();
        this.putByte(pk.type);
        this.putBoolean(pk.isLocalized || pk.type == TextPacket.TYPE_TRANSLATION);
        switch (pk.type) {
            case TextPacket.TYPE_POPUP:
            case TextPacket.TYPE_CHAT:
            case TextPacket.TYPE_WHISPER:
            case TextPacket.TYPE_ANNOUNCEMENT:
                this.putString(pk.source);
                if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
                    putString(""); //third party name
                    putVarInt(0); //platform id
                }
            case TextPacket.TYPE_RAW:
            case TextPacket.TYPE_TIP:
            case TextPacket.TYPE_SYSTEM:
                this.putString(pk.message);
                break;
            case TextPacket.TYPE_TRANSLATION:
                this.putString(pk.message);
                this.putUnsignedVarInt(pk.parameters.length);
                for (String parameter : pk.parameters) {
                    this.putString(parameter);
                }

                /*if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
                    this.putString(""); //third party name
                    putVarInt(0); //platform id
                }*/
        }

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            putString(""); //platform id
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, TextPacket pk) {
        pk.type = (byte) getByte();
        pk.isLocalized = this.getBoolean() || pk.type == TextPacket.TYPE_TRANSLATION;
        switch (pk.type) {
            case TextPacket.TYPE_POPUP:
            case TextPacket.TYPE_CHAT:
            case TextPacket.TYPE_WHISPER:
            case TextPacket.TYPE_ANNOUNCEMENT:
                pk.source = this.getString();

                if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
                    getString();
                    getVarInt();
                }
            case TextPacket.TYPE_RAW:
            case TextPacket.TYPE_TIP:
            case TextPacket.TYPE_SYSTEM:
                pk.message = this.getString();
                break;
            case TextPacket.TYPE_TRANSLATION:
                pk.message = this.getString();
                int count = (int) this.getUnsignedVarInt();
                pk.parameters = new String[count];
                for (int i = 0; i < count; i++) {
                    pk.parameters[i] = this.getString();
                }
        }

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            getString();
        }
    }
}
