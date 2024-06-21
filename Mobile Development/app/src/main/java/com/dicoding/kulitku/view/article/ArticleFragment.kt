package com.dicoding.kulitku.view.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kulitku.R
import com.dicoding.kulitku.adapter.ArticleAdapter
import com.dicoding.kulitku.adapter.OnArticleClickListener
import com.dicoding.kulitku.databinding.FragmentArticleBinding
import com.dicoding.kulitku.view.MainViewModelFactory

class ArticleFragment : Fragment(), OnArticleClickListener {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleAdapter: ArticleAdapter

    private val articleViewModel: ArticleViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[ArticleViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context)
        binding.rvArticle.layoutManager = layoutManager

        articleAdapter = ArticleAdapter(this)
        binding.rvArticle.adapter = articleAdapter

        articleViewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        articleViewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            Log.d("ARTIKEL FRAGMENT", message)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArticleClick(position: Int) {
        val article = articleAdapter.currentList[position]
        val bundle = Bundle().apply {
            putString("arg_title", article.title ?: "")
            putString("arg_content", article.content ?: "")
            putString("arg_image_url", article.image_url)
        }
        findNavController().navigate(R.id.navigation_detail_article, bundle)
    }
}