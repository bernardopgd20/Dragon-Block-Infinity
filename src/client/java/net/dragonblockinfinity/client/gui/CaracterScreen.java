package net.dragonblockinfinity.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;



public class CaracterScreen extends Screen {
    private static final ResourceLocation MENU = ResourceLocation.fromNamespaceAndPath("dragonblockinfinity", "textures/gui/gui.png");
    private static final ResourceLocation MENU = ResourceLocation.fromNamespaceAndPath("dragonblockinfinity", "textures/gui/icons.png");


    public CaracterScreen() {
        super(Component.literal("Caracter"));
    }
    private int WIDTH = 255;
    private int HEIGHT = 159;
    private 

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.blit(MENU, (this.width - WIDTH) / 2, (this.height - HEIGHT) / 2, 0, 0, WIDTH, HEIGHT);
        super.render(graphics, mouseX, mouseY, delta);

    }
}