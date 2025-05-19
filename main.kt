package mult

import kotlin.math.abs
import kotlin.math.sqrt

data class Alternative(
    val x1: Double,
    val x2: Double
) {
    // Расчет значений критериев
    val f1: Double = x1 + 2 * x2
    val f2: Double = 3 * x1 - 2 * x2
    val f3: Double = -abs(x1 - 4)
}

data class NormalizedCriteria(val fNorm: List<Double>, val gNorm: List<Double>, val hNorm: List<Double>)

fun collectAndNormalizeCriteria(alternatives: List<Alternative>): NormalizedCriteria {
    // 1. Сбор значений критериев
    val fValues = alternatives.map { it.f1 }
    val gValues = alternatives.map { it.f2 }
    val hValues = alternatives.map { it.f3 }

    // 2. Нормализация
    fun normalize(values: List<Double>): List<Double> {
        val minVal = values.minOrNull() ?: 0.0
        val maxVal = values.maxOrNull() ?: 1.0
        return values.map { (it - minVal) / (maxVal - minVal) }
    }

    val f1Norm = normalize(fValues)
    val f2Norm = normalize(gValues)
    val f3Norm = normalize(hValues)

    return NormalizedCriteria(f1Norm, f2Norm, f3Norm)
}


fun generalCrit(alternatives: List<Alternative>, wf1: Double, wf2: Double, wf3: Double): Alternative {
    // 1. Сбор и нормализация критериев
    val normalizedCriteria = collectAndNormalizeCriteria(alternatives)
    val f1Norm = normalizedCriteria.fNorm
    val f2Norm = normalizedCriteria.gNorm
    val f3Norm = normalizedCriteria.hNorm

    // 3. Расчет обобщенных критериев
    val generalcrieters = alternatives.indices.map { i -> wf1 * f1Norm[i] + wf2 * f2Norm[i] + wf3 * f3Norm[i] }

    // 4. Выбор лучшей альтернативы
    return alternatives[generalcrieters.indexOf(generalcrieters.maxOrNull())]
}


fun perfectPoint(alternatives: List<Alternative>): Alternative {
    // 1. Сбор и нормализация критериев
    val normalizedCriteria = collectAndNormalizeCriteria(alternatives)
    val f1Norm = normalizedCriteria.fNorm
    val f2Norm = normalizedCriteria.gNorm
    val f3Norm = normalizedCriteria.hNorm

    // 3. Расчет расстояний до идеальной точки
    val distance = alternatives.indices.map { i -> sqrt((f1Norm[i] - 1).pow(2) + (f2Norm[i] - 1).pow(2) + (f3Norm[i] - 1).pow(2)) }

    // 4. Выбор лучшей альтернативы (с минимальным расстоянием)
    return alternatives[distance.indexOf(distance.minOrNull())]
}


fun Double.pow(exponent: Int): Double = Math.pow(this, exponent.toDouble())


fun main() {
    val alternatives = listOf(
        Alternative(1.0, 6.0),   // A
        Alternative(8.5, 2.7), // B
        Alternative(5.9, 2.2), // C
        Alternative(5.13, 3.63) // D
    )

    // Веса для обобщенного критерия
    val wf1 = 0.5
    val wf2 = 0.3
    val wf3 = 0.2

    // Вывод результатов
    println("Лучшая альтернатива по методу обобщенного критерия: ${generalCrit(alternatives, wf1, wf2, wf3)}")
    println("Лучшая альтернатива по методу идеальной точки: ${perfectPoint(alternatives)}")
}