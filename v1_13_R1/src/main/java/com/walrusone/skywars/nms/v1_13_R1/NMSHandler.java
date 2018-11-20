package com.walrusone.skywars.nms.v1_13_R1;

import net.minecraft.server.v1_13_R1.*;
import org.bukkit.*;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_13_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class NMSHandler
        implements com.walrusone.skywars.api.NMS {
    public void respawnPlayer(Player player) {
        ((CraftServer) Bukkit.getServer()).getHandle().moveToWorld(((CraftPlayer) player).getHandle(), 0, false);
    }

    @SuppressWarnings("deprecation")
    public void updateChunks(org.bukkit.World world, List<org.bukkit.Chunk> chunks) {
        Iterator<EntityHuman> localIterator2;
        for (Iterator<Chunk> localIterator1 = chunks.iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
            org.bukkit.Chunk currentChunk = (org.bukkit.Chunk) localIterator1.next();
            net.minecraft.server.v1_13_R1.World mcWorld = ((CraftChunk) currentChunk).getHandle().world;
            localIterator2 = mcWorld.players.iterator();
            EntityHuman eh = (EntityHuman) localIterator2.next();
            EntityPlayer ep = (EntityPlayer) eh;
            Player p = (Player) ep;
            p.getWorld().refreshChunk(currentChunk.getX(), currentChunk.getZ());
            continue;
        }
    }

    public void sendParticles(org.bukkit.World world, String type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float data, int amount) {
        world.spawnParticle(Particle.valueOf(type), new Location(world, x, y, z), amount, offsetX, offsetY, offsetZ, data);
    }

    public String getName(org.bukkit.inventory.ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack).getName().getString();
    }

    public FireworkEffect getFireworkEffect(Color one, Color two, Color three, Color four, Color five, FireworkEffect.Type type) {
        return FireworkEffect.builder().flicker(false).withColor(new Color[]{one, two, three, four}).withFade(five).with(type).trail(true).build();
    }

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        PlayerConnection pConn = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle pTitleInfo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadein, stay, fadeout);
        pConn.sendPacket(pTitleInfo);
        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle pSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, iComp);
            pConn.sendPacket(pSubtitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle pTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, iComp);
            pConn.sendPacket(pTitle);
        }
    }

    public boolean isOnePointSeven() {
        return false;
    }

    public Biome getIcePlainsBiome() {
        return Biome.SNOWY_TUNDRA;
    }
}
