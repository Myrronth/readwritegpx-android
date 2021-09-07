package com.tobiaskress.readwritegpxandroid.parser.types

import com.tobiaskress.readwritegpxandroid.parser.exceptions.NumberOutOfBoundsException

private const val MIN = 0u
private const val MAX = 1023u

/**
 * Represents a differential GPS station.
 *
 * @throws NumberOutOfBoundsException When the latitude is less than 0 or greater than 1023.
 */
class DgpsStation @Throws(NumberOutOfBoundsException::class) constructor(value: UInt) {
    var value: UInt = value
        private set(value) {
            if (value < MIN) {
                throw NumberOutOfBoundsException("DgpsStationType has to be at least $MIN.")
            }

            if (value > MAX) {
                throw NumberOutOfBoundsException("DgpsStationType has to be at most $MAX.")
            }

            field = value
        }

    //region Mimic data class

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DgpsStation) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String = """
    |DgpsStationType [
    |  value: $value
    |]
    """.trimMargin()

    //endregion
}
