package xiaojin.astralflux.client.gui.layers;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ctn.ctnapi.client.gui.widget.ImageProgressBar;
import ctn.ctnapi.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import xiaojin.astralflux.api.sourcesoul.IModifySourceSouItem;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldEntity;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.core.AstralFluxConfig;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.util.SourceSoulUtil;

import java.util.*;


public class SourceSoulBarLayerDraw implements LayeredDraw.Layer {
  private static final ResourceLocation BOTTOM_TEXT = AstralFlux.modRL("source_soul_bar/bottom");
  private static final ResourceLocation CONTENT_TEXT = AstralFlux.modRL("source_soul_bar/content");
  private static final ResourceLocation COVER_TEXT = AstralFlux.modRL("source_soul_bar/cover");
  private static final ResourceLocation SURFACE_TEXT = AstralFlux.modRL("source_soul_bar/surface");
  public static final SourceSoulBarLayerDraw INSTANCE = new SourceSoulBarLayerDraw();

  private final Minecraft minecraft;
  private final SourceSoulBar sourceSoulBar;
  private final Font font;
  private final int sourceSoulBarWidth;
  private final int sourceSoulBarHeight;

  private Player player;
  private int screenWidth;
  private int screenHeight;
  private int leftPos;
  private int topPos;
  private double value;
  private double maxValue;
  private float realtimeDeltaTicks;

  /**
   * 需要处理的调整数值
   */
  private final Deque<Double> modifySkipWordsDeque = new ArrayDeque<>();
  private final List<ModifyText> modifySkipWordsTextList = new ArrayList<>();
  private float displayTime;

  public SourceSoulBarLayerDraw() {
    this.minecraft = Minecraft.getInstance();
    this.font = this.minecraft.font;
    this.sourceSoulBar = new SourceSoulBar();
    this.sourceSoulBarWidth = this.sourceSoulBar.getWidth();
    this.sourceSoulBarHeight = this.sourceSoulBar.getHeight();
  }

  public void addModify(double value) {
    if (AstralFluxConfig.CLIENT_CONFIG.detailsModifySourceSoulValueSkipWords.get()) {
      this.modifySkipWordsDeque.addLast(value);
    }
    display();
  }

  public void display() {
    this.displayTime = 100;
  }

  public int getSourceSoulBarHeight() {
    return sourceSoulBarHeight;
  }
  public boolean isDisplay() {
    return this.displayTime > 0;
  }

  @Override
  public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
    if (minecraft.options.hideGui) {
      if (displayTime != 0) {
        this.displayTime = 0;
      }
      return;
    }
    this.player = this.minecraft.player;
    var oldValue = this.value;
    var oldMaxValue = this.maxValue;
    var newValue = SourceSoulUtil.getValue(this.player);
    var newMaxValue = SourceSoulUtil.getMaxValue(this.player);
    var isVaryValue = newValue != oldValue;
    var isVaryMaxValue = newMaxValue != oldMaxValue;
    var isVary = isVaryValue || isVaryMaxValue;

    if (isVary) {
      this.value = newValue;
      this.maxValue = newMaxValue;
      display();
    }

    var weaponItem = this.player.getWeaponItem();
    boolean isModifySourceSouItem;
    if (weaponItem.getItem() instanceof IModifySourceSouItem i) {
      isModifySourceSouItem = true;
      this.sourceSoulBar.setFlicker(i.getModifyValue(weaponItem, this.player));
    } else {
      isModifySourceSouItem = false;
      this.sourceSoulBar.setFlicker(0);
      if (this.displayTime <= 0) {
        return;
      }
    }

    if (isVary){
      this.sourceSoulBar.setValue(this.value, maxValue);
    }

    int oldScreenWidth = guiGraphics.guiWidth();
    int oldScreenHeight = guiGraphics.guiHeight();

    if (oldScreenWidth != this.screenWidth || oldScreenHeight != this.screenHeight) {
      this.screenWidth = oldScreenWidth;
      this.screenHeight = oldScreenHeight;
      this.leftPos = oldScreenWidth / 2 - this.sourceSoulBarWidth / 2;
      this.topPos = oldScreenHeight - 65;
      this.sourceSoulBar.setX(this.leftPos);
      this.sourceSoulBar.setY(this.topPos);
    }

    final var pose = guiGraphics.pose();
    this.realtimeDeltaTicks = deltaTracker.getGameTimeDeltaTicks();

