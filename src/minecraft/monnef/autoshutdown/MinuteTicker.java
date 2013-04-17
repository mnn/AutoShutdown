/*
 * Copyright (c) 2013 monnef.
 */

package monnef.autoshutdown;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.server.MinecraftServer;

import java.util.EnumSet;

public class MinuteTicker implements IScheduledTickHandler {
    public static final int TICKS_PER_SECOND = 20;
    public static final int TICKS_PER_MINUTE = TICKS_PER_SECOND * 60;

    @Override
    public int nextTickSpacing() {
        return TICKS_PER_MINUTE;
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (!AutoShutdown.active) return;

        MinecraftServer server = MinecraftServer.getServer();
        int players = server.getCurrentPlayerCount();
        if (players <= 0) {
            AutoShutdown.minutesServerIsDead++;

            if (AutoShutdown.minutesServerIsDead == AutoShutdown.getShutdownAfterXMinutes() - 1) {
                if (AutoShutdown.doSave) {
                    AutoShutdown.println("Forcing save 1m before potential shutdown.");
                    server.executeCommand("save-all");
                }
            }

            if (AutoShutdown.minutesServerIsDead >= AutoShutdown.getShutdownAfterXMinutes()) {
                AutoShutdown.println("No players detected in " + AutoShutdown.minutesServerIsDead + " minutes => shutting down server.");
                server.initiateShutdown();
            }
        } else {
            AutoShutdown.minutesServerIsDead = 0;
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
