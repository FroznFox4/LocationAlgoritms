package utils

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ReaderAndWriterTest {

    private val exportFile = "tempData/geoJson.json"
    private val importFile = ""
    private val readerAndWriter = ReaderAndWriter(exportFile, importFile)

    @Test
    fun readFromFile() {
        val result = readerAndWriter.readFromFileAndReturnDots()
        result
            .map { el -> assertTrue(el.isNotEmpty()) }
    }

    @Test
    fun writeToConsole() {
        val result = readerAndWriter.readFromFileAndReturnDots()
        result.flatten().forEach {
            println(it.toCords())
        }
    }
}