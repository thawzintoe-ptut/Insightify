package com.ptut.insightify.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ptut.insightify.ui.InsightifyIcons.KeyboardArrowRight
import com.ptut.insightify.ui.R
import com.ptut.insightify.ui.theme.neuzeitFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeRoute(innerPaddingValues: PaddingValues) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val indicatorScrollState = rememberLazyListState()

    HomeScreenPager(
        pagerState = pagerState,
        indicatorScrollState = indicatorScrollState,
        innerPaddingValues = innerPaddingValues,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenPager(
    pagerState: PagerState,
    indicatorScrollState: LazyListState,
    innerPaddingValues: PaddingValues = PaddingValues(20.dp),
) {
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
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            HomeScreenBackground(page)
        }

        TopProfileContent(
            modifier =
                Modifier.padding(innerPaddingValues).padding(20.dp)
                    .align(Alignment.TopStart),
        )

        Column(
            Modifier.padding(innerPaddingValues).align(Alignment.BottomCenter),
        ) {
            IndicatorRow(currentPage = pagerState.currentPage, pageCount = pagerState.pageCount)
            BottomDescriptionContent(
                currentPage = pagerState.currentPage,
            )
        }
    }
}

@Composable
fun TopProfileContent(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Monday June 2023",
            style =
                MaterialTheme.typography.titleSmall.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontFamily = neuzeitFontFamily,
                ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Good Morning",
                style =
                    MaterialTheme.typography.titleLarge.copy(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontFamily = neuzeitFontFamily,
                    ),
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_dummy_profile),
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(36.dp).clip(CircleShape),
            )
        }
    }
}

@Composable
fun BottomDescriptionContent(currentPage: Int) {
    val titles =
        listOf(
            "Welcome to Insightify",
            "Career training and development",
            "Inclusion and Belonging",
        )
    val descriptions =
        listOf(
            "We would like to know how you feel about our work from home...",
            "We would like to know what are your goals and skills you wanted...",
            "Building a workplace culture that prioritizes belonging and inclusio...",
        )

    Text(
        text = titles.getOrNull(currentPage) ?: "",
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
            text = descriptions.getOrNull(currentPage) ?: "",
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
            onClick = { /*TODO*/ },
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
    currentPage: Int,
    pageCount: Int,
) {
    LazyRow(
        state = rememberLazyListState(),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        repeat(pageCount) { iteration ->
            val color = if (currentPage == iteration) Color.DarkGray else Color.LightGray
            item {
                val size = if (currentPage == iteration) 10.dp else 6.dp
                Spacer(
                    modifier =
                        Modifier.padding(horizontal = 4.dp).size(size).clip(CircleShape)
                            .background(color),
                )
            }
        }
    }
}

@Composable
fun HomeScreenBackground(screenCurrentPage: Int) {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter =
            painterResource(
                when (screenCurrentPage) {
                    0 -> R.drawable.ic_survey_background_1
                    1 -> R.drawable.ic_survey_background_2
                    else -> R.drawable.ic_survey_background_3
                },
            ),
        contentDescription = "home background",
    )
}
