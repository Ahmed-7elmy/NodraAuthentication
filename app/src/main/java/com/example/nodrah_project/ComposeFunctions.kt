package com.example.nodrah_project

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.DyslexicFont
import com.example.nodrah_project.ui.theme.LocalAppColorScheme

@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    var lineHeightVal by remember { mutableStateOf(24.0) }
    var isContrast by remember { mutableStateOf(false) }
    var isMonochromeTheme by remember { mutableStateOf(false) }
    var useCustomFont by remember { mutableStateOf(false) }
    val currentFont = if (useCustomFont) DyslexicFont else FontFamily.Default
    var fontSizeVal by remember { mutableStateOf(20.0) }
    var letterSpacingVal by remember { mutableStateOf(0.0) }
    var contentScaleVal by remember { mutableStateOf(400.0) }


    AppTheme(isContrastTheme = isContrast, isMonoChrome = isMonochromeTheme) {
        val colors = LocalAppColorScheme.current
        val context = LocalContext.current
        val items = listOf(
            BottomNavItem("Home", Icons.Default.Home),
            BottomNavItem("Video", Icons.Default.PlayArrow),
            BottomNavItem("Accessibility", Icons.Rounded.Accessibility),
            BottomNavItem("Notification", Icons.Default.Notifications),
            BottomNavItem("Profile", Icons.Default.Person)
        )

        NavigationBar(
            containerColor = colors.primary,
            tonalElevation = 4.dp,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))

        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)),
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = colors.onPrimary
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = fontSizeVal.sp,
                            style = TextStyle(color = colors.onPrimary),
                            fontFamily = currentFont,
                        )
                    },
                    selected = selectedItem == index,
                    onClick = {
                        onItemSelected(index)

                        if (index == 0) {
                            val intent = Intent(context, HomeActivity::class.java)
                            context.startActivity(intent)
                        }else if (index == 1) {
                            val intent = Intent(context, VideosActivity::class.java)
                            context.startActivity(intent)
                        } else if (index == 2) {
                            val intent = Intent(context, AccessibilityActivity::class.java)
                            context.startActivity(intent)


                    }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colors.secondary,
                        unselectedIconColor = colors.onPrimary,
                        indicatorColor = Color.Transparent
                    ),
                )
            }
        }
    }
}

@Composable
fun RowButton(
    s1: String,
    s2: String,
    icon1: Int? = null,
    icon2: Int? = null,
    onClick1: () -> Unit,
    onClick2: () -> Unit,
    fontFamily: FontFamily = FontFamily.Default,

    ) {
    val colors = LocalAppColorScheme.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedCard(
            onClick = onClick1,
            colors = CardDefaults.elevatedCardColors(
                containerColor = colors.primary, contentColor = colors.onPrimary
            ),
            shape = RoundedCornerShape(6.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            modifier = Modifier.size(160.dp, 83.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (icon1 != null) {
                    Image(
                        painter = painterResource(id = icon1),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(colors.onPrimary),
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    s1,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamily,

                    )
            }
        }

        ElevatedCard(
            onClick = onClick2,
            colors = CardDefaults.elevatedCardColors(
                containerColor = colors.primary, contentColor = colors.onPrimary
            ),
            shape = RoundedCornerShape(6.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            modifier = Modifier.size(160.dp, 83.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (icon2 != null) {
                    Image(
                        painter = painterResource(id = icon2),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(colors.onPrimary),
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    s2,
                    fontFamily = fontFamily
                )
            }
        }
    }
}

@Composable
fun CardLineHeight(
    text: String,
    fontFamily: FontFamily = FontFamily.Default,
    lineheightValue: Double,
    onLineHeightChange: (Double) -> Unit,

    ) {
    val colors = LocalAppColorScheme.current

    ElevatedCard(

        colors = CardDefaults.elevatedCardColors(
            containerColor = colors.primary, contentColor = colors.onPrimary
        ),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier.size(160.dp, 83.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$text",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = fontFamily,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.down_arrow_icon),
                    colorFilter = ColorFilter.tint(colors.onPrimary),
                    contentDescription = "down arrow",
                    modifier = Modifier
                        .clickable { onLineHeightChange(lineheightValue - 2.4) } //logic
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${(lineheightValue / 24.0 * 100).toInt()}%", //logic
                    fontFamily = fontFamily,

                    )
                Image(
                    painter = painterResource(R.drawable.up_arrow_icon),
                    colorFilter = ColorFilter.tint(colors.onPrimary),
                    contentDescription = "up arrow",
                    modifier = Modifier
                        .clickable { onLineHeightChange(lineheightValue + 2.41) } //logic
                )
            }
        }
    }
}

@Composable
fun CardLetterSpacing(
    text: String,
    fontFamily: FontFamily = FontFamily.Default,
    letterSpacingValue: Double,
    onLetterSpacingChange: (Double) -> Unit,

) {
    val colors = LocalAppColorScheme.current

    ElevatedCard(

        colors = CardDefaults.elevatedCardColors(
            containerColor = colors.primary, contentColor = colors.onPrimary
        ),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier.size(160.dp, 83.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$text",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = fontFamily,

                )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.down_arrow_icon),
                    colorFilter = ColorFilter.tint(colors.onPrimary),
                    contentDescription = "down arrow",
                    modifier = Modifier
                        .clickable {
                            onLetterSpacingChange(letterSpacingValue -1)
                        }//logic
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${((letterSpacingValue * 10) + 100).toInt()}%", //logic
                    fontFamily = fontFamily,
                )
                Image(
                    painter = painterResource(R.drawable.up_arrow_icon),
                    colorFilter = ColorFilter.tint(colors.onPrimary),
                    contentDescription = "up arrow",
                    modifier = Modifier
                        .clickable { onLetterSpacingChange(letterSpacingValue + 1) } //logic
                )
            }
        }
    }
}

