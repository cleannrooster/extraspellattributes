package com.extraspellattributes.api;

import net.minecraft.entity.player.PlayerEntity;

public class RecoupInstances{

    public static class RecoupInstanceHealth{
        public  double remainingduration = 0;
        public double value = 0;
        public double remainingvalue = 0;
        public double duration = 4;
        public PlayerEntity player = null;
        public  RecoupInstanceHealth(PlayerEntity player, double duration, double value) {
            this.player = player;
            this.duration = duration;
            this.value = value;
            this.remainingvalue = value;
            this.remainingduration = duration;
        }
        public void tick(){
            if(this.remainingduration > 0 && this.remainingduration % 10 == 0) {
                this.player.heal(10.0F*((float) (this.value / duration)));
                this.remainingvalue -= 10.0F*((float) (this.value / duration));
            }
            this.remainingduration--;

        }
    }
    public static class RecoupInstanceAbsorption{
        public  double remainingduration = 0;
        public double value = 0;
        public double remainingvalue = 0;
        public double duration = 4;
        public PlayerEntity player = null;
        public  RecoupInstanceAbsorption(PlayerEntity player, double duration, double value) {
                this.player = player;
                this.duration = duration;
                this.value = value;
                this.remainingvalue = value;
                this.remainingduration = duration;
        }
        public void tick(){
            if(this.remainingduration > 0 && this.remainingduration % 10 == 0) {
                this.player.setAbsorptionAmount(this.player.getAbsorptionAmount()+10.0F*((float) (this.value / duration)));
                this.remainingvalue -= 10.0F*((float) (this.value / duration));
            }
            this.remainingduration--;

        }
    }

}
