# Histórico de versões

Feito a partir dos commits do `main`. Versão = `versionName` do `app/build.gradle.kts`.

## 0.45 — em teste

- **Modo seta.** Em vez de o foco pular de item em item, aparece uma seta na tela que as teclas
  do controle movem livremente, e o OK clica no que estiver embaixo dela. Vale no menu, no
  conteúdo, nas configurações e nos dois teclados.
- O botão que liga e desliga é **ensinado por você**, no terceiro passo de *Mapear controle
  remoto*. Segurar VOLTAR desliga sempre, como saída de emergência.
- Como funciona por dentro: a seta não manda toque nenhum na tela. Ela só diz qual item está
  embaixo dela, e o app move o foco para lá — o OK continua sendo o mesmo OK, pelo mesmo
  caminho. Nada do que já funcionava trocou de rota.
- **Fora do AstroLume não vale**, e isso é limite do Android, não escolha de implementação: um
  app comum não pode tocar na tela de outro app. Só um Serviço de Acessibilidade poderia, e foi
  decidido não pedir essa permissão.

## 0.44

- **Piscada na reprodução, segunda causa.** A decisão "esta TV não aguenta o reforço de imagem na
  GPU" valia só até o fim do título. Cada filme novo tentava o reforço outra vez, perdia quadros
  outra vez e trocava o player outra vez — e trocar o player apaga a tela. Agora a decisão fica
  guardada no aparelho: uma vez que a TV mostrou que não aguenta, não se insiste. Mexer na opção
  do Assistente IA no menu limpa a marca, porque aí foi pedido.

## 0.43

- **Piscada na reprodução, primeira causa.** Numa lista adaptativa a TV troca de faixa de
  qualidade várias vezes por minuto, e o app recalculava o plano de imagem a cada troca; sair de
  "reforço para 1080p" para "direto" refaz o player inteiro. Agora o plano é escolhido uma vez
  por título.
- Refazer o player por perda de quadros ficou limitado a uma vez a cada 20 s.
- A tela de SEM SINAL, que é preta e cobre tudo, só aparece se o problema durar mais de 1,5 s:
  falha de meio segundo que o player resolve sozinho também aparecia como piscada.
- Portão novo: refazer o player deixa marca no log, e o teste reprova se acontecer mais de uma
  vez durante a reprodução. Foi ele que mostrou que a 0.43 ainda piscava.

## 0.42

- **Um teclado só no app inteiro.** Endereço, usuário, senha, proxy e senha do adulto deixaram
  de abrir a caixa de texto do Android e passaram a usar as mesmas teclas da busca. Abre ao
  confirmar o campo, fecha no `PRONTO` devolvendo o texto; `CANCELAR` ou Voltar desistem.
- Teclas novas: `APAGAR TUDO`, `abc/ABC` e a linha de símbolos `: / . - _ @ ? = & + % #`,
  que endereço de IPTV exige e antes só existia no teclado do sistema.
- Senha aparece como `••••` enquanto é digitada.
- O teste no emulador agora reprova se o teclado não abrir, não aceitar a tecla ou não devolver
  o texto certo.

## 0.41

- **Espera 30 s antes da nova tentativa de reprodução** (era 1 s). O servidor de IPTV costuma
  demorar para liberar o canal, e reconectar cedo fazia ele começar tudo de novo — era isso
  que parecia travamento.
- Sinopse do filme ou série no destaque, buscada ao provedor só quando o título aparece.
- IPTV em **duas abas**: conta com usuário e senha, e um endereço só para todos os outros
  formatos. Bloco de apagar igual nas duas, sempre embaixo.
- `tools/ordem.py`: pega antes de compilar o uso de função local antes da declaração.

## 0.40

- Detector de fonte: abre o site uma vez e mostra de qual endereço ele busca os dados.
- Proxy nas conexões de IPTV e de vídeo.
- Abas distribuídas dentro do painel, sem nenhuma encostando na borda.

## 0.39

- O gancho de teste só existia na versão de depuração — por isso os testes de som, pulo,
  retomada e reprodução passavam **vazios em silêncio desde a 0.29**. Agora a esteira compila
  as duas: funcionamento na de depuração, fluidez na de produção que é a publicada.

## 0.38

- Teclado da busca com `Apagar tudo`.
- **Lançamentos** só do ano corrente; mídias em ordem alfabética.
- Sua lista de canais (287) embutida no app.
- Excluir todas as contas de cada tipo.
- A esteira passou a validar a sua playlist.

## 0.37

- Guarda o trecho já visto para voltar sem rebaixar a imagem; limite de 1080p (o painel é 1080p).
- Reduz a qualidade sozinho após 3 engasgos no minuto, avisando na tela.
- Capas decodificadas no tamanho do cartão; limpeza automática do que envelhece na pasta
  temporária; capas são a primeira coisa largada quando a TV fica sem memória.
- Seções **Lançamentos**, **Infantil** e **Vistos recentemente**, com sugestões pelo histórico.
- Ano, gênero e data de entrada lidos do provedor; busca com filtro de tipo, ano e ordenação.
- **Antena**: lê só o sintonizador da TV em vez de todas as fontes — era por isso que abria o
  Pluto TV sem volta — e a fileira só aparece com canais sintonizados de verdade.
- Gestão de contas: limpar cache do catálogo, remover conta e apagar credenciais dos 3 acessos,
  cada ação destrutiva com confirmação.
- Área útil maior; aba **Adulto** bloqueada por senha, que volta a travar ao reabrir o app.
- Leitor de serviço aberto passou a entender catálogo em JSON além de M3U.

## 0.33 – 0.36

- IPTV com três abas e três acessos cada; TV online saiu do menu principal.
- Barra de abas só mostra item inteiro; logo nova no ícone e no banner.
- Fonte do conteúdo mostrada de forma discreta por 5 s ao iniciar.
- O que não reproduz some até a próxima abertura do app.
- **Passou a publicar o APK de produção.** O de depuração desliga as otimizações do Android:
  era por isso que a tela principal estava lenta (48 ms → 24 ms por quadro).

## 0.28 – 0.32

- Canais abertos do iptv-org nativos, guardados no aparelho e usados quando a outra fonte falha.
- Transmissão de rede estilo VLC: endereço direto, lista `.m3u` e `rtsp://`.
- O destaque só troca quando o foco para.
- Logo fina (Inter Light, sem relevo, gradiente da abertura).
- Fundo só anima em stand-by, após 12 s parado.
- Cache de capas medido em memória (40 MB) em vez de contar 400 imagens.
- **Nova abertura**: o meteoro cai, se parte e os cacos formam o nome, em fonte fina com
  gradiente azul fogo ao lilás.

## 0.26 – 0.27

- Fundo leve: véu dentro da própria imagem, sombra só nas faixas de cima e de baixo, brilho
  das estrelas sem degradê por quadro, uma pintura de tela cheia a menos.
- Portão de fluidez no emulador, comparando com as Configurações do Android em vez de um
  número fixo; portão de tela parada.
- Portão de fluidez também no celular.
- **AstroLume Home**: launcher da TV, em módulo separado (`home/`, pausado por escolha sua).
