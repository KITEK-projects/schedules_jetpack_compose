package com.example.kitekapp.ui.components.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kitekapp.R
import com.example.kitekapp.data.model.Lesson
import com.example.kitekapp.domain.calculateLessonSchedule
import com.example.kitekapp.ui.theme.customColors
import com.example.kitekapp.ui.theme.customTypography
import com.example.kitekapp.viewmodel.MyViewModel

@Composable
fun ScheduleListItem(
    viewModel: MyViewModel,
    item: Lesson,
    date: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(customColors.itemPrimary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            item.items.mapIndexed { index, lesson ->
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = lesson.title,
                            style = customTypography.robotoRegular16,
                            color = customColors.mainText,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = lesson.type,
                            style = customTypography.robotoRegular14,
                            color = customColors.secondaryTextAndIcons,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = calculateLessonSchedule(viewModel, item.number, lesson.partner, date),
                        style = customTypography.robotoRegular14,
                        color = customColors.secondaryTextAndIcons,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    color = customColors.itemSecondary,
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_partner),
                                contentDescription = "partner",
                                tint = customColors.secondaryTextAndIcons,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = lesson.partner,
                                style = customTypography.robotoRegular12,
                                color = customColors.mainText,
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        if (!lesson.location.isNullOrEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_location),
                                    contentDescription = "location",
                                    tint = customColors.secondaryTextAndIcons,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = lesson.location,
                                    style = customTypography.robotoMedium12,
                                    color = customColors.mainText,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    if (item.items.size > 1 && index == 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = customColors.forLine)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}