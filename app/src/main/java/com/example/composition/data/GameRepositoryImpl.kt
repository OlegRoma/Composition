package com.example.composition.data

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import java.lang.Integer.max
import kotlin.math.min
import kotlin.random.Random

//класс object  гарантирует что будет использоваться один и тот же объект GameRepositoryImpl
object GameRepositoryImpl: GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1
    //генерация вопроса:
    // 1)видимое значение суммы в кружочке
    // 2)видимое число отображается в левом квадрате
    // 3)варианты ответов
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        //получаем значение суммы(видимая)
           val sum = Random.nextInt(MIN_SUM_VALUE,maxSumValue + 1)//+1 потому что число maxSumValue не включительно
        //получаем значение видимого числа
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE,sum)// от 1 до Верхней границы, не включительно
        //коллекция для хранения вариантов ответов
        val options = HashSet<Int>()//характеризуется тем что в ней не может быть одинаковых значений
        //правильный ответ
        val rightAnswer = sum-visibleNumber
        //поместить правильный ответ в нашу коллекцию для вариантов ответов
        options.add(rightAnswer)
        //генерация остальных неправильных  вариантов ответов
        //Нижняя граница диапазона ответов
        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE) //предлагаемые варианты ответов будут
                                                                         // возле правильного ответа,но не меньше 1
        //Верхняя граница диапазона ответов
       val to = min(maxSumValue,rightAnswer+countOfOptions)//но не больше максимальной суммы
        //генерируем коллекцию ответов,пока её размер не будет равен countOfOptions
        while (options.size<countOfOptions){
            options.add(Random.nextInt(from,to))            //добавляем новое значение
        }
        return Question(sum,visibleNumber, options.toList())//возвращаем полученные значения
    }

    override fun getGameSettings(level: Level): GameSettings {
       return when(level){
           Level.TEST-> {
               GameSettings(
                   10,
                   3,
                   50,
                   8
               )
           }

           Level.EASY-> {
               GameSettings(
                   10,
                   10,
                   70,
                   60
               )
           }

           Level.NORMAL-> {
               GameSettings(
                   20,
                   20,
                   80,
                   40

               )
           }

           Level.HARD-> {
               GameSettings(
                   30,
                   30,
                   90,
                   40
               )
           }

       }
    }

}