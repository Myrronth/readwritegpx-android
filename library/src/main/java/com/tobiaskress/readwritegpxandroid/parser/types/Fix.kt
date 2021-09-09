package com.tobiaskress.readwritegpxandroid.parser.types

/**
 * Type of GPS fix. none means GPS had no fix. To signify "the fix info is unknown", leave out fixType entirely.
 * pps = military signal used
 */
enum class Fix(val identifier: String) {
    NONE("none"),
    TWO_D("2d"),
    THREE_D("3d"),
    DGPS("dgps"),
    PPS("pps")
}
