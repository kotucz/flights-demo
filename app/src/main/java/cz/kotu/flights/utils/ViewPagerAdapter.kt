package cz.kotu.flights.utils

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

abstract class ViewPagerAdapter : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int) =
        createPageView(container, position)
            .apply { container.addView(this) }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, any: Any) = view == any

    abstract fun createPageView(container: ViewGroup, position: Int): View
}