@Composable
fun CardFontSize(
    text: String,
    fontFamily: FontFamily = FontFamily.Default,
    fontSizeValue: Double,
    onFontSizeChange: (Double) -> Unit,
) {
    val colors = LocalAppColorScheme.current

    ElevatedCard(

        colors = CardDefaults.elevatedCardColors(
            containerColor = colors.primary, contentColor = colors.onPrimary
        ),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .size(160.dp, 83.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$text",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = fontFamily,

                )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.down_arrow_icon),
                    colorFilter = ColorFilter.tint(colors.onPrimary),
                    contentDescription = "down arrow",
                    modifier = Modifier
                        .clickable { onFontSizeChange(fontSizeValue - 2) } //logic
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${(fontSizeValue / 20.0 * 100).toInt()}%", //logic
                    fontFamily = fontFamily,
                )
                Image(
                    painter = painterResource(R.drawable.up_arrow_icon),
                    colorFilter = ColorFilter.tint(colors.onPrimary),
                    contentDescription = "up arrow",
                    modifier = Modifier
                        .clickable { onFontSizeChange(fontSizeValue + 2) } //logic
                )
            }
        }
    }
}

@Composable
fun CardContentScale(
    text: String,
    fontFamily: FontFamily = FontFamily.Default,
    contentScaleValue: Double,
    onContentScaleChange: (Double) -> Unit,
) {
    val colors = LocalAppColorScheme.current

    ElevatedCard(

        colors = CardDefaults.elevatedCardColors(
            containerColor = colors.primary, contentColor = colors.onPrimary
        ),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier.size(160.dp, 83.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$text",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = fontFamily,

                )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.down_arrow_icon),
                    colorFilter = ColorFilter.tint(colors.onPrimary),
                    contentDescription = "down arrow",
                    modifier = Modifier
                        .clickable { onContentScaleChange(contentScaleValue - 40) } //logic
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${(contentScaleValue / 400.0 * 100).toInt()}%", //logic
                    fontFamily = fontFamily,
                )
                Image(
                    painter = painterResource(R.drawable.up_arrow_icon),
                    colorFilter = ColorFilter.tint(colors.onPrimary),
                    contentDescription = "up arrow",
                    modifier = Modifier
                        .clickable { onContentScaleChange(contentScaleValue + 40) } //logic
                )
            }
        }
    }
}