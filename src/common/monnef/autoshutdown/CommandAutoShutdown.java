package monnef.autoshutdown;

import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandAutoShutdown extends CommandBase {
    @Override
    public String getCommandName() {
        return "autoshutdown";
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/" + getCommandName() + " [postpone=pp|on|off]";
    }

    @Override
    public List getCommandAliases() {
        List res = new ArrayList();
        res.add("asd");
        return res;
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if (var2.length > 1) {
            switch (var2[0]) {
                case "postpone":
                case "pp":
                    mod_autoshutdown.minutesServerIsDead = 0;
                    break;

                case "on":
                    mod_autoshutdown.active = true;
                    break;

                case "off":
                    mod_autoshutdown.active = false;
                    break;

                default:
                    break;
            }
        }

        var1.sendChatToPlayer(mod_autoshutdown.getStatus());
    }

}
