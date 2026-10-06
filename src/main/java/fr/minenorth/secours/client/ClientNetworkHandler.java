package fr.minenorth.secours.client;

import fr.minenorth.secours.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;

public final class ClientNetworkHandler {
    private ClientNetworkHandler() {}

    public static void state(ModNetwork.StatePacket p) {
        boolean wasComa = ClientState.coma;
        ClientState.set(p.level(), p.healSeconds(), p.bleeding(), p.coma(), p.comaSeconds(), p.dispatch(), p.careSeconds(), p.zones());
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        Pose pose = null;
        if (p.coma()) {
            try { pose = Pose.valueOf(p.pose()); } catch (RuntimeException e) { pose = Pose.SLEEPING; }
            // Au moment où il tombe, on ferme l'écran ouvert (inventaire, tablette…). Le chat reste utilisable ensuite.
            if (!wasComa && mc.screen != null) mc.setScreen(null);
        }
        mc.player.setForcedPose(pose);
    }

    public static void tablet(ModNetwork.TabletPacket p) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof TabletScreen s) s.update(p);
        else mc.setScreen(new TabletScreen(p));
    }

    public static void clinic(ModNetwork.ClinicPacket p) {
        Minecraft.getInstance().setScreen(new ClinicScreen(p));
    }
}
