package dev.sasikanth.fancy.toggle

import android.graphics.Path
import androidx.annotation.FloatRange
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.sasikanth.fancy.toggle.SwitchDefaults.RevealProgressChecked
import dev.sasikanth.fancy.toggle.SwitchDefaults.RevealProgressUnchecked
import dev.sasikanth.fancy.toggle.SwitchDefaults.StiffnessMedium
import kotlin.math.roundToInt

/**
 * Switch can be used to toggle between on and off states.
 *
 * @param checked: whether this switch is checked or not
 * @param enabled: controls the enabled state of this switch. When false, switch will not
 * respond to user input, and visual appearance changes to indicate it is disabled
 * @param onValueChange: Callback to notify state when the switch state changes
 */
@Composable
fun FancySwitch(
  checked: Boolean,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  switchColors: SwitchColors = SwitchDefaults.colors(),
  onValueChange: ((Boolean) -> Unit)? = null
) {
  val rippleColor by switchColors.rippleColor()

  val toggleableModifier = if (onValueChange != null) {
    Modifier.toggleable(
      value = checked,
      enabled = enabled,
      interactionSource = remember { MutableInteractionSource() },
      indication = rememberRipple(color = rippleColor),
      role = Role.Switch,
      onValueChange = onValueChange
    )
  } else {
    Modifier
  }

  SwitchContainer(
    modifier = modifier
      .requiredSize(SwitchDefaults.TrackWidth, SwitchDefaults.TrackHeight)
      .padding(vertical = SwitchDefaults.VerticalPadding)
      .clip(SwitchDefaults.TrackShape)
      .then(toggleableModifier)
      .testTag("Switch"),
    checked = checked,
    enabled = enabled,
    switchColors = switchColors
  )
}

@Composable
private fun SwitchContainer(
  checked: Boolean,
  enabled: Boolean,
  switchColors: SwitchColors,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    val transition = updateTransition(
      targetState = checked,
      label = "Switch"
    )

    val thumbSize = SwitchDefaults.ThumbSize
    val thumbPadding = SwitchDefaults.ThumbPadding

    val density = LocalDensity.current

    val trackWidthPx = with(density) { SwitchDefaults.TrackWidth.toPx() }
    val thumbSizePx = with(density) { thumbSize.toPx() }
    val thumbPaddingPx = with(density) { thumbPadding.toPx() }

    val thumbCenterPx = thumbSizePx / 2f

    @Suppress("UnnecessaryVariable")
    val thumbOffsetMin = thumbPaddingPx
    val thumbOffsetMax = (trackWidthPx - thumbSizePx) - thumbPaddingPx

    val revealOffsetMin = thumbPaddingPx + thumbCenterPx
    val revealOffsetMax = (trackWidthPx - thumbCenterPx) - thumbPaddingPx

    val thumbOffset by transition.animateFloat(
      label = "ThumbOffset",
      transitionSpec = {
        spring(stiffness = StiffnessMedium)
      }
    ) {
      if (it) thumbOffsetMax else thumbOffsetMin
    }

    val revealOffset by transition.animateFloat(
      label = "ThumbOffset",
      transitionSpec = {
        spring(stiffness = StiffnessMedium)
      }
    ) {
      if (it) revealOffsetMax else revealOffsetMin
    }

    val revealProgress by transition.animateFloat(
      label = "RevealProgress",
      transitionSpec = {
        spring(stiffness = StiffnessMedium)
      }
    ) {
      if (it) RevealProgressChecked else RevealProgressUnchecked
    }

    if (revealProgress != RevealProgressChecked || !enabled) {
      SwitchOff(
        checked = checked,
        enabled = enabled,
        switchColors = switchColors
      ) { thumbOffset }
    }

    /**
     * When a switch is enabled and turned on, we animate the checked
     * state using a circular reveal. Colors used to indicate
     * checked switch are different from unchecked state.
     *
     * Since we want the content to have a "clipping" effect
     * as the reveal happens. We are placing the checked switch
     * above the unchecked switch and animate the clip using
     * circular reveal.
     */
    if (enabled && revealProgress != RevealProgressUnchecked) {
      SwitchOn(
        checked = checked,
        switchColors = switchColors,
        thumbOffsetProvider = { thumbOffset },
        revealProgressProvider = { revealProgress }
      ) { revealOffset }
    }
  }
}

