package net.dragonblockinfinity.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CaracterScreen extends Screen {
    private static final ResourceLocation MENU = ResourceLocation.fromNamespaceAndPath("dragonblockinfinity", "textures/gui/gui.png");
    private static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath("dragonblockinfinity", "textures/gui/icons.png");

    private static final int WIDTH = 255;
    private static final int HEIGHT = 159;
    private static final int BUTTON_SIZE = 10;
    private static final int LEFT_BUTTON_U = 0;
    private static final int LEFT_BUTTON_V = 0;
    private static final int RIGHT_BUTTON_U = 10;
    private static final int RIGHT_BUTTON_V = 0;
    private static final int LEFT_BUTTON_PRESSED_U = 0;
    private static final int LEFT_BUTTON_PRESSED_V = 10;
    private static final int RIGHT_BUTTON_PRESSED_U = 10;
    private static final int RIGHT_BUTTON_PRESSED_V = 10;

    private int menuX;
    private int menuY;
    private boolean leftArrowPressed;
    private boolean rightArrowPressed;

    public CaracterScreen() {
        super(Component.literal("Caracter"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        menuX = (this.width - WIDTH) / 2;
        menuY = (this.height - HEIGHT) / 2;

        graphics.blit(MENU, menuX, menuY, 0, 0, WIDTH, HEIGHT);
        super.render(graphics, mouseX, mouseY, delta);

        int leftButtonX = menuX + 20;
        int leftButtonY = menuY + 20;
        int rightButtonX = menuX + WIDTH - 30;
        int rightButtonY = menuY + 20;

        graphics.blit(ICONS, leftButtonX, leftButtonY,
            leftArrowPressed ? LEFT_BUTTON_PRESSED_U : LEFT_BUTTON_U,
            leftArrowPressed ? LEFT_BUTTON_PRESSED_V : LEFT_BUTTON_V,
            BUTTON_SIZE, BUTTON_SIZE, 10, 10);

        graphics.blit(ICONS, rightButtonX, rightButtonY,
            rightArrowPressed ? RIGHT_BUTTON_PRESSED_U : RIGHT_BUTTON_U,
            rightArrowPressed ? RIGHT_BUTTON_PRESSED_V : RIGHT_BUTTON_V,
            BUTTON_SIZE, BUTTON_SIZE, 10, 10);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return false;
        }

        int leftButtonX = menuX + 20;
        int leftButtonY = menuY + 20;
        int rightButtonX = menuX + WIDTH - 30;
        int rightButtonY = menuY + 20;

        if (isMouseOverButton(mouseX, mouseY, leftButtonX, leftButtonY)) {
            leftArrowPressed = true;
            return true;
        }

        if (isMouseOverButton(mouseX, mouseY, rightButtonX, rightButtonY)) {
            rightArrowPressed = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        leftArrowPressed = false;
        rightArrowPressed = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isMouseOverButton(double mouseX, double mouseY, int buttonX, int buttonY) {
        return mouseX >= buttonX && mouseX < buttonX + BUTTON_SIZE && mouseY >= buttonY && mouseY < buttonY + BUTTON_SIZE;
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}