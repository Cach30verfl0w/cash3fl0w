/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
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
        this.checkedThumbColor.deriveHSL(saturation = 0.0f),
        this.checkedTrackColor.deriveHSL(saturation = 0.0f),
        this.checkedBorderColor.deriveHSL(saturation = 0.0f),
        this.checkedIconColor.deriveHSL(saturation = 0.0f),
        this.uncheckedThumbColor.deriveHSL(saturation = 0.0f),
        this.uncheckedTrackColor.deriveHSL(saturation = 0.0f),
        this.uncheckedBorderColor.deriveHSL(saturation = 0.0f),
        this.uncheckedIconColor.deriveHSL(saturation = 0.0f)
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

fun Color.deriveHSLIf(
    derive: Boolean,
    hue: Float = this.hue,
    saturation: Float = this.saturation,
    lightness: Float = this.lightness,
    alpha: Float = this.alpha
): Color = if (derive) {
    this.deriveHSL(hue, saturation, lightness, alpha)
} else {
    this
}

fun Color.deriveHSL(
    hue: Float = this.hue,
    saturation: Float = this.saturation,
    lightness: Float = this.lightness,
    alpha: Float = this.alpha
): Color = Color.hsl(hue, saturation, lightness, alpha)