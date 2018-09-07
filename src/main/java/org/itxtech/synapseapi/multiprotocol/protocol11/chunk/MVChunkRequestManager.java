package org.itxtech.synapseapi.multiprotocol.protocol11.chunk;

import cn.nukkit.Player;
import cn.nukkit.level.ChunkRequestManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.ChunkRequestTask;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.scheduler.AsyncTask;
import com.google.common.cache.CacheBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import lombok.AllArgsConstructor;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author CreeperFace
 */
public class MVChunkRequestManager implements ChunkRequestManager {

    private final Level level;

    private final Long2ObjectMap<Map<ProtocolGroup, Player>> chunkSendQueue = new Long2ObjectOpenHashMap<>();
    private final LongSet chunkSendTasks = new LongOpenHashSet();

    private final ConcurrentMap<Long, Map<ProtocolGroup, DataPacket>> chunkCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .<Long, Map<ProtocolGroup, DataPacket>>build().asMap();

    public MVChunkRequestManager(Level level) {
        this.level = level;
    }

    @Override
    public void process() {
        if (!this.chunkSendQueue.isEmpty()) {
            this.level.timings.syncChunkSendTimer.startTiming();
            for (Map.Entry<Long, Map<ProtocolGroup, Player>> entry : new ArrayList<>(this.chunkSendQueue.entrySet())) {
                Long index = entry.getKey();

                if (this.chunkSendTasks.contains(index)) {
                    continue;
                }
                int x = Level.getHashX(index);
                int z = Level.getHashZ(index);
                this.chunkSendTasks.add(index);
                if (this.chunkCache.containsKey(index)) {
                    this.sendChunkFromCache(x, z);
                    continue;
                }
                this.level.timings.syncChunkSendPrepareTimer.startTiming();
                AsyncTask task = this.level.getProvider().requestChunkTask(x, z);
                if (task != null) {
                    this.level.getServer().getScheduler().scheduleAsyncTask(task);
                }
                this.level.timings.syncChunkSendPrepareTimer.stopTiming();
            }
            this.level.timings.syncChunkSendTimer.stopTiming();
        }
    }

    private void sendChunkFromCache(int x, int z) {
        Long index = Level.chunkHash(x, z);
        if (this.chunkSendTasks.contains(index)) {
            Map<ProtocolGroup, DataPacket> packets = this.chunkCache.get(index);

            for (Player player : this.chunkSendQueue.get(index).values()) {
                if (player.isConnected() && player.usedChunks.containsKey(index)) {
                    DataPacket pk = select(player, packets);

                    player.sendChunk(x, z, pk);
                }
            }

            this.chunkSendQueue.remove(index);
            this.chunkSendTasks.remove(index);
        }
    }

    @Override
    public void requestChunk(long index, Player player) {
        if (!this.chunkSendQueue.containsKey(index)) {
            this.chunkSendQueue.put(index, new HashMap<>());
        }

        this.chunkSendQueue.get(index).put(ProtocolGroup.getOrDefault(player), player);
    }

    @Override
    public void requestCallback(ChunkRequestTask.Result result) {
        this.level.timings.syncChunkSendTimer.startTiming();
        int x = result.x;
        int z = result.z;

        if (!(result instanceof ChunkResult)) {
            throw new UnsupportedOperationException("Invalid chunk result");
        }

        ChunkResult chunkResult = (ChunkResult) result;

        Long index = Level.chunkHash(x, z);

        if (this.level.isCacheChunks() && !this.chunkCache.containsKey(index)) {
            this.chunkCache.put(index, chunkResult.getPayloads());
            this.sendChunkFromCache(x, z);
            this.level.timings.syncChunkSendTimer.stopTiming();
            return;
        }

        if (this.chunkSendTasks.contains(index)) {


            for (Player player : this.chunkSendQueue.get(index).values()) {
                if (player.isConnected() && player.usedChunks.containsKey(index)) {
                    player.sendChunk(x, z, select(player, chunkResult.getPayloads()));
                }
            }

            this.chunkSendQueue.remove(index);
            this.chunkSendTasks.remove(index);
        }

        this.level.timings.syncChunkSendTimer.stopTiming();
    }

    @Override
    public void onChunkChanged(long index) {
        this.chunkCache.remove(index);
    }

    @Override
    public void clearCache(boolean full) {
        if (full) {
            this.chunkCache.clear();
        } else {
            if (this.chunkCache.size() > 2048) {
                this.chunkCache.clear();
            }
        }
    }

    private DataPacket select(Player p, Map<ProtocolGroup, DataPacket> packets) {
        ProtocolGroup group = ProtocolGroup.getOrDefault(p);
        DataPacket pk;

        if (group == ProtocolGroup.PROTOCOL_11) {
            pk = packets.get(group);
        } else if (group.ordinal() > ProtocolGroup.PROTOCOL_11.ordinal() && group.ordinal() < ProtocolGroup.PROTOCOL_16.ordinal()) {
            pk = packets.get(ProtocolGroup.PROTOCOL_12);
        } else {
            pk = packets.get(ProtocolGroup.PROTOCOL_16);
        }

        return pk;
    }

    public static void init() {
        Level.setChunkRequestManagerFactory(MVChunkRequestManager::new);
    }

    @AllArgsConstructor
    private static class CachedChunk {

        private final byte[] payload;
        private final byte[] payload11;

    }
}
