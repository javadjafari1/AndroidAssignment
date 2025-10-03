package ir.miare.androidcodechallenge.ui.screens.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed

@Composable
internal fun DetailBottomSheet(
    sheetState: SheetState,
    player: PlayerWithTeamAndFollowed,
    modifier: Modifier = Modifier,
    onFollowClicked: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Text(
                text = player.playerEntity.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.size(8.dp))

            Text(
                text = stringResource(R.string.goals, player.playerEntity.totalGoal),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.size(8.dp))

            Text(
                text = stringResource(R.string.team_name, player.teamEntity.name),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.size(8.dp))

            Text(
                text = stringResource(R.string.team_rank, player.teamEntity.rank),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.size(8.dp))

            AnimatedContent(player.followedEntity != null) { isFollowed ->
                if (isFollowed) {
                    OutlinedButton(
                        onClick = { onFollowClicked(false) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                    ) {
                        Text(stringResource(R.string.unfollow))
                    }
                } else {
                    Button(
                        onClick = { onFollowClicked(true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                    ) {
                        Text(stringResource(R.string.follow))
                    }
                }
            }
        }
    }
}
