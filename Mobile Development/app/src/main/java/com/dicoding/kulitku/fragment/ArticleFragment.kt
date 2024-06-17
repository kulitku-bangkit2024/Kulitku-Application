package com.dicoding.kulitku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kulitku.R
import com.dicoding.kulitku.adapter.ArticleAdapter
import com.dicoding.kulitku.adapter.OnArticleClickListener
import com.dicoding.kulitku.data.articleItems
import com.dicoding.kulitku.databinding.FragmentArticleBinding

class ArticleFragment : Fragment(), OnArticleClickListener {

    private var _binding: FragmentArticleBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rvArticle = binding.rvArticle
        val layoutManager = GridLayoutManager(context, 2)
        rvArticle.layoutManager = layoutManager
        rvArticle.adapter = ArticleAdapter(articleItems, this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArcticleClick(position: Int) {
        findNavController().navigate(R.id.navigation_detail_article)
    }
}