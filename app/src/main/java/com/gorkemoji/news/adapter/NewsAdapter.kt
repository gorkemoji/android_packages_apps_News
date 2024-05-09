import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gorkemoji.news.data.Articles
import com.gorkemoji.news.data.Source
import com.gorkemoji.news.databinding.NewsLayoutBinding

class NewsAdapter(private val articles: List<Articles>, private val sources: List<Source>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsLayoutBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        val source = sources[position]
        holder.bind(article, source)
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(private val binding: NewsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Articles, source: Source) {
            binding.title.text = article.title
            binding.desc.text = article.desc
            binding.date.text = article.date
            binding.source.text = source.sourceName

            Glide.with(binding.root.context)
                .load(article.urlOfImage)
                .into(binding.imageView)
        }
    }
}