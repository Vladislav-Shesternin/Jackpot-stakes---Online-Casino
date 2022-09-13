package spintimez.slot.com.game.actors.slot

import spintimez.slot.com.game.actors.slot.Matrix3x4.Item.*

interface CombinationMatrixEnum3x4 {
    val matrix: Matrix3x4
}



/*
* Подумай над тем что-бы сделать не енамы а object классы с полями а CombinationMatrixEnum3x4
* обязательное поле это список Matrix-ов, и потом в класах определяй их как поля и добавляй в этот обязательный список
* потом юзай этот список
* */

object Combination3x3 {

    enum class Mix(
        override val matrix: Matrix3x4,
    ) : CombinationMatrixEnum3x4 {
        _1(
            Matrix3x4(
                a0 = a1, b0 = a5, c0 = a2,
                a1 = a2, b1 = a6, c1 = a3,
                a2 = a3, b2 = a7, c2 = a4,
                a3 = a4, b3 = a1, c3 = a5,
            )
        ),
        _2(
            Matrix3x4(
                a0 = a5, b0 = a1, c0 = a4,
                a1 = a4, b1 = a7, c1 = a3,
                a2 = a3, b2 = a6, c2 = a2,
                a3 = a2, b3 = a5, c3 = a1,
            )
        ),
        _3(
            Matrix3x4(
                a0 = a3, b0 = a5, c0 = a2,
                a1 = a2, b1 = a6, c1 = a3,
                a2 = a3, b2 = a7, c2 = a4,
                a3 = a4, b3 = a1, c3 = a2,
            )
        ),
        _4(
            Matrix3x4(
                a0 = a2, b0 = a5, c0 = a7,
                a1 = a2, b1 = a6, c1 = a3,
                a2 = a3, b2 = a1, c2 = a4,
                a3 = a4, b3 = a1, c3 = a2,
            )
        ),
        _5(
            Matrix3x4(
                a0 = a5, b0 = a5, c0 = a7,
                a1 = a2, b1 = a6, c1 = a3,
                a2 = a3, b2 = a1, c2 = a4,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
    }

    enum class MixWithWild(
        override val matrix: Matrix3x4,
    ) : CombinationMatrixEnum3x4 {
        _1(
            Matrix3x4(
                scheme = """
                    W - -
                    - - -
                    - - -
                    - - -
                """,
                a0 = W, b0 = a4, c0 = a7,
                a1 = a2, b1 = a5, c1 = a8,
                a2 = a3, b2 = a6, c2 = a3,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _2(
            Matrix3x4(
                scheme = """
                    - W -
                    - - -
                    - - -
                    - - -
                """,
                a0 = a6, b0 = W, c0 = a7,
                a1 = a2, b1 = a5, c1 = a8,
                a2 = a3, b2 = a6, c2 = a3,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _3(
            Matrix3x4(
                scheme = """
                    - - W
                    - - -
                    - - -
                    - - -
                """,
                a0 = a7, b0 = a4, c0 = W,
                a1 = a2, b1 = a5, c1 = a8,
                a2 = a3, b2 = a6, c2 = a3,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _4(
            Matrix3x4(
                scheme = """
                    - - -
                    W - -
                    - - -
                    - - -
                """,
                a0 = a7, b0 = a4, c0 = a4,
                a1 = W, b1 = a5, c1 = a8,
                a2 = a3, b2 = a6, c2 = a3,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _5(
            Matrix3x4(
                scheme = """
                    - - -
                    - W -
                    - - -
                    - - -
                """,
                a0 = a7, b0 = a4, c0 = a8,
                a1 = a2, b1 = W, c1 = a8,
                a2 = a3, b2 = a6, c2 = a3,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _6(
            Matrix3x4(
                scheme = """
                    - - -
                    - - W
                    - - -
                    - - -
                """,
                a0 = a7, b0 = a4, c0 = a8,
                a1 = a2, b1 = a5, c1 = W,
                a2 = a3, b2 = a6, c2 = a3,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _7(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    W - -
                    - - -
                """,
                a0 = a7, b0 = a4, c0 = a8,
                a1 = a2, b1 = a5, c1 = a3,
                a2 = W, b2 = a6, c2 = a3,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _8(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    - W -
                    - - -
                """,
                a0 = a7, b0 = a4, c0 = a8,
                a1 = a2, b1 = a5, c1 = a3,
                a2 = a7, b2 = W, c2 = a3,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _9(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    - - W
                    - - -
                """,
                a0 = a7, b0 = a4, c0 = a8,
                a1 = a2, b1 = a5, c1 = a3,
                a2 = a1, b2 = a6, c2 = W,
                a3 = a4, b3 = a1, c3 = a4,
            )
        ),
        _10(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    - - -
                    W - -
                """,
                a0 = a7, b0 = a4, c0 = a8,
                a1 = a2, b1 = a5, c1 = a3,
                a2 = a1, b2 = a6, c2 = a3,
                a3 = W, b3 = a1, c3 = a4,
            )
        ),
        _11(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    - - -
                    - W -
                """,
                a0 = a7, b0 = a4, c0 = a8,
                a1 = a2, b1 = a5, c1 = a3,
                a2 = a1, b2 = a6, c2 = a5,
                a3 = a5, b3 = W, c3 = a4,
            )
        ),
        _12(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    - - -
                    - - W
                """,
                a0 = a7, b0 = a4, c0 = a8,
                a1 = a2, b1 = a5, c1 = a3,
                a2 = a1, b2 = a6, c2 = a7,
                a3 = a4, b3 = a1, c3 = W,
            )
        ),

    }

    enum class WinMonochrome(
        override val matrix: Matrix3x4,
    ) : CombinationMatrixEnum3x4 {
        _1(
            Matrix3x4(
                scheme = """
                    1 1 1
                    - - -
                    - - -
                    - - -
                """,
                winItemList = listOf(a1),
                a0 = a1, b0 = a1, c0 = a1,
                a1 = a2, b1 = a5, c1 = a8,
                a2 = a3, b2 = a6, c2 = a4,
                a3 = a2, b3 = a5, c3 = a7,
            )
        ),
        _2(
            Matrix3x4(
                scheme = """
                    - - -
                    1 1 1
                    - - -
                    - - -
                """,
                winItemList = listOf(a1),
                a0 = a2, b0 = a5, c0 = a8,
                a1 = a1, b1 = a1, c1 = a1,
                a2 = a3, b2 = a6, c2 = a4,
                a3 = a2, b3 = a5, c3 = a7,
            )
        ),
        _3(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    1 1 1
                    - - -
                """,
                winItemList = listOf(a1),
                a0 = a2, b0 = a5, c0 = a8,
                a1 = a3, b1 = a6, c1 = a4,
                a2 = a1, b2 = a1, c2 = a1,
                a3 = a2, b3 = a5, c3 = a7,
            )
        ),
        _4(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    - - -
                    1 1 1
                """,
                winItemList = listOf(a1),
                a0 = a2, b0 = a5, c0 = a8,
                a1 = a3, b1 = a6, c1 = a4,
                a2 = a6, b2 = a5, c2 = a7,
                a3 = a1, b3 = a1, c3 = a1,
            )
        ),
    }

    enum class WinMonochromeWithWild(
        override val matrix: Matrix3x4,
    ) : CombinationMatrixEnum3x4 {
        _1(
            Matrix3x4(
                scheme = """
                    1 W 1
                    - - -
                    - - -
                    - - -
                """,
                winItemList = listOf(a1),
                a0 = a1, b0 = W, c0 = a1,
                a1 = a2, b1 = a5, c1 = a8,
                a2 = a3, b2 = a6, c2 = a4,
                a3 = a2, b3 = a5, c3 = a7,
            )
        ),
        _2(
            Matrix3x4(
                scheme = """
                    - - -
                    1 W 1
                    - - -
                    - - -
                """,
                winItemList = listOf(a1),
                a0 = a2, b0 = a5, c0 = a8,
                a1 = a1, b1 = W, c1 = a1,
                a2 = a3, b2 = a6, c2 = a4,
                a3 = a2, b3 = a5, c3 = a7,
            )
        ),
        _3(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    1 W 1
                    - - -
                """,
                winItemList = listOf(a1),
                a0 = a2, b0 = a5, c0 = a8,
                a1 = a3, b1 = a6, c1 = a4,
                a2 = a1, b2 = W, c2 = a1,
                a3 = a2, b3 = a5, c3 = a7,
            )
        ),
        _4(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    - - -
                    1 W 1
                """,
                winItemList = listOf(a1),
                a0 = a2, b0 = a5, c0 = a8,
                a1 = a3, b1 = a6, c1 = a4,
                a2 = a6, b2 = a5, c2 = a7,
                a3 = a1, b3 = W, c3 = a1,
            )
        ),
    }

    enum class WinColorful(
        override val matrix: Matrix3x4,
    ) : CombinationMatrixEnum3x4 {
        _1(
            Matrix3x4(
                scheme = """
                    2 2 2
                    - - -
                    - - -
                    1 1 1
                """,
                winItemList = listOf(a1, a2),
                a0 = a2, b0 = a2, c0 = a2,
                a1 = a3, b1 = a6, c1 = a4,
                a2 = a6, b2 = a5, c2 = a7,
                a3 = a1, b3 = a1, c3 = a1,
            )
        ),
        _2(
            Matrix3x4(
                scheme = """
                    2 2 2
                    1 1 1
                    - - -
                    - - -
                """,
                winItemList = listOf(a1, a2),
                a0 = a2, b0 = a2, c0 = a2,
                a1 = a1, b1 = a1, c1 = a1,
                a2 = a6, b2 = a5, c2 = a7,
                a3 = a3, b3 = a6, c3 = a4,
            )
        ),
        _3(
            Matrix3x4(
                scheme = """
                    2 2 2
                    - - -
                    1 1 1
                    - - -
                """,
                winItemList = listOf(a1, a2),
                a0 = a2, b0 = a2, c0 = a2,
                a1 = a6, b1 = a5, c1 = a7,
                a2 = a1, b2 = a1, c2 = a1,
                a3 = a3, b3 = a6, c3 = a4,
            )
        ),
        _4(
            Matrix3x4(
                scheme = """
                    - - -
                    2 2 2
                    1 1 1
                    - - -
                """,
                winItemList = listOf(a1, a2),
                a0 = a6, b0 = a5, c0 = a7,
                a1 = a2, b1 = a2, c1 = a2,
                a2 = a1, b2 = a1, c2 = a1,
                a3 = a3, b3 = a6, c3 = a4,
            )
        ),
        _5(
            Matrix3x4(
                scheme = """
                    - - -
                    - - -
                    1 1 1
                    2 2 2
                """,
                winItemList = listOf(a1, a2),
                a0 = a6, b0 = a5, c0 = a7,
                a1 = a3, b1 = a6, c1 = a4,
                a2 = a1, b2 = a1, c2 = a1,
                a3 = a2, b3 = a2, c3 = a2,
            )
        ),
        _6(
            Matrix3x4(
                scheme = """
                    3 3 3
                    - - -
                    1 1 1
                    2 2 2
                """,
                winItemList = listOf(a1, a2, a3),
                a0 = a3, b0 = a3, c0 = a3,
                a1 = a5, b1 = a6, c1 = a4,
                a2 = a1, b2 = a1, c2 = a1,
                a3 = a2, b3 = a2, c3 = a2,
            )
        ),
        _7(
            Matrix3x4(
                scheme = """
                    3 3 3
                    4 4 4
                    1 1 1
                    2 2 2
                """,
                winItemList = listOf(a1, a2, a3, a4),
                a0 = a3, b0 = a3, c0 = a3,
                a1 = a4, b1 = a4, c1 = a4,
                a2 = a1, b2 = a1, c2 = a1,
                a3 = a2, b3 = a2, c3 = a2,
            )
        ),
    }

    enum class WinColorfulWithWild(
        override val matrix: Matrix3x4,
    ) : CombinationMatrixEnum3x4 {
        _1(
            Matrix3x4(
                scheme = """
                    - - -
                    1 1 W
                    - - -
                    W 2 2
                """,
                winItemList = listOf(a1, a2),
                a0 = a3, b0 = a4, c0 = a5,
                a1 = a1, b1 = a1, c1 = W,
                a2 = a5, b2 = a6, c2 = a7,
                a3 = W, b3 = a2, c3 = a2,
            )
        ),
        _2(
            Matrix3x4(
                scheme = """
                    - - -
                    W 1 W
                    - - -
                    2 2 2
                """,
                winItemList = listOf(a1, a2),
                a0 = a3, b0 = a4, c0 = a5,
                a1 = W, b1 = a1, c1 = W,
                a2 = a5, b2 = a6, c2 = a7,
                a3 = a2, b3 = a2, c3 = a2,
            )
        ),
        _3(
            Matrix3x4(
                scheme = """
                    1 1 1
                    W 1 W
                    - - -
                    2 2 2
                """,
                winItemList = listOf(a1, a2),
                a0 = a1, b0 = a1, c0 = a1,
                a1 = W, b1 = a1, c1 = W,
                a2 = a5, b2 = a6, c2 = a7,
                a3 = a2, b3 = a2, c3 = a2,
            )
        ),
        _4(
            Matrix3x4(
                scheme = """
                    1 1 1
                    W 1 W
                    2 2 2
                    2 W 2
                """,
                winItemList = listOf(a1, a2),
                a0 = a1, b0 = a1, c0 = a1,
                a1 = W, b1 = a1, c1 = W,
                a2 = a2, b2 = a2, c2 = a2,
                a3 = a2, b3 = W, c3 = a2,
            )
        ),
    }

}