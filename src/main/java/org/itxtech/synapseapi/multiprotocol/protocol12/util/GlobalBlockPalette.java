package org.itxtech.synapseapi.multiprotocol.protocol12.util;

import cn.nukkit.utils.MainLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import org.itxtech.synapseapi.SynapseAPI;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author CreeperFace
 */
public class GlobalBlockPalette {

    private static final Map<ProtocolGroup, VersionEntry> versions = new EnumMap<>(ProtocolGroup.class);

    static {
        //Pattern pattern = Pattern.compile("^runtimeid_table_(\\d+)\\.json$", Pattern.CASE_INSENSITIVE);

        try {
            Gson gson = new Gson();

            for (ProtocolGroup group : ProtocolGroup.values()) {
                if (group.ordinal() < ProtocolGroup.PROTOCOL_1213.ordinal())
                    continue;

                InputStream stream = SynapseAPI.class.getClassLoader().getResourceAsStream("runtimeid_tables/runtimeid_table_" + group.name().substring(9) + ".json");

                Reader reader = new InputStreamReader(stream, "UTF-8");
                Type collectionType = new TypeToken<Collection<TableEntry>>() {
                }.getType();
                Collection<TableEntry> entries = gson.fromJson(reader, collectionType);

                for (TableEntry entry : entries) {
                    registerMapping(group, entry.runtimeID, (entry.id << 4) | entry.data);
                }
            }
        } catch (IOException e) {
            MainLogger.getLogger().logException(e);
        }
    }

    public static int getOrCreateRuntimeId(ProtocolGroup group, int id, int meta) {
        return getOrCreateRuntimeId(group, (id << 4) | meta);
    }

    public static int getOrCreateRuntimeId(ProtocolGroup group, int legacyId) {
        VersionEntry entry = versions.get(group);

        int runtimeId = entry.legacyToRuntimeId.get(legacyId);
        if (runtimeId == -1) {
            runtimeId = registerMapping(group, entry.runtimeIdAllocator.incrementAndGet(), legacyId);
            MainLogger.getLogger().warning("Unmapped block registered (" + legacyId + "). May not be recognised client-side");
        }
        return runtimeId;
    }

    private static int registerMapping(ProtocolGroup group, int runtimeId, int legacyId) {
        VersionEntry versionEntry = versions.get(group);
        if (versionEntry == null) {
            versionEntry = new VersionEntry();
            versions.put(group, versionEntry);
        }

        versionEntry.runtimeIdToLegacy.put(runtimeId, legacyId);
        versionEntry.legacyToRuntimeId.put(legacyId, runtimeId);
        versionEntry.runtimeIdAllocator.set(Math.max(versionEntry.runtimeIdAllocator.get(), runtimeId));

        return runtimeId;
    }

    private static class TableEntry {
        private int id;
        private int data;
        private int runtimeID;
        private String name;
    }

    private static class VersionEntry {
        private final Int2IntArrayMap legacyToRuntimeId = new Int2IntArrayMap();
        private final Int2IntArrayMap runtimeIdToLegacy = new Int2IntArrayMap();
        private final AtomicInteger runtimeIdAllocator = new AtomicInteger(0);

        {
            legacyToRuntimeId.defaultReturnValue(-1);
            runtimeIdToLegacy.defaultReturnValue(-1);
        }
    }
}
