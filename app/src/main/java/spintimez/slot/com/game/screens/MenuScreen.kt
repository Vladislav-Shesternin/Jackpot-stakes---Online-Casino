package spintimez.slot.com.game.screens

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import spintimez.slot.com.MainActivity
import spintimez.slot.com.R
import spintimez.slot.com.game.manager.NavigationManager
import spintimez.slot.com.game.util.*
import spintimez.slot.com.util.log
import spintimez.slot.com.game.util.Layout.Menu as LM

class MenuScreen(override val activity: FragmentActivity): Screen(activity) {

    private val playButton = Button(activity)
    private val exitButton = Button(activity)



    override fun show(parent: ConstraintLayout) {
        super.show(parent)
        MainActivity.hideLoader()
        rootViewGroup.setBackgroundResource(R.drawable.background)
    }

    override fun ViewGroup.addActorsOnRootViewGroup() {
        addPlayButton()
        addExitButton()
    }


    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------
    private fun ViewGroup.addPlayButton() {
        addView(playButton)

        playButton.apply {
            isAllCaps = false
            setText(R.string.play)
            setPadding(20 * DENSITY)
            setTextColor(GameColor.orange)
            typeface = Typeface.createFromAsset(activity.assets, "FredokaOne.ttf")
            setBackgroundResource(R.drawable.state_button)
            setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            setBounds(LM.play.x, LM.play.y, LM.play.w, LM.play.h)

            setOnClickListener { NavigationManager.navigate(GameScreen(activity), MenuScreen(activity)) }
        }
    }

    private fun ViewGroup.addExitButton() {
        addView(exitButton)

        exitButton.apply {
            isAllCaps = false
            setText(R.string.exit)
            setPadding(20 * DENSITY)
            setTextColor(GameColor.orange)
            typeface = Typeface.createFromAsset(activity.assets, "FredokaOne.ttf")
            setBackgroundResource(R.drawable.state_button)
            setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            setBounds(LM.exit.x, LM.exit.y, LM.exit.w, LM.exit.h)

            setOnClickListener { NavigationManager.exit(activity) }
        }
    }


}