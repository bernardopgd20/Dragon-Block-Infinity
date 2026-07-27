package net.dragonblockinfinity.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;

import java.util.Locale;

public class CharacterCustomizationScreen extends Screen {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("dragon-block-infinity", "textures/gui/gui.png");
    private static final int MENU_WIDTH = 255;
    private static final int MENU_HEIGHT = 159;

    private enum Race {
        SAYAJIN,
        HUMANO,
        HALF
    }

    private enum HairStyle {
        HAIR_1,
        HAIR_2,
        HAIR_3
    }

    private Race selectedRace = Race.SAYAJIN;
    private HairStyle selectedHair = HairStyle.HAIR_1;

    public CharacterCustomizationScreen() {
        super(Component.literal("Character Customization"));
    }

    @Override
    protected void init() {
        super.init();
        int baseX = (this.width - MENU_WIDTH) / 2;
        int baseY = (this.height - MENU_HEIGHT) / 2;

        this.addRenderableWidget(Button.builder(Component.literal("Race: Sayajin"), button -> {
            this.selectedRace = Race.SAYAJIN;
            this.updateButtons();
        }).bounds(baseX + 20, baseY + 20, 70, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Race: Humano"), button -> {
            this.selectedRace = Race.HUMANO;
            this.updateButtons();
        }).bounds(baseX + 95, baseY + 20, 70, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Race: Half"), button -> {
            this.selectedRace = Race.HALF;
            this.updateButtons();
        }).bounds(baseX + 170, baseY + 20, 70, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Hair 1"), button -> {
            this.selectedHair = HairStyle.HAIR_1;
            this.updateButtons();
        }).bounds(baseX + 20, baseY + 55, 70, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Hair 2"), button -> {
            this.selectedHair = HairStyle.HAIR_2;
            this.updateButtons();
        }).bounds(baseX + 95, baseY + 55, 70, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Hair 3"), button -> {
            this.selectedHair = HairStyle.HAIR_3;
            this.updateButtons();
        }).bounds(baseX + 170, baseY + 55, 70, 20).build());
    }

    private void updateButtons() {
        this.clearWidgets();
        this.init();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int baseX = (this.width - MENU_WIDTH) / 2;
        int baseY = (this.height - MENU_HEIGHT) / 2;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getResourceManager().getResource(GUI_TEXTURE).isPresent()) {
            RenderSystem.enableBlend();
            guiGraphics.blit(GUI_TEXTURE, baseX, baseY, 0, 0, MENU_WIDTH, MENU_HEIGHT, MENU_WIDTH, MENU_HEIGHT);
            RenderSystem.disableBlend();
        } else {
            guiGraphics.fill(baseX, baseY, baseX + MENU_WIDTH, baseY + MENU_HEIGHT, 0xCC000000);
        }
        guiGraphics.drawCenteredString(this.font, "Race: " + this.selectedRace.name().toLowerCase(Locale.ROOT), this.width / 2, baseY + 10, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "Hair: " + this.selectedHair.name().toLowerCase(Locale.ROOT).replace('_', ' '), this.width / 2, baseY + 90, 0xFFFFFF);

        this.renderPlayerPreview(guiGraphics, this.width / 2, baseY + 125, 80, partialTick);
    }

    private void renderPlayerPreview(GuiGraphics guiGraphics, int centerX, int centerY, int scale, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(centerX, centerY + 90, 1000.0F);
        poseStack.scale(scale, scale, scale);

        Quaternionf quaternion = new Quaternionf().rotationZ(0.0F);
        quaternion.rotateY((float) Math.toRadians(180.0F));
        poseStack.mulPose(quaternion);

        EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
        Player player = minecraft.player;
        if (player == null) {
            poseStack.popPose();
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.0F, 1.6F, 0.0F);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        dispatcher.render(player, 0.0, 0.0, 0.0, 0.0F, partialTick, poseStack, guiGraphics.bufferSource(), 0xF000F0);
        poseStack.popPose();
        poseStack.popPose();
    }
}
