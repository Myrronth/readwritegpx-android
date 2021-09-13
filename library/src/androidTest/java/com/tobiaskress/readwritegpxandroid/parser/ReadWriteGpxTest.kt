package com.tobiaskress.readwritegpxandroid.parser

import android.content.res.AssetManager
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.tobiaskress.readwritegpxandroid.parser.types.Bounds
import com.tobiaskress.readwritegpxandroid.parser.types.Copyright
import com.tobiaskress.readwritegpxandroid.parser.types.Email
import com.tobiaskress.readwritegpxandroid.parser.types.Link
import com.tobiaskress.readwritegpxandroid.parser.types.Metadata
import com.tobiaskress.readwritegpxandroid.parser.types.Person
import org.junit.Assert
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.time.OffsetDateTime
import java.time.ZoneOffset

object ReadWriteGpxTest {

    internal fun getAssets(): AssetManager? {
        return InstrumentationRegistry.getInstrumentation().context.assets
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun testShoresOfDerwentwater(input: InputStream) {
        val gpx = GpxParser().parse(input)

        Assert.assertNotNull(gpx)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun testWadlbeisserExport(input: InputStream) {
        val gpx = GpxParser().parse(input)

        Assert.assertEquals(0, gpx.tracks.size)
        Assert.assertEquals(2, gpx.waypoints.size)
        Assert.assertEquals(1, gpx.routes.size)
        Assert.assertEquals(7847, gpx.routes.first().points.size)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun testGarminBaseCampExport(input: InputStream) {
        val gpx = GpxParser().parse(input)
        val metadata = gpx.metadata

        Assert.assertNotNull(metadata)
        metadata as Metadata

        Assert.assertEquals(Uri.parse("http://www.garmin.com"), metadata.links.first().href)
        Assert.assertEquals("Garmin International", metadata.links.first().text)
        Assert.assertNotNull(metadata.bounds)
        Assert.assertEquals(1, gpx.tracks.size)
        Assert.assertEquals(1, gpx.tracks.first().segments.size)
        Assert.assertEquals(10, gpx.tracks.first().segments.first().points.size)
        Assert.assertEquals(3, gpx.waypoints.size)
        Assert.assertEquals(1, gpx.routes.size)
        Assert.assertEquals(7, gpx.routes.first().points.size)
        Assert.assertEquals(" A92", gpx.waypoints.first().description)
        Assert.assertEquals("Erding Ab", gpx.waypoints[2].description)
        Assert.assertEquals("user", gpx.waypoints.first().type)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun testGarminBaseCampExportTruncated(input: InputStream) {
        GpxParser().parse(input)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun testGarminBaseCampExportNoClosingTag(input: InputStream) {
        val gpx = GpxParser().parse(input)

        Assert.assertEquals(1, gpx.tracks.size)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun testFullMetadataParsing(input: InputStream) {
        val gpx = GpxParser().parse(input)
        val metadata = gpx.metadata

        Assert.assertNotNull(metadata)
        metadata as Metadata

        // Name
        Assert.assertEquals("metadata-full", metadata.name)
        Assert.assertEquals("Full metadata test", metadata.description)

        // Author
        val author = metadata.author

        Assert.assertNotNull(author)
        author as Person

        Assert.assertEquals("John Doe", author.name)

        // Author email
        val authorEmail = author.email

        Assert.assertNotNull(authorEmail)
        authorEmail as Email

        Assert.assertEquals("john.doe", authorEmail.id)
        Assert.assertEquals("example.org", authorEmail.domain)

        // Author link
        val authorLink = author.link

        Assert.assertNotNull(authorLink)
        authorLink as Link

        Assert.assertEquals(Uri.parse("www.example.org"), authorLink.href)
        Assert.assertEquals("Example Org.", authorLink.text)
        Assert.assertEquals("text/html", authorLink.type)

        // Copyright
        val copyright = metadata.copyright

        Assert.assertNotNull(copyright)
        copyright as Copyright

        Assert.assertEquals("Jane Doe", copyright.author)
        Assert.assertEquals(2019, copyright.year)
        Assert.assertEquals(Uri.parse("https://www.apache.org/licenses/LICENSE-2.0.txt"), copyright.license)

        // Link
        val links = metadata.links

        Assert.assertEquals(2, links.size)

        val firstLink = links[0]
        Assert.assertEquals(Uri.parse("www.example.org"), firstLink.href)
        Assert.assertNull(firstLink.text)
        Assert.assertNull(firstLink.type)

        val secondLink = links[1]
        Assert.assertEquals(Uri.parse("www.example.com"), secondLink.href)
        Assert.assertEquals("Example Com.", secondLink.text)
        Assert.assertNull(secondLink.type)

        // Time
        val expectedTime = OffsetDateTime.of(2019, 4, 4, 7, 0, 0, 0, ZoneOffset.ofHours(3))
        Assert.assertTrue(expectedTime.isEqual(metadata.time))

        // Keywords
        Assert.assertEquals("metadata, test", metadata.keywords)

        // Bounds
        val bounds = metadata.bounds

        Assert.assertNotNull(bounds)
        bounds as Bounds

        Assert.assertEquals(59.93305556, bounds.minimumLatitude.decimalDegrees, 0.001)
        Assert.assertEquals(20.78944444, bounds.minimumLongitude.decimalDegrees, 0.001)
        Assert.assertEquals(70.23250000, bounds.maximumLatitude.decimalDegrees, 0.001)
        Assert.assertEquals(31.63944444, bounds.maximumLongitude.decimalDegrees, 0.001)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun testMinimalMetadataParsing(input: InputStream) {
        val gpx = GpxParser().parse(input)
        val metadata = gpx.metadata

        Assert.assertNotNull(metadata)
        metadata as Metadata

        // Author
        val author = metadata.author

        Assert.assertNotNull(author)
        author as Person

        Assert.assertNull(author.name)
        Assert.assertNull(author.email)
        Assert.assertNull(author.link)

        // Copyright
        val copyright = metadata.copyright

        Assert.assertNotNull(copyright)
        copyright as Copyright

        Assert.assertEquals("Jane Doe", copyright.author)
        Assert.assertNull(copyright.year)
        Assert.assertNull(copyright.license)
    }

    fun testMissingMagvar(input: InputStream) {
        GpxParser().parse(input)
    }
}