    pose.pushPose();
    {
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      guiGraphics.setColor(1.0F, 1.0F, 1.0F, Math.min(1.0f, this.displayTime / 2));

      // 渲染主体
      pose.pushPose();

      {
        // 渲染主体图片
        pose.pushPose();
        this.sourceSoulBar.renderWidget(guiGraphics, 0, 0, this.realtimeDeltaTicks);
        pose.popPose();

        if (AstralFluxConfig.CLIENT_CONFIG.detailsSourceSoulValueType.get()) {
          // 渲染详细数值文本
          pose.pushPose();
          renderValueText(guiGraphics, pose);
          pose.popPose();
        }
      }

      pose.popPose();

      if (AstralFluxConfig.CLIENT_CONFIG.detailsModifySourceSoulValueSkipWords.get()) {
        if (!modifySkipWordsDeque.isEmpty()) {
          // 添加调整数值跳字
          addModifySkipWords();
        }
        if (!this.modifySkipWordsTextList.isEmpty()) {
          // 渲染调整数值跳字
          pose.pushPose();
          renderModifySkipWordsText(guiGraphics, pose);
          pose.popPose();
        }
      }

      guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0f);
      RenderSystem.disableBlend();
    }

    pose.popPose();

    if (isModifySourceSouItem) {
      display();
      return;
    }
    this.displayTime -= this.realtimeDeltaTicks;
    if (this.displayTime <= 0) {
      this.displayTime = 0;
    }
  }

  private void renderValueText(final @NotNull GuiGraphics guiGraphics, final PoseStack pose) {
    var valueText = "%s/%s".formatted(
      TextUtil.formatNumber(this.value, 0),
      TextUtil.formatNumber(this.maxValue, 0)
    );

    var textComponent = Component.literal(valueText);
    var stringWidth = font.getSplitter().stringWidth(textComponent);
    var x = (int) (this.leftPos + (float) this.sourceSoulBarWidth / 2 + stringWidth / 2);
    var y = (int) (this.topPos + (float) sourceSoulBar.getInternalHeight() / 2 + (float) font.lineHeight / 2);
    guiGraphics.drawString(this.font, textComponent.getVisualOrderText(), x, y, ChatFormatting.WHITE.getColor(), true);
  }

  private void addModifySkipWords() {
    var poll = modifySkipWordsDeque.poll();
    if (poll == null) {
      return;
    }

    var text = "%s".formatted((poll > 0 ? "+" : "") + TextUtil.formatNumber(poll, ""));
    var textComponent = Component.literal(text).withStyle(poll < 0 ? ChatFormatting.RED : ChatFormatting.GREEN);

    int soulBarHeight = sourceSoulBar.getInternalHeight();
    var x = (float) (this.leftPos + (double) this.sourceSoulBarWidth / 2 - font.getSplitter().stringWidth(textComponent) / 2);
    var y = (float) (this.topPos + (double) soulBarHeight / 2 + font.lineHeight);
    this.modifySkipWordsTextList.add(new ModifyText(new Vector2f(x, y), getSkipWordsSpeed(), textComponent));
  }

  /**
   * 获取跳字运动速度
   */
  private @NotNull Vector2f getSkipWordsSpeed() {
    var random = player.getRandom();

    // 优化抛物线算法 - 使用更真实的抛物运动参数
    // 角度在45度附近变化，以获得更好的抛物线轨迹
    double angle = Math.toRadians(random.nextFloat() * 30 + 30); // 30-60度之间
    double speedFactor = 10 + random.nextFloat() * 0.5 + 0.7; // 3.7-4.2之间的速度因子

    // 根据角度计算初始速度分量
    float vx = (float) (Math.cos(angle) * speedFactor);
    float vy = (float) (Math.sin(angle) * speedFactor);

    // 随机决定方向（左或右）
    if (random.nextBoolean()) {
      vx = -vx;
    }

    return new Vector2f(vx, -vy);
  }

  private void renderModifySkipWordsText(final @NotNull GuiGraphics guiGraphics, final PoseStack pose) {
    // 使用迭代器提高删除效率
    Iterator<ModifyText> iterator = this.modifySkipWordsTextList.iterator();
    while (iterator.hasNext()) {
      var modifyText = iterator.next();
      var pos = modifyText.pos;
      var text = modifyText.text;

      pose.pushPose();
      {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, Math.min(1.0f, modifyText.time / 2) / 2);
        guiGraphics.drawString(font, text.getVisualOrderText(), pos.x, pos.y, ChatFormatting.WHITE.getColor(), true);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
      pose.popPose();


      pos.add(
        modifyText.speed.x * this.realtimeDeltaTicks,
        modifyText.speed.y * this.realtimeDeltaTicks
      );
      // 添加重力
      modifyText.speed.y += 0.7f * this.realtimeDeltaTicks;
      // 添加水平阻力
      modifyText.speed.x *= 0.99f;
      modifyText.time -= this.realtimeDeltaTicks;
      // 移除过期或超出屏幕的文本
      if (modifyText.time <= 0 ||
        modifyText.pos.y <= 0 ||
        modifyText.pos.y >= this.screenHeight ||
        modifyText.pos.x <= 0 ||
        modifyText.pos.x >= this.screenWidth) {
        iterator.remove();
      }
    }
  }

  private static final class ModifyText {
    private final Vector2f pos;
    private final Vector2f speed;
    private final Component text;
    private float time;

    private ModifyText(Vector2f pos, Vector2f speed, Component text) {
      this.pos = pos;
      this.speed = speed;
      this.text = text;
      this.time = 60 ;
    }
  }

  private static final class SourceSoulBar extends ImageProgressBar.Vertical {
    private final ResourceLocation bottom;
    private final ResourceLocation cover;
    private final ResourceLocation surface;
    private final int internalXPos;
    private final int internalYPos;
    private final int internalWidth;
    private final int internalHeight;
    private boolean isFlicker;
    private double flickerValue;

    public SourceSoulBar() {
      super(0, 0,
        43, 36,
        0, 0,
        CONTENT_TEXT,
        "", true);
      this.bottom = BOTTOM_TEXT;
      this.cover = COVER_TEXT;
      this.surface = SURFACE_TEXT;
      this.internalXPos = 11;
      this.internalYPos = 10;
      this.internalWidth = 25;
      this.internalHeight = 18;
    }

    @Override
    protected void renderTexture(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      var pose = guiGraphics.pose();
      var x = getX();
      var y = getY();
      var width1 = getWidth();
      var height1 = getHeight();

      guiGraphics.blitSprite(this.bottom, x, y, width1, height1);
      renderInternal(guiGraphics, x, y , pose);
      guiGraphics.blitSprite(this.cover, x, y, width1, height1);
    }

    private void renderInternal(final @NotNull GuiGraphics guiGraphics,
                                int x,
                                int y,
                                final PoseStack pose) {
      var textureWidth = this.getWidth();
      var textureHeight = this.getHeight();
      int uWidth = getInternalWidth();
      int vFlickerHeight = 0;
      int vHeight = (int) getRenderHeightValue();

      if (this.isFlicker) {
        vFlickerHeight = (int) (this.flickerValue / getMaxValue() * getInternalHeight());
        if (this.flickerValue < 0 && vFlickerHeight != 0) {
          vHeight += vFlickerHeight;
        }
      }

      int xPosition = this.internalXPos;
      int yPosition = this.internalYPos + getInternalHeight() - vHeight;

      var internalHeight1 = getInternalHeight();
      x += xPosition;
      y += yPosition;

      vHeight = Math.clamp(vHeight, 0, internalHeight1);
      guiGraphics.blitSprite(this.sprite, textureWidth, textureHeight, xPosition, yPosition, x, y, uWidth, vHeight);


      if (this.isFlicker) {
        pose.pushPose();
        var alpha = (float) (0.1f * Math.sin(System.currentTimeMillis() % 10000 / 200.0) + 0.5);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);

        var yPosition1 = yPosition - (vFlickerHeight > 0 ? vFlickerHeight : -vFlickerHeight);
        var y1 = getY() + yPosition1;
        var vHeight1 = Math.clamp(Math.abs(vFlickerHeight), 0, internalHeight1);
        guiGraphics.blitSprite(this.sprite, textureWidth, textureHeight, xPosition, yPosition1, x, y1, uWidth, vHeight1);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0f);
        pose.popPose();
      }

      guiGraphics.blitSprite(this.surface, textureWidth, textureHeight, xPosition, yPosition, x, y, uWidth, 1);
    }

    public void setFlicker(double value) {
      if (value != 0 && this.getValue() + value > 0) {
        this.flickerValue = value;
        this.isFlicker = true;
        return;
      }
      this.isFlicker = false;
    }

    public double getRenderHeightValue() {
      return getRenderValue() / getMaxValue() * getInternalHeight();
    }

    public int getInternalHeight() {
      return this.internalHeight;
    }

    public int getInternalWidth() {
      return this.internalWidth;
    }
  }
}
