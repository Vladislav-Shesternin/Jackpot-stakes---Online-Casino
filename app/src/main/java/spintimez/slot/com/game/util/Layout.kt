package spintimez.slot.com.game.util

object Layout {

    object Menu {
        val play = LayoutData(465f, 201f, 470f, 150f)
        val exit = LayoutData(465f, 409f, 470f, 150f)
    }

    object Game {
        val balance = LayoutData(58f, 45f, 680f, 103f)
        val bet = LayoutData(247f, 221f, 302f, 96f)
        val spin = LayoutData(518f, 530f, 108f, 113f)
        val auto = LayoutData(344f, 530f, 108f, 113f)
        val menu = LayoutData(170f, 530f,108f, 113f)
        val plus = LayoutData(247f, 367f,108f, 113f)
        val minus = LayoutData(441f, 367f,108f, 113f)
        val slotPanel = LayoutData(0f, 0f,1400f, 700f)
    }

    object SlotPanel {
        val mask       = LayoutData(0f, 0f,1400f, 700f)
        val slot       = LayoutData(868f, -7875f,120f,8498f, hs = 41f)
        val glow       = LayoutData(848f, 57f,160f,586f, hs = 1f)
    }

    object Slot {
        val item = LayoutData(0f, 0f, 120f, 120f, vs = 22f)
        val slotMinY   = -7875f
        val slotMaxY   = 76f
    }

    object Glow {
        val item = LayoutData(0f, 0f, 160f, 160f, vs = -18f)
    }

    data class LayoutData(
        val x: Float = 0f,
        val y: Float = 0f,
        val w: Float = 0f,
        val h: Float = 0f,
        // horizontal space
        val hs: Float = 0f,
        // vertical space
        val vs: Float = 0f,
    )
}