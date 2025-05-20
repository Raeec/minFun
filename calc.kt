package huc
import kotlin.math.*

fun calculateFunction2(x1: Double, x2: Double, flag: Int): Double {
    val sqrtTerm = sqrt(2 + x1.pow(2) + x2.pow(2)) // Выносим общий член

    return when(flag) {
        1 -> x1.pow(4) + x2.pow(4) + sqrtTerm - 2 * x1 + 3 * x2
        2 -> 4 * x1.pow(3) + x1 / sqrtTerm - 2 // Первая производная по x1 (∂f/∂x)
        3 -> 4 * x2.pow(3) + x2 / sqrtTerm + 3 // Первая производная по х2 (∂f/∂y)
        else -> throw IllegalArgumentException("Недопустимый флаг для function2: $flag. Допустимые значения: 1, 2, 3.")
    }
}