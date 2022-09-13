package spintimez.slot.com.game.screens

import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import spintimez.slot.com.R
import spintimez.slot.com.game.actors.slot.SlotPanel
import spintimez.slot.com.game.manager.GameDataStoreManager
import spintimez.slot.com.game.manager.NavigationManager
import spintimez.slot.com.game.util.*
import spintimez.slot.com.util.Once
import spintimez.slot.com.util.toMS
import spintimez.slot.com.game.util.Layout.Game as LG

class GameScreen(override val activity: FragmentActivity): Screen(activity) {

    companion object {
        const val BET_STEP = 500L
        const val BET_MIN  = BET_STEP
        const val BET_MAX  = 5_000L

        const val TIME_WAIT_AFTER_AUTOSPIN = 1f
    }

    private val balancePanelText = TextView(activity)
    private val betPanelText     = TextView(activity)
    private val spinButton       = Button(activity)
    private val autoButton       = Button(activity)
    private val menuButton       = Button(activity)
    private val plusButton       = Button(activity)
    private val minusButton      = Button(activity)
    private val slotPanel        = SlotPanel(activity)

    private val betFlow = MutableStateFlow(BET_MIN)

    private val autoSpinStateFlow = MutableStateFlow(AutospinState.DEFAULT)

    private val onceStartAutoSpin = Once()



    override fun show(parent: ConstraintLayout) {
        super.show(parent)
        rootViewGroup.setBackgroundResource(R.drawable.background)
    }

