package app.hinl.oreharvest.registry

import app.hinl.oreharvest.Constants
import app.hinl.oreharvest.OreHarvest
import app.hinl.oreharvest.item.rings.BasicRing
import app.hinl.oreharvest.item.rings.RingOfAttraction
import app.hinl.oreharvest.item.rings.RingOfNatural
import app.hinl.oreharvest.item.tools.HoeOfNatural
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.AliasedBlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

object ModItems {
    val oreCropsSeed: Item = object : AliasedBlockItem(
        ModBlock.oreCropsBlock, Settings().group(OreHarvest.itemGroup).rarity(
            Rarity.UNCOMMON
        )
    ) {
        override fun appendTooltip(
            stack: ItemStack?,
            world: World?,
            tooltip: MutableList<Text>?,
            context: TooltipContext?
        ) {
            tooltip?.add(Text.translatable("item.tooltip.ore_crops_seed.1").formatted(Formatting.DARK_GREEN))
        }
    }

    val basicRing = BasicRing(FabricItemSettings().group(OreHarvest.itemGroup))
    val ringOfNatural = RingOfNatural(FabricItemSettings().group(OreHarvest.itemGroup))
    val ringOfAttraction = RingOfAttraction(FabricItemSettings().group(OreHarvest.itemGroup))

    val hoeOfNatural = HoeOfNatural(FabricItemSettings().group(OreHarvest.itemGroup))

    fun register() {
        Registry.register(Registry.ITEM, Identifier(Constants.ModID, "ore_crops_seed"), oreCropsSeed)

        Registry.register(Registry.ITEM, Identifier(Constants.ModID, "ring_basic"), basicRing)
        Registry.register(Registry.ITEM, Identifier(Constants.ModID, "ring_natural"), ringOfNatural)
        Registry.register(Registry.ITEM, Identifier(Constants.ModID, "ring_attraction"), ringOfAttraction)

        Registry.register(Registry.ITEM, Identifier(Constants.ModID, "tool_hoe_natural"), hoeOfNatural)
    }
}