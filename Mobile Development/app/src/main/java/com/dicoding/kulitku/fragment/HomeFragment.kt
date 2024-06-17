package com.dicoding.kulitku.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kulitku.R
import com.dicoding.kulitku.adapter.OnSkinInformationClickListener
import com.dicoding.kulitku.adapter.SkinInformationAdapter
import com.dicoding.kulitku.data.informasiItem
import com.dicoding.kulitku.databinding.FragmentHomeBinding
import com.dicoding.kulitku.view.HomeViewModel
import com.dicoding.kulitku.view.UserModelFactory
import com.dicoding.kulitku.view.UserPreferences
import com.dicoding.kulitku.view.home.banner.BannerFragment
import com.dicoding.kulitku.view.home.banner.BannerSlider
import com.dicoding.kulitku.view.home.banner.SliderIndicator
import com.dicoding.kulitku.view.home.banner.SliderPagerAdapter
import com.dicoding.kulitku.view.login.dataStore
import com.dicoding.kulitku.view.quiz.QuizMainActivity

class HomeFragment : Fragment(), View.OnClickListener, OnSkinInformationClickListener {

    // banner
    private lateinit var mAdapter: SliderPagerAdapter
    private lateinit var mIndicator: SliderIndicator

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userPreferences = UserPreferences.getInstance(requireContext().dataStore)
        val homeViewModel = ViewModelProvider(
            this,
            UserModelFactory(userPreferences)
        ).get(HomeViewModel::class.java)

        homeViewModel.userName.observe(viewLifecycleOwner, Observer { userName ->
            binding.textViewUser.text = "Hello $userName"
        })

        // BANNER
        val bannerSlider = binding.ilBanner.sliderView
        val mLinearLayout = binding.ilBanner.pagesContainer

        setupSlider(bannerSlider, mLinearLayout)

        val rvInformasiItem = binding.recyclerViewArticles2
        val layoutManagerInformasiItem =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvInformasiItem.layoutManager = layoutManagerInformasiItem
        rvInformasiItem.adapter = SkinInformationAdapter(informasiItem, this)

        // Set click listeners for menu items
        binding.menuTips.setOnClickListener(this)
        binding.menuInformasi.setOnClickListener(this)
        binding.menuQuiz.setOnClickListener(this)

        binding.readMoreButton.setOnClickListener(this)

        binding.menuQuiz.setOnClickListener {
            val intent = Intent(context, QuizMainActivity::class.java)
            startActivity(intent)
        }

        // Cek apakah ada data yang dikirimkan dari Activity
        val backToFragmentHome = arguments?.getBoolean("backToFragmentHome", false)

        if (backToFragmentHome == true) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, this)
                .commit()
        }
        return root
    }

    private fun setupSlider(bannerSlider: BannerSlider, mLinearLayout: LinearLayout) {
        bannerSlider.setDurationScroll(800)
        val fragments: MutableList<Fragment> = ArrayList()

        fragments.add(BannerFragment.newInstance(R.drawable.banner))
        fragments.add(BannerFragment.newInstance(R.drawable.banner2))
        fragments.add(BannerFragment.newInstance(R.drawable.banner3))
        fragments.add(BannerFragment.newInstance(R.drawable.banner4))
        fragments.add(BannerFragment.newInstance(R.drawable.banner5))

        mAdapter = SliderPagerAdapter(childFragmentManager, fragments)
        bannerSlider.adapter = mAdapter

        mIndicator =
            SliderIndicator(context, mLinearLayout, bannerSlider, R.drawable.indicator_circle)
        mIndicator.setPageCount(fragments.size)
        mIndicator.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            // R.id.menu_tips -> navigateToFragment(R.id.navigation_article)
            // sementara pake news
            R.id.menu_tips -> navigateToFragment(R.id.navigation_news)
            R.id.menu_informasi -> navigateToFragment(R.id.navigation_skin_information)
            R.id.readMoreButton -> navigateToFragment(R.id.navigation_news)
        }
    }

    private fun navigateToFragment(fragmentId: Int) {
        findNavController().navigate(fragmentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onInformationClick(position: Int) {
        findNavController().navigate(R.id.navigation_detail_skin_information)
    }
}
