package com.tobiaskress.readwritegpxandroid.parser

import org.junit.Test
import java.io.ByteArrayInputStream

class GpxSerializerTest {

    @Test
    fun testWriteShoresOfDerwentwater() {
        val input = ReadWriteGpxTest.getAssets()!!.open("shores-of-derwentwater.xml")
        val gpx = GpxParser().parse(input)
        val serializedGpx = GpxSerializer().serialize(gpx)
        val serializedInput = ByteArrayInputStream(serializedGpx.toByteArray(Charsets.UTF_8))
        ReadWriteGpxTest.testShoresOfDerwentwater(serializedInput)
    }

    @Test
    fun testWriteWadlbeisserExport() {
        val input = ReadWriteGpxTest.getAssets()!!.open("wadlbeisserExport.gpx")
        val gpx = GpxParser().parse(input)
        val serializedGpx = GpxSerializer().serialize(gpx)
        val serializedInput = ByteArrayInputStream(serializedGpx.toByteArray(Charsets.UTF_8))
        ReadWriteGpxTest.testWadlbeisserExport(serializedInput)
    }

    @Test
    fun testWriteGarminBaseCampExport() {
        val input = ReadWriteGpxTest.getAssets()!!.open("garminBaseCampExport.gpx")
        val gpx = GpxParser().parse(input)
        val serializedGpx = GpxSerializer().serialize(gpx)
        val serializedInput = ByteArrayInputStream(serializedGpx.toByteArray(Charsets.UTF_8))
        ReadWriteGpxTest.testGarminBaseCampExport(serializedInput)
    }

    @Test
    fun testWriteFullMetadataParsing() {
        val input = ReadWriteGpxTest.getAssets()!!.open("metadata-full.gpx")
        val gpx = GpxParser().parse(input)
        val serializedGpx = GpxSerializer().serialize(gpx)
        val serializedInput = ByteArrayInputStream(serializedGpx.toByteArray(Charsets.UTF_8))
        ReadWriteGpxTest.testFullMetadataParsing(serializedInput)
    }

    @Test
    fun testWriteMinimalMetadataParsing() {
        val input = ReadWriteGpxTest.getAssets()!!.open("metadata-minimal.gpx")
        val gpx = GpxParser().parse(input)
        val serializedGpx = GpxSerializer().serialize(gpx)
        val serializedInput = ByteArrayInputStream(serializedGpx.toByteArray(Charsets.UTF_8))
        ReadWriteGpxTest.testMinimalMetadataParsing(serializedInput)
    }

    @Test
    fun testWriteMissingMagvar() {
        val input = ReadWriteGpxTest.getAssets()!!.open("missing-magvar.gpx")
        val gpx = GpxParser().parse(input)
        val serializedGpx = GpxSerializer().serialize(gpx)
        val serializedInput = ByteArrayInputStream(serializedGpx.toByteArray(Charsets.UTF_8))
        ReadWriteGpxTest.testMissingMagvar(serializedInput)
    }
}
