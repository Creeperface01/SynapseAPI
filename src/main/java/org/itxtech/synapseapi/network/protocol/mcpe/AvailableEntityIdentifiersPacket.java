package org.itxtech.synapseapi.network.protocol.mcpe;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.MainLogger;
import com.google.common.io.ByteStreams;
import org.itxtech.synapseapi.SynapseAPI;

import java.io.IOException;

/**
 * @author CreeperFace
 */
public class AvailableEntityIdentifiersPacket extends DataPacket {

    public static final byte NETWORK_ID = 0x77;

    private static final byte[] TAG;

    static {
        try {
            //noinspection UnstableApiUsage
            TAG = ByteStreams.toByteArray(SynapseAPI.class.getClassLoader().getResourceAsStream("entity_identifiers/entity_identifiers_18.dat"));
        } catch (NullPointerException | IOException e) {
            MainLogger.getLogger().logException(e);
            throw new AssertionError("Unable to load entity_identifiers_net.dat");
        }
    }

    public byte[] tag = TAG;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.tag = this.get();
    }

    @Override
    public void encode() {
        this.reset();
        this.put(this.tag);
    }
}
