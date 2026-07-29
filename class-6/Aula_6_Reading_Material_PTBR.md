# Aula 6: Segurança no Quarkus com Autenticação Baseada em Tokens

Bem-vindos à nossa sexta aula! Até o momento, a nossa API está completamente aberta: qualquer pessoa com acesso à rede pode criar clientes, buscar produtos e, em breve, fazer pedidos. No mundo real, precisamos controlar quem acessa o quê. Hoje, vamos introduzir a camada de segurança na nossa aplicação.

---

## Parte 1: Segurança e Tokens (O "Ingresso" Digital)

No desenvolvimento de APIs REST modernas, a abordagem mais comum para segurança é a **Autenticação Baseada em Tokens** (frequentemente usando o padrão JWT - *JSON Web Token*).

### Como funciona?
Imagine que o Token é como uma pulseira VIP em um evento:
1.  **Login:** O usuário envia suas credenciais (ex: usuário e senha) para a API.
2.  **Geração do Token:** Se as credenciais estiverem corretas, o servidor não cria uma "sessão" na memória (o que quebraria o princípio *stateless* do REST). Em vez disso, ele gera um Token assinado digitalmente e o devolve ao usuário.
3.  **Acesso aos Endpoints:** Nas próximas requisições, o usuário anexa essa "pulseira" (o Token) no cabeçalho (*Header*) da requisição HTTP (geralmente no formato `Authorization: Bearer <token>`).
4.  **Validação:** O servidor apenas verifica a assinatura digital do Token. Se for válido, a requisição é permitida.

Isso é extremamente rápido, seguro e escala perfeitamente em arquiteturas de microsserviços.

---

## Parte 2: Implementando a Segurança no Quarkus

### Adicionando Dependências
O Quarkus possui um ecossistema de segurança muito robusto. Para a nossa prática, precisaremos adicionar extensões focadas em segurança, como o suporte a JWT (`quarkus-smallrye-jwt`) ou extensões de segurança integradas, dependendo da abordagem de geração de tokens que utilizaremos em sala. Podemos adicionar isso rapidamente via terminal.

### Protegendo os Endpoints
Com a segurança ativada, a nossa API passa a bloquear acessos não autorizados. O Quarkus nos permite controlar o acesso de forma muito granular (por classe ou por método) usando anotações simples e diretas:
*   `@PermitAll`: Deixa o endpoint público (ex: a rota de Login, onde o usuário ainda não tem um token).
*   `@Authenticated`: Garante que apenas usuários com um token válido possam acessar o recurso, independentemente de quem sejam.
*   `@RolesAllowed("admin")`: O nível mais restrito. Além de ter um token válido, o usuário precisa ter o "papel" (Role) específico exigido (ex: apenas administradores podem deletar produtos).

### Implementando a Autenticação
Na nossa prática em sala, vamos construir o fluxo de entrada. Criaremos um endpoint dedicado para receber as credenciais, validá-las contra dados seguros e emitir o Token que será usado nas requisições subsequentes para os nossos recursos de Cliente e Produto.

---

## Parte 3: Desafio Prático dos Alunos

Depois de configurarmos juntos a estrutura base de segurança e protegermos nossos endpoints principais, a missão passará para vocês.

O desafio prático será:
1.  Garantir que as dependências de segurança estejam corretamente instaladas e configuradas no projeto de vocês.
2.  Aplicar as anotações de proteção (`@Authenticated`, `@RolesAllowed`, etc.) nos endpoints de **Produto** que vocês criaram nas aulas anteriores.
3.  Testar o fluxo completo: tentar acessar os produtos sem o token (devendo receber um Erro 401 - Unauthorized) e, em seguida, gerar o token e realizar o acesso com sucesso.

Boa leitura e vamos blindar nossa API!
