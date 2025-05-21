import kotlin.math.*

//Для 1 лабы (c 1 переменной)
fun calculateFunction1(x: Double, flag: Int): Double {
    return when (flag) {
        1 -> 2.0 + x.pow(2) + x.pow(2.0 / 3.0) - ln(1 + x.pow(2.0 / 3.0)) - 2 * x * atan(x.pow(1.0 / 3.0))
        2 -> 2 * x - 2 * atan(x.pow(1.0 / 3.0)) // Первая производная
        3 -> 2.0 - 2.0 / (3.0 * x.pow(2.0 / 3.0) * (1.0 + x.pow(2.0 / 3.0))) // Вторая производная
        else -> throw IllegalArgumentException("Недопустимый флаг для function1: $flag. Допустимые значения: 1, 2, 3.")
    }
}

//Для 2 - 4 лабы (с 2 перменными)
fun calculateFunction2(x1: Double, x2: Double, flag: Int): Double {
    val sqrtTerm = sqrt(2 + x1.pow(2) + x2.pow(2)) // Выносим общий член

    return when(flag) {
        1 -> x1.pow(4) + x2.pow(4) + sqrtTerm - 2 * x1 + 3 * x2
        2 -> 4 * x1.pow(3) + x1 / sqrtTerm - 2 // Первая производная по x1 (∂f/∂x)
        3 -> 4 * x2.pow(3) + x2 / sqrtTerm + 3 // Первая производная по х2 (∂f/∂y)
        else -> throw IllegalArgumentException("Недопустимый флаг для function2: $flag. Допустимые значения: 1, 2, 3.")
    }
}


fun calculateFunction3(x1: Double, x2: Double, flag: Int): Double {
    return when (flag) {
        1 ->  x1.pow(2) - 3 * x1 * x2 + 10 * x2.pow(2) + 5 * x1 - 3 * x2;
        2 -> 2 * x1 - 3 * x2 + 5 // Первая производная по x1 (∂f/∂x)
        3 -> -3 * x1 + 20 * x2 - 3 // Первая производная по х2 (∂f/∂y)
        else -> throw IllegalArgumentException("Недопустимый флаг для function1: $flag. Допустимые значения: 1, 2, 3.")
    }
}

