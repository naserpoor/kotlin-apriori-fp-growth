
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


object Apriori {
    fun main(minSupportCount: Int, transactions: ArrayList<ArrayList<Int>>) : ArrayList<Pair<Int, ArrayList<Int>>>? {
        val _transactions = ArrayList<ArrayList<Int>>()
        var prevItemSetsWithMinSupportCount: ArrayList<Pair<Int, ArrayList<Int>>>? = ArrayList()
        _transactions.addAll(transactions)
        var items = getUniqueItems(transactions)
        var x = 0
        while (true) {
            x++
            val supportCountList = ArrayList<Int>()
            val itemSets = getItemSets(items, x)
            for (itemSet in itemSets) {
                var count = 0
                for (transaction in transactions) {
                    if (existsInTransaction(itemSet, transaction)) count++
                }
                supportCountList.add(count)
            }
            val itemSetsWithMinSupportCount =
                getItemSetsWithMinSupportCount(itemSets, supportCountList, minSupportCount)

            if (itemSetsWithMinSupportCount.size == 0) {
                return prevItemSetsWithMinSupportCount
            }
            items = getUniqueItems(ArrayList(itemSetsWithMinSupportCount.map {
                it.second
            }))
            prevItemSetsWithMinSupportCount = itemSetsWithMinSupportCount
        }
    }
    private fun getUniqueItems(data: ArrayList<ArrayList<Int>>): ArrayList<Int> {
        val toReturn = ArrayList<Int>()
        for (transaction in data) {
            for (item in transaction) {
                if (!toReturn.contains(item)) toReturn.add(item)
            }
        }
        toReturn.sort()
        return toReturn
    }
    private fun getItemSets(items: ArrayList<Int>, number: Int): ArrayList<ArrayList<Int>> {
        return if (number == 1) {
            val toReturn = ArrayList<ArrayList<Int>>()
            for (item in items) {
                val aList = ArrayList<Int>()
                aList.add(item)
                toReturn.add(aList)
            }
            toReturn
        } else {
            val size = items.size
            val toReturn = ArrayList<ArrayList<Int>>()
            for (i in 0 until size) {
                val _items = ArrayList<Int>()
                for (item in items) {
                    _items.add(item)
                }
                val thisItem = items[i]
                for (j in 0..i) {
                    _items.removeAt(0)
                }
                val permutationsBelow = getItemSets(_items, number - 1)
                for (aList in permutationsBelow) {
                    aList.add(thisItem)
                    aList.sort()
                    toReturn.add(aList)
                }
            }
            toReturn
        }
    }
    private fun existsInTransaction(items: ArrayList<Int>, transaction: ArrayList<Int>): Boolean {
        for (item in items) {
            if (!transaction.contains(item)) return false
        }
        return true
    }
    private fun getItemSetsWithMinSupportCount(
        itemSets: ArrayList<ArrayList<Int>>, count: ArrayList<Int>, minSupportCount: Int
    ): ArrayList<Pair<Int, ArrayList<Int>>> {
        val toReturn = ArrayList<Pair<Int, ArrayList<Int>>>()
        for (i in count.indices) {
            val c = count[i]
            if (c >= minSupportCount) {
                toReturn.add(Pair(c, itemSets[i]))
            }
        }
        return toReturn
    }
}