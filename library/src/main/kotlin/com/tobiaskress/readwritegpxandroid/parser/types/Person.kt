package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.ReadWriteGpx
import com.tobiaskress.readwritegpxandroid.parser.readText
import org.xmlpull.v1.XmlPullParser

/**
 * A person or organization.
 */
data class Person(
    /**
     * Name of person or organization.
     */
    val name: String? = null,

    /**
     * Email address.
     */
    val email: Email? = null,

    /**
     * Link to Web site or other external information about person.
     */
    val link: Link? = null
) {
    companion object {

        private const val ELEMENT_NAME = "name"
        private const val ELEMENT_EMAIL = "email"
        private const val ELEMENT_LINK = "link"

        internal fun read(
            parser: XmlPullParser,
            elementName: String,
            namespace: String?,
            skip: (parser: XmlPullParser) -> Unit,
            loopMustContinue: (next: Int) -> Boolean
        ): Person {
            parser.require(XmlPullParser.START_TAG, namespace, elementName)

            var name: String? = null
            var email: Email? = null
            var link: Link? = null

            while (loopMustContinue(parser.next())) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                when (parser.name) {
                    ELEMENT_NAME -> {
                        name = ReadWriteGpx.readText(parser, ELEMENT_NAME, namespace)
                    }
                    ELEMENT_EMAIL -> {
                        email = Email.read(parser, ELEMENT_EMAIL, namespace, skip, loopMustContinue)
                    }
                    ELEMENT_LINK -> {
                        link = Link.read(parser, ELEMENT_LINK, namespace, skip, loopMustContinue)
                    }
                    else -> skip(parser)
                }
            }

            parser.require(XmlPullParser.END_TAG, namespace, elementName)

            return Person(
                name = name,
                email = email,
                link = link
            )
        }
    }
}
