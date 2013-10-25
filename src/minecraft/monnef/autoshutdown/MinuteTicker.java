/*
 * Copyright (c) 2013 monnef.
 */

package monnef.autoshutdown;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

import java.util.EnumSet;

import static monnef.autoshutdown.AutoShutdown.println;

public class MinuteTicker implements IScheduledTickHandler {
    public static final int TICKS_PER_SECOND = 20;
    public static final int TICKS_PER_MINUTE = TICKS_PER_SECOND * 60;

    private MinecraftServer getServer(){
        return MinecraftServer.getServer();
    }

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

        if (AutoShutdown.idleShutdown) idleShutdownTick();
        if (AutoShutdown.timeShutdown) timeShutdownTick();
    }

    private void timeShutdownTick() {
        if (AutoShutdown.getTimeStatus() == TimeShutdownStatus.SAVING) {
            save("Forcing save 1m before potential shutdown (scheduled shutdown).");
            AutoShutdown.setTimeStatus(TimeShutdownStatus.SHUTDOWN);
            broadcastMessage(String.format("[%s] Server will shutdown in one minute.", AutoShutdown.Name));
        } else if (AutoShutdown.getTimeStatus() == TimeShutdownStatus.SHUTDOWN) {
            println("Scheduled shutdown is taking place.");
            shutDown();
        }
    }

    private void idleShutdownTick() {
        int players = getServer().getCurrentPlayerCount();
        if (players <= 0) {
            AutoShutdown.minutesServerIsDead++;

            if (AutoShutdown.minutesServerIsDead == AutoShutdown.getShutdownAfterXMinutes() - 1) {
                save("Forcing save 1m before potential shutdown (no players around).");
            }

            if (AutoShutdown.minutesServerIsDead >= AutoShutdown.getShutdownAfterXMinutes()) {
                println("No players detected in " + AutoShutdown.minutesServerIsDead + " minutes => shutting down server.");
                shutDown();
            }
        } else {
            AutoShutdown.minutesServerIsDead = 0;
        }
    }

    private void save(String msg) {
        if (AutoShutdown.doSave) {
            println(msg);
            getServer().executeCommand("save-all");
        }
    }

    private void broadcastMessage(String msg) {
        ChatMessageComponent component = new ChatMessageComponent();
        component.addText(msg);
        getServer().getConfigurationManager().sendChatMsg(component);
    }

    private void shutDown() {
        getServer().initiateShutdown();
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
