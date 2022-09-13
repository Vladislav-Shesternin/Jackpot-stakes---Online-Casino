package spintimez.slot.com.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import spintimez.slot.com.databinding.FragmentGameBinding
import spintimez.slot.com.game.screens.MenuScreen
import spintimez.slot.com.game.manager.NavigationManager
import spintimez.slot.com.game.util.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameFragment: Fragment() {

    private val coroutineMain           = CoroutineScope(Dispatchers.Main)
    private var previousScreen: Screen? = null

    lateinit var binding: FragmentGameBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavigationManager.navigate(MenuScreen(requireActivity()))

        coroutineMain.launch {
            NavigationManager.screenFlow.collect { screen ->
                previousScreen?.hide(binding.rootLayout)
                screen.show(binding.rootLayout)
                previousScreen = screen
            }
        }
    }


}