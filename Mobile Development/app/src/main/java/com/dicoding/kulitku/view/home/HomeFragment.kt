package com.dicoding.kulitku.view.home

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
import com.dicoding.kulitku.adapter.ArticleAdapter
import com.dicoding.kulitku.adapter.OnArticleClickListener
import com.dicoding.kulitku.databinding.FragmentHomeBinding
import com.dicoding.kulitku.view.MainViewModelFactory
import com.dicoding.kulitku.view.UserModelFactory
import com.dicoding.kulitku.view.UserPreferences
import com.dicoding.kulitku.view.article.ArticleViewModel
import com.dicoding.kulitku.view.home.banner.BannerFragment
import com.dicoding.kulitku.view.home.banner.BannerSlider
import com.dicoding.kulitku.view.home.banner.SliderIndicator
import com.dicoding.kulitku.view.home.banner.SliderPagerAdapter
import com.dicoding.kulitku.view.login.dataStore
import com.dicoding.kulitku.view.quiz.QuizMainActivity

class HomeFragment : Fragment(), View.OnClickListener, OnArticleClickListener {

    // banner
    private lateinit var mAdapter: SliderPagerAdapter
    private lateinit var mIndicator: SliderIndicator

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var articleAdapter: ArticleAdapter
    private val articleViewModel: ArticleViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[ArticleViewModel::class.java]
    }

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

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticle.layoutManager = layoutManager

        articleAdapter = ArticleAdapter(this)
        binding.rvArticle.adapter = articleAdapter

        articleViewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
        }

        binding.menuArticle.setOnClickListener(this)
        binding.menuTips.setOnClickListener(this)
        binding.menuQuiz.setOnClickListener(this)

        binding.readMoreButton.setOnClickListener(this)
        binding.seeAllButton.setOnClickListener(this)

        binding.menuQuiz.setOnClickListener {
            val intent = Intent(context, QuizMainActivity::class.java)
            startActivity(intent)
        }

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
            R.id.menu_article -> navigateToFragment(R.id.navigation_article)
            R.id.menu_tips -> navigateToFragment(R.id.navigation_tips)
            R.id.readMoreButton -> navigateToFragment(R.id.navigation_tips)
            R.id.seeAllButton -> navigateToFragment(R.id.navigation_article)
        }
    }

    private fun navigateToFragment(fragmentId: Int) {
        findNavController().navigate(fragmentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArticleClick(position: Int) {
        findNavController().navigate(R.id.navigation_detail_article)
    }
}
