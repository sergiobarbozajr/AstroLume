package com.auroratv.tv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.math.abs

/**
 * Modo seta: uma seta na tela que as teclas do controle movem livremente, em vez de pular de
 * item em item.
 *
 * Como ela funciona é o detalhe que importa: a seta **não** manda toque nenhum para a tela.
 * Ela só diz qual item está debaixo dela, e o app move o foco para lá. O OK continua sendo o
 * mesmo OK de sempre, com o mesmo código por trás. Isso tem duas consequências boas: nada do
 * que já funciona muda de caminho, e não existe o caso de a seta clicar num lugar onde não há
 * nada para clicar.
 *
 * Cada item que aceita a seta se registra com [alvo], dizendo onde está na tela. Quando sai da
 * tela (uma fileira que rolou, por exemplo), ele se apaga do registro — senão a seta clicaria
 * num cartão que já não está mais ali.
 *
 * Fora do AstroLume isto não vale: o Android não deixa um app comum tocar na tela de outro app.
 * Para isso seria preciso um Serviço de Acessibilidade, habilitado à mão, e foi decidido não ir
 * por esse caminho.
 */
object Ponteiro {

    var ligado by mutableStateOf(false)
        private set

    /** Posição da seta, em pixels da janela. */
    var x by mutableStateOf(-1f)
        private set
    var y by mutableStateOf(-1f)
        private set

    /** Tamanho da janela, para a seta não sair da tela. */
    var largura = 0f
    var altura = 0f

    private val areas = LinkedHashMap<String, Rect>()

    /** Passo do movimento: começa curto para mirar e vai crescendo enquanto a tecla fica presa. */
    private var seguidos = 0
    private var ultimaDir = 0

    fun marcar(id: String, r: Rect) {
        synchronized(areas) { areas[id] = r }
    }

    fun apagar(id: String) {
        synchronized(areas) { areas.remove(id) }
    }

    fun ligar(v: Boolean) {
        ligado = v
        seguidos = 0
        ultimaDir = 0
        if (v && (x < 0f || y < 0f)) centralizar()
    }

    fun alternar(): Boolean {
        ligar(!ligado)
        return ligado
    }

    fun centralizar() {
        x = largura / 2f
        y = altura / 2f
    }

    /** Põe a seta em cima de um retângulo conhecido (ao ligar, cai no item que já estava em foco). */
    fun irPara(id: String) {
        val r = synchronized(areas) { areas[id] } ?: return
        x = r.center.x
        y = r.center.y
    }

    /**
     * Move a seta. O passo cresce de 18 a 90 px enquanto a tecla segue presa na mesma direção,
     * para dar tanto a mira curta quanto a travessia da tela sem cansar o dedo.
     */
    fun mover(dx: Int, dy: Int) {
        val dir = dx * 10 + dy
        if (dir == ultimaDir) seguidos = (seguidos + 1).coerceAtMost(14) else seguidos = 0
        ultimaDir = dir
        // o passo acompanha o tamanho da tela: 18 px num painel 1080p valem o mesmo que 36 num 4K
        val escala = (largura / 1920f).coerceIn(0.6f, 2.5f)
        val passo = (18f + seguidos * 5.2f).coerceAtMost(90f) * escala
        x = (x + dx * passo).coerceIn(0f, (largura - 1f).coerceAtLeast(0f))
        y = (y + dy * passo).coerceIn(0f, (altura - 1f).coerceAtLeast(0f))
    }

    /**
     * Qual item está debaixo da seta. Quando há vários (um cartão dentro de uma fileira dentro de
     * uma coluna), vale o menor: é o que a pessoa está mirando.
     */
    fun sob(): String? {
        var achado: String? = null
        var menor = Float.MAX_VALUE
        synchronized(areas) {
            for ((id, r) in areas) {
                if (x < r.left || x > r.right || y < r.top || y > r.bottom) continue
                val area = abs(r.width) * abs(r.height)
                if (area < menor) {
                    menor = area
                    achado = id
                }
            }
        }
        return achado
    }

    /** Esquece todos os alvos: usado ao trocar de tela, para não sobrar retângulo velho. */
    fun limpar() {
        synchronized(areas) { areas.clear() }
    }
}

/**
 * Marca este item como alcançável pela seta. O `id` diz ao app o que fazer quando a seta parar
 * em cima: "nav:3" é a terceira aba, "card:5:2" o terceiro cartão da fileira 5, e assim por diante.
 */
@Composable
fun Modifier.alvo(id: String): Modifier {
    DisposableEffect(id) {
        onDispose { Ponteiro.apagar(id) }
    }
    return this.onGloballyPositioned { Ponteiro.marcar(id, it.boundsInWindow()) }
}
