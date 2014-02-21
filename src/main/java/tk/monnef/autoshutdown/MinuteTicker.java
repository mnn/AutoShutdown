/*
 * Copyright (c) 2013 tk.monnef.monnef.
 */

package tk.monnef.autoshutdown;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import static tk.monnef.autoshutdown.AutoShutdown.println;

public class MinuteTicker {
    public static final int TICKS_PER_SECOND = 20;
    public static final int TICKS_PER_MINUTE = TICKS_PER_SECOND * 60;

    private int counter = 0;

    private MinecraftServer getServer() {
        return MinecraftServer.getServer();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent evt) {
        counter++;
        if (counter > TICKS_PER_MINUTE) {
            counter = 0;
            onMinuteEvent();
        }
    }

    public void onMinuteEvent() {
        if (!AutoShutdown.active) return;

        if (AutoShutdown.idleShutdown) idleShutdownTick();
        if (AutoShutdown.timeShutdown) timeShutdownTick();
    }

    private void timeShutdownTick() {
        if (AutoShutdown.getTimeStatus() == TimeShutdownStatus.SAVING) {
            save("Forcing save 1m before potential shutdown (scheduled shutdown).");
            AutoShutdown.setTimeStatus(TimeShutdownStatus.SHUTDOWN);
            broadcastMessage(String.format("[%s] Server will shutdown in one minute.", AutoShutdown.NAME));
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
            getServer().handleRConCommand("save-all");
        }
    }

    private void broadcastMessage(String msg) {
        ChatComponentText line = new ChatComponentText(msg);
        getServer().getConfigurationManager().sendChatMsg(line);
    }

    private void shutDown() {
        getServer().initiateShutdown();
    }
}
