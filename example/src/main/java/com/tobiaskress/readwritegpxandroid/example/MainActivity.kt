package com.tobiaskress.readwritegpxandroid.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.tobiaskress.readwritegpxandroid.example.ui.theme.ReadWriteGpxAndroidTheme
import com.tobiaskress.readwritegpxandroid.parser.GpxParser
import com.tobiaskress.readwritegpxandroid.parser.types.Gpx
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val LOG_TAG = "MainActivity"

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private val gpx: Gpx?
        get() {
            var gpx: Gpx? = null

            assets.open("test.gpx").use {
                try {
                    Log.e(LOG_TAG, "Start parsing")
                    gpx = GpxParser().parse(it)
                    Log.e(LOG_TAG, "Finished parsing")
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: XmlPullParserException) {
                    e.printStackTrace()
                }
            }

            return gpx
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ReadWriteGpxAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val gpx by remember { mutableStateOf(gpx) }

                    Column(modifier = Modifier.fillMaxSize()) {
                        if (gpx != null) {
                            val track = gpx!!.tracks.first()
                            val segment = track.segments.first()

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colors.background)
                            ) {
                                itemsIndexed(segment.points) { index, waypoint ->
                                    Surface(color = MaterialTheme.colors.surface) {
                                        ListItem(
                                            text = {
                                                Text("Waypoint $index at ${waypoint.time?.format(
                                                    DateTimeFormatter.ofLocalizedDateTime(
                                                        FormatStyle.MEDIUM,
                                                        FormatStyle.MEDIUM
                                                    )
                                                )}")
                                            },
                                            secondaryText = {
                                                Text("Latitude: ${waypoint.latitude.decimalDegrees}; " +
                                                        "Longitude: ${waypoint.longitude.decimalDegrees}")
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
