package xiaojin.astralflux.common.item.aegusbarrier;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModAttachmentTypes;
import xiaojin.astralflux.util.SourceSoulUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Aegus屏障护盾管理类
 * 负责管理护盾的生成、消耗、旋转等逻辑
 */
public final class AegusBarrierShields {
  public static final int CONSUME_VALUE = -15;

  private int tics;
  private int consumeTics;
  private float xRot;
  private float yRot;
  private float xRotO;
  private float yRotO;
  private float targetXRot;
  private float targetYRot;
  /**
   * 第一面展开
   */
  private boolean isTheFirstSideExpands;
  private List<Shield> shields = new ArrayList<>(7);
  private int expandsCount;
  private final UUID playerUUID;

  /**
   * 构造一个新的Aegus屏障护盾实例
   * 初始化时添加第一个护盾
   */
  public AegusBarrierShields(UUID playerUUID) {
    this.shields.add(new Shield(0));
    this.playerUUID = playerUUID;
  }

  /**
   * 设置目标旋转角度
   *
   * @param xRot X轴旋转角度
   * @param yRot Y轴旋转角度
   */
  public void setTargetRot(float xRot, float yRot) {
    this.setTargetXRot(xRot);
    this.setTargetYRot(yRot);
  }

  /**
   * 每游戏刻更新护盾状态
   * 包括生成新护盾、消耗资源和移除损坏护盾
   *
   * @param player 拥有此护盾的玩家
   */
  public void tick(Player player) {
    var level = player.level();

    // 客户端
    if (level.isClientSide()) {
      move();
      return;
    }

    var value = SourceSoulUtil.getValue(player);
    // 玩家已无源魂值
    if (value <= 0 || shields.isEmpty()) {
      remove(player);
      return;
    }

    move();

    // 每0.2秒尝试添加一个新护盾
    if (this.tics >= 20 * 0.2) {
      addShield(player);
      if (!this.isTheFirstSideExpands) {
        isTheFirstSideExpands = true;
      }
      this.tics = 0;
    }

    // 如果第一面已展开，每3秒消耗一次资源维持护盾
    if (this.isTheFirstSideExpands && this.consumeTics >= 20 * 3) {
      if (!SourceSoulUtil.modify(player, CONSUME_VALUE)) {
        remove(player);
        return;
      }
      this.consumeTics = 0;
    }

    // 移除标记为删除的护盾
    this.shields.removeIf(next -> next.isRemove);

    // 更新计时器
    if (this.isTheFirstSideExpands) {
      this.consumeTics++;
    }
    this.tics++;

    syncData(player);
  }

  private void move() {
    this.xRotO = this.getXRot();
    this.yRotO = this.getYRot();
//    this.xRot = (float) interpolation(this.getTargetXRot());
//    this.yRot = (float) interpolation(this.getTargetYRot());
    this.xRot = (float) this.getTargetXRot();
    this.yRot = (float) this.getTargetYRot();
  }

  private void syncData(Player player) {
    player.syncData(ModAttachmentTypes.AEGUS_BARRIER_SHIELD);
  }

  private static double interpolation(final float x) {
    return Math.pow(-2 + x, 2) * Math.pow(x, 2);
  }

  /**
   * 移除玩家的护盾
   *
   * @param player 需要移除护盾的玩家
   */
  public void remove(final Player player) {
    player.removeData(ModAttachmentTypes.AEGUS_BARRIER_SHIELD);
    syncData(player);
  }

  /**
   * 拦截指定编号的护盾
   *
   * @param shieldNumber 要拦截的护盾编号
   * @return 如果成功拦截护盾返回true，否则返回false
   */
  public boolean interdict(int shieldNumber) {
    return shields.stream()
      .filter(shield -> shield.isNumber(shieldNumber))
      .filter(Shield::isIntact)
      .findFirst()
      .map(shield -> {
        shield.onRemove();
        return true;
      }).orElse(false);
  }

