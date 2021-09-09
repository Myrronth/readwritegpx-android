package com.tobiaskress.readwritegpxandroid.parser

import androidx.test.filters.LargeTest
import org.junit.Test
import org.xmlpull.v1.XmlPullParserException

@LargeTest
internal class GpxParserTest {

    @Test
    fun testReadShoresOfDerwentwater() {
        val input = ReadWriteGpxTest.getAssets()!!.open("shores-of-derwentwater.xml")
        ReadWriteGpxTest.testShoresOfDerwentwater(input)
    }

    @Test
    fun testReadWadlbeisserExport() {
        val input = ReadWriteGpxTest.getAssets()!!.open("wadlbeisserExport.gpx")
        ReadWriteGpxTest.testWadlbeisserExport(input)
    }

    @Test
    fun testReadGarminBaseCampExport() {
        val input = ReadWriteGpxTest.getAssets()!!.open("garminBaseCampExport.gpx")
        ReadWriteGpxTest.testGarminBaseCampExport(input)
    }

    @Test(expected = XmlPullParserException::class)
    fun testReadGarminBaseCampExportTruncated() {
        val input = ReadWriteGpxTest.getAssets()!!.open("garminBaseCampExport-truncated.gpx")
        ReadWriteGpxTest.testGarminBaseCampExportTruncated(input)
    }

    @Test(expected = XmlPullParserException::class)
    fun testReadGarminBaseCampExportNoClosingTag() {
        val input = ReadWriteGpxTest.getAssets()!!.open("garminBaseCampExport-noclosingtag.gpx")
        ReadWriteGpxTest.testGarminBaseCampExportNoClosingTag(input)
    }

    @Test
    fun testReadFullMetadataParsing() {
        val input = ReadWriteGpxTest.getAssets()!!.open("metadata-full.gpx")
        ReadWriteGpxTest.testFullMetadataParsing(input)
    }

    @Test
    fun testReadMinimalMetadataParsing() {
        val input = ReadWriteGpxTest.getAssets()!!.open("metadata-minimal.gpx")
        ReadWriteGpxTest.testMinimalMetadataParsing(input)
    }

    @Test
    fun testReadMissingMagvar() {
        val input = ReadWriteGpxTest.getAssets()!!.open("missing-magvar.gpx")
        ReadWriteGpxTest.testMissingMagvar(input)
    }
}
