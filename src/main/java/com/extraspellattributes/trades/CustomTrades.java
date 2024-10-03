package com.extraspellattributes.trades;

import com.extraspellattributes.items.ItemInit;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.WanderingTraderManager;

import java.util.Optional;

public class CustomTrades {
    public static void registerCustomTrades(){
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC,4,
                factories -> {
                    factories.add((entity,random) ->
                        new TradeOffer(
                                new TradedItem(Items.EMERALD,8),
                                new ItemStack(ItemInit.GOLDQUARTZRING,1),
                                4,20,0.05F)
                    );
                    factories.add((entity,random) ->
                            new TradeOffer(
                                    new TradedItem(Items.EMERALD,16),
                                    new ItemStack(ItemInit.GOLDQUARTZAMULET,1),
                                    2,25,0.05F)
                    );
                });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC,5,
                factories -> {
                    factories.add((entity,random) ->
                            new TradeOffer(
                                    new TradedItem(Items.EMERALD,10),
                                    Optional.of(new TradedItem((Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),1)),
                                    new ItemStack(ItemInit.NETHERITEDIAMOND,1),
                                    4,30,0.05F)
                    );
                    factories.add((entity,random) ->
                            new TradeOffer(
                                    new TradedItem(Items.EMERALD,20),
                                    Optional.of(new TradedItem((Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),1)),
                                    new ItemStack(ItemInit.NETHERITEDIAMONDAMULET,1),
                                    2,35,0.05F)
                    );
                });
        TradeOfferHelper.registerWanderingTraderOffers(1,    factories -> {
            factories.add((entity,random) ->
                    new TradeOffer(
                            new TradedItem(Items.EMERALD,8),
                            new ItemStack(ItemInit.GOLDQUARTZRING,1),
                            4,20,0.05F)
            );
            factories.add((entity,random) ->
                    new TradeOffer(
                            new TradedItem(Items.EMERALD,16),
                            new ItemStack(ItemInit.GOLDQUARTZAMULET,1),
                            2,25,0.05F)
            );
        });
        TradeOfferHelper.registerWanderingTraderOffers(2,factories -> {
            factories.add((entity,random) ->
                    new TradeOffer(
                            new TradedItem(Items.EMERALD,10),
                            Optional.of(new TradedItem((Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),1)),
                            new ItemStack(ItemInit.NETHERITEDIAMOND,1),
                            4,30,0.05F)
            );
            factories.add((entity,random) ->
                    new TradeOffer(
                            new TradedItem(Items.EMERALD,20),
                            Optional.of(new TradedItem((Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),1)),
                            new ItemStack(ItemInit.NETHERITEDIAMONDAMULET,1),
                            2,35,0.05F)
            );
        });
    }
}
