package org.itxtech.synapseapi;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.BatchPacketsEvent;
import cn.nukkit.network.RakNetInterface;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.ConfigSection;
import org.itxtech.synapseapi.messaging.Messenger;
import org.itxtech.synapseapi.messaging.StandardMessenger;
import org.itxtech.synapseapi.multiprotocol.PacketRegister;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;
import org.itxtech.synapseapi.multiprotocol.protocol11.chunk.ChunkCompressor;
import org.itxtech.synapseapi.multiprotocol.protocol11.chunk.MVChunkRequestManager;
import org.itxtech.synapseapi.multiprotocol.protocol11.inventory.crafting.CraftingManager11;
import org.itxtech.synapseapi.multiprotocol.protocol11.item.Item11;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.ProtocolInfo;
import org.itxtech.synapseapi.multiprotocol.protocol12.util.GlobalBlockPalette;
import org.itxtech.synapseapi.network.protocol.mcpe.SetHealthPacket;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author boybook
 */
public class SynapseAPI extends PluginBase implements Listener {

    public static boolean enable = true;
    private static SynapseAPI instance;
    private boolean autoConnect = true;
    private boolean loadingScreen = true;
    private boolean autoCompress = true;  //Compress in Nukkit, not Nemisys
    private Map<String, SynapseEntry> synapseEntries = new HashMap<>();
    private Messenger messenger;

    public static SynapseAPI getInstance() {
        return instance;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    private CraftingManager11 craftingManager;

    @Override
    public void onLoad() {
        instance = this;

        GlobalBlockPalette.getOrCreateRuntimeId(ProtocolGroup.PROTOCOL_1213, 0, 0); //Force it to load
    }

    @Override
    public void onEnable() {
        this.getServer().getNetwork().registerPacket(ProtocolInfo.SET_HEALTH_PACKET, SetHealthPacket.class);
        this.messenger = new StandardMessenger();
        loadEntries();

        this.getServer().getPluginManager().registerEvents(this, this);

        saveResource("recipes11.json", true);
        ChunkCompressor.init();
        MVChunkRequestManager.init();
        PacketRegister.init();
        this.craftingManager = new CraftingManager11(this);
        Item11.init();
    }

    public CraftingManager11 getCraftingManager() {
        return craftingManager;
    }

    public boolean isUseLoadingScreen() {
        return loadingScreen;
    }

    public boolean isAutoCompress() {
        return autoCompress;
    }

    public Map<String, SynapseEntry> getSynapseEntries() {
        return synapseEntries;
    }

    public void addSynapseAPI(SynapseEntry entry) {
        this.synapseEntries.put(entry.getHash(), entry);
    }

    public SynapseEntry getSynapseEntry(String hash) {
        return this.synapseEntries.get(hash);
    }

    public void shutdownAll() {
        /*for (Player p : getServer().getOnlinePlayers().values()) {
            if (p instanceof SynapsePlayer) {
                ((SynapsePlayer) p).transferToLobby();
            }
        }*/

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {

        }

        for (SynapseEntry entry : new ArrayList<>(this.synapseEntries.values())) {

            entry.shutdown();
        }
    }

    @Override
    public void onDisable() {
        this.shutdownAll();
    }

    public DataPacket getPacket(byte[] buffer) {
        byte pid = buffer[0] == (byte) 0xfe ? (byte) 0xff : buffer[0];

        byte start = 3;
        DataPacket data;
        data = this.getServer().getNetwork().getPacket(pid);

        if (data == null) {
            Server.getInstance().getLogger().notice("C => S 未找到匹配数据包");
            return null;
        }
        data.setBuffer(buffer, start);
        return data;
    }

    private void loadEntries() {
        this.saveDefaultConfig();
        enable = this.getConfig().getBoolean("enable", true);
        this.autoCompress = this.getConfig().getBoolean("autoCompress", true);
        if (!enable) {
            this.getLogger().warning("The SynapseAPI is not be enabled!");
            this.setEnabled(false);
        } else {
            if (this.getConfig().getBoolean("disable-rak")) {
                for (SourceInterface sourceInterface : this.getServer().getNetwork().getInterfaces()) {
                    if (sourceInterface instanceof RakNetInterface) {
                        sourceInterface.shutdown();
                    }
                }
            }

            List entries = this.getConfig().getList("entries");

            for (Object entry : entries) {
                @SuppressWarnings("unchecked")
                ConfigSection section = new ConfigSection((LinkedHashMap) entry);
                String serverIp = section.getString("server-ip", "127.0.0.1");
                int port = section.getInt("server-port", 10305);
                boolean isMainServer = section.getBoolean("isMainServer");
                boolean isLobbyServer = section.getBoolean("isLobbyServer");
                boolean transfer = section.getBoolean("transferOnShutdown", true);
                String password = section.getString("password");
                String serverDescription = section.getString("description");
                this.loadingScreen = section.getBoolean("loadingScreen", true);
                this.autoConnect = section.getBoolean("autoConnect", true);
                if (this.autoConnect) {
                    this.addSynapseAPI(new SynapseEntry(this, serverIp, port, isMainServer, isLobbyServer, transfer, password, serverDescription));
                }
            }

        }
    }

    public Messenger getMessenger() {
        return messenger;
    }

    @EventHandler
    public void onBatchPackets(BatchPacketsEvent e) {
        e.setCancelled();
       /* Set<DataPacket> sortedPackets = new HashSet<>();
        Set<DataPacket> sortedPackets11 = new HashSet<>();*/

        DataPacket[] packets = e.getPackets();
        Player[] players = e.getPlayers();
        HashMap<SynapseEntry, List<Player>> map = new HashMap<>();

        for (Player p : players) {
            SynapsePlayer player = (SynapsePlayer) p;

            SynapseEntry entry = player.getSynapseEntry();
            List<Player> list = map.get(entry);
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(player);
            map.put(entry, list);
        }

        for (Entry<SynapseEntry, List<Player>> entry : map.entrySet()) {
            entry.getKey().getSynapseInterface().getPutPacketThread().addMainToThreadBroadcast(entry.getValue().stream().toArray(Player[]::new), packets);
        }

        /*for(DataPacket pk : packets) {
            if(pk instanceof Packet11) {
                sortedPackets11.add(pk);
                sortedPackets.add(((Packet11) pk).toDefault());
            } else {
                sortedPackets.add(pk);
                DataPacket compatible = PacketRegister.getCompatiblePacket(pk, 113, true);

                if(compatible != null) {
                    sortedPackets11.add(compatible);
                }
            }
        }*/
    }
}