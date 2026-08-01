package net.dragonblockinfinity.client;

import net.dragonblockinfinity.client.gui.CaracterScreen;
import net.dragonblockinfinity.client.models.render.Hair1Layer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.lwjgl.glfw.GLFW;

public class DragonBlockInfinityClient implements ClientModInitializer {
    private static KeyMapping customizationKey;

    @Override
    public void onInitializeClient() {
        customizationKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "Open Character Customization",
            GLFW.GLFW_KEY_J,
            "key.categories.Dragon Block Infinity"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (customizationKey.consumeClick()) {
                Minecraft.getInstance().setScreen(new CaracterScreen());
            }
        });

        // Anexa o Hair1Layer ao PlayerRenderer (todas as variantes: default/slim).
        // Sem isso o layer existe mas nunca e chamado, e o cabelo nao aparece.
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerRenderer playerRenderer) {
                registrationHelper.register(new Hair1Layer(playerRenderer));
            }
        });
    }
}
