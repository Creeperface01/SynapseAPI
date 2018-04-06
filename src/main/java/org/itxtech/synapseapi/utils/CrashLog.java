package org.itxtech.synapseapi.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.*;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.MainLogger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author CreeperFace
 */
public class CrashLog {

    public static final int CRASH_DATA = -1;

    public long lastTest = 0;
    public Queue<Entry> sentPackets = new ConcurrentLinkedQueue<>();

    public void update() {
        long time = System.currentTimeMillis();
        if (time - this.lastTest >= 1000) {
            this.sentPackets.clear();
            this.lastTest = time;
        }
    }

    public void checkPacket(DataPacket packet) {
        switch (packet.pid()) {
            case ProtocolInfo.MOVE_ENTITY_PACKET:
            case ProtocolInfo.MOVE_PLAYER_PACKET:
            case ProtocolInfo.FULL_CHUNK_DATA_PACKET:
            case ProtocolInfo.SET_TIME_PACKET:
                //case ProtocolInfo.LEVEL_EVENT_PACKET:
                break;
            default:
                sentPackets.add(new Entry(packet));
                break;
        }
    }

    public void post(Player p) {
        if (sentPackets.isEmpty()) {
            return;
        }

        //save entities
        Deque<DataPacket> entities = new ArrayDeque<>();
        for (Player pl : p.getLevel().getPlayers().values()) {
            if (pl.getViewers().containsKey(p.getLoaderId())) {
                AddPlayerPacket pk = new AddPlayerPacket();
                pk.uuid = pl.getUniqueId();
                pk.username = pl.getName();
                pk.entityUniqueId = pl.getId();
                pk.entityRuntimeId = pl.getId();
                pk.x = (float) pl.x;
                pk.y = (float) pl.y;
                pk.z = (float) pl.z;
                pk.speedX = (float) pl.motionX;
                pk.speedY = (float) pl.motionY;
                pk.speedZ = (float) pl.motionZ;
                pk.yaw = (float) pl.yaw;
                pk.pitch = (float) pl.pitch;
                pk.item = pl.getInventory().getItemInHand();
                pk.metadata = pl.getDataProperties();

                PlayerListPacket pk2 = new PlayerListPacket();
                pk2.type = PlayerListPacket.TYPE_ADD;
                pk2.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(pl.getUniqueId(), pl.getId(), pl.getName(), pl.getSkin(), pl.getLoginChainData().getXUID())};

                MobArmorEquipmentPacket pk1 = new MobArmorEquipmentPacket();
                pk1.eid = pl.getId();
                pk1.slots = pl.getInventory().getArmorContents();

                entities.add(pk);
                entities.add(pk1);
                entities.add(pk2);
            }
        }

        /*for(Entity entity : p.getLevel().getEntities()) {
            if(entity.getViewers().containsKey(p.getLoaderId())) {

            }
        }*/

        Server.getInstance().getScheduler().scheduleAsyncTask(new AsyncTask() {
            @Override
            public void onRun() {
                try {
                    File file = new File(Server.getInstance().getDataPath() + "players/" + p.getName() + "    " + System.currentTimeMillis() + ".txt");
                    file.createNewFile();

                    DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));

                    stream.writeLong(lastTest); //last packet sent time

                    //entities
                    stream.writeInt(entities.size());
                    System.out.println("entities: " + entities.size());
                    while (!entities.isEmpty()) {
                        DataPacket packet = entities.poll();
                        packet.encode();

                        byte[] buffer = packet.getBuffer();
                        stream.writeInt(buffer.length);
                        stream.write(buffer);
                    }

                    int count = sentPackets.size();
                    stream.writeInt(count);

                    //System.out.println("count: "+count);
                    for (int i = 0; i < count; i++) {
                        Entry entry = sentPackets.poll();
                        DataPacket pk = entry.packet;
                        long time = entry.time;

                        stream.writeLong(time);
                        byte[] buffer = pk instanceof BatchPacket ? ((BatchPacket) pk).payload : pk.getBuffer();
                        stream.writeInt(buffer.length);
                        stream.write(buffer);
                    }

                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    MainLogger.getLogger().logException(e);
                }
            }
        });
    }

    private class Entry {

        private DataPacket packet;
        private long time = System.currentTimeMillis();

        public Entry(DataPacket pk) {
            this.packet = pk.clone();
        }
    }
}