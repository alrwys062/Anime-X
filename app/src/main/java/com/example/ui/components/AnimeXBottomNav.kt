package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.TextSecondary

enum class NavTab(val titleAr: String) {
    HOME("الرئيسية"),
    CATEGORIES("الأقسام"),
    MY_LIST("قائمتي"),
    MORE("المزيد")
}

@Composable
fun AnimeXBottomNav(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBackground)
    ) {
        HorizontalDivider(color = BorderStroke, thickness = 0.5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Home
            BottomNavItem(
                title = NavTab.HOME.titleAr,
                icon = if (selectedTab == NavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                isSelected = selectedTab == NavTab.HOME,
                onClick = { onTabSelected(NavTab.HOME) },
                testTag = "nav_home"
            )

            // Tab 2: Categories
            BottomNavItem(
                title = NavTab.CATEGORIES.titleAr,
                icon = if (selectedTab == NavTab.CATEGORIES) Icons.Filled.Category else Icons.Outlined.Category,
                isSelected = selectedTab == NavTab.CATEGORIES,
                onClick = { onTabSelected(NavTab.CATEGORIES) },
                testTag = "nav_categories"
            )

            // Tab 3: My List
            BottomNavItem(
                title = NavTab.MY_LIST.titleAr,
                icon = if (selectedTab == NavTab.MY_LIST) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                isSelected = selectedTab == NavTab.MY_LIST,
                onClick = { onTabSelected(NavTab.MY_LIST) },
                testTag = "nav_my_list"
            )

            // Tab 4: More
            BottomNavItem(
                title = NavTab.MORE.titleAr,
                icon = if (selectedTab == NavTab.MORE) Icons.Filled.Menu else Icons.Outlined.Menu,
                isSelected = selectedTab == NavTab.MORE,
                onClick = { onTabSelected(NavTab.MORE) },
                testTag = "nav_more"
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .testTag(testTag)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) AnimeRedPrimary else TextSecondary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            color = if (isSelected) AnimeRedPrimary else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
