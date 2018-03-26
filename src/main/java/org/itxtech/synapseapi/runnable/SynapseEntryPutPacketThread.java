package org.itxtech.synapseapi.runnable;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Zlib;
import org.itxtech.synapseapi.SynapseAPI;
import org.itxtech.synapseapi.SynapsePlayer;
import org.itxtech.synapseapi.multiprotocol.PacketRegister;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.Packet11;
import org.itxtech.synapseapi.network.SynapseInterface;
import org.itxtech.synapseapi.network.protocol.spp.RedirectPacket;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.Deflater;

/**
 * org.itxtech.synapseapi.runnable
 * ===============
 * author: boybook
 * SynapseAPI Project
 * itxTech
 * ===============
 */
public class SynapseEntryPutPacketThread extends Thread {

    private final SynapseInterface synapseInterface;
    private final Queue<Entry> queue = new LinkedBlockingQueue<>();
    private final Queue<BroadcastEntry> broadcastQueue = new LinkedBlockingQueue<>();

    private final Deflater deflater = new Deflater(Server.getInstance().networkCompressionLevel);
    private final byte[] buf = new byte[1024];

    private final boolean isAutoCompress;
    private long tickUseTime = 0;
    private boolean isRunning = true;

    public SynapseEntryPutPacketThread(SynapseInterface synapseInterface) {
        super("SynapseEntryPutPacketThread");
        this.synapseInterface = synapseInterface;
        this.isAutoCompress = SynapseAPI.getInstance().isAutoCompress();
        this.start();
    }

    public void addMainToThread(SynapsePlayer player, DataPacket packet, boolean needACK, boolean immediate) {
        this.queue.offer(new Entry(player, packet, needACK, immediate));
        //Server.getInstance().getLogger().debug("SynapseEntryPutPacketThread Offer: " + packet.getClass().getSimpleName());
    }

