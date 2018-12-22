package info.tommarsh.presentation.ui.article.top.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.tommarsh.core.extensions.ArticleClickListener
import info.tommarsh.core.extensions.inflate
import info.tommarsh.core.extensions.loadUrl
import info.tommarsh.presentation.R
import info.tommarsh.presentation.model.ArticleViewModel
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_article)) {

    fun bind(article: ArticleViewModel) = with(itemView) {
        article_image.loadUrl(article.urlToImage)
        article_title.text = article.title
        article_updated.text = article.publishedAt
        article_author.text = article.author

        setOnClickListener(ArticleClickListener(article.url))
    }
}