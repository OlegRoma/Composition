package com.example.composition.domain.entity


//класс вопрос
data  class Question (
    val sum: Int,            //значение суммы ,которая отображается в кружке
    val visibleNumber: Int,  //значение  видимого числа,отображаются слева в квадрате
    val options: List<Int>   //варианты ответов
    ){
    val rightAnswer = sum - visibleNumber  //правильный ответ
}