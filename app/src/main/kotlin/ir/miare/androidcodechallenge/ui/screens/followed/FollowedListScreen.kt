package ir.miare.androidcodechallenge.ui.screens.followed

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
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
        val shouldShowEmptyState by remember(players) {
            derivedStateOf { players.isEmpty() }
        }
        AnimatedContent(shouldShowEmptyState) { isEmpty ->
            if (isEmpty) {
                Box(Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(46.dp),
                            painter = painterResource(R.drawable.ic_person),
                            contentDescription = null,
                        )

                        Text(
                            text = stringResource(R.string.your_followed_list_is_empty),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
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
    }
}
