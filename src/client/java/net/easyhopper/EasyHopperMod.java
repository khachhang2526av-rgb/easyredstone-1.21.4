package net.easyhopper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyHopperMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("easyredstone");
    private static boolean enabled = true;
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        EasyHopperConfig.load();
        enabled = EasyHopperConfig.easyRedstoneEnabled;

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.easyredstone.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.easyredstone"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                EasyHopperConfig.easyRedstoneEnabled = enabled;
                EasyHopperConfig.save();

                if (client.player != null && EasyHopperConfig.showActionBarMessage) {
                    Text status = enabled
                        ? Text.translatable("message.easyredstone.on").formatted(Formatting.GREEN)
                        : Text.translatable("message.easyredstone.off").formatted(Formatting.RED);
                    client.player.sendMessage(
                        Text.translatable("message.easyredstone.status", status),
                        true
                    );
                }
            }
        });

        LOGGER.info("Easy Redstone (1.21.4) initialized. {} item(s). Press H to toggle.",
            EasyHopperConfig.enabledItems.size());
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
        EasyHopperConfig.easyRedstoneEnabled = value;
    }
}
