package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//класс уровень

@Parcelize            //аннотация интерфейса Parcelize
enum class Level: Parcelable                   // реализуем интерфейс Parcelable
{
    TEST,EASY,NORMAL,HARD           //может быть только 4 экземпляра класса, перечисление enum
}