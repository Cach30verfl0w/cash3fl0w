/*
 * Copyright 2024 Cach30verfl0w
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cacheoverflow.cashflow.utils

import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue

operator fun Color.times(factor: Float): Color {
    return Color(
        (red * factor).coerceIn(0F..1F),
        (green * factor).coerceIn(0F..1F),
        (blue * factor).coerceIn(0F..1F),
        alpha
    )
}

operator fun Color.div(factor: Float): Color {
    return Color(
        (red / factor).coerceIn(0F..1F),
        (green / factor).coerceIn(0F..1F),
        (blue / factor).coerceIn(0F..1F),
        alpha
    )
}

inline val Color.minChannel: Float
    get() = minOf(red, green, blue)

inline val Color.maxChannel: Float
    get() = maxOf(red, green, blue)

inline val Color.hue: Float
    get() {
        val max = maxChannel
        val delta = max - minChannel
        return ((if (delta == 0F) 0F
        else when (max) {
            red -> (green - blue) / delta + (if (green < blue) 6F else 0F)
            green -> (blue - red) / delta + 2F
            blue -> (red - green) / delta + 4F
            else -> 0F
        } / 6F) * 360F).coerceIn(0F..360F)
    }

inline val Color.lightness: Float
    get() = ((maxChannel + minChannel) / 2F).coerceIn(0F..1F)

inline val Color.saturation: Float
    get() {
        val max = maxChannel
        val delta = max - minChannel
        return (if (delta == 0F) 0F
        else delta / (1F - (2F * lightness - 1F).absoluteValue)).coerceIn(0F..1F)
    }

fun SwitchColors.grayOutIfDisabled(enabled: Boolean): SwitchColors = if (enabled) {
    this
} else {
    SwitchColors(
        this.checkedThumbColor.deriveHSL(saturation = 0.2f),
        this.checkedTrackColor.deriveHSL(saturation = 0.2f),
        this.checkedBorderColor.deriveHSL(saturation = 0.2f),
        this.checkedIconColor.deriveHSL(saturation = 0.2f),
        this.uncheckedThumbColor.deriveHSL(saturation = 0.2f),
        this.uncheckedTrackColor.deriveHSL(saturation = 0.2f),
        this.uncheckedBorderColor.deriveHSL(saturation = 0.2f),
        this.uncheckedIconColor.deriveHSL(saturation = 0.2f),
        this.disabledCheckedThumbColor.deriveHSL(saturation = 0.2f),
        this.disabledCheckedTrackColor.deriveHSL(saturation = 0.2f),
        this.disabledCheckedBorderColor.deriveHSL(saturation = 0.2f),
        this.disabledCheckedIconColor.deriveHSL(saturation = 0.2f),
        this.disabledUncheckedThumbColor.deriveHSL(saturation = 0.2f),
        this.disabledUncheckedTrackColor.deriveHSL(saturation = 0.2f),
        this.disabledUncheckedBorderColor.deriveHSL(saturation = 0.2f),
        this.disabledUncheckedIconColor.deriveHSL(saturation = 0.2f)
    )
}

fun RadioButtonColors.grayOutIfDisabled(enabled: Boolean): RadioButtonColors = if (enabled) {
    this
} else {
    RadioButtonColors(
        this.selectedColor.deriveHSL(saturation = 0.2f),
        this.unselectedColor.deriveHSL(saturation = 0.2f),
        this.disabledSelectedColor.deriveHSL(saturation = 0.2f),
        this.disabledUnselectedColor.deriveHSL(saturation = 0.2f)
    )
}

fun Color.deriveHSL(
    hue: Float = this.hue,
    saturation: Float = this.saturation,
    lightness: Float = this.lightness,
    alpha: Float = this.alpha
): Color = Color.hsl(hue, saturation, lightness, alpha)