package wx.demo.hook.experiment

import me.hd.wauxv.data.config.DexDescData
import me.hd.wauxv.hook.anno.HookAnno
import me.hd.wauxv.hook.anno.ViewAnno
import me.hd.wauxv.hook.base.SwitchHook
import me.hd.wauxv.hook.core.dex.IDexFind
import me.hd.wauxv.hook.factory.findDexClassMethod
import me.hd.wauxv.hook.factory.toDexMethod
import org.lsposed.lsparanoid.Obfuscate
import org.luckypray.dexkit.DexKitBridge

@Obfuscate
@HookAnno
@ViewAnno
object PadModeHook : SwitchHook("PadModeHook"), IDexFind {
    private object MethodIsPadDevice : DexDescData("PadModeHook.MethodIsPadDevice")
    private object MethodIsFoldableDevice : DexDescData("PadModeHook.MethodIsFoldableDevice")

    override val location = "实验"
    override val funcName = "平板模式"
    override val funcDesc = "可在当前设备登录另一台设备的微信号"

    override fun initOnce() {
        MethodIsPadDevice.toDexMethod {
            hook {
                beforeIfEnabled {
                    resultTrue()
                }
            }
        }
        MethodIsFoldableDevice.toDexMethod {
            hook {
                beforeIfEnabled {
                    resultFalse()
                }
            }
        }
    }

    override fun dexFind(dexKit: DexKitBridge) {
        MethodIsPadDevice.findDexClassMethod(dexKit) {
            onMethod {
                searchPackages("com.tencent.mm.ui")
                matcher {
                    usingEqStrings("Lenovo TB-9707F")
                }
            }
        }
        MethodIsFoldableDevice.findDexClassMethod(dexKit) {
            onMethod {
                searchPackages("com.tencent.mm.ui")
                matcher {
                    usingEqStrings("isRoyoleFoldableDevice!!!")
                }
            }
        }
    }
}
