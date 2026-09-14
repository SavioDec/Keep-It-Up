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

// Simula um banco de dados local com 100 itens para testar o funcionamento da RecyclerView.
private val homeList = List(100) {
    if (it == 0) {
        // O primeiro item da lista sempre será o livro principal
        return@List HomeListItem(
            it.toString(),
            "How to Train Your Dragon",
            200
        )
    }
    // Os demais itens são gerados de forma genérica
    HomeListItem(it.toString(), "Title $it", 100)
}

// Activity principal do aplicativo, serve como porta de entrada e exibe a lista de itens.
class HomeActivity : AppCompatActivity() {
    // Declaração do binding para acessar os elementos do layout activity_home.xml de forma segura e sem findViewById
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ativa o layout que se estende por trás das barras de sistema (status bar e navigation bar) para dar um ar moderno (Edge-to-edge)
        enableEdgeToEdge()
        
        // Infla (constrói na tela) a view definida no XML
        binding = ActivityHomeBinding.inflate(layoutInflater)

        // Define a raiz inflada como o conteúdo visual (content view) desta Activity
        setContentView(binding.root)
        
        // Conecta o Adapter que criamos com o RecyclerView, enviando os nossos dados (homeList) para ele
        binding.listView.adapter = HomeListViewAdapter(homeList)
        
        // Configura o comportamento de layout da RecyclerView, definindo que ela será uma lista linear e vertical (padrão)
        binding.listView.layoutManager = LinearLayoutManager(this)
    }
}

// Adapter responsável por pegar os dados da lista e "colar" eles nas visualizações (views) na tela.
class HomeListViewAdapter(val list: List<HomeListItem>) :
    RecyclerView.Adapter<HomeListViewAdapter.ViewHolder>() {
    
    // ViewHolder: funciona como um invólucro ou contêiner que guarda a referência visual de um item único da lista
    class ViewHolder(val binding: HomeListItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Pega o Context atual (necessário para iniciar intents ou pegar recursos)
        val context: Context = binding.root.context
    }

    // Chamado quando a RecyclerView precisa de um novo "cartão visual" para mostrar, mas não tem nenhum sobrando para reciclar.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Infla o layout visual de um item da lista
        val listItemBinding =
            HomeListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listItemBinding)
    }

    // Chamado sempre que um "cartão" vai aparecer na tela. É aqui que inserimos de fato o conteúdo dinâmico (título, páginas, etc)
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = list[position]

        // Define os textos a partir do objeto instanciado na memória
        holder.binding.title.text = item.title
        holder.binding.itemBadge.badgeTextView.text = "Book"
        holder.binding.pageCount.text = "${item.pagesCount}p"
        
        // Configura a ação de clique do item inteiro.
        holder.binding.root.setOnClickListener {
            // Cria a "intenção" de sair dessa tela e ir para a tela BookDetailActivity
            val intent = Intent(holder.context, BookDetailActivity::class.java)
            // Embala a variável da ID desse livro específico (como se fosse uma bagagem extra) para enviar para a outra tela
            intent.putExtra(BookDetailActivity.BOOK_ID_KEY, item.id)
            // Dá o comando de partida
            holder.context.startActivity(intent)
        }
    }

    // Retorna a quantidade de itens que devem ser exibidos
    override fun getItemCount(): Int = list.size
}

// Representa a estrutura de dados de cada linha na lista principal
data class HomeListItem(
    val id: String, // Identificador único do livro
    val title: String, // Título do livro
    val pagesCount: Int // Total de páginas
)