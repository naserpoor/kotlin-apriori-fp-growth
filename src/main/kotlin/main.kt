import com.zakgof.velvetvideo.impl.JNRHelper
import com.zakgof.velvetvideo.impl.VelvetVideoLib
import java.lang.Exception
import org.dhatim.fastexcel.reader.ReadableWorkbook
import smile.association.*
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import java.util.*
import java.util.function.Supplier
import java.util.stream.Stream
import javax.imageio.ImageIO
import javax.sound.sampled.AudioFormat
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val map = HashMap<String,LinkedList<String>>()
    val hashSet = HashSet<String>()
    val dataSet = LinkedList<Set<Int>>()
    try {
        val wb = ReadableWorkbook(FileInputStream("C:\\Users\\ts\\Desktop\\Online Retail.xlsx"))
//        val wb = ReadableWorkbook(FileInputStream("C:\\Users\\ts\\Desktop\\online_retail.xlsx"))
        val rowsStream = wb.firstSheet.openStream().skip(1)
        rowsStream.forEach {
            val invoiceId = it.getCellText(0)
            val stockCode = it.getCellText(1)
            val description = it.getCellText(2)
            val quantity = it.getCellAsNumber(3).get()

            if (invoiceId.startsWith("C")
                || invoiceId.startsWith("c")
                || description.isBlank()
                || quantity <= BigDecimal.ZERO
                || stockCode.matches(Regex("[^0-9]+"))) {
                return@forEach
            }

            var list:LinkedList<String>? = map[invoiceId]
            if (list == null) {
                list = LinkedList()
                map[invoiceId] = list
            }
            list.add(description.trim())
        }
    } catch (e:Exception) {
        e.printStackTrace()
    }

    map.forEach {
        val stringList = it.value
        if (stringList.isNotEmpty()) {
            val intList = HashSet<Int>()
            stringList.forEach{
                hashSet.add(it)
                val hash = hashSet.indexOf(it)
                intList.add(hash)
            }
            dataSet.add(intList)
        }
    }

    val itemSet = dataSet.map {
        it.toIntArray()
    }.toTypedArray()
    val minSup = (0.02*itemSet.size).toInt()

//    println("Apriori")
//    val aprioriResult:ArrayList<Pair<Int, ArrayList<Int>>>?
//    println("TimeMillis:"+measureTimeMillis {
//         aprioriResult = Apriori.main(minSup, ArrayList(itemSet.map {
//            ArrayList(it.toList())
//        }))
//    })
//    aprioriResult?.sortBy {
//        it.first
//    }
//    aprioriResult?.forEach {
//        print(it.first)
//        print("  ")
//        print((it.second.map { hashSet.elementAt(it) }))
//        println()
//    }


    println("Fp-Growth")
    val fpGrowthResult: Stream<ItemSet>
    val fpTree:FPTree
    println("TimeMillis:"+measureTimeMillis {
        fpTree = fptree(minSup) {
            Stream.of(*itemSet)
        }
        fpGrowthResult = fpgrowth(fpTree)
    })
    var count = 0
    fpGrowthResult.sorted { o1, o2 ->
        o1.support - o2.support
    }.forEach {
        print(it.support)
        print("  ")
        print((it.items.map { hashSet.elementAt(it) }))
        println()
        count++
    }
    println("count:${count}")

    println("Association-Rule")
    arm(0.85,fpTree).forEach {
        println(it)
    }
}