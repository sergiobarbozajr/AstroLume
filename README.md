# AstroLume TV

Aplicativo de TV para Android TV / Google TV, feito e medido na **TCL A400M** (Android 14,
Realtek RTD6748, GPU PowerVR rtkg8300, 2,6 GB de RAM, painel 1080p).

Mesmo código, dois aparelhos:

| Variante  | Pacote                  | Tela                                   |
| --------- | ----------------------- | -------------------------------------- |
| `tv`      | `com.auroratv.tv`       | controle remoto, foco e setas          |
| `celular` | `com.astrolume.celular` | toque, abas embaixo, rolagem vertical  |

> **IPTV:** o app não traz nem distribui conteúdo. Ele toca o que **a sua própria conta
> contratada** entrega. As credenciais ficam só no aparelho, em `SharedPreferences`,
> e não são enviadas a nenhum servidor nosso — não existe servidor nosso.

## O que ele faz

**Conteúdo**
- Abas por categoria: Início, Pesquisar, Minhas Listas, Vistos recentemente, Streams, Filmes,
  Lançamentos, Séries, Novelas, Infantil, Documentários, Esporte, Notícias, TV ao vivo, Música,
  Adulto, Configurações.
- **Lançamentos** mostra só o ano corrente; filmes, séries, novelas, documentários e infantil
  saem em ordem alfabética.
- **Vistos recentemente** sugere pelo seu próprio histórico, guardado no aparelho.
- **Adulto** entra bloqueado por senha (padrão `0000`) e volta a travar a cada abertura do app.
- **Antena digital**: lê só o sintonizador da própria TV. Sem antena sintonizada, a fileira
  não aparece — era isso que antes abria o Pluto TV sem volta.
- **Canais abertos** do iptv-org, guardados no aparelho por 24 h, e a sua playlist própria
  (287 canais) embutida no app.

**Formas de entrar com IPTV** (3 acessos guardados em cada aba)
1. **Conta** — endereço, usuário e senha (padrão Xtream Codes).
2. **Endereço** — lista `.m3u`/`.m3u8`, catálogo em JSON, vídeo direto, `rtsp://` ou site para
   abrir no navegador da própria TV. Aceita proxy e tem um **detector de fonte**, que abre o
   site uma vez e mostra de qual endereço ele busca os dados.

**Reprodução**
- ExoPlayer (Media3) com HDR10 nativo, limite de 1080p (o painel é 1080p) e buffer de retorno,
  para voltar o que já foi visto sem rebaixar a imagem.
- Reduz a qualidade sozinho depois de 3 engasgos no minuto, e avisa na tela.
- **Espera 30 s antes da nova tentativa.** IPTV costuma demorar para liberar o canal;
  reconectar antes disso faz o servidor começar tudo de novo.
- O que não reproduz some da lista até a próxima abertura do app.

**Interface**
- Abertura própria: o meteoro cai, se parte e os cacos formam o nome.
- Um **teclado só** no app inteiro — busca e todos os campos, com `APAGAR TUDO`, microfone,
  maiúscula e a linha de símbolos que endereço de IPTV exige.
- O fundo só anima depois de 12 s parado (stand-by); em uso, não desenha sozinho.

## Instalar

Veja [INSTALACAO.md](INSTALACAO.md).

## Construir

Nada de Android Studio: a esteira do GitHub compila, testa e publica.

```
.github/workflows/smoke.yml     testa no emulador e barra a publicação
.github/workflows/build.yml     publica o APK (só depois que o teste passa)
.github/workflows/capturas.yml  tira as capturas dos temas
.github/workflows/home.yml      o launcher AstroLume Home (módulo home/)
```

Localmente, com o SDK do Android instalado:

```bash
./gradlew :app:assembleTvRelease
```

O APK publicado é **de produção** (`release`), nunca o de depuração: o APK de depuração desliga
as otimizações do Android e sozinho dobrava o tempo de quadro.

## Qualidade

Nada vai para o `main` sem passar pelo emulador. O teste reprova a publicação se:

- houver travamento (ANR), fechamento inesperado ou o app morrer;
- a mediana de tempo de quadro passar de 2,5× a das Configurações do Android medidas no mesmo
  emulador (a máquina do GitHub é compartilhada, então o limite acompanha uma referência,
  não um número fixo);
- o app desenhar sozinho durante o uso, dentro dos 12 s antes do stand-by;
- algum dos 3 endereços de vídeo não tocar;
- a playlist própria não for lida;
- o teclado não abrir no campo, não aceitar a tecla ou não devolver o texto ao fechar.

Antes de compilar, `tools/ordem.py` pega em segundos o uso de função local antes da declaração
— o erro que já custou quatro rodadas de emulador.

## Documentação

- [INSTALACAO.md](INSTALACAO.md) — instalar na TV e no celular
- [ARQUITETURA.md](ARQUITETURA.md) — como o código está dividido
- [CHANGELOG.md](CHANGELOG.md) — o que mudou em cada versão
- [ROADMAP.md](ROADMAP.md) — o que falta
- [CONTRIBUINDO.md](CONTRIBUINDO.md) — as regras do projeto
