package rahulstech.jfx.balancesheet.frontend.util;

import javafx.animation.Interpolator;
import javafx.util.Duration;

public class AnimationUtil {

    public static final Interpolator EMPHASIZED = Interpolator.SPLINE(0.166,0.4,0.25,0.1);

    public static final Interpolator EMPHASIZED_ACCELERATE = Interpolator.SPLINE(0.3,0.0,0.8,0.15);

    public static final Interpolator EMPHASIZED_DECELERATE = Interpolator.SPLINE(0.05,0.7,0.1,1.0); // TODO: check

    public static final Interpolator STANDARD = Interpolator.SPLINE(0.2, 0, 0, 1);

    public static final Interpolator STANDARD_ACCELERATE = Interpolator.SPLINE(0.3, 0, 1, 1);

    public static final Interpolator STANDARD_DECELERATE = Interpolator.SPLINE(0, 0, 0, 1);

    public static final Interpolator LEGACY = Interpolator.SPLINE(0.4, 0, 0.2, 1);

    public static final Interpolator LEGACY_ACCELERATE = Interpolator.SPLINE(0.4, 0, 1, 1);

    public static final Interpolator LEGACY_DECELERATE = Interpolator.SPLINE(0, 0, 0.2, 1);

    private final Interpolator X_CUBE = Interpolator.SPLINE(0.33,0.033, 0.66,0.296);

    public static final Duration DURATION_SHORT1 = Duration.millis(50);

    public static final Duration DURATION_SHORT2 = Duration.millis(100);

    public static final Duration DURATION_SHORT3 = Duration.millis(150);

    public static final Duration DURATION_SHORT4 = Duration.millis(200);

    public static final Duration DURATION_MEDIUM1 = Duration.millis(250);

    public static final Duration DURATION_MEDIUM2 = Duration.millis(300);

    public static final Duration DURATION_MEDIUM3 = Duration.millis(350);

    public static final Duration DURATION_MEDIUM4 = Duration.millis(400);

    public static final Duration DURATION_LONG1 = Duration.millis(450);

    public static final Duration DURATION_LONG2 = Duration.millis(500);

    public static final Duration DURATION_LONG3 = Duration.millis(550);

    public static final Duration DURATION_LONG4 = Duration.millis(600);

    public static final Duration DURATION_EXTRA_LONG1 = Duration.millis(700);

    public static final Duration DURATION_EXTRA_LONG2 = Duration.millis(800);

    public static final Duration DURATION_EXTRA_LONG3 = Duration.millis(900);

    public static final Duration DURATION_EXTRA_LONG4 = Duration.millis(1000);

    private AnimationUtil() {}
}
