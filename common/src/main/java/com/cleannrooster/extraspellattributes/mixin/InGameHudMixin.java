package com.cleannrooster.extraspellattributes.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
public class InGameHudMixin {
   /* private static final Identifier ICON = Identifier.of(MOD_ID,"textures/gui/gui.png");
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


            if (config.visuals && type == InGameHud.HeartType.ABSORBING && client.player.getAttributeValue(WARDING) > 0 && client.player instanceof PlayerInterface entityInterface && entityInterface.getReabsorbing())
            {
                    context.drawTexture(ICON, x, y, type.getU(halfHeart, blinking), v, 9, 9);
                    info.cancel();
                }

        }
    }*/
}
