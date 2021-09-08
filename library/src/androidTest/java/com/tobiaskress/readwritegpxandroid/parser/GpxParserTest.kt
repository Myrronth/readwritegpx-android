package com.tobiaskress.readwritegpxandroid.parser

import android.content.res.AssetManager
import android.net.Uri
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.tobiaskress.readwritegpxandroid.parser.types.Bounds
import com.tobiaskress.readwritegpxandroid.parser.types.Copyright
import com.tobiaskress.readwritegpxandroid.parser.types.Email
import com.tobiaskress.readwritegpxandroid.parser.types.Gpx
import com.tobiaskress.readwritegpxandroid.parser.types.Link
import com.tobiaskress.readwritegpxandroid.parser.types.Metadata
import com.tobiaskress.readwritegpxandroid.parser.types.Person
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.time.OffsetDateTime
import java.time.ZoneOffset

@LargeTest
internal class GpxParserTest {

    @Test
    @Throws(IOException::class, XmlPullParserException::class)
    fun testShoresOfDerwentwater() {
        val input = getAssets()!!.open("shores-of-derwentwater.xml")
        val gpx: Gpx = ReadWriteGpx().read(input)

        assertNotNull(gpx)
    }

    @Test
    @Throws(IOException::class, XmlPullParserException::class)
    fun testWadlbeisserExport() {
        val input = getAssets()!!.open("wadlbeisserExport.gpx")
        val gpx: Gpx = ReadWriteGpx().read(input)

        assertEquals(0, gpx.tracks.size)
        assertEquals(2, gpx.waypoints.size)
        assertEquals(1, gpx.routes.size)
        assertEquals(7847, gpx.routes.first().points.size)
    }

    @Test
    @Throws(IOException::class, XmlPullParserException::class)
    fun testGarminBaseCampExport() {
        val input = getAssets()!!.open("garminBaseCampExport.gpx")
        val gpx: Gpx = ReadWriteGpx().read(input)
        val metadata = gpx.metadata

        assertNotNull(metadata)
        metadata as Metadata

        assertEquals(Uri.parse("http://www.garmin.com"), metadata.links.first().href)
        assertEquals("Garmin International", metadata.links.first().text)
        assertNotNull(metadata.bounds)
        assertEquals(1, gpx.tracks.size)
        assertEquals(1, gpx.tracks.first().segments.size)
        assertEquals(10, gpx.tracks.first().segments.first().points.size)
        assertEquals(3, gpx.waypoints.size)
        assertEquals(1, gpx.routes.size)
        assertEquals(7, gpx.routes.first().points.size)
        assertEquals(" A92", gpx.waypoints.first().description)
        assertEquals("Erding Ab", gpx.waypoints[2].description)
        assertEquals("user", gpx.waypoints.first().type)
    }

    @Test(expected = XmlPullParserException::class)
    @Throws(IOException::class, XmlPullParserException::class)
    fun testGarminBaseCampExportTruncated() {
        val input = getAssets()!!.open("garminBaseCampExport-truncated.gpx")
        ReadWriteGpx().read(input)
    }

    @Test(expected = XmlPullParserException::class)
    @Throws(IOException::class, XmlPullParserException::class)
    fun testGarminBaseCampExportNoClosingTag() {
        val input = getAssets()!!.open("garminBaseCampExport-noclosingtag.gpx")
        val gpx: Gpx = ReadWriteGpx().read(input)

        assertEquals(1, gpx.tracks.size)
    }

    @Test
    @Throws(IOException::class, XmlPullParserException::class)
    fun testFullMetadataParsing() {
        val input = getAssets()!!.open("metadata-full.gpx")
        val gpx = ReadWriteGpx().read(input)
        val metadata = gpx.metadata

        assertNotNull(metadata)
        metadata as Metadata

        // Name
        assertEquals("metadata-full", metadata.name)
        assertEquals("Full metadata test", metadata.description)

        // Author
        val author = metadata.author

        assertNotNull(author)
        author as Person

        assertEquals("John Doe", author.name)

        // Author email
        val authorEmail = author.email

        assertNotNull(authorEmail)
        authorEmail as Email

        assertEquals("john.doe", authorEmail.id)
        assertEquals("example.org", authorEmail.domain)

        // Author link
        val authorLink = author.link

        assertNotNull(authorLink)
        authorLink as Link

        assertEquals(Uri.parse("www.example.org"), authorLink.href)
        assertEquals("Example Org.", authorLink.text)
        assertEquals("text/html", authorLink.type)

        // Copyright
        val copyright = metadata.copyright

        assertNotNull(copyright)
        copyright as Copyright

        assertEquals("Jane Doe", copyright.author)
        assertEquals(2019, copyright.year)
        assertEquals(Uri.parse("https://www.apache.org/licenses/LICENSE-2.0.txt"), copyright.license)

        // Link
        val links = metadata.links

        assertEquals(2, links.size)

        val firstLink = links[0]
        assertEquals(Uri.parse("www.example.org"), firstLink.href)
        assertNull(firstLink.text)
        assertNull(firstLink.type)

        val secondLink = links[1]
        assertEquals(Uri.parse("www.example.com"), secondLink.href)
        assertEquals("Example Com.", secondLink.text)
        assertNull(secondLink.type)

        // Time
        val expectedTime = OffsetDateTime.of(2019, 4, 4, 7, 0, 0, 0, ZoneOffset.ofHours(3))
        assertTrue(expectedTime.isEqual(metadata.time))

        // Keywords
        assertEquals("metadata, test", metadata.keywords)

        // Bounds
        val bounds = metadata.bounds

        assertNotNull(bounds)
        bounds as Bounds

        assertEquals(59.93305556, bounds.minimumLatitude.decimalDegrees, 0.001)
        assertEquals(20.78944444, bounds.minimumLongitude.decimalDegrees, 0.001)
        assertEquals(70.23250000, bounds.maximumLatitude.decimalDegrees, 0.001)
        assertEquals(31.63944444, bounds.maximumLongitude.decimalDegrees, 0.001)
    }

    @Test
    @Throws(IOException::class, XmlPullParserException::class)
    fun testMinimalMetadataParsing() {
        val input = getAssets()!!.open("metadata-minimal.gpx")
        val gpx = ReadWriteGpx().read(input)
        val metadata = gpx.metadata

        assertNotNull(metadata)
        metadata as Metadata

        // Author
        val author = metadata.author

        assertNotNull(author)
        author as Person

        assertNull(author.name)
        assertNull(author.email)
        assertNull(author.link)

        // Copyright
        val copyright = metadata.copyright

        assertNotNull(copyright)
        copyright as Copyright

        assertEquals("Jane Doe", copyright.author)
        assertNull(copyright.year)
        assertNull(copyright.license)
    }

    @Test
    fun testMissingMagvar() {
        val input = getAssets()!!.open("missing-magvar.gpx")
        ReadWriteGpx().read(input)
    }

    private fun getAssets(): AssetManager? {
        return InstrumentationRegistry.getInstrumentation().context.assets
    }
}
