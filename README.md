### Projeto API Vendas com Spring Framework
* Projeto de uma API de vendas para fins de prática aplicando boas práticas e autenticação/autorização via token JWT;
* Usuário com controle de acesso baseado em roles, com bloqueio de acesso por data de expiração para a credencial e para a conta;
* Exceções do Spring Security sendo capturadas via HandlerExceptionResolver;
* Utilização do messagesource para tradução de mensagens;
* Validação da complexidade das senhas via anotação personalizada;

###   Dependências utilizadas
1. Spring boot 3
2. Spring Web
3. Spring Security 6 - Usei implementações mais recentes no projeto
4. Lombok - Diminuir a verbosidade do código
5. Spring Data JPA
6. PostgreSQL Driver para acesso ao banco de dados no ambiente de produção
7. Database H2 para ambiente de dev
8. Validation - Validações diversas
9. Spring OpenAPI UI - Swagger para documentação da API
10. Spring Context - Fornece contexto de execução da aplicação
11. JJWT - Autenticação e Autorização via JWT
12. ModelMapper - Mapeamento entre Model e DTO


<details open><summary>Neste projeto estou aplicando uma série de boas práticas para garantir a qualidade e a eficiência do código da minha API REST com Spring Framework. Estou aplicando técnicas diferentes para demostrar diversas maneiras de implementar os recursos explorados e algumas permissões foram propositalmente mantidas para facilitar o acesso como a console do h2 no ambiente de dev, mas ciente de que um projeto em produção deve seguir padrões de desenvolvimento definidos no escopo do projeto/equipe.</summary>

**1. Estrutura de Pacotes Organizada:** Mantenho uma estrutura de pacotes bem organizada, dividindo os componentes em controladores, serviços, repositórios e modelos (DTOs/Entidades).

**2. Nomenclatura Convincente:** Utilizo nomenclaturas descritivas e significativas para classes, métodos e variáveis, tornando o código mais compreensível.

**3. DTOs para Comunicação:** Emprego DTOs para representar dados enviados e recebidos pela API, protegendo detalhes da implementação.

**4. Validações de Entrada:** Implemento validações para garantir a integridade dos dados fornecidos pelos clientes da API.

**5. Respostas HTTP Adequadas:** Utilizo os códigos de status HTTP apropriados nas respostas da API, assegurando respostas claras e adequadas.

**6. Tratamento de Exceções:** Implemento um tratamento de exceções personalizado para fornecer respostas apropriadas em caso de erros e evitar a exposição de detalhes técnicos. As exceções do Spring Security também são tratadas via HandlerExceptionResolver.

**7. Paginação e Ordenação:** Implemento recursos de paginação e ordenação para simplificar a navegação pelos resultados da API e um limit de requisições.

**8. Uso Consciente do Banco de Dados:** Evito consultas complexas ou repetitivas ao banco de dados, considerando a implementação de caches para otimizar o desempenho. As credenciais do banco ficam protegidas por meio de variáveis de ambiente e uso bancos de dados diferentes conforme ambiente (prod com PostgreSQL, test ou dev com H2).

**9. Código Limpo e Legível:** Mantenho meu código limpo, sigo as convenções de estilo e asseguro que ele permaneça altamente legível. Adoto uso constante de Optional, conjuntos imutáveis ao usar métodos getters etc.

**10. Princípios SOLID:** Aplico os princípios SOLID (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion) para manter meu código limpo e altamente modular.

**11. Boas Práticas de Segurança:** Apliquei implementações de segurança, incluindo autenticação, autorização e validação de token, para proteger a API contra ameaças. Também apliquei configuração global para o CORS, validação de complexidade de senha e validação se o username já está em uso no banco de dados.

**12. Documentação Clara:** Implementei springdoc-openapi-ui para fornecer uma documentação clara e abrangente da API, descrevendo o uso de cada recurso, parâmetros necessários e formatos de resposta. As requisições por meio do Swagger também exigem autorização via JWT de modo global ou por endpoint.

**13. Atualização de versão Spring Boot, Java e JJWT:** Atualizei a versão de todas as dependências que exigiu diversas alterações como substituir o WebSecurityConfigurerAdapter, migrar do Spring Boot versão 2.7 para o 3.0, do Java 8 para o Java 17 e implementei atualizações referente ao JJWT.

