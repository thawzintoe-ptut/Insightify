package com.ptut.insightify.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ptut.insightify.domain.survey.model.Survey
import com.ptut.insightify.home.HomeViewModel.UiEvent
import com.ptut.insightify.ui.Design
import com.ptut.insightify.ui.InsightifyIcons.KeyboardArrowRight
import com.ptut.insightify.ui.components.ShimmerLoadingIndicator
import com.ptut.insightify.ui.screens.ErrorScreen
import com.ptut.insightify.ui.theme.Black
import com.ptut.insightify.ui.theme.Black01
import com.ptut.insightify.ui.theme.neuzeitFontFamily
import com.ptut.insightify.ui.util.gradientBackground
import com.ptut.insightify.ui.R as uiR

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    innerPaddingValues: PaddingValues,
    onDetailContinueClicked: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val surveys = uiState.surveys.collectAsLazyPagingItems()

    Box(Modifier.fillMaxSize()) {
        Design.Components.ShimmerLoadingIndicator(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
        )

        if (
            surveys.itemCount != 0
            && surveys.loadState.refresh is LoadState.NotLoading
            && surveys.loadState.append is LoadState.NotLoading
        ) {
            HomeScreenPager(
                name = uiState.name,
                innerPaddingValues = innerPaddingValues,
                profileUrl = uiState.profileImageUrl,
                currentDate = uiState.currentDate,
                onDetailContinueClicked = onDetailContinueClicked,
                surveyItems = surveys,
                onScroll = {
                    viewModel.onUiEvent(
                        UiEvent.Scroll,
                    )
                },
            )
        }

        if (
            surveys.loadState.refresh is LoadState.Error
            || surveys.loadState.append is LoadState.Error
        ) {
            uiState.errorType?.let {
                Design.Components.ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    errorType = it,
                    onActionButtonClick = {
                        viewModel.onUiEvent(
                            UiEvent.OnErrorRetryClicked,
                        )
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenPager(
    name: String,
    profileUrl: String,
    currentDate: String,
    surveyItems: LazyPagingItems<Survey>,
    innerPaddingValues: PaddingValues = PaddingValues(20.dp),
    onDetailContinueClicked: (String) -> Unit = {},
    onScroll: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { surveyItems.itemCount })
    val indicatorScrollState = rememberLazyListState()

    LaunchedEffect(pagerState.currentPage) {
        val currentPage = pagerState.currentPage
        val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
        if (size > 0) {
            val lastVisibleIndex = indicatorScrollState.layoutInfo.visibleItemsInfo.last().index
            val firstVisibleItemIndex = indicatorScrollState.firstVisibleItemIndex

            if (currentPage > lastVisibleIndex - 1) {
                indicatorScrollState.animateScrollToItem(currentPage - size + 2)
            } else if (currentPage <= firstVisibleItemIndex + 1) {
                indicatorScrollState.animateScrollToItem(currentPage - 1)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            surveyItems[page]?.let { survey ->
                HomeScreenBackground(
                    survey.coverImageUrl,
                )
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .gradientBackground(
                    topColor = Black01,
                    bottomColor = Black,
                ),
        )

        TopProfileContent(
            modifier = Modifier
                .padding(innerPaddingValues)
                .padding(20.dp)
                .align(Alignment.TopStart),
            name = name,
            profileUrl = profileUrl,
            currentDate = currentDate,
        )

        Column(
            Modifier
                .padding(innerPaddingValues)
                .align(Alignment.BottomCenter),
        ) {
            IndicatorRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 60.dp, bottom = 16.dp),
                currentPage = pagerState.currentPage,
                indicatorCount = surveyItems.itemCount,
            )
            BottomDescriptionContent(
                descTitle = surveyItems[pagerState.currentPage]?.title ?: "",
                description = surveyItems[pagerState.currentPage]?.description ?: "",
                onDetailContinueClicked = onDetailContinueClicked,
            )
        }
    }
}

@Composable
fun TopProfileContent(
    modifier: Modifier,
    name: String,
    profileUrl: String,
    currentDate: String,
) {
    Column(modifier = modifier) {
        Text(
            text = currentDate,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontFamily = neuzeitFontFamily,
            ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name,
                style =
                MaterialTheme.typography.titleLarge.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontFamily = neuzeitFontFamily,
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = profileUrl,
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
            )
        }
    }
}

@Composable
fun BottomDescriptionContent(
    descTitle: String,
    description: String,
    onDetailContinueClicked: (String) -> Unit,
) {
    Text(
        text = descTitle,
        style =
        MaterialTheme.typography.titleLarge.copy(
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
        ),
        lineHeight = 40.sp,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
    Spacer(modifier = Modifier.height(16.dp))
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (title, icon) = createRefs()
        Text(
            text = description,
            style =
            MaterialTheme.typography.titleMedium.copy(
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
            ),
            modifier =
            Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 20.dp)
                end.linkTo(icon.start, margin = 20.dp)
                bottom.linkTo(parent.bottom, margin = 20.dp)
                width = Dimension.fillToConstraints
            },
        )
        IconButton(
            onClick = {
                onDetailContinueClicked("detail")
            },
            modifier =
            Modifier.constrainAs(icon) {
                top.linkTo(parent.top)
                end.linkTo(parent.end, margin = 20.dp)
                bottom.linkTo(parent.bottom, margin = 20.dp)
                width = Dimension.value(56.dp)
                height = Dimension.value(56.dp)
            },
            colors =
            IconButtonColors(
                contentColor = Color.Black,
                containerColor = Color.White,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.White,
            ),
        ) {
            Icon(
                imageVector = KeyboardArrowRight,
                contentDescription = "next",
            )
        }
    }
}

@Composable
fun IndicatorRow(
    modifier: Modifier = Modifier,
    currentPage: Int,
    indicatorCount: Int,
) {
    LazyRow(
        state = rememberLazyListState(),
        modifier = modifier,
    ) {
        repeat(indicatorCount) { iteration ->
            val color = if (currentPage == iteration) Color.DarkGray else Color.LightGray
            item {
                val size = if (currentPage == iteration) 10.dp else 6.dp
                Spacer(
                    modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
                        .size(size)
                        .clip(CircleShape)
                        .background(color),
                )
            }
        }
    }
}

@Composable
fun HomeScreenBackground(imageUrl: String) {
    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground(
                topColor = Black01,
                bottomColor = Black,
            ),
        model = imageUrl,
        contentDescription = "home background",
        contentScale = ContentScale.Crop,
        error = painterResource(uiR.drawable.ic_survey_background_1),
    )
}
