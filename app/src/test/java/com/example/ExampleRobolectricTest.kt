package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.PlantSample
import com.example.network.PlantAnalyzerService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PlantCareAppTest {

    @Test
    fun testAppNameString() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("PlantCare AI", appName)
    }

    @Test
    fun testPlantSampleIdentification() {
        val agave = PlantSample(
            id = "sample_agave",
            commonName = "Agave Azul",
            scientificName = "Agave tequilana",
            category = "AGAVE",
            description = "Xerófita",
            iconName = "agave",
            typicalIssue = "Estrés solar"
        )
        assertEquals("AGAVE", agave.category)
        assertNotNull(agave.scientificName)
    }
}
