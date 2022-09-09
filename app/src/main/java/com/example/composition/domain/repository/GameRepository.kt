package com.example.composition.domain.repository

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question

interface GameRepository {
//возвращает вопрос, в зависимости от максимальной суммы и от количество вариантов ответа
    fun generateQuestion(maxSumValue: Int,countOfOptions: Int ): Question
//возвращает настройки игры в зависимости от уровня
    fun getGameSettings(level: Level): GameSettings
}