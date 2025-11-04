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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import xiaojin.astralflux.api.sourcesoul.IModifySourceSouItem;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.core.AstralFluxConfig;
import xiaojin.astralflux.util.SourceSoulUtil;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class SourceSoulBarLayerDraw implements LayeredDraw.Layer {
  private static final ResourceLocation BOTTOM_TEXT = AstralFlux.modRL("source_soul_bar/bottom");
  private static final ResourceLocation CONTENT_TEXT = AstralFlux.modRL("source_soul_bar/content");
  private static final ResourceLocation COVER_TEXT = AstralFlux.modRL("source_soul_bar/cover");
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
    this.sourceSoulBar = new SourceSoulBar(BOTTOM_TEXT, CONTENT_TEXT, COVER_TEXT);
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
    this.displayTime = 60;
  }

  private void init(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
    // 使用局部变量减少字段访问次数
    int screenWidth = guiGraphics.guiWidth();
    int screenHeight = guiGraphics.guiHeight();

    // 只有当屏幕尺寸发生变化时才重新计算位置
    if (screenWidth != this.screenWidth || screenHeight != this.screenHeight) {
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
      this.leftPos = screenWidth / 2 - this.sourceSoulBarWidth / 2;
      this.topPos = screenHeight - 65;
      this.sourceSoulBar.setX(this.leftPos);
      this.sourceSoulBar.setY(this.topPos);
    }

    // 只有当玩家存在时才更新玩家相关数据
    if (this.minecraft.player != null) {
      this.value = SourceSoulUtil.getValue(this.player);
      this.maxValue = SourceSoulUtil.getMaxValue(this.player);
      this.sourceSoulBar.setValue(this.value, this.maxValue);
    }
  }

  @Override
  public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
    this.player = this.minecraft.player;
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
    init(guiGraphics, deltaTracker);

    final var pose = guiGraphics.pose();
    this.realtimeDeltaTicks = deltaTracker.getRealtimeDeltaTicks();

    pose.pushPose();

    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    var alpha = Math.min(1.0f, this.displayTime / 2);
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);

    // 渲染主体
    pose.pushPose();

    // 渲染主体图片
    renderSourceSoulBarText(guiGraphics, deltaTracker, pose);
    if (AstralFluxConfig.CLIENT_CONFIG.detailsSourceSoulValueType.get()) {
      // 渲染详细数值文本
      renderValueText(guiGraphics, pose);
    }

    pose.popPose();

    if (AstralFluxConfig.CLIENT_CONFIG.detailsModifySourceSoulValueSkipWords.get()) {
      if (!modifySkipWordsDeque.isEmpty()) {
        // 添加调整数值跳字
        addModifySkipWords();
      }
      if (!this.modifySkipWordsTextList.isEmpty()) {
        // 渲染调整数值跳字
        renderModifySkipWordsText(guiGraphics, pose);
      }
    }

    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0f);
    RenderSystem.disableBlend();
    pose.popPose();
    if (isModifySourceSouItem) {
      display();
    } else {
      this.displayTime -= this.realtimeDeltaTicks;
    }
  }

  private void renderSourceSoulBarText(final @NotNull GuiGraphics guiGraphics, final @NotNull DeltaTracker deltaTracker, final PoseStack pose) {
    pose.pushPose();
    sourceSoulBar.renderWidget(guiGraphics, 0, 0, realtimeDeltaTicks);
    pose.popPose();
  }

  private void renderValueText(final @NotNull GuiGraphics guiGraphics, final PoseStack pose) {
    pose.pushPose();
    var valueText = "%s/%s".formatted(
      TextUtil.formatNumber(this.value, 2),
      TextUtil.formatNumber(this.maxValue, 2)
    );

    // 缓存文本组件以提高性能
    var textComponent = Component.literal(valueText);
    guiGraphics.drawCenteredString(
      this.font,
      textComponent,
      this.leftPos + this.sourceSoulBarWidth / 2,
      this.topPos + sourceSoulBar.getRenderHeight() / 2 + font.lineHeight / 2,
      ChatFormatting.WHITE.getColor()
    );
    pose.popPose();
  }

  private void addModifySkipWords() {
    var poll = modifySkipWordsDeque.poll();
    if (poll == null) {
      return;
    }
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

    var speed = new Vector2f(vx, -vy); // 负的vy因为屏幕坐标系y轴向下

    var text = "%s".formatted((poll > 0 ? "+" : "") + TextUtil.formatNumber(poll, ""));
    var component = Component.literal(text).withStyle(poll < 0 ? ChatFormatting.RED : ChatFormatting.GREEN);

    int soulBarHeight = sourceSoulBar.getRenderHeight();
    float x = (float) this.sourceSoulBarWidth / 2 - (float) font.width(component) / 2;
    float y = (float) ((double) soulBarHeight / 2 - sourceSoulBar.getRenderHeightValue() + (double) font.lineHeight);
    this.modifySkipWordsTextList.add(new ModifyText(new Vector2f(this.leftPos + x, this.topPos + y), speed, component));
  }

  private void renderModifySkipWordsText(final @NotNull GuiGraphics guiGraphics, final PoseStack pose) {
    pose.pushPose();
    // 使用迭代器提高删除效率
    Iterator<ModifyText> iterator = this.modifySkipWordsTextList.iterator();
    while (iterator.hasNext()) {
      var modifyText = iterator.next();
      var pos = modifyText.pos;
      var text = modifyText.text;

      pose.pushPose();
      float alpha = Math.min(1.0f, modifyText.time / 2) / 2;
      guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
      guiGraphics.drawString(font, text.getVisualOrderText(), pos.x, pos.y, ChatFormatting.WHITE.getColor(), true);
      guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
      pose.popPose();

      pos.add(
        modifyText.speed.x * realtimeDeltaTicks,
        modifyText.speed.y * realtimeDeltaTicks
      );

      // 添加重力
      modifyText.speed.y += 0.9f * realtimeDeltaTicks;

      // 添加水平阻力
      modifyText.speed.x *= 0.99f;

      modifyText.time -= realtimeDeltaTicks;

      // 移除过期或超出屏幕的文本
      if (modifyText.time <= 0 ||
        modifyText.pos.y <= 0 ||
        modifyText.pos.y >= screenHeight ||
        modifyText.pos.x <= 0 ||
        modifyText.pos.x >= screenWidth) {
        iterator.remove();
      }
    }
    pose.popPose();
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
      this.time = 10;
    }
  }

  private static class SourceSoulBar extends ImageProgressBar.Vertical {
    private final ResourceLocation bottom;
    private final ResourceLocation cover;
    private final int renderXPos;
    private final int renderYPos;
    private final int renderWidth;
    private final int renderHeight;
    private boolean isFlicker;
    private double flickerValue;
    private float time;

    public SourceSoulBar(final ResourceLocation bottom, ResourceLocation content, final ResourceLocation cover) {

      super(0, 0, 27, 32, 0, 0, content, "", true);
      this.bottom = bottom;
      this.cover = cover;
      this.renderXPos = 4;
      this.renderYPos = 1;
      this.renderWidth = 19;
      this.renderHeight = 22;
    }

    @Override
    protected void renderTexture(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      var pose = guiGraphics.pose();
      int uWidth = getRenderWidth();
      int vFlickerHeight = 0;
      int vHeight = (int) getRenderHeightValue();
      if (isFlicker) {
        vFlickerHeight = (int) (flickerValue / getMaxValue() * getRenderHeight());
        if (flickerValue < 0 && vFlickerHeight != 0) {
          vHeight += vFlickerHeight;
        }
      }
      int xPosition = this.renderXPos;
      int yPosition = this.renderYPos + getRenderHeight() - vHeight;
      int x = getX() + xPosition;
      int y = getY() + yPosition;

      pose.pushPose();
      guiGraphics.blitSprite(this.bottom, getX(), getY(), getWidth(), getHeight());
      guiGraphics.blitSprite(this.sprite,
        this.getWidth(), this.getHeight(),
        xPosition, yPosition,
        x, y,
        uWidth, Math.clamp(vHeight, 0, getRenderHeight()));
      if (isFlicker) {
        float alpha = (float) (0.3f * Math.sin(0.2f * time + 0.5f) + 0.7);
        pose.pushPose();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        var abs = Math.abs(vFlickerHeight);
        if (vFlickerHeight > 0) {
          vFlickerHeight = 0;
        }
        guiGraphics.blitSprite(this.sprite,
          this.getWidth(), this.getHeight(),
          xPosition, this.renderYPos - vFlickerHeight,
          x, getY() + this.renderYPos - vFlickerHeight,
          uWidth, Math.clamp(abs, 0, getRenderHeight()));
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0f);
        pose.popPose();
        this.time += partialTick;
        if (this.time < 0) {
          this.time = 0;
        }
      } else if (this.time != 0) {
        this.time = 0;
      }
      guiGraphics.blitSprite(this.cover, getX(), getY(), getWidth(), getHeight());
      pose.popPose();
    }

    public void setFlicker(double value) {
      if (value != 0 && this.getValue() >= value) {
        this.flickerValue = value;
        this.isFlicker = true;
        return;
      }
      this.isFlicker = false;
    }

    public double getRenderHeightValue() {
      return getRenderValue() / getMaxValue() * getRenderHeight();
    }

    public int getRenderHeight() {
      return this.renderHeight;
    }

    public int getRenderWidth() {
      return this.renderWidth;
    }
  }
}
