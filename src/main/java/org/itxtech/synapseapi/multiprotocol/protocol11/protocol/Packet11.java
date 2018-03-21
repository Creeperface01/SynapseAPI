package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.multiprotocol.protocol11.item.Item11;

/**
 * @author CreeperFace
 */
public abstract class Packet11 extends DataPacket {

    public DataPacket toDefault() {
        return null;
    }

    public DataPacket fromDefault(DataPacket pk) {
        return null;
    }

    static Class<? extends DataPacket> getDefaultPacket() {
        return null;
    }

    public boolean needConversion() {
        return true;
    }

    boolean blockShort = false;

    @Override
    public void reset() {
        blockShort = true;
        super.reset();
        blockShort = false;
    }

    @Override
    public void putShort(int s) {
        if (!blockShort) {
            super.putShort(s);
        }
    }

    @Override
    public void putSlot(Item item) {
        item = Item11.check(item);

        super.putSlot(item);
    }
}