  /**
   * 获取用于渲染的X轴旋转角度（带插值）
   *
   * @param partialTicks 插值因子
   * @return 插值后的X轴旋转角度
   */
  public float getViewXRot(float partialTicks) {
    return partialTicks == 1.0F ? this.getXRot() :
      Mth.lerp(partialTicks, this.xRotO, this.getXRot());
  }

  /**
   * 获取用于渲染的Y轴旋转角度（带插值）
   *
   * @param partialTick 插值因子
   * @return 插值后的Y轴旋转角度
   */
  public float getViewYRot(float partialTick) {
    return partialTick == 1.0F ? this.getYRot() :
      Mth.lerp(partialTick, this.yRotO, this.getYRot());
  }

  /**
   * 调整护盾的朝向
   *
   * @param yRot Y轴旋转增量
   * @param xRot X轴旋转增量
   */
  public void turn(double yRot, double xRot) {
    float f = (float) xRot * 0.15F;
    float f1 = (float) yRot * 0.15F;
    this.xRot = this.getXRot() + f;
    this.yRot = this.getYRot() + f1;
    this.xRot = Mth.clamp(this.getXRot(), -90.0F, 90.0F);
    this.xRotO += f;
    this.yRotO += f1;
    this.xRotO = Mth.clamp(this.xRotO, -90.0F, 90.0F);
  }

  public float getYRot() {
    return yRot;
  }

  public float getXRot() {
    return xRot;
  }

  public float getTargetYRot() {
    return targetYRot;
  }

  public float getTargetXRot() {
    return targetXRot;
  }

  public float getYRotO() {
    return yRotO;
  }

  public float getXRotO() {
    return xRotO;
  }

  public int getTics() {
    return tics;
  }

  /**
   * 设置目标Y轴旋转角度
   *
   * @param targetYRot 目标Y轴旋转角度
   */
  public void setTargetYRot(float targetYRot) {
    if (!Float.isFinite(xRot)) {
      rotationSetErrorLog(yRot);
      return;
    }
    this.targetYRot = targetYRot;
  }

  /**
   * 设置目标X轴旋转角度
   *
   * @param targetXRot 目标X轴旋转角度
   */
  public void setTargetXRot(float targetXRot) {
    if (!Float.isFinite(xRot)) {
      rotationSetErrorLog(yRot);
      return;
    }
    this.targetXRot = targetXRot;
  }

  /**
   * 记录旋转设置错误日志
   *
   * @param rot 无效的旋转值
   */
  private static void rotationSetErrorLog(final float rot) {
    AstralFlux.LOGGER.error("Invalid aegusBarrierShield rotation: {}, discarding.", rot);
  }

  /**
   * 添加一个新的护盾
   *
   * @return 添加结果类型
   */
  @NotNull
  @SuppressWarnings("UnusedReturnValue")
  public AddType addShield(Player player) {
    if (this.expandsCount == 6) {
      syncData(player);
      return AddType.FAILURE;
    }
    // 获取最后一个未完全成型的护盾
    var last = this.shields.stream().filter(shield -> !shield.isIntact()).findFirst().orElse(null);
    AddType type;
    if (last == null) {
      type = AddType.ADD;
    } else {
      type = AddType.INTACT;
      last.onIntact();
    }

    // 添加一个新护盾
    this.expandsCount++;
    var shield = new Shield(this.shields.stream().map(Shield::getNumber).max(Integer::compareTo).orElse(-1) + 1);
    this.shields.addLast(shield);
    syncData(player);
    return type;
  }

  /**
   * 获取当前完整护盾数量
   *
   * @return 完整护盾数量
   */
  public int getShieldCount() {
    return this.shields.size() - (this.shields.getLast().isIntact ? 0 : 1);
  }

  /**
   * 获取可用护盾的编号
   */
  public int[] getShieldNumbers() {
    return this.shields.stream().mapToInt(Shield::getNumber).toArray();
  }

