package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DEFAULT_ARGS_KEY
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResault


class GameFinishedFragment : Fragment() {
    lateinit var binding: FragmentGameFinishedBinding
    private  lateinit var gameResault: GameResault

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()             //получаем объект GameResault для отображения фрагментa
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameFinishedBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setuoClickListener()
        bindView()

    }
      //устанавливаем все значения
    private fun bindView(){
        with(binding){
            emojiResult.setImageResource(getSmileResId())         //картинка
            tvRequiredAnswers.text = String.format("необходимые количество правильных ответов: %s",
            gameResault.gameSettings.minCountOfRightAnswers)
            tvScoreAnswers.text = String.format("ваш счёт: %s",gameResault.countOfRightAnswers)
            tVRequiredPercentage.text = String.format("необходимый процент правильных ответов: %s",gameResault.gameSettings.minPercentOfRightAnswers)
            tvScopePercentage.text = String.format("процент правильных ответов %s",getPercentOfRightAnswers())
        }
    }
           //возвращает процент правильных ответов
    private fun getPercentOfRightAnswers() = with(gameResault){
        if (countOfQuestion == 0){       //чтобы не делить на 0
            0
        }else{
            ((countOfRightAnswers/countOfQuestion.toDouble())*100).toInt()
        }
    }
    //выбор  картинки
    private fun getSmileResId(): Int {
        return if (gameResault.winner){
            R.drawable.emoticon                //Победа
        }
        else{
            R.drawable.grystnii_smailik       // проигрыш
        }
    }

   // переход к  уровню ChooseLevelFragment,установка слушателя
    private fun setuoClickListener(){
        //подключаем слушатель на кнопку назад
        //принимает объект viewLifecycleOwner-жизненный цикл
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                retryGame() // переход к  уровню ChooseLevelFragment
            }
        }
        //подключаем слушатель на кнопку(попробовать снова - buttonRetry)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        binding.buttonRetry.setOnClickListener {
            retryGame() // переход к  уровню ChooseLevelFragment
        }
    }

      //получаем объект GameResault ,преобразуя набор байтов обратно в объект GameResault
    private fun parseArgs(){
        requireArguments().getParcelable<GameResault>(KEY_GAME_RESULT)?.let {
            gameResault = it//получаем объект типа <GameResault>,если он не равен null присваиваем переменной
        }
      }
      //переход к Fragment выбора уровня ChooseLevelFragment
    private fun retryGame(){
        requireActivity().supportFragmentManager.popBackStack(GameFragment.NAME,FragmentManager
            .POP_BACK_STACK_INCLUSIVE)//-эта инструкция  удалит  фрагмент GameFragment.NAME из BACK_STACK
    }                                          //и мы попадём на фрагмент ChooseLevelFragment
    //можно так:(ChooseLevelFragment.NAME,
    // 0) - эта инструкция оставит фрагмент ChooseLevelFragment в BACK_STACK

        companion object{

         private const val KEY_GAME_RESULT = "game_result"//ключ для Bundle()



            //метод получения экземпляра фрагмента,принимает класс GameResault,передать его мы можем только
            // интерфейсoм Serializable,а класс GameResault его реализует
            //данные классa преобразуются в набор байтов
        fun newInstance(gameResault: GameResault):GameFinishedFragment{
         return GameFinishedFragment().apply {
             arguments = Bundle().apply {
                 putParcelable(KEY_GAME_RESULT,gameResault)   //преобразуются в набор байтов
             }
         }
      }
    }

 }