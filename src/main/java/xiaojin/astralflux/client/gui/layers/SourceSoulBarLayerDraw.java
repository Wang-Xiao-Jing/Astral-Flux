package xiaojin.astralflux.client.gui.layers;

import ctn.ctnapi.client.gui.widget.ImageProgressBar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.AstralFlux;
import xiaojin.astralflux.api.sourcesoul.ISourceSoul;
import xiaojin.astralflux.api.sourcesoul.SourceSoulUtil;

@OnlyIn(Dist.CLIENT)
public class SourceSoulBarLayerDraw implements LayeredDraw.Layer {
  private static final ResourceLocation BOTTOM_TEXT = AstralFlux.modRL("source_soul_bar/bottom");
  private static final ResourceLocation CONTENT_TEXT = AstralFlux.modRL("source_soul_bar/content");
  private static final ResourceLocation COVER_TEXT = AstralFlux.modRL("source_soul_bar/cover");
  private final Minecraft minecraft;
  private final Player player;
  private final ISourceSoul sourceSoul;
  private final SourceSoulBar sourceSoulBar;
  private final int width;
  private final int height;

  public SourceSoulBarLayerDraw(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Minecraft minecraft) {
    this.minecraft = minecraft;
    this.player = this.minecraft.player;
    this.sourceSoul = ISourceSoul.of(player);
    this.width = 27;
    this.height = 32;
    this.sourceSoulBar = new SourceSoulBar(
      0, 0,
      width, height, 4, 1,
      19, 22,
      player == null ? 0 : sourceSoul.astralFlux$getSourceSoulValue(),
      player == null ? 0 : SourceSoulUtil.getSourceSoulMaxValue(player),
      CONTENT_TEXT);
    render(guiGraphics, deltaTracker);
  }

  @Override
  public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
    final var pose = guiGraphics.pose();
    final int screenWidth = guiGraphics.guiWidth();
    final int screenHeight = guiGraphics.guiHeight();
    final int leftPos = screenWidth / 2 - width / 2;
    final int topPos = screenHeight - 65;

    pose.pushPose();
    sourceSoulBar.setX(leftPos);
    sourceSoulBar.setY(topPos);
    sourceSoulBar.setValue(
      player == null ? 0 : sourceSoul.astralFlux$getSourceSoulValue(),
      player == null ? 0 : SourceSoulUtil.getSourceSoulMaxValue(player));
    guiGraphics.blitSprite(BOTTOM_TEXT, leftPos, topPos, width, height);
    sourceSoulBar.renderWidget(guiGraphics, 0, 0, deltaTracker.getGameTimeDeltaPartialTick(false));
    guiGraphics.blitSprite(COVER_TEXT, leftPos, topPos, width, height);

    pose.popPose();
  }

  private static class SourceSoulBar extends ImageProgressBar.Vertical {
    private final int renderXPos;
    private final int renderYPos;
    private final int renderWidth;
    private final int renderHeight;

    public SourceSoulBar(int x, int y,
                         int width, int height,
                         int renderXPos, int renderYPos,
                         int renderWidth, int renderHeight,
                         double value, double maxValue,
                         ResourceLocation texture) {
      super(x, y, width, height, value, maxValue, texture, "", true);
      this.renderXPos = renderXPos;
      this.renderYPos = renderYPos;
      this.renderWidth = renderWidth;
      this.renderHeight = renderHeight;
    }

    @Override
    protected void renderTexture(@NotNull GuiGraphics guiGraphics) {
      int uWidth = getWidth();
      int value = (int) ((getRenderValue() / (float) getMaxValue()) * getHeight());
      int vHeight = (isToTop ? value : getHeight() - value);
      int xPosition = renderXPos;
      int yPosition = isToTop ? getHeight() - vHeight : renderYPos;
      int x = getX() + xPosition;
      int y = isToTop ? getY() + yPosition : getY();

      guiGraphics.blitSprite(sprite,
        width, height,
        xPosition, yPosition,
        x, y,
        uWidth, vHeight);
    }

    @Override
    public int getHeight() {
      return renderHeight;
    }

    @Override
    public int getWidth() {
      return renderWidth;
    }
  }
}
