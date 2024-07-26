package me.maurxce.paidportals.command.subcommand;

import md.schorn.spigothelper.command.Context;
import md.schorn.spigothelper.utility.Chat;
import me.maurxce.paidportals.PaidPortals;
import me.maurxce.paidportals.command.contract.Subcommand;
import me.maurxce.paidportals.language.Language;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class InfoCommand implements Subcommand {
    private final PluginDescriptionFile description;

    public InfoCommand() {
        this.description = PaidPortals.getInstance().getDescription();
    }

    @Override
    public String execute(Context context) {
        String author = description.getAuthors().get(0);
        String version = description.getVersion();
        String website = description.getWebsite();

        String message = Chat.translate("%s by %s - v%s", Language.PREFIX, author, version);
        if (!context.isPlayer()) {
            return message;
        }

        TextComponent component = new TextComponent(message);
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, website);
        component.setClickEvent(clickEvent);

        Player player = context.getPlayer();
        player.spigot().sendMessage(component);

        return null;
    }
}
