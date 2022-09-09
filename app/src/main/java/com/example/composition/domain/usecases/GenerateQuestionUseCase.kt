package com.example.composition.domain.usecases

import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository

//генерирует вопрос,с вариантами ответов
class GenerateQuestionUseCase(private val repository: GameRepository) {
//передаём максимальное значение суммы и количество вариантов ответов
    operator fun invoke(maxSumValue: Int): Question{//operator fun invoke нужен для того ,
    // чтобы вызывать  GetGameSettingsUseCase будто мы вызвали метод - GenerateQuestionUseCase()
        return repository.generateQuestion(maxSumValue,COUNT_OF_OPTIONS)
    }

    private companion object{
        private const val COUNT_OF_OPTIONS = 6//количество вариантов ответов
    }
}