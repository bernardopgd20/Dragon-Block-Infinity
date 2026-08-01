package net.dragonblockinfinity.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dragonblockinfinity.client.customization.CharacterSelectionState;
import net.dragonblockinfinity.client.customization.Hair;
import net.dragonblockinfinity.client.customization.Race;
import net.dragonblockinfinity.client.models.render.Hair1Layer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hair1LayerMixin — injeta no inicio do render() de Hair1Layer e
 * cancela o desenho (retorna cedo, sem chamar o corpo original do
 * metodo) a menos que a selecao atual (CharacterSelectionState) seja
 * exatamente Race.SAYAJIN + Hair.HAIR_1.
 *
 * Ou seja: o cabelo so e desenhado quando o jogador escolheu essa
 * combinacao especifica na CaracterScreen; qualquer outra escolha
 * (ex.: Humano) faz o layer nao desenhar nada naquele frame.
 */
@Mixin(Hair1Layer.class)
public class Hair1LayerMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void dragonblockinfinity$onlyRenderForSayajinHair1(
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight,
        AbstractClientPlayer player,
        float limbSwing,
        float limbSwingAmount,
        float partialTicks,
        float ageInTicks,
        float netHeadYaw,
        float headPitch,
        CallbackInfo ci
    ) {
        Race selectedRace = CharacterSelectionState.getSelectedRace();
        Hair selectedHair = CharacterSelectionState.getSelectedHair();

        if (selectedRace != Race.SAYAJIN || selectedHair != Hair.HAIR_1) {
            ci.cancel();
        }
    }
}

