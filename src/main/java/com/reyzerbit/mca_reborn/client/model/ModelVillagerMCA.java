package com.reyzerbit.mca_reborn.client.model;

import com.reyzerbit.mca_reborn.common.entities.MCAVillager;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelVillagerMCA extends BipedModel<MCAVillager> {
	
    public ModelRenderer breasts;

    public ModelVillagerMCA() {
    	
        super(0.0F, 0.0F, 64, 64);
        
        breasts = new ModelRenderer(this, 18, 21);
        breasts.addBox(-3F, 0F, -1F, 6, 3, 3);
        breasts.setPos(0.5F, 3.5F, -2F);
        breasts.xRot = 0.65F;
        breasts.setTexSize(64, 64);
        body.addChild(breasts);
        //breasts.mirror = true;
        
	}

    //public void setupAnim(Entity entity, float swing, float swingAmount, float age, float headYaw, float headPitch, float scale)
    //TODO Animations
    /*
    @Override
    public void prepareMobModel(MCAVillager entity, float age, float headYaw, float headPitch) {
    	
        super.prepareMobModel(entity, age, headYaw, headPitch);
        MCAVillager villager = entity;
        
        if (EnumGender.byId(villager.get(MCAVillager.GENDER)) == EnumGender.FEMALE && !villager.isBaby() && !villager.hasItemInSlot(EquipmentSlotType.CHEST)) {
        	
        	MatrixStack matrix = new MatrixStack();
        	
        	matrix.pushPose();
        	matrix.translate(0.005D, -0.05D, -0.28D);
        	matrix.scale(1.15F, 1.0F, 1.0F);
        	matrix.mulPose(new Quaternion(60.0F, 1.0F, 0.0F, 0.0F));
        	
            //GL11.glPushMatrix();
            //GL11.glTranslated(0.005D, -0.05D, -0.28D);
            //GL11.glScaled(1.15D, 1.0D, 1.0D);
            //GL11.glRotatef(60.0F, 1.0F, 0.0F, 0.0F);
            
        	
            breasts.render(matrix, null, 1, 1, 1.15F, 1.0F, 60F, 1.0F);
            
            matrix.popPose();
            //GL11.glPopMatrix();
            
        }
        
    }
    */
    
}