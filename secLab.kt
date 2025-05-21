package sec

import kotlin.math.*

// Обновленная функция для вычисления целевой функции и ее производной
fun calculateFunction(x: Double, flag: Int): Double {
    return when (flag) {
        1 -> 2.0 + x.pow(2) + x.pow(2.0 / 3.0) - ln(1 + x.pow(2.0 / 3.0)) - 2 * x * atan(x.pow(1.0 / 3.0))  // Целевая функция
        2 -> 2 * x - 2 * atan(x.pow(1.0 / 3.0)) // Первая производная
        3 -> 2.0 - 2.0 / (3.0 * x.pow(2.0 / 3.0) * (1.0 + x.pow(2.0 / 3.0))) // Вторая производная
        else -> throw IllegalArgumentException("Недопустимый флаг для function: $flag. Допустимые значения: 1, 2, 3.")
    }
}

fun methodOfTangentsCustom(
    startPoint: Double, // Начальная точка
    epsilon: Double, // Точность
    maxIterations: Int, // Максимальное количество итераций
    alphaInitial: Double = 0.1, // Начальный коэффициент уменьшения шага (уменьшен)
): Pair<Double, Double> {
    var x_k = startPoint // Текущая точка
    var f_x_k: Double = calculateFunction(x_k, 1)  // Значение функции в текущей точке (ИНИЦИАЛИЗИРУЕМ!)
    var derivative_x_k: Double // Производная в текущей точке
    var x_next: Double // Следующая точка
    var iterations = 0
    var alpha = alphaInitial // Текущий коэффициент уменьшения шага

    while (iterations < maxIterations) {
        iterations++

        // 1. Вычисляем значение функции и производной в текущей точке
        derivative_x_k = calculateFunction(x_k, 2)

        println("Итерация $iterations: x_k = %.6f, f(x_k) = %.6f, f'(x_k) = %.6f".format(x_k, f_x_k, derivative_x_k))

        // 3. Вычисляем следующую точку с уменьшением шага
        x_next = x_k - alpha * f_x_k / derivative_x_k

        // 4. Вычисляем значение функции в новой точке
        val f_x_next = calculateFunction(x_next, 1)

        if (f_x_next > f_x_k) {
            // Шаг слишком большой, уменьшаем alpha
            alpha /= 2.0
            println("Шаг слишком большой, уменьшаем alpha до $alpha")
            if (alpha < epsilon) {
                println("Alpha слишком мала. Алгоритм не сходится.")
                break
            }
            continue
        }

        // 5. Проверяем критерий остановки
        if (f_x_next - f_x_k < epsilon && x_next - x_k < epsilon) {
            println("Метод касательных сошелся за $iterations итераций.")
            return Pair(x_next, f_x_next) // Возвращаем результат
        }

        // 6. Обновляем текущую точку
        x_k = x_next
        f_x_k = f_x_next
        alpha = alphaInitial
    }

    println("Метод касательных не сошелся за $maxIterations итераций")
    return Pair(x_k, f_x_k)
}

fun main() {
    val startPoint = 0.5 // Начальная точка
    val epsilon = 1e-10 // Точность
    val maxIterations = 100 // Максимальное количество итераций
    val alphaInitial = 0.005 // Начальный коэффициент уменьшения шага

    val (bestSolution, bestValue) = methodOfTangentsCustom(
        startPoint,
        epsilon,
        maxIterations,
        alphaInitial,
    )

    println("Лучшее решение (Метод касательных): x = $bestSolution")
    println("Значение целевой функции: $bestValue")
}