package ir.miare.androidcodechallenge.ui.screens.followed

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.ui.PlayerItemUiModel
import ir.miare.androidcodechallenge.ui.screens.home.components.DetailBottomSheet
import ir.miare.androidcodechallenge.ui.screens.home.components.PlayerItemContent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun FollowedListScreen(
    navController: NavController
) {
    FollowedListScreen(
        viewModel = koinViewModel(),
        onBackClicked = {
            navController.navigateUp()
        }
    )
}

@Composable
internal fun FollowedListScreen(
    viewModel: FollowedListViewModel,
    onBackClicked: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val players by viewModel.players.collectAsStateWithLifecycle()
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedPlayer by viewModel.selectedPlayerToShow.collectAsStateWithLifecycle()

    FollowedListScreen(
        players = players,
        onBackClicked = onBackClicked,
        selectedPlayer = selectedPlayer,
        detailSheetState = detailSheetState,
        onDetailBottomSheetDismissRequest = { viewModel.removeSelectedPlayer() },
        onPlayerClicked = { viewModel.getPlayerWithId(it) },
        onFollowClicked = { shouldFollow, id ->
            scope.launch { detailSheetState.hide() }
                .invokeOnCompletion {
                    viewModel.updateFollowStatus(shouldFollow, id)
                    viewModel.removeSelectedPlayer()
                }
        }
    )
}

@Composable
internal fun FollowedListScreen(
    players: List<PlayerItemUiModel>,
    selectedPlayer: PlayerWithTeamAndFollowed?,
    detailSheetState: SheetState,
    onDetailBottomSheetDismissRequest: () -> Unit,
    onFollowClicked: (Boolean, Int) -> Unit,
    onPlayerClicked: (Int) -> Unit,
    onBackClicked: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    if (selectedPlayer != null) {
        DetailBottomSheet(
            sheetState = detailSheetState,
            player = selectedPlayer,
            onFollowClicked = { shouldFollow ->
                onFollowClicked(
                    shouldFollow,
                    selectedPlayer.playerEntity.id
                )
            },
            onDismissRequest = onDetailBottomSheetDismissRequest
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.followed_list)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            contentPadding = PaddingValues(
                vertical = 8.dp,
            )
        ) {
            items(
                players,
                key = { it.id }
            ) { player ->
                PlayerItemContent(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioLowBouncy
                        )
                    ),
                    player = player,
                    onClick = { onPlayerClicked(player.id) }
                )
            }
        }
    }
}