    public void addMainToThreadBroadcast(Player[] players, DataPacket[] packets) {
        if(players.length == 0 || packets.length == 0) {
            return;
        }

        this.broadcastQueue.offer(new BroadcastEntry(players, packets));
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        while (this.isRunning) {
            long start = System.currentTimeMillis();
            Entry entry;

            packetloop:
            while ((entry = queue.poll()) != null) {
                try {
                    if (!entry.player.closed || entry.immediate || entry.player.transfering) {
                        RedirectPacket pk = new RedirectPacket();
                        pk.uuid = entry.player.getUniqueId();
                        pk.direct = entry.immediate;

                        /*if(entry.packet.pid() == UpdateAttributesPacket.NETWORK_ID) {
                            MainLogger.getLogger().info("ENTITY: "+((UpdateAttributesPacket) entry.packet).entityId);
                        }*/

                        //MainLogger.getLogger().notice("PACKET: "+entry.packet.getClass().getName());
                        entry.packet = PacketRegister.getCompatiblePacket(entry.packet, ((SynapsePlayer) entry.player).getProtocolGroup(), true);

                        if (entry.packet == null) {
                            //MainLogger.getLogger().info("NULL PACKET");
                            continue;
                        }

                        /*int pid = entry.packet.pid();
                        switch (pid) {
                            case ProtocolInfo.ADD_ENTITY_PACKET:
                            case ProtocolInfo.MOVE_ENTITY_PACKET:
                            case ProtocolInfo.SET_ENTITY_DATA_PACKET:
                            case ProtocolInfo.SET_ENTITY_MOTION_PACKET:
                            case ProtocolInfo.LEVEL_EVENT_PACKET:
                            case ProtocolInfo.LEVEL_SOUND_EVENT_PACKET:
                            case ProtocolInfo.ADD_ITEM_ENTITY_PACKET:
                            case ProtocolInfo.UPDATE_ATTRIBUTES_PACKET:
                            case ProtocolInfo.TEXT_PACKET:
                            case ProtocolInfo.SET_TIME_PACKET:
                            case ProtocolInfo.BATCH_PACKET:
                                continue;
                        }*/

                        /*if(entry.packet.pid() != BatchPacket.NETWORK_ID && entry.packet.pid() != UpdateBlockPacket.NETWORK_ID) {
                            MainLogger.getLogger().info("PACKET: " + entry.packet.getClass().getName());
                        }*/

                        //entry.packet = DataPacketEidReplacer.replace(entry.packet, entry.player.getId(), Long.MAX_VALUE);
                        //entry.packet = DataPacketEidReplacer.replace(entry.packet, this.getId(), Long.MAX_VALUE);

                        /*if(entry.packet.pid() == UpdateAttributesPacket.NETWORK_ID) {
                            System.out.println("ID: "+((UpdateAttributesPacket) entry.packet).entityId);
                        }*/
                        //System.out.println("packet: "+entry.packet.getClass().getName());

                        ProtocolGroup protocol = ((SynapsePlayer)entry.player).getProtocolGroup();

                        /*switch (entry.packet.pid()) {
                            case PlayerListPacket.NETWORK_ID:
                            case AddPlayerPacket.NETWORK_ID:
                                continue packetloop;
                        }*/

                        if (!entry.packet.isEncoded) {
                            PacketRegister.encodePacket(entry.packet, protocol);
                            //entry.packet.encode();
                            entry.packet.isEncoded = true;
                        }

                        /*if(entry.packet instanceof BatchPacket) {
                            for(DataPacket packet : PacketRegister.decodeBatch((BatchPacket) entry.packet)) {
                                MainLogger.getLogger().notice("BATCH PACKET: "+packet.getClass().getName());
                            }
                        } else {*/
                            //MainLogger.getLogger().notice("SEND PACKET: "+entry.packet.getClass().getName());
                        //}

                        if(protocol != ProtocolGroup.PROTOCOL_11 && entry.packet instanceof Packet11) {
                            MainLogger.getLogger().warning("SENDING 1.1 PACKET '"+entry.packet.getClass().getName()+"' TO PLAYER '"+entry.player.getName()+"' with protocol: "+entry.player.getProtocol());
                        }

                        if (!(entry.packet instanceof BatchPacket) && this.isAutoCompress) {
                            byte[] buffer = entry.packet.getBuffer();
                            try {
                                buffer = deflate(
                                        Binary.appendBytes(Binary.writeUnsignedVarInt(buffer.length), buffer),
                                        Server.getInstance().networkCompressionLevel);
                                pk.mcpeBuffer = Binary.appendBytes((byte) 0xfe, buffer);
                                /*if (entry.packet.pid() == ProtocolInfo.RESOURCE_PACKS_INFO_PACKET)
                                    Server.getInstance().getLogger().notice("ResourcePacksInfoPacket length=" + buffer.length + " " + Binary.bytesToHexString(buffer));*/
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            //Server.getInstance().getLogger().notice("S => C  " + entry.packet.getClass().getSimpleName());
                        } else {
                            pk.mcpeBuffer = entry.packet instanceof BatchPacket ? Binary.appendBytes((byte) 0xfe, ((BatchPacket) entry.packet).payload) : entry.packet.getBuffer();
                        }
                        this.synapseInterface.putPacket(pk);

                        if(((SynapsePlayer) entry.player).logPackets.get()) { //TODO: remove
                            ((SynapsePlayer) entry.player).crashLog.checkPacket(entry.packet);
                        }
                        //Server.getInstance().getLogger().warning("SynapseEntryPutPacketThread PutPacket");
                        /*try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {

                        }*/
                    }
                } catch (Exception e) {
                    Server.getInstance().getLogger().alert("Catch exception when Synapse Entry Put Packet: " + e.getMessage());
                    Server.getInstance().getLogger().logException(e);
                }
            }

            BroadcastEntry entry1;
            while ((entry1 = broadcastQueue.poll()) != null) {
                try {
                    Map<ProtocolGroup, List<DataPacket>> packets = new EnumMap<>(ProtocolGroup.class);
                    List<DataPacket> batch = new ArrayList<>();
                    for(Player p : entry1.player) {
                        packets.putIfAbsent(((SynapsePlayer)p).getProtocolGroup(), new ArrayList<>());
                    }

                    for(DataPacket targetPk : entry1.packet) {
                        /*RedirectPacket pk = new RedirectPacket();
                        pk.uuid = entry.player.getUniqueId();
                        pk.direct = entry.immediate;*/

                        if (targetPk.pid() == BatchPacket.NETWORK_ID) {
                            batch.add(targetPk);
                            continue;
                        }

                        for(ProtocolGroup protocol : packets.keySet()) {
                            DataPacket packet = PacketRegister.getCompatiblePacket(targetPk, protocol, true);

                            if(packet != null)
                                packets.get(protocol).add(packet);
                        }
                        /* packet = PacketRegister.getCompatiblePacket(targetPk, ProtocolInfo.CURRENT_PROTOCOL, true);

                        //System.out.println("packet: "+packet.getClass().getName());
                        if(packet != null) {
                            packets.add(packet);
                        }

                        packet = PacketRegister.getCompatiblePacket(targetPk, org.itxtech.synapseapi.multiprotocol.protocol11.protocol.ProtocolInfo.CURRENT_PROTOCOL, true);

                        if(packet != null) {
                            packets11.add(packet);
                        }*/
                    }

                    Map<ProtocolGroup, byte[]> protocolMap = new EnumMap<>(ProtocolGroup.class);
                    for(Map.Entry<ProtocolGroup, List<DataPacket>> pkEntry : packets.entrySet()) {
                        BatchPacket packet = batchPackets(pkEntry.getValue().stream().toArray(DataPacket[]::new), pkEntry.getKey());

                        if(packet != null) {
                            protocolMap.put(pkEntry.getKey(), Binary.appendBytes((byte) 0xfe, packet.payload));
                        }
                    }

                    /*BatchPacket packet = batchPackets(packets.stream().toArray(DataPacket[]::new));
                    BatchPacket packet11 = batchPackets(packets11.stream().toArray(DataPacket[]::new));

                    byte[] data = packet != null ? Binary.appendBytes((byte) 0xfe, packet.payload) : null;
                    byte[] data11 = packet11 != null ? Binary.appendBytes((byte) 0xfe, packet11.payload) : null;*/

                    for(Player player : entry1.player) {
                        SynapsePlayer sPlayer = (SynapsePlayer) player;
                        /*if(player.closed) {
                            continue;
                        }

                        byte[] dat;
                        if(player.getProtocol() <= 113) {
                            if(data11 == null) {
                                continue;
                            }
                            if(((SynapsePlayer) player).logPackets.get()) { //TODO: remove
                                for(DataPacket pack : packets11) {
                                    ((SynapsePlayer) player).crashLog.checkPacket(pack);
                                }
                            }

                            dat = data11;
                        } else {
                            if(data == null) {
                                continue;
                            }

                            if(((SynapsePlayer) player).logPackets.get()) { //TODO: remove
                                for(DataPacket pack : packets) {
                                    ((SynapsePlayer) player).crashLog.checkPacket(pack);
                                }
                            }
                            dat = data;
                        }*/
                        byte[] data = protocolMap.get(sPlayer.getProtocolGroup());

                        if(data == null) {
                            continue;
                        }

                        if(sPlayer.logPackets.get()) { //TODO: remove
                            for(DataPacket pack : packets.get(sPlayer.getProtocolGroup())) {
                                ((SynapsePlayer) player).crashLog.checkPacket(pack);
                            }
                        }

                        RedirectPacket pk = new RedirectPacket();
                        pk.protocol = player.getProtocol();
                        pk.uuid = player.getUniqueId();
                        pk.mcpeBuffer = data;
                        this.synapseInterface.putPacket(pk);
                    }
                } catch (Exception e) {
                    Server.getInstance().getLogger().alert("Catch exception when Synapse Entry Put Packet: " + e.getMessage());
                    Server.getInstance().getLogger().logException(e);
                }
            }

            tickUseTime = System.currentTimeMillis() - start;
            if (tickUseTime < 10){
                try {
                    Thread.sleep(10 - tickUseTime);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        }
    }

    private byte[] deflate(byte[] data, int level) throws Exception {
        if (deflater == null) throw new IllegalArgumentException("No deflate for level "+level+" !");
        deflater.reset();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        while (!deflater.finished()) {
            int i = deflater.deflate(buf);
            bos.write(buf, 0, i);
        }
        //Deflater::end is called the time when the process exits.
        return bos.toByteArray();
    }

    private static class Entry {
        private SynapsePlayer player;
        private DataPacket packet;
        private boolean needACK;
        private boolean immediate;

        public Entry(SynapsePlayer player, DataPacket packet, boolean needACK, boolean immediate) {
            this.player = player;
            this.packet = packet;
            this.needACK = needACK;
            this.immediate = immediate;
        }
    }

    private class BroadcastEntry {
        private Player[] player;
        private DataPacket[] packet;

        public BroadcastEntry(Player[] player, DataPacket[] packet) {
            this.player = player;
            this.packet = packet;
        }
    }

    public double getTicksPerSecond() {
        long more = this.tickUseTime - 10;
        if (more < 0) return 100;
        return NukkitMath.round(10f / (double)this.tickUseTime, 3) * 100;
    }

    private BatchPacket batchPackets(DataPacket[] packets, ProtocolGroup protocol) {
        try {
            byte[][] payload = new byte[packets.length * 2][];
            for (int i = 0; i < packets.length; i++) {
                DataPacket p = packets[i];

                if (!p.isEncoded) {
                    PacketRegister.encodePacket(p, protocol);
                    p.isEncoded = true;
                    //p.encode();
                }
                byte[] buf = p.getBuffer();
                payload[i * 2] = Binary.writeUnsignedVarInt(buf.length);
                payload[i * 2 + 1] = buf;
            }
            byte[] data;
            data = Binary.appendBytes(payload);

            byte[] compressed = Zlib.deflate(data, Server.getInstance().networkCompressionLevel);
            BatchPacket packet = new BatchPacket();
            packet.payload = compressed;

            return packet;
        } catch (Exception e) {
            MainLogger.getLogger().logException(e);
        }

        return null;
    }
}
