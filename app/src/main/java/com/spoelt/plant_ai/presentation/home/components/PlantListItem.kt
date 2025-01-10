package com.spoelt.plant_ai.presentation.home.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.plant_ai.R
import com.spoelt.plant_ai.domain.model.Plant
import com.spoelt.plant_ai.presentation.theme.PlantAITheme

@Composable
fun PlantListItem(
    plant: Plant,
    onClick: (Int) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        onClick = { onClick(plant.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 18.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.plant_icon),
                contentDescription = stringResource(R.string.plant_icon_content_desc)
            )

            Text(
                modifier = Modifier.weight(1f),
                text = plant.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.show_plant_details_icon_content_desc)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlantListItemPreview() {
    PlantAITheme {
        PlantListItem(
            plant = Plant(
                id = 0,
                name = "Orchid",
                temperature = null,
                photo = null,
                humidity = null,
                light = null,
                fertilizer = null,
                additionalTips = null
            ),
            onClick = {}
        )
    }
}