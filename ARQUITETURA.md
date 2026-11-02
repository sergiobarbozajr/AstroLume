# Arquitetura

Kotlin + Jetpack Compose, sem fragmentos e sem navegação por rotas. A tela da TV é **uma**
`Activity` e **uma** máquina de estados; o controle remoto manda ações de texto (`"up"`,
`"down"`, `"ok"`, `"back"`, `"home"`, `"fav"`) para um só despachante.

```
MainActivity (TV)  ──> act(a)  ──> teclado? ──> tecAct   (modal: atende primeiro)
                                 └> por zona ──> playerAct / contentAct / settingsAct / ...
Mobile.kt (celular) ──> a mesma camada de dados, interface de toque
```

## Zonas

`Z.INTRO`, `Z.NAV`, `Z.CONTENT`, `Z.PLAYER`, `Z.TEST`, `Z.IPTV`, `Z.EXIT`, `Z.KEYMAP`,
`Z.READER`, `Z.EPISODES`, `Z.SOUND`. A zona decide quem recebe a tecla. O teclado único é a
exceção: enquanto está aberto, ele atende antes da zona, em qualquer tela.

## Arquivos

| Arquivo | Linhas | Papel |
| --- | ---: | --- |
| `MainActivity.kt` | 3618 | tela da TV, estado, despacho de teclas, telas e painéis |
| `Fx.kt` | 1094 | fundo, estrelas, logo, ícones e desenhos (`drawWithCache`, `drawPoints`) |
| `Mobile.kt` | 839 | tela de toque do celular |
| `Playback.kt` | 655 | ExoPlayer: qualidade adaptativa, tentativas, buffer, saúde do canal |
| `Open.kt` | 532 | fontes abertas: iptv-org, transmissão de rede, leitor de serviço, sua playlist |
| `Iptv.kt` | 521 | assinatura do provedor: catálogo, seções, normalização de nomes |
| `Sound.kt` | 344 | tema musical, sons de tecla, mixagem |
| `Data.kt` | 344 | `Card`, `Section`, `SECTIONS`, `SETTING_ITEMS`, `Store` (preferências) |
| `Web.kt` | 336 | navegador na própria TV, cursor por setas, processo `:web`, detector de fonte |
| `Intro.kt` | 328 | abertura: meteoro, fragmentos, palavras |
| `Helpers.kt` | 254 | `u()`, `ts()`, utilidades de layout em escala |
| `Img.kt` | 232 | capas e ícones, cache medido em memória (40 MB) |
| `News.kt` | 230 | notícias por RSS aberto e cotações |
| `SoundAi.kt` / `ImageAi.kt` | 199 / 199 | realce de som por cena e de imagem (480p→1080p) |
| `Update.kt` | 154 | confere e instala a versão nova |
| `Net.kt` | 149 | rede em thread separada com retorno na principal |
| `Logos.kt` | 118 | logo dos canais abertos pelo nome |
| `Model.kt` | 116 | modelos do catálogo |
| `Summary.kt` | 111 | resumos |
| `Antena.kt` | 83 | só o sintonizador da TV, via `TvInputInfo.TYPE_TUNER` |
| `AudioFx.kt` | 62 | equalizador |
| `Sinopse.kt` | 63 | sinopse buscada ao provedor, só quando o título aparece no destaque |
| `Wiki.kt` | 56 | capa pela Wikipédia quando a lista não traz imagem |
| `ThemePal.kt` | 34 | paleta tirada da imagem de fundo |

`MainActivity.kt` com 3618 linhas é o ponto fraco reconhecido do projeto: é dele que vêm os
erros de ordem de função local, e dividi-lo está no [ROADMAP.md](ROADMAP.md).

## Onde ficam os dados

Tudo no aparelho, nada em servidor:

- `Store` (`SharedPreferences`) — contas de IPTV (3 por aba), favoritos, histórico, tema,
  qualidade, som, proxy, senha do adulto, posição de retomada por título.
- `cacheDir` — catálogo do provedor, lista do iptv-org (24 h), capas. Arquivos com mais de
  7 dias são apagados sozinhos, e as capas são as primeiras coisas largadas quando o Android
  avisa que a memória acabou (`onTrimMemory` → `Img.limpar()`).

## Regras de desempenho que o código segue

Vieram de medição no aparelho, não de palpite:

- **Nada de animação infinita em tela de repouso.** Uma única `rememberInfiniteTransition`
  deixou o launcher 100 % travado. O brilho hoje passa duas vezes e para.
- **Camada de tela cheia custa caro.** Cinco camadas repintadas 30×/s davam 150 ms por quadro
  e 59 °C. O véu virou `ColorFilter.tint` dentro da própria imagem.
- **O destaque só troca quando o foco para** (`delay(280)`). Trocar a cada tecla era o maior
  custo da navegação.
- **O APK publicado é o de produção.** O de depuração desliga as otimizações do ART e sozinho
  dobrava o tempo de quadro (48 ms → 24 ms ao trocar).
- **Cache de capas medido em bytes**, não em quantidade: 400 imagens sem olhar tamanho enchiam
  a memória e a TV ficava recarregando textura.

## Como o teste prova isso

`smoke.yml` instala o APK de depuração (só ele aceita o gancho de teste), roda os testes de
funcionamento — som, pulo de 10 s, retomada, reprodução de 3 endereços, teclado — e depois
**reinstala o de produção** para medir fluidez, que é o que vai ser publicado.

O portão reprova quando **não consegue medir**, não só quando o número é ruim: foi assim que
apareceu que o teste de reprodução passava vazio em silêncio desde a 0.29.
