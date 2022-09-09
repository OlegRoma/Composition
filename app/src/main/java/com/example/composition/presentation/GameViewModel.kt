package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResault
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase
             //ViewModel для GameFragment
            //Если нужен context,наследуемся от AndroidViewModel
           //class GameViewModel(application: Application): AndroidViewModel(application) {
          //private val context = application //передача контекста
class GameViewModel: ViewModel() {
     //ссылки на репозиторий,UseCase
    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)



    private lateinit var gameSettings: GameSettings // для настройки
    private lateinit var level:Level                //для уровня
    private var timer: CountDownTimer? = null        //  для timer
    private var countOfRightAnswer = 0             //правильный ответ
    private var countOfQuestions = 0                //вопрос

     // LiveData следующий вопрос
    private val _question = MutableLiveData<Question>()
     val question:LiveData<Question> = _question
    // LiveData процент правильных ответов
    private val _percentOfRigghtAnswers = MutableLiveData<Int>()
     val percentOfRigghtAnswers:LiveData<Int> = _percentOfRigghtAnswers
    // LiveData строка правильных ответов
    private val _progressAnswers = MutableLiveData<String>()
     val progressAnswers:LiveData<String> = _progressAnswers
    //LiveData для времени
    private val _formattedTime = MutableLiveData<String>()           //изменяемая
     val formattedTime:LiveData<String> = _formattedTime      //неизменяемая
    // LiveData количество правильных ответов
    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount:LiveData<Boolean> = _enoughCount
    // LiveData проверка достаточно ли правильных ответов
    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent:LiveData<Boolean> = _enoughPercent
    // LiveData минимальное количество процентов для прохождение уровня(secondaryProgressTint)
    private val _minPercent = MutableLiveData<Int>()
     val minPercent:LiveData<Int> = _minPercent
    // LiveData  завершение игры и передача результатов
    private val _gameResult = MutableLiveData<GameResault>()
     val gameResult:LiveData<GameResault> = _gameResult

    //Стартуем игру для этого уровня :получаем настройки игры ,запускаем таймер,генерируем вопрос
    //метод будет запускаться в фрагменте
    fun startGame(level: Level){
        getGameSettings(level)  //получаем настройки для этого уровня
        startTimer()             //запуск таймера
        generateQuestion()        //генерация вопросов
        updateProgress()          //установка данных и их обновления
    }
     //получение настроек игры
    private fun getGameSettings(level: Level){
        this.level = level                               //присваивание переданного значение
        this.gameSettings = getGameSettingsUseCase(level)//присваивание переданного значение
        _minPercent.value = gameSettings.minPercentOfRightAnswers//передача LiveDate минимального процентa
    }

      //запуск таймера
    private fun startTimer(){
         timer = object : CountDownTimer(//принимает: количество секунд ,На сколько timer запускается
                                          //И как часто обновлять оставшееся время
    gameSettings.gameTimeInSeconds* MILLIS_IN_SECONDS,//приводим к миллисекундам (тип Long)
                MILLIS_IN_SECONDS                                //1000 миллисекунд,раз в секунду обновлять

        ){
             //передача времени LiveDate
            override fun onTick(millisFineshed: Long) {
               _formattedTime.value = formatTime(millisFineshed)//передача отформатированного  времени
            }
             //Время вышло
            override fun onFinish() {
                finishGame()          //завершить игру
            }
        }
        timer?.start()                //Стартуем таймер
    }
      //когда мы уходим c GameFragment отменяем таймер
    override fun onCleared() {
        super.onCleared()
        timer?.cancel()             //отменяем таймер ,останавливаем

    }
      //генерируем следующий вопрос
    private fun generateQuestion(){//передача вопросa в зависимости от настроек (gameSettings.maxSumValue)
     _question.value = generateQuestionUseCase(gameSettings.maxSumValue)   //передача В LiveData
    }

    //ответ на вопрос,принимает выбранный вариант ответа
    fun chooseAnswer(number: Int){
        checkAnswer(number)         //подсчёт вопросов и правильных ответов
        updateProgress()              //обновление данных
        generateQuestion()            //генерируем следующий вопрос
    }
    //обновление данных о проценте правильных ответов и строки о правильных ответов
    private fun updateProgress(){
        val percent = calculatePercentOfRightAnswers() //вычислить процент правильных ответов
        _percentOfRigghtAnswers.value = percent
        _progressAnswers.value = String.format(                      //формируем строку
         "правильных ответов  %s (минимум %s)",countOfRightAnswer,   //правильных ответов
         gameSettings.minCountOfRightAnswers                       //необходимый минимум правильных ответов
        )
        //проверка достаточно ли правильных ответов(true/false)
        _enoughCount.value = countOfRightAnswer >= gameSettings.minCountOfRightAnswers
        //проверка достаточен ли процент правильных ответов(true/false)
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers


    }
      //вычислить процент правильных ответов
    private fun calculatePercentOfRightAnswers(): Int{
          if (countOfQuestions == 0){    //чтобы не делить на 0
              return 0
          }
        return ((countOfRightAnswer/countOfQuestions.toDouble())*100).toInt()//в начале Нужно привести к типу Double
    }
      //подсчёт вопросов и правильных ответов
    private fun checkAnswer(number: Int){
        val rightAnswer = question.value?.rightAnswer  //правильный ответ из объекта question
        if (number == rightAnswer){
            countOfRightAnswer++        //правильный ответ,считаем
        }
        countOfQuestions++               //вопрос,считаем
    }

     //форматирование времени в String
    private fun formatTime(millisFineshed: Long):String{
        val seconds = millisFineshed/ MILLIS_IN_SECONDS   //количество секунд
        val minutes = seconds/ SECONDS_IN_MINUTES            //количество минут
        val leftSeconds = seconds-(minutes* SECONDS_IN_MINUTES)//количество оставшихся секунд
        return String.format("%02d:%02d",minutes,leftSeconds)  //возвращает строку
    }



        // Конец игры и передача параметров через ViewModel следующему фрагменту
    private fun finishGame(){
        _gameResult.value = GameResault(
        enoughCount.value ==true && enoughPercent.value == true,//выиграли или нет
            countOfRightAnswer,                                 //количество правильных ответов
            countOfQuestions,                                   //количество вопросов
            gameSettings                                        //настройки игры с учётом уровня
        )

    }

    companion object{
        private const val MILLIS_IN_SECONDS = 1000L //таймер требует тип Long
        private const val SECONDS_IN_MINUTES = 60
    }




}