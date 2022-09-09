package com.example.composition.domain.entity

import android.os.Parcelable
import com.example.composition.domain.entity.GameSettings
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


//класс результат игры
@Parcelize            //аннотация интерфейса Parcelize
data class GameResault (
     val winner: Boolean,             //значение как закончилась игра Победа или проигрыш
     val countOfRightAnswers: Int,    //количество правильных ответов
     val countOfQuestion: Int,        //общее количество вопросов,чтобы вычислить процент правильных ответов
     val gameSettings: GameSettings  //настройки игры,для отображения пользователю
         ): Parcelable                   // реализуем интерфейс Parcelable
