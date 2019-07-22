package voxelum.doginventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiDogInventoryContainer extends GuiContainer {
    private static ResourceLocation GUI_LOCATION = new ResourceLocation("supertools", "textures/gui/container/dog_inventory.png");
    public GuiDogInventoryContainer(Container inventorySlotsIn) {
        super(inventorySlotsIn);

        this.xSize = 176;
        this.ySize = 68;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(GUI_LOCATION);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
    }
}
