package huc

import kotlin.math.*

fun getDirectionName(direction: DoubleArray): String {
    return when {
        direction.contentEquals(directions[0]) -> "Вправо"
        direction.contentEquals(directions[1]) -> "Влево"
        direction.contentEquals(directions[2]) -> "Вверх"
        direction.contentEquals(directions[3]) -> "Вниз"
        direction.contentEquals(directions[4]) -> "Вправо-вверх"
        direction.contentEquals(directions[5]) -> "Вправо-вниз"
        direction.contentEquals(directions[6]) -> "Влево-вверх"
        direction.contentEquals(directions[7]) -> "Влево-вниз"
        else -> "Неизвестное направление"
    }
}
// Определяем 8 направлений (можно адаптировать под свои нужды)
val directions = arrayOf(
    doubleArrayOf(1.0, 0.0),   // Вправо
    doubleArrayOf(-1.0, 0.0),  // Влево
    doubleArrayOf(0.0, 1.0),   // Вверх
    doubleArrayOf(0.0, -1.0),  // Вниз
    doubleArrayOf(1.0, 1.0),   // Вправо-вверх
    doubleArrayOf(1.0, -1.0),  // Вправо-вниз
    doubleArrayOf(-1.0, 1.0),  // Влево-вверх
    doubleArrayOf(-1.0, -1.0)  // Влево-вниз
)

fun hookeJeeves8Directions(
    startPoint: DoubleArray,
    stepSize: Double,
    alpha: Double,
    epsilon: Double,
    maxIterations: Int,
    objectiveFunction: (DoubleArray) -> Double
): Pair<DoubleArray, Double> {

    var currentPoint = startPoint.copyOf()
    var bestPoint = startPoint.copyOf()
    var currentStepSize = stepSize

    var iterations = 0
    while (currentStepSize > epsilon && iterations < maxIterations) {
        iterations++

        // 1. Исследующий поиск в 8 направлениях
        val (exploratoryPoint, bestDirection) = explore8DirectionsWithLog(currentPoint, currentStepSize, objectiveFunction)


        // 2. Движение по образцу
        if (objectiveFunction(exploratoryPoint) < objectiveFunction(bestPoint)) {
            val newPoint = DoubleArray(startPoint.size) { i ->
                exploratoryPoint[i] + (exploratoryPoint[i] - bestPoint[i])
            }
            bestPoint = exploratoryPoint.copyOf()
            currentPoint = newPoint.copyOf()
            println("Итерация $iterations: Движение в направлении: ${getDirectionName(bestDirection!!)}")

        } else {
            // 3. Уменьшение шага
            currentStepSize *= alpha
            currentPoint = bestPoint.copyOf()
        }
    }

    println("Hooke-Jeeves (8 направлений) завершен за $iterations итераций.")
    return Pair(bestPoint, objectiveFunction(bestPoint))
}

// Исследующий поиск в 8 направлениях
fun explore8DirectionsWithLog(
    point: DoubleArray,
    stepSize: Double,
    objectiveFunction: (DoubleArray) -> Double
): Pair<DoubleArray, DoubleArray?> { // Возвращаем также направление

    var bestPoint = point.copyOf()
    var bestValue = objectiveFunction(point)
    var bestDirection: DoubleArray? = null // Добавляем переменную для хранения лучшего направления

    for (directionIndex in directions.indices) {
        val direction = directions[directionIndex]

        // Нормализуем направление (чтобы длина вектора была равна 1)
        val norm = sqrt(direction[0] * direction[0] + direction[1] * direction[1])
        val normalizedDirection = doubleArrayOf(direction[0] / norm, direction[1] / norm)

        // Создаем новую точку, сдвинутую в данном направлении
        val newPoint = DoubleArray(point.size) { i ->
            point[i] + stepSize * normalizedDirection[i]
        }

        val newValue = objectiveFunction(newPoint)
        if (newValue < bestValue) {
            bestValue = newValue
            bestPoint = newPoint.copyOf()
            bestDirection = direction.copyOf() // Запоминаем направление
        }
    }

    return Pair(bestPoint, bestDirection) // Возвращаем и точку, и направление
}

fun main() {
    // Целевая функция (ваша функция)
    val objectiveFunction: (DoubleArray) -> Double = { x ->
        val sqrtTerm = sqrt(x[0] * x[0] + x[1] * x[1])
        x[0].pow(4) + x[1].pow(4) + sqrtTerm - 2 * x[0] + 3 * x[1]
    }

    val startPoint = doubleArrayOf(0.5, -0.5) // Пример начальной точки
    val stepSize = 0.01
    val alpha = 0.1
    val epsilon = 1e-6
    val maxIterations = 10000

    val (bestSolution) = hookeJeeves8Directions(startPoint, stepSize, alpha, epsilon, maxIterations, objectiveFunction)

    println("Лучшее решение: x1 = ${bestSolution[0]}, x2 = ${bestSolution[1]}")
    println("Значение целевой функции: ${calculateFunction2(bestSolution[0],bestSolution[1],1)}")
}