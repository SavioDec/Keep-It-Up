package com.example.aula01

import android.os.Bundle
import android.provider.MediaStore
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.aula01.R
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aula01.ui.theme.Aula01Theme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.aula01.ui.theme.Composables.MediaTrackerCard
import com.example.aula01.ui.theme.Composables.TitleAndValueRow

class MovieDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aula01Theme {
                MovieDetailPage()
            }
        }
    }
}

@Preview
@Composable
fun MovieDetailPage() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = androidx.compose.ui.graphics.Color.Black,
        contentColor = androidx.compose.ui.graphics.Color.White
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            Box(contentAlignment = Alignment.BottomStart) {
                Image(
                    painter = painterResource(R.drawable.blade_runner),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Movier Poster"
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Text("Blade Runner 2049", style = MaterialTheme.typography.displayMedium)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        listOf(
                            "Movie",
                            "Sci-Fi",
                            "2017",
                            movieDuration.format()
                        ).forEach {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant

                            ) {
                                Text(it, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
                            }
                        }
                    }
                }
            }
            MediaTrackerCard("Synopsis") {
                Text(
                    text = "30 years after the events of Blade Runner, in 2049, replicates-bio engineered humans-remain employed as slave labor. The Los Angeles Police Department employs a Nexus-9 replicate named K (short for serial number, KD6-3.7) as a \"blade runner,\" an officer who hunts and \"retires\" (kills) rogue replicate models.\n" +
                            "\n" +
                            "After Sapper Morton retires, K discovers a box at Morton's farm, buried beneath a tree. A female replicate who passed away during a Cesarean section was buried there. This proves what was previously believed to be impossible-that replicates could reproduce biologically. Lt. Joshi, K's superior, orders K to retire the replicate child because she believes this information could spark a conflict between humans and replicates."
                )
            }

            MediaTrackerCard("Watch progress") {
                var sliderValue by remember { mutableStateOf(0.5f) }
                val watchProgress = movieDuration * sliderValue.toDouble()
                Text(watchProgress.format())
                Slider(
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = it
                    }
                )
            }
            MediaTrackerCard(title = "Details") {
                TitleAndValueRow("Director", "Denis Villeneuve")
            }
        }
    }
}


val movieDuration = 2.hours + 30.minutes



// Cria uma função de extensão de dentro da própria classe de Duration
private fun Duration.format(): String {
    return toComponents {  hours, minutes, _, _ ->
        "${hours}h${minutes}m"
    }
}




@Preview
@Composable
fun MovieDetailPagePreview(){
    Aula01Theme {
        MovieDetailPage()
    }
}
