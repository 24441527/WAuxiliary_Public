package wx.demo.hook.chat

import android.widget.Button
import androidx.core.view.isVisible
import com.highcapable.kavaref.KavaRef.Companion.asResolver
import com.highcapable.kavaref.KavaRef.Companion.resolve
import me.hd.wauxv.hook.anno.HookAnno
import me.hd.wauxv.hook.anno.ViewAnno
import me.hd.wauxv.hook.base.SwitchHook
import me.hd.wauxv.hook.factory.toAppClass
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
@HookAnno
@ViewAnno
object AutoViewOriginalPhotoHook : SwitchHook("AutoViewOriginalPhotoHook") {
    override val location = "聊天"
    override val funcName = "自动查看原图测试"
    override val funcDesc = "在打开图片和视频时自动点击查看原图"

    override fun initOnce() {
        "com.tencent.mm.ui.chatting.gallery.ImageGalleryUI".toAppClass().resolve().firstMethod {
            name = "initView"
        }.hook {
            afterIfEnabled {
                instance.asResolver().field {
                    type = Button::class
                }.forEach {
                    it.get<Button>()?.let { imgBtn ->
                        if (imgBtn.isVisible) {
                            val keywords = listOf("查看原图", "Full Image")
                            if (keywords.any { text -> imgBtn.text.contains(text, true) }) {
                                imgBtn.performClick()
                            }
                        }
                    }
                }
            }
        }
    }
}