@Composable
private fun SwitchOff(
  checked: Boolean,
  enabled: Boolean,
  switchColors: SwitchColors,
  modifier: Modifier = Modifier,
  thumbOffsetProvider: () -> Float
) {
  val uncheckedTrackColor by switchColors.uncheckedTrackColor(enabled = enabled)
  val uncheckedThumbColor by switchColors.uncheckedThumbColor(enabled = enabled)
  val uncheckedIconColor by switchColors.uncheckedIconTint(enabled = enabled)

  SwitchImpl(
    modifier = modifier,
    checked = checked,
    trackColor = uncheckedTrackColor,
    thumbColor = uncheckedThumbColor,
    iconTint = uncheckedIconColor,
    thumbOffsetProvider = thumbOffsetProvider
  )
}

@Composable
private fun SwitchOn(
  checked: Boolean,
  switchColors: SwitchColors,
  thumbOffsetProvider: () -> Float,
  revealProgressProvider: () -> Float,
  modifier: Modifier = Modifier,
  revealOffsetProvider: () -> Float
) {
  val trackColor by switchColors.checkedTrackColor()
  val thumbColor by switchColors.checkedThumbColor()
  val iconTint by switchColors.checkedIconTint()

  SwitchImpl(
    modifier = modifier
      .graphicsLayer {
        clip = true
        shape = CircularRevealShape(
          progress = revealProgressProvider(),
          offset = revealOffsetProvider()
        )
      },
    checked = checked,
    trackColor = trackColor,
    thumbColor = thumbColor,
    iconTint = iconTint,
    thumbOffsetProvider = thumbOffsetProvider
  )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun SwitchImpl(
  checked: Boolean,
  trackColor: Color,
  thumbColor: Color,
  iconTint: Color,
  modifier: Modifier = Modifier,
  thumbOffsetProvider: () -> Float
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(trackColor)
  ) {
    Box(
      modifier = Modifier
        .align(Alignment.CenterStart)
        .offset {
          IntOffset(
            x = thumbOffsetProvider().roundToInt(),
            y = 0
          )
        }
        .requiredSize(SwitchDefaults.ThumbSize)
        .background(
          color = thumbColor,
          shape = SwitchDefaults.ThumbShape
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        modifier = Modifier.requiredSize(16.dp),
        painter = rememberAnimatedVectorPainter(
          animatedImageVector = AnimatedImageVector
            .animatedVectorResource(id = R.drawable.avd_switch),
          atEnd = checked
        ),
        contentDescription = null,
        tint = iconTint
      )
    }
  }
}

private class CircularRevealShape(
  @FloatRange(from = 0.0, to = 1.0) private val progress: Float,
  private val offset: Float
) : Shape {

  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density
  ): Outline {
    val radius = with(density) { SwitchDefaults.RevealRadius.toPx() } * progress

    val yCenter = size.height / 2

    return Outline.Generic(
      Path().apply {
        addCircle(
          offset,
          yCenter,
          radius,
          Path.Direction.CW
        )
      }.asComposePath()
    )
  }
}

private object SwitchDefaults {
  val TrackWidth = 64.dp
  val TrackHeight = 48.dp

  val VerticalPadding = 4.dp
  val ThumbPadding = 6.dp

  val ThumbSize = 28.dp

  val TrackShape = CircleShape
  val ThumbShape = CircleShape

  val RevealRadius = 44.dp

  const val StiffnessMedium = (Spring.StiffnessMedium + Spring.StiffnessMediumLow) / 2f

