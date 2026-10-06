package fr.minenorth.secours.compat;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.ModList;

import java.util.Locale;
import java.util.UUID;

/** Lien avec le mod Identité (par réflexion) pour afficher les noms RP. */
public final class Compat {
    private Compat() {}

    /** « Prénom NOM » de la carte d'identité, ou "" si le citoyen n'en a pas (ou si le mod est absent). */
    public static String rpName(MinecraftServer s, UUID id) {
        if (!ModList.get().isLoaded("minenorthidentite")) return "";
        try {
            Class<?> c = Class.forName("fr.minenorth.identite.document.IdentityData");
            Object data = c.getMethod("get", ServerLevel.class).invoke(null, s.overworld());
            Object p = c.getMethod("get", UUID.class).invoke(data, id);
            if (p == null) return "";
            return p.getClass().getMethod("firstName").invoke(p) + " "
                    + String.valueOf(p.getClass().getMethod("lastName").invoke(p)).toUpperCase(Locale.ROOT);
        } catch (Throwable t) {
            return "";
        }
    }
}
