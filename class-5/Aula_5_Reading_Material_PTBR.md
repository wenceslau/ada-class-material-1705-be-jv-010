# Aula 5: Validação de Dados, Tratamento de Exceções e Padrão DTO

Bem-vindos à nossa quinta aula! Agora que nossa API já consegue salvar e buscar dados no banco, precisamos garantir que essas informações sejam consistentes e seguras. Hoje vamos focar em proteger nossa aplicação contra dados inválidos e em como responder de forma elegante quando algo dá errado.

---

## Parte 1: Garantindo a Qualidade da nossa API

### O Padrão DTO (Data Transfer Object)
Até o momento, estivemos recebendo e devolvendo as nossas próprias Entidades do banco de dados diretamente nos *Controllers*. Em aplicações reais, isso é considerado uma má prática. 
O padrão **DTO** resolve isso criando classes simples que servem apenas para transportar dados entre o cliente (quem faz a requisição) e o servidor. 
*   **Por que usar?** Ele protege a estrutura do seu banco de dados, evita vazamento de dados sensíveis e permite que a API receba ou devolva informações formatadas de maneira diferente de como estão salvas nas tabelas.

### Quarkus Bean Validation
Não podemos confiar cegamente nos dados enviados pelo usuário. Precisamos verificar se um nome está em branco, se um preço é negativo ou se um e-mail é válido antes de tentar salvar isso no banco.
A especificação **Bean Validation** (implementada via Hibernate Validator) nos permite fazer isso usando anotações simples diretamente nos nossos DTOs ou Entidades, como `@NotBlank`, `@Min`, ou `@Email`. O Quarkus valida tudo automaticamente antes mesmo do código principal ser executado.

### Quarkus Exception Mapper
Quando uma validação falha (ou quando qualquer outro erro ocorre na aplicação), o comportamento padrão do Java é estourar uma exceção e devolver uma tela de erro assustadora (com o *stack trace*) para o cliente, geralmente com o status `500 Internal Server Error`.
O **Exception Mapper** do Quarkus nos permite interceptar esses erros e transformá-los em respostas HTTP adequadas e amigáveis (como um status `400 Bad Request` com um JSON explicando exatamente qual campo estava preenchido incorretamente).

---

## Parte 2: Prática Integrada (Cliente)

Na nossa etapa prática conduzida em sala, vamos aplicar esses três conceitos no nosso fluxo de **Clientes**:

1.  **Adicionando Dependências:** Vamos incluir a extensão de validação do Quarkus (`quarkus-hibernate-validator`) através do terminal.
2.  **Validando os Dados:** Criaremos regras de negócio estritas para os atributos do Cliente (por exemplo, garantindo que o nome não seja vazio).
3.  **Controle de Exceções:** Vamos programar uma classe dedicada a capturar as falhas de validação (*ConstraintViolationException*) para que a nossa API retorne uma mensagem de erro padronizada e limpa sempre que o cliente enviar um dado inválido.

---

## Parte 3: Desafio Prático dos Alunos

Após construirmos a validação e o tratamento de erros do Cliente juntos, vocês terão a missão de blindar a área de **Produtos**.

O desafio da segunda metade da aula será:
1.  Adicionar as anotações de validação apropriadas para os Produtos (ex: garantir que o preço seja sempre maior que zero e que a descrição não esteja em branco).
2.  Assegurar que o *Controller* de Produtos dispare corretamente o fluxo de controle de exceções que construímos, retornando as mensagens de erro formatadas caso um produto inválido tente ser cadastrado.

Boa leitura e vamos proteger nossa API!
