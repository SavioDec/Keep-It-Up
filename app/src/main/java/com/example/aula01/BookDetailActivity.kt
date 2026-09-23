package com.example.aula01

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.aula01.databinding.BookDetailLayoutBinding

// Variavel global provisória que guarda os dados detalhados de um livro. 
// Num projeto real isso viria de um Banco de Dados ou de uma API.
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

// Activity que gerencia a tela de Detalhes do Livro
class BookDetailActivity : AppCompatActivity() {
    // companion object funciona como um bloco para variáveis/métodos estáticos (disponíveis mesmo sem instanciar a classe)
    companion object {
        // Chave constante usada para acessar o ID do livro passado no Intent
        const val BOOK_ID_KEY = "bookId"
    }
    
    // Variável para acessar diretamente os elementos visuais (TextViews, Buttons) usando o ViewBinding
    private lateinit var binding: BookDetailLayoutBinding

    // Função executada assim que a tela (Activity) for criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Estende o conteúdo até as bordas (atrás da barra de status)
        enableEdgeToEdge()

        // Infla (converte XML para objetos da memória) o layout book_detail_layout.xml
        binding = BookDetailLayoutBinding.inflate(layoutInflater)
        
        // Tenta capturar o ID enviado da HomeActivity via Intent (na bagagem extra)
        val bookId = intent.getStringExtra(BOOK_ID_KEY)
        // Log que exibe a informação no painel do programador (Logcat) para debugar
        android.util.Log.d("MyTag", "Book ID: $bookId")

        // Define a visualização raiz inflada na tela atual
        setContentView(binding.root)

        setSupportActionBar(binding.bookDetailToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)



        // Preenche os campos textuais buscando as propriedades do objeto bookDetail
        binding.bookTitleTextView.text = bookDetail.name
        binding.synopsisTextView.text = bookDetail.synopsis
        binding.author.text = bookDetail.author
        binding.publisher.text = bookDetail.publisher
        binding.firstPublished.text = bookDetail.firstPublishedAt
        
        // Junta a lista de gêneros usando a vírgula como separador
        binding.genresTextView.text = bookDetail.genres.joinToString(", ")
        
        // Escuta os cliques no botão flutuante de Voltar página
        binding.fabPreviousPage.setOnClickListener {
            //   bookDetail.currentPage -= 1 // dá erro pois é imutavel;
            
            // Pede uma nova instância do BookDetail com 1 página a menos
            bookDetail.getPreviousPage()?.let {
                // Atualiza a variável caso o retorno não seja nulo (ou seja, caso o número de páginas seja maior que 0)
                bookDetail = it
                // Atualiza os componentes visuais com as novas estatísticas de progresso
                updateReadingProgressViews()
            }
        }
        
        // Escuta os cliques no botão flutuante de Avançar página
        binding.fabNextPage.setOnClickListener {
            bookDetail.getNextPage()?.let { nextPageDetail -> // substitui a nomenclatura genérica "it" por nextPageDetail
                bookDetail = nextPageDetail
                updateReadingProgressViews()
            }
        }
        
        // Chama a função que constrói os pequenos "emblemas/etiquetas" dinamicamente
        createBadgesViews(
            listOf(
                "Book", // Badge 1
                bookDetail.genres.first(), // Badge 2 (Primeiro genêro da lista)
                getString(R.string.pages, bookDetail.totalPages) // Badge 3 (ex: "200 pages")
            )
        )
        
        // Define o estado inicial da barra de progresso antes do usuário clicar nos botões
        updateReadingProgressViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    // Função que cria pílulas informativas (emblemas/badges) e as insere nas linhas LinearLayout do XML
    private fun createBadgesViews(badges: List<String>) {
        val maxBadgesPerRow = 3
        
        // Pega os 3 primeiros itens e os insere dinamicamente na Row 1 (Linha 1)
        for (badge in badges.take(maxBadgesPerRow)) {
            val badgeLayout = LayoutInflater.from(this).inflate(R.layout.badge_layout, null, false)
            badgeLayout.findViewById<TextView>(R.id.badge_text_view).text = badge
            binding.bookBadgeRow1.addView(badgeLayout)
        }

        // Pula os primeiros 3 itens (drop) e pega os próximos 3, se houverem, para a Row 2
        for (badge in badges.drop(maxBadgesPerRow).take(maxBadgesPerRow)) {
            val badgeLayout = LayoutInflater.from(this).inflate(R.layout.badge_layout, null, false)
            badgeLayout.findViewById<TextView>(R.id.badge_text_view).text = badge
            binding.bookBadgeRow2.addView(badgeLayout)
        }
    }

    // Função que recalcula e atualiza as views relativas ao progresso de leitura do usuário
    private fun updateReadingProgressViews() {
        // Atualiza a frase tipo "12 of 200" buscando o recurso de string de formatação em strings.xml
        binding.pageCount.text =
            getString(R.string.of, bookDetail.currentPage, bookDetail.totalPages)
            
        // Atualiza o percentual escrito na tela
        binding.readingProgressPercentage.text = "${bookDetail.progress}%"
        
        // Atualiza a barra horizontal de progresso em si
        binding.linearProgressReadingProgress.progress = bookDetail.progress.toInt()
    }
}

// Data Class (Classe focada somente em segurar dados estruturados) representando um Livro
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
    init { //Constructor, executado sempre que um novo BookDetail é instanciado
        require(totalPages > 0) { "Total pages must be greater than zero" } // require valida a expressão (similar ao throw exception / catch)

        // currentPage > 0 && currentPages <= totalPage
        require(currentPage in currentPageValidRange) { "Current page must be greater than zero and less than or equal to total pages" }
    }

    // Propriedade gerada automaticamente calculando o intervalo permitido
    private val currentPageValidRange: IntRange
        get() = 0..totalPages

    // Propriedade que devolve automaticamente a conversão matemática da porcentagem
    val progress: Float
        get() = (currentPage.toFloat() / totalPages) * 100

    // Função que subtrai 1 página, retornando uma CÓPIA instanciada do Livro pois os campos val são imutáveis
    fun getPreviousPage(): BookDetail? {
        if (currentPage > 0) {
            // copy cria um clone e deixa alterar atributos em específico no clone
            return copy(currentPage = currentPage - 1)
        }
        return null
    }

    // Função que avança 1 página, também retornando cópia para não violar a imutabilidade
    fun getNextPage(): BookDetail? {
        val next = currentPage + 1
        return if (next in currentPageValidRange) copy(currentPage = next) else null
    }
}
