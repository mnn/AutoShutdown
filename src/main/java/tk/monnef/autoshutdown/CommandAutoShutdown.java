/*
 * Copyright (c) 2013 tk.monnef.monnef.
 */

package tk.monnef.autoshutdown;

import com.google.common.base.Joiner;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class CommandAutoShutdown extends CommandBase {
    @Override
    public String getCommandName() {
        return "autoshutdown";
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/" + getCommandName() + " [postpone=pp|on|off|status=s|about]";
    }

    @Override
    public List getCommandAliases() {
        List res = new ArrayList();
        res.add("asd");
        return res;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] params) {
        boolean printStatus = false;
        boolean ok = false;
        boolean skipLog = false;

        if (params.length >= 1) {
            String t = params[0].toLowerCase();
            if (t.equals("postpone") || t.equals("pp")) {
                AutoShutdown.minutesServerIsDead = 0;
                AutoShutdown.setTimeStatus(TimeShutdownStatus.IDLE);
                ok = printStatus = true;
            } else if (t.equals("on")) {
                AutoShutdown.active = true;
                ok = printStatus = true;
            } else if (t.equals("off")) {
                AutoShutdown.active = false;
                ok = printStatus = true;
            } else if (t.equals("status") || t.equals("s")) {
                ok = printStatus = skipLog = true;
            } else if (t.equals("about")) {
                ok = true;
                AutoShutdown.sendMessage(sender, String.format("%s %s: %s", AutoShutdown.NAME, AutoShutdown.VERSION, AutoShutdown.ABOUT));
            }
        }

        if (printStatus) AutoShutdown.sendMessage(sender, AutoShutdown.instance.getStatus());

        if (!ok) {
            AutoShutdown.sendMessage(sender, "Unknown parameters. See /help for more info.");
        }

        if (ok && !skipLog && sender instanceof Entity) {
            MinecraftServer s = MinecraftServer.getServer();
            Entity p = (Entity) sender;
            s.logInfo("[" + AutoShutdown.NAME + "] " + "\"" + p.getCommandSenderName() + "\" used command: \"" + Joiner.on(" ").join(params) + "\"");
        }
    }

}
