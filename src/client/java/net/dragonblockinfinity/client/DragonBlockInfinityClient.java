package net.dragonblockinfinity.client;

import net.dragonblockinfinity.client.gui.CharacterCustomizationScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class DragonBlockInfinityClient implements ClientModInitializer {
    private static KeyMapping customizationKey;

    @Override
    public void onInitializeClient() {
        customizationKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "Open Character Customization",
            GLFW.GLFW_KEY_K,
            "key.categories.Dragon Block Infinity"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (customizationKey.consumeClick()) {
                Minecraft.getInstance().setScreen(new CharacterCustomizationScreen());
            }
        });
    }
}