package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.ReadWriteGpx
import com.tobiaskress.readwritegpxandroid.parser.readText
import com.tobiaskress.readwritegpxandroid.parser.readTextAsOffsetTime
import org.xmlpull.v1.XmlPullParser
import java.time.OffsetDateTime

/**
 * Information about the GPX file, [author], and [copyright] restrictions goes in the metadata section. Providing
 * rich, meaningful information about your GPX files allows others to search for and use your GPS data.
 */
data class Metadata(
    /**
     * The name of the GPX file.
     */
    val name: String? = null,

    /**
     * A description of the contents of the GPX file.
     */
    val description: String? = null,

    /**
     * The person or organization who created the GPX file.
     */
    val author: Person? = null,

    /**
     * Copyright and license information governing use of the file.
     */
    val copyright: Copyright? = null,

    /**
     * URLs associated with the location described in the file.
     */
    val links: List<Link> = listOf(),

    /**
     * The creation date of the file.
     */
    val time: OffsetDateTime? = null,

    /**
     * Keywords associated with the file. Search engines or databases can use this information to classify the data.
     */
    val keywords: String? = null,

    /**
     * Minimum and maximum coordinates which describe the extent of the coordinates in the file.
     */
    val bounds: Bounds? = null,

    /**
     * Not yet implemented.
     *
     * You can add extend GPX by adding your own elements from another schema here.
     */
    val extensions: Extensions? = null,
) {

    companion object {

        private const val ELEMENT_NAME = "name"
        private const val ELEMENT_DESC = "desc"
        private const val ELEMENT_AUTHOR = "author"
        private const val ELEMENT_COPYRIGHT = "copyright"
        private const val ELEMENT_LINK = "link"
        private const val ELEMENT_TIME = "time"
        private const val ELEMENT_KEYWORDS = "keywords"
        private const val ELEMENT_BOUNDS = "bounds"
        private const val ELEMENT_EXTENSIONS = "extensions"

        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Metadata {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            var name: String? = null
            var desc: String? = null
            var author: Person? = null
            var copyright: Copyright? = null
            val links: MutableList<Link> = mutableListOf()
            var time: OffsetDateTime? = null
            var keywords: String? = null
            var bounds: Bounds? = null
            val extensions: Extensions? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_NAME -> {
                        name = ReadWriteGpx.readText(parser, ELEMENT_NAME, namespace)
                    }
                    ELEMENT_DESC -> {
                        desc = ReadWriteGpx.readText(parser, ELEMENT_DESC, namespace)
                    }
                    ELEMENT_AUTHOR -> {
                        author = Person.read(parser, ELEMENT_AUTHOR, namespace, skip, loopMustContinue)
                    }
                    ELEMENT_COPYRIGHT -> {
                        copyright = Copyright.read(parser, ELEMENT_COPYRIGHT, namespace, skip, loopMustContinue)
                    }
                    ELEMENT_LINK -> {
                        links.add(Link.read(parser, ELEMENT_LINK, namespace, skip, loopMustContinue))
                    }
                    ELEMENT_TIME -> {
                        time = ReadWriteGpx.readTextAsOffsetTime(parser, ELEMENT_TIME, namespace)
                    }
                    ELEMENT_KEYWORDS -> {
                        keywords = ReadWriteGpx.readText(parser, ELEMENT_KEYWORDS, namespace)
                    }
                    ELEMENT_BOUNDS -> {
                        bounds = Bounds.read(parser, ELEMENT_BOUNDS, namespace, skip, loopMustContinue)
                    }
                    ELEMENT_EXTENSIONS -> {
                        Extensions.read(parser, ELEMENT_EXTENSIONS, namespace, skip, loopMustContinue)
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Metadata(
                name = name,
                description = desc,
                author = author,
                copyright = copyright,
                links = links.toList(),
                time = time,
                keywords = keywords,
                bounds = bounds,
                extensions = extensions
            )
        }
    }
}