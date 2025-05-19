package haha

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

data class Alternative(
    val x1: Double,
    val x2: Double
) {
    val f: Double = x1 + 2 * x2
    val g: Double = 3 * x1 - 2 * x2
    val h: Double = -abs(x1 - 4)
}

data class NormalizedCriteria(val fNorm: List<Double>, val gNorm: List<Double>, val hNorm: List<Double>)

fun collectAndNormalizeCriteria(alternatives: List<Alternative>): NormalizedCriteria {
    val fValues = alternatives.map { it.f }
    val gValues = alternatives.map { it.g }
    val hValues = alternatives.map { it.h }

    fun normalize(values: List<Double>): List<Double> {
        val minVal = values.minOrNull() ?: 0.0
        val maxVal = values.maxOrNull() ?: 1.0
        return values.map { (it - minVal) / (maxVal - minVal) }
    }

    return NormalizedCriteria(normalize(fValues), normalize(gValues), normalize(hValues))
}

fun weightedSumModel(
    alternatives: List<Alternative>,
    weightF: Double,
    weightG: Double,
    weightH: Double
): Alternative {
    val normalizedCriteria = collectAndNormalizeCriteria(alternatives)
    val generalizedCriteria = alternatives.indices.map { i ->
        weightF * normalizedCriteria.fNorm[i] +
                weightG * normalizedCriteria.gNorm[i] +
                weightH * normalizedCriteria.hNorm[i]
    }
    return alternatives[generalizedCriteria.indexOf(generalizedCriteria.maxOrNull())]
}

fun idealPointMethod(alternatives: List<Alternative>): Alternative {
    val normalizedCriteria = collectAndNormalizeCriteria(alternatives)
    val distances = alternatives.indices.map { i ->
        sqrt((normalizedCriteria.fNorm[i] - 1).pow(2) +
                (normalizedCriteria.gNorm[i] - 1).pow(2) +
                (normalizedCriteria.hNorm[i] - 1).pow(2))
    }
    return alternatives[distances.indexOf(distances.minOrNull())]
}

fun Double.pow(exponent: Int): Double = Math.pow(this, exponent.toDouble())

// Функция для проверки, принадлежит ли точка ОДР
fun isWithinODR(x1: Double, x2: Double): Boolean {
    return (-3 * x1 + 2 * x2 <= 9) &&
            (3 * x1 + 4 * x2 >= 27) &&
            (2 * x1 + x2 <= 14) &&
            (x1 >= 0) &&
            (x2 >= 0)
}

// Функция для генерации случайной точки в ОДР
fun generateRandomPointInODR(): Alternative {
    val x1Min = 0.0
    val x1Max = 8.5
    val x2Min = 2.2
    val x2Max = 6.0

    var x1: Double
    var x2: Double

    do {
        x1 = x1Min + Random.nextDouble() * (x1Max - x1Min)
        x2 = x2Min + Random.nextDouble() * (x2Max - x2Min)
    } while (!isWithinODR(x1, x2))

    return Alternative(x1, x2)
}

fun main() {
    val weightF = 0.5
    val weightG = 0.3
    val weightH = 0.2

    var currentBest = generateRandomPointInODR() // Начальная точка
    var step = 0.1 // Начальный шаг
    val minStep = 0.001 // Минимальный шаг

    while (step > minStep) {
        // Создание новых альтернатив
        val newAlternatives = mutableListOf(
            Alternative(currentBest.x1 + step, currentBest.x2 + step),
            Alternative(currentBest.x1 - step, currentBest.x2 - step),
            Alternative(currentBest.x1 + step, currentBest.x2 - step),
            Alternative(currentBest.x1 - step, currentBest.x2 + step)
        ).filter { isWithinODR(it.x1, it.x2) }.toMutableList() //  Создаем MutableList

        // Добавляем текущую лучшую точку
        newAlternatives.add(currentBest)

        // Выбор лучшей альтернативы с использованием обобщенного критерия
        val bestAlternative = weightedSumModel(newAlternatives, weightF, weightG, weightH)

        // Если новая лучшая альтернатива лучше, чем текущая, обновляем текущую лучшую точку
        if (bestAlternative != currentBest) {
            currentBest = bestAlternative
            println("Улучшение! Текущая лучшая альтернатива: ${currentBest}, шаг: ${step}")
        } else {
            // Если нет улучшения, уменьшаем шаг
            step *= 0.5
            println("Нет улучшения, уменьшаем шаг до: ${step}")
        }
    }

    println("Финальная лучшая альтернатива (локальный поиск): ${currentBest}")
}