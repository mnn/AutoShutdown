package monnef.autoshutdown;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.server.MinecraftServer;

import java.util.EnumSet;

public class MinuteTicker implements IScheduledTickHandler {
    @Override
    public int nextTickSpacing() {
        return 20 * 60;
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (!mod_autoshutdown.active) return;

        MinecraftServer server = MinecraftServer.getServer();
        int players = server.getCurrentPlayerCount();
        if (players <= 0) {
            mod_autoshutdown.minutesServerIsDead++;

            if (mod_autoshutdown.minutesServerIsDead == mod_autoshutdown.getShutdownAfterXMinutes() - 1) {
                if (mod_autoshutdown.doSave) {
                    mod_autoshutdown.println("Forcing save 1m before potential shutdown.");
                    server.executeCommand("save-all");
                }
            }

            if (mod_autoshutdown.minutesServerIsDead >= mod_autoshutdown.getShutdownAfterXMinutes()) {
                mod_autoshutdown.println("No players detected in " + mod_autoshutdown.minutesServerIsDead + " minutes => shutting down server.");
                server.initiateShutdown();
            }
        } else {
            mod_autoshutdown.minutesServerIsDead = 0;
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel() {
        return "autoshutdown.minuteticker";
    }
}
