@file:Suppress("UnusedPrivateMember", "Unused")

package com.tobiaskress.readwritegpxandroid.parser

import android.util.Xml
import com.tobiaskress.readwritegpxandroid.parser.types.Gpx
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private const val ELEMENT_GPX = "gpx"
// private const val ELEMENT_TRACK = "trk"
// private const val ELEMENT_SEGMENT = "trkseg"
// private const val ELEMENT_TRACK_POINT = "trkpt"
// private const val ELEMENT_LAT = "lat"
// private const val ELEMENT_LON = "lon"
// private const val ELEMENT_ELEVATION = "ele"
// private const val ELEMENT_TIME = "time"
// private const val ELEMENT_WAY_POINT = "wpt"
// private const val ELEMENT_ROUTE = "rte"
// private const val ELEMENT_ROUTE_POINT = "rtept"
// private const val ELEMENT_NAME = "name"
// private const val ELEMENT_DESC = "desc"
// private const val ELEMENT_CMT = "cmt"
// private const val ELEMENT_SRC = "src"
// private const val ELEMENT_LINK = "link"
// private const val ELEMENT_NUMBER: String = "number"
// private const val ELEMENT_TYPE: String = "type"
// private const val ELEMENT_TEXT: String = "text"
// private const val ELEMENT_AUTHOR: String = "author"
// private const val ELEMENT_COPYRIGHT: String = "copyright"
// private const val ELEMENT_KEYWORDS: String = "keywords"
// private const val ELEMENT_BOUNDS: String = "bounds"
// private const val ELEMENT_EXTENSIONS: String = "extensions"
// private const val ELEMENT_MIN_LAT: String = "minlat"
// private const val ELEMENT_MIN_LON: String = "minlon"
// private const val ELEMENT_MAX_LAT: String = "maxlat"
// private const val ELEMENT_MAX_LON: String = "maxlon"
// private const val ELEMENT_HREF: String = "href"
// private const val ELEMENT_YEAR: String = "year"
// private const val ELEMENT_LICENSE: String = "license"
// private const val ELEMENT_EMAIL: String = "email"
// private const val ELEMENT_ID: String = "id"
// private const val ELEMENT_DOMAIN: String = "domain"

private val namespace: String? = null

/**
 * [ReadWriteGpx] currently supports all GPX tags listed in GPX v1.1 schema.
 */
class ReadWriteGpx {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): Gpx {
        return read(inputStream)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun read(inputStream: InputStream): Gpx {
        inputStream.use { input ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
            parser.setInput(input, null)
            parser.nextTag()

            return Gpx.read(parser, ELEMENT_GPX, namespace, { skip(it) }, { loopMustContinue(it) })
        }
    }

    private fun loopMustContinue(next: Int): Boolean {
        return next != XmlPullParser.END_TAG && next != XmlPullParser.END_DOCUMENT
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException("Expected ${XmlPullParser.START_TAG} but got ${parser.eventType}.")
        }

        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    companion object
}
