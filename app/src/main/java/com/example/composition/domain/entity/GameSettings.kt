package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//класс настройки игры
@Parcelize            //аннотация интерфейса Parcelize
 data class GameSettings (
     val maxSumValue: Int,               //максимальное значение суммы
     val minCountOfRightAnswers: Int,    //минимальное количество правильных ответов Для победы
     val minPercentOfRightAnswers: Int,  //минимальный процент правильных ответов Для победы
     val gameTimeInSeconds: Int          //время игры в секундах
         ):Parcelable                   // реализуем интерфейс Parcelable