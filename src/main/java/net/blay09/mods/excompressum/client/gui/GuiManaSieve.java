package net.blay09.mods.excompressum.client.gui;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.TileAutoSieveBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiManaSieve extends GuiAutoSieve {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/mana_sieve.png");

    public GuiManaSieve(InventoryPlayer inventoryPlayer, TileAutoSieveBase tileEntity) {
        super(inventoryPlayer, tileEntity);
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return texture;
    }

    @Override
    protected void renderEnergyBar() {
    }

    @Override
    protected void renderPowerTooltip(int mouseX, int mouseY) {
    }
}
