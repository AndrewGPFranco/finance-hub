# Guia de Design - Finance

Este arquivo define o padrão visual e de experiência para telas Thymeleaf do projeto Finance.
Sempre que uma nova tela, componente ou fluxo visual for criado, siga estas diretrizes.

## Stack visual

- Usar Thymeleaf para renderização server-side.
- Preferir Bootstrap para grid, espaçamentos, formulários, botões, tabelas e componentes básicos.
- Usar CSS próprio apenas para identidade visual, ajustes de layout e componentes específicos.
- Usar HTMX quando precisar de interações parciais sem recarregar a página inteira.
- Usar Chart.js ou ApexCharts para gráficos financeiros.
- Usar ícones de uma biblioteca consistente, preferencialmente Lucide ou Bootstrap Icons.

## Personalidade visual

O sistema deve parecer uma ferramenta financeira de uso diário para casal:

- Moderno, limpo, organizado e confiável.
- Visual simples, com aparência de produto financeiro sério.
- Interface agradável para uso frequente, sem excesso de decoração.
- Priorizar clareza, alinhamento, espaçamento e hierarquia visual.
- Denso o suficiente para análise de dados, mas sem poluição visual.
- Usar cores com muita contenção. A tela não deve parecer colorida.
- Destaques devem aparecer apenas em ações principais, alertas e status.
- Evitar telas com cara de app genérico ou template administrativo pesado.
- Evitar gradientes fortes, sombras exageradas, fundos chamativos e elementos decorativos.

## Layout base

- Páginas autenticadas devem ter:
  - topo fixo ou estável com nome do sistema, usuário e sair;
  - navegação principal visível;
  - conteúdo em largura máxima entre `1120px` e `1280px`;
  - fundo geral claro;
  - áreas de conteúdo bem separadas por espaçamento, não por excesso de bordas.

- Evitar cards aninhados.
- Usar cards apenas para:
  - resumo financeiro;
  - formulário principal;
  - itens repetidos;
  - blocos de dashboard.

## Cores

O visual deve ser majoritariamente neutro. A base deve usar branco, cinzas frios e texto escuro.
Verde, vermelho, amarelo ou azul só devem aparecer como sinal funcional discreto.

Paleta base recomendada:

- Fundo da página: `#f6f7f9`
- Fundo alternativo: `#eef1f4`
- Superfícies/cards: `#ffffff`
- Superfície sutil: `#f9fafb`
- Texto principal: `#111827`
- Texto secundário: `#4b5563`
- Texto discreto: `#6b7280`
- Bordas: `#d8dde3`
- Bordas suaves: `#e5e7eb`
- Ação primária: `#111827`
- Ação primária hover: `#000000`
- Ação secundária: `#ffffff`
- Ação secundária texto: `#111827`
- Foco/acento discreto: `#334155`

Cores funcionais, usadas com moderação:

- Sucesso/pago: `#166534`
- Atenção/agendado: `#92400e`
- Erro/atrasado: `#991b1b`
- Informação/pendente: `#1f2937`

Regras:

- Não usar uma tela dominada por verde, azul, roxo ou qualquer cor viva.
- Não usar gradientes como base visual.
- Não usar badges muito saturados. Preferir fundo claro e texto escuro.
- A cor principal da aplicação deve ser neutra, não colorida.
- Status podem ter cor, mas devem ocupar pouca área visual.

## Tipografia

- Usar fonte do sistema ou `Inter`, se adicionada ao projeto.
- Títulos de página: `24px` a `32px`.
- Títulos de seção: `18px` a `22px`.
- Texto comum: `14px` a `16px`.
- Labels e metadados: `12px` a `14px`.
- Não usar fontes gigantes em telas operacionais.
- Não usar letter spacing negativo.

## Componentes

### Botões

- Primário: ações principais como salvar, cadastrar, confirmar pagamento.
- Secundário: ações neutras como voltar, filtrar, limpar.
- Perigo: excluir, cancelar operação irreversível.
- Botões devem ter texto curto e claro.
- Quando houver ícones, usar ícone + texto para ações importantes.

### Formulários

- Labels sempre visíveis.
- Campos obrigatórios devem ser claros pelo contexto ou marcador discreto.
- Erros devem aparecer abaixo do campo.
- Mensagens de erro devem estar em português do Brasil.
- Formulários longos devem ser divididos em seções.
- Valores monetários devem usar máscara visual no frontend quando possível.

### Tabelas

- Usar tabelas para listas financeiras densas.
- Cabeçalho fixo ou bem destacado quando a tabela for grande.
- Colunas monetárias alinhadas à direita.
- Datas em formato brasileiro: `dd/MM/yyyy`.
- Valores monetários em formato brasileiro: `R$ 1.234,56`.
- Ações de linha devem ficar no final.

### Status

Usar badges consistentes:

- Pago: texto verde escuro sobre fundo verde muito claro.
- Pendente: texto cinza escuro sobre fundo cinza claro.
- Atrasado: texto vermelho escuro sobre fundo vermelho muito claro.
- Agendado: texto âmbar escuro sobre fundo âmbar muito claro.
- Cancelado: texto cinza sobre fundo neutro.

Badges devem ser discretos. Evitar aparência chamativa ou colorida demais.

### Dashboard

Dashboard deve priorizar:

- saldo do mês;
- receitas;
- despesas;
- contas pendentes;
- faturas próximas;
- maiores categorias de gasto;
- evolução mensal.

Evitar gráficos decorativos. Todo gráfico deve responder a uma pergunta prática.

## Responsividade

- Mobile primeiro para formulários e listas simples.
- Dashboard pode reorganizar cards em uma coluna no mobile.
- Tabelas grandes devem ter scroll horizontal ou virar lista compacta.
- Botões não devem quebrar texto de forma feia.
- Nenhum texto deve sobrepor outro elemento.

## Textos

- Usar português do Brasil.
- Preferir frases diretas:
  - "Despesa cadastrada com sucesso."
  - "Email ou senha inválidos."
  - "Você não tem permissão para acessar este recurso."

- Evitar texto explicativo longo dentro da interface.
- Não colocar instruções óbvias na tela.

## Estrutura recomendada de templates

Quando possível, organizar assim:

```text
templates/
  layout/
    base.html
    fragments/
      header.html
      sidebar.html
      alerts.html
  auth/
    login.html
    register.html
  dashboard/
    index.html
  expenses/
    list.html
    form.html
```

## CSS recomendado

Quando o projeto crescer, criar:

```text
static/
  css/
    app.css
  js/
    app.js
```

Evitar CSS inline em novas telas, exceto protótipos rápidos. Templates existentes podem ser migrados gradualmente.

## Regra para novas telas

Antes de implementar uma tela:

1. Identificar se é tela operacional, formulário, listagem ou dashboard.
2. Reutilizar layout, cores, espaçamentos e componentes já existentes.
3. Evitar criar um estilo novo sem necessidade.
4. Garantir mensagens em português do Brasil.
5. Validar visualmente desktop e mobile quando a tela tiver complexidade.
