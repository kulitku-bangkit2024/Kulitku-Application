package com.dicoding.kulitku.view.home.banner

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dicoding.kulitku.R

@Suppress("DEPRECATION")
class SliderPagerAdapter(fm: FragmentManager, private val mFrags: List<Fragment>) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val index = position % mFrags.size
        val imageResId = mFrags[index].arguments?.getInt("imgSlider") ?: R.drawable.img_placeholder
        return BannerFragment.newInstance(imageResId)
    }

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }
}