**14. Controle de acesso:** Implementei estratégia de controle de acesso que verifica se a conta do usuário está expirada, bloqueada, ativa ou com senha expirada. Além de usar roles com anotação @RolesAllowed para limitar acesso por endpoint. 

</details>
<br>

<details><summary> Implementações pendentes:</summary>

** Max Retry, Bloqueio de usuario: ** Criar limite de tentativas de autenticação e bloquear usuário apóx X tentativas malsucedidas, registrar no banco as tentativas de autenticação malsucedidas etc.

** Testes Unitários: ** Criar os testes unitários para garantir a funcionalidade correta dos componentes da API, aumentando a confiabilidade do sistema.

** Monitoramento e Logs: ** Integrar registros de logs e métricas para facilitar a identificação e resolução de problemas operacionais.

** Versionamento da API: ** Implementar uma estratégia de versionamento da API para garantir compatibilidade com clientes existentes.
</details>
<br>
<details open><summary> Como executar esse projeto:</summary>

- Para executar o projeto faça o clone do repositório, no diretório onde executar esse comando será criado uma pasta com o nome spring-vendas

  ```bash
  git clone https://github.com/EdsonSFreitas/spring-vendas.git
  ```

- Credenciais do banco: O projeto foi configurado para que o usuário e a senha de acesso ao banco estejam registradas apenas como variáveis de ambiente, portanto, crie as variáveis de ambiente de nome "DB_USER" e "DB_PASS" no Sistema Operacional ou direto na IDE de sua escolha. Por exemplo, usando Linux basta executar esses dois comandos no terminal informando o usuario e senha do seu banco relacional entre aspas duplas como no exemplo abaixo.

    ```bash
    export DB_USER="sa"
    export DB_PASS="senha987"
    ```

- Precisamos também definir um hash base64 para a variável JWT_SECRET_DEV ou JWT_SECRET_PRD (depende de qual ambiente pretende usar). 
- Esse hash pode ser gerado em diversos sites ou direto no shell Linux. Use o comando echo com uma frase qualquer encadeado com o openssl para gerar o hash, repare que existe propositalmente um espaço em branco antes do comando echo, isso é uma boa prática para que o comando não fique gravado no history do shell.

    ```bash
     echo -n 'olá, sejam bem vindo ao meu projeto API Vendas' | openssl base64
    ```

-  Se usou a frase acima o resultado será sempre esse hash:

    ```bash
    b2zDoSwgc2VqYW0gYmVtIHZpbmRvIGFvIG1ldSBwcm9qZXRvIEFQSSBWZW5kYXM=
    ```

- Define o hash como valor para a variável que pretende usar, nesse exemplo usei a JWT_SECRET_DEV

    ```bash
    export JWT_SECRET_DEV="b2zDoSwgc2VqYW0gYmVtIHZpbmRvIGFvIG1ldSBwcm9qZXRvIEFQSSBWZW5kYXM="
    ```

- Compilação: Entre no diretório que contém o arquivo pom.xml e gere o artefato .jar com o comando mvn do maven:

    ```bash
    cd spring-vendas/
    mvn clean package -DskipTests
    ```

- Execução: Ao término do comando será gerado o arquivo vendas-0.9.8-SNAPSHOT.jar dentro da pasta target. Na mesma sessão do shell Linux que usou para definir as variáveis de ambiente execute o comando para entrar no diretório target e execute a aplicação com o comando:

    ```bash
    cd target/
    java -jar vendas-0.9.8-SNAPSHOT.jar
    ```
  
- Também é possível usar o comando:
  ```
  mvn spring-boot:run
  ```

- Com isso você terá acesso ao http://localhost:8080/swagger-ui/index.html para usar todos os endpoints disponíveis.
- No endpoint /api/auth/register você deve criar um usuário com senha contendo caracteres minúsculo, maiúsculo, número e ao menos um caractere especial. 
- Em seguida use o token gerado na etapa anterior para autorizar o acesso via ícone de cadeado com nome “Authorize” disponível no canto direito superior para que o mesmo token seja usado em todos os endpoints.


</details>