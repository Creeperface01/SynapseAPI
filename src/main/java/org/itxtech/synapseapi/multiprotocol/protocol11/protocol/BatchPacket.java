package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

/**
 * @author CreeperFace
 */
public class BatchPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.BATCH_PACKET;
    public byte[] payload;

    public BatchPacket() {
    }

    public byte pid() {
        return NETWORK_ID;
    }

    public void decode() {
        this.payload = this.get();
    }

    public void encode() {
    }
}
