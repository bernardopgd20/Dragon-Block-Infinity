package net.dragonblockinfinity.client.models.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dragonblockinfinity.client.models.HairModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class HairRender extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
   public static final ResourceLocation HAIR_TEXTURE = ResourceLocation.fromNamespaceAndPath("dragon-block-infinity", "textures/aura.png");
   public static final int[] HAIR_COLORS = {
      0xFF00CCFF,
      0xFFFF66CC,
      0xFF66FF66,
      0xFFFFAA00
   };
   public static float HAIR_ALPHA = 0.85F;

   private final List<HairModel> hairModels = new ArrayList<>();

   public HairRender(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
      super(renderer);
      this.hairModels.add(new HairModel("Hair.obj", 0.16F, 0.0F, 0.18F, 0.0F));
      // Futuras camadas podem ser adicionadas assim:
      // this.hairModels.add(new HairModel("hair2.obj", 0.16F, 0.0F, 0.18F, 0.0F));
      // this.hairModels.add(new HairModel("hair3.obj", 0.16F, 0.0F, 0.18F, 0.0F));
   }

   public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int pPackedLight,
      AbstractClientPlayer pLivingEntity,
      float pLimbSwing,
      float pLimbSwingAmount,
      float pPartialTick,
      float pAgeInTicks,
      float pNetHeadYaw,
      float pHeadPitch
   ) {
      poseStack.pushPose();
      PlayerModel<AbstractClientPlayer> playerModel = this.getParentModel();
      RenderType renderType = RenderType.entityTranslucent(HAIR_TEXTURE);
      VertexConsumer vertexConsumer = buffer.getBuffer(renderType);

      for (int i = 0; i < this.hairModels.size(); i++) {
         HairModel hairModel = this.hairModels.get(i);
         hairModel.copyPropertiesTo(playerModel);
         hairModel.setupAnim(playerModel, pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

         int color = i < HAIR_COLORS.length ? HAIR_COLORS[i] : HAIR_COLORS[0];
         float red = ((color >> 16) & 0xFF) / 255.0F;
         float green = ((color >> 8) & 0xFF) / 255.0F;
         float blue = (color & 0xFF) / 255.0F;
         hairModel.setTint(red, green, blue, HAIR_ALPHA);

         hairModel.renderToBuffer(pLivingEntity, poseStack, vertexConsumer, pPartialTick, pPackedLight, pLivingEntity.hurtTime > 0 ? 3 : OverlayTexture.NO_OVERLAY);
      }

      poseStack.popPose();
   }
}