  public boolean isTheFirstSideExpands() {
    return isTheFirstSideExpands;
  }

  public void setTheFirstSideExpands(boolean theFirstSideExpands) {
    isTheFirstSideExpands = theFirstSideExpands;
  }

  /**
   * 获取已展开的护盾数量
   *
   * @return 已展开护盾数
   */
  public int getExpandsCount() {
    return expandsCount;
  }

  public UUID getPlayerUUID() {
    return playerUUID;
  }

  /**
   * 护盾添加结果枚举
   */
  public enum AddType {
    ADD,      // 成功添加新护盾
    INTACT,   // 修复了现有护盾
    FAILURE   // 添加失败（已达上限）
  }

  /**
   * 护盾内部类
   * 表示单个护盾的状态
   */
  public static final class Shield {
    private final int number;
    /**
     * 是否完整
     */
    private boolean isIntact;
    /**
     * 是否移除
     */
    private boolean isRemove;

    /**
     * 创建一个新的护盾
     *
     * @param number 护盾编号
     */
    public Shield(int number) {
      this.number = number;
    }

    public int getNumber() {
      return number;
    }

    public boolean isNumber(int number) {
      return this.number == number;
    }

    public boolean isIntact() {
      return isIntact;
    }

    /**
     * 标记护盾为完整状态
     */
    public void onIntact() {
      isIntact = true;
    }

    /**
     * 标记护盾为移除
     */
    public void onRemove() {
      isRemove = true;
    }
  }

  /**
   * 护盾数据同步处理器
   * 负责在网络间同步护盾状态
   */
  public static final class Sync implements AttachmentSyncHandler<AegusBarrierShields> {
    /**
     * 将护盾数据写入网络缓冲区
     *
     * @param buf         网络缓冲区
     * @param attachment  要同步的护盾对象
     * @param initialSync 是否为初始同步
     */
    @Override
    public void write(final RegistryFriendlyByteBuf buf, final AegusBarrierShields attachment, final boolean initialSync) {
      buf.writeUUID(attachment.playerUUID);
//      buf.writeInt(attachment.tics);
//      buf.writeInt(attachment.consumeTics);
      buf.writeFloat(attachment.xRot);
      buf.writeFloat(attachment.yRot);
      buf.writeFloat(attachment.xRotO);
      buf.writeFloat(attachment.yRotO);
      buf.writeFloat(attachment.targetXRot);
      buf.writeFloat(attachment.targetYRot);
      buf.writeArray(attachment.shields.toArray(Shield[]::new), (buf1, shield) -> {
        buf1.writeInt(shield.number);
        buf1.writeBoolean(shield.isIntact);
//        buf1.writeBoolean(shield.isRemove);
      });
    }

    /**
     * 从网络缓冲区读取护盾数据
     *
     * @param holder        附件持有者
     * @param buf           网络缓冲区
     * @param previousValue 之前的护盾对象（可为空）
     * @return 读取到的护盾对象
     */
    @Override
    public @NotNull AegusBarrierShields read(final IAttachmentHolder holder, final RegistryFriendlyByteBuf buf, @Nullable final AegusBarrierShields previousValue) {
      var uuid = buf.readUUID();
      var shield = previousValue == null ? new AegusBarrierShields(uuid) : previousValue;
//      shield.tics = buf.readInt();
//      shield.consumeTics = buf.readInt();
      shield.xRot = buf.readFloat();
      shield.yRot = buf.readFloat();
      shield.xRotO = buf.readFloat();
      shield.yRotO = buf.readFloat();
      shield.targetXRot = buf.readFloat();
      shield.targetYRot = buf.readFloat();
      shield.shields = new ArrayList<>(List.of(buf.readArray(Shield[]::new, buf1 -> {
        var shield1 = new Shield(buf1.readInt());
        shield1.isIntact = buf1.readBoolean();
//        shield1.isRemove = buf1.readBoolean();
        return shield1;
      })));
      return shield;
    }
  }
}