    override fun ViewGroup.addActorsOnRootViewGroup() {
        addSlotPanel()
        addBalancePanelText()
        addBetPanelText()
        addSpinButton()
        addAutoButton()
        addMenuButton()
        addPlusButton()
        addMinusButton()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------
    private fun ViewGroup.addSlotPanel() {
        addView(slotPanel.rootViewGroup)
        disposableList.add(slotPanel)

        slotPanel.rootViewGroup.apply {
            setBounds(LG.slotPanel.x, LG.slotPanel.y, LG.slotPanel.w, LG.slotPanel.h)

            coroutineMain.launch {
                MutableStateFlow(0).apply {
                    collect {
                        animate().apply {
                            duration = 1f.toMS
                            translationYBy(20f)

                            withEndAction {
                                animate().apply {
                                    duration = 2f.toMS
                                    translationYBy(-40f)

                                    withEndAction {
                                        animate().apply {
                                            duration = 1f.toMS
                                            translationYBy(20f)
                                            interpolator = LinearInterpolator()

                                            withEndAction { value++ }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        slotPanel.initialize()
    }

    private fun ViewGroup.addBalancePanelText() {
        addView(balancePanelText)

        balancePanelText.apply {
            setBounds(LG.balance.x, LG.balance.y, LG.balance.w, LG.balance.h)
            setBackgroundResource(R.drawable.balance_panel)
            gravity = Gravity.CENTER
            setTextColor(GameColor.orange)
            setPadding(10 * DENSITY)
            typeface = Typeface.createFromAsset(activity.assets, "FredokaOne.ttf")
            setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            animationInfinityFlyRotate(coroutineMain, 1f, 1f)
        }

        updateBalance()
    }

    private fun ViewGroup.addBetPanelText() {
        addView(betPanelText)

        betPanelText.apply {
            setBounds(LG.bet.x, LG.bet.y, LG.bet.w, LG.bet.h)
            setBackgroundResource(R.drawable.bet_panel)
            gravity = Gravity.CENTER
            setTextColor(GameColor.orange)
            setPadding(10 * DENSITY)
            typeface = Typeface.createFromAsset(activity.assets, "FredokaOne.ttf")
            setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            animationInfinityFlyRotate(coroutineMain, 1f, 1f)
        }

        updateBet()
    }

    private fun ViewGroup.addSpinButton() {
        addView(spinButton)

        spinButton.apply {
            setBackgroundResource(R.drawable.state_spin)
            setBounds(LG.spin.x, LG.spin.y, LG.spin.w, LG.spin.h)
            animationInfinityFlyRotate(coroutineMain, 30f, 1f)

            setOnClickListener { spinHandler() }
        }
    }

    private fun ViewGroup.addAutoButton() {
        addView(autoButton)

        autoButton.apply {
            setBackgroundResource(R.drawable.state_auto)
            setBounds(LG.auto.x, LG.auto.y, LG.auto.w, LG.auto.h)
            animationInfinityFlyRotate(coroutineMain, 30f, 1f)

            setOnClickListener { autoHandler() }
        }
    }

    private fun ViewGroup.addMenuButton() {
        addView(menuButton)

        menuButton.apply {
            setBackgroundResource(R.drawable.state_menu)
            setBounds(LG.menu.x, LG.menu.y, LG.menu.w, LG.menu.h)
            animationInfinityFlyRotate(coroutineMain, 30f, 1f)

            setOnClickListener { NavigationManager.back(activity) }
        }
    }

    private fun ViewGroup.addPlusButton() {
        addView(plusButton)

        plusButton.apply {
            setBackgroundResource(R.drawable.state_plus)
            setBounds(LG.plus.x, LG.plus.y, LG.plus.w, LG.plus.h)
            animationInfinityFlyRotate(coroutineMain, -30f, 1f)

            setOnClickListener { betPlusHandler() }
        }
    }

    private fun ViewGroup.addMinusButton() {
        addView(minusButton)

        minusButton.apply {
            setBackgroundResource(R.drawable.state_minus)
            setBounds(LG.minus.x, LG.minus.y, LG.minus.w, LG.minus.h)
            animationInfinityFlyRotate(coroutineMain, -30f, 1f)

            setOnClickListener { betMinusHandler() }
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------
    private fun updateBalance() {
        coroutineMain.launch(Dispatchers.IO) {
            GameDataStoreManager.Balance.collect { balance ->
                withContext(Dispatchers.Main) { balancePanelText.text = balance?.transformToBalanceFormat() }
            }
        }
    }

    private fun updateBet() {
        coroutineMain.launch {
            betFlow.collect { bet -> betPanelText.text = bet.transformToBalanceFormat() }
        }
    }

    private fun betPlusHandler() {
        coroutineMain.launch {
            with(betFlow) {
                value = if ((value + BET_STEP) < BET_MAX) value + BET_STEP else BET_MAX
            }
        }
    }

    private fun betMinusHandler() {
        coroutineMain.launch {
            with(betFlow) {
                value = if ((value - BET_STEP) > BET_MIN) value - BET_STEP else BET_MIN
            }
        }
    }

    private fun spinHandler() {
            spinButton.isEnabled = false
            autoButton.isEnabled = false
            plusButton.isEnabled = false
            minusButton.isEnabled = false

            coroutineMain.launch {
                if (checkBalance()) spinAndSetResult()

                spinButton.isEnabled = true
                autoButton.isEnabled = true
                plusButton.isEnabled = true
                minusButton.isEnabled = true
            }
    }

    private fun autoHandler() {
        autoSpinStateFlow.apply {
            value = if (value == AutospinState.DEFAULT) AutospinState.GO else AutospinState.DEFAULT
        }
        onceStartAutoSpin.once { startAutospin() }
    }

    private suspend fun SpinResult.calculateAndSetResult() {
        val slotItemPriceFactor: Float = winSlotItemSet?.map { it.priceCoff }?.sum() ?: 0f
        val sumPrice = (betFlow.value * slotItemPriceFactor).toLong()
        coroutineMain.launch(Dispatchers.IO) {
            GameDataStoreManager.Balance.update { it!! + sumPrice }
        }
    }

    private suspend fun spinAndSetResult() {
        slotPanel.spin().apply { calculateAndSetResult() }
    }

    private suspend fun checkBalance() = CompletableDeferred<Boolean>().also { continuation ->
        GameDataStoreManager.Balance.update {
            val balance = it ?: 0L
            if ((balance - betFlow.value) >= 0) {
                // Достаточно средств для запуска
                continuation.complete(true)
                balance - betFlow.value
            } else {
                // Недостаточно средств для запуска
                continuation.complete(false)
                balance
            }
        }
    }.await()

    private fun startAutospin() {
        coroutineMain.launch {
            autoSpinStateFlow.collect { state ->
                when (state) {
                    AutospinState.GO      -> {
                        autoButton.isSelected = true
                        spinButton.isEnabled  = false
                        plusButton.isEnabled  = false
                        minusButton.isEnabled = false

                        coroutineMain.launch {
                            while (autoSpinStateFlow.value == AutospinState.GO) {
                                if (checkBalance()) {
                                    spinAndSetResult()
                                    delay(TIME_WAIT_AFTER_AUTOSPIN.toMS)
                                } else autoSpinStateFlow.value = AutospinState.DEFAULT
                            }

                            withContext(Dispatchers.Main) {
                                autoButton.apply {
                                    isSelected = false
                                    isEnabled = true
                                }
                                spinButton.isEnabled  = true
                                plusButton.isEnabled  = true
                                minusButton.isEnabled = true
                            }
                        }
                    }

                    AutospinState.DEFAULT -> autoButton.isEnabled = false
                }
            }
        }
    }

}