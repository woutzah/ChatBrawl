package be.woutzah.chatbrawl.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public final class FireworkCreator {

    private FireworkCreator() { }

    public static void spawnFirework(Player player) {
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta data = firework.getFireworkMeta();
        data.addEffects(
                FireworkEffect.builder()
                        .withColor(Color.PURPLE)
                        .withColor(Color.GREEN)
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withFlicker()
                        .build()
        );
        data.setPower(1);
        firework.setFireworkMeta(data);
    }

}
