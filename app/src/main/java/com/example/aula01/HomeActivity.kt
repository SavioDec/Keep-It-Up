package com.example.aula01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aula01.databinding.ActivityHomeBinding
import com.example.aula01.databinding.HomeListItemLayoutBinding

private val homeList = List(100) {
    if (it == 0) {
        return@List HomeListItem(
            it.toString(),
            "How to Train Your Dragon",
            200
        )
    }
    HomeListItem(it.toString(), "Title $it", 100)
}

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.listView.adapter = HomeListViewAdapter(homeList)
        binding.listView.layoutManager = LinearLayoutManager(this)
    }
}

class HomeListViewAdapter(val list: List<HomeListItem>) :
    RecyclerView.Adapter<HomeListViewAdapter.ViewHolder>() {
    class ViewHolder(val binding: HomeListItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val listItemBinding =
            HomeListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = list[position]

        holder.binding.title.text = item.title
        holder.binding.itemBadge.badgeTextView.text = "Book"
        holder.binding.pageCount.text = "${item.pagesCount}p"
        
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.context, BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.BOOK_ID_KEY, item.id)
            holder.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size
}

data class HomeListItem(
    val id: String,
    val title: String,
    val pagesCount: Int
)