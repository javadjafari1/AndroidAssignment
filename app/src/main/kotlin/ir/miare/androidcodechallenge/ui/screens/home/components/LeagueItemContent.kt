package ir.miare.androidcodechallenge.ui.screens.home.components

import androidx.compose.foundation.background
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
import ir.miare.androidcodechallenge.domain.models.ui.LeagueItemUiModel

@Composable
internal fun LeagueItemContent(
    league: LeagueItemUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
            .then(modifier)
    ) {
        Text(
            text = league.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Spacer(Modifier.size(4.dp))

        Text(
            text = stringResource(R.string.league_rank, league.rank),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (league.averageGoal != null) {
            Spacer(Modifier.size(4.dp))

            Text(
                text = stringResource(R.string.average_goal_per_game, league.averageGoal),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
