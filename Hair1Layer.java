package net.dragonblockinfinity.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dragonblockinfinity.client.models.hair.Hair1Mesh;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * Hair1Layer — RenderLayer vanilla (Minecraft/Fabric) que desenha o
 * mesh de Hair1Mesh preso na cabeca do jogador.
 *
 * Esta e a UNICA classe do sistema de cabelo que fala diretamente com
 * a API de renderizacao do Minecraft. Ela nao guarda nenhum dado de
 * geometria propria — toda a malha (posicoes, uvs, normais, indices)
 * vem de Hair1Mesh, que e Java puro e nao sabe nada sobre Minecraft.
 *
 * Usa a assinatura de RenderLayer valida para Minecraft 1.21.1
 * (net.minecraft.client.renderer.entity.layers.RenderLayer com
 * render(PoseStack, MultiBufferSource, int, T, float, float, float,
 * float, float, float)). A partir de 1.21.2 essa assinatura muda para
 * usar EntityRenderState em vez da entidade direta — se o projeto for
 * atualizado para 1.21.2+, esta classe precisa ser revisada.
 *
 * Nao depende de GeoRenderLayer/GeoRenderer do GeckoLib porque o
 * jogador e renderizado por PlayerRenderer vanilla, nao por um
 * GeoEntityRenderer — os dois nao se encaixam diretamente. O GeckoLib
 * continua disponivel como dependencia do projeto, mas nao e usado
 * aqui; se no futuro o cabelo precisar de animacoes por keyframe,
 * esta classe pode ser revista para delegar a um AnimationProcessor
 * do GeckoLib.
 */
public class Hair1Layer extends RenderLayer<AbstractClientPlayer, PlayerModel> {

    private static final ResourceLocation HAIR_TEXTURE =
        ResourceLocation.fromNamespaceAndPath("dragonblockinfinity", "textures/hair/hair_1.png");

    public Hair1Layer(RenderLayerParent<AbstractClientPlayer, PlayerModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight,
        AbstractClientPlayer player,
        float limbSwing,
        float limbSwingAmount,
        float partialTicks,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        // Nao desenha cabelo em jogadores invisiveis (ex.: efeito de invisibilidade).
        if (player.isInvisible()) {
            return;
        }

        poseStack.pushPose();

        // Segue a cabeca do jogador: aplica a mesma transformacao (posicao +
        // rotacao) que o ModelPart "head" do PlayerModel usa, para que o
        // cabelo acompanhe o movimento/rotacao da cabeca corretamente.
        getParentModel().getHead().translateAndRotate(poseStack);

        PoseStack.Pose pose = poseStack.last();
        Matrix4f positionMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(HAIR_TEXTURE));

        int overlay = OverlayTexture.NO_OVERLAY;

        Hair1Mesh.forEachTriangle((a, b, c) -> {
            emitVertex(buffer, positionMatrix, normalMatrix, a, packedLight, overlay);
            emitVertex(buffer, positionMatrix, normalMatrix, b, packedLight, overlay);
            emitVertex(buffer, positionMatrix, normalMatrix, c, packedLight, overlay);
        });

        poseStack.popPose();
    }

    /**
     * Escreve um unico vertice do mesh no VertexConsumer, transformando
     * posicao e normal pela matriz atual da PoseStack (para que o cabelo
     * respeite a posicao/rotacao da cabeca, e nao apareca sempre "reto"
     * ignorando a pose do jogador).
     */
    private static void emitVertex(
        VertexConsumer buffer,
        Matrix4f positionMatrix,
        Matrix3f normalMatrix,
        int vertexIndex,
        int packedLight,
        int overlay
    ) {
        float x = Hair1Mesh.getPositionX(vertexIndex);
        float y = Hair1Mesh.getPositionY(vertexIndex);
        float z = Hair1Mesh.getPositionZ(vertexIndex);

        float nx = Hair1Mesh.getNormalX(vertexIndex);
        float ny = Hair1Mesh.getNormalY(vertexIndex);
        float nz = Hair1Mesh.getNormalZ(vertexIndex);

        float u = Hair1Mesh.getUvU(vertexIndex);
        float v = Hair1Mesh.getUvV(vertexIndex);

        // setNormal espera a normal ja transformada (float, float, float);
        // nao existe overload que aceite a Matrix3f diretamente aqui, entao
        // aplicamos a transformacao manualmente antes de repassar.
        org.joml.Vector3f transformedNormal = normalMatrix.transform(new org.joml.Vector3f(nx, ny, nz));

        buffer.addVertex(positionMatrix, x, y, z)
            .setColor(255, 255, 255, 255)
            .setUv(u, v)
            .setOverlay(overlay)
            .setLight(packedLight)
            .setNormal(transformedNormal.x, transformedNormal.y, transformedNormal.z);
    }
}
