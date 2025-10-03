package ir.miare.androidcodechallenge.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.domain.models.ui.PlayerItemUiModel

@Composable
internal fun PlayerItemContent(
    player: PlayerItemUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .then(modifier)
    ) {
        Text(
            text = player.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.size(4.dp))

        Text(
            text = stringResource(R.string.team_rank, player.teamRank),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (player.goals != null) {
            Spacer(Modifier.size(4.dp))

            Text(
                text = stringResource(R.string.goals, player.goals),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.size(4.dp))

        Text(
            text = stringResource(R.string.team_name, player.teamName),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
