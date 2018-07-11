package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.MoveEntityPacket;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class MoveEntity extends PacketDecoder<MoveEntityPacket> {

    @Override
    public byte pid() {
        return MoveEntityPacket.NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, MoveEntityPacket pk) {
        this.reset();

        this.putEntityRuntimeId(pk.eid);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_15.ordinal()) {
            byte flags = 0;

            if (pk.teleport) {
                flags |= 0x01;
            }
            if (pk.onGround) {
                flags |= 0x02;
            }

            this.putByte(flags);
        }

        this.putVector3f((float) pk.x, (float) pk.y, (float) pk.z);
        this.putByte((byte) ((int) (pk.pitch / 1.40625D)));
        this.putByte((byte) ((int) (pk.headYaw / 1.40625D)));
        this.putByte((byte) ((int) (pk.yaw / 1.40625D)));

        if (group.ordinal() < ProtocolGroup.PROTOCOL_15.ordinal()) {
            this.putBoolean(pk.onGround);
            this.putBoolean(pk.teleport);
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, MoveEntityPacket pk) {
        pk.eid = this.getEntityRuntimeId();

        Vector3f v = this.getVector3f();

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_15.ordinal()) {
            int flags = getByte();
            pk.teleport = (flags & 0x01) != 0;
            pk.onGround = (flags & 0x02) != 0;
        }

        pk.x = (double) v.x;
        pk.y = (double) v.y;
        pk.z = (double) v.z;
        pk.pitch = (double) this.getByte() * 1.40625D;
        pk.headYaw = (double) this.getByte() * 1.40625D;
        pk.yaw = (double) this.getByte() * 1.40625D;

        if (group.ordinal() < ProtocolGroup.PROTOCOL_15.ordinal()) {
            pk.onGround = this.getBoolean();
            pk.teleport = this.getBoolean();
        }
    }
}
