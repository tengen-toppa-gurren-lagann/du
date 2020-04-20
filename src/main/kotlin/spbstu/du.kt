package spbstu

import java.io.File

fun main(args: Array<String>) {
    var readable = false
    var si = false
    var summed = false

    val fileList = mutableListOf<String>()
    val sizes = mutableListOf<Long>()
    for (i in 0 until args.size) {
        when (args[i]) {
            "-h", "-H" -> readable = true
            "-c", "-C" -> summed = true
            "--si", "--sI", "--Si", "--SI" -> si = true
            else -> fileList.add(args[i])
        }
    }
    for (i in 0 until fileList.size) {
        val curFile = File(fileList[i])
        if (curFile.exists()) sizes.add(i, getFileOrFolderSize(curFile))
        else {
            print("Can't get file " + fileList[i])
            return
        }
    }

    val divisor = if (si) 1000 else 1024 // if option -si is on, then div by 1000, else div by 1024
    if (summed) {
        println("Files summary size = " + makeOutputString(summarise(sizes), readable, divisor))
    } else for (i in 0 until fileList.size) {
        println(fileList[i] + " " + makeOutputString(sizes[i], readable, divisor))
    }

}

fun getFileOrFolderSize(dir: File): Long {
    var size: Long = 0
    if (!dir.isFile) {
        for (file in dir.listFiles()!!) {
            size += if (file.isFile) {
                file.length()
            } else getFileOrFolderSize(file)
        }
    } else size += dir.length()
    return size
}


fun summarise(list: List<Long>): Long {
    var sum: Long = 0
    for (i in 0 until list.size)
        sum += list[i]
    return sum
}

fun makeOutputString(l: Long, readable: Boolean, divisor: Int): String {
    val suffix = listOf("B", "KB", "MB", "GB")
    val s: String
    var cur = l
    var i = 0
    if (readable) {
        while (cur >= divisor && i < 4) {
            cur /= divisor
            i++
        }
        s = cur.toString() + " " + suffix[if (i < 4) i else 3]
    } else {
        cur /= divisor
        if (cur < 1) cur = 1 // Если размер меньше килобайта, то округляем вверх (до 1 кб)
        s = cur.toString()
    }
    return s
}
