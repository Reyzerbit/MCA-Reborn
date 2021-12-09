package com.reyzerbit.mca_reborn.client.render;

import static com.reyzerbit.mca_reborn.common.entities.MCAVillager.VILLAGER_NAME;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reyzerbit.mca_reborn.client.model.ModelVillagerMCA;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.enums.EnumAgeState;
import com.reyzerbit.mca_reborn.common.enums.EnumGender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class RenderVillagerMCA<T extends MCAVillager> extends BipedRenderer<MCAVillager, ModelVillagerMCA> {
	
    private static final ResourceLocation gui = new ResourceLocation("mca_reborn:textures/gui.png");
    private static final float LABEL_SCALE = 0.027F;
	
	private static ModelVillagerMCA model = new ModelVillagerMCA();

    public RenderVillagerMCA(EntityRendererManager manager) {
    	
        super(manager, model , 0.5F);
        
        //this.addLayer(new BipedArmorLayer(this));
        
        //This might not be needed, as there is already a held item layer for BipedRenders
        //this.addLayer(new HeldItemLayer(this));
        
    }
    
    @Override
	public void render(MCAVillager entity, float f1, float f2, MatrixStack matrix, IRenderTypeBuffer buffer, int packedLight) {
    	
    	model.breasts.visible = EnumGender.byId(entity.getEntityData().get(MCAVillager.GENDER)).equals(EnumGender.MALE) ? false : true;
    	
    	super.render(entity, f1, f2, matrix, buffer, packedLight);
    	
    }

	@Override
    protected void scale(MCAVillager villager, MatrixStack matrix, float partialTickTime) {
    	
        if (villager.isBaby()) {
        	
            float scaleForAge = EnumAgeState.byId(villager.get(MCAVillager.AGE_STATE)).getScaleForAge();
            matrix.scale(scaleForAge, scaleForAge, scaleForAge);
            
        }
        
        super.scale(villager, matrix, partialTickTime);
        
    }

    @Override
    protected void renderNameTag(MCAVillager entity, ITextComponent component, MatrixStack matrix, IRenderTypeBuffer buffer, int unknown) {
        
        if (shouldShowName(entity)) {
        	
            if (entity.getHealth() < entity.getMaxHealth()) {
            	
                renderHealth(entity, matrix, buffer, (int) entity.getHealth(), (int) entity.getMaxHealth());
                
            }

            //TODO Figure this out...
            /*
            if (entity.getCurrentActivity() != null) {
            	
                double d0 = entity.distanceToSqr(this.entityRenderDispatcher.crosshairPickEntity);
                float f = entity.isCrouching() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
                
                if (d0 < (double) (f * f)) {
                	
                    this.renderEntityName(entity, x, y - 0.25F, z, "(" + entity.getCurrentActivity() + ")", d0);
                    
                }
                
            }
            */
        	
            super.renderNameTag(entity, new StringTextComponent(entity.getEntityData().get(VILLAGER_NAME)), matrix, buffer, unknown);
            
        }
        
    }

    private void renderHealth(MCAVillager villager, MatrixStack matrix, IRenderTypeBuffer buffer, int currentHealth, int maxHealth) {
    	
        final int redHeartU = 80;
        final int darkHeartU = 96;
        int heartsDrawn = 0;

        float maxHealthF = Math.round((float)maxHealth / 2.0F);
        float currentHealthF = Math.round((float)currentHealth / 2.0F);
        int heartsMax = Math.round((maxHealthF / maxHealthF) * 10.0F);
        int heartsToDraw = Math.round((currentHealthF / maxHealthF) * 10.0F);

        for (int i = 0; i < heartsMax; i++) {
        	
            int heartU = i < heartsToDraw ? redHeartU : darkHeartU;
            heartsDrawn++;

            matrix.pushPose();
            matrix.translate(0.0F, villager.getBbHeight() + 1.0F, 0.0F);
            matrix.mulPose(new Quaternion(-entityRenderDispatcher.camera.getLookVector().y(), 0.0F, 1.0F, 0.0F));
            matrix.mulPose(new Quaternion(entityRenderDispatcher.camera.getLookVector().x(), 1.0F, 0.0F, 0.0F));
            matrix.scale(-LABEL_SCALE, -LABEL_SCALE, LABEL_SCALE);
            //GL11.glDisable(GL11.GL_LIGHTING);
            matrix.translate(-2.0F, 2.0F, -2.0F);
            drawTexturedRectangle(gui, (int)/*posX +*/ (heartsDrawn * 8) - 45, (int)/*posY -*/ 4, heartU, 0, 16, 16);
            
            matrix.popPose();
            //GL11.glDepthMask(true);
            //GL11.glEnable(GL11.GL_LIGHTING);
            
        }
        
    }

    @Override
	public ResourceLocation getTextureLocation(MCAVillager villager) {
    	
        return villager.getTextureResourceLocation();
        
    }

    @Override
    protected boolean shouldShowName(MCAVillager entity) {
    	
    	Minecraft mc = Minecraft.getInstance();
        float distance = mc.player.distanceTo(entity);
        return distance < 5F;
        
    }

    public static void drawTexturedRectangle(ResourceLocation texture, int x, int y, int u, int v, int width, int height) {
    	
        Minecraft mc = Minecraft.getInstance();
        mc.textureManager.bind(texture);

        float f = 0.00390625F;
        float f1 = 0.00390625F;

        final Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.vertex(x + 0, y + height, 0.0D).uv((u + 0) * f, ((v + height) * f1)).endVertex();
        buffer.vertex(x + width, y + height, 0.0D).uv((u + width) * f, ((v + height) * f1)).endVertex();
        buffer.vertex(x + width, y + 0,	0.0D).uv((u + width) * f, ((v + 0) * f1)).endVertex();
        buffer.vertex(x + 0, y + 0, 0.0D).uv((u + 0) * f, ((v + 0) * f1)).endVertex();
        tessellator.end();
        
    }

    /*
    @Override
    protected void renderLivingAt(MCAVillager entityLiving, double x, double y, double z) {
    	
        if (entityLiving.isEntityAlive() && entityLiving.isSleeping()) {
        	
            super.renderLivingAt(entityLiving, x + (double)entityLiving.renderOffsetX, y + (double)entityLiving.renderOffsetY, z + (double)entityLiving.renderOffsetZ);
            
        } else {
        	
            super.renderLivingAt(entityLiving, x, y, z);
            
        }
        
    }*/

    @Override
    protected void setupRotations(MCAVillager entityLiving, MatrixStack matrix, float p_77043_2_, float rotationYaw, float partialTicks) {
    	
        if (entityLiving.isSleeping()) {
            
            matrix.mulPose(entityLiving.getBedOrientation().getRotation());
            matrix.mulPose(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
            matrix.mulPose(new Quaternion(270.0F, 0.0F, 1.0F, 0.0F));
            
            rotationYaw = 180.0f;
            
        }
        
        super.setupRotations(entityLiving, matrix, p_77043_2_, rotationYaw, partialTicks);
        
    }
    
}
