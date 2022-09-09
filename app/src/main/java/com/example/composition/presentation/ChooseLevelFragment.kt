package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.composition.R
import com.example.composition.databinding.FragmentChooseLevelBinding
import com.example.composition.domain.entity.Level
import java.lang.RuntimeException


class ChooseLevelFragment : Fragment() {

lateinit var binding: FragmentChooseLevelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseLevelBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)//
       //устанавливаем слушатель кликов у кнопок
        binding.buttonLevelTest.setOnClickListener {
            launchGameFragment(Level.TEST)
        }

        binding.buttonLevelEasy.setOnClickListener {
            launchGameFragment(Level.EASY)
        }

        binding.buttonLevelNormal.setOnClickListener {
            launchGameFragment(Level.NORMAL)
        }

        binding.buttonLevelHard.setOnClickListener {
            launchGameFragment(Level.HARD)
        }

    }


    //переход к другому фрагменту: ( ChooseLevelFragment -> GameFragment)
     private fun launchGameFragment(level: Level){
      requireActivity().supportFragmentManager.beginTransaction()     //начать транзакцию
        .replace(R.id.main_container,GameFragment.newInstance(level)) //указываем контейнер,создаем фрагмент
        .addToBackStack(GameFragment.NAME)                            //добавление Fragment в стек под именем
        .commit()                                                     //закрыть транзакцию
}


    companion object{
            //присвоили имя фрагменту
        const val NAME = "ChooseLevelFragment"

        //метод получения экземпляра фрагмента
        fun newInstance(): ChooseLevelFragment{
            return ChooseLevelFragment()
        }
    }


}