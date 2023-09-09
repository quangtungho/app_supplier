package vn.techres.supplier.animation

import android.view.View
import androidx.viewpager.widget.ViewPager

class FadePageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        when {
            position < -0.999f -> {
                view.alpha = 0f
                view.visibility = View.GONE
                view.translationX = pageWidth.toFloat()
            }
            position <= 0.999f -> {
                view.alpha = getAlpha(position)
                view.translationX = pageWidth * -position
                view.visibility = View.VISIBLE
            }
            else -> {
                view.alpha = 0f
                view.visibility = View.GONE
                view.translationX = -pageWidth.toFloat()
            }
        }
    }

    companion object {
        /**
         * Get the alpha value that should be applied to a position.
         *
         * @param position Position to find an alpha for.
         * @return An alpha value.
         */
        private fun getAlpha(position: Float): Float {
            return getSlowQuadraticAlpha(position)
        }

        private fun getSlowQuadraticAlpha(position: Float): Float {
            return 1 - position * position
        } //    @Override
        //    public void transformPage(@NonNull @NotNull View page, float position) {
        //        page.setTranslationX(page.getWidth() * (-position));
        //
        //        if (position <= -1.0F || position >= 1.0F)
        //            page.setAlpha(0.0F);
        //        else if (position == 0.0F)
        //            page.setAlpha(1.0F);
        //        else
        //            page.setAlpha(1.0F - Math.abs(position));
        //    }
    }
}