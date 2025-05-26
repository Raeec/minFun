package sec

import kotlin.math.*

fun calculateFunction(x: Double, flag: Int): Double {
    return when (flag) {
        1 -> 2.0 + x.pow(2) + x.pow(2.0 / 3.0) - ln(1 + x.pow(2.0 / 3.0)) - 2 * x * atan(x.pow(1.0 / 3.0))  // Целевая функция
        2 -> 2 * x - 2 * atan(x.pow(1.0 / 3.0)) // Первая производная
        else -> throw IllegalArgumentException("Недопустимый флаг для function: $flag. Допустимые значения: 1, 2.")
    }
}

fun cuttingPlaneMethod(
    a: Double, // Левая граница отрезка [a, b]
    b: Double, // Правая граница отрезка [a, b]
    startPoint: Double, // Начальная точка x_0
    epsilon: Double, // Точность
    maxIterations: Int // Максимальное количество итераций
): Pair<Double, Double> {

    var x_k = startPoint // Текущая точка
    val x_history = mutableListOf(x_k) // История точек x_i
    val derivative_history = mutableListOf<Double>() // История производных f'(x_i)

    // 2. Функция для вычисления касательной g(x, x_i) в точке x_i
    fun tangent(x: Double, x_i: Double, derivative_i: Double): Double {
        return derivative_i * (x - x_i) + calculateFunction(x_i, 1)
    }

    // 3. Функция для вычисления кусочно-линейной аппроксимации p_k(x)
    fun pk(x: Double): Double {
        var maxValue = Double.NEGATIVE_INFINITY
        for (i in x_history.indices) {
            maxValue = max(maxValue, tangent(x, x_history[i], derivative_history[i]))
        }
        return maxValue
    }

    for (iteration in 0 until maxIterations) {
        // 1. Вычисляем производную в текущей точке x_k
        val derivative_x_k = calculateFunction(x_k, 2)
        derivative_history.add(derivative_x_k)

        // 4. Поиск минимума p_k(x) на отрезке [a, b] (аналитически)
        val breakpoints = mutableSetOf<Double>() // Множество точек излома
        breakpoints.add(a) // Добавляем границы отрезка
        breakpoints.add(b)

        // Находим точки пересечения касательных
        for (i in 0 until x_history.size) {
            for (j in i + 1 until x_history.size) {
                val x_i = x_history[i]
                val x_j = x_history[j]
                val derivative_i = derivative_history[i]
                val derivative_j = derivative_history[j]

                // Если производные равны, касательные параллельны и не пересекаются
                if (derivative_i != derivative_j) {
                    // Находим точку пересечения двух касательных
                    val x_intersect = (calculateFunction(x_j, 1) - calculateFunction(x_i, 1) + derivative_i * x_i - derivative_j * x_j) / (derivative_i - derivative_j)
                    // Проверяем, что точка пересечения находится внутри отрезка [a, b]
                    if (x_intersect in a..b) {
                        breakpoints.add(x_intersect)
                    }
                }
            }
        }

        // Вычисляем p_k(x) во всех точках излома и на границах отрезка
        var p_k_min = Double.MAX_VALUE
        var x_min = a

        for (x in breakpoints) {
            val p_k_x = pk(x)
            if (p_k_x < p_k_min) {
                p_k_min = p_k_x
                x_min = x
            }
        }

        // 5. Обновляем текущую точку (с учетом ограничений)
        val x_next = x_min
        x_k = x_next.coerceIn(a, b) // Убеждаемся, что x_k находится в пределах [a, b]
        x_history.add(x_k)
        derivative_history.add(calculateFunction(x_k, 2)) // Add derivative of current x to history

        //println("Итерация $iteration: x_k = %.6f, p_k(x_k) = %.6f".format(x_k, pk(x_k)))

        // 6. Критерий остановки (простой: изменение x)
        if (abs(x_k - x_history[x_history.size - 2]) < epsilon) {
            println("Метод секущих плоскостей сошелся за $iteration итераций.")
            return Pair(x_k, calculateFunction(x_k, 1))
        }

    }

    println("Метод секущих плоскостей не сошелся за $maxIterations итераций.")
    return Pair(x_k, calculateFunction(x_k, 1))
}

fun main() {
    val a = 0.5 // Левая граница отрезка
    val b = 1.0 // Правая граница отрезка
    val startPoint = 0.5 // Начальная точка
    val epsilon = 1e-8 // Точность
    val maxIterations = 100 // Максимальное количество итераций

    val (bestSolution, bestValue) = cuttingPlaneMethod(a, b, startPoint, epsilon, maxIterations)

    println("Лучшее решение (Метод касательных): x = $bestValue")
    println("Значение целевой функции: f(x) = $bestSolution")
}