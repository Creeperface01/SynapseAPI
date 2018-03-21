package org.itxtech.synapseapi.multiprotocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.BinaryStream;
import org.itxtech.synapseapi.multiprotocol.protocol1210.protocol.MVBinaryStream;

/**
 * @author CreeperFace
 */
public abstract class PacketDecoder<T extends DataPacket> extends MVBinaryStream {

    public abstract void decode(ProtocolGroup group, T pk);

    public abstract byte[] encode(ProtocolGroup group, T pk);

    @Override
    public void reset() {
        super.reset();
        this.putByte(this.pid());
        this.putShort(0);
    }

    public abstract byte pid();
}
