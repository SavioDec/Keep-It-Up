package com.example.aula01

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.aula01.databinding.BookDetailLayoutBinding

var bookDetail = BookDetail(
    name = "How to Train Your Dragon",
    synopsis = "The island of Berk is a Viking village that is plagued by attacks from dragons that steal livestock and endanger the villagers. Hiccup, the 15-year-old son of the village chieftain, Stoick the Vast, is deemed too weak to fight. Stoick had popped a dragon's head clean off its shoulders when he was a baby.\n" +
            "Berk is situated on an Island, very close to the Arctic circle, where it is always cold. The village had been in existence for 7 generations and relies on fishing and hunting for sustenance. Despite the frequent attack from dragons, the Vikings refuse to leave the island. Most of the buildings in the village are new as they are frequently destroyed in the dragon attacks.\"\n",
    totalPages = 200,
    author = "Cressida Cowell",
    publisher = "Hodder Children's Books",
    firstPublishedAt = "February 1, 2003",
    genres = listOf("Fantasy", "Adventure", "Epic")
)

class BookDetailActivity : ComponentActivity() {
    companion object {
        const val BOOK_ID_KEY = "bookId"
    }
    
    private lateinit var binding: BookDetailLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = BookDetailLayoutBinding.inflate(layoutInflater)
        
        val bookId = intent.getStringExtra(BOOK_ID_KEY)
        android.util.Log.d("MyTag", "Book ID: $bookId")

        setContentView(binding.root)

        binding.bookTitleTextView.text = bookDetail.name
        binding.synopsisTextView.text = bookDetail.synopsis
        binding.author.text = bookDetail.author
        binding.publisher.text = bookDetail.publisher
        binding.firstPublished.text = bookDetail.firstPublishedAt
        binding.genresTextView.text = bookDetail.genres.joinToString(", ")
        
        binding.fabPreviousPage.setOnClickListener {
            //   bookDetail.currentPage -= 1 // dá erro pois é imutavel;
            bookDetail.getPreviousPage()?.let {
                bookDetail = it
                updateReadingProgressViews()
            }
        }
        binding.fabNextPage.setOnClickListener {
            bookDetail.getNextPage()?.let { nextPageDetail -> // substitui a nomenclatura de it
                bookDetail = nextPageDetail
                updateReadingProgressViews()
            }
        }
        createBadgesViews(
            listOf(
                "Book",
                bookDetail.genres.first(),
                getString(R.string.pages, bookDetail.totalPages)
            )
        )
        updateReadingProgressViews()
    }

    private fun createBadgesViews(badges: List<String>) {
        val maxBadgesPerRow = 3
        for (badge in badges.take(maxBadgesPerRow)) {
            val badgeLayout = LayoutInflater.from(this).inflate(R.layout.badge_layout, null, false)
            badgeLayout.findViewById<TextView>(R.id.badge_text_view).text = badge
            binding.bookBadgeRow1.addView(badgeLayout)
        }

        for (badge in badges.drop(maxBadgesPerRow).take(maxBadgesPerRow)) {
            val badgeLayout = LayoutInflater.from(this).inflate(R.layout.badge_layout, null, false)
            badgeLayout.findViewById<TextView>(R.id.badge_text_view).text = badge
            binding.bookBadgeRow2.addView(badgeLayout)
        }
    }

    private fun updateReadingProgressViews() {
        binding.pageCount.text =
            getString(R.string.of, bookDetail.currentPage, bookDetail.totalPages)
        binding.readingProgressPercentage.text = "${bookDetail.progress}%"
        binding.linearProgressReadingProgress.progress = bookDetail.progress.toInt()
    }
}

data class BookDetail(
    val name: String,
    val synopsis: String,
    val currentPage: Int = 0,
    val totalPages: Int,
    val author: String,
    val publisher: String,
    val firstPublishedAt: String,
    val genres: List<String>
) {
    init { //Constructor
        require(totalPages > 0) { "Total pages must be greater than zero" } // similar ao catch

        // currentPage > 0 && currentPages <= totalPage
        require(currentPage in currentPageValidRange) { "Current page must be greater than zero and less than or equal to total pages" }
    }

    private val currentPageValidRange: IntRange
        get() = 0..totalPages

    val progress: Float
        get() = (currentPage.toFloat() / totalPages) * 100

    fun getPreviousPage(): BookDetail? {
        if (currentPage > 0) {
            return copy(currentPage = currentPage - 1)
        }
        return null
    }

    fun getNextPage(): BookDetail? {
        val next = currentPage + 1
        return if (next in currentPageValidRange) copy(currentPage = next) else null
    }
}
