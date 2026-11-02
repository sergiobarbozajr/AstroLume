package com.auroratv.tv

import androidx.compose.runtime.mutableStateMapOf
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * Sinopse dos filmes e séries.
 *
 * A lista do provedor vem só com nome e capa; o texto está num segundo pedido, um por título.
 * Então ele é buscado só quando o título fica em destaque na tela, uma vez, e guardado.
 */
object Sinopse {
    private val cache = mutableStateMapOf<String, String>()
    private val pedindo = HashSet<String>()

    /** O que mostrar agora. Dispara a busca na primeira vez e devolve vazio até o texto chegar. */
    fun de(c: Card): String {
        val pronta = c.desc.takeIf { it.length > 25 && !it.startsWith("Canal aberto") && !it.startsWith("Fonte:") }
        if (pronta != null) return pronta
        cache[c.name]?.let { return it }
        buscar(c)
        return ""
    }

    private fun buscar(c: Card) {
        synchronized(pedindo) {
            if (!pedindo.add(c.name)) return
        }
        thread(isDaemon = true) {
            val txt = try { doBuscar(c) } catch (e: Throwable) { "" }
            cache[c.name] = txt
        }
    }

    /** Pergunta ao provedor o texto do título, pelo mesmo endereço da conta. */
    private fun doBuscar(c: Card): String {
        val u = c.url ?: return ""
        // .../movie/USUARIO/SENHA/ID.ext  →  player_api.php?action=get_vod_info&vod_id=ID
        val m = Regex("^(https?://[^/]+)/(movie|series)/([^/]+)/([^/]+)/(\\d+)").find(u) ?: return ""
        val (base, tipo, user, pass, id) = m.destructured
        val acao = if (tipo == "movie") "get_vod_info&vod_id=$id" else "get_series_info&series_id=$id"
        val api = "$base/player_api.php?username=$user&password=$pass&action=$acao"
        val corpo = try {
            val px = Iptv.proxy()
            val cx = (if (px != null) URL(api).openConnection(px) else URL(api).openConnection()) as HttpURLConnection
            cx.connectTimeout = 9000
            cx.readTimeout = 12000
            if (cx.responseCode !in 200..299) return "" else cx.inputStream.bufferedReader().use { it.readText() }
        } catch (e: Throwable) {
            return ""
        }
        return try {
            val o = org.json.JSONObject(corpo)
            val info = o.optJSONObject("info") ?: o.optJSONObject("movie_data") ?: return ""
            val plot = info.optString("plot", "").ifBlank { info.optString("description", "") }
            plot.trim().take(400)
        } catch (e: Throwable) {
            ""
        }
    }
}
