package ir.miare.androidcodechallenge.ui.screens.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.ui.LeagueItemUiModel
import ir.miare.androidcodechallenge.domain.models.ui.ListItemUiModel
import ir.miare.androidcodechallenge.domain.models.ui.PlayerItemUiModel
import ir.miare.androidcodechallenge.ui.navigation.AppScreens
import ir.miare.androidcodechallenge.ui.screens.home.components.DetailBottomSheet
import ir.miare.androidcodechallenge.ui.screens.home.components.LeagueItemContent
import ir.miare.androidcodechallenge.ui.screens.home.components.PlayerItemContent
import ir.miare.androidcodechallenge.ui.screens.home.components.SortBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun HomeScreen(navController: NavController) {
    HomeScreen(
        viewModel = koinViewModel(),
        navigateToFollowedList = {
            navController.navigate(AppScreens.FollowedList) {
                launchSingleTop = true
            }
        }
    )
}

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToFollowedList: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagingItems = viewModel.items.collectAsLazyPagingItems()
    var isSortBottomSheetShowing by rememberSaveable { mutableStateOf(false) }
    val sortSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    HomeScreen(
        pagingItems = pagingItems,
        currentSortType = viewModel.currentSortType,
        navigateToFollowedList = navigateToFollowedList,
        isSortBottomSheetShowing = isSortBottomSheetShowing,
        sortSheetState = sortSheetState,
        onSortTypeChanged = { sortType ->
            scope.launch { sortSheetState.hide() }
                .invokeOnCompletion {
                    viewModel.updateSortType(sortType)
                    isSortBottomSheetShowing = false
                }
        },
        onSortBottomSheetDismissRequest = {
            scope.launch { sortSheetState.hide() }
                .invokeOnCompletion {
                    isSortBottomSheetShowing = false
                }
        },
        openSortBottomSheet = { isSortBottomSheetShowing = true },
        selectedPlayer = viewModel.selectedPlayerToShow,
        detailSheetState = detailSheetState,
        onDetailBottomSheetDismissRequest = { viewModel.removeSelectedPlayer() },
        onPlayerClicked = { viewModel.getPlayerWithId(it) },
        onFollowClicked = { shouldFollow, id ->
            viewModel.updateFollowStatus(shouldFollow, id)
        }
    )
}

@Composable
internal fun HomeScreen(
    pagingItems: LazyPagingItems<ListItemUiModel>,
    currentSortType: SortType,
    selectedPlayer: PlayerWithTeamAndFollowed?,
    sortSheetState: SheetState,
    isSortBottomSheetShowing: Boolean,
    detailSheetState: SheetState,
    onSortBottomSheetDismissRequest: () -> Unit,
    onDetailBottomSheetDismissRequest: () -> Unit,
    onSortTypeChanged: (SortType) -> Unit,
    openSortBottomSheet: () -> Unit,
    navigateToFollowedList: () -> Unit,
    onFollowClicked: (Boolean, Int) -> Unit,
    onPlayerClicked: (Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    if (isSortBottomSheetShowing) {
        SortBottomSheet(
            sheetState = sortSheetState,
            selectedSort = currentSortType,
            onChangedSort = onSortTypeChanged,
            onDismissRequest = onSortBottomSheetDismissRequest,
        )
    }

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
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = openSortBottomSheet) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sort),
                            contentDescription = stringResource(R.string.open_sorting_options),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = navigateToFollowedList) {
                        Icon(
                            painter = painterResource(R.drawable.ic_followed),
                            contentDescription = stringResource(R.string.open_followed_list_screen),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            pagingItems.itemSnapshotList.forEachIndexed { index, _ ->
                pagingItems[index]?.let { item ->
                    when (item) {
                        is LeagueItemUiModel -> {
                            stickyHeader(
                                key = item.id,
                                contentType = "leagueType"
                            ) {
                                LeagueItemContent(
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = tween(durationMillis = 250),
                                        fadeOutSpec = tween(durationMillis = 100),
                                        placementSpec = spring(
                                            stiffness = Spring.StiffnessLow,
                                            dampingRatio = Spring.DampingRatioLowBouncy
                                        )
                                    ),
                                    league = item
                                )
                            }
                        }

                        is PlayerItemUiModel -> {
                            item(
                                key = item.id,
                                contentType = "playerType"
                            ) {
                                PlayerItemContent(
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = tween(durationMillis = 250),
                                        fadeOutSpec = tween(durationMillis = 100),
                                        placementSpec = spring(
                                            stiffness = Spring.StiffnessLow,
                                            dampingRatio = Spring.DampingRatioLowBouncy
                                        )
                                    ),
                                    player = item,
                                    onClick = { onPlayerClicked(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
