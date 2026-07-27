package net.dragonblockinfinity.client.render;


import net.minecraft.client.renderer.ShaderInstance;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import com.mojang.blaze3d.shaders.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

public class AuraLayer {
    
    public static void AuraLayerRender() {
        BufferBuilder bufferBuilder = new BufferBuilder(256, DefaultVertexFormat.POSITION);
        BufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION, 0);
        BufferUploader.drawWithShader(bufferBuilder.end(), RenderSystem.getShader());
        BufferBuilder.end();

        BufferBuilder.addVertex(bufferBuilder, -1.0F, -1.0F, 0.0F);
        BufferBuilder.addVertex(bufferBuilder, 1.0F, -1.0F, 0.0F);
        BufferBuilder.addVertex(bufferBuilder, 1.0F, 1.0F, 0.0F);
        BufferBuilder.addVertex(bufferBuilder, -1.0F, 1.0F, 0.0F);
        BufferBuilder.addVertex(bufferBuilder, -1.0F, -1.0F, 0.0F);
    }
}