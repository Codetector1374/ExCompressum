package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.tile.TileHeavySieve;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderHeavySieve extends TileEntitySpecialRenderer<TileHeavySieve> {

    @Override
    public void render(TileHeavySieve tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        // Render mesh
        ItemStack meshStack = tileEntity.getMeshStack();
        if (!meshStack.isEmpty()) {
            SieveMeshRegistryEntry sieveMesh = SieveMeshRegistry.getEntry(meshStack);
            if (sieveMesh != null) {
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                TextureAtlasSprite sprite = sieveMesh.getSpriteLocation() != null ? mc.getTextureMapBlocks().getTextureExtry(sieveMesh.getSpriteLocation().toString()) : null;
                if (sprite == null) {
                    sprite = mc.getTextureMapBlocks().getMissingSprite();
                }
                int brightness = tileEntity.getWorld().getCombinedLight(tileEntity.getPos().up(), 0);
                float meshXZ = 0.0625f;
                float meshXZ2 = 1f - meshXZ;
                float meshY = 0.56f;
                RenderUtils.renderQuadUp(renderer, meshXZ, meshY, meshXZ, meshXZ2, meshY, meshXZ2, 0xFFFFFFFF, brightness, sprite);
                tessellator.draw();
            }
        }

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            IBlockState state = StupidUtils.getStateFromItemStack(currentStack);
            if(state != null) {
                float progress = tileEntity.getProgress();
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0625f, 0.5625f, 0.0625f);
                float tt = 0.42f;
                GlStateManager.scale(0.88f, tt - progress * tt, 0.88f);
                RenderUtils.renderBlockWithTranslate(mc, state, tileEntity.getWorld(), tileEntity.getPos(), renderer);
                tessellator.draw();
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }

}
