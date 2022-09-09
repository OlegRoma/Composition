package com.example.composition.presentation

import android.os.Bundle
import android.service.chooser.ChooserTarget
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.composition.R
import com.example.composition.databinding.FragmentWelcomeBinding
import java.lang.RuntimeException


class WelcomeFragment : Fragment() {

   lateinit  var binding: FragmentWelcomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater,container, false)

        return binding.root
    }
      //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUnderstand.setOnClickListener {
          launchChooseLevelFragment()  //вызов методa перехода к ChooseLevelFragment
        }
    }



           //переход к другому фрагменту: (WelcomeFragment -> ChooseLevelFragment)
    private fun launchChooseLevelFragment(){
        requireActivity().supportFragmentManager.beginTransaction()//начать транзакцию
            .replace(R.id.main_container,ChooseLevelFragment.newInstance())//передаём контейнер и экземпляр класса фрагмента
            .addToBackStack(ChooseLevelFragment.NAME)    //добавление ChooseLevelFragment в стек под именем
            .commit()                                  //закрыть транзакцию
    }




}