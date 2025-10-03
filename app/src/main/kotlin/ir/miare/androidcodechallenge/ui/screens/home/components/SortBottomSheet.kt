package ir.miare.androidcodechallenge.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.domain.models.SortType

@Composable
internal fun SortBottomSheet(
    sheetState: SheetState,
    selectedSort: SortType,
    modifier: Modifier = Modifier,
    onChangedSort: (SortType) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        LazyColumn(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            items(SortType.entries) { sortType ->
                SortTypeItem(
                    isSelected = selectedSort == sortType,
                    title = stringResource(
                        when (sortType) {
                            SortType.TeamAndLeagueRank -> R.string.sort_type_team_and_league_rank
                            SortType.MostGoals -> R.string.sort_type_most_goals_by_players
                            SortType.AverageGoalPerMatchOfLeague -> R.string.sort_type_average_goal_per_match_of_leagues
                            SortType.None -> R.string.sort_type_none
                        }
                    ),
                    onClick = { onChangedSort(sortType) },
                )
            }
        }
    }
}

@Composable
private fun SortTypeItem(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
        )

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
