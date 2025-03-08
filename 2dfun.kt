import org.knowm.xchart.*
import kotlin.math.*
import java.text.DecimalFormat

fun main() {
    draw2dgraph()
    splitingByHalf()
    golden()
}

fun draw2dgraph(){
    // 1. Параметры графика
    val xMin = 0.5
    val xMax = 1.0
    val xStep = 0.01

    // 2. Создание данных для графика
    val xData = mutableListOf<Double>()
    val yData = mutableListOf<Double>()

    var x = xMin
    while (x <= xMax) {
        xData.add(x)
        yData.add(calculateFunction(x))
        x += xStep
    }

    // Создание графика
    val chart = QuickChart.getChart(
        "График функции",
        "x",
        "y",
        "f(x)",
        xData.toDoubleArray(),
        yData.toDoubleArray()
    )

    // Установите диапазон осей
    chart.styler.xAxisMin = xMin
    chart.styler.xAxisMax = xMax


    val xTickPositions = mutableListOf<Double>()
    val xTickLabels = mutableListOf<String>()
    var currentTick = xMin
    while (currentTick <= xMax) {
        xTickPositions.add(currentTick)
        xTickLabels.add(DecimalFormat("#.##").format(currentTick))
        currentTick += xStep
    }

    // Отображение графика
    SwingWrapper(chart).displayChart()
}

fun calculateFunction(x: Double): Double {
    return 2.0 + x.pow(2) + x.pow(2.0 / 3.0) - ln(1 + x.pow(2.0 / 3.0)) - 2 * x * atan(x.pow(1.0 / 3.0)) }

fun splitingByHalf() {

    fun searchX1X2(a: Double, b: Double, eps: Double): Pair<Double, Double> {
        val delta = eps / 3
        val x1 = (a + b - delta) / 2
        val x2 = (a + b + delta) / 2
        return Pair(x1, x2)
    }

    fun splitingByHalf() {
        var a = 0.5
        var b = 1.0
        val eps = 1e-6
        var counter = 0

        while ((b - a) >= eps) {
            val (x1, x2) = searchX1X2(a, b, eps)
            if (calculateFunction(x1) >= calculateFunction(x2)) a = x2 else b = x1
            counter++
        }

        val x1x2 = searchX1X2(a, b, eps)
        val x1 = x1x2.first
        val x2 = x1x2.second

        println(
            "Итерация дихотомии $counter; a = ${"%.6g".format(a)}; b = ${"%.6g".format(b)}; x1 = ${"%.6g".format(x1)}; x2 = ${"%.6g".format(x2)}\n" +
                    "Средняя точка минимума f(x): ${"%.6g".format((a + b) / 2)}"
        )
    }
    splitingByHalf()
}

fun golden() {

    var a = 0.5
    var b = 1.0
    val eps = 1e-6
    val phi = (1 + sqrt(5.0)) / 2
    var counter = 0

    var x1 = b - (b - a) / phi
    var x2 = a + (b - a) / phi

    while ((b - a) > eps) {
        counter++

        if (calculateFunction(x1) < calculateFunction(x2)) {
            b = x2
            x2 = x1
            x1 = b - (b - a) / phi
        } else {
            a = x1
            x1 = x2
            x2 = a + (b - a) / phi
        }
    }

    println(
        "Итерация золотого сечения $counter; a = ${"%.6g".format(a)}; b = ${"%.6g".format(b)}; x1 = ${"%.6g".format(x1)}; x2 = ${"%.6g".format(x2)}\n" +
                "Средняя точка минимума f(x): ${"%.6g".format((a + b) / 2)}"
    )
}
