package com.extraspellattributes.mixin;

import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.ReabsorptionInit;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
   /* private static final Identifier ICON = Identifier.of(ReabsorptionInit.MOD_ID,"textures/gui/gui.png");
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    @Shadow
    private int renderHealthValue;
    @Shadow
    private int ticks;*/
 /*   @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private  void drawheartCleann(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player != null) {


            if (ReabsorptionInit.config.visuals && type == InGameHud.HeartType.ABSORBING && client.player.getAttributeValue(ReabsorptionInit.WARDING) > 0 && client.player instanceof PlayerInterface entityInterface && entityInterface.getReabsorbing())
            {
                    context.drawTexture(ICON, x, y, type.getU(halfHeart, blinking), v, 9, 9);
                    info.cancel();
                }

        }
    }*/
}
