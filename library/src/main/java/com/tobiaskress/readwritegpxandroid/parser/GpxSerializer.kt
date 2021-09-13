package com.tobiaskress.readwritegpxandroid.parser

import android.util.Xml
import com.tobiaskress.readwritegpxandroid.parser.types.Gpx
import java.io.StringWriter

/**
 * [GpxSerializer] currently supports all GPX tags listed in
 * [Topografix GPX 1.1 schema](http://www.topografix.com/GPX/1/1/), except for the extensions element.
 */
class GpxSerializer {

    private val namespace: String? = null

    fun serialize(gpx: Gpx): String {
        val xmlSerializer = Xml.newSerializer()
        val stringWriter = StringWriter()

        xmlSerializer.setOutput(stringWriter)

        xmlSerializer.startDocument("UTF-8", null)
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true)

        gpx.serialize(xmlSerializer, ROOT_ELEMENT, namespace)

        xmlSerializer.endDocument()

        return stringWriter.toString()
    }

}
