package net.dragonblockinfinity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HairModel extends HumanoidModel<AbstractClientPlayer> {
   private final ObjMesh objMesh;
   private float red = 1.0F;
   private float green = 1.0F;
   private float blue = 1.0F;
   private float alpha = 0.85F;
   private final float scale;
   private final float offsetX;
   private final float offsetY;
   private final float offsetZ;

   public HairModel() {
      this("Hair.obj", 0.16F, 0.0F, 0.18F, 0.0F);
   }

   public HairModel(String objResource, float scale, float offsetX, float offsetY, float offsetZ) {
      super(createBakedRoot());
      this.objMesh = loadObjMesh(objResource);
      this.scale = scale;
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      this.offsetZ = offsetZ;
   }

   public void setTint(float red, float green, float blue, float alpha) {
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
   }

   private static ModelPart createBakedRoot() {
      MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
      LayerDefinition layer = LayerDefinition.create(mesh, 1, 1);
      return layer.bakeRoot();
   }

   public void renderToBuffer(AbstractClientPlayer player, PoseStack poseStack, VertexConsumer buffer, float partialTicks, int packedLight, int packedOverlay) {
      poseStack.pushPose();
      this.head.translateAndRotate(poseStack);
      poseStack.scale(this.scale, this.scale, this.scale);
      poseStack.translate(this.offsetX, this.offsetY, this.offsetZ);
      if (this.objMesh != null && !this.objMesh.faces.isEmpty()) {
         this.renderObjMesh(buffer, poseStack, packedLight, packedOverlay);
      }
      poseStack.popPose();
   }

   private void renderObjMesh(VertexConsumer buffer, PoseStack poseStack, int packedLight, int packedOverlay) {
      Matrix4f poseMatrix = poseStack.last().pose();
      Matrix3f normalMatrix = poseStack.last().normal();
      for (ObjFace face : this.objMesh.faces) {
         for (int i = 1; i < face.vertices.length - 1; i++) {
            this.addFaceVertex(buffer, poseMatrix, normalMatrix, face.vertices[0], packedLight, packedOverlay);
            this.addFaceVertex(buffer, poseMatrix, normalMatrix, face.vertices[i], packedLight, packedOverlay);
            this.addFaceVertex(buffer, poseMatrix, normalMatrix, face.vertices[i + 1], packedLight, packedOverlay);
         }
      }
   }

   private void addFaceVertex(VertexConsumer buffer, Matrix4f poseMatrix, Matrix3f normalMatrix, ObjFaceVertex faceVertex, int packedLight, int packedOverlay) {
      Vector3f position = this.objMesh.vertices.get(faceVertex.vertexIndex);
      Vector2f uv = faceVertex.uvIndex >= 0 ? this.objMesh.uvs.get(faceVertex.uvIndex) : new Vector2f(0.0F, 0.0F);
      Vector3f normal = faceVertex.normalIndex >= 0 ? this.objMesh.normals.get(faceVertex.normalIndex) : new Vector3f(0.0F, 1.0F, 0.0F);

      buffer.addVertex(position.x, position.y, position.z)
         .setColor(this.red, this.green, this.blue, this.alpha)
         .setUv(uv.x, uv.y)
         .setUv2(packedLight, packedOverlay)
         .setNormal(normal.x, normal.y, normal.z);
   }

   private static ObjMesh loadObjMesh(String resourceName) {
      ObjMesh mesh = new ObjMesh();
      String resourcePath = "assets/dragon-block-infinity/models/hair/" + resourceName;
      InputStream inputStream = HairModel.class.getClassLoader().getResourceAsStream(resourcePath);
      if (inputStream == null) {
         return mesh;
      }

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
         String line;
         while ((line = reader.readLine()) != null) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
               continue;
            }

            String[] tokens = trimmed.split("\\s+");
            switch (tokens[0]) {
               case "v" -> mesh.vertices.add(new Vector3f(
                  Float.parseFloat(tokens[1]),
                  Float.parseFloat(tokens[2]),
                  Float.parseFloat(tokens[3])
               ));
               case "vt" -> mesh.uvs.add(new Vector2f(
                  Float.parseFloat(tokens[1]),
                  1.0F - Float.parseFloat(tokens[2])
               ));
               case "vn" -> mesh.normals.add(new Vector3f(
                  Float.parseFloat(tokens[1]),
                  Float.parseFloat(tokens[2]),
                  Float.parseFloat(tokens[3])
               ));
               case "f" -> {
                  List<ObjFaceVertex> faceVertices = new ArrayList<>();
                  for (int i = 1; i < tokens.length; i++) {
                     String[] values = tokens[i].split("/");
                     int vertexIndex = values.length > 0 && !values[0].isEmpty() ? normalizeIndex(Integer.parseInt(values[0]), mesh.vertices.size()) : -1;
                     int uvIndex = values.length > 1 && !values[1].isEmpty() ? normalizeIndex(Integer.parseInt(values[1]), mesh.uvs.size()) : -1;
                     int normalIndex = values.length > 2 && !values[2].isEmpty() ? normalizeIndex(Integer.parseInt(values[2]), mesh.normals.size()) : -1;
                     faceVertices.add(new ObjFaceVertex(vertexIndex, uvIndex, normalIndex));
                  }

                  if (faceVertices.size() >= 3) {
                     mesh.faces.add(new ObjFace(faceVertices.toArray(new ObjFaceVertex[0])));
                  }
               }
            }
         }
      } catch (IOException exception) {
         exception.printStackTrace();
      }

      return mesh;
   }

   private static int normalizeIndex(int index, int size) {
      if (index < 0) {
         return size + index;
      }
      return index - 1;
   }

   public void copyPropertiesTo(PlayerModel<AbstractClientPlayer> playermodel) {
      super.copyPropertiesTo(playermodel);
      this.head.copyFrom(playermodel.head);
      this.hat.copyFrom(playermodel.hat);
      this.body.copyFrom(playermodel.body);
      this.rightArm.copyFrom(playermodel.rightArm);
      this.leftArm.copyFrom(playermodel.leftArm);
      this.rightLeg.copyFrom(playermodel.rightLeg);
      this.leftLeg.copyFrom(playermodel.leftLeg);
   }

   public void setupAnim(
      PlayerModel<AbstractClientPlayer> playerModel,
      AbstractClientPlayer p_102866_,
      float p_102867_,
      float p_102868_,
      float p_102869_,
      float p_102870_,
      float p_102871_
   ) {
      playerModel.setupAnim(p_102866_, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
   }

   private static class ObjMesh {
      private final List<Vector3f> vertices = new ArrayList<>();
      private final List<Vector2f> uvs = new ArrayList<>();
      private final List<Vector3f> normals = new ArrayList<>();
      private final List<ObjFace> faces = new ArrayList<>();
   }

   private static class ObjFace {
      private final ObjFaceVertex[] vertices;

      private ObjFace(ObjFaceVertex[] vertices) {
         this.vertices = vertices;
      }
   }

   private static class ObjFaceVertex {
      private final int vertexIndex;
      private final int uvIndex;
      private final int normalIndex;

      private ObjFaceVertex(int vertexIndex, int uvIndex, int normalIndex) {
         this.vertexIndex = vertexIndex;
         this.uvIndex = uvIndex;
         this.normalIndex = normalIndex;
      }
   }
}
