package com.example.composition.domain.usecases

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.repository.GameRepository

//получает настройки игры в зависимости от уровня
class GetGameSettingsUseCase(private val repository: GameRepository) {
    operator fun invoke(level: Level): GameSettings{//operator fun invoke нужен для того ,
        // чтобы вызывать GetGameSettingsUseCase будто мы вызвали метод - GenerateQuestionUseCase()
        return repository.getGameSettings(level)
    }
}