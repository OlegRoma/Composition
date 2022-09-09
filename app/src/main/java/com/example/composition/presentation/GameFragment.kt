package com.example.composition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResault
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import org.w3c.dom.Text

import java.lang.RuntimeException



class GameFragment : Fragment() {
//получить application
    //отложенная инициализация, при первом обращении к viewModel будет произведена инициализация
 //не забывай ,обращаться к viewModel можно только после создания View то есть в методе onViewCreated
//    private val viewModel by lazy {
//        ViewModelProvider(
//            this,
//        //  так как у нас class GameViewModel(application: Application): AndroidViewModel(application)
//         //  нужно получить application
//            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
//        )[GameViewModel::class.java]
//    }
    //отложенная инициализация, при первом обращении к viewModel будет произведена инициализация
    //не забывай ,обращаться к viewModel можно только после создания View то есть в методе onViewCreated
    private val viewModel by lazy {ViewModelProvider(this )[GameViewModel::class.java]}

    //отложенная инициализация
    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)            //устанавливаем значение во все варианты ответов
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }
        lateinit var binding: FragmentGameBinding
        private lateinit var level: Level//переменная для хранения уровня
        // private lateinit var viewModel: GameViewModel

        //фрагмент создан но ещё не видем,здесь передаём аргументы для фрагментa
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            parseArgs()          //получаем объект Level для отображения фрагментa
        }

    //создание View
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentGameBinding.inflate(inflater,container,false)
            // Inflate the layout for this fragment
            return binding.root
        }

          //здесь View уже создана и с ней можно работать
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            //viewModel = ViewModelProvider(this)[GameViewModel::class.java]
            observeViewModel()                  //подписываемся на LiveData из ViewModel,Отложенная инициализация
            setClickListenersToOptioons()        //слушатель кликов на варианты ответов
            viewModel.startGame(level)          //Стартуем игру

        }

      //слушатель кликов на варианты ответов
    private fun setClickListenersToOptioons() {
    for (tvOption in tvOptions) {            //устанавливаем слушатель во все варианты ответов
        tvOption.setOnClickListener {
            viewModel.chooseAnswer(tvOption.text.toString().toInt())//передаём выбранный вариант ответа
        }
    }

    }


        //подписываемся на LiveData из ViewModel
        private fun observeViewModel(){
            //подписались на вопрос
            viewModel.question.observe(viewLifecycleOwner) {
                binding.tvSum.text = it.sum.toString()          //устанавливаем значение в окно сумма
                binding.tvLeftNumber.text = it.visibleNumber.toString()//устанавливаем значение в окно видимый номер
                for (i in 0 until tvOptions.size) {        //устанавливаем значение во все варианты ответов
                    tvOptions[i].text = it.options[i].toString()//проходим по  mutableList,Отложенная инициализация
                }
            }
          // подписались на процент правильных ответов
            viewModel.percentOfRigghtAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it,true)// второй параметр только для api 24 и выше
            }
           // подписались на проверку достаточно ли правильных ответов(true/false),устанавливаем цвет
            viewModel.enoughCount.observe(viewLifecycleOwner) {
             val color = getColorByState(it)                 //получаем цвет,приходит True или false
                binding.tvAnswersProgress.setTextColor(color)//устанавливаем цвет текста

            }

           // подписались на проверку достаточен ли процент правильных ответов(true/false),устанавливаем цвет
            viewModel.enoughPercent.observe(viewLifecycleOwner) {
                val color = getColorByState(it)                            //красный или зелёный
                binding.progressBar.progressTintList = ColorStateList.valueOf(color)//устанавливаем цвет progressBar
            }
             //подписываемся на время
            viewModel.formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it            //устанавливаем это значение,
            }
             //подписываемся на минимальный процент чтобы установить secondaryProgress
            viewModel.minPercent.observe(viewLifecycleOwner) {

                binding.progressBar.secondaryProgress = it //отображается чёрным цветом
            }
            //подписываемся на gameResult,для запуска GameFinishedFragment
            viewModel.gameResult.observe(viewLifecycleOwner) {

                launchGameFinishedFragment(it)  //запускаем GameFinishedFragment
            }
               //строка правильных ответов
            viewModel.progressAnswers.observe(viewLifecycleOwner){
                binding.tvAnswersProgress.text = it
            }

        }
            //получаем цвет,приходит True или false
    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light    //получаем цвет
        }else {android.R.color.holo_red_light   }
       return ContextCompat.getColor(requireContext(),colorResId)//мы находимся в фрагменте,поэтому Context
                                                                 // передаём методом requireContext()
    }
        //получаем объект Level ,преобразуя набор байтов обратно в объект Level
        private fun parseArgs(){
            requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
                level = it  //получаем объект типа <Level>,если он не равен null присваиваем переменной
            }
        }

        //переход к другому фрагменту: ( GameFragment -> GameFinishedFragment)
        private fun launchGameFinishedFragment(gameResault: GameResault){
            requireActivity().supportFragmentManager.beginTransaction()               //начать транзакцию
                .replace(R.id.main_container,GameFinishedFragment.newInstance(gameResault))//указываем контейнер,создаем фрагмент
                .addToBackStack(null)                         //добавление Fragment в стек без имени
                .commit()                                             //закрыть транзакцию
        }

        companion object{
        const val NAME = "GameFragment"//присвоили имя фрагменту

        private const val KEY_LEVEL = "level"//ключ для Bundle()

        //метод получения экземпляра фрагмента,принимает класс Level,передать его мы можем только
        // интерфейсoм Serializable,а класс Level неявно его реализует ,так как является классом enum.
        //данные классa преобразуются в набор байтов
        fun newInstance(level: Level):GameFragment{
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL,level)          //преобразуются в набор байтов
                }
            }
        }
    }
 }