  const val RevealProgressChecked = 1f
  const val RevealProgressUnchecked = 0f

  private const val DisabledAlpha = 0.12f

  @Composable
  fun colors(
    uncheckedTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    checkedTrackColor: Color = MaterialTheme.colorScheme.primary,
    disabledTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(
      alpha = DisabledAlpha
    ),
    uncheckedThumbColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    checkedThumbColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledThumbColor: Color = MaterialTheme.colorScheme.onSurface.copy(
      alpha = DisabledAlpha
    ),
    uncheckedIconTint: Color = MaterialTheme.colorScheme.surfaceVariant,
    checkedIconTint: Color = MaterialTheme.colorScheme.primary,
    disabledIconTint: Color = MaterialTheme.colorScheme.surface,
    rippleColor: Color = MaterialTheme.colorScheme.primary
  ) = SwitchColors(
    uncheckedTrackColor = uncheckedTrackColor,
    checkedTrackColor = checkedTrackColor,
    disabledTrackColor = disabledTrackColor,
    uncheckedThumbColor = uncheckedThumbColor,
    checkedThumbColor = checkedThumbColor,
    disabledThumbColor = disabledThumbColor,
    uncheckedIconTint = uncheckedIconTint,
    checkedIconTint = checkedIconTint,
    disabledIconTint = disabledIconTint,
    rippleColor = rippleColor
  )
}

@Immutable
class SwitchColors internal constructor(
  private val uncheckedTrackColor: Color,
  private val checkedTrackColor: Color,
  private val disabledTrackColor: Color,
  private val uncheckedThumbColor: Color,
  private val checkedThumbColor: Color,
  private val disabledThumbColor: Color,
  private val uncheckedIconTint: Color,
  private val checkedIconTint: Color,
  private val disabledIconTint: Color,
  private val rippleColor: Color
) {

  @Composable
  internal fun uncheckedTrackColor(enabled: Boolean): State<Color> {
    return rememberUpdatedState(
      if (enabled) {
        uncheckedTrackColor
      } else {
        disabledTrackColor
      }
    )
  }

  @Composable
  internal fun checkedTrackColor(): State<Color> {
    return rememberUpdatedState(checkedTrackColor)
  }

  @Composable
  internal fun uncheckedThumbColor(enabled: Boolean): State<Color> {
    return rememberUpdatedState(
      if (enabled) {
        uncheckedThumbColor
      } else {
        disabledThumbColor
      }
    )
  }

  @Composable
  internal fun checkedThumbColor(): State<Color> {
    return rememberUpdatedState(checkedThumbColor)
  }

  @Composable
  internal fun uncheckedIconTint(enabled: Boolean): State<Color> {
    return rememberUpdatedState(
      if (enabled) {
        uncheckedIconTint
      } else {
        disabledIconTint
      }
    )
  }

  @Composable
  internal fun checkedIconTint(): State<Color> {
    return rememberUpdatedState(checkedIconTint)
  }

  @Composable
  internal fun rippleColor(): State<Color> {
    return rememberUpdatedState(rippleColor)
  }
}

@Preview
@Composable
private fun SwitchPreview_Unchecked() {
  MaterialTheme {
    FancySwitch(
      checked = false
    ) {
      // Handle value changes
    }
  }
}

@Preview
@Composable
private fun SwitchPreview_Unchecked_Disabled() {
  MaterialTheme {
    FancySwitch(
      checked = false,
      enabled = false
    ) {
      // Handle value changes
    }
  }
}

@Preview
@Composable
private fun SwitchPreview_Checked() {
  MaterialTheme {
    FancySwitch(
      checked = true
    ) {
      // Handle value changes
    }
  }
}

@Preview
@Composable
private fun SwitchPreview_Checked_Disabled() {
  MaterialTheme {
    FancySwitch(
      checked = true,
      enabled = false
    ) {
      // Handle value changes
    }
  }
